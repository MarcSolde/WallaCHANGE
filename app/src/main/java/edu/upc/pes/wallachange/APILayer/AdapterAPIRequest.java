package edu.upc.pes.wallachange.APILayer;

/**
 * Created by sejo on 21/04/17.
 */

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

public class AdapterAPIRequest  {

    public void GETStringRequestAPI(String url){

        String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";

        StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);
    }


    //BUENO
    public void GETJsonObjectRequestAPI(String url, Response.Listener responseListener, Response.ErrorListener errorListener){

        String  REQUEST_TAG = "com.androidtutorialpoint.volleyJsonObjectRequest";



        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null, responseListener, errorListener);
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq,REQUEST_TAG);

    }
    //YA NO BUENO

    public void GETJsonArrayRequestAPI(String url){

        String  REQUEST_TAG = "com.androidtutorialpoint.volleyJsonArrayRequest";

        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                       }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayReq, REQUEST_TAG);
    }

    public void GETImageLoaderAPI(String url){
        ImageLoader imageLoader = AppSingleton.getInstance(getApplicationContext()).getImageLoader();

        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {

                }
            }
        });
    }

    public void uploadImage(String url, final String image, final String name){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Logg message of the response
                        Log.d(TAG, s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Showing toast
                        Log.e(TAG, "Image Upload Error: " + error.getMessage());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String,String> params = new HashMap<String, String>();

                //Adding parameters
                params.put("KEY_IMAGE", image);
                params.put("KEY_NAME", name);

                //returning parameters
                return params;
            }
        };
        //TODO
        //Creating a Request Queue
        //RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        //requestQueue.add(stringRequest);
    }

    public void POSTImageUploaderAPI(String url){
        //TODO:Entero
        ImageLoader imageLoader = AppSingleton.getInstance(getApplicationContext()).getImageLoader();

        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {

                }
            }
        });
    }

    //TODO:body JSON?
    void POSTRequestAPI(String url ) {
        String  REQUEST_TAG = "com.androidtutorialpoint.volleyPOSTRequest";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //TODO:Parametrizar
                params.put("name", "Alif");
                params.put("domain", "http://itsalif.info");

                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest, REQUEST_TAG);
    }

    public void PUTRequestAPI(String url){

        String  REQUEST_TAG = "com.androidtutorialpoint.putRequest";
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String> ();
                params.put("name", "Alif");
                params.put("domain", "http://itsalif.info");

                return params;
            }

        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(putRequest, REQUEST_TAG);
    }

    public void DELETERequestAPI(String url){
        String  REQUEST_TAG = "com.androidtutorialpoint.Delete";

        StringRequest dr = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.

                    }
                }
        );
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(dr, REQUEST_TAG);
    }



    public void volleyCacheRequest(String url){
        Cache cache = AppSingleton.getInstance(getApplicationContext()).getRequestQueue().getCache();
        Cache.Entry reqEntry = cache.get(url);
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
}


