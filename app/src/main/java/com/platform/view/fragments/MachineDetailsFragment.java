package com.platform.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.platform.R;

public class MachineDetailsFragment extends Fragment implements View.OnClickListener {
    private View machineDetailsFragmentView;
    String machineId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        machineDetailsFragmentView = inflater.inflate(R.layout.fragment_machine_details, container, false);
        return machineDetailsFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        machineId = bundle.getString("machineId");
        init();
    }

    private void init(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        if (structureMachineListFragmentPresenter != null) {
//            structureMachineListFragmentPresenter.clearData();
//            structureMachineListFragmentPresenter = null;
//        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_eligible){

        } else if(view.getId() == R.id.btn_not_eligible){

        }
    }
}
