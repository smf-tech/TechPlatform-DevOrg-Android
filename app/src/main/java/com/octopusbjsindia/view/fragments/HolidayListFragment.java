package com.octopusbjsindia.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.LeaveDataListener;
import com.octopusbjsindia.models.leaves.YearlyHolidayData;
import com.octopusbjsindia.models.leaves.YearlyHolidaysAPIResponse;
import com.octopusbjsindia.presenter.LeavesPresenter;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.YearlyHolidaysAdapter;
import java.util.ArrayList;
import java.util.List;

public class HolidayListFragment extends Fragment implements LeaveDataListener {

    private LeavesPresenter presenter;
    private final List<YearlyHolidayData> yearlyHolidayList = new ArrayList<>();
    YearlyHolidaysAdapter yearlyHolidaysAdapter;

    public HolidayListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_holiday_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvYearlyHoliday = view.findViewById(R.id.rv_holiday_list);

        presenter = new LeavesPresenter(this);
        presenter.getHolidayList();
        yearlyHolidaysAdapter = new YearlyHolidaysAdapter(yearlyHolidayList);
        rvYearlyHoliday.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvYearlyHoliday.setAdapter(yearlyHolidaysAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (getActivity() != null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.msg_failure),
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (getActivity() != null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.msg_failure),
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        YearlyHolidaysAPIResponse yearlyHolidaysAPIResponse = PlatformGson.getPlatformGsonInstance().fromJson(response, YearlyHolidaysAPIResponse.class);
        yearlyHolidayList.clear();
        if(yearlyHolidaysAPIResponse != null){
            yearlyHolidayList.addAll(yearlyHolidaysAPIResponse.getData());
            yearlyHolidaysAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showProgressBar() {
        Util.showSimpleProgressDialog(getActivity(), null, getString(R.string.please_wait), false);
    }

    @Override
    public void hideProgressBar() {
        Util.removeSimpleProgressDialog();
    }

    @Override
    public void closeCurrentActivity() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }
}
