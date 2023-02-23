package com.octopusbjsindia.view.fragments.formComponents.adapter

import android.annotation.SuppressLint
import android.text.*
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.octopusbjsindia.R
import com.octopusbjsindia.models.forms.Elements
import com.octopusbjsindia.models.forms.MultiTextItem
import com.octopusbjsindia.utility.Util
import com.octopusbjsindia.view.activities.FormDisplayActivity
import com.octopusbjsindia.view.fragments.formComponents.MultiTextFragment
import java.util.*


class MultiTextAdapter(
    private val fragment: MultiTextFragment,
    private val element: Elements
) : RecyclerView.Adapter<MultiTextAdapter.ViewHolder>() {

    private val dataList: List<MultiTextItem> = element.items
    val answersHashMap = HashMap<String, String>()

    init {
        //add prefilled data
        if (fragment.tempHashMap.isNotEmpty()) {
            answersHashMap.putAll(fragment.tempHashMap)
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

    //need this method because the adapter is replacing the values of views
    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtTitle: TextView = itemView.findViewById(R.id.txt_q_title)
        private val etValue: TextInputEditText = itemView.findViewById(R.id.et_value)
        private val ilValue: TextInputLayout = itemView.findViewById(R.id.il_value)

        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: MultiTextItem) {

            if (fragment.tempHashMap.isNotEmpty() && fragment.tempHashMap.containsKey(item.name)) {
                etValue.setText(fragment.tempHashMap[item.name])
            }

            txtTitle.text = item.title

            if (!(fragment.activity as FormDisplayActivity).isEditable) {
                etValue.isFocusable = false
                etValue.isEnabled = false
            }
            else {
                etValue.isEnabled = true
                etValue.isFocusable = true
            }

            when (item.inputType) {
                "date" -> {
                    etValue.isFocusable = false
                    ilValue.endIconMode = TextInputLayout.END_ICON_CUSTOM
                    ilValue.setEndIconActivated(true)
                    ilValue.endIconDrawable = ResourcesCompat.getDrawable(fragment.resources,
                        R.drawable.ic_arrow_drop_down_24,null)
                }
                "time" -> {
                    etValue.isFocusable = false
                    ilValue.endIconMode = TextInputLayout.END_ICON_CUSTOM
                    ilValue.setEndIconActivated(true)
                    ilValue.endIconDrawable = ResourcesCompat.getDrawable(fragment.resources,
                        R.drawable.ic_arrow_drop_down_24,null)
                }
                "number" -> {
                   /* etValue.isFocusable = true
                    ilValue.endIconMode = TextInputLayout.END_ICON_NONE
                    ilValue.setEndIconActivated(false)
                    ilValue.endIconDrawable = null*/
                    etValue.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    if (item.maxLength != null) {
                        etValue.filters = arrayOf(InputFilter.LengthFilter(item.maxLength))
                        ilValue.helperText = "Max length is ${item.maxLength}"
                    }
                }
                else -> {
                   /* ilValue.setEndIconActivated(false)
                    ilValue.endIconMode = TextInputLayout.END_ICON_NONE
                    ilValue.endIconDrawable = null
                    etValue.isFocusable = true*/
                    etValue.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                }
            }

            if (item.placeHolder != null) {
                etValue.hint = item.placeHolder
            }

            etValue.setOnClickListener {
                if (item.inputType.equals("date", ignoreCase = true)) {
                    Util.hideKeyboard(etValue)
                    Util.showAllDateDialog(fragment.context, etValue)
                    /* if (item.minDate != null || item.maxDate != null) {
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
                     }
                     else if (item.pastAllowedDays != null || item.futureAllowedDays != null) {
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
                     }
                     else {
                         Util.showAllDateDialog(fragment.context, etValue)
                     }*/
                } else if (item.inputType.equals("time", ignoreCase = true)) {
                    Util.hideKeyboard(etValue)
                    Util.showTimeDialogTwelveHourFormat(fragment.context, etValue)
                }else {
                    etValue.requestFocus()
                    etValue.isFocusable = true
                }
            }


            etValue.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    answersHashMap.put(item.name, etValue.text.toString())
                }

            })

            /*  etValue.setOnEditorActionListener { v, actionId, _ ->
                  if (actionId == EditorInfo.IME_ACTION_DONE) {
                      Util.hideKeyboard(v)
                      true
                  } else false
              }*/

           /* etValue.setOnTouchListener { v, event ->
                if (etValue.hasFocus()) {
                    v.parent.requestDisallowInterceptTouchEvent(true)
                    when (event.action and MotionEvent.ACTION_MASK) {
                        MotionEvent.ACTION_SCROLL -> {
                            v.parent.requestDisallowInterceptTouchEvent(false)
                        }
                    }
                }
                false
            }*/
        }

    }

}