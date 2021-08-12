package com.zollos.crypto.indicator;

import com.zollos.crypto.app.Client;
import com.zollos.crypto.app.Symbol;
import com.zollos.crypto.event.Candlestick;

import java.time.Duration;

public class RSI extends CandlestickIndicator {

    public RSI(Symbol symbol, Duration duration) {
        super(symbol, duration);
    }

    @Override
    public boolean handleEvent(Candlestick event) {
        // TODO: fill out
        return false;
    }

    @Override
    public void initialize(Client client) {

    }

    /*
    public RSI(String asset, String symbol, CandlestickInterval interval) {
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


    private double avgUp;
    private double avgDwn;
    private double prevClose;
    private final int period;
    private String explanation;
    public static int POSITIVE_MIN;
    public static int POSITIVE_MAX;
    public static int NEGATIVE_MIN;
    public static int NEGATIVE_MAX;

    public RSI(List<Double> closingPrice, int period) {
        avgUp = 0;
        avgDwn = 0;
        this.period = period;
        explanation = "";
        init(closingPrice);
    }

    @Override
    public void init(List<Double> closingPrices) {
        prevClose = closingPrices.get(0);
        for (int i = 1; i < period + 1; i++) {
            double change = closingPrices.get(i) - prevClose;
            if (change > 0) {
                avgUp += change;
            } else {
                avgDwn += Math.abs(change);
            }
        }

        //Initial SMA values
        avgUp = avgUp / (double) period;
        avgDwn = avgDwn / (double) period;

        //Dont use latest unclosed value
        for (int i = period + 1; i < closingPrices.size() - 1; i++) {
            update(closingPrices.get(i));
        }
    }

    @Override
    public double get() {
        return 100 - 100.0 / (1 + avgUp / avgDwn);
    }

    @Override
    public double getTemp(double newPrice) {
        double change = newPrice - prevClose;
        double tempUp;
        double tempDwn;
        if (change > 0) {
            tempUp = (avgUp * (period - 1) + change) / (double) period;
            tempDwn = (avgDwn * (period - 1)) / (double) period;
        } else {
            tempDwn = (avgDwn * (period - 1) + Math.abs(change)) / (double) period;
            tempUp = (avgUp * (period - 1)) / (double) period;
        }
        return 100 - 100.0 / (1 + tempUp / tempDwn);
    }

    @Override
    public void update(double newPrice) {
        double change = newPrice - prevClose;
        if (change > 0) {
            avgUp = (avgUp * (period - 1) + change) / (double) period;
            avgDwn = (avgDwn * (period - 1)) / (double) period;
        } else {
            avgUp = (avgUp * (period - 1)) / (double) period;
            avgDwn = (avgDwn * (period - 1) + Math.abs(change)) / (double) period;
        }
        prevClose = newPrice;
    }

    @Override
    public int check(double newPrice) {
        double temp = getTemp(newPrice);
        if (temp < POSITIVE_MIN) {
            explanation = "RSI of " + Formatter.formatDecimal(temp);
            return 2;
        }
        if (temp < POSITIVE_MAX) {
            explanation = "RSI of " + Formatter.formatDecimal(temp);
            return 1;
        }
        if (temp > NEGATIVE_MIN) {
            explanation = "RSI of " + Formatter.formatDecimal(temp);
            return -1;
        }
        if (temp > NEGATIVE_MAX) {
            explanation = "RSI of " + Formatter.formatDecimal(temp);
            return -2;
        }
        explanation = "";
        return 0;
    }

    @Override
    public String getExplanation() {
        return explanation;
    }

     */
}
