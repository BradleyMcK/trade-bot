package com.zollos.crypto.event;

import java.time.Instant;

public class Period {

    private final Instant start;
    private final Instant end;

    public Period(Instant start, Instant end) {
        this.start = start;
        this.end = end;
    }

    public Period(long start, long end) {
        this.start = Instant.ofEpochMilli(start);
        this.end = Instant.ofEpochMilli(end);
    }

    public long getMilliEnd() {
        return end.toEpochMilli();
    }

    public long getMilliStart() {
        return start.toEpochMilli();
    }

}
