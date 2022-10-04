package com.octopusbjsindia.view.customs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.view.adapters.MutiselectDialogAdapter;

import java.util.ArrayList;

public class CustomSpinnerDialogClass extends BottomSheetDialog implements
        android.view.View.OnClickListener  {

    public Activity activity;
    public LinearLayout linear_dynamic_filterheight;
    public TextView toolbarTitle;
    public ImageView img_close;
    private Button btnApply;
    public String bottomSheetTitle;
    private RecyclerView rvCustomSpinner;
    private MutiselectDialogAdapter mutiselectDialogAdapter;
    private ArrayList<CustomSpinnerObject> subFiltersets = new ArrayList<>();
    private CustomSpinnerListener customSpinnerListener;
    private boolean isMultiselectionAllowed;

    public CustomSpinnerDialogClass(Activity a, CustomSpinnerListener f, String formTitle,
                                    ArrayList<CustomSpinnerObject> subFiltersets, boolean isMultiselectionAllowed) {
        super(a);
        // TODO Auto-generated constructor stub
        this.activity = a;
        bottomSheetTitle = formTitle;
        this.subFiltersets = subFiltersets;
        this.customSpinnerListener = f;
        this.isMultiselectionAllowed = isMultiselectionAllowed;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.custom_dialog_filter);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.custom_spinner_dialog, null);
        setContentView(bottomSheetView);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        //bottomSheetBehavior.setPeekHeight(500);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        linear_dynamic_filterheight = findViewById(R.id.linear_dynamic_filterheight);
        rvCustomSpinner = findViewById(R.id.rv_custom_spinner);
        btnApply = findViewById(R.id.btn_apply);
        btnApply.setOnClickListener(this);
        img_close =findViewById(R.id.toolbar_edit_action);
        img_close.setOnClickListener(this);
        toolbarTitle =findViewById(R.id.toolbar_title);
        toolbarTitle.setText(bottomSheetTitle);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getOwnerActivity());
        rvCustomSpinner.setLayoutManager(layoutManager);
        mutiselectDialogAdapter = new MutiselectDialogAdapter(getOwnerActivity(), subFiltersets, isMultiselectionAllowed);
        rvCustomSpinner.setAdapter(mutiselectDialogAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_apply:
                    customSpinnerListener.onCustomSpinnerSelection(bottomSheetTitle);
                    dismiss();
                break;
            case R.id.toolbar_edit_action:
                dismiss();
                break;
        }
    }
}
