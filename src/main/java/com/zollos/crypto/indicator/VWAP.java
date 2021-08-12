package com.zollos.crypto.indicator;

import com.zollos.crypto.app.Client;
import com.zollos.crypto.app.Symbol;
import com.zollos.crypto.event.Candlestick;
import com.zollos.crypto.event.Event;
import com.zollos.crypto.event.Period;

import java.time.Duration;
import java.time.Instant;

public class VWAP extends CandlestickIndicator {

    public static final String FORMAT = "[ name: %s, time: %s, close: %,.4f, vwap: %,.4f ]";
    public static final long SECONDS_PER_DAY = Duration.ofDays(1).getSeconds();
    public static final long SECONDS_PER_WEEK = Duration.ofDays(7).getSeconds();
    public static final long SECONDS_PER_MONTH = Duration.ofDays(30).getSeconds(); // approximate but fit for use case
    public static final long SECONDS_PER_YEAR = Duration.ofDays(365).getSeconds(); // approximate but fit for use case

    public static final int DEFAULT_PERIOD_COUNT = 14;

    private final VwapData[] vwapData;
    private final int vwapDataLength;

    private VwapData current = null;
    private int currentIndex = 0;

    public VWAP(Symbol symbol, Duration duration) {
        this(symbol, duration, DEFAULT_PERIOD_COUNT);
    }

    public VWAP(Symbol symbol, Duration duration, int periodCount) {
        super(symbol, duration);
        vwapDataLength = calculateVwapDataLength(periodCount, duration.getSeconds());
        vwapData = new VwapData[vwapDataLength];
    }

    private int calculateVwapDataLength(int periodCount, long periodSeconds) {

        long sampleSeconds = periodSeconds * periodCount * 10;
        if (sampleSeconds < SECONDS_PER_DAY) {
            return (int) (SECONDS_PER_DAY / periodSeconds);
        } else if (sampleSeconds < SECONDS_PER_WEEK) {
            return (int) (SECONDS_PER_WEEK / periodSeconds);
        } else if (sampleSeconds < SECONDS_PER_MONTH) {
            return (int) (SECONDS_PER_MONTH / periodSeconds);
        } else if (sampleSeconds < SECONDS_PER_YEAR) {
            return (int) (SECONDS_PER_YEAR / periodSeconds);
        } else {
            String msg = String.format("Sample size too large [period: %ds, count: %ds]", periodSeconds, periodCount);
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public void initialize(Client client) {

        long SECONDS_IN_DAY = 60 * 60 * 24;
        long DAYS_IN_LEAP_YEAR = 366;
        long DAYS_IN_COMMON_YEAR = 365;
        long DAYS_IN_4_YEARS = (DAYS_IN_COMMON_YEAR * 3) + DAYS_IN_LEAP_YEAR;
        long DAYS_IN_100_YEARS = (DAYS_IN_4_YEARS * 25) - 1;
        long DAYS_IN_400_YEARS = (DAYS_IN_100_YEARS * 4) + 1;
        long SECONDS_IN_YEAR = (DAYS_IN_400_YEARS * SECONDS_IN_DAY) / 400;

        long time = 0;
        for (int year = 0; year < 4000; year++) {
            String msg = "";
            int daysInYear = ((year % 4) == 0) ? 366 : 365;
            if ((year % 100) == 0) {
                if ((year % 400) > 0) {
                    daysInYear--;
                    msg = "100 year mark";
                } else {
                    msg = "400 year mark";
                }
            }
            for (int day = 0; day < daysInYear; day++) {
                if (day == (daysInYear-1)) {
                    long calcYear = time / SECONDS_IN_YEAR;
                    if (calcYear != year) msg = "year is off";
                    System.out.print(String.format("%3d - %d / %d  ", day+1, year, calcYear));
                    System.out.println(msg);
                    if (!msg.isEmpty()) {
                        System.out.print("");
                    }
                }
                time += SECONDS_IN_DAY;
            }
        }

        /*
        long day = 0;
        long prevYear = 0;
        for (long time = 0; time < (SECONDS_IN_YEAR * 4000); time += SECONDS_IN_DAY) {

            day++;
            long year = time / SECONDS_IN_YEAR;

            if (day == 1 || day > 363) {
                System.out.println(String.format("%3d - %4d", day, year));
            }

            if (year != prevYear) day = 0;
            prevYear = year;
        }
        */

        super.initialize(client);

        for (int index = 0; index < vwapDataLength; index++) {
            vwapData[index] = new VwapData();
            vwapData[index].time = Long.MAX_VALUE;
        }

        Duration initializeDuration = getDuration().multipliedBy(14);
        Period period = client.calculateInitializationPeriod(initializeDuration);
        client.getCandlesticks(getSymbolName(), getDuration(), period)
                .forEach(candlestick -> handleEvent(candlestick));
    }

    private void consolidateVwapData() {
        long prevTime = vwapData[0].time;
        int sameTimeCount = 0;
        int writeIndex = 0;

        double volumeSum = 0.0;
        double closeSum = 0.0;
        double pvSum = 0.0;

        for (int index = 0; index < vwapDataLength; index++) {
            VwapData data = vwapData[index];
            if (data != null) {
                if (data.time != prevTime) {
                    // write the cosolidated data
                    VwapData newData = vwapData[writeIndex++];
                    newData.volume = volumeSum / sameTimeCount;
                    newData.close = closeSum / sameTimeCount;
                    newData.pv = pvSum / sameTimeCount;
                    newData.time = prevTime;
                    // reset sums and counts and prevTime
                    prevTime = Long.MAX_VALUE;
                    sameTimeCount = 0;
                    volumeSum = 0.0;
                    closeSum = 0.0;
                    pvSum = 0.0;
                }
                if (data.time != Long.MAX_VALUE && data.volume > 0.0) {
                    volumeSum += data.volume;
                    closeSum += data.close;
                    pvSum += data.pv;
                    prevTime = data.time;
                    sameTimeCount++;
                }
            }
        }

        for (int index = writeIndex; index < vwapDataLength; index++) {
            vwapData[index].time = Long.MAX_VALUE;
            vwapData[index].volume = 0.0;
        }
    }

    @Override
    synchronized public boolean handleEvent(Candlestick event) {
        if (event.isDurationFinal()) {
            current = vwapData[currentIndex++];
            if (currentIndex >= vwapDataLength) currentIndex = 0;
            current.time = event.getCloseTime().toEpochMilli();
            current.volume = event.getVolume();
            current.pv = event.getHLC3() * current.volume;
            current.close = event.getClose();
            return true;
        } else {
            return false;
        }
    }

    synchronized double getVWAP() {

        double pvSum = 0.0;
        double volumeSum = 0.0;
        for (VwapData data : vwapData) {
            if (data != null && data.volume > 0.0) {
                volumeSum += data.volume;
                pvSum += data.pv;
            }
        }

        return pvSum / volumeSum;
    }

    public String toString() {

        if (current != null) {
            String clock = Event.DF.format(Instant.ofEpochMilli(current.time));
            return String.format(FORMAT, getSymbolName(), clock, current.close, getVWAP());
        } else {
            return "";
        }
    }

    public class VwapData {
        double volume;
        double close;
        double pv;
        long time;
    }
}
