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

import static com.octopusbjsindia.utility.Constants.FORM_DATE;
import static com.octopusbjsindia.utility.Util.showDateDialogEnableAfterMin;
import static com.octopusbjsindia.utility.Util.showDateDialogEnableBeforeMax;
import static com.octopusbjsindia.utility.Util.showDateDialogEnableBetweenMinToday;

public class TextFragment extends Fragment implements View.OnClickListener, APIDataListener {

    View view;
    TextFragmentPresenter textFragmentPresenter;
    private Elements element;
    private TextView tvQuetion;
    private EditText etAnswer, et_answer_name;
    boolean isFirstpage = false;

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
            if (!((FormDisplayActivity) getActivity()).isEditable) {
                etAnswer.setFocusable(false);
                etAnswer.setEnabled(false);
            }
            if (element.getPlaceHolder() != null && !TextUtils.isEmpty(element.getPlaceHolder().getLocaleValue()))
                etAnswer.setHint(element.getPlaceHolder().getLocaleValue());
            if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()))) {
                etAnswer.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()));
            }
        }
        else if (element.getInputType().equals("date")) {
            view.findViewById(R.id.ti_answer).setVisibility(View.GONE);
            view.findViewById(R.id.ti_answer_date).setVisibility(View.VISIBLE);
            if (!((FormDisplayActivity) getActivity()).isEditable) {
                view.findViewById(R.id.et_answer_date).setFocusable(false);
                view.findViewById(R.id.et_answer_date).setEnabled(false);
            } else {
                view.findViewById(R.id.et_answer_date).setOnClickListener(this);
            }
            etAnswer = view.findViewById(R.id.et_answer_date);
            if (element.getPlaceHolder() != null && !TextUtils.isEmpty(element.getPlaceHolder().getLocaleValue()))
                etAnswer.setHint(element.getPlaceHolder().getLocaleValue());
            if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()))) {
                etAnswer.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()));
            }
        }
        else if (element.getInputType().equals("time")) {
            view.findViewById(R.id.ti_answer).setVisibility(View.GONE);
            view.findViewById(R.id.ti_answer_date).setVisibility(View.VISIBLE);
            if (!((FormDisplayActivity) getActivity()).isEditable) {
                view.findViewById(R.id.et_answer_date).setFocusable(false);
                view.findViewById(R.id.et_answer_date).setEnabled(false);
            } else {
                view.findViewById(R.id.et_answer_date).setOnClickListener(this);
            }
            etAnswer = view.findViewById(R.id.et_answer_date);
            if (element.getPlaceHolder() != null && !TextUtils.isEmpty(element.getPlaceHolder().getLocaleValue()))
                etAnswer.setHint(element.getPlaceHolder().getLocaleValue());
            if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()))) {
                etAnswer.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()));
            }
        }
        else if (element.getInputType().equals("number")) {
            etAnswer = view.findViewById(R.id.et_answer);
            if (!((FormDisplayActivity) getActivity()).isEditable) {
                etAnswer.setFocusable(false);
                etAnswer.setEnabled(false);
            }
            if (element.getMaxLength() != null) {
                element.setMaxLength(element.getMaxLength());
            }
            if (element.getPlaceHolder() != null && !TextUtils.isEmpty(element.getPlaceHolder().getLocaleValue()))
                etAnswer.setHint(element.getPlaceHolder().getLocaleValue());
            etAnswer.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            et_answer_name = view.findViewById(R.id.et_answer_name);
            et_answer_name.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()))) {
                etAnswer.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()));
            }
        }
        else if (element.getInputType().equals("tel")) {
            etAnswer = view.findViewById(R.id.et_answer);
            if (!((FormDisplayActivity) getActivity()).isEditable) {
                etAnswer.setFocusable(false);
                view.findViewById(R.id.et_answer_date).setEnabled(false);
            }
            if (element.getPlaceHolder() != null && !TextUtils.isEmpty(element.getPlaceHolder().getLocaleValue()))
                etAnswer.setHint(element.getPlaceHolder().getLocaleValue());
            etAnswer.setInputType(InputType.TYPE_CLASS_PHONE);
            et_answer_name = view.findViewById(R.id.et_answer_name);
            et_answer_name.setVisibility(View.VISIBLE);
            etAnswer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int count, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int count, int i, int i2) {
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
                if (!((FormDisplayActivity) getActivity()).isEditable) {
                    et_answer_name.setFocusable(false);
                    et_answer_name.setEnabled(false);

                }
            }
        }
        else {
            etAnswer = view.findViewById(R.id.et_answer);
            if (!((FormDisplayActivity) getActivity()).isEditable) {
                etAnswer.setFocusable(false);
                etAnswer.setEnabled(false);
            }
            if (element.getPlaceHolder() != null && !TextUtils.isEmpty(element.getPlaceHolder().getLocaleValue()))
                etAnswer.setHint(element.getPlaceHolder().getLocaleValue());
            if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()))) {
                etAnswer.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()));
            }
        }

        tvQuetion.setText(element.getTitle().getLocaleValue());

        if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName() + "phone"))) {
            etAnswer.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName() + "phone"));
        }
        if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName() + "name"))) {
            et_answer_name.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName() + "name"));
        }

        view.findViewById(R.id.bt_previous).setOnClickListener(this);
        view.findViewById(R.id.bt_next).setOnClickListener(this);

        if (isFirstpage) {
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
                if (element.getInputType().equalsIgnoreCase("date")) {
                    if (element.getMinDate() != null || element.getMaxDate() != null) {
                        if (element.getMinDate() != null && element.getMaxDate() != null) {
                            String minDate = Util.getDateFromTimestamp(element.getMinDate(), FORM_DATE);
                            String maxDate = Util.getDateFromTimestamp(element.getMaxDate(), FORM_DATE);
                            Util.showDateDialogEnableBetweenMinMax(getActivity(), etAnswer,
                                    minDate, maxDate);
                        } else if (element.getMinDate() != null && element.getMaxDate() == null) {
                            String minDate = Util.getDateFromTimestamp(element.getMinDate(), FORM_DATE);
                            //    As per MV requirement, calling showDateDialogEnableBetweenMinToday() method.
                            //    If max date in form schema is null, todays date will be max date.
//                        Util.showDateDialogEnableAfterMin(getActivity(), etAnswer,
//                                minDate);
                            showDateDialogEnableBetweenMinToday(getActivity(), etAnswer,
                                    minDate);
                        } else if (element.getMaxDate() != null && element.getMinDate() == null) {
                            String maxDate = Util.getDateFromTimestamp(element.getMaxDate(), FORM_DATE);
                            showDateDialogEnableBeforeMax(getActivity(), etAnswer,
                                    maxDate);
                        }
                    } else if (element.getPastAllowedDays() != null || element.getFutureAllowedDays() != null) {
                        if (element.getPastAllowedDays() != null && element.getFutureAllowedDays() != null) {
                            String pastAllowedDate = Util.getPastFutureDateStringFromToday(-element.getPastAllowedDays(), FORM_DATE);
                            String futureAllowedDate = Util.getPastFutureDateStringFromToday(element.getFutureAllowedDays(), FORM_DATE);
                            Util.showDateDialogEnableBetweenMinMax(getActivity(), etAnswer,
                                    pastAllowedDate, futureAllowedDate);
                        } else if (element.getPastAllowedDays() != null && element.getFutureAllowedDays() == null) {
                            String pastAllowedDate = Util.getPastFutureDateStringFromToday(-element.getPastAllowedDays(), FORM_DATE);
                            showDateDialogEnableAfterMin(getActivity(), etAnswer,
                                    pastAllowedDate);
                        } else if (element.getFutureAllowedDays() != null && element.getPastAllowedDays() == null) {
                            String futureAllowedDate = Util.getPastFutureDateStringFromToday(element.getFutureAllowedDays(), FORM_DATE);
                            showDateDialogEnableBeforeMax(getActivity(), etAnswer,
                                    futureAllowedDate);
                        }
                    } else {
                        Util.showAllDateDialog(getContext(), etAnswer);
                    }
                } else {
                    Util.showTimeDialogTwelveHourFormat(getContext(), etAnswer);
                }

                break;
            case R.id.bt_previous:
                ((FormDisplayActivity) getActivity()).goPrevious();
                break;
            case R.id.bt_next:
                if (element.getInputType() != null) {
                    if (element.getInputType().equals("number")) {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        if (element.getMaxLength() != null) {
                            if (TextUtils.isEmpty(etAnswer.getText().toString())) {
                                if (element.isRequired()) {
                                    if (element.getRequiredErrorText() != null) {
                                        Util.showToast(element.getRequiredErrorText().getLocaleValue(), this);
                                    } else {
                                        Util.showToast(getResources().getString(R.string.required_error), this);
                                    }
                                    return;
                                }
                                ((FormDisplayActivity) getActivity()).goNext(hashMap);
                            } else {
                                if (isMaxLengthValid(element.getMaxLength())) {
                                    hashMap.put(element.getName() + "phone", etAnswer.getText().toString());
                                    ((FormDisplayActivity) getActivity()).goNext(hashMap);
                                }
                            }
                        } else {
                            if (TextUtils.isEmpty(etAnswer.getText().toString())) {
                                if (element.isRequired()) {
                                    if (element.getRequiredErrorText() != null) {
                                        Util.showToast(element.getRequiredErrorText().getLocaleValue(), this);
                                    } else {
                                        Util.showToast(getResources().getString(R.string.required_error), this);
                                    }
                                    return;
                                }
                                ((FormDisplayActivity) getActivity()).goNext(hashMap);
                            } else {
                                hashMap.put(element.getName() + "phone", etAnswer.getText().toString());
                                ((FormDisplayActivity) getActivity()).goNext(hashMap);
                            }
                        }
                    } else if (element.getInputType().equals("tel")) {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        if (TextUtils.isEmpty(etAnswer.getText().toString())) {
                            if (element.isRequired()) {
                                if (element.getRequiredErrorText() != null) {
                                    Util.showToast(element.getRequiredErrorText().getLocaleValue(), this);
                                } else {
                                    Util.showToast(getResources().getString(R.string.required_error), this);
                                }
                                return;
                            }
                            ((FormDisplayActivity) getActivity()).goNext(hashMap);
                        } else {
                            if (isAllInputsValid()) {
                                hashMap.put(element.getName() + "phone", etAnswer.getText().toString());
                                hashMap.put(element.getName() + "name", et_answer_name.getText().toString());
                                ((FormDisplayActivity) getActivity()).goNext(hashMap);
                            }
                        }
                    } else {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        if (TextUtils.isEmpty(etAnswer.getText().toString())) {
                            if (element.isRequired()) {
                                if (element.getRequiredErrorText() != null) {
                                    Util.showToast(element.getRequiredErrorText().getLocaleValue(), this);
                                } else {
                                    Util.showToast(getResources().getString(R.string.required_error), this);
                                }
                                return;
                            }
                        } else {
                            hashMap.put(element.getName(), etAnswer.getText().toString());
                        }
                        ((FormDisplayActivity) getActivity()).goNext(hashMap);
                    }
                } else {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    if (TextUtils.isEmpty(etAnswer.getText().toString())) {
                        if (element.isRequired()) {
                            if (element.getRequiredErrorText() != null) {
                                Util.showToast(element.getRequiredErrorText().getLocaleValue(), this);
                            } else {
                                Util.showToast(getResources().getString(R.string.required_error), this);
                            }
                            return;
                        }
                    } else {
                        hashMap.put(element.getName(), etAnswer.getText().toString());
                    }
                    ((FormDisplayActivity) getActivity()).goNext(hashMap);
                }
                /////////////////
                /*if (element.getInputType() != null && element.getInputType().equals("number")) {
                    if (element.getMaxLength() != null && element.getMaxLength() >= 10) {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        if (TextUtils.isEmpty(etAnswer.getText().toString())) {
                            if (element.isRequired()) {
                                if (element.getRequiredErrorText() != null) {
                                    Util.showToast(element.getRequiredErrorText().getLocaleValue(), this);
                                } else {
                                    Util.showToast(getResources().getString(R.string.required_error), this);
                                }
                                return;
                            }
                            ((FormDisplayActivity) getActivity()).goNext(hashMap);
                        } else {
                            if (isAllInputsValid()) {
                                hashMap.put(element.getName() + "phone", etAnswer.getText().toString());
                                hashMap.put(element.getName() + "name", et_answer_name.getText().toString());

                                ((FormDisplayActivity) getActivity()).goNext(hashMap);
                            }
                        }
                    } else {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        if (TextUtils.isEmpty(etAnswer.getText().toString())) {
                            if (element.isRequired()) {
                                if (element.getRequiredErrorText() != null) {
                                    Util.showToast(element.getRequiredErrorText().getLocaleValue(), this);
                                } else {
                                    Util.showToast(getResources().getString(R.string.required_error), this);
                                }
                                return;
                            }
                        } else {
                            if (element.getMaxLength() != null) {
                                if (etAnswer.getText().toString().length() == element.getMaxLength()) {
                                    hashMap.put(element.getName(), etAnswer.getText().toString());
                                } else {
                                    if (element.getRequiredErrorText() != null) {
                                        Util.showToast(element.getRequiredErrorText().getLocaleValue(), this);
                                    } else {
                                        Util.showToast(getResources().getString(R.string.required_error), this);
                                    }
                                    return;
                                }
                            } else {
                                hashMap.put(element.getName(), etAnswer.getText().toString());
                            }
                        }
                        ((FormDisplayActivity) getActivity()).goNext(hashMap);
                    }
                } else {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    if (TextUtils.isEmpty(etAnswer.getText().toString())) {
                        if (element.isRequired()) {
                            if (element.getRequiredErrorText() != null) {
                                Util.showToast(element.getRequiredErrorText().getLocaleValue(), this);
                            } else {
                                Util.showToast(getResources().getString(R.string.required_error), this);
                            }
                            return;
                        }
                    } else {
                        hashMap.put(element.getName(), etAnswer.getText().toString());
                    }
                    ((FormDisplayActivity) getActivity()).goNext(hashMap);
                }*/
                break;
        }
    }

    private boolean isMaxLengthValid(int maxLength) {
        boolean isInputValid = true;
        if (getTextInputValue().trim().length() != maxLength) {
            Util.setError(etAnswer, "Not a valid number.");
            isInputValid = false;
        }
        return isInputValid;
    }

    private boolean isAllInputsValid() {
        boolean isInputValid = true;
        if (getTextInputValue().trim().length() != 10) {
            Util.setError(etAnswer, getResources().getString(R.string.msg_mobile_number_is_invalid));
            isInputValid = false;
        } else if (et_answer_name.getText().toString().trim().length() == 0) {
            Util.setError(et_answer_name, "Please enter the name of user.");
            isInputValid = false;
        }
        return isInputValid;
    }

    private String getTextInputValue() {
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
