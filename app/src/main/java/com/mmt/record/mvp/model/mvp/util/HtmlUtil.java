package com.mmt.record.mvp.model.mvp.util;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.widget.TextView;


public class HtmlUtil {

    public static void handleHtmlClickAndStyle(Context context, TextView textview,int color) {

        textview.setMovementMethod(LinkMovementMethod.getInstance());//需要处理点击得加这句
        CharSequence text = textview.getText();
        if (text instanceof Spannable) {
            Spannable sp = (Spannable) text;
            URLSpan[] oldUrlSpans = sp.getSpans(0, text.length(), URLSpan.class);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
            for (URLSpan oldUrlSpan: oldUrlSpans) {
                //span 不能重复设置，需要先删除
                spannableStringBuilder.removeSpan(oldUrlSpan);
                CustomURLSpan customURLSpan= new CustomURLSpan(context,oldUrlSpan.getURL(),color);
                spannableStringBuilder.setSpan(customURLSpan, sp.getSpanStart(oldUrlSpan),
                        sp.getSpanEnd(oldUrlSpan), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            textview.setText(spannableStringBuilder);
        }
    }
}
