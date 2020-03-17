package com.octopusbjsindia.view.fragments.formComponents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.form_component.MvUserNameResponseModel;
import com.octopusbjsindia.models.forms.Elements;
import com.octopusbjsindia.presenter.TextFragmentPresenter;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.FormDisplayActivity;

import java.util.HashMap;
import java.util.Map;

public class TextFragment extends Fragment implements View.OnClickListener, APIDataListener {

    View view;
    TextFragmentPresenter textFragmentPresenter;
    private Elements element;
    private TextView tvQuetion;
    private EditText etAnswer, et_answer_name;
    boolean isFirstpage =false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_text, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        element = (Elements) getArguments().getSerializable("Element");
        isFirstpage = getArguments().getBoolean("isFirstpage");
        textFragmentPresenter = new TextFragmentPresenter(this);
        tvQuetion = view.findViewById(R.id.tv_question);

        if (TextUtils.isEmpty(element.getInputType())) {
            etAnswer = view.findViewById(R.id.et_answer);
            /*if (!TextUtils.isEmpty(element.getPlaceHolder().getLocaleValue()))
                etAnswer.setHint(element.getPlaceHolder().getLocaleValue());*/
            if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()))) {
                etAnswer.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()));
            }
        } else if (element.getInputType().equals("date")) {
            view.findViewById(R.id.ti_answer).setVisibility(View.GONE);
            view.findViewById(R.id.ti_answer_date).setVisibility(View.VISIBLE);
            view.findViewById(R.id.et_answer_date).setOnClickListener(this);
            etAnswer = view.findViewById(R.id.et_answer_date);
            /*if (!TextUtils.isEmpty(element.getPlaceHolder().getLocaleValue()))
                etAnswer.setHint(element.getPlaceHolder().getLocaleValue());*/
            if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()))) {
                etAnswer.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()));
            }
        } else if (element.getInputType().equals("number")) {
            if (element.getMaxLength() != null && element.getMaxLength() >= 10) {
                etAnswer = view.findViewById(R.id.et_answer);
                /*if (!TextUtils.isEmpty(element.getPlaceHolder().getLocaleValue()))
                    etAnswer.setHint(element.getPlaceHolder().getLocaleValue());*/
                etAnswer.setInputType(InputType.TYPE_CLASS_PHONE);
                et_answer_name = view.findViewById(R.id.et_answer_name);
                et_answer_name.setVisibility(View.VISIBLE);
                etAnswer.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int count, int i2) {
                        //Log.d("mvUserName_Phone", String.valueOf(charSequence));
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int count, int i, int i2) {
                        //Log.d("mvUserName_Phone", String.valueOf(charSequence));
                        if (count >= 9) {

                            textFragmentPresenter.GET_MV_USER_INFO(String.valueOf(charSequence));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName() + "phone"))) {
                    etAnswer.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName() + "phone"));
                }
                if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName() + "name"))) {
                    et_answer_name.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName() + "name"));
                }

            } else {
                etAnswer = view.findViewById(R.id.et_answer);
                /*if (!TextUtils.isEmpty(element.getPlaceHolder().getLocaleValue()))
                    etAnswer.setHint(element.getPlaceHolder().getLocaleValue());*/
                etAnswer.setInputType(InputType.TYPE_CLASS_NUMBER);
                et_answer_name = view.findViewById(R.id.et_answer_name);
                et_answer_name.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()))) {
                    etAnswer.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()));
                }
            }
        } else {
            etAnswer = view.findViewById(R.id.et_answer);
            /*if (!TextUtils.isEmpty(element.getPlaceHolder().getLocaleValue()))
                etAnswer.setHint(element.getPlaceHolder().getLocaleValue());*/
            if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()))) {
                etAnswer.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()));
            }
        }

        tvQuetion.setText(element.getTitle().getDefaultValue());

        if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName() + "phone"))) {
            etAnswer.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName() + "phone"));
        }
        if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName() + "name"))) {
            et_answer_name.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName() + "name"));
        }

        view.findViewById(R.id.bt_previous).setOnClickListener(this);
        view.findViewById(R.id.bt_next).setOnClickListener(this);

        if (isFirstpage){
            view.findViewById(R.id.bt_previous).setVisibility(View.GONE);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_answer_date:
                Util.showAllDateDialog(getContext(), etAnswer);
                break;
            case R.id.bt_previous:
                ((FormDisplayActivity) getActivity()).goPrevious();
                break;
            case R.id.bt_next:

                if (element.getInputType() != null && element.getInputType().equals("number")) {
                    if (element.getMaxLength() != null && element.getMaxLength() >= 10) {
                        if (isAllInputsValid()) {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            if (TextUtils.isEmpty(etAnswer.getText().toString())) {
                                Util.showToast("Please enter some value", this);
                                return;
                            } else {
                                /*JsonObject ColomJsonObject = new JsonObject();
                                ColomJsonObject.addProperty("phone", etAnswer.getText().toString());
                                ColomJsonObject.addProperty("name", et_answer_name.getText().toString());
                                JsonObject finalJsonObjet = new JsonObject();
                                finalJsonObjet.add(element.getName(),ColomJsonObject);*/
                                hashMap.put(element.getName() + "phone", etAnswer.getText().toString());
                                hashMap.put(element.getName() + "name", et_answer_name.getText().toString());
                            }
                            hashMap.entrySet().forEach((Map.Entry<String, String> entry) -> {
                                System.out.println(entry.getKey() + " " + entry.getValue());
                            });
                            ((FormDisplayActivity) getActivity()).goNext(hashMap);

                        }
                    } else {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        if (TextUtils.isEmpty(etAnswer.getText().toString())) {
                            Util.showToast("Please enter some value", this);
                            return;
                        } else {
                            hashMap.put(element.getName(), etAnswer.getText().toString());
                        }

                        ((FormDisplayActivity) getActivity()).goNext(hashMap);
                    }
                } else {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    if (TextUtils.isEmpty(etAnswer.getText().toString())) {
                        Util.showToast("Please enter some value", this);
                        return;
                    } else {
                        hashMap.put(element.getName(), etAnswer.getText().toString());
                    }

                    ((FormDisplayActivity) getActivity()).goNext(hashMap);
                }
                break;
        }
    }


    private boolean isAllInputsValid() {
        boolean isInputValid = true;
        if (TextUtils.isEmpty(getUserMobileNumber())) {
            Util.setError(etAnswer, getResources().getString(R.string.msg_mobile_number_is_empty));
            isInputValid = false;
        } else if (getUserMobileNumber().trim().length() < 10 || getUserMobileNumber().trim().length() > 10) {
            Util.setError(etAnswer, getResources().getString(R.string.msg_mobile_number_is_invalid));
            isInputValid = false;
        } else if (et_answer_name.getText().toString().trim().length() == 0) {
            Util.setError(et_answer_name, "Please enter the name of user.");
            isInputValid = false;
        }
        return isInputValid;
    }

    private String getUserMobileNumber() {
        if (etAnswer != null) {
            return String.valueOf(etAnswer.getText());
        }
        return "";
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        et_answer_name.setText("");
        et_answer_name.setEnabled(true);

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        et_answer_name.setText("");
        et_answer_name.setEnabled(true);
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        Log.d("mvUserName_Phone", response);
        Gson gson = new Gson();
        MvUserNameResponseModel mvUserNameResponseModel = gson.fromJson(response, MvUserNameResponseModel.class);
        if (mvUserNameResponseModel != null) {
            if (!TextUtils.isEmpty(mvUserNameResponseModel.getMvUserNameResponses().get(0).getName())) {
                et_answer_name.setText(mvUserNameResponseModel.getMvUserNameResponses().get(0).getName());
                et_answer_name.setEnabled(false);
            } else {
                et_answer_name.setEnabled(true);
            }
        } else {
            et_answer_name.setEnabled(true);
        }
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void closeCurrentActivity() {

    }
}
