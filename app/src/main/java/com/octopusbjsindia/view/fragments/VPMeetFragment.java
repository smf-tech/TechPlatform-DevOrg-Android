package com.octopusbjsindia.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.Matrimony.MatrimonyMeet;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MatrimonyActivity;

public class VPMeetFragment extends Fragment implements View.OnClickListener{

    final private String MEET_DATA = "MeetData";
    private final String MEET_POS = "MeetPos";
    private View view;
    private MatrimonyMeet meetData;
    private CardView cvMain;

    private RelativeLayout progressBar;
    boolean regFlag;
    String status;
    public TextView tvRegistered,tvStatus;
    int pos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vpmeet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        meetData = (MatrimonyMeet) getArguments().getSerializable(MEET_DATA);
        pos = getArguments().getInt(MEET_POS,0);

//        Log.e("List size: ",""+meetData.getContacts().size());
        cvMain = view.findViewById(R.id.cv_main);
        cvMain.setOnClickListener(this);
//        view.findViewById(R.id.bt_create_profile).setOnClickListener(this);
        ImageView ivMeetPic = view.findViewById(R.id.iv_meet_pic);

        if (meetData.getMeetImageUrl() != null && !meetData.getMeetImageUrl().equals("")) {
            Glide.with(this)
                    .load(meetData.getMeetImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivMeetPic);
        }
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvAddress = view.findViewById(R.id.tv_address);
        TextView tvDate = view.findViewById(R.id.tv_date);
        tvRegistered = view.findViewById(R.id.tv_registered);
        tvStatus = view.findViewById(R.id.tv_stetus);

        regFlag = false;
//        for(Contact user : meetData.getContacts()){
//            if(user.getUserId().equals(Utils.getLoginObjectFromPref().getUserDetails().getId())){
//        if(meetData.getStatus()!=null && !TextUtils.isEmpty(meetData.getStatus())){

//                tvRegistered.setText("Registered");
//                tvRegistered.setVisibility(View.VISIBLE);
//            status= "Your Profile "+meetData.getStatus();
//            tvStatus.setText(status);
//            tvStatus.setVisibility(View.VISIBLE);
//            regFlag = true;
//                break;
//        }
//        }

        tvTitle.setText(meetData.getTitle());
        tvAddress.setText(meetData.getLocation().getCity() + ", " + meetData.getLocation().getState());
        tvDate.setText(Util.getDateFromTimestamp(meetData.getSchedule().getDateTime(), Constants.DAY_MONTH_YEAR)+" @ "+
                Util.getFormattedDatee(meetData.getSchedule().getMeetStartTime(),"hh:mm aaa","HH:mm"));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
//        MatrimoFragment.getInstance().setPos(pos);
        Intent intent = new Intent(getActivity(), MatrimonyActivity.class);
        intent.putExtra("ToOpen", "Meet Detail");
        intent.putExtra("MeetData", meetData);
//        intent.putExtra("RegFlag", regFlag);;
//        intent.putExtra("Status", status);
        startActivity(intent);
    }
}
