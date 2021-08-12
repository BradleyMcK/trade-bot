package com.zollos.crypto.event;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public interface Event<T> {

    public static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());

    String getKey();

    default boolean isDurationFinal() {
        return true;
    }

    default String formatTime(Instant instant) {
        return DF.format(instant);
    }
}
