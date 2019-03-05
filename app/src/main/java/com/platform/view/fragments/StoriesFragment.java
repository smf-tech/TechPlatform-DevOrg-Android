package com.platform.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.platform.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

@SuppressWarnings({"EmptyMethod", "WeakerAccess"})
public class StoriesFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stories, container, false);
    }
}
