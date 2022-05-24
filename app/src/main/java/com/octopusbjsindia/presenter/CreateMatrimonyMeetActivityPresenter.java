package com.octopusbjsindia.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.ImageRequestCallListener;
import com.octopusbjsindia.request.ImageRequestCall;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.CreateMatrimonyMeetActivity;
import com.octopusbjsindia.view.fragments.CreateMeetFirstFragment;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;

public class CreateMatrimonyMeetActivityPresenter implements ImageRequestCallListener {
    private final String TAG = this.getClass().getName();
    private WeakReference<CreateMatrimonyMeetActivity> mContext;

    public CreateMatrimonyMeetActivityPresenter(CreateMatrimonyMeetActivity tmFragment) {
        mContext = new WeakReference<>(tmFragment);
    }

    public void uploadProfileImage(File file, String type, final String formName) {
        ImageRequestCall requestCall = new ImageRequestCall();
        requestCall.setListener(this);

        mContext.get().showProgressBar();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... voids) {
                requestCall.uploadImageUsingHttpURLEncoded(file, type, formName ,null,null);
                return null;
            }
        }.execute();

    }

    @Override
    public void onImageUploadedListener(String response, String formName) {
        Log.e(TAG, "onImageUploadedListener:\n" + response);
//        mContext.get().runOnUiThread(() -> Util.showToast(
//                mContext.get().getResources().getString(R.string.image_upload_success),mContext.get()));
        mContext.get().hideProgressBar();
        try {
            if (new JSONObject(response).has("data")) {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                String url = (String) data.get("url");
                Log.e(TAG, "onPostExecute: Url: " + url);
                mContext.get().imageUploadedSuccessfully(url,formName);
            } else {
                Log.e(TAG, "onPostExecute: Invalid response");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onFailureListener(String error) {
        mContext.get().hideProgressBar();
    }
}
