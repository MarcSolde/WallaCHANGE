package edu.upc.pes.wallachange.Models;

import android.net.Uri;

/**
 * Andreu Conesa 28/04/2017.
 */

public class Comment {
    // foto, nom d'usuari, text del comentari, data
    private Uri fotoUsuari;
    private String nomUsuari;
    private String textComentari;
    private String data;

    public Comment(Uri fotoUsuari, String nomUsuari, String textComentari, String data) {
        this.fotoUsuari = fotoUsuari;
        this.nomUsuari = nomUsuari;
        this.textComentari = textComentari;
        this.data = data;
    }

    public Uri getFotoUsuari() {
        return fotoUsuari;
    }

    public void setFotoUsuari(Uri fotoUsuari) {
        this.fotoUsuari = fotoUsuari;
    }

    public String getNomUsuari() {
        return nomUsuari;
    }

    public void setNomUsuari(String nomUsuari) {
        this.nomUsuari = nomUsuari;
    }

    public String getTextComentari() {
        return textComentari;
    }

    public void setTextComentari(String textComentari) {
        this.textComentari = textComentari;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
