package com.example.timwe.nasamarsroverphotoapp.api;

import android.os.AsyncTask;
import android.util.Log;

import com.example.timwe.nasamarsroverphotoapp.database.ImageDatabase;
import com.example.timwe.nasamarsroverphotoapp.domain.NasaImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by timwe on 13-Mar-18.
 */

public class ImageTask extends AsyncTask<String, Void, String> {


    private OnImageAvailable listener;
    private ImageDatabase imageDatabase;
    private static final String TAG = ImageTask.class.getSimpleName();


    ;

    public ImageTask(OnImageAvailable listener, ImageDatabase imageDatabase) {
        this.listener = listener;
        this.imageDatabase = imageDatabase;
    }

    @Override
    protected String doInBackground(String... params) {
        InputStream inputStream = null;
        int responsCode = -1;
        // URL we got from the .execute in the MainActivity class
        String imageUrl = params[0];
        // Returned result
        String response = "";

        Log.i(TAG, "doInBackground - " + imageUrl);
        try {
            // Create URL object
            URL url = new URL(imageUrl);
            // Open a connection on the URL
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                return null;
            }

            // Initialise a HTTP connection
            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestMethod("GET");

            // Run HTTP request on URL
            httpConnection.connect();

            // Check if response code is correct
            responsCode = httpConnection.getResponseCode();
            if (responsCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
                response = getStringFromInputStream(inputStream);
            } else {
                Log.e(TAG, "Error, invalid response");
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground MalformedURLEx " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            Log.e("TAG", "doInBackground IOException " + e.getLocalizedMessage());
            return null;
        }

        // Hier eindigt deze methode.
        // Het resultaat gaat naar de onPostExecute methode.
        return response;
    }

    @Override
    protected void onPostExecute(String response) {

        Log.i(TAG, "onPostExecute " + response);

        // Check of er een response is
        if(response == null || response == "") {
            Log.e(TAG, "onPostExecute kreeg een lege response!");
            return;
        }

        JSONObject jsonObject;
        try {
            // Top level json object
            jsonObject = new JSONObject(response);

            // Get all photos and start looping
            JSONArray photos = jsonObject.getJSONArray("photos");
            for(int idx = 0; idx < photos.length(); idx++) {
                // array level objects and get photo
                JSONObject photo = photos.getJSONObject(idx);

                // Get id, url and camera name
                String imageId = photo.getString("id");
                String imageUrl = photo.getString("img_src");
                String cameraName = photo.getJSONObject("camera").getString("full_name");
                Log.i(TAG, "Got info " + imageId + " " + imageUrl + " " + cameraName);

                // Create new NasaImage object
                NasaImage nasaImage = new NasaImage(cameraName, imageUrl, imageId);

                // call back with new person data
                listener.onImageAvailable(nasaImage);
                imageDatabase.addImage(nasaImage);
            }
        } catch( JSONException ex) {
            Log.e(TAG, "onPostExecute JSONException " + ex.getLocalizedMessage());
        }
    }

        private static String getStringFromInputStream(InputStream is) {

            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;
            try {

                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return sb.toString();
        }
    }
