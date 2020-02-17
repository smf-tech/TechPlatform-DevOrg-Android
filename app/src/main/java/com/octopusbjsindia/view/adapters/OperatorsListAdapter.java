package com.octopusbjsindia.view.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.SujalamSuphalam.OpratorListData;
import com.octopusbjsindia.presenter.OperatorListFragmentPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OperatorsListAdapter extends RecyclerView.Adapter<OperatorsListAdapter.ViewHolder> {

    private ArrayList<OpratorListData> opratorList;
    private Context mContext;
    private OperatorListFragmentPresenter presenter;
    private String machineId;

    public OperatorsListAdapter(String machineId, List<OpratorListData> opratorList, Context mContext, OperatorListFragmentPresenter presenter) {
        this.opratorList = (ArrayList<OpratorListData>) opratorList;
        this.mContext = mContext;
        this.presenter = presenter;
        this.machineId = machineId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_each_oprator,
                parent, false);
        return new OperatorsListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(opratorList.get(position).getName());
        holder.tvPhone.setText(opratorList.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return opratorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone;
        LinearLayout lyMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            lyMain = itemView.findViewById(R.id.ly_main);
            lyMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    releaseOperator(getAdapterPosition());
                }
            });
        }
    }

    public void releaseOperator(int position) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(mContext));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        TextView title = dialog.findViewById(R.id.tv_dialog_title);
        title.setText(R.string.alert);
        title.setVisibility(View.VISIBLE);

        TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
        text.setText(R.string.assign_opretor_alert_message);
        text.setVisibility(View.VISIBLE);

        Button button = dialog.findViewById(R.id.btn_dialog);
        button.setText(R.string.ok);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(v -> {
            // Close dialog
            presenter.assignOperators(opratorList.get(position).getId(), machineId);
            dialog.dismiss();
        });

        Button button1 = dialog.findViewById(R.id.btn_dialog_1);
        button1.setText(R.string.cancel);
        button1.setVisibility(View.VISIBLE);
        button1.setOnClickListener(v -> {
            // Close dialog
            dialog.dismiss();
        });

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
