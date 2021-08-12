package com.zollos.crypto.app;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.general.SymbolInfo;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.zollos.crypto.indicator.Indicator;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OldClient {

    /*
    private final BinanceApiWebSocketClient webClient;
    private final BinanceApiRestClient restClient;
    private final ExchangeInfo exchangeInfo;

    private final Map<String, CandlestickIndicator> candlestickIndicatorMap = new HashMap<>();
    private final Long initialServerTime;

    private String fiatCurrency;
    private int eventCount = 0;

    public Client() {

        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
        webClient = factory.newWebSocketClient();
        restClient = factory.newRestClient();

        fiatCurrency = "USD";
        exchangeInfo = restClient.getExchangeInfo();
        initialServerTime = restClient.getServerTime();
    }

    public List<SymbolInfo> getMatchingSymbols(String asset, CandlestickInterval interval) {

        return exchangeInfo.getSymbols().stream()
                .filter(si -> asset.equalsIgnoreCase(si.getBaseAsset()))
                .filter(si -> si.getQuoteAsset().toUpperCase().contains(fiatCurrency.toUpperCase()))
                .collect(Collectors.toList());
    }

    public void addIndicator(Class<? extends CandlestickIndicator> clazz, String asset, CandlestickInterval interval) {

        for (SymbolInfo si : getMatchingSymbols(asset, interval)) {
            if (candlestickIndicatorMap.containsKey(si.getSymbol().toUpperCase())) {
                throw new IllegalArgumentException("Duplicate Indicator added");
            } else {
                CandlestickIndicator ci = Indicator.create(clazz, si, interval);
                candlestickIndicatorMap.put(ci.getSymbol(), ci);
            }
        }
    }

    public void start() {

        // initialize all indicators
        for (CandlestickIndicator indicator : candlestickIndicatorMap.values()) {
            indicator.processInputList(getCandlesticks(indicator));
        }

        // setup listener handlers
        for (CandlestickIndicator indicator : candlestickIndicatorMap.values()) {
            webClient.onCandlestickEvent()
            webClient.onCandlestickEvent(indicator.getSymbol().toLowerCase(),
                    indicator.getInterval(),
                    response -> handleCandlestickEvent(response));
        }
    }

    private void handleCandlestickEvent(CandlestickEvent response) {

        try {
            CandlestickIndicator ci = candlestickIndicatorMap.get(response.getSymbol());
            if (ci != null && ci.handleEvent(response)) {
                eventCount++;
                if (eventCount == candlestickIndicatorMap.size()) {
                    eventCount = 0;
                    printIndicators();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void printIndicators() {

        for (CandlestickIndicator ci : candlestickIndicatorMap.values()) {
            System.out.println(ci.toString());
        }
    }

    private List<Candlestick> getCandlesticks(CandlestickIndicator ci) {

        Duration duration = Indicator.toDuration(ci.getIntervalId()).multipliedBy(ci.getInitializeLength());
        long start = Indicator.toInstant(initialServerTime).minus(duration).toEpochMilli();
        long end = Indicator.toInstant(initialServerTime).toEpochMilli();
        return restClient.getCandlestickBars(ci.getSymbol().toUpperCase(), ci.getInterval(), 500, start, end);
    }
    */
}
