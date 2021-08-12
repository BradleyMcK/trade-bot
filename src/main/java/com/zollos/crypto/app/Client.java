package com.zollos.crypto.app;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.general.SymbolInfo;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.zollos.crypto.event.*;
import com.zollos.crypto.indicator.Indicator;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Client {

    public static final Map<Long, CandlestickInterval> candlestickIntervalMap = createCandlestickIntervalMap();

    private final BinanceApiWebSocketClient webClient;
    private final BinanceApiRestClient restClient;
    private final ExchangeInfo exchangeInfo;

    private final Map<String, List<Indicator>> nameIndicatorMap = new HashMap<>();
    private final Map<EventType, List<Indicator>> typeIndicatorMap = new HashMap<>();
    private final List<Indicator> indicatorList = new ArrayList<>();
    private final Instant initialServerTime;

    public Client() {

        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
        webClient = factory.newWebSocketClient();
        restClient = factory.newRestClient();

        exchangeInfo = restClient.getExchangeInfo();
        initialServerTime = Instant.ofEpochMilli(exchangeInfo.getServerTime());
    }

    public <T extends Event> void addIndicator(Indicator<T> indicator) {

        indicatorList.add(indicator);
    }

    public void start() {

        // initialize all indicators and create indicator maps
        indicatorList.forEach(indicator -> {
            indicator.initialize(this);

            List<Indicator> eventList = typeIndicatorMap.getOrDefault(indicator.getEventType(), new ArrayList<>());
            typeIndicatorMap.putIfAbsent(indicator.getEventType(), eventList);
            eventList.add(indicator);

            List<Indicator> nameList = nameIndicatorMap.getOrDefault(indicator.getSymbolName(), new ArrayList<>());
            nameIndicatorMap.putIfAbsent(indicator.getSymbolName(), nameList);
            nameList.add(indicator);

        });

        // setup listener handlers
        for (Map.Entry<EventType, List<Indicator>> entry : typeIndicatorMap.entrySet()) {
            if (entry.getKey() == EventType.CANDLESTICK) {
                initializeCandlestickIndicators(entry.getValue());
            } else {
                initializeIndicators(entry.getKey(), entry.getValue());
            }
        }
    }

    private void initializeCandlestickIndicators(List<Indicator> candlestickIndicatorList) {

        for (Indicator indicator : candlestickIndicatorList) {
            String symbolName = indicator.getSymbolName().toLowerCase();
            CandlestickInterval interval = candlestickIntervalMap.get(indicator.getDuration().getSeconds());
            webClient.onCandlestickEvent(symbolName, interval, event -> handleEvent(new Candlestick(event)));
        }
    }

    private void initializeIndicators(EventType eventType, List<Indicator> symbolIndicatorList) {

        List<String> names = symbolIndicatorList.stream().map(Indicator::getSymbolName).collect(Collectors.toList());
        if (!names.isEmpty()) {
            String csvNames = String.join(",", names);
            switch (eventType) {
                case DEPTH:
                    webClient.onDepthEvent(csvNames, event -> handleEvent(new Depth(event)));
                    break;
                case TICKER:
                    webClient.onTickerEvent(csvNames, event -> handleEvent(new Ticker(event)));
                    break;
                case BOOK_TICKER:
                    webClient.onBookTickerEvent(csvNames, event -> handleEvent(new BookTicker(event)));
                    break;
                case AGG_TRADE:
                    webClient.onAggTradeEvent(csvNames, event -> handleEvent(new AggTrade(event)));
                    break;
            }
        }
    }

    private void handleEvent(Event event) {
        try {
            if (nameIndicatorMap.containsKey(event.getKey())) {
                List<Indicator> matchingIndicators = nameIndicatorMap.get(event.getKey());
                matchingIndicators.forEach(indicator -> indicator.handleEvent(event));
                if (!matchingIndicators.isEmpty() && event.isDurationFinal()) {
                    System.out.println(matchingIndicators.get(0).toString());
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public List<Candlestick> getCandlesticks(String symbol, Duration duration, Period period) {

        long end = period.getMilliEnd();
        long start = period.getMilliStart();
        CandlestickInterval interval = candlestickIntervalMap.get(duration.getSeconds());

        return restClient.getCandlestickBars(symbol, interval, 500, start, end)
                .stream()
                .map(candlestick -> new Candlestick(candlestick, symbol))
                .collect(Collectors.toList());
    }

    public Period calculateInitializationPeriod(Duration initializeDuration) {

        Instant start = initialServerTime.minus(initializeDuration);
        return new Period(start, initialServerTime);
    }

    public String[] getSymbolArray(String asset, String[] fiat) {

        List<String> fiatList = Arrays.asList(fiat);
        return exchangeInfo.getSymbols().stream()
                .filter(symbolInfo -> symbolInfo.getBaseAsset().equalsIgnoreCase(asset))
                .filter(symbolInfo -> fiatList.contains(symbolInfo.getQuoteAsset()))
                .map(SymbolInfo::getSymbol)
                .collect(Collectors.toList())
                .toArray(new String[0]);
    }

    public Optional<Symbol> lookupSymbol(String symbol) {
        return exchangeInfo.getSymbols()
                .stream()
                .filter(symbolInfo -> symbolInfo.getSymbol().equalsIgnoreCase(symbol))
                .map(Symbol::new)
                .findFirst();
    }

    public List<Symbol> getMatchingSymbols(String baseAsset, List<String> quoteAssets) {

        return exchangeInfo.getSymbols()
                .stream()
                .map(Symbol::new)
                .filter(symbol -> symbol.getBaseAsset().equalsIgnoreCase(baseAsset))
                .filter(symbol -> quoteAssets.contains(symbol.getQuoteAsset().toUpperCase()))
                .collect(Collectors.toList());
    }

    public void addIndicators(Class<? extends Indicator> clazz, String buyAsset, String[] sellAssets, Duration span) {

        List<Symbol> list = getMatchingSymbols(buyAsset, Arrays.asList(sellAssets));

        for (Symbol symbol : getMatchingSymbols(buyAsset, Arrays.asList(sellAssets))) {
            try {
                Indicator indicator = ConstructorUtils.invokeExactConstructor(clazz, symbol, span);
                addIndicator(indicator);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
                // LOG this
            }
        }
    }

    public void printIndicators() {

        for (Indicator ci : indicatorList) {
            System.out.println(ci.toString());
        }
    }

    // static methods

    private static Map<Long, CandlestickInterval> createCandlestickIntervalMap() {

        Map<Long, CandlestickInterval> map = new HashMap<>();
        for (CandlestickInterval ci : CandlestickInterval.values()) {
            Duration duration = toDuration(ci.getIntervalId());
            map.put(duration.getSeconds(), ci);
        }
        return map;
    }

    private static Duration toDuration(String intervalId) {

        int length = intervalId.length();
        char units = intervalId.charAt(length-1);
        int quantity = Integer.parseInt(intervalId.substring(0, length-1));

        if (units == 'm') {
            return Duration.ofMinutes(quantity);
        } else if (units == 'h') {
            return Duration.ofHours(quantity);
        } else if (units == 'd') {
            return Duration.ofDays(quantity);
        } else if (units == 'w') {
            return Duration.ofDays(quantity * 7);
        } else if (units == 'M') {
            return Duration.ofDays(quantity * 30);
        } else {
            throw new IllegalArgumentException("Cannot calculate duration of " + intervalId);
        }
    }

}
