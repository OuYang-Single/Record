package com.mmt.record.mvp.model.mvp.util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间工具类
 * Created by tifezh on 2016/4/27.
 */
public class DateUtil {
    public static SimpleDateFormat longTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat shortTimeFormat = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy/MM/dd");
    public static String timeStamp2Date(Long auser) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String lastSignTime = dateFormat.format(new Date(auser));


        return lastSignTime;
    }





    public static boolean isNull(String string) {
        boolean isNull = false;
        if (string != null && "".equals(string)) {
            isNull = true;
        }
        return isNull;
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(final String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 电话号码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhone(final String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");     // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }
}
