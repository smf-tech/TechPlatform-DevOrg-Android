package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.Matrimony.UserProfileList;
import com.octopusbjsindia.view.activities.MatrimonyProfileDetailsActivity;
import com.octopusbjsindia.view.activities.MatrimonyProfileListActivity;

import java.util.ArrayList;
import java.util.List;

public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.ViewHolder> {

    private final String TO_OPEN = "toOpen";
    private final String PROFILE_DETAIL = "profileDetail";
    private final String PROFILE_CONTACT = "profileContact";
    private final String PROFILE_DATA = "profileData";

    ArrayList<UserProfileList> userProfileList;
    Context mContext;
    boolean openDetail;
    boolean openContact;

    private RequestOptions requestOptions;

    public UserProfileAdapter(Context context, List<UserProfileList> data, boolean openDetail, boolean openContact) {
        userProfileList = (ArrayList<UserProfileList>) data;
        mContext = context;
        this.openDetail = openDetail;
        this.openContact = openContact;
        requestOptions = new RequestOptions().placeholder(R.drawable.ic_no_image);
        requestOptions = requestOptions.apply(RequestOptions.noTransformation());
    }

    @NonNull
    @Override
    public UserProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_each_profile_user,
                parent, false);
        return new UserProfileAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserProfileAdapter.ViewHolder holder, int position) {
        if (userProfileList.get(position).getMatrimonial_profile() != null) {
            if (userProfileList.get(position).getMatrimonial_profile().getOther_marital_information().getProfile_image() != null
                    && !userProfileList.get(position).getMatrimonial_profile().getOther_marital_information().getProfile_image().equals("")) {

                Glide.with(mContext)
                        .applyDefaultRequestOptions(requestOptions)
                        .load(userProfileList.get(position).getMatrimonial_profile()
                                .getOther_marital_information().getProfile_image().get(0))
                        .into(holder.ivUserProfilePic);
            } else {
                holder.ivUserProfilePic.setImageResource(R.drawable.ic_no_image);
            }
            holder.tvName.setText(userProfileList.get(position).getMatrimonial_profile().getPersonal_details().getFirst_name()
                    + ", " + userProfileList.get(position).getMatrimonial_profile().getPersonal_details().getAge() + " yrs");
            holder.tvLocation.setText(userProfileList.get(position).getMatrimonial_profile().getEducational_details().getQualification_degree()
                    + ", " + userProfileList.get(position).getMatrimonial_profile().getResidential_details().getCountry());
        }
    }

    @Override
    public int getItemCount() {
        return userProfileList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserProfilePic;
        TextView tvName, tvLocation;
        FrameLayout lyMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserProfilePic = itemView.findViewById(R.id.iv_user_profile_pic);
            tvName = itemView.findViewById(R.id.tv_name);
            tvLocation = itemView.findViewById(R.id.tv_location);
            lyMain = itemView.findViewById(R.id.ly_main);

            lyMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    UserProfileList Profile = userProfileList.get(getAdapterPosition());
                    Gson gson = new Gson();
                    String jsonInString = gson.toJson(Profile);
                    Intent startMain1 = new Intent(mContext, MatrimonyProfileDetailsActivity.class);
                    startMain1.putExtra("filter_type", jsonInString);
                    mContext.startActivity(startMain1);

                }
            });
        }
    }
   /* public void showDialog(Context context, String dialogTitle, String message, String btn1String, String
            btn2String, int position, int flag) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(context));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_alert_dialog);

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
                dialog.dismiss();
            });
        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }*/
}
