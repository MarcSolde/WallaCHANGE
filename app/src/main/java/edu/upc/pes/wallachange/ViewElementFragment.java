package edu.upc.pes.wallachange;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import edu.upc.pes.wallachange.Adapters.CommentListViewAdapter;
import edu.upc.pes.wallachange.Models.Comment;
import edu.upc.pes.wallachange.Others.ExpandableHeightGridView;

public class ViewElementFragment extends Fragment implements View.OnClickListener{

    private View fragmentViewElementView;
    private MainActivity myActivity;
    private Integer imatgeActual;
    private ArrayList<Parcelable> uris;
    private ImageView imatge;
    private EditText editTextWriteComment;
    private ArrayList<Comment> comentaris;

    public ViewElementFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        fragmentViewElementView = inflater.inflate(R.layout.fragment_view_element, container, false);
        myActivity = (MainActivity) getActivity();
        myActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle bundle = getArguments();
        myActivity.setTitle(R.string.advertisements_view);

        ImageButton previousPictureButton = (ImageButton) fragmentViewElementView.findViewById(
                R.id.previousButton);
        previousPictureButton.setOnClickListener(this);
        ImageButton nextPictureButton = (ImageButton) fragmentViewElementView.findViewById(
                R.id.nextButton);
        nextPictureButton.setOnClickListener(this);
        ImageButton writeCommentButton = (ImageButton) fragmentViewElementView.findViewById(
                R.id.writeComment);
        writeCommentButton.setOnClickListener(this);
        Button tradeButton = (Button) fragmentViewElementView.findViewById(R.id.tradeButton);
        tradeButton.setOnClickListener(this);
        editTextWriteComment = (EditText) fragmentViewElementView.findViewById(R.id.editTextComment);

        comentaris = new ArrayList<>();

        EditText editTextTitol = (EditText) fragmentViewElementView.findViewById(R.id.titolAnunci);
        editTextTitol.setText(bundle.getString("titol"));
        editTextTitol.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    // TODO: fer un update del nou titol
                }
            }
        });

        final EditText editTextDescripcio = (EditText) fragmentViewElementView.findViewById(R.id.editTextDescripcio);
        String descripcio = bundle.getString("descripcio");
        editTextDescripcio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (!Objects.equals(editTextDescripcio.getText().toString(), getResources().getString(R.string.edit_description_here_eng))){
                        // TODO: fer update de la nova descripcio si es diferent al text "descriptiu" quan és buida

                    }
                }
            }
        });

        final EditText editTextCategoria = (EditText) fragmentViewElementView.findViewById(R.id.editTextCategoria);
        String categoria = bundle.getString("categoria");
        editTextCategoria.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!Objects.equals(editTextCategoria.getText().toString(),
                            getResources().getString(R.string.edit_category_here_eng))) {
                        // TODO: fer update de la nova categoria si es diferent al text "descriptiu" quan és buida
                    }
                }
            }
        });

        final EditText editTextTemporalitat = (EditText) fragmentViewElementView.findViewById(R.id.temporalitat);
        String temporalitat = bundle.getString("temporalitat");
        editTextTemporalitat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (!Objects.equals(editTextTemporalitat.getText().toString(),
                            getResources().getString(R.string.edit_temporality_here_eng))) {
                        // TODO: fer update de la nova temporalitat si es diferent al text "descriptiu" quan és buida
                    }
                }
            }
        });

        Boolean esTemporal = bundle.getBoolean("tipusIntercanvi");
        String usuariActual = myActivity.getUsername();
        String usuariAnunci = bundle.getString("usuari");
        if (Objects.equals(usuariAnunci, usuariActual)) {
            editTextTitol.setEnabled(true);
            editTextDescripcio.setEnabled(true);
            editTextCategoria.setEnabled(true);
            tradeButton.setEnabled(false);

            if (!Objects.equals(descripcio, "")) editTextDescripcio.setText(descripcio);
            else editTextDescripcio.setText(getResources().getString(R.string.edit_description_here_eng));

            if (!Objects.equals(categoria, "")) editTextCategoria.setText(categoria);
            else editTextCategoria.setText(getResources().getString(R.string.edit_category_here_eng));

            if (esTemporal) {
                if (!Objects.equals(temporalitat, "")) editTextTemporalitat.setText(temporalitat);
                else editTextTemporalitat.setText(
                        getResources().getString(R.string.edit_temporality_here_eng));
            }
        }else {
            editTextTitol.setEnabled(false);
            editTextDescripcio.setEnabled(false);
            editTextCategoria.setEnabled(false);
            tradeButton.setEnabled(true);

            if (!Objects.equals(descripcio, "")) editTextDescripcio.setText(descripcio);
            else editTextDescripcio.setText(getResources().getString(R.string.empty_description_eng));

            if (!Objects.equals(categoria, "")) editTextCategoria.setText(categoria);
            else editTextCategoria.setText(getResources().getString(R.string.empty_category_eng));

            if (esTemporal) {
                if (!Objects.equals(temporalitat, "")) editTextTemporalitat.setText(temporalitat);
                else editTextTemporalitat.setText(
                        getResources().getString(R.string.empty_temporality_eng));
            }
        }

        TextView textViewCreatedBy = (TextView) fragmentViewElementView.findViewById(R.id.createdByTextView);
        String createdBy = textViewCreatedBy.getText().toString();
        createdBy += "\n" + usuariAnunci;
        textViewCreatedBy.setText(createdBy);

        imatgeActual = 0;
        imatge = (ImageView) fragmentViewElementView.findViewById(R.id.imageViewFotoElement);
        uris = bundle.getParcelableArrayList("fotografies");
        if (uris != null ){
            if (uris.size()> 0) {
                Uri u = (Uri) uris.get(imatgeActual);
                Picasso.with(myActivity).load(u).into(imatge);
            }
        }

        setHasOptionsMenu(true);

        return fragmentViewElementView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        myActivity.getMenuInflater().inflate(R.menu.menu_fragment_view_element, menu);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.previousButton:
                if (imatgeActual > 0){
                    imatgeActual--;
                    Uri u = (Uri)uris.get(imatgeActual);
                    Picasso.with(myActivity).load(u).into(imatge);
                }
                break;
            case R.id.nextButton:
                if (imatgeActual < (uris.size()-1)){
                    imatgeActual++;
                    Uri u = (Uri)uris.get(imatgeActual);
                    Picasso.with(myActivity).load(u).into(imatge);
                }
                break;
            case R.id.writeComment:
                if (!Objects.equals(editTextWriteComment.getText().toString(), "")){
                    String comentari = editTextWriteComment.getText().toString();
                    Uri path = Uri.parse("android.resource://edu.upc.pes.wallachange/" + R.drawable.userpicture);


                    Date date = new Date();
                    String data = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("es","ES")).format(date);
                    data += "  " + DateFormat.getTimeInstance(DateFormat.MEDIUM, new Locale("es","ES")).format(date);
                    String nomUsuari = myActivity.getUsername();
                    Comment nouComentari = new Comment(path,nomUsuari,comentari,data);
                    comentaris.add(0,nouComentari);

                    ExpandableHeightGridView listViewComentaris = (ExpandableHeightGridView) fragmentViewElementView.findViewById(R.id.comments_list);
                    listViewComentaris.setExpanded(true);
                    CommentListViewAdapter adapter = new CommentListViewAdapter(myActivity, R.layout.comment_row_layout, comentaris);
                    listViewComentaris.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    editTextWriteComment.setText("");
                }else{
                    String errorCommentCanNotBeEmpty = getResources().getString(R.string.this_field_is_required_eng);
                    editTextWriteComment.setError(errorCommentCanNotBeEmpty);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO: opcions report and help del menu superior
        switch (item.getItemId()){
            case R.id.menu_report:
                //TODO: report si les imatges i/o els texts son inapropiats

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
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //TODO: boto cancelar denuncia
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
}
