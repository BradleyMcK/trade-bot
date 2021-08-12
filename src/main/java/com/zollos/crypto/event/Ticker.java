package com.zollos.crypto.event;

import com.binance.api.client.domain.event.TickerEvent;

public class Ticker implements Event<TickerEvent> {

    public Ticker(TickerEvent event) {

    }

    @Override
    public String getKey() {
        return null;
    }
}
