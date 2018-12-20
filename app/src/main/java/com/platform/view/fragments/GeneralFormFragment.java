package com.platform.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.platform.R;
import com.platform.listeners.GeneralFormFragmentListener;

@SuppressWarnings("ConstantConditions")
public class GeneralFormFragment extends Fragment implements GeneralFormFragmentListener {

    private View viewFormFragment;
    private ProgressBar pbGenFragment;
    private RelativeLayout rlProgressBarView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewFormFragment = inflater.inflate(R.layout.gen_form_fragment, container, false);
        return viewFormFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        rlProgressBarView = viewFormFragment.findViewById (R.id.gen_frag_progress_bar);
        pbGenFragment = viewFormFragment.findViewById(R.id.pb_gen_form_fragment);
    }

    @Override
    public void showProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (rlProgressBarView != null && pbGenFragment != null) {
                pbGenFragment.setVisibility (View.VISIBLE);
                rlProgressBarView.setVisibility (View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (rlProgressBarView != null && pbGenFragment != null) {
                pbGenFragment.setVisibility (View.GONE);
                rlProgressBarView.setVisibility (View.GONE);
            }
        });
    }
}
