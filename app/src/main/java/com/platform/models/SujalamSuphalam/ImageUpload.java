package com.platform.models.SujalamSuphalam;

import android.graphics.Bitmap;

public class ImageUpload {
    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private String imageName;
    private Bitmap bitmap;
}
