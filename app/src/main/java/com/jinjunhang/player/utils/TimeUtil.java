package com.jinjunhang.player.utils;

/**
 * Created by lzn on 16/6/19.
 */
public class TimeUtil {
    public static String secondsToString(long seconds) {
            long secs = seconds % 60;
            long minutes = (seconds / 60) % 60;
            long hours = (seconds / 3600);
            if ( hours > 0) {
                return String.format("%02d:%02d:%02d", hours, minutes, secs);
            } else {
                return String.format("%02d:%02d", minutes, secs);
            }



    }
}
