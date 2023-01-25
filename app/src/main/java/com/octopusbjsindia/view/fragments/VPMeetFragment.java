package com.octopusbjsindia.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

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
        TextView txtMeetType = view.findViewById(R.id.txt_meet_type);
        TextView txtMeetFee = view.findViewById(R.id.txt_meet_fee);

        regFlag = false;

        tvTitle.setText(meetData.getTitle());
        tvAddress.setText(meetData.getLocation().getCity() + ", " + meetData.getLocation().getState());
        tvDate.setText(Util.getDateFromTimestamp(meetData.getSchedule().getDateTime(), Constants.DAY_MONTH_YEAR)+" @ "+
                Util.getFormattedDatee(meetData.getSchedule().getMeetStartTime(),"hh:mm aaa","HH:mm"));

        txtMeetType.setText(meetData.getMeetType());

        if (meetData.getIsRegPaid()) {
            txtMeetFee.setText("Paid Meet - Rs." + meetData.getRegAmount());
        } else {
            txtMeetFee.setText("Free Meet");
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
