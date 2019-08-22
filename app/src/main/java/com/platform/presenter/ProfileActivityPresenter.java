package com.platform.presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.Platform;
import com.platform.R;
import com.platform.listeners.ImageRequestCallListener;
import com.platform.listeners.ProfileRequestCallListener;
import com.platform.models.profile.JurisdictionLevelResponse;
import com.platform.models.profile.OrganizationProjectsResponse;
import com.platform.models.profile.OrganizationResponse;
import com.platform.models.profile.OrganizationRolesResponse;
import com.platform.models.user.User;
import com.platform.models.user.UserInfo;
import com.platform.request.ImageRequestCall;
import com.platform.request.ProfileRequestCall;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.EditProfileActivity;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class ProfileActivityPresenter {
}
