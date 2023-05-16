package com.octopusbjsindia.view.fragments.formComponents;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.forms.Elements;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.FormDisplayActivity;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.octopusbjsindia.utility.Util.getImageName;

public class FileQuestionFragment extends Fragment implements View.OnClickListener {

    private boolean isFirstpage = false;
    private View view;
    private ImageView imageView;
    private Uri outputUri;
    private Uri finalUri;
    private Elements element;
    private String currentPhotoPath;
    private RequestOptions requestOptions;
    private final String TAG = FileQuestionFragment.class.getName();
    private boolean isImageSelected = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_file_question, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        element = (Elements) getArguments().getSerializable("Element");
        TextView tvQuetion = view.findViewById(R.id.tv_question);
        imageView = view.findViewById(R.id.iv_img1);
        if (((FormDisplayActivity) getActivity()).isEditable) {
            imageView.setOnClickListener(this);
        }
        tvQuetion.setText(element.getTitle().getLocaleValue().trim());
        isFirstpage = getArguments().getBoolean("isFirstpage");

        view.findViewById(R.id.bt_previous).setOnClickListener(this);
        view.findViewById(R.id.bt_next).setOnClickListener(this);

        requestOptions = new RequestOptions().placeholder(R.drawable.ic_img);
        requestOptions = requestOptions.apply(RequestOptions.noTransformation());

        if (isFirstpage) {
            view.findViewById(R.id.bt_previous).setVisibility(View.GONE);
        }
        if (((FormDisplayActivity) getActivity()).isFromApproval) {
            if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName()))) {
                Glide.with(getActivity()).applyDefaultRequestOptions(requestOptions).load(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName())).into(imageView);
            }
        } else if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName() + "Uri"))) {
            Uri imageUri = Uri.parse(((FormDisplayActivity) getActivity()).formAnswersMap.get(element.getName() + "Uri"));
            imageView.setImageURI(imageUri);
            ((FormDisplayActivity) getActivity()).isImageUploadPending = false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_img1:
                if (Util.isConnected(getActivity())) {
                    ((FormDisplayActivity) getActivity()).isImageUploadPending = false;
                    onAddImageClick();
                } else {
                    ((FormDisplayActivity) getActivity()).isImageUploadPending = true;

                    Util.showDialog(getActivity(), "Alert", "To upload image, you " + "need internet connectivity. If you do not have internet,you can skip this" + " question and save the form.", "OK", "");
                }
                break;
            case R.id.bt_previous:
                ((FormDisplayActivity) getActivity()).goPrevious();
                break;
            case R.id.bt_next:
                HashMap<String, String> hashMap = new HashMap<String, String>();
                if (((FormDisplayActivity) getActivity()).isImageUploadPending) {
                    ((FormDisplayActivity) getActivity()).goNext(hashMap);
                } else {
                    if (isImageSelected) {
                        hashMap.put(element.getName(), (((FormDisplayActivity) getActivity()).mUploadedImageUrlList.get(((FormDisplayActivity) getActivity()).mUploadedImageUrlList.size() - 1).get(element.getName())));

                        hashMap.put(element.getName() + "Uri", finalUri.toString());
                        ((FormDisplayActivity) getActivity()).goNext(hashMap);
                    } else {
                        if (element.isRequired()) {
                            Util.showToast("Please select image.", this);
                        } else {
                            ((FormDisplayActivity) getActivity()).goNext(hashMap);
                        }
                    }
                }
                break;
        }
    }

    private void onAddImageClick() {
        if (Permissions.isCameraPermissionGranted(getActivity(), this)) {
            showPictureDialog();
        }
    }

    private void showPictureDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(getString(R.string.title_choose_picture));
        String[] items = {getString(R.string.label_gallery), getString(R.string.label_camera)};

        dialog.setItems(items, (dialog1, which) -> {
            switch (which) {
                case 0:
                    choosePhotoFromGallery();
                    break;
                case 1:
                    takePhotoFromCamera();
                    break;
            }
        });
        dialog.show();
    }

    private void choosePhotoFromGallery() {
        try {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, Constants.CHOOSE_IMAGE_FROM_GALLERY);
        } catch (ActivityNotFoundException e) {
            Util.showToast(getString(R.string.msg_error_in_photo_gallery), this);
        }
    }

    private void takePhotoFromCamera() {
        try {
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = getImageFile(); // 1
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
                uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID.concat(".file_provider"), file);
            else uri = Uri.fromFile(file); // 3
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
            startActivityForResult(pictureIntent, Constants.CHOOSE_IMAGE_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            //display an error message
            Toast.makeText(getContext(), getResources().getString(R.string.msg_image_capture_not_support), Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(getContext(), getResources().getString(R.string.msg_take_photo_error), Toast.LENGTH_SHORT).show();
        }
    }

    private File getImageFile() {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.Image.IMAGE_STORAGE_DIRECTORY);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File file;
        file = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        currentPhotoPath = file.getPath();
        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            try {
                finalUri = Uri.fromFile(new File(currentPhotoPath));
                //Crop.of(finalUri, finalUri).start(getContext(), this);
                Util.openCropActivityFreeCropWithFragment(getActivity(),this, finalUri, finalUri);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    getImageFile();
                    outputUri = data.getData();
                    finalUri = Uri.fromFile(new File(currentPhotoPath));
                   // Crop.of(outputUri, finalUri).start(getContext(), this);
                    Util.openCropActivityFreeCropWithFragment(getActivity(),this, outputUri, finalUri);

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        /*else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                imageView.setImageURI(finalUri);
                final File imageFile = new File(Objects.requireNonNull(finalUri.getPath()));
                final File compressedImageFile = Util.compressFile(imageFile);
                isImageSelected = true;
                if (Util.isConnected(getContext())) {
                    if (Util.isValidImageSize(compressedImageFile)) {
                        ((FormDisplayActivity) getActivity()).uploadImage(compressedImageFile, Constants.Image.IMAGE_TYPE_FILE, element.getName());
                    } else {
                        Util.showToast(getString(R.string.msg_big_image), this);
                    }
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), this);
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }*/
        else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            if (data != null) {
                imageView.setImageURI(finalUri);
                final Uri resultUri = UCrop.getOutput(data);
                final File imageFile = new File(Objects.requireNonNull(resultUri).getPath());
                final File compressedImageFile = Util.compressFile(imageFile);
                isImageSelected = true;
                if (Util.isConnected(getContext())) {
                    if (Util.isValidImageSize(compressedImageFile)) {
                        ((FormDisplayActivity) getActivity()).uploadImage(compressedImageFile,
                                Constants.Image.IMAGE_TYPE_FILE, element.getName());
                    } else {
                        Util.showToast(getString(R.string.msg_big_image), this);
                    }
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), this);
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }

    }
}