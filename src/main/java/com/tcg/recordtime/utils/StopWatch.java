package com.tcg.recordtime.utils;

/**
 * Created by JoseR on 11/2/2016.
 */
public class StopWatch extends Thread {

    public static String formatTime(long time) {
        double seconds = ((time / 1000.0) % 60);
        long millis = (long) ((seconds - (long) seconds) * 1000);
        long minutes = ((time / (1000*60)) % 60);
        long hours   = ((time / (1000*60*60)) % 24);
        return String.format("%02d:%02d:%02d.%03d", hours, minutes, (long) seconds, millis);
    }
}
