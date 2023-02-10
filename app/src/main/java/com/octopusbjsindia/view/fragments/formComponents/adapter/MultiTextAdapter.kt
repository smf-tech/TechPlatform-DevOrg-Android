package com.octopusbjsindia.view.fragments.formComponents.adapter

import android.text.InputFilter
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.octopusbjsindia.R
import com.octopusbjsindia.models.forms.Elements
import com.octopusbjsindia.models.forms.MultiTextItem
import com.octopusbjsindia.utility.Constants
import com.octopusbjsindia.utility.Util
import com.octopusbjsindia.view.activities.FormDisplayActivity
import com.octopusbjsindia.view.fragments.formComponents.MultiTextFragment

class MultiTextAdapter(
    private val fragment: MultiTextFragment,
    private val element: Elements
) : RecyclerView.Adapter<MultiTextAdapter.ViewHolder>() {

    private val dataList: List<MultiTextItem> = element.items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiTextAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.multi_text_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MultiTextAdapter.ViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txt_q_title)
        val etValue: TextInputEditText = itemView.findViewById(R.id.et_value)

        fun bind(item: MultiTextItem) {

            txtTitle.text = item.title

            if (!(fragment.activity as FormDisplayActivity).isEditable) {
                etValue.isFocusable = false
                etValue.isEnabled = false
            } else {
                etValue.isEnabled = true
                etValue.isFocusable = true
            }

            when (item.inputType) {
                "date" -> {
                    //todo disable cursor visibility and open date picker on click

                    etValue.isFocusable = false
                    etValue.isEnabled = true

                    /* if (!TextUtils.isEmpty((fragment.activity as FormDisplayActivity).formAnswersMap[element.getName()])) {
                         etAnswer.setText((getActivity() as FormDisplayActivity).formAnswersMap[element.getName()])
                     }*/
                }
                "time" -> {
                    //TODO disable cursor visibility and open time picker on click

                    etValue.isFocusable = false
                    etValue.isEnabled = true
                    /*if (!TextUtils.isEmpty((getActivity() as FormDisplayActivity).formAnswersMap[element.getName()])) {
                        etAnswer.setText((getActivity() as FormDisplayActivity).formAnswersMap[element.getName()])
                    }*/
                }

                "number" -> {
                    etValue.isFocusable = true
                    etValue.isEnabled = true
                    etValue.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    if (item.maxLength != null) {
                        etValue.filters = arrayOf(InputFilter.LengthFilter(item.maxLength))
                    }
                    /*if (!TextUtils.isEmpty((getActivity() as FormDisplayActivity).formAnswersMap[element.getName()])) {
                        etAnswer.setText((getActivity() as FormDisplayActivity).formAnswersMap[element.getName()])
                    }*/
                }
                else -> {
                    //todo if input type is null -> "text"
                    etValue.isFocusable = true
                    etValue.isEnabled = true
                    etValue.inputType = InputType.TYPE_CLASS_TEXT

                    /*if (!TextUtils.isEmpty((getActivity() as FormDisplayActivity).formAnswersMap[element.name])) {
                        etAnswer.setText((getActivity() as FormDisplayActivity).formAnswersMap[element.name])
                    }*/
                }
            }

            if (item.placeHolder != null) {
                etValue.hint = item.placeHolder
            }

            etValue.setOnClickListener {
                if (item.inputType.equals("date", ignoreCase = true)) {
                    if (item.minDate != null || item.maxDate != null) {
                        if (item.minDate != null && item.maxDate != null) {
                            val minDate =
                                Util.getDateFromTimestamp(item.minDate, Constants.FORM_DATE)
                            val maxDate =
                                Util.getDateFromTimestamp(item.maxDate, Constants.FORM_DATE)
                            Util.showDateDialogEnableBetweenMinMax(
                                fragment.activity, etValue,
                                minDate, maxDate
                            )
                        } else if (item.minDate != null && item.maxDate == null) {
                            val minDate =
                                Util.getDateFromTimestamp(item.minDate, Constants.FORM_DATE)
                            Util.showDateDialogEnableBetweenMinToday(
                                fragment.activity,
                                etValue,
                                minDate
                            )
                        } else if (item.maxDate != null && item.minDate == null) {
                            val maxDate =
                                Util.getDateFromTimestamp(item.maxDate, Constants.FORM_DATE)
                            Util.showDateDialogEnableBeforeMax(fragment.activity, etValue, maxDate)
                        }
                    } else if (item.pastAllowedDays != null || item.futureAllowedDays != null) {
                        if (item.pastAllowedDays != null && item.futureAllowedDays != null) {
                            val pastAllowedDate = Util.getPastFutureDateStringFromToday(
                                -item.pastAllowedDays!!,
                                Constants.FORM_DATE
                            )
                            val futureAllowedDate = Util.getPastFutureDateStringFromToday(
                                item.futureAllowedDays,
                                Constants.FORM_DATE
                            )
                            Util.showDateDialogEnableBetweenMinMax(
                                fragment.activity, etValue,
                                pastAllowedDate, futureAllowedDate
                            )
                        } else if (item.pastAllowedDays != null && item.futureAllowedDays == null) {
                            val pastAllowedDate = Util.getPastFutureDateStringFromToday(
                                -item.pastAllowedDays!!,
                                Constants.FORM_DATE
                            )
                            Util.showDateDialogEnableAfterMin(
                                fragment.activity,
                                etValue,
                                pastAllowedDate
                            )
                        } else if (item.futureAllowedDays != null && item.pastAllowedDays == null) {
                            val futureAllowedDate = Util.getPastFutureDateStringFromToday(
                                item.futureAllowedDays,
                                Constants.FORM_DATE
                            )
                            Util.showDateDialogEnableBeforeMax(
                                fragment.activity,
                                etValue,
                                futureAllowedDate
                            )
                        }
                    } else {
                        Util.showAllDateDialog(fragment.context, etValue)
                    }
                } else {
                    Util.showTimeDialogTwelveHourFormat(fragment.context, etValue)
                }
            }
        }

    }

}