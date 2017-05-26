package edu.upc.pes.wallachange;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import edu.upc.pes.wallachange.APILayer.AdapterAPIRequest;
import edu.upc.pes.wallachange.Adapters.CategoriesAdapter;
import edu.upc.pes.wallachange.Adapters.ImatgesMiniaturaListViewAdapter;
import edu.upc.pes.wallachange.Models.Comment;
import edu.upc.pes.wallachange.Models.CurrentUser;
import edu.upc.pes.wallachange.Models.Element;
import edu.upc.pes.wallachange.Others.ExpandableHeightGridView;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddElementFragment extends Fragment implements View.OnClickListener {

    private final int PICK_IMAGE = 1;
    private MainActivity myActivity;
    private ArrayList<Uri> imatgesMiniatura;
    private ArrayList<Bitmap> imatgesMiniaturaEnBitmap;
    private Integer nombreImatges;
    private ImageButton botoFotografia;
    private EditText editTextCategoria, editTextDescripcio, editTextTitol, editTextTemporalitat;
    private ExpandableHeightGridView miniatureImagesExpandableGridView;
    private RadioGroup radioGroupTipusProducte, radioGroupTipusIntercanvi;
    private CurrentUser currentUser;
    private ArrayList<String> categories;
    private CategoriesAdapter categoriesAdapter;

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
        imatgesMiniaturaEnBitmap = new ArrayList<>();
        currentUser = CurrentUser.getInstance();
        obtenirPermisos();
        categories = new ArrayList<>();

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
        final ImageView netejaTemporalitat = (ImageView) fragmentAddElementView.findViewById(R.id.neteja_edittext_temporalitat);
        netejaTemporalitat.setOnClickListener(this);
        ImageView afegeixCategoria = (ImageView) fragmentAddElementView.findViewById(R.id.botoAfegirCategoria);
        afegeixCategoria.setOnClickListener(this);

        ExpandableHeightGridView gridCategories =
                (ExpandableHeightGridView) fragmentAddElementView.findViewById(R.id.gridCategories);
        gridCategories.setExpanded(true);
        categoriesAdapter = new CategoriesAdapter(myActivity, R.layout.category_list_item, categories, this);
        gridCategories.setAdapter(categoriesAdapter);
        categoriesAdapter.notifyDataSetChanged();

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


    private void publicarElement(String titol, String descripcio, ArrayList<String> categories, String tipusProducte,
            Boolean temporal, String temporalitat, String username, ArrayList<Uri> imatgesMiniatura,
            ArrayList<Comment> comentaris, String localitat) {
        AdapterAPIRequest adapterAPIRequest = new AdapterAPIRequest();

        JSONObject nouElement = new JSONObject();
        try {
            nouElement.put("titol",titol);
            nouElement.put("descripcio",descripcio);
            //nouElement.put("imatges",null);
            //nouElement.put("nom_user",username);

            //DateFormat df = DateFormat.getTimeInstance();
            //1df.setTimeZone(TimeZone.getTimeZone("UTC"));
            //String avui = df.format(new Date());

            //nouElement.put("data_publicacio",avui);
            nouElement.put("tipus_element",tipusProducte);
            nouElement.put("es_temporal",temporal);
            JSONArray tags = obtenirJSONarrayTags(categories);
            nouElement.put("tags",tags);
            int i = 0;
            //JSONArray coments = null;
            //nouElement.put("comentaris",coments);
            //nouElement.put("localitat",localitat);
            // params ara sera un JSON
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("x-access-token",currentUser.getToken());
        String url = "http://10.0.2.2:3000/".concat("api/element");
        //adapter.POSTRequestAPI("http://104.236.98.100:3000/loginFB"
        adapterAPIRequest.POSTRequestAPI(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //TODO: LOAD imagenes
                        try {
                            myActivity.changeToItem(response.getString("_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG,error.getMessage());
                    }
                },nouElement, headers);
    }

    private JSONArray obtenirJSONarrayComentaris(ArrayList<Comment> comentaris){
        //TODO aqui s'hauria de convertir larraylist de comentaris a vector de parelles textComentari, nom_usuari

        return null;
    }

    private JSONArray obtenirJSONarrayTags(ArrayList<String> tags){
        //TODO
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i < tags.size(); ++i){
            jsonArray.put(tags.get(i));
        }
        return jsonArray;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null
                && data.getData() != null) {
            nombreImatges++;
            if (nombreImatges == 5) {
                botoFotografia.setClickable(false);
            }
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(myActivity.getContentResolver(), uri);
                imatgesMiniaturaEnBitmap.add(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imatgesMiniatura.add(uri);
            ImatgesMiniaturaListViewAdapter adapter2 = new ImatgesMiniaturaListViewAdapter(
                    myActivity, R.layout.miniature_images_item, imatgesMiniatura, imatgesMiniaturaEnBitmap, this);
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
                    String localitat = obtenirLocalitatUsuari(myActivity.getUsername());
                    //crida POST
                    publicarElement(editTextTitol.getText().toString(),editTextDescripcio.getText().toString(),
                            categories,
                            tipusProducte,esTemporal,editTextTemporalitat.getText().toString(),
                            myActivity.getUsername(),imatgesMiniatura,new ArrayList<Comment>(),
                            localitat);
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
            case R.id.neteja_edittext_temporalitat:
                editTextTemporalitat.setText("");
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

    public void actualitzarNombreImatges(List<Uri> imatges, List<Bitmap> imatgesBitmap) {
        if (nombreImatges == 5) botoFotografia.setClickable(true);
        nombreImatges= imatges.size();
        imatgesMiniatura = (ArrayList<Uri>) imatges;
        imatgesMiniaturaEnBitmap = (ArrayList<Bitmap>) imatgesBitmap;
    }

    public void actualitzarCategories (ArrayList<String> catgs) {
        categories = catgs;
    }

}
