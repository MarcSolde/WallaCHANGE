package edu.upc.pes.wallachange.APILayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by sejo on 9/06/17.
 */

public class DonwloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;

    public DownloadPostImageTask(ImageView imageView) {
        this.imageView = imageView;
        this.imageView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            int imageViewHeight = this.getCorrectHeight(result.getWidth(), result.getHeight(), this.imageView.getWidth());

            this.imageView.setImageBitmap(result);
            this.imageView.setVisibility(View.VISIBLE);
            this.imageView.getLayoutParams().height = imageViewHeight;
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
