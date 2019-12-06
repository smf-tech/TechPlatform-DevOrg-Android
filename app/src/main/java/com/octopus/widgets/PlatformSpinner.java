package com.octopus.widgets;

import android.content.Context;
import android.util.AttributeSet;

public class PlatformSpinner extends SearchableSpinner {
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
        super.setSelection(position, animate);
        // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
        if (getOnItemSelectedListener() != null) {
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }

    @Override
    public void
    setSelection(int position) {
        super.setSelection(position);
        if (getOnItemSelectedListener() != null) {
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }

}
