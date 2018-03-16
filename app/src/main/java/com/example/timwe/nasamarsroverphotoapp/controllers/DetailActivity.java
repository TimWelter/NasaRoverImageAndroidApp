package com.example.timwe.nasamarsroverphotoapp.controllers;

import android.content.Intent;
import android.os.DeadObjectException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timwe.nasamarsroverphotoapp.R;
import com.example.timwe.nasamarsroverphotoapp.domain.NasaImage;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private TextView cameraName;
    private ImageView roverImage;

    private final static String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        NasaImage nasaImage = (NasaImage) intent.getSerializableExtra("NASA_IMAGE");

        cameraName = (TextView) findViewById(R.id.cameraNameId);
        roverImage = (ImageView) findViewById(R.id.roverPhotoId);

        cameraName.setText(nasaImage.getCameraName());
        Picasso.get().load(nasaImage.getImageUrl()).into(roverImage);

        Log.i(TAG, "Created " + TAG);
    }
}
