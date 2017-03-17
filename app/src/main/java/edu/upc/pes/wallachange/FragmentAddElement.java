package edu.upc.pes.wallachange;


import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddElement extends Fragment implements View.OnClickListener {


    public FragmentAddElement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentAddElementView =  inflater.inflate(R.layout.fragment_add_element,
                container, false);

        final ImageButton botoAfegir =
                (ImageButton) fragmentAddElementView.findViewById(R.id.afegirElement);
        botoAfegir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText editTextTitolAnunci = (EditText) fragmentAddElementView.findViewById(R.id.titolAnunci);
                EditText editTextDescripcio = (EditText) fragmentAddElementView.findViewById(R.id.descripcio);

                //Element nouElement = new Element(editTextTitolAnunci.getText(),editTextDescripcio.getText(),...);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Titol de l'anunci: " + editTextTitolAnunci.getText() + "/nDescripcio: " + editTextDescripcio.getText());
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        return fragmentAddElementView;
    }

    @Override
    public void onClick(View v) {

    }
}
