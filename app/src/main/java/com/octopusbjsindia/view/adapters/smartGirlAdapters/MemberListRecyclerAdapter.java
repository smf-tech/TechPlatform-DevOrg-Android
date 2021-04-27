package com.octopusbjsindia.view.adapters.smartGirlAdapters;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.models.smartgirl.TrainerList;
import com.octopusbjsindia.models.smartgirl.WorkshopBachList;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.PreferenceHelper;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.SmartGirlWorkshopListActivity;
import com.octopusbjsindia.view.activities.TrainerBatchListActivity;

import java.util.List;
import java.util.Objects;

import static com.octopusbjsindia.utility.Constants.DAY_MONTH_YEAR;

public class MemberListRecyclerAdapter extends RecyclerView.Adapter<MemberListRecyclerAdapter.EmployeeViewHolder> {

    Context mContext;
    private List<TrainerList>  dataList;
    private OnRequestItemClicked clickListener;
    private RequestOptions requestOptions;

    public MemberListRecyclerAdapter(Context context,List<TrainerList>  dataList, final OnRequestItemClicked clickListener) {
        mContext = context;
        this.dataList = dataList;
        this.clickListener = clickListener;

        requestOptions = new RequestOptions().placeholder(R.drawable.ic_user_avatar);
        requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());


    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_batchmember_list, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        holder.tv_member_name.setText(dataList.get(position).getName());
        holder.tv_member_phone.setText(dataList.get(position).getPhone());
        if (dataList.get(position).getMocktTestStatus()){
            holder.tv_mock_test.setText("Mock completed");
            holder.tv_mock_test.setBackgroundResource(R.drawable.bg_rect_mock_completed);
            holder.btnPopupMenu.setVisibility(View.INVISIBLE);
        }else {
            holder.tv_mock_test.setText("Mock pending");
            holder.tv_mock_test.setBackgroundResource(R.drawable.bg_rect_mock_pending);
        }


        if (!TextUtils.isEmpty(dataList.get(position).getProfilePic())) {
            Glide.with(mContext)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(dataList.get(position).getProfilePic())
                    .into(holder.user_profile_pic);


        }

        if (dataList.get(position).getPreFeedBackStatus()) {
            holder.tv_prefeedback_status.setText("Pre Feedback -"+"Submitted");
        }else {
            holder.tv_prefeedback_status.setText("Pre Feedback -"+"Pending");
        }
        if (dataList.get(position).getPostFeedBackStatus()) {
            holder.tv_postfeedback_status.setText("Post Feedback -"+"Submitted");
        }else {
            holder.tv_postfeedback_status.setText("Post Feedback -"+"Pending");
        }

        /*holder.tv_state_value.setText(dataList.get(position).getState().getName());
        holder.tv_district_value.setText(dataList.get(position).getDistrict().getName());

        holder.tv_program_value.setText("Smart Girl");
        holder.tv_category_value.setText(dataList.get(position).getWorkshop_category_id());

        holder.tv_city_value.setText(dataList.get(position).getCity());*/
        //holder.tv_attendence_value.setText(dataList.get(position).geta);
        /*holder.tv_startdate_value.setText(Util.getDateFromTimestamp(dataList.get(position).getWorkShopSchedule().getStartDate(),DAY_MONTH_YEAR));
        holder.tv_enddate_value.setText(Util.getDateFromTimestamp(dataList.get(position).getWorkShopSchedule().getEndDate(),DAY_MONTH_YEAR));*/

/*        holder.tv_additional_trainer_title.setText(dataList.get(position).getTrainerList().get(0).getName());
        if (dataList.get(position).getTrainerList().size()>1) {
            holder.tv_additional_trainer_tow_value.setText(dataList.get(position).getTrainerList().get(1).getName());
        }*/


        //holder.tv_startdate_value.setText(String.valueOf(dataList.get(position).getBatchschedule().getStartDate()));
        //holder.tv_enddate_value.setText(String.valueOf(dataList.get(position).getBatchschedule().getStartDate()));

        /*holder.tv_startdate.setText(Util.getLongDateInString(dataList.get(position).getBatchschedule().getStartDate(), Constants.DAY_MONTH_YEAR));
        holder.tv_enddate.setText(Util.getLongDateInString(dataList.get(position).getBatchschedule().getEndDate(), Constants.DAY_MONTH_YEAR));*/


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView tv_member_phone,tv_member_name,tv_mock_test,tv_prefeedback_status,tv_postfeedback_status;
        ImageView btnPopupMenu;
        ImageView user_profile_pic;
        PopupMenu popup;
        Button btn_view_profile;


        EmployeeViewHolder(View itemView) {
            super(itemView);
            tv_member_phone = itemView.findViewById(R.id.tv_member_phone);
            tv_member_name = itemView.findViewById(R.id.tv_member_name);
            tv_mock_test = itemView.findViewById(R.id.tv_mock_test);
            user_profile_pic = itemView.findViewById(R.id.user_profile_pic);
            tv_postfeedback_status = itemView.findViewById(R.id.tv_postfeedback_status);
            tv_prefeedback_status = itemView.findViewById(R.id.tv_prefeedback_status);


            btn_view_profile = itemView.findViewById(R.id.btn_view_profile);
            btn_view_profile.setVisibility(View.VISIBLE);
            btn_view_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((TrainerBatchListActivity)mContext).getSelectedUserProfile(getAdapterPosition(),
                            dataList.get(getAdapterPosition()).getUserId());
                }
            });
            //btn_approve = itemView.findViewById(R.id.btn_approve);
            //btn_reject = itemView.findViewById(R.id.btn_reject);
            itemView.setOnClickListener(v -> {
                //clickListener.onItemClicked(getAdapterPosition());
            });
            tv_member_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent dial = new Intent();
                        dial.setAction("android.intent.action.DIAL");

                        {
                            dial.setData(Uri.parse("tel:" + tv_member_phone.getText().toString()));
                        }

                        mContext.startActivity(dial);
                    } catch (Exception e) {
                        Log.e("Calling Phone", "" + e.getMessage());
                    }
                }
            });
            /*btn_approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClickListner.onApproveClicked(getAdapterPosition());
                }
            });
            btn_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClickListner.onRejectClicked(getAdapterPosition());
                }
            });*/

            btnPopupMenu = itemView.findViewById(R.id.btn_popmenu);
            btnPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup = new PopupMenu((mContext), v);
                    popup.inflate(R.menu.sg_batchlist_menu);
                    popup.getMenu().findItem(R.id.action_add_beneficiary).setVisible(false);
                    popup.getMenu().findItem(R.id.action_register_beneficiary).setVisible(false);
                    popup.getMenu().findItem(R.id.action_pre_feedback).setVisible(false);
                    popup.getMenu().findItem(R.id.action_post_feedback).setVisible(false);
                    popup.getMenu().findItem(R.id.action_take_mocktest).setVisible(true);
                    popup.show();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (Util.isConnected(mContext)) {
                                switch (item.getItemId()) {
                                    case R.id.action_add_beneficiary:
                                        if (Util.isConnected(mContext)) {

                                            showDialog(mContext, "Alert", "Do you want register beneficiary for workshop?", "No", "Yes", false,1,getAdapterPosition());
                                        } else {
                                            Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                                        }
                                        break;
                                    case R.id.action_register_beneficiary:
                                        if (Util.isConnected(mContext)) {

                                            showDialog(mContext, "Alert", "Do you want to register to workshop?", "No", "Yes", false,2,getAdapterPosition());

                                        } else {
                                            Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                                        }
                                        break;
                                    case R.id.action_post_feedback:
                                        if (Util.isConnected(mContext)) {
                                            ((SmartGirlWorkshopListActivity)mContext).addPostFeedbackFragment(getAdapterPosition());
                                        } else {
                                            Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                                        }
                                        break;
                                    case R.id.action_pre_feedback:
                                        if (Util.isConnected(mContext)) {
                                            ((SmartGirlWorkshopListActivity)mContext).addPreFeedbackFragment(getAdapterPosition());
                                        } else {
                                            Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                                        }
                                        break;
                                    case R.id.action_take_mocktest:
                                        if (Util.isConnected(mContext)) {
                                            clickListener.onItemClicked(getAdapterPosition());

                                        } else {
                                            Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                                        }
                                        break;


                                }
                            } else {
                                Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                            }
                            return false;
                        }
                    });
                }
            });

        }
    }

    public interface OnRequestItemClicked {
        void onItemClicked(int pos);
    }

    public interface OnApproveRejectClicked {
        void onApproveClicked(int pos);

        void onRejectClicked(int pos);
    }
    public void showDialog(Context context, String dialogTitle, String message, String btn1String, String
            btn2String, boolean discardFlag,int type,int pos) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(context));
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

                dialog.dismiss();
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                // Close dialog

                if (type==1){
                    ((SmartGirlWorkshopListActivity)mContext).addRegisterTrainerFragment(pos);
                }
                if (type==2){
                    ((SmartGirlWorkshopListActivity)mContext).addSelfTrainerToBatch(pos);
                }

                if (discardFlag) {
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            });
        }

        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

}
