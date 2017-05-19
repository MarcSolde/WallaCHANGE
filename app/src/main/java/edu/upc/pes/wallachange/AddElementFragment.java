package edu.upc.pes.wallachange;

import static com.android.volley.VolleyLog.TAG;

import static edu.upc.pes.wallachange.R.id.editTextCategoria;
import static edu.upc.pes.wallachange.R.id.editTextDescripcio;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.upc.pes.wallachange.APILayer.AdapterAPIRequest;
import edu.upc.pes.wallachange.Adapters.ImatgesMiniaturaListViewAdapter;
import edu.upc.pes.wallachange.LoginSystem.CallbackFacebook;
import edu.upc.pes.wallachange.Models.Comment;
import edu.upc.pes.wallachange.Models.Element;
import edu.upc.pes.wallachange.Others.ExpandableHeightGridView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddElementFragment extends Fragment implements View.OnClickListener {

    private final int PICK_IMAGE = 1;
    private MainActivity myActivity;
    private ArrayList<Uri> imatgesMiniatura;
    private Integer nombreImatges;
    private ImageButton botoFotografia;
    private EditText editTextCategoria, editTextDescripcio, editTextTitol, editTextTemporalitat;
    private ExpandableHeightGridView miniatureImagesExpandableGridView;
    private RadioGroup radioGroupTipusProducte, radioGroupTipusIntercanvi;

    public AddElementFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View fragmentAddElementView = inflater.inflate(R.layout.fragment_add_element, container,
                false);
        myActivity = (MainActivity) getActivity();
        myActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myActivity.setTitle(R.string.navigationNewItem_eng);
        nombreImatges = 0;
        imatgesMiniatura = new ArrayList<>();
        obtenirPermisos();

        Button botoAfegirElement = (Button) fragmentAddElementView.findViewById(R.id.afegirElement);
        botoAfegirElement.setOnClickListener(this);
        Button botoCancelarElement = (Button) fragmentAddElementView.findViewById(R.id.cancelarElement);
        botoCancelarElement.setOnClickListener(this);
        botoFotografia = (ImageButton) fragmentAddElementView.findViewById(R.id.afegirImatge);
        botoFotografia.setOnClickListener(this);

        ImageView netejaTitol = (ImageView) fragmentAddElementView.findViewById(R.id.neteja_edittext_titol);
        netejaTitol.setOnClickListener(this);
        ImageView netejaDescripcio = (ImageView) fragmentAddElementView.findViewById(R.id.neteja_edittext_descripcio);
        netejaDescripcio.setOnClickListener(this);
        ImageView netejaCategoria = (ImageView) fragmentAddElementView.findViewById(R.id.neteja_edittext_categoria);
        netejaCategoria.setOnClickListener(this);
        final ImageView netejaTemporalitat = (ImageView) fragmentAddElementView.findViewById(R.id.neteja_edittext_temporalitat);
        netejaTemporalitat.setOnClickListener(this);

        editTextTemporalitat = (EditText) fragmentAddElementView.findViewById(R.id.temporalitat);
        editTextTitol = (EditText) fragmentAddElementView.findViewById(R.id.titolAnunci);
        editTextDescripcio = (EditText) fragmentAddElementView.findViewById(R.id.descripcio);
        editTextCategoria = (EditText) fragmentAddElementView.findViewById(R.id.categoria);

        radioGroupTipusProducte = (RadioGroup) fragmentAddElementView.findViewById(R.id.radioGroupTipusProducte);
        radioGroupTipusIntercanvi = (RadioGroup) fragmentAddElementView.findViewById(R.id.radioGroupTipusIntercanvi);
        miniatureImagesExpandableGridView = (ExpandableHeightGridView) fragmentAddElementView.findViewById(R.id.gridViewImatgesMiniatura);
        miniatureImagesExpandableGridView.setExpanded(true);

        final TextView textViewDurada = (TextView) fragmentAddElementView.findViewById(R.id.textViewDurada);
        radioGroupTipusIntercanvi.setOnCheckedChangeListener(  new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radioButtonTemporal:
                        editTextTemporalitat.setVisibility(View.VISIBLE);
                        textViewDurada.setVisibility(View.VISIBLE);
                        netejaTemporalitat.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioButtonPermanent:
                        editTextTemporalitat.setVisibility(View.GONE);
                        textViewDurada.setVisibility(View.GONE);
                        netejaTemporalitat.setVisibility(View.GONE);
                        break;
                }
            }
        });
        return fragmentAddElementView;
    }


    private Element obtenirElement(String id) {
        //TODO
        return null;
    }

    private void publicarElement(String titol, String descripcio, String categoria, String tipusProducte,
            Boolean temporal, String temporalitat, String username, ArrayList<Uri> imatgesMiniatura,
            ArrayList<Comment> comentaris, String localitat) {
        AdapterAPIRequest adapterAPIRequest = new AdapterAPIRequest();

        //docker run
        //i node
        //node server.js
        Map<String, String> nouElement = new HashMap<>();
            nouElement.put("titol",titol);
            nouElement.put("descripcio",descripcio);
            nouElement.put("imatges",null);
            nouElement.put("nom_user",username);
            //jSO.put("data_publicacio",date); al json del ruteo que te veo posa que es DATE
            nouElement.put("tipus_element",tipusProducte);
            nouElement.put("es_temporal",temporal.toString());
            nouElement.put("tags",categoria);
            // aqui s'hauria de convertir larraylist de comentaris a vector de parelles textComentari, nom_usuari
            nouElement.put("comentaris",null);
            nouElement.put("localitat",localitat);
        // params ara sera un JSON

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("token","EAAEtuINTdk8BAKjYGNPoN5VwMsaoyoyPY2SfhFrH13mKABxtYxTWpv4l7KYcZAVACcdRx1TN5H4XdagAn7buk5QklMXxjMg0ZCQnq2Qc0Oi8TopSy23iRMZA3I4p56yNG0DJZA0VJJUxdAIj1edGRnCI2Hz1h0T7CTsGyBAnNqoZChjQQSq7cHsQQMhJFT6gZD"); //http://10.0.2.2:3000/element

        adapterAPIRequest.POSTSJsonObjectRequestAPI("http://10.0.2.2:3000/loginFB",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            myActivity.changeToItem(response.getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG,"Error ");
                    }
                },nouElement, headers);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            nombreImatges++;
            if (nombreImatges == 5){
                botoFotografia.setClickable(false);
            }
            Uri uri = data.getData();
            imatgesMiniatura.add(uri);
            ImatgesMiniaturaListViewAdapter adapter2 = new ImatgesMiniaturaListViewAdapter(myActivity, R.layout.miniature_images_item, imatgesMiniatura,this);
            miniatureImagesExpandableGridView.setAdapter(adapter2);
            adapter2.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.afegirElement:
                boolean b = faltenCamps();
                if (!b){
                    //TODO:
                    String tipusProducte = obtenirTipusProducte();
                    String tipusIntercanvi = obtenirTipusIntercanvi();
                    Boolean esTemporal = (Objects.equals(tipusIntercanvi, getResources().getString(R.string.temporal_eng)));
                    // 1 POST
                    String localitat = obtenirLocalitatUsuari(myActivity.getUsername());
                    publicarElement(editTextTitol.getText().toString(),editTextDescripcio.getText().toString(),editTextCategoria.getText().toString(),tipusProducte,esTemporal,editTextTemporalitat.getText().toString(),myActivity.getUsername(),imatgesMiniatura,new ArrayList<Comment>(),localitat);
                    // 2 GET http://localhost:3000/element/:id
                    Toast.makeText(myActivity,"tots els camps necessaris",Toast.LENGTH_LONG).show();
                    // 3 construir element amb el que retorna el GET
                    // 4 Mostrar element just creat
                    //myActivity.changeToItem(id);
                }
                break;
            case R.id.cancelarElement:
                myActivity.changeFragmentToHome();
                break;
            case R.id.afegirImatge:
                Intent pickIntent;
                if (Build.VERSION.SDK_INT >= 19) {
                    pickIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                } else pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pickIntent.setType("image/*");
                pickIntent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(pickIntent, "Import img"), PICK_IMAGE);
                break;
            case R.id.neteja_edittext_titol:
                editTextTitol.setText("");
                break;
            case R.id.neteja_edittext_descripcio:
                editTextDescripcio.setText("");
                break;
            case R.id.neteja_edittext_categoria:
                editTextCategoria.setText("");
                break;
            case R.id.neteja_edittext_temporalitat:
                editTextTemporalitat.setText("");
                break;
            default:
                break;
        }
    }

    private String obtenirLocalitatUsuari(String username) {
        //TODO
        return null;
    }

    private String obtenirTipusIntercanvi() {
        String tipusIntercanvi = getResources().getString(R.string.permanent_eng);
        if(radioGroupTipusIntercanvi.getCheckedRadioButtonId()!=-1){
            int id= radioGroupTipusIntercanvi.getCheckedRadioButtonId();
            View radioButton = radioGroupTipusIntercanvi.findViewById(id);
            int radioId = radioGroupTipusIntercanvi.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) radioGroupTipusIntercanvi.getChildAt(radioId);
            tipusIntercanvi = (String) btn.getText();
        }
        return tipusIntercanvi;
    }

    private String obtenirTipusProducte() {
        String tipusProducte = getResources().getString(R.string.product_eng);
        if(radioGroupTipusProducte.getCheckedRadioButtonId()!=-1){
            int id= radioGroupTipusProducte.getCheckedRadioButtonId();
            View radioButton = radioGroupTipusProducte.findViewById(id);
            int radioId = radioGroupTipusProducte.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) radioGroupTipusProducte.getChildAt(radioId);
            tipusProducte = (String) btn.getText();
        }
        return tipusProducte;
    }

    private boolean faltenCamps() {
        // titol de l'anunci obligatori
        boolean falten = false;
        if (Objects.equals(editTextTitol.getText().toString(), "")){
            falten = true;
            String errorTitleMustBeFilled = getResources().getString(R.string.this_field_is_required_eng);
            editTextTitol.setError(errorTitleMustBeFilled);
        }
        String tp = obtenirTipusProducte();
        // minim 1 imatge per producte
        if (nombreImatges == 0 && Objects.equals(tp, getResources().getString(R.string.product_eng))){
            falten = true;
        }
        return falten;
    }

    private void obtenirPermisos(){
        int permission;
        if (Build.VERSION.SDK_INT >= 23) {
            permission = myActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                int REQUEST_IMAGE = 123;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_IMAGE);
            }
        }
    }

    public void actualitzarNombreImatges(List<Uri> imatges) {
        if (nombreImatges == 5) botoFotografia.setClickable(true);
        nombreImatges= imatges.size();
        imatgesMiniatura = (ArrayList<Uri>) imatges;
    }
}
