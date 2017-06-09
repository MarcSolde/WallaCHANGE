package edu.upc.pes.wallachange;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


import edu.upc.pes.wallachange.APILayer.AdapterAPIRequest;
import edu.upc.pes.wallachange.Adapters.CategoriesViewElementAdapter;
import edu.upc.pes.wallachange.Adapters.CommentListViewAdapter;
import edu.upc.pes.wallachange.Models.Comment;
import edu.upc.pes.wallachange.Models.CurrentUser;
import edu.upc.pes.wallachange.Models.Element;
import edu.upc.pes.wallachange.Models.User;
import edu.upc.pes.wallachange.Others.ExpandableHeightGridView;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.android.volley.VolleyLog.TAG;

public class ViewElementFragment extends Fragment implements View.OnClickListener{

    private View fragmentViewElementView;
    private MainActivity myActivity;
    private Integer imatgeActual;
    private ArrayList<Uri> imatges;
    private EditText editTextWriteComment;
    private ArrayList<Comment> comentaris;
    private String idElement;
    private Element mElement;
    private ArrayList<String> categories;
    private CategoriesViewElementAdapter categoriesAdapter;
    private EditText editTextCategoria;
    private CurrentUser us;
    private TextView textViewCategoria;
    private ImageView afegeixCategoria;
    private ImageButton editButton;
    private String idUsuariAnunci;
    private ImageButton saveButton;
    private boolean botoEdicioClicat;
    private Button tradeButton;
    private EditText editTextTemporalitat;
    private boolean esTemporal;
    private boolean esOffer;
    private MenuItem deleteElementItem;
    private ImageView imageViewFoto;
    private OkHttpClient client;

    public ViewElementFragment() {}
    //private static final String BASE_URL = "http://10.0.2.2:3000";
    private static final String BASE_URL = "http://104.236.98.100:3000";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        fragmentViewElementView = inflater.inflate(R.layout.fragment_view_element, container, false);
        myActivity = (MainActivity) getActivity();
        myActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myActivity.setTitle(R.string.advertisements_view);

        categories = new ArrayList<>();
        imatges = new ArrayList<>();
        comentaris = new ArrayList<>();
        botoEdicioClicat = false;
        us = CurrentUser.getInstance();

        editButton = (ImageButton) fragmentViewElementView.findViewById(R.id.botoEditar);
        editButton.setOnClickListener(this);
        saveButton = (ImageButton) fragmentViewElementView.findViewById(R.id.botoGuardar);
        saveButton.setOnClickListener(this);
        ImageButton previousPictureButton = (ImageButton) fragmentViewElementView.findViewById(R.id.previousButton);
        previousPictureButton.setOnClickListener(this);
        ImageButton nextPictureButton = (ImageButton) fragmentViewElementView.findViewById(R.id.nextButton);
        nextPictureButton.setOnClickListener(this);
        ImageButton writeCommentButton = (ImageButton) fragmentViewElementView.findViewById(R.id.writeComment);
        writeCommentButton.setOnClickListener(this);
        afegeixCategoria = (ImageView) fragmentViewElementView.findViewById(R.id.botoAfegirCategoria);
        afegeixCategoria.setOnClickListener(this);
        tradeButton = (Button) fragmentViewElementView.findViewById(R.id.tradeButton);
        tradeButton.setOnClickListener(this);
        editTextWriteComment = (EditText) fragmentViewElementView.findViewById(R.id.editTextComment);
        editTextCategoria = (EditText) fragmentViewElementView.findViewById(R.id.categoria);
        textViewCategoria = (TextView) fragmentViewElementView.findViewById(R.id.textViewCategoria);
        editTextTemporalitat = (EditText) fragmentViewElementView.findViewById(R.id.temporalitat);

        AdapterAPIRequest adapterAPIRequest = new AdapterAPIRequest();
        Map<String, String> headers = new HashMap<>();
        headers.put("x-access-token", us.getToken());
        headers.put("Content-Type", "application/json");
        idElement = getArguments().getString("id");
        esOffer = getArguments().getBoolean("chat",false);
        if (idElement != null) {
            //adapterAPIRequest.GETRequestAPI("http://104.236.98.100:3000/element/".concat(id),
            adapterAPIRequest.GETRequestAPI("/api/element/".concat(idElement),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                mElement = new Element(response);
                                idUsuariAnunci = mElement.getUser();
                                obtenirUsuariAnunci(idUsuariAnunci);
                                loadElement(mElement);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                        }
                    }, headers);
        }

        imageViewFoto = (ImageView) fragmentViewElementView.findViewById(R.id.imageViewFoto);

        imatgeActual = 0;

        deshabilitarCamps();

        setHasOptionsMenu(true);


        editTextCategoria.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (i) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            myActivity.hideKeyboard();
                            return true;
                        default: break;
                    }
                }
                return false;
            }
        });

        return fragmentViewElementView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        myActivity.getMenuInflater().inflate(R.menu.menu_fragment_view_element, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        deleteElementItem = menu.findItem(R.id.menu_delete);
    }

    private void habilitarCamps(){
        EditText editTextTitol = (EditText) fragmentViewElementView.findViewById(R.id.titolAnunci);
        editTextTitol.setEnabled(true);
        EditText editTextDescripcio = (EditText) fragmentViewElementView.findViewById(R.id.editTextDescripcio);
        editTextDescripcio.setEnabled(true);
        editTextCategoria.setEnabled(true);
        editTextCategoria.setVisibility(View.VISIBLE);
        if (esTemporal) {
            editTextTemporalitat.setEnabled(true);
            editTextTemporalitat.setVisibility(View.VISIBLE);
        }
        textViewCategoria.setVisibility(View.VISIBLE);
        afegeixCategoria.setVisibility(View.VISIBLE);
        afegeixCategoria.setEnabled(true);
        editButton.setVisibility(View.GONE);
        editButton.setEnabled(false);
        saveButton.setVisibility(View.VISIBLE);
        saveButton.setEnabled(true);
    }

    private void deshabilitarCamps(){
        EditText editTextTitol = (EditText) fragmentViewElementView.findViewById(R.id.titolAnunci);
        editTextTitol.setEnabled(false);
        EditText editTextDescripcio = (EditText) fragmentViewElementView.findViewById(R.id.editTextDescripcio);
        editTextDescripcio.setEnabled(false);
        editTextCategoria.setEnabled(false);
        editTextCategoria.setVisibility(View.GONE);
        editTextTemporalitat.setEnabled(false);
        textViewCategoria.setVisibility(View.GONE);
        afegeixCategoria.setVisibility(View.GONE);
        afegeixCategoria.setEnabled(false);
        editButton.setVisibility(View.VISIBLE);
        editButton.setEnabled(true);
        saveButton.setVisibility(View.GONE);
        saveButton.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.botoEditar:
                habilitarCamps();
                botoEdicioClicat = true;

                ExpandableHeightGridView gridCategories = (ExpandableHeightGridView) fragmentViewElementView.findViewById(R.id.gridCategories);
                gridCategories.setExpanded(true);
                boolean editables = Objects.equals(idUsuariAnunci, us.getId()) && botoEdicioClicat;
                categoriesAdapter = new CategoriesViewElementAdapter(myActivity, R.layout.category_list_item, categories, this, editables);
                gridCategories.setAdapter(categoriesAdapter);
                categoriesAdapter.notifyDataSetChanged();

                break;
            case R.id.botoGuardar:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.save_changes_eng);
                builder.setIcon(R.drawable.ic_warning);
                builder.setMessage(R.string.are_you_sure_you_want_to_save_the_changes_eng);
                final EditText finalEditTextTitol = (EditText) fragmentViewElementView.findViewById(R.id.titolAnunci);
                final EditText finalEditTextDescripcio = (EditText) fragmentViewElementView.findViewById(R.id.editTextDescripcio);
                final EditText finalEditTextTemporalitat = (EditText) fragmentViewElementView.findViewById(R.id.temporalitat);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        boolean faltenCamps = false;
                        if (Objects.equals(finalEditTextTitol.getText().toString(), "")) faltenCamps = true;
                        else mElement.setTitol(finalEditTextTitol.getText().toString());
                        mElement.setDescripcio(finalEditTextDescripcio.getText().toString());
                        mElement.setTemporalitat(finalEditTextTemporalitat.getText().toString());
                        JSONObject elementModificat = new JSONObject();
                        try {
                            elementModificat.put("titol", mElement.getTitol());
                            elementModificat.put("descripcio", mElement.getDescripcio());
                            JSONObject esTemp = new JSONObject();
                            esTemp.put("temporalitat",mElement.getEsTemporal());
                            esTemp.put("periode",mElement.getTemporalitat());
                            elementModificat.put("es_temporal",esTemp);
                            JSONArray tags = obtenirJSONarrayTags(mElement.getTags());
                            elementModificat.put("tags", tags);
                            if (!faltenCamps) {
                                botoEdicioClicat = false;
                                actualitzarElement(elementModificat);
                            }else Toast.makeText(myActivity,R.string.the_advertisement_must_have_a_title_eng,Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (!faltenCamps) deshabilitarCamps();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                builder.show();
                break;
            case R.id.previousButton:
                if (imatgeActual > 0){
                    imatgeActual--;
                    Picasso picasso = new Picasso.Builder(myActivity)
                            .downloader(new OkHttp3Downloader(client))
                            .build();
                    picasso.load(imatges.get(imatgeActual)).into(imageViewFoto);
                }
                break;
            case R.id.nextButton:
                if (imatgeActual < (imatges.size()-1)){
                    imatgeActual++;
                    Picasso picasso = new Picasso.Builder(myActivity)
                            .downloader(new OkHttp3Downloader(client))
                            .build();
                    picasso.load(imatges.get(imatgeActual)).into(imageViewFoto);
                }
                break;
            case R.id.botoAfegirCategoria:
                String novaCategoria = editTextCategoria.getText().toString();
                if (novaCategoria.trim().length() != 0) {
                    boolean categoriaExistent = false;
                    int i = 0;
                    while (i < categories.size() && !categoriaExistent){
                        if (Objects.equals(categories.get(i), novaCategoria)) categoriaExistent = true;
                        ++i;
                    }
                    if (!categoriaExistent) {
                        categories.add(novaCategoria);
                        mElement.setTags(categories);
                        categoriesAdapter.notifyDataSetChanged();
                        editTextCategoria.setText("");
                    } else {
                        String errorExisting = getResources().getString(R.string.errorExistingCategory_eng);
                        editTextCategoria.setError(errorExisting);
                    }
                } else {
                    String errorEmpty = getResources().getString(R.string.errorEmptyField_eng);
                    editTextCategoria.setError(errorEmpty);
                }
                break;
            case R.id.writeComment:
                if (!Objects.equals(editTextWriteComment.getText().toString(), "")){
                    String comentari = editTextWriteComment.getText().toString();
                    JSONObject nouComentari = new JSONObject();
                    try {
                        nouComentari.put("text",comentari);
                        nouComentari.put("user_id",us.getId());
                        Date avui = new Date();
                        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        nouComentari.put("data", df1.format(avui));
                        publicarComentari(nouComentari);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    String errorCommentCanNotBeEmpty = getResources().getString(R.string.this_field_is_required_eng);
                    editTextWriteComment.setError(errorCommentCanNotBeEmpty);
                }
                break;
            case R.id.tradeButton:
                myActivity.changeToMakeOffer(idElement);
                break;
            default:
                break;
        }
    }

    private void loadElement(Element e){

        if (Objects.equals(idUsuariAnunci, us.getId()) && !esOffer) {
            editButton.setEnabled(true);
            tradeButton.setEnabled(false);
            tradeButton.setVisibility(View.GONE);
            if (deleteElementItem!=null){
                deleteElementItem.setEnabled(true);
                deleteElementItem.setVisible(true);
            }

        }else {
            editButton.setEnabled(false);
            editButton.setVisibility(View.GONE);
            tradeButton.setEnabled(true);
            tradeButton.setVisibility(View.VISIBLE);
        }

        EditText editTextTitol = (EditText) fragmentViewElementView.findViewById(R.id.titolAnunci);
        editTextTitol.setText(e.getTitol());
        EditText editTextDescripcio = (EditText) fragmentViewElementView.findViewById(R.id.editTextDescripcio);
        editTextDescripcio.setText(e.getDescripcio());

        categories = e.getTags();
        ExpandableHeightGridView gridCategories = (ExpandableHeightGridView) fragmentViewElementView.findViewById(R.id.gridCategories);
        gridCategories.setExpanded(true);
        boolean editables = Objects.equals(idUsuariAnunci, us.getId()) && botoEdicioClicat;
        categoriesAdapter = new CategoriesViewElementAdapter(myActivity, R.layout.category_list_item, categories, this, editables);
        gridCategories.setAdapter(categoriesAdapter);
        categoriesAdapter.notifyDataSetChanged();

        // TODO: al CommentListViewAdapter HE DAFEGIR LA FOTO DE L'USUARI
        comentaris = mElement.getComentaris();
        Collections.reverse(comentaris);
        ExpandableHeightGridView listViewComentaris = (ExpandableHeightGridView) fragmentViewElementView.findViewById(R.id.comments_list);
        listViewComentaris.setExpanded(true);
        CommentListViewAdapter adapter = new CommentListViewAdapter(myActivity, R.layout.comment_row_layout, comentaris, idElement);
        listViewComentaris.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        editTextWriteComment.setText("");

        esTemporal = mElement.getEsTemporal();
        if (esTemporal) {
            if (!Objects.equals(e.getTemporalitat(), "")){
                editTextTemporalitat.setText(e.getTemporalitat());
            }else{
                editTextTemporalitat.setText(R.string.edit_temporality_here_eng);
            }
        }else{
            TextView textViewDurada = (TextView) fragmentViewElementView.findViewById(R.id.textViewDurada);
            textViewDurada.setVisibility(View.GONE);
        }


        /*
        for (int i = 0; i < mElement.getFotografies().size(); ++i){

        }*/


        client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("x-access-token", us.getToken())
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();




        if (mElement.getFotografies().size()> 0) {
            for (int i = 0; i < mElement.getFotografies().size(); i++) {
                Uri uri = Uri.parse(BASE_URL + "/static/elements/" + mElement.getFotografies().get(i).toString().replace("uploads/elements/", ""));
                imatges.add(uri);
            }
        }

        if (imatges.size()> 0) {
            Picasso picasso = new Picasso.Builder(myActivity)
                    .downloader(new OkHttp3Downloader(client))
                    .build();
            picasso.load(imatges.get(imatgeActual)).into(imageViewFoto);
        }

    }

    private void obtenirUsuariAnunci(String id) {
        Map<String, String> headers = new HashMap<>();
        AdapterAPIRequest adapterAPIRequest = new AdapterAPIRequest();
        adapterAPIRequest.GETRequestAPI("/user/"+id,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("JSON: ",response.toString());
                            JSONArray var2 = response.getJSONArray("preferencies");
                            ArrayList<String> aux2 = new ArrayList<> ();
                            for (int j = 0; j < var2.length();++j) {
                                aux2.add(var2.get(j).toString());
                            }
                            User u = new User(response.getString("id"),
                                    response.getString("nom"),
                                    response.getString("localitat"),
                                    null,
                                    response.getInt("reputacio"),
                                    Uri.parse(response.getString("path")),
                                    aux2);
                            setUsuariAnunci(u);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                },
                headers
        );
    }

    private void setUsuariAnunci(User u) {
        TextView textViewCreatedBy = (TextView) fragmentViewElementView.findViewById(R.id.createdByTextView);
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dia = df1.format(mElement.getDataPublicacio());
        textViewCreatedBy.append(" " + u.getUsername() + "\n" +dia.replace(" "," - ") );
    }

    private void actualitzarElement(JSONObject elementModificat){
        AdapterAPIRequest adapterAPIRequest = new AdapterAPIRequest();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("x-access-token",us.getToken());
        String url = "/api/element/"+idElement;
        //adapter.PUTRequestAPI("http://104.236.98.100:3000/loginFB"
        adapterAPIRequest.PUTRequestAPI(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mElement = new Element(response);
                            loadElement(mElement);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG,error.getMessage());
                    }
                },elementModificat, headers);
    }

    private void publicarComentari(JSONObject nouComentari){
        AdapterAPIRequest adapterAPIRequest = new AdapterAPIRequest();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("x-access-token",us.getToken());
        String url = "/api/element/".concat(idElement).concat("/comment");
        //adapter.POSTRequestAPI("http://104.236.98.100:3000/loginFB"
        adapterAPIRequest.PUTRequestAPI(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mElement = new Element(response);
                            loadElement(mElement);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG,error.getMessage());
                    }
                },nouComentari, headers);
    }

    private JSONArray obtenirJSONarrayTags(ArrayList<String> tags){
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i < tags.size(); ++i){
            jsonArray.put(tags.get(i));
        }
        return jsonArray;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_delete:
                AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(myActivity);
                deleteBuilder.setTitle(R.string.delete_item_eng);
                deleteBuilder.setIcon(R.drawable.ic_warning);
                deleteBuilder.setMessage(R.string.are_you_sure_you_want_delete_this_item_eng);
                deleteBuilder.setPositiveButton(R.string.ok_eng, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AdapterAPIRequest adapterAPIRequest = new AdapterAPIRequest();
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        headers.put("x-access-token",us.getToken());
                        String url = "/api/element/".concat(idElement);
                        //adapter.POSTRequestAPI("http://104.236.98.100:3000/loginFB"
                        adapterAPIRequest.DELETERequestAPI(url,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        myActivity.changeToYourItems();
                                    }
                                },new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        VolleyLog.d(TAG,error.getMessage());
                                    }
                                }, headers);
                    }
                });
                deleteBuilder.setNegativeButton(R.string.cancelProfile_eng, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                deleteBuilder.show();
                break;
            case R.id.menu_report:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();

                final View mView = inflater.inflate(R.layout.report_dialog, null);
                builder.setView(mView);

                final RadioGroup radioGroup = (RadioGroup) mView.findViewById(R.id.report_radio_group);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        int checkedId = radioGroup.getCheckedRadioButtonId();
                        RadioButton radioButton = (RadioButton) mView.findViewById(checkedId);
                        //TODO: procedir amb la denuncia
                        String tipusDenuncia = radioButton.getText().toString();

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                builder.show();
                break;
            case R.id.menu_help:
                //TODO: informacio sobre casos en que es pot denunciar
                break;
            default:break;
        }
        return true;
    }

    public void actualitzarCategories(ArrayList<String> data) {
        categories = data;
        mElement.setTags(categories);
    }
}