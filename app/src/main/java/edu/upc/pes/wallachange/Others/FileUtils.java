package edu.upc.pes.wallachange.Others;

import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.util.ArrayList;

import edu.upc.pes.wallachange.MainActivity;

/**
 * Created by sejo on 7/06/17.
 */

public class FileUtils {
    private static MainActivity myActivity;

    public FileUtils(MainActivity activity){
        myActivity = activity;
    }

    public String getPath(Uri uri)
    {
        String wholeID = DocumentsContract.getDocumentId(uri);
        String id = wholeID.split(":")[1];
        String sel = MediaStore.Images.Media._ID + "=?";
        String[] projection = { MediaStore.Images.Media.DATA };

        //Cursor cursor = MediaStore.Images.Media.query(myActivity.getContentResolver(), uri, projection);
        Cursor cursor = myActivity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, sel, new String[]{ id }, null);
        if (cursor == null) return null;
        cursor.moveToFirst();
        String filePath = "";

        int columnIndex = cursor.getColumnIndex(projection[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();
        return filePath;
    }

    public ArrayList<Uri> getPath(ArrayList<Uri> imatgesMiniatura) {

        ArrayList<Uri> pathUris = new ArrayList<>();

        for (Uri uri : imatgesMiniatura){
            pathUris.add(Uri.parse(getPath(uri)));
        }

        return pathUris;
    }
}
