package com.example.timwe.nasamarsroverphotoapp.domain;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by timwe on 13-Mar-18.
 */

public class NasaImage implements Serializable {
    private String cameraName;
    private String imageUrl;
    private String imageId;

    private final static String TAG = NasaImage.class.getSimpleName();

    public NasaImage(String cameraName, String imageUrl, String imageId) {
        this.cameraName = cameraName;
        this.imageUrl = imageUrl;
        this.imageId = imageId;
    }

    public String getCameraName() {
        Log.i(TAG, "Returned " + this.cameraName);
        return cameraName;
    }

    public String getImageUrl() {
        Log.i(TAG, "Returned " + this.imageUrl);
        return imageUrl;
    }

    public String getImageId() {
        Log.i(TAG, "Returned " + this.imageId);
        return imageId;
    }

    public void setCameraName(String cameraName) {
        Log.i(TAG, "Set " + this.cameraName + "to " + cameraName);
        this.cameraName = cameraName;
    }

    public void setImageUrl(String imageUrl) {
        Log.i(TAG, "Set " + this.imageUrl + "to " + imageUrl);
        this.imageUrl = imageUrl;
    }

    public void setImageId(String imageId) {
        Log.i(TAG, "Set " + this.imageId + "to " + imageId);
        this.imageId = imageId;
    }
}
