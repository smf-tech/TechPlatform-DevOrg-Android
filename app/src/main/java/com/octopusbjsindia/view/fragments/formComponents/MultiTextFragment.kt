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
import java.lang.reflect.Type

class MultiTextFragment : Fragment(), View.OnClickListener {

    private lateinit var rvMultiText: RecyclerView
    private lateinit var multiTextAdapter: MultiTextAdapter
    private lateinit var element: Elements
    private var isFirstpage = false

    private val valueJsonArray = JsonArray()
    var valueHashMap = HashMap<String, String>()  //
    var tempHashMap = HashMap<String, String>()

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

    }


    private fun jsonToMap(str: String?) {
        tempHashMap.clear()
       /* tempHashMap.putAll(
            g.fromJson<Map<out String, HashMap<String,String>>>(str, object :
                    TypeToken<HashMap<String, String>> {}.type
            )
        )
        rowMap =
            java.util.HashMap<String, java.util.HashMap<String, java.util.HashMap<String, String>>>()
        rowMap.clear()
        rowMap.putAll(tempHashMap.get(elements.getName()))*/

       //var arrayList = ArrayList<JsonObject>()
        var jsonArray = JsonArray()
       // arrayList = Gson().fromJson(str, object : TypeToken<ArrayList<JsonObject>>() {}.type )
       jsonArray = Gson().fromJson(str, object : TypeToken<JsonArray>() {}.type )
        tempHashMap = Gson().fromJson(str, object : TypeToken<HashMap<String, String>>() {}.type)
       val a =0


    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_previous -> {
                Util.hideKeyboard(v)
                (activity as FormDisplayActivity).goPrevious()
            }

            R.id.bt_next -> {
                Util.hideKeyboard(v)

                if (multiTextAdapter.answersHashMap.isNotEmpty()) {
                    for (i in multiTextAdapter.answersHashMap) {
                        val jsonObject = JsonObject()
                        jsonObject.addProperty(i.key, i.value)
                        valueJsonArray.add(jsonObject)
                    }
                    val gson = Gson()
                    valueHashMap.put(element.name, gson.toJson(valueJsonArray))
                }

                if (valueHashMap.size > 0) {
                    (requireActivity() as FormDisplayActivity).goNext(valueHashMap)
                } else {
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