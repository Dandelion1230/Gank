package com.dandelion.gank.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/27.
 */
public class SpannableUtils {
    public static void setSpannableColor(TextView textView, int color, int start, int end) {
        SpannableString spannableString = new SpannableString(textView.getText());
        spannableString.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }
}
