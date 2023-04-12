package com.mmt.record.mvp.model.mvp.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public  static void makeText(Context mContext,String text){
        Toast toast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.show();
    }
    public  static void makeTexts(Context mContext,String text){
        Toast toast = Toast.makeText(mContext, "", Toast.LENGTH_LONG);
        toast.setText(text);
        toast.show();
    }
}
