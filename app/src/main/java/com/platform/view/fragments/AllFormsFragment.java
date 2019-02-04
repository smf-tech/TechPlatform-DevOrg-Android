package com.platform.view.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.platform.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllFormsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllFormsFragment extends Fragment {

    ExpandableListView mExpandableListView;

    public AllFormsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AllFormsFragment.
     */
    public static AllFormsFragment newInstance() {
        AllFormsFragment fragment = new AllFormsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_forms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mExpandableListView = view.findViewById(R.id.forms_expandable_list);
        mExpandableListView.setAdapter(new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return 2;
            }

            @Override
            public int getChildrenCount(final int groupPosition) {
                return 2;
            }

            @Override
            public Object getGroup(final int groupPosition) {
                return null;
            }

            @Override
            public Object getChild(final int groupPosition, final int childPosition) {
                return null;
            }

            @Override
            public long getGroupId(final int groupPosition) {
                return 0;
            }

            @Override
            public long getChildId(final int groupPosition, final int childPosition) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getGroupView(final int groupPosition, final boolean isExpanded, final View convertView, final ViewGroup parent) {
                return LayoutInflater.from(getContext()).inflate(R.layout.all_forms_item, parent, false);
            }

            @Override
            public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, final View convertView, final ViewGroup parent) {
                return LayoutInflater.from(getContext()).inflate(R.layout.all_form_sub_item, parent, false);
            }

            @Override
            public boolean isChildSelectable(final int groupPosition, final int childPosition) {
                return true;
            }
        });
    }
}
