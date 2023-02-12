package com.octopusbjsindia.view.fragments.formComponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.octopusbjsindia.R
import com.octopusbjsindia.models.forms.Elements
import com.octopusbjsindia.utility.Util
import com.octopusbjsindia.view.activities.FormDisplayActivity
import com.octopusbjsindia.view.fragments.formComponents.adapter.MultiTextAdapter

class MultiTextFragment : Fragment(), View.OnClickListener {

    private lateinit var rvMultiText: RecyclerView
    private lateinit var multiTextAdapter: MultiTextAdapter
    private lateinit var element: Elements
    private var isFirstpage = false

    private val valueHashMap = HashMap<String, String>()  //
    private var valuesJsonObject = JsonObject()  //to store answers

    /** form answers submitting format
     *{
    "question1":[{
    "item1":"value1"
    },{
    "item2":"value2"
    },{
    "item3":"value3"
    }]
    }
     **/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_multi_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        element = arguments?.getSerializable("Element") as Elements
        isFirstpage = arguments?.getBoolean("isFirstpage") ?: false
        val tvQuestion = view.findViewById<TextView>(R.id.tv_question)
        rvMultiText = view.findViewById(R.id.rv_multitext)

        tvQuestion.text = element.title?.localeValue

        multiTextAdapter = MultiTextAdapter(this, element)

        rvMultiText.layoutManager = LinearLayoutManager(this.context)
        rvMultiText.adapter = multiTextAdapter

        view.findViewById<Button>(R.id.bt_previous).setOnClickListener(this)
        view.findViewById<Button>(R.id.bt_next).setOnClickListener(this)

        if (isFirstpage) {
            view.findViewById<View>(R.id.bt_previous).visibility = View.GONE
        }
    }

    fun receiveAnswerJson(receivedJsonObjectString: JsonObject) {
        valuesJsonObject = receivedJsonObjectString
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_previous -> {
                (activity as FormDisplayActivity).goPrevious()
            }

            R.id.bt_next -> {
                //todo check for isRequired and answersHashMap has value for any one question else get value from edittext and save in hashmap

                if (valuesJsonObject.size() > 0) {
                    valueHashMap.put(element.name, Gson().toJson(valuesJsonObject))
                    (requireActivity() as FormDisplayActivity).goNext(valueHashMap)
                }else{
                    if (element.isRequired) {
                        if (element.requiredErrorText != null) {
                            Util.showToast(element.requiredErrorText.localeValue, this)
                        } else {
                            Util.showToast(resources.getString(R.string.required_error), this)
                        }
                    } else {
                        (requireActivity() as FormDisplayActivity).goNext(valueHashMap)
                    }
                }
            }
        }
    }


}