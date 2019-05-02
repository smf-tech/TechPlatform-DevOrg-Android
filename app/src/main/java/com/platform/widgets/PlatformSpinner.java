package com.platform.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.platform.view.adapters.FormSpinnerAdapter;

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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ((FormSpinnerAdapter) getAdapter()).setData(((FormSpinnerAdapter) getAdapter()).getOriginalObjects());
        ((FormSpinnerAdapter) getAdapter()).notifyDataSetChanged();
        super.onTouch(v, event);
        return true;
    }

}
