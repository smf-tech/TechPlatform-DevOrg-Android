package com.octopusbjsindia.view.fragments.formComponents;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.forms.Elements;
import com.octopusbjsindia.view.activities.FormDisplayActivity;
import com.octopusbjsindia.view.fragments.formComponents.adapter.MatrixQuestionFragmentAdapter;

import java.util.HashMap;
import java.util.Objects;

public class RatingQuestionFragment extends Fragment implements View.OnClickListener {
    //views
    private RecyclerView rv_matrix_question;
    private MatrixQuestionFragmentAdapter matrixQuestionFragmentAdapter;
    private HashMap<String, String> hashMap = new HashMap<>();
    boolean isFirstpage = false;
    private TextView text_title;
    private View view;
    private Elements elements;
    private RatingBar ratingBar;
    private TextView txt_description;
    private JsonObject ratingJsonObject  = new JsonObject();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_rating_question, container, false);
        text_title = view.findViewById(R.id.text_title);
        txt_description = view.findViewById(R.id.txt_description);
        Button btn_loadnext = view.findViewById(R.id.btn_loadnext);
        Button btn_loadprevious = view.findViewById(R.id.btn_loadprevious);
        rv_matrix_question = view.findViewById(R.id.rv_matrix_question);
        ratingBar   = view.findViewById(R.id.ratingBar);

        btn_loadnext.setOnClickListener(this);
        btn_loadprevious.setOnClickListener(this);

        //check for arguments
        if (getActivity() != null && getArguments() != null) {
            if (getArguments().containsKey("Element")) {
                elements = (Elements) getArguments().getSerializable("Element");
                if (elements.getRateMax()!=null) {
                    ratingBar.setNumStars(elements.getRateMax());
                }else {
                    ratingBar.setNumStars(5);
                }
                ratingBar.setStepSize(1);

                if (elements.getDescription() != null){
                    txt_description.setText(elements.getDescription().getLocaleValue());
                }
                ratingJsonObject = new JsonObject();
                ratingJsonObject.addProperty(elements.getName(),elements.getRateMax());
            }
            isFirstpage = getArguments().getBoolean("isFirstpage");

            if (isFirstpage) {
                view.findViewById(R.id.btn_loadprevious).setVisibility(View.INVISIBLE);
            }
        }
        if (!((FormDisplayActivity) getActivity()).isEditable) {
            ratingBar.setFocusable(false);
            ratingBar.setClickable(false);
            ratingBar.setEnabled(false);
        }
        if(!TextUtils.isEmpty(((FormDisplayActivity)getActivity()).formAnswersMap.get(elements.getName()))){
            ratingBar.setRating(Float.parseFloat(((FormDisplayActivity)getActivity()).formAnswersMap.get(elements.getName())));
        }

//        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//
////                Log.d("ratingBar", "Clicked-->"+ ratingBar.getRating());
////                ratingJsonObject.addProperty(elements.getName(),ratingBar.getRating());
//            }
//        });
        // set quetion at top
        text_title.setText(elements.getTitle().getLocaleValue().trim());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_loadnext:
                //set json object and go to next fragment
                hashMap.put(elements.getName(), String.valueOf(ratingBar.getRating()));
                Log.d("btn_loadnext", "Clicked-->"+new Gson().toJson(ratingJsonObject));
                ((FormDisplayActivity) requireActivity()).goNext(hashMap);
                break;
            case R.id.btn_loadprevious:
                //Go to previous fragment
                ((FormDisplayActivity) requireActivity()).goPrevious();
                break;
        }
    }
}