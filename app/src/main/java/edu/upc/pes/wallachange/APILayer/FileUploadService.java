package edu.upc.pes.wallachange.APILayer;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by sejo on 6/06/17.
 */

public interface FileUploadService {
    // previous code for single file uploads
    @Multipart
    @POST("/api/element/{element}/image")
    Call<ResponseBody> upload(
            @Path("element") String element,
            @Header("x-access-token") String token,
            @Part List<MultipartBody.Part> foto
    );
}
