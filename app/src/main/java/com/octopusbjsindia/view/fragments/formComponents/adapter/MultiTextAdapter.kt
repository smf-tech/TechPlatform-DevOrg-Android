package com.octopusbjsindia.view.fragments.formComponents.adapter

import android.text.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
import java.util.*

class MultiTextAdapter(
    private val fragment: MultiTextFragment,
    private val element: Elements
) : RecyclerView.Adapter<MultiTextAdapter.ViewHolder>() {

    private val dataList: List<MultiTextItem> = element.items
    val answersHashMap = HashMap<String,String>()

    init {
        //todo add prefill data in @answersHashMap
        //add prefilled data

        //add prefilled data
        if (fragment.tempHashMap.isNotEmpty()) {

        } else {

        }

    }

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
        private val txtTitle: TextView = itemView.findViewById(R.id.txt_q_title)
        private val etValue: TextInputEditText = itemView.findViewById(R.id.et_value)

        fun bind(item: MultiTextItem) {


            if (fragment.tempHashMap.isNotEmpty() && fragment.tempHashMap.containsKey(item.name)){
                etValue.setText(fragment.tempHashMap[item.name])
            }

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
                    etValue.isFocusable = false
                    etValue.isEnabled = true

                    /*if (!TextUtils.isEmpty((fragment.activity as FormDisplayActivity).formAnswersMap[element.name])) {
                        etValue.setText((fragment.activity as FormDisplayActivity).formAnswersMap[element.name])
                    }*/

                }
                "time" -> {
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
                    Util.hideKeyboard(etValue)
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
                }
                else if (item.inputType.equals("time", ignoreCase = true)) {
                    Util.hideKeyboard(etValue)
                    Util.showTimeDialogTwelveHourFormat(fragment.context, etValue)
                }
            }

            etValue.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    //TODO
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    //TODO
                }

                override fun afterTextChanged(s: Editable?) {
                    answersHashMap.put(item.name, etValue.text.toString())
                }

            })

            etValue.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Util.hideKeyboard(v)
                    true
                } else false
            }
        }

    }

}