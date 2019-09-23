package com.platform.view.adapters;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.platform.R;

import java.util.ArrayList;

public class ProfileImagesPagerAdapter extends PagerAdapter {
    private ArrayList<String> profileImageList;
    private Context context;

    public ProfileImagesPagerAdapter(Context context, ArrayList<String> bannerList) {
        this.context = context;
        this.profileImageList = bannerList;
    }

    @Override
    public int getCount() {
        return profileImageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        /*TextView textView = new TextView(view.getContext());
        textView.setText(String.valueOf(position + 1));
        textView.setBackgroundColor(0xff000000 | random.nextInt(0x00ffffff));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(48);*/
        final ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        //bannerObject = bannerList.get(position);
        if (profileImageList != null) {
            String imageUrl = profileImageList.get(position);
//            System.out.println("BANNER PAGER ADAPTER " + imageUrl);
            if (imageUrl != null) {
                if (!imageUrl.isEmpty())
                    Glide.with(context).load(imageUrl).placeholder(R.drawable.ic_user_avatar).thumbnail(0.1f).into(imageView);
            }
        }
        view.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enlargePhoto(profileImageList.get(position));
            }
        });

        return imageView;
    }

    private void enlargePhoto(String photoUrl) {
        // stop the video if playing

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        LayoutInflater factory = LayoutInflater.from(context);
        final View enlargePhotoView = factory.inflate(
                R.layout.enlarge_photo_layout, null);
        FrameLayout closeOption = (FrameLayout) enlargePhotoView.findViewById(R.id.close_layout);
        closeOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        PhotoView photoView = (PhotoView) enlargePhotoView.findViewById(R.id.img_feed_photo);
        ImageView closeImageView = (ImageView) enlargePhotoView.findViewById(R.id.img_close_dialog);
        closeImageView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(photoUrl)) {
            try {
                Glide.with(context)
                        .load(photoUrl)
                        /*.placeholder(R.drawable.image_placeholder)*/
                        .into(photoView);
            } catch (Exception e) {
                e.printStackTrace();
                photoView.setImageResource(R.drawable.ic_user_avatar);
            }
        } else {
            photoView.setImageResource(R.drawable.ic_user_avatar);
        }

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(enlargePhotoView);
        dialog.show();

    }
}
    /*private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }*/
//}
