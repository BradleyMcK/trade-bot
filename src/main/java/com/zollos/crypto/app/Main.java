package com.zollos.crypto.app;

import com.zollos.crypto.event.Candlestick;
import com.zollos.crypto.event.Event;
import com.zollos.crypto.indicator.Indicator;
import com.zollos.crypto.indicator.VWAP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

public class Main {

    public static void main(String[] args) {

        try {
            ConfigSetup.readConfig();

            System.out.println("Welcome to TradeBot (v0.9.0)\n" +
                    "This is a cryptocurrency trading bot that uses the Binance API,\n" +
                    "and a strategy based on 15 minute chart indicators\n");

            Client client = new Client();
            client.addIndicators(VWAP.class, "ADA", Symbol.USD, Indicator.ONE_MINUTE);
            client.start();

            while(true);    // add command line capability

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(3);
        }
    }
}