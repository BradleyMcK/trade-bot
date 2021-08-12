package com.zollos.crypto.event;

import com.binance.api.client.domain.event.*;

public enum EventType {

    DEPTH(DepthEvent.class),
    TICKER(TickerEvent.class),
    BOOK_TICKER(BookTickerEvent.class),
    AGG_TRADE(AggTradeEvent.class),
    CANDLESTICK(CandlestickEvent.class);

    private final Class<?> eventClass;

    EventType(Class<?> eventClass) {
        this.eventClass = eventClass;
    }

}
