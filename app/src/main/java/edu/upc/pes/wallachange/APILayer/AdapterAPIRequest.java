package edu.upc.pes.wallachange.APILayer;

/**
 * Created by sejo on 21/04/17.
 */


import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AdapterAPIRequest   {

    private static final String BASE_URL_LOCAL = "http://10.0.2.2:3000";
    private static final String BASE_URL_SERVER = "http://104.236.98.100:3000";

    // GETERS
//    public void GETRequestAPI(String url, Response.Listener responseListener, Response.ErrorListener errorListener){
//
//        String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";
//
//        StringRequest strReq = new StringRequest(url, responseListener, errorListener) {
//            @Override
//            public
//        };
//        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);
//    }


    public void GETRequestAPI(String url, Response.Listener responseListener, Response.ErrorListener errorListener, final Map<String,String> headers){
        String  REQUEST_TAG = "com.androidtutorialpoint.volleyJsonObjectRequest";
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, BASE_URL_LOCAL+url, null, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params = headers;
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq,REQUEST_TAG);
    }



    public void GETJsonArrayRequestAPI(String url, Response.Listener responseListener, Response.ErrorListener errorListener, final Map<String, String> headers){
        String  REQUEST_TAG = "com.androidtutorialpoint.volleyJsonArrayRequest";

        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(Request.Method.GET, BASE_URL_LOCAL+url, null, responseListener, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params = headers;
                return params;
            }
        };
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayReq, REQUEST_TAG);
    }



    public void GETImageLoaderAPI(String url, ImageLoader.ImageListener imageListener){
        ImageLoader imageLoader = AppSingleton.getInstance(getApplicationContext()).getImageLoader();

        imageLoader.get(url, imageListener);
        /*new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Image Load Error: " + error.getMessage());
            }
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                }
            }
        });*/
    }



    //POST
    public void POSTRequestAPI(String url, Response.Listener responseListener, Response.ErrorListener errorListener,  final JSONObject body, final Map<String,String>  headers) {
        String  REQUEST_TAG = "com.androidtutorialpoint.volleyPOSTRequest";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL_LOCAL+url, body, responseListener, errorListener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params = headers;
                return params;
            }

        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest, REQUEST_TAG);
    }

//
//    public void POSTStringRequestAPI(String url, Response.Listener responseListener, Response.ErrorListener errorListener, final Map<String,String> parametres, final Map<String, String> capceleres) {
//        String  REQUEST_TAG = "com.androidtutorialpoint.volleyPOSTRequest";
//        StringRequest postRequest = new StringRequest(url, responseListener, errorListener) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>(parametres);
//                /*Iterator it = parametres.entrySet().iterator();
//                while (it.hasNext()) {
//                    Map.Entry pair = (Map.Entry)it.next();
//                    params.put(pair.getKey().toString(), pair.getValue().toString());
//                    it.remove(); // avoids a ConcurrentModificationException
//                }*/
//
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<String, String>(capceleres);
//                /*Iterator it = capceleres.entrySet().iterator();
//                while (it.hasNext()) {
//                    Map.Entry pair = (Map.Entry)it.next();
//                    headers.put(pair.getKey().toString(), pair.getValue().toString());
//                    it.remove(); // avoids a ConcurrentModificationException
//                }*/
//
//                return headers;
//            }
//        };
//        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest, REQUEST_TAG);
//    }

    //PUT
    public void PUTRequestAPI(String url, Response.Listener responseListener, Response.ErrorListener errorListener, final JSONObject body, final Map<String,String> headers){
        String  REQUEST_TAG = "com.androidtutorialpoint.putRequest";
        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, BASE_URL_LOCAL+url, body, responseListener, errorListener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params = headers;
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(putRequest, REQUEST_TAG);
    }

    public void DELETERequestAPI(String url, Response.Listener responseListener, Response.ErrorListener errorListener, final Map<String,String> headers){
        String  REQUEST_TAG = "com.androidtutorialpoint.Delete";

        JsonObjectRequest deleteRequest = new JsonObjectRequest(Request.Method.DELETE, BASE_URL_LOCAL+url, null, responseListener, errorListener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params = headers;
                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

                if (response.data == null || response.data.length == 0) {
                    return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
                } else {
                    return super.parseNetworkResponse(response);
                }
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(deleteRequest, REQUEST_TAG);
        /*
        StringRequest dr = new StringRequest(Request.Method.DELETE, BASE_URL_LOCAL+url, responseListener, errorListener);
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(dr, REQUEST_TAG);
        */
    }



    public void volleyCacheRequest(String url){
        Cache cache = AppSingleton.getInstance(getApplicationContext()).getRequestQueue().getCache();
        Cache.Entry reqEntry = cache.get(BASE_URL_LOCAL+url);
        if(reqEntry != null){
            try {
                String data = new String(reqEntry.data, "UTF-8");
                //Handle the Data here.
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else{

            //Request Not present in cache, launch a network request instead.
        }
    }

    public void volleyInvalidateCache(String url){
        AppSingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().invalidate(url, true);
    }

    public void volleyDeleteCache(String url){
        AppSingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().remove(url);
    }

    public void volleyClearCache(){
        AppSingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();
    }




    /////////////////////////////////////////////////////////



}