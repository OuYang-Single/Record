package com.mmt.record.mvp.model.mvp.util;


import static com.mmt.record.mvp.model.mvp.util.RoutingUtils.REGISTER_PATH;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;


public class CustomURLSpan extends ClickableSpan {
        public static final String URL="http://8.134.38.88:3003";
        public static  String PROVICY;
        public static  String PROVICY_ULR;//条款协议链接
        public static  String TERMS;
        public static  String TERMS_URL;
        private Context mContext;
        private String mUrl;
        private int color;

    public CustomURLSpan(Context context, String url, int color) {
           this.mContext=context;
            mUrl = url;
        this. color = color;
        }
 
        @Override
        public void onClick(View view) {
               if (mUrl.equals("3")){
                   ARouter.getInstance().build(REGISTER_PATH).navigation();
               }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(color);//设置文本颜色
            ds.setUnderlineText(false);//取消下划线
        }
}