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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import edu.upc.pes.wallachange.Adapters.CommentListViewAdapter;
import edu.upc.pes.wallachange.Models.Comment;
import edu.upc.pes.wallachange.Others.ExpandableHeightGridView;

public class ViewElementFragment extends Fragment implements View.OnClickListener{

    private View fragmentViewElementView;
    private MainActivity myActivity;
    private ImageButton previousPictureButton;
    private ImageButton nextPictureButton;
    private ImageButton reportButton;
    private ImageButton reportInformationButton;
    private ImageButton writeCommentButton;
    private Button tradeButton;
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

        previousPictureButton = (ImageButton) fragmentViewElementView.findViewById(R.id.previousButton);
        previousPictureButton.setOnClickListener(this);
        nextPictureButton = (ImageButton) fragmentViewElementView.findViewById(R.id.nextButton);
        nextPictureButton.setOnClickListener(this);
        writeCommentButton = (ImageButton) fragmentViewElementView.findViewById(R.id.writeComment);
        writeCommentButton.setOnClickListener(this);
        tradeButton = (Button) fragmentViewElementView.findViewById(R.id.tradeButton);
        tradeButton.setOnClickListener(this);

        comentaris = new ArrayList<>();

        TextView textViewTitol = (TextView) fragmentViewElementView.findViewById(R.id.titolAnunci);
        textViewTitol.setText(bundle.getString("titol"));

        imatgeActual = 0;
        imatge = (ImageView) fragmentViewElementView.findViewById(R.id.imageViewFotoElement);
        uris = bundle.getParcelableArrayList("fotografies");
        Uri u = (Uri)uris.get(imatgeActual);
        Picasso.with(myActivity).load(u).resize(400,400).into(imatge);

        TextView textViewDescripcio = (TextView) fragmentViewElementView.findViewById(R.id.textViewDescripcio);
        textViewDescripcio.setText(bundle.getString("descripcio"));

        editTextWriteComment = (EditText) fragmentViewElementView.findViewById(R.id.editTextComment);

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
                    editTextWriteComment.setFocusable(false);
                    String comentari = editTextWriteComment.getText().toString();
                    Uri path = Uri.parse("android.resource://edu.upc.pes.wallachange/" + R.drawable.userpicture);
                    String date = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date());
                    Comment nouComentari = new Comment(path,"Andreu Conesa",comentari,date);
                    comentaris.add(0,nouComentari);
                    // TODO: el comentari s'afegeix a l'anunci

                    ExpandableHeightGridView listViewComentaris = (ExpandableHeightGridView) fragmentViewElementView.findViewById(R.id.comments_list);
                    listViewComentaris.setExpanded(true);
                    CommentListViewAdapter adapter = new CommentListViewAdapter(myActivity, R.layout.comment_row_layout, comentaris,this);
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
        //TODO: report and help
        return super.onOptionsItemSelected(item);
    }

    /*
    private void mostrarInformacioSobreDenuncia() {
        AlertDialog alertDialog = new AlertDialog.Builder(myActivity).create();
        alertDialog.setTitle(getResources().getString(R.string.this_field_is_required_eng));
        alertDialog.setMessage("Alert message to be shown");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    */
}
