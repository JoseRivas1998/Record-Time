package com.tcg.recordtime.utils;

import com.sun.prism.paint.Stop;
import com.tcg.recordtime.components.frames.StopWatchFrame;

/**
 * Created by JoseR on 11/2/2016.
 */
public class StopWatch extends Thread {

    private StopWatchFrame stopWatchFrame;

    public volatile boolean running;

    private volatile boolean loop;

    private volatile long startTime;

    private volatile long time;

    private volatile long pauseTime;

    public StopWatch(StopWatchFrame stopWatchFrame) {
        super();
        loop = true;
        time = 0;
        pauseTime = 0;
        running = false;
        this.stopWatchFrame = stopWatchFrame;
    }

    @Override
    public void run() {
        while(loop) {
            if(running) {
                time = (System.currentTimeMillis() - startTime) + pauseTime;
                stopWatchFrame.setTimeText(StopWatch.formatTime(time));
            }
        }
    }

    public void startStopWatch() {
        try {
            start();
        } catch (Exception e){
        } finally {
            startTime = System.currentTimeMillis();
            running = true;
        }
    }

    public void stopStopWatch() {
        running = false;
        pauseTime = 0;
    }

    public void pauseStopWatch() {
        running = false;
        pauseTime = time;
    }

    public long kill() {
        stopStopWatch();
        loop = false;
        return time;
    }


    public void resetTime() {
        time = 0;
        pauseTime = 0;
        stopWatchFrame.setTimeText(StopWatch.formatTime(time));
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isLooping() {
        return loop;
    }

    public long getTime() {
        return time;
    }

    public static String formatTime(long time) {
        double seconds = ((time / 1000.0) % 60);
        long millis = (long) ((seconds - (long) seconds) * 1000);
        long minutes = ((time / (1000*60)) % 60);
        long hours   = ((time / (1000*60*60)) % 24);
        return String.format("%02d:%02d:%02d.%03d", hours, minutes, (long) seconds, millis);
    }
}
