package com.platform.widgets;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSpinner;

public class PlatformSpinner extends AppCompatSpinner {
    public PlatformSpinner(Context context) {
        super(context);
    }

    public PlatformSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlatformSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void
    setSelection(int position, boolean animate) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position, animate);
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            if (getOnItemSelectedListener() != null) {
                getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
            }
        }
    }

    @Override
    public void
    setSelection(int position) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position);
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            if (getOnItemSelectedListener() != null) {
                getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
            }
        }
    }

}
