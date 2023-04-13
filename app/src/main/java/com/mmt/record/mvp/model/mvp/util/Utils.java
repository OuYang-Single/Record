package com.mmt.record.mvp.model.mvp.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.mmt.record.R;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Utils {
 
    // 状态栏高度
    private static  int statusBarHeight = 0;
    // 屏幕像素点
    private static final Point screenSize = new Point();
 
    // 获取屏幕像素点
    public static Point getScreenSize(Activity context) {
 
        if (context == null) {
            return screenSize;
        }
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            Display diplay = wm.getDefaultDisplay();
            if (diplay != null) {
                diplay.getMetrics(mDisplayMetrics);
                int W = mDisplayMetrics.widthPixels;
                int H = mDisplayMetrics.heightPixels;
                if (W * H > 0 && (W > screenSize.x || H > screenSize.y)) {
                    screenSize.set(W, H);
                }
            }
        }
        return screenSize;
    }
 
    // 获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight <= 0) {
            Rect frame = new Rect();
            ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }
        if (statusBarHeight <= 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(x);
 
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) throws PatternSyntaxException {
//        String regExp = "^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-8])|(18[0-9])|(19[8-9])|(147,145))\\d{8}$";
        String regExp = "^(13|14|15|16|17|18|19)[0-9]{9}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 显示一条自然通知
     */
    public static void showNotification(@NonNull String title, @NonNull String content, Context  context) {

       /* NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// 定义Notification的各种属性
        Notification.Builder mBuilder = new Notification.Builder(context)
                .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)        //
                .setSmallIcon(R.mipmap.ic_launcher)                                         //设置通知的图标
                .setTicker("有新消息呢")                                                     //设置状态栏的标题
                .setContentTitle(title)                                               //设置标题
                .setContentText(content)                                                //消息内容
                                                 //设置默认的提示音
                 .setDefaults(Notification.DEFAULT_LIGHTS)
                .setPriority(Notification.PRIORITY_DEFAULT)                                 //设置该通知的优先级
                .setOngoing(false)                                                          //让通知左右滑的时候不能取消通知
                .setPriority(Notification.PRIORITY_DEFAULT)                                 //设置该通知的优先级
                                                   //设置通知时间，默认为系统发出通知的时间，通常不用设置
                .setAutoCancel(true);        */                                               //打开程序后图标消失
//处理点击Notification的逻辑
     /*   Intent resultIntent = new Intent(this, TestActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);           //添加为栈顶Activity
        resultIntent.putExtra("what",5);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,5,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);*/
//发送
    //    mNotificationManager.notify(1, mBuilder.build());
//结束广播
/*mNotificationManager.cancel(1);*/

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    /*    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);*/

        String channelId = createNotificationChannel("my_channel_ID", "my_channel_NAME", NotificationManager.IMPORTANCE_HIGH,context);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, channelId)
                .setTicker("有新消息呢")                                                     //设置状态栏的标题
                .setContentTitle(title)                                               //设置标题
                .setContentText(content)                                                //消息内容
                //设置默认的提示音
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS)//设置使用系统默认的声音、默认震动
                .setPriority(Notification.PRIORITY_DEFAULT)                                 //设置该通知的优先级
                .setOngoing(false)                                                          //让通知左右滑的时候不能取消通知
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis());


        notificationManager.notify(16657, notification.build());

    }
    private static String createNotificationChannel(String channelID, String channelNAME, int level, Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(channelID, channelNAME, level);
            manager.createNotificationChannel(channel);
            return channelID;
        } else {
            return null;
        }
    }


}
 