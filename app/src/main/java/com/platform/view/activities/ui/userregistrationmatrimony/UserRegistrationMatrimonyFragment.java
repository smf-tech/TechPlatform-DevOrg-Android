package com.platform.view.activities.ui.userregistrationmatrimony;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.platform.R;
import com.platform.view.activities.UserRegistrationMatrimonyActivity;


public class UserRegistrationMatrimonyFragment extends Fragment {

    private UserRegistrationMatrimonyViewModel mViewModel;
    private View tmFragmentView;
    private Button btnShowNext;

    public static UserRegistrationMatrimonyFragment newInstance() {
        return new UserRegistrationMatrimonyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        tmFragmentView = inflater.inflate(R.layout.user_registration_matrimony_fragment, container, false);
        btnShowNext = tmFragmentView.findViewById(R.id.btnshownext);
        btnShowNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((UserRegistrationMatrimonyActivity) getActivity()).showNextFragment(2);
            }
        });
        return tmFragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserRegistrationMatrimonyViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.setFragmentName("Fragment_Two_selected");
    }


}
