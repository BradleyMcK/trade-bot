package com.zollos.crypto.indicator;

import com.zollos.crypto.app.Client;
import com.zollos.crypto.app.Symbol;
import com.zollos.crypto.event.Event;
import com.zollos.crypto.event.EventType;

import java.time.Duration;

public abstract class Indicator<T extends Event> {

    public static final Duration ONE_MINUTE = Duration.ofMinutes(1);
    public static final Duration THREE_MINUTES = Duration.ofMinutes(3);
    public static final Duration FIVE_MINUTES = Duration.ofMinutes(5);
    public static final Duration FIFTEEN_MINUTES = Duration.ofMinutes(15);
    public static final Duration HALF_HOURLY = Duration.ofMinutes(30);
    public static final Duration HOURLY = Duration.ofHours(1);
    public static final Duration TWO_HOURLY = Duration.ofHours(2);
    public static final Duration FOUR_HOURLY = Duration.ofHours(4);
    public static final Duration SIX_HOURLY = Duration.ofHours(6);
    public static final Duration EIGHT_HOURLY = Duration.ofHours(8);
    public static final Duration TWELVE_HOURLY = Duration.ofHours(12);
    public static final Duration DAILY = Duration.ofDays(1);
    public static final Duration THREE_DAILY = Duration.ofDays(3);
    public static final Duration WEEKLY = Duration.ofDays(7);
    public static final Duration MONTHLY = Duration.ofDays(30);

    private final Symbol symbol;
    private final Duration duration;

    public Indicator(Symbol symbol, Duration duration) {
        this.symbol = symbol;
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getBaseAsset() {
        return symbol.getBaseAsset();
    }

    public String getQuoteAsset() {
        return symbol.getQuoteAsset();
    }

    public String getSymbolName() { return symbol.getSymbolName(); }

    // abstract methods

    public abstract EventType getEventType();

    public abstract boolean handleEvent(T event);

    public abstract void initialize(Client client);

}
