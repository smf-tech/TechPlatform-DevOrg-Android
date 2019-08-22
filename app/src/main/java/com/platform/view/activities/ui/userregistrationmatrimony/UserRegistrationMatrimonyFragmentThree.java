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


public class UserRegistrationMatrimonyFragmentThree extends Fragment {

    private UserRegistrationMatrimonyViewModel mViewModel;

    public static UserRegistrationMatrimonyFragmentThree newInstance() {
        return new UserRegistrationMatrimonyFragmentThree();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_registration_matrimony_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserRegistrationMatrimonyViewModel.class);
        // TODO: Use the ViewModel
    }

}
