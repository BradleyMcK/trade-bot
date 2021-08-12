package com.zollos.crypto.indicator;


import com.zollos.crypto.app.Client;
import com.zollos.crypto.app.Symbol;
import com.zollos.crypto.event.Candlestick;
import com.zollos.crypto.event.EventType;

import java.time.Duration;

public abstract class CandlestickIndicator extends Indicator<Candlestick> {

    public CandlestickIndicator(Symbol symbol, Duration duration) {
        super(symbol, duration);
    }

    @Override
    public EventType getEventType() {
        return EventType.CANDLESTICK;
    }

    @Override
    public void initialize(Client client) {
        //Optional<Symbol> xfiat = client.lookupSymbol(getSymbol());

    }

}
