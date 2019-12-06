package com.octopus.view.customs;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.octopus.Platform;
import com.octopus.R;

public class TextViewRegular extends AppCompatTextView {

    public TextViewRegular(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public TextViewRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("opensans_regular.ttf", context);
        this.setTextColor(ContextCompat.getColor(Platform.getInstance(), R.color.dark_grey));
        setTypeface(customFont);
    }
}
