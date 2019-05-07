package com.platform.utility;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.platform.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

    private final Drawable mDrawable;
    private final HashSet<CalendarDay> dates;

    @SuppressWarnings("deprecation")
    public EventDecorator(Context context, Collection<CalendarDay> dates, Drawable drawable) {
        if (drawable == null) {
            mDrawable = context.getResources().getDrawable(R.drawable.circle_background);
        } else {
            mDrawable = drawable;
        }

        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(mDrawable);
    }
}
