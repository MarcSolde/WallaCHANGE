package edu.upc.pes.wallachange;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.upc.pes.wallachange.Adapters.ImatgesMiniaturaListViewAdapter;
import edu.upc.pes.wallachange.Models.Element;
import edu.upc.pes.wallachange.Others.ExpandableHeightGridView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddElementFragment extends Fragment implements View.OnClickListener {

    private final int PICK_IMAGE = 1;
    private MainActivity myActivity;
    private View fragmentAddElementView;
    private ArrayList<Uri> imatgesMiniatura;
    private Integer nombreImatges;
    private ImageButton botoFotografia;
    private String tipusProducte, tipusIntercanvi;
    private EditText textTemporalitat, textCategoria, textDescripcio, textTitol;
    private ImageView netejaTemporalitat;
    private ExpandableHeightGridView miniatureImagesExpandableGridView;

    public AddElementFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        fragmentAddElementView =  inflater.inflate(R.layout.fragment_add_element,container, false);
        myActivity = (MainActivity) getActivity();
        myActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myActivity.setTitle(R.string.navigationNewItem_eng);
        nombreImatges = 0;
        imatgesMiniatura = new ArrayList<>();
        tipusProducte = getResources().getString(R.string.product_eng);
        tipusIntercanvi = getResources().getString(R.string.permanent_eng);

        obtenirPermisos();

        botoFotografia = (ImageButton) fragmentAddElementView.findViewById(R.id.afegirImatge);
        botoFotografia.setOnClickListener(this);
        Button botoAfegirElement = (Button) fragmentAddElementView.findViewById(R.id.afegirElement);
        botoAfegirElement.setOnClickListener(this);
        Button botoCancelarElement = (Button) fragmentAddElementView.findViewById(
                R.id.cancelarElement);
        botoCancelarElement.setOnClickListener(this);

        miniatureImagesExpandableGridView = (ExpandableHeightGridView) fragmentAddElementView.findViewById(R.id.gridViewImatgesMiniatura);
        miniatureImagesExpandableGridView.setExpanded(true);

        ImageView netejaTitol = (ImageView) fragmentAddElementView.findViewById(
                R.id.neteja_edittext_titol);
        netejaTitol.setOnClickListener(this);
        ImageView netejaDescripcio = (ImageView) fragmentAddElementView.findViewById(
                R.id.neteja_edittext_descripcio);
        netejaDescripcio.setOnClickListener(this);
        ImageView netejaCategoria = (ImageView) fragmentAddElementView.findViewById(
                R.id.neteja_edittext_categoria);
        netejaCategoria.setOnClickListener(this);
        netejaTemporalitat = (ImageView) fragmentAddElementView.findViewById(R.id.neteja_edittext_temporalitat);
        netejaTemporalitat.setOnClickListener(this);
        textTitol = (EditText) fragmentAddElementView.findViewById(R.id.titolAnunci);
        textDescripcio = (EditText) fragmentAddElementView.findViewById(R.id.descripcio);
        textCategoria = (EditText) fragmentAddElementView.findViewById(R.id.categoria);
        textTemporalitat = (EditText) fragmentAddElementView.findViewById(R.id.temporalitat);

        final TextView textViewDurada = (TextView) fragmentAddElementView.findViewById(R.id.textViewDurada);
        final RadioGroup radioGroupTipusIntercanvi = (RadioGroup) fragmentAddElementView.findViewById(R.id.radioGroupTipusIntercanvi);
        radioGroupTipusIntercanvi.setOnCheckedChangeListener(  new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radioButtonTemporal:
                        textTemporalitat.setVisibility(View.VISIBLE);
                        textViewDurada.setVisibility(View.VISIBLE);
                        netejaTemporalitat.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioButtonPermanent:
                        textTemporalitat.setVisibility(View.GONE);
                        textViewDurada.setVisibility(View.GONE);
                        netejaTemporalitat.setVisibility(View.GONE);
                        break;
                }
            }
        });
        return fragmentAddElementView;
    }

    private Element generarNouElement(){
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
        //TODO:
        element = new Element("1",editTextTitolAnunci.getText().toString(),editTextDescripcio.getText().toString(),editTextCategoria.getText().toString(),tipusProducte,tipusIntercanvi,editTextTemporalitat.getText().toString(),myActivity.getUsername(),imatgesMiniatura);

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
            ImatgesMiniaturaListViewAdapter adapter2 = new ImatgesMiniaturaListViewAdapter(myActivity, R.layout.miniature_images_item, imatgesMiniatura,this);
            miniatureImagesExpandableGridView.setAdapter(adapter2);
            adapter2.notifyDataSetChanged();

            //img.removeAllViews();
            //img.addView(miniatureImagesView);
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
                if (Objects.equals(e.getTitol(), "")){
                    faltenCamps = true;
                    String errorTitleMustBeFilled = getResources().getString(R.string.this_field_is_required_eng);
                    textTitol.setError(errorTitleMustBeFilled);
                }
                // minim 1 imatge per producte
                if (nombreImatges == 0 && Objects.equals(tipusProducte, getResources().getString(R.string.product_eng))){
                    faltenCamps = true;
                }

                if (!faltenCamps){
                    // tot ha anat bé
                    myActivity.changeToItem(e);
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
                textTitol.setText("");
                break;
            case R.id.neteja_edittext_descripcio:
                textDescripcio.setText("");
                break;
            case R.id.neteja_edittext_categoria:
                textCategoria.setText("");
                break;
            case R.id.neteja_edittext_temporalitat:
                textTemporalitat.setText("");
                break;
            default:
                break;
        }
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
