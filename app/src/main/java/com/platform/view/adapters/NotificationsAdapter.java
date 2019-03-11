package com.platform.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.platform.R;
import com.platform.utility.Util;
import com.platform.view.fragments.TMUserApprovalsFragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings({"CanBeFinal", "SameParameterValue"})
public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private Context mContext;

    public NotificationsAdapter(Context context) {
        this.mContext = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(android.R.id.text1);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);
        view.setPadding(10, 10, 10, 10);
        view.setBackground(mContext.getDrawable(R.drawable.bg_rounded_rect_gray));
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mTextView.setText("Notifications appear here!");
        holder.mTextView.setOnClickListener(v ->
                Util.launchFragment(new TMUserApprovalsFragment(), mContext,
                        mContext.getString(R.string.approvals), true));
    }

    @Override
    public int getItemCount() {
        // FIXME: 3/9/2019 Remove this hardcoded count
        return 1;
    }
}
