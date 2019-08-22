package com.platform.view.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.platform.Platform;
import com.platform.R;
import com.platform.listeners.ProfileTaskListener;
import com.platform.models.login.LoginInfo;
import com.platform.models.profile.Jurisdiction;
import com.platform.models.profile.JurisdictionType;
import com.platform.models.profile.Location;
import com.platform.models.profile.Organization;
import com.platform.models.profile.OrganizationProject;
import com.platform.models.profile.OrganizationRole;
import com.platform.models.profile.UserLocation;
import com.platform.models.user.UserInfo;
import com.platform.presenter.ProfileActivityPresenter;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.utility.Permissions;
import com.platform.utility.Util;
import com.platform.widgets.MultiSelectBottomSheet;
import com.platform.widgets.SingleSelectBottomSheet;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("CanBeFinal")
public class EditProfileActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        }
}
