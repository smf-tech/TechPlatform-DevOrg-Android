package com.octopusbjsindia.utility

import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.octopusbjsindia.R
import java.util.Calendar

const val MIN_YEAR = 1950

class MonthYearPickerDialog : DialogFragment() {

    private lateinit var listener: OnDateSetListener
    private var year: Int? = null
    private var month: Int? = null
    private var day: Int? = null

    fun setListener(listener: OnDateSetListener) {
        this.listener = listener
    }

    fun setDefaultDate(year: Int, month: Int, day: Int) {
        this.year = year
        this.month = month
        this.day = day
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = MaterialAlertDialogBuilder(requireActivity(),/* R.style.ThemeOverlay_Catalog_MaterialAlertDialog*/)
        val inflater = activity?.layoutInflater
        val cal = Calendar.getInstance()

        val dialog: View = inflater?.inflate(R.layout.date_picker_dialog, null)!!
        val dayPicker: NumberPicker = dialog.findViewById(R.id.picker_day)
        val monthPicker: NumberPicker = dialog.findViewById(R.id.picker_month)
        val yearPicker: NumberPicker = dialog.findViewById(R.id.picker_year)

        val selectedYear :Int= year ?: cal.get(Calendar.YEAR)
        val selectedMonth :Int= month ?: cal.get(Calendar.MONTH)
        val selectedDay :Int= day ?: cal.get(Calendar.DAY_OF_MONTH)

        monthPicker.apply {
            minValue = 0
            maxValue = 11
            value = selectedMonth
        }
        val monthList = listOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "June",
            "July",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        )
        monthPicker.displayedValues = monthList.toTypedArray()

        yearPicker.apply {
            minValue = MIN_YEAR
            maxValue = cal.get(Calendar.YEAR)
            value = selectedYear
        }

        dayPicker.apply {
            minValue = 1
            maxValue = getMaxMonthDays(monthPicker.value, yearPicker.value)
            value = selectedDay
        }


        yearPicker.setOnValueChangedListener { numberPicker, oldValue, newValue ->
            dayPicker.apply {
                maxValue = getMaxMonthDays(monthPicker.value, newValue)
            }
        }

        monthPicker.setOnValueChangedListener { numberPicker, oldValue, newValue ->
            dayPicker.apply {
                maxValue = getMaxMonthDays(newValue, yearPicker.value)
                value = if (dayPicker.value > maxValue) {
                    maxValue
                } else dayPicker.value
            }
        }

        builder.setView(dialog)
            .setTitle("Select Date")
            .setIcon(R.drawable.ic_calendar_today_24)
            .setPositiveButton("Ok") { _, _ ->
                listener.onDateSet(null, yearPicker.value, monthPicker.value, dayPicker.value)
            }
            .setNegativeButton("Cancel") { dialog1, _ ->
                dialog1.dismiss();
            }
        return builder.create()
    }

    private fun getMaxMonthDays(month: Int, year: Int): Int {
        return when (month) {
            0 -> 31 //jan
            1 -> if (year.isLeapYear()) 29 else 28 //feb
            2 -> 31//march
            3 -> 30 //april
            4 -> 31 //may
            5 -> 30 //june
            6 -> 31 //july
            7 -> 31 //aug
            8 -> 30 //sept
            9 -> 31 //oct
            10 -> 30 //nov
            11 -> 31 //dec
            else -> 31
        }
    }
}

fun Int.isLeapYear(): Boolean {
    return (this % 4 == 0 && this % 100 != 0) || (this % 400 == 0)
}