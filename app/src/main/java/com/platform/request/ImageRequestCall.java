package com.platform.request;

import android.util.Log;

import com.platform.BuildConfig;
import com.platform.listeners.ImageRequestCallListener;
import com.platform.models.login.Login;
import com.platform.utility.Constants;
import com.platform.utility.Urls;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.platform.utility.Util.getLoginObjectFromPref;

public class ImageRequestCall {

    private ImageRequestCallListener listener;

    public void setListener(final ImageRequestCallListener listener) {
        this.listener = listener;
    }

    public void uploadImageUsingHttpURLEncoded(File file, final String type, final String formName) {
        final String twoHyphens = "--";
        final String lineEnd = "\r\n";
        final String boundary = "WebKitFormBoundary7MA4YWxkTrZu0gW";
        String response;

        FileInputStream fileInputStream = null;
        DataOutputStream dos = null;
        try {
            fileInputStream = new FileInputStream(file);
            URL connectURL = new URL(BuildConfig.BASE_URL + Urls.Profile.UPLOAD_IMAGE);
            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");

            Login loginObj = getLoginObjectFromPref();
            if (loginObj != null && loginObj.getLoginData() != null &&
                    loginObj.getLoginData().getAccessToken() != null) {
                conn.setRequestProperty(Constants.Login.AUTHORIZATION,
                        "Bearer " + loginObj.getLoginData().getAccessToken());
            }
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"type\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(type);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + file.getName() + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            int bytesAvailable = fileInputStream.available();

            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];

            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            fileInputStream.close();
            dos.flush();

            int ch;
            InputStream errorStream = conn.getErrorStream();
            if (errorStream != null) {
                StringBuilder b = new StringBuilder();
                while ((ch = errorStream.read()) != -1) {
                    b.append((char) ch);
                }

                response = b.toString();
                listener.onFailureListener(response);
                Log.i("Error#Response", response);
                return;
            }

            InputStream is = conn.getInputStream();

            StringBuilder b = new StringBuilder();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            response = b.toString();
            Log.i("Success#Response", response);

            listener.onImageUploadedListener(response, formName);

        } catch (IOException e) {
            Log.e("TAG", e.getMessage());
            listener.onFailureListener(e.getMessage());
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                    if (dos != null) {
                        dos.flush();
                        dos.close();
                    }
                } catch (IOException e) {
                    Log.e("TAG", e.getMessage());
                    listener.onFailureListener(e.getMessage());
                }
            }
        }
    }
}
