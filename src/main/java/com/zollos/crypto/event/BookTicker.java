package com.zollos.crypto.event;

import com.binance.api.client.domain.event.BookTickerEvent;

public class BookTicker implements Event<com.binance.api.client.domain.market.BookTicker> {

    public BookTicker(BookTickerEvent event) {

    }

    public BookTicker(com.binance.api.client.domain.market.BookTicker bookTicker) {

    }

    @Override
    public String getKey() {
        return null;
    }
}
