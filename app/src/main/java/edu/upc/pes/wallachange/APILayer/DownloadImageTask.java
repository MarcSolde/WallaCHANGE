package edu.upc.pes.wallachange.APILayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import edu.upc.pes.wallachange.Models.CurrentUser;

/**
 * Created by sejo on 9/06/17.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ArrayList<Bitmap> imageSwitcher;
    private static final String BASE_URL = "http://10.0.2.2:3000";

    public DownloadImageTask(ArrayList<Bitmap> imageSwitcher) {
        this.imageSwitcher = imageSwitcher;
        //this.imageView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            java.net.URL url = new java.net.URL(BASE_URL + File.separator + urldisplay);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            CurrentUser us = CurrentUser.getInstance();
            connection.addRequestProperty("x-access-token", us.getToken());
            InputStream in = connection.getInputStream();
            //InputStream in = url.openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
            connection.disconnect();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            //int imageViewHeight = this.getCorrectHeight(result.getWidth(), result.getHeight(), this.imageView.getWidth());

            this.imageSwitcher.add(result);
            //this.imageView.setImageBitmap(result);
            //this.imageView.setVisibility(View.VISIBLE);
            //this.imageView.getLayoutParams().height = imageViewHeight;
        }
    }

    private int getCorrectHeight(int width, int height, int windowWidth) {
        float widthFactor = ((float)windowWidth) / (float) width;

        if (height <= width) {
            float result = height * widthFactor;
            return (int) result;
        }

        return windowWidth;
    }
}
