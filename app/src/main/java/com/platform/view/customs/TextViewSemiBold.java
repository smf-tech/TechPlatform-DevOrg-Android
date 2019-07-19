package com.platform.view.customs;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class TextViewSemiBold extends AppCompatTextView {

    public TextViewSemiBold(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public TextViewSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public TextViewSemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("opensans_semibold.ttf", context);
        setTypeface(customFont);
    }
}
