package edu.upc.pes.wallachange;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddElement extends Fragment implements View.OnClickListener {

    private int PICK_IMAGE = 1, REQUEST_IMAGE = 123;
    private GridLayout img;
    private MainActivity myActivity;
    private GridView miniatureImagesGridView;
    private View miniatureImagesView, fragmentAddElementView;
    private ArrayList<Uri> imatgesMiniatura;
    private Integer nombreImatges;
    private ImageButton botoFotografia, botoAfegirElement;
    private String tipusProducte, tipusIntercanvi;

    public FragmentAddElement() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        fragmentAddElementView =  inflater.inflate(R.layout.fragment_add_element,container, false);
        myActivity = (MainActivity) getActivity();
        myActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myActivity.setTitle(R.string.navigationNewItem_eng);
        img = (GridLayout) fragmentAddElementView.findViewById(R.id.imatgesMiniatura);
        miniatureImagesView = inflater.inflate(R.layout.grid_view_miniature_images,null);
        nombreImatges = 0;
        imatgesMiniatura = new ArrayList<>();
        tipusProducte = getResources().getString(R.string.product_eng);
        tipusIntercanvi = getResources().getString(R.string.permanent_eng);

        obtenirPermisos();

        botoFotografia = (ImageButton) fragmentAddElementView.findViewById(R.id.afegirImatge);
        botoFotografia.setOnClickListener(this);
        botoAfegirElement = (ImageButton) fragmentAddElementView.findViewById(R.id.afegirElement);
        botoAfegirElement.setOnClickListener(this);

        final EditText textTemporalitat = (EditText) fragmentAddElementView.findViewById(R.id.temporalitat);
        final TextView textViewDurada = (TextView) fragmentAddElementView.findViewById(R.id.textViewDurada);
        final RadioGroup radioGroupTipusIntercanvi = (RadioGroup) fragmentAddElementView.findViewById(R.id.radioGroupTipusIntercanvi);
        radioGroupTipusIntercanvi.setOnCheckedChangeListener(  new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radioButtonTemporal:
                        textTemporalitat.setVisibility(View.VISIBLE);
                        textViewDurada.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioButtonPermanent:
                        textTemporalitat.setVisibility(View.GONE);
                        textViewDurada.setVisibility(View.GONE);
                        break;
                }
            }
        });
        return fragmentAddElementView;
    }

    public Element generarNouElement(){
        Element element;
        EditText editTextTitolAnunci = (EditText) fragmentAddElementView.findViewById(R.id.titolAnunci);
        EditText editTextDescripcio = (EditText) fragmentAddElementView.findViewById(R.id.descripcio);
        EditText editTextCategoria = (EditText) fragmentAddElementView.findViewById(R.id.categoria);
        EditText editTextTemporalitat = (EditText) fragmentAddElementView.findViewById(R.id.temporalitat);
        RadioGroup radioGroupTipusProducte = (RadioGroup) fragmentAddElementView.findViewById(R.id.radioGroupTipusProducte);
        RadioGroup radioGroupTipusIntercanvi = (RadioGroup) fragmentAddElementView.findViewById(R.id.radioGroupTipusIntercanvi);

        if(radioGroupTipusProducte.getCheckedRadioButtonId()!=-1){
            int id= radioGroupTipusProducte.getCheckedRadioButtonId();
            View radioButton = radioGroupTipusProducte.findViewById(id);
            int radioId = radioGroupTipusProducte.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) radioGroupTipusProducte.getChildAt(radioId);
            tipusProducte = (String) btn.getText();
        }
        if(radioGroupTipusIntercanvi.getCheckedRadioButtonId()!=-1){
            int id= radioGroupTipusIntercanvi.getCheckedRadioButtonId();
            View radioButton = radioGroupTipusIntercanvi.findViewById(id);
            int radioId = radioGroupTipusIntercanvi.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) radioGroupTipusIntercanvi.getChildAt(radioId);
            tipusIntercanvi = (String) btn.getText();
        }
        element = new Element(editTextTitolAnunci.getText().toString(),editTextDescripcio.getText().toString(),editTextCategoria.getText().toString(),tipusProducte,tipusIntercanvi,editTextTemporalitat.getText().toString(),imatgesMiniatura);

        return element;
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
            miniatureImagesGridView = (GridView) miniatureImagesView.findViewById(R.id.gridViewImatgesMiniatura);
            ImatgesMiniaturaListViewAdapter adapter2 = new ImatgesMiniaturaListViewAdapter(myActivity, R.layout.miniature_images_item, imatgesMiniatura,this);
            miniatureImagesGridView.setAdapter(adapter2);
            img.removeAllViews();
            img.addView(miniatureImagesView);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.afegirElement:
                Element e = generarNouElement();
                // CONTROL ERRORS
                // titol de l'anunci obligatori
                boolean faltenCamps = false;
                String campsObligatoris = getResources().getString(R.string.these_fields_must_be_filled_eng);
                if (Objects.equals(e.getTitol(), "")){
                    campsObligatoris += System.getProperty("line.separator") + getResources().getString(R.string.advertisement_title_eng);
                    faltenCamps = true;
                }
                // minim 1 imatge per producte
                if (nombreImatges == 0 && Objects.equals(tipusProducte, getResources().getString(R.string.product_eng))){
                    campsObligatoris += System.getProperty("line.separator") + getResources().getString(R.string.at_least_one_image_eng);
                    faltenCamps = true;
                }

                if (faltenCamps){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage(campsObligatoris);
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    // tot ha anat bé
                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
                }
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
        }
    }

    public void obtenirPermisos(){
        int permission;
        if (Build.VERSION.SDK_INT >= 23) {
            permission = myActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE);
            }
        }
    }

    public void actualitzarNombreImatges(List<Uri> imatges) {
        if (nombreImatges == 5) botoFotografia.setClickable(true);
        nombreImatges= imatges.size();
        imatgesMiniatura = (ArrayList<Uri>) imatges;
    }
}
