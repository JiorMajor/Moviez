package com.chanpyaeaung.moviez.view;

import android.graphics.Color;
import android.os.Parcel;
import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;

/**
 * Created by Chan Pyae Aung on 5/3/17.
 */

public class AlphaColorSpan extends ForegroundColorSpan {

    private float alpha;

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getAlpha() {
        return alpha;
    }

    public AlphaColorSpan(@ColorInt int color) {
        super(color);
    }

    public AlphaColorSpan(Parcel src) {
        super(src);
        alpha = src.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeFloat(alpha);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(getAlphaColor());
    }

    private int getAlphaColor() {
        int foregroundColor = getForegroundColor();
        return Color.argb((int) (alpha * 255), Color.red(foregroundColor), Color.green(foregroundColor), Color.blue(foregroundColor));
    }
}

