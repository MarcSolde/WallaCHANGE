package edu.upc.pes.wallachange.APILayer;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.upc.pes.wallachange.Others.FileUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * Created by sejo on 6/06/17.
 */

public class ServiceGenerator {

    private static final String BASE_URL = "http://10.0.2.2:3000/";
    //private static final String BASE_URL = "http://104.236.98.100:3000";

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    public static <S> S createService(
            Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    @NonNull
    private static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull
    private static MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = new File(fileUri.getPath());
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String extension = mimeTypeMap.getFileExtensionFromUrl(fileUri.getPath());
        String memeType = mimeTypeMap.getMimeTypeFromExtension(extension);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(memeType),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }



    public static void UploadFile(ArrayList<Uri> fileUris, String element, String token) {
        // create upload service client
        FileUploadService service =
               createService(FileUploadService.class);

        List<MultipartBody.Part> photo = new ArrayList<>();


        for (Uri fileUri : fileUris) {
            // add dynamic amount
            if (fileUri != null) {

                photo.add(prepareFilePart("photo", fileUri));
            }
        }


        Call<ResponseBody> call = service.upload(element, token, photo);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                System.out.println(response.code());
                System.out.println(response.message());
                System.out.println(response.body());

                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }



    public static void DownloadFile(String fileUrl, final String elementId, final AsyncTask<Void, Void, Void> mostrarImatge){
        final FileDownloadService downloadService =
                ServiceGenerator.createService(FileDownloadService.class);
        String[] aux = fileUrl.split("/");
        final String nameFile = aux[aux.length-1];

        Call<ResponseBody> call = downloadService.downloadFileWithDynamicUrlSync(fileUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            boolean writtenToDisk = FileUtils.writeResponseBodyToDisk(response.body(), elementId, nameFile);

                            Log.d(TAG, "file download was a success? " + writtenToDisk);
                            return null;
                        }
                    }.execute();

                    mostrarImatge.execute();
                }
                else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "error");
            }
        });
    }
}
