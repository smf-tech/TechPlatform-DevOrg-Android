package com.octopusbjsindia.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octopusbjsindia.R;
import com.octopusbjsindia.matrimonyregistration.APIListener;
import com.octopusbjsindia.models.support.TicketData;
import com.octopusbjsindia.presenter.TicketListFragmentPresenter;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.CommentsAdapter;
import com.octopusbjsindia.view.adapters.TicketListAdapter;

import java.util.List;

public class TicketListFragment extends Fragment implements APIListener {

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ticket_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        TicketListFragmentPresenter presenter = new TicketListFragmentPresenter(this);
        presenter.getTickets();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showMessage(String requestID, String message, int code) {
        Util.showToast(getActivity(),message);
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    public void setTicket(List<TicketData> data) {
        RecyclerView rvTicket = view.findViewById(R.id.rvTickets);
        TicketListAdapter adapter = new TicketListAdapter(this, data);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),
                RecyclerView.VERTICAL, false);
        rvTicket.setLayoutManager(mLayoutManager);
        rvTicket.setAdapter(adapter);
    }
}
