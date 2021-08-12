package com.zollos.crypto.event;

import com.binance.api.client.domain.event.CandlestickEvent;

import java.time.Instant;

public class Candlestick implements Event<com.binance.api.client.domain.market.Candlestick> {

    private final String key;
    private final boolean durationFinal;
    private final Instant closeTime;
    private final double open;
    private final double high;
    private final double low;
    private final double close;
    private final double volume;

    public Candlestick(CandlestickEvent event) {
        key = event.getSymbol();
        durationFinal = event.getBarFinal();
        closeTime = Instant.ofEpochMilli(event.getCloseTime());
        open = Double.parseDouble(event.getOpen());
        high = Double.parseDouble(event.getHigh());
        low = Double.parseDouble(event.getLow());
        close = Double.parseDouble(event.getClose());
        volume = Double.parseDouble(event.getVolume());
    }

    public Candlestick(com.binance.api.client.domain.market.Candlestick candlestick, String key) {
        this.key = key;
        durationFinal = true;
        closeTime = Instant.ofEpochMilli(candlestick.getCloseTime());
        open = Double.parseDouble(candlestick.getOpen());
        high = Double.parseDouble(candlestick.getHigh());
        low = Double.parseDouble(candlestick.getLow());
        close = Double.parseDouble(candlestick.getClose());
        volume = Double.parseDouble(candlestick.getVolume());
    }

    @Override
    public String getKey() {
        return key;
    }

    public boolean isDurationFinal() {
        return durationFinal;
    }

    public Instant getCloseTime() {
        return closeTime;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public double getVolume() {
        return volume;
    }

    public double getHLC3() {
        return (high + low + close) / 3;
    }
}
