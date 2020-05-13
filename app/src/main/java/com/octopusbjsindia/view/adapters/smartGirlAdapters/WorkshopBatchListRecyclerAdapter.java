package com.octopusbjsindia.view.adapters.smartGirlAdapters;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.models.smartgirl.TrainerBachList;
import com.octopusbjsindia.models.smartgirl.WorkshopBachList;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.PreferenceHelper;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.SmartGirlWorkshopListActivity;
import com.octopusbjsindia.view.activities.TrainerBatchListActivity;

import java.util.List;
import java.util.Objects;

import static com.octopusbjsindia.utility.Constants.DAY_MONTH_YEAR;
import static com.octopusbjsindia.utility.Constants.SmartGirlModule.ACCESS_CODE_ADD_BENEFICIARY;

public class WorkshopBatchListRecyclerAdapter extends RecyclerView.Adapter<WorkshopBatchListRecyclerAdapter.EmployeeViewHolder> {

    Context mContext;
    private List<WorkshopBachList> dataList;
    private OnRequestItemClicked clickListener;
    private OnApproveRejectClicked buttonClickListner;
    private PreferenceHelper preferenceHelper;

    public WorkshopBatchListRecyclerAdapter(Context context, List<WorkshopBachList> dataList, final OnRequestItemClicked clickListener, final OnApproveRejectClicked approveRejectClickedListner) {
        mContext = context;
        this.dataList = dataList;
        this.clickListener = clickListener;
        this.buttonClickListner = approveRejectClickedListner;
        preferenceHelper = new PreferenceHelper(Platform.getInstance());
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_trainer_batch_item, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        if (dataList.get(position).getTitle()!=null) {
            holder.tv_title_batch.setText(dataList.get(position).getTitle());
        }
        if (dataList.get(position).getAdditional_master_trainer()!=null) {
            holder.tv_main_trainer_name.setText(dataList.get(position).getAdditional_master_trainer().getUser_name());
        }

        if (dataList.get(position).getCurrentUserWorkShopData() != null) {
            holder.tv_register_lable.setVisibility(View.VISIBLE);
        }else {
            holder.tv_register_lable.setVisibility(View.GONE);
        }

        holder.tv_state_value.setText(dataList.get(position).getState().getName());
        holder.tv_district_value.setText(dataList.get(position).getDistrict().getName());
        holder.tv_venue_value.setText(dataList.get(position).getVenue());
        holder.tv_program_value.setText("Smart Girl");
        if (dataList.get(position).getCategory()!=null && dataList.get(position).getCategory().getCategoryName()!=null) {
            holder.tv_category_value.setText(dataList.get(position).getCategory().getCategoryName().getDefault());
        }

        holder.tv_city_value.setText(dataList.get(position).getCity());
        if (dataList.get(position).getBeneficiariesList()!=null) {
            holder.tv_attendence_value.setText(String.valueOf(dataList.get(position).getBeneficiariesList().size())+"/"+dataList.get(position).getTotal_praticipants());//dataList.get(position).getTotal_praticipants());
        }else {
            holder.tv_attendence_value.setText("0"+"/"+dataList.get(position).getTotal_praticipants());//dataList.get(position).getTotal_praticipants());
        }
        holder.tv_startdate_value.setText(Util.getDateFromTimestamp(dataList.get(position).getWorkShopSchedule().getStartDate(),DAY_MONTH_YEAR));
        holder.tv_enddate_value.setText(Util.getDateFromTimestamp(dataList.get(position).getWorkShopSchedule().getEndDate(),DAY_MONTH_YEAR));

        /*holder.tv_additional_trainer_title.setText(dataList.get(position).getTrainerList().get(0).getName());*/
        /*if (dataList.get(position).getAdditional_master_trainer()!=null) {
            holder.tv_additional_trainer_tow_value.setText(dataList.get(position).getAdditional_master_trainer().getUser_name());
        }*/

        if (dataList.get(position).getCreated_by()!=null) {
            holder.tv_additional_trainer_value.setText(dataList.get(position).getCreated_by().get(0).getName());
        }
        if (dataList.get(position).getAdditional_master_trainer()!=null) {
            holder.tv_additional_trainer_tow_value.setText(dataList.get(position).getAdditional_master_trainer().getUser_name());
        }


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

        TextView tv_state_value, tv_district_value, tv_program_value, tv_category_value, tv_city_value,tv_main_trainer_name,tv_venue_value,tv_title_batch,
                tv_attendence_value, tv_startdate_value, tv_enddate_value, tv_additional_trainer_value,tv_additional_trainer_tow_value,tv_register_lable;
        ImageView btnPopupMenu;
        PopupMenu popup;
        Button btn_view_members;


        EmployeeViewHolder(View itemView) {
            super(itemView);

            tv_register_lable = itemView.findViewById(R.id.tv_register_lable);
            btn_view_members = itemView.findViewById(R.id.btn_view_members);
            tv_title_batch = itemView.findViewById(R.id.tv_title_batch);
            tv_venue_value = itemView.findViewById(R.id.tv_venue_value);
            tv_main_trainer_name = itemView.findViewById(R.id.tv_main_trainer_name);
            tv_state_value = itemView.findViewById(R.id.tv_state_value);
            tv_district_value = itemView.findViewById(R.id.tv_district_value);
            tv_program_value = itemView.findViewById(R.id.tv_program_value);
            tv_category_value = itemView.findViewById(R.id.tv_category_value);
            tv_city_value = itemView.findViewById(R.id.tv_city_value);
            tv_attendence_value = itemView.findViewById(R.id.tv_attendence_value);
            tv_startdate_value = itemView.findViewById(R.id.tv_startdate_value);
            tv_enddate_value = itemView.findViewById(R.id.tv_enddate_value);
            tv_additional_trainer_value = itemView.findViewById(R.id.tv_additional_trainer_value);
            tv_additional_trainer_tow_value = itemView.findViewById(R.id.tv_additional_trainer_tow_value);

            //btn_approve = itemView.findViewById(R.id.btn_approve);
            //btn_reject = itemView.findViewById(R.id.btn_reject);
            itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
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
            btn_view_members.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //show member list fragment
                    if (dataList.get(getAdapterPosition()).getBeneficiariesList()!=null) {
                        ((SmartGirlWorkshopListActivity) mContext).addMemberListFragment(getAdapterPosition());
                    }else {
                        Util.showToast("Members not found.",mContext);
                    }
                }
            });
            btnPopupMenu = itemView.findViewById(R.id.btn_popmenu);
            btnPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup = new PopupMenu((mContext), v);
                    popup.inflate(R.menu.sg_batchlist_menu);


                    RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
                    RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
                    if(roleAccessList != null) {
                        List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
                        for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                            switch(roleAccessObject.getActionCode()){

                                case Constants.SmartGirlModule.ACCESS_CODE_EDIT_WORKSHOP:
                                    popup.getMenu().findItem(R.id.action_edit_batch).setVisible(true);

                                    break;

                                case Constants.SmartGirlModule.ACCESS_CODE_CANCEL_WORKSHOP:
                                    //CANCEL WORKSHOP
                                    popup.getMenu().findItem(R.id.action_cancel_batch).setVisible(true);
                                    break;
                                case Constants.SmartGirlModule.ACCESS_CODE_ADD_BENEFICIARY:
                                    //add beneficiary
                                    popup.getMenu().findItem(R.id.action_add_beneficiary).setVisible(true);
                                    break;
                                case Constants.SmartGirlModule.ACCESS_CODE_REGISTER_WORKSHOP:
                                    //add beneficiary
                                    popup.getMenu().findItem(R.id.action_register_beneficiary).setVisible(true);
                                    break;
                                case Constants.SmartGirlModule.ACCESS_CODE_PRE_FEEDBACK:
                                    popup.getMenu().findItem(R.id.action_pre_feedback).setVisible(true);
                                    break;
                                case Constants.SmartGirlModule.ACCESS_CODE_POST_FEEDBACK:
                                    popup.getMenu().findItem(R.id.action_post_feedback).setVisible(true);
                                    break;
                                case Constants.SmartGirlModule.ACCESS_CODE_PRE_FEEDBACK_WORKSHOP:
                                    popup.getMenu().findItem(R.id.action_pre_feedback).setVisible(true);
                                    break;
                                case Constants.SmartGirlModule.ACCESS_CODE_POST_FEEDBACK_WORKSHOP:
                                    popup.getMenu().findItem(R.id.action_post_feedback).setVisible(true);
                                    break;
                                case Constants.SmartGirlModule.ACCESS_CODE_ORGANIZER_FEEDBACK:
                                    popup.getMenu().findItem(R.id.action_organizer_feedback).setVisible(true);
                                    break;
                                case Constants.SmartGirlModule.ACCESS_CODE_PARENTS_FEEDBACK:
                                    popup.getMenu().findItem(R.id.action_parent_feedback).setVisible(true);
                                    break;



                                case Constants.SmartGirlModule.ACCESS_CODE_REGISTER_BATCH:
                                    break;
                                case Constants.SmartGirlModule.ACCESS_CODE_ADD_TRAINER:
                                    break;
                                case Constants.SmartGirlModule.ACCESS_CODE_EDIT_BATCH:
                                    break;

                            }
                        }
                    }





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
                                            if (dataList.get(getAdapterPosition()).getCurrentUserWorkShopData()!=null) {
                                                if (dataList.get(getAdapterPosition()).getCurrentUserWorkShopData().get(0).getPreFeedBackStatus()!=null) {
                                                    if (dataList.get(getAdapterPosition()).getCurrentUserWorkShopData().get(0).getPreFeedBackStatus()) {
                                                        if (!dataList.get(getAdapterPosition()).getCurrentUserWorkShopData().get(0).getPostFeedBackStatus()) {
                                                            ((SmartGirlWorkshopListActivity) mContext).addPostFeedbackFragment(getAdapterPosition());
                                                        }else {Util.showToast("Feedback already submitted.",mContext);}
                                                    } else {
                                                        Util.showToast("you need to fill pre feedback first.", mContext);
                                                    }
                                                }else {Util.showToast("feedback status not available.", mContext);}
                                            }else {
                                                Util.showToast("Please Register to workshop first.",mContext);
                                            }
                                        } else {
                                            Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                                        }
                                        break;
                                    case R.id.action_pre_feedback:
                                        if (Util.isConnected(mContext)) {
                                            if (dataList.get(getAdapterPosition()).getCurrentUserWorkShopData()!=null) {
                                                if (dataList.get(getAdapterPosition()).getCurrentUserWorkShopData().get(0).getPreFeedBackStatus()!=null) {
                                                    if (!dataList.get(getAdapterPosition()).getCurrentUserWorkShopData().get(0).getPreFeedBackStatus()) {
                                                        ((SmartGirlWorkshopListActivity) mContext).addPreFeedbackFragment(getAdapterPosition());
                                                    } else {
                                                        Util.showToast("Feedback already submitted.", mContext);
                                                    }
                                                }else {Util.showToast("feedback status not available.", mContext);}
                                            }else {
                                                Util.showToast("Please Register to workshop first.",mContext);
                                            }
                                        } else {
                                            Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                                        }
                                        break;

                                    case R.id.action_parent_feedback:
                                        if (Util.isConnected(mContext)) {
                                            ((SmartGirlWorkshopListActivity)mContext).addParentFeedbackFragment(getAdapterPosition());
                                        } else {
                                            Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                                        }
                                        break;
                                    case R.id.action_organizer_feedback:
                                        if (Util.isConnected(mContext)) {
                                            ((SmartGirlWorkshopListActivity)mContext).addOrganiserFeedbackFragment(getAdapterPosition());
                                        } else {
                                            Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                                        }
                                        break;

                                    case R.id.action_pretest_trainer:
                                        if (Util.isConnected(mContext)) {
                                            if (dataList.get(getAdapterPosition()).getCurrentUserWorkShopData()!=null) {
                                                ((SmartGirlWorkshopListActivity)mContext).addTrainingPreTestFragment(getAdapterPosition());
                                            }else {
                                                Util.showToast("Please Register to workshop first.",mContext);
                                            }


                                        } else {
                                            Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                                        }
                                        break;
                                    case R.id.action_cancel_batch:
                                        if (Util.isConnected(mContext)) {
                                            ((SmartGirlWorkshopListActivity)mContext).cancelWorkshopRequest(getAdapterPosition());
                                            //Util.showToast(mContext.getString(R.string.coming_soon), mContext);
                                        } else {
                                            Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                                        }
                                        break;
                                    case R.id.action_edit_batch:
                                        if (Util.isConnected(mContext)) {
                                            ((SmartGirlWorkshopListActivity)mContext).EditWorkshopRequest(getAdapterPosition());
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
            RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
            RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
            if(roleAccessList != null) {
                List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
                for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                    if (roleAccessObject.getActionCode()== Constants.SmartGirlModule.ACCESS_CODE_VIEW_PROFILE){
                        btn_view_members.setVisibility(View.VISIBLE);
                    }else {
                        btn_view_members.setVisibility(View.VISIBLE);
                    }
                }
            }
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
