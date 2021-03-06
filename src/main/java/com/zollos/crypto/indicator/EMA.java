package com.zollos.crypto.indicator;

import com.zollos.crypto.app.Client;
import com.zollos.crypto.app.Symbol;
import com.zollos.crypto.event.Candlestick;

import java.time.Duration;

/**
 * EXPONENTIAL MOVING AVERAGE
 */
public class EMA extends CandlestickIndicator {

    public EMA(Symbol symbol, Duration duration) {
        super(symbol, duration);
    }

    @Override
    public boolean handleEvent(Candlestick event) {
        return false;
    }

    @Override
    public void initialize(Client client) {

    }

/*
    public EMA(String asset, String symbol, CandlestickInterval interval) {
        super(asset, symbol, interval);
    }

    @Override
    public int getInitializeLength() {
        return 14;
    }

    @Override
    public boolean handleEvent(CandlestickEvent event) {
        // TODO: fill out
        return false;
    }


    private double currentEMA;
    private final int period;
    private final double multiplier;
    private final List<Double> EMAhistory;
    private final boolean historyNeeded;
    private String fileName;

    public EMA(List<Double> closingPrices, int period, boolean historyNeeded) {
        currentEMA = 0;
        this.period = period;
        this.historyNeeded = historyNeeded;
        this.multiplier = 2.0 / (double) (period + 1);
        this.EMAhistory = new ArrayList<>();
        init(closingPrices);
    }

    @Override
    public double get() {
        return currentEMA;
    }

    @Override
    public double getTemp(double newPrice) {
        return (newPrice - currentEMA) * multiplier + currentEMA;
    }

    @Override
    public void init(List<Double> closingPrices) {
        if (period > closingPrices.size()) return;

        //Initial SMA
        for (int i = 0; i < period; i++) {
            currentEMA += closingPrices.get(i);
        }

        currentEMA = currentEMA / (double) period;
        if (historyNeeded) EMAhistory.add(currentEMA);
        //Dont use latest unclosed candle;
        for (int i = period; i < closingPrices.size() - 1; i++) {
            update(closingPrices.get(i));
        }
    }

    @Override
    public void update(double newPrice) {
        // EMA = (Close - EMA(previousBar)) * multiplier + EMA(previousBar)
        currentEMA = (newPrice - currentEMA) * multiplier + currentEMA;

        if (historyNeeded) EMAhistory.add(currentEMA);
    }

    @Override
    public int check(double newPrice) {
        return 0;
    }

    @Override
    public String getExplanation() {
        return null;
    }

    public List<Double> getEMAhistory() {
        return EMAhistory;
    }

    public int getPeriod() {
        return period;
    }
     */
}
