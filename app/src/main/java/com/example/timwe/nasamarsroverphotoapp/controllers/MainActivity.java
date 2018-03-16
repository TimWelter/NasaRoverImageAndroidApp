package com.example.timwe.nasamarsroverphotoapp.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;


import com.example.timwe.nasamarsroverphotoapp.R;
import com.example.timwe.nasamarsroverphotoapp.api.ImageTask;
import com.example.timwe.nasamarsroverphotoapp.api.OnImageAvailable;
import com.example.timwe.nasamarsroverphotoapp.database.ImageDatabase;
import com.example.timwe.nasamarsroverphotoapp.domain.NasaImage;
import com.example.timwe.nasamarsroverphotoapp.util.ImageAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements OnImageAvailable {
    private ListView lvPhotos;
    private ArrayList<NasaImage> nasaImages;
    private ImageTask imageTask;
    private ImageAdapter adapter;
    private ImageDatabase db;
    private ProgressBar progressBar;

    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Created " + TAG);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creates progressbar
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.VISIBLE);

        // Creates array, database and asynctask
        this.nasaImages = new ArrayList<>();
        this.db = new ImageDatabase(this);
        this.imageTask = new ImageTask(this, this.db);

        // Checks if database is full or not
        if (this.db.getAllImages().isEmpty()) {
            this.imageTask.execute("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&api_key=KKHR8O1Z4W5pQJAWPBuh3InuCfF9dQQGqzuMQfhH");
        } else {
            progressBar.setVisibility(View.GONE);
            this.nasaImages = db.getAllImages();
            Log.i(TAG, "Database contains " + nasaImages.size() + " images.");
        }

        // Initialise ListView
        this.lvPhotos = (ListView) findViewById(R.id.photoListView);
        this.adapter = new ImageAdapter(this, nasaImages);
        this.lvPhotos.setAdapter(adapter);

        this.lvPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // If item in viewlist is clicked, go to DetailView
                Log.i(TAG, "Clicked row " + i);
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                NasaImage nasaImage = nasaImages.get(i);
                intent.putExtra("NASA_IMAGE", nasaImage);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onImageAvailable(NasaImage nasaImage) {
        Log.i(TAG, "Image is available");
        this.progressBar.setVisibility(View.GONE);
        this.nasaImages.add(nasaImage);
        this.adapter.notifyDataSetChanged();
    }
}
