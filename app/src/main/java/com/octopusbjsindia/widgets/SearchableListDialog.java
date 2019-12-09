/*
  Copyright 2015-2016 Mitesh Pithadiya
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package com.octopusbjsindia.widgets;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.octopusbjsindia.R;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SearchableListDialog extends DialogFragment implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private static final String ITEMS = "items";

    private ArrayAdapter listAdapter;
    private ListView _listViewItems;
    private SearchableItem _searchableItem;
    private AdapterFactory _adapterFactory;
    private OnSearchTextChanged _onSearchTextChanged;
    private SearchView _searchView;

    private String _strTitle;
    @SuppressWarnings("unused")
    private String _strPositiveButtonText;

    public SearchableListDialog() {

    }

    public static SearchableListDialog newInstance(List items) {
        SearchableListDialog multiSelectExpandableFragment = new SearchableListDialog();

        Bundle args = new Bundle();
        args.putSerializable(ITEMS, (Serializable) items);
        multiSelectExpandableFragment.setArguments(args);

        return multiSelectExpandableFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog().getWindow()).setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Getting the layout inflater to inflate the view in an alert dialog.

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        // Crash on orientation change #7

        // Change Start

        // Description: As the instance was re initializing to null on rotating the device,

        // getting the instance from the saved instance

        if (null != savedInstanceState) {
            _searchableItem = (SearchableItem) savedInstanceState.getSerializable("item");
        }
        // Change End

        @SuppressLint("InflateParams") View rootView = inflater.inflate(R.layout.searchable_list_dialog, null);
        setData(rootView);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(rootView);

//        String strPositiveButton = _strPositiveButtonText == null ? "CLOSE" : _strPositiveButtonText;
//        alertDialog.setPositiveButton(strPositiveButton, _onClickListener);

        String strTitle = _strTitle == null ? "Select Item" : _strTitle;
        alertDialog.setTitle(strTitle);

        final AlertDialog dialog = alertDialog.create();
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setCancelable(true);
        return dialog;
    }

    // Crash on orientation change #7

    // Change Start

    // Description: Saving the instance of searchable item instance.

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("item", _searchableItem);
        super.onSaveInstanceState(outState);
    }
    // Change End

    public void setTitle(String strTitle) {
        _strTitle = strTitle;
    }

    public void setPositiveButton(String strPositiveButtonText) {
        _strPositiveButtonText = strPositiveButtonText;
    }

    @SuppressWarnings({"UnnecessaryLocalVariable", "unused"})
    public void setPositiveButton(String strPositiveButtonText, DialogInterface.OnClickListener onClickListener) {
        _strPositiveButtonText = strPositiveButtonText;
        DialogInterface.OnClickListener _onClickListener = onClickListener;
    }

    public void setOnSearchableItemClickListener(SearchableItem searchableItem) {
        this._searchableItem = searchableItem;
    }

    public void setOnSearchTextChangedListener(OnSearchTextChanged onSearchTextChanged) {
        this._onSearchTextChanged = onSearchTextChanged;
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    private void setData(View rootView) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        _searchView = rootView.findViewById(R.id.search);
        _searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        _searchView.setIconifiedByDefault(false);
        _searchView.setOnQueryTextListener(this);
        _searchView.setOnCloseListener(this);
        _searchView.clearFocus();

        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(_searchView.getWindowToken(), 0);

        List items = (List) getArguments().getSerializable(ITEMS);
        _listViewItems = rootView.findViewById(R.id.listItems);

        //create the adapter by passing your ArrayList data

        if (_adapterFactory != null) {
            listAdapter = _adapterFactory.newInstance(items);
        } else {
            listAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, items);
        }

        //attach the adapter to the list
        _listViewItems.setAdapter(listAdapter);

        _listViewItems.setTextFilterEnabled(true);

        _listViewItems.setOnItemClickListener((parent, view, position, id) -> {
            _searchableItem.onSearchableItemClicked(listAdapter.getItem(position), position);
            getDialog().dismiss();
        });
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        _searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
//        listAdapter.filterData(s);
        if (TextUtils.isEmpty(s)) {
//                _listViewItems.clearTextFilter();
            ((ArrayAdapter) _listViewItems.getAdapter()).getFilter().filter(null);
        } else {
            ((ArrayAdapter) _listViewItems.getAdapter()).getFilter().filter(s);
        }

        if (null != _onSearchTextChanged) {
            _onSearchTextChanged.onSearchTextChanged(s);
        }

        return true;
    }

    public void setAdapterFactory(AdapterFactory adapterFactory) {
        this._adapterFactory = adapterFactory;
    }

    @SuppressWarnings("unused")
    public interface SearchableItem<T> extends Serializable {
        void onSearchableItemClicked(T item, int position);
    }


    public interface OnSearchTextChanged {
        void onSearchTextChanged(String strText);
    }


    public interface AdapterFactory {
        ArrayAdapter newInstance(List items);
    }

}