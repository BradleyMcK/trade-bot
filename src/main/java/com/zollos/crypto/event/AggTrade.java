package com.zollos.crypto.event;

import com.binance.api.client.domain.event.AggTradeEvent;

public class AggTrade implements Event<com.binance.api.client.domain.market.AggTrade> {

    public AggTrade(AggTradeEvent event) {

    }

    public AggTrade(com.binance.api.client.domain.market.AggTrade aggTrade) {

    }

    @Override
    public String getKey() {
        return null;
    }
}
