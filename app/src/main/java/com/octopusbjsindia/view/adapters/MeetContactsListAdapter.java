package com.octopusbjsindia.view.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.Matrimony.MatrimonyUserDetails;
import com.octopusbjsindia.utility.Permissions;

import java.util.ArrayList;
import java.util.Objects;

public class MeetContactsListAdapter extends RecyclerView.Adapter<MeetContactsListAdapter.ViewHolder> {

    private ArrayList<MatrimonyUserDetails> usersList;
    private Activity activity;

    public MeetContactsListAdapter(final ArrayList<MatrimonyUserDetails> usersList, Activity activity) {
        this.usersList = usersList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_meet_contacts,
                parent, false);
        return new MeetContactsListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(usersList.get(position).getName());
        holder.tvContact.setText(usersList.get(position).getPhone());
        holder.tvMailId.setText(usersList.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvContact, tvMailId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvContact = itemView.findViewById(R.id.tv_contact);
            tvContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Permissions.isCallPermission(activity, this)) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + tvContact.getText().toString()));
                        activity.startActivity(callIntent);
                    }
                    showPublishApiDialog("Confirmation",
                            "Are you sure you want to call this number?", "YES", "No",tvContact.getText().toString());
                }
            });
            tvMailId = itemView.findViewById(R.id.tv_mail_id);
        }


        private void showPublishApiDialog(String dialogTitle, String message, String btn1String, String
                btn2String ,String phoneNumber) {
            final Dialog dialog = new Dialog(Objects.requireNonNull(activity));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialogs_leave_layout);

            if (!TextUtils.isEmpty(dialogTitle)) {
                TextView title = dialog.findViewById(R.id.tv_dialog_title);
                title.setText(dialogTitle);
                title.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(message)) {
                TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
                text.setText(message);
                text.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(btn1String)) {
                Button button = dialog.findViewById(R.id.btn_dialog);
                button.setText(btn1String);
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(v -> {
                    // Close dialog

                    if (Permissions.isCallPermission(activity, this)) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + phoneNumber));
                        activity.startActivity(callIntent);
                    }
                    dialog.dismiss();
                });
            }

            if (!TextUtils.isEmpty(btn2String)) {
                Button button1 = dialog.findViewById(R.id.btn_dialog_1);
                button1.setText(btn2String);
                button1.setVisibility(View.VISIBLE);
                button1.setOnClickListener(v -> {
                    // Close dialog
                    dialog.dismiss();
                });
            }

            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
    }
}
