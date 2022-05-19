//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mmt.record.mvp.model.mvp.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CountDownTimeTextUtil {
    public CountDownTimeTextUtil() {
    }

    public static String getTimerString(long time) {
        int second;
        int minute;
        int hour;
        if (time > 86400000L) {
            int day = (int)(time / 1000L / 60L / 60L / 24L);
            hour = (int)((time - (long)(day * 1000 * 60 * 60 * 24)) / 1000L / 60L / 60L);
            minute = (int)((time - (long)(day * 1000 * 60 * 60 * 24) - (long)(hour * 1000 * 60 * 60)) / 1000L / 60L);
            second = (int)(time - (long)(day * 1000 * 60 * 60 * 24) - (long)(hour * 1000 * 60 * 60) - (long)(minute * 1000 * 60)) / 1000;
            return day + "D " + hour + ":" + minute + ":" + second;
        } else if (time > 3600000L) {
            hour = (int)(time / 1000L / 60L / 60L);
            minute = (int)((time - (long)(hour * 1000 * 60 * 60)) / 1000L / 60L);
            second = (int)(time - (long)(hour * 1000 * 60 * 60) - (long)(minute * 1000 * 60)) / 1000;
            String hourStr;
            if (hour < 10) {
                hourStr = "0" + hour;
            } else {
                hourStr = "" + hour;
            }

            if (minute < 10) {
                return second < 10 ? hourStr + ":0" + minute + ":0" + second + "" : hourStr + ":0" + minute + ":" + second + "";
            } else {
                return second < 10 ? hourStr + ":" + minute + ":0" + second + "" : hourStr + ":" + minute + ":" + second + "";
            }
        } else if (time > 60000L) {
            minute = (int)time / 1000 / 60;
            second = (int)(time - (long)(minute * 1000 * 60)) / 1000;
            if (minute < 10) {
                return second < 10 ? "00:0" + minute + ":0" + second + "" : "00:0" + minute + ":" + second + "";
            } else {
                return second < 10 ? "00:" + minute + ":0" + second + "" : "00:" + minute + ":" + second + "";
            }
        } else {
            second = (int)time / 1000;
            return second < 10 ? "00:00:0" + second + "" : "00:00:" + second + "";
        }
    }

    public static String nowTime() {
        long time = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date d1 = new Date(time);
        return format.format(d1);
    }
}
