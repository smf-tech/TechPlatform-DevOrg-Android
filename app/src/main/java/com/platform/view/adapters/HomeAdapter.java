package com.platform.view.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.home.HomeModel;

import java.util.List;

@SuppressWarnings({"CanBeFinal", "unused"})
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private Context context;
    private List<HomeModel> menuItemList;
    private final String TAG = HomeAdapter.class.getName();

    public HomeAdapter(List<HomeModel> menuList, Context context) {
        this.menuItemList = menuList;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_home_module, parent, false);
        return new HomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        if (!menuItemList.get(position).getAccessible()) {
            holder.lockedLayout.setVisibility(View.VISIBLE);
        } else {
            holder.lockedLayout.setVisibility(View.GONE);
        }

        holder.moduleIcon.setImageResource(menuItemList.get(position).getModuleIcon());
        holder.moduleName.setText(menuItemList.get(position).getModuleName());
    }

    @Override
    public int getItemCount() {
        return menuItemList.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        TextView moduleName;
        ImageView moduleIcon;
        LinearLayout moduleView;
        RelativeLayout lockedLayout;

        HomeViewHolder(View view) {
            super(view);

            moduleName = view.findViewById(R.id.home_module_name);
            moduleIcon = view.findViewById(R.id.home_module_icon);
            lockedLayout = view.findViewById(R.id.home_modules_locked);

            moduleView = view.findViewById(R.id.home_module_view);
            moduleView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.home_module_view:
                    try {
                        if (menuItemList.get(getAdapterPosition()).getAccessible()) {
                            Intent openClass = new Intent(context,
                                    menuItemList.get(getAdapterPosition()).getDestination());
                            context.startActivity(openClass);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                    break;
            }
        }
    }
}
