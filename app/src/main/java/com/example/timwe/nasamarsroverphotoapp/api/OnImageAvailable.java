package com.example.timwe.nasamarsroverphotoapp.api;

import com.example.timwe.nasamarsroverphotoapp.domain.NasaImage;

/**
 * Created by timwe on 13-Mar-18.
 */

//Listener

public interface OnImageAvailable {
    void onImageAvailable(NasaImage nasaImage);
}
