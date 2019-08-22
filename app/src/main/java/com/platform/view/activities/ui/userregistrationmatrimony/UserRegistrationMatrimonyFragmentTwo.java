package com.platform.view.activities.ui.userregistrationmatrimony;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.platform.R;


public class UserRegistrationMatrimonyFragmentTwo extends Fragment {

    private UserRegistrationMatrimonyViewModel mViewModel;

    public static UserRegistrationMatrimonyFragmentTwo newInstance() {
        return new UserRegistrationMatrimonyFragmentTwo();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_registration_matrimony_fragment_tow, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserRegistrationMatrimonyViewModel.class);
        // TODO: Use the ViewModel.
        mViewModel.setFragmentName("Fragment_Two_selected");
    }

}
