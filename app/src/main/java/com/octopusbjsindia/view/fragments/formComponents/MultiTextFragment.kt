package com.octopusbjsindia.view.fragments.formComponents

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.octopusbjsindia.R
import com.octopusbjsindia.models.forms.Elements
import com.octopusbjsindia.utility.Util
import com.octopusbjsindia.view.activities.FormDisplayActivity
import com.octopusbjsindia.view.fragments.formComponents.adapter.MultiTextAdapter
import org.json.JSONException

class MultiTextFragment : Fragment(), View.OnClickListener {

    private lateinit var rvMultiText: RecyclerView
    private lateinit var multiTextAdapter: MultiTextAdapter
    private lateinit var element: Elements
    private var isFirstpage = false

    private val valueJsonArray = JsonArray()
    var valueHashMap = HashMap<String, String>()  //
    var tempHashMap = HashMap<String, String>()
    var arrayList = ArrayList<JsonObject>()

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

        if (!TextUtils.isEmpty((activity as FormDisplayActivity).formAnswersMap[element.name])) {
            val str = (activity as FormDisplayActivity).formAnswersMap[element.name]
            try {
                str?.let {
                    jsonToMap(it)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        multiTextAdapter = MultiTextAdapter(this, element)

        rvMultiText.layoutManager = LinearLayoutManager(this.context)
        rvMultiText.adapter = multiTextAdapter

        view.findViewById<Button>(R.id.bt_previous).setOnClickListener(this)
        view.findViewById<Button>(R.id.bt_next).setOnClickListener(this)

        if (isFirstpage) {
            view.findViewById<View>(R.id.bt_previous).visibility = View.GONE
        }

    }


    private fun jsonToMap(str: String?) {
        tempHashMap.clear()
        val jsonObj : JsonObject = Gson().fromJson(str, object : TypeToken<JsonObject>() {}.type)
        //arrayList = Gson().fromJson(str, object : TypeToken<ArrayList<JsonObject>>() {}.type)

        val elementArray = jsonObj.get("AnswerArray")
        arrayList = Gson().fromJson(elementArray.toString(),object : TypeToken<ArrayList<JsonObject>>() {}.type)

        for (i in arrayList) {
            val key: String = i.get("questionKey").asString
            val value: String = i.get("answerKey").asString
            tempHashMap.put(key, value)
        }

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_previous -> {
               // Util.hideKeyboard(v)
                (activity as FormDisplayActivity).goPrevious()
            }

            R.id.bt_next -> {
                //Util.hideKeyboard(v)

                if (multiTextAdapter.answersHashMap.isNotEmpty()) {
                    for (i in multiTextAdapter.answersHashMap) {
                        val jsonObject = JsonObject()
                        jsonObject.addProperty("questionKey", i.key)
                        jsonObject.addProperty("answerKey", i.value)
                        valueJsonArray.add(jsonObject)
                    }
                    val gson = Gson()
                    val responseJsonObj = JsonObject()
                    responseJsonObj.addProperty("QuestionType",element.type)
                    responseJsonObj.add("AnswerArray",valueJsonArray)
                    valueHashMap.put(element.name, gson.toJson(responseJsonObj))
                }

                if (valueHashMap.size > 0) {
                    (requireActivity() as FormDisplayActivity).goNext(valueHashMap)
                }
                else {
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