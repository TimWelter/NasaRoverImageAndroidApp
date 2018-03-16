package com.example.timwe.nasamarsroverphotoapp.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timwe.nasamarsroverphotoapp.R;
import com.example.timwe.nasamarsroverphotoapp.domain.NasaImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by timwe on 13-Mar-18.
 */

public class ImageAdapter extends ArrayAdapter<NasaImage> {

    private final static String TAG = ImageAdapter.class.getSimpleName();

    public ImageAdapter(Context context, ArrayList<NasaImage> NasaImages) {
        super(context, 0, NasaImages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get nasaImage
        NasaImage nasaImage = getItem(position);

        // Create view
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_row_listview,
                    parent,
                    false
            );
        }

        // Koppelen UI aan nasaImage attributen
        TextView imageId = (TextView) convertView.findViewById(R.id.imageIdTextId);
        ImageView roverPhoto = (ImageView) convertView.findViewById(R.id.roverPhotoId);

        imageId.setText("Image ID: " + nasaImage.getImageId());
        Picasso.get().load(nasaImage.getImageUrl()).into(roverPhoto);

        Log.i(TAG, "Got adapter");
        return convertView;
    }


}
