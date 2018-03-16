package com.example.timwe.nasamarsroverphotoapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.timwe.nasamarsroverphotoapp.api.OnImageAvailable;
import com.example.timwe.nasamarsroverphotoapp.domain.NasaImage;

import java.util.ArrayList;

/**
 * Created by timwe on 13-Mar-18.
 */

public class ImageDatabase extends SQLiteOpenHelper {

    private final static String DB_NAME = "NasaImages.db";
    private final static int DB_VERSION = 1;
    private final static String TAG = ImageDatabase.class.getSimpleName();
    private final static String TABLE_NAME = "Images";

    private final static String COLUMN_ID = "id";
    private final static String COLUMN_IMAGEID = "imageId";
    private final static String COLUMN_IMAGEURL = "imageUrl";
    private final static String COLUMN_CAMERANAME = "cameraName";

    public ImageDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate aangeroepen.");

        db.execSQL("CREATE TABLE " + TABLE_NAME + " \n" +
                "( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                COLUMN_IMAGEID + " TEXT NOT NULL, \n" +
                COLUMN_IMAGEURL + " TEXT NOT NULL, \n" +
                COLUMN_CAMERANAME + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, "onUpgrade - DROPPING EXISTING DATABASE");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);

        }




    public void addImage(NasaImage nasaImage) {
        // Add image to database
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_IMAGEID, nasaImage.getImageId());
        contentValues.put(COLUMN_IMAGEURL, nasaImage.getImageUrl());
        contentValues.put(COLUMN_CAMERANAME, nasaImage.getCameraName());

        SQLiteDatabase db = this.getWritableDatabase();
        Long id = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        Log.i(TAG, "Added image " + nasaImage.getImageId() + " to the database.");
    }

    public ArrayList<NasaImage> getAllImages() {
        // Gets all NasaImages in database
        ArrayList<NasaImage> nasaImages = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = ("SELECT * \n" +
                "FROM " + TABLE_NAME);
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {

            String imageId = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGEID));
            String imageUrl = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGEURL));
            String cameraName = cursor.getString(cursor.getColumnIndex(COLUMN_CAMERANAME));

            NasaImage nasaImage = new NasaImage(cameraName, imageUrl, imageId);
            nasaImages.add(nasaImage);
            cursor.moveToNext();
        }

        Log.i(TAG, "Got all images");
        return nasaImages;
    }

    public String[] getCameraNames() {
        // Returns all camera names
        SQLiteDatabase db = this.getReadableDatabase();
        String query = ("SELECT DISTINCT " + COLUMN_CAMERANAME + "\n" +
                    "FROM " + TABLE_NAME);

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        String[] cameraNames = new String[cursor.getCount()];
        int i = 0;
        while(!cursor.isAfterLast()) {
            String cameraName = cursor.getString(cursor.getColumnIndex(COLUMN_CAMERANAME));
            cameraNames[i] = cameraName;
        }
        Log.i(TAG, "Got all camera names");
        return cameraNames;
    }
}


