package edu.upc.pes.wallachange.Models;

import android.net.Uri;

import java.util.Date;

/**
 * Andreu Conesa 28/04/2017.
 */

public class Comment {
    // foto, nom d'usuari, text del comentari, data
    private Uri fotoUsuari;
    private String idComentari;
    private String idUsuari;
    private String textComentari;
    private Date dia;
    private String data;


    public Comment(String idComentari, String idUsuari, String textComentari, Date dia) {
        this.idComentari = idComentari;
        this.idUsuari = idUsuari;
        this.textComentari = textComentari;
        this.dia = dia;
        //TODO obtenir foto usuari
    }
    public Comment(Uri fotoUsuari, String idUsuari, String textComentari, String data) {
        this.fotoUsuari = fotoUsuari;
        this.idUsuari = idUsuari;
        this.textComentari = textComentari;
        this.data = data;
    }

    public Uri getFotoUsuari() {
        return fotoUsuari;
    }

    public String getIdComentari() {
        return idComentari;
    }

    public void setIdComentari(String idComentari) {
        this.idComentari = idComentari;
    }

    public void setFotoUsuari(Uri fotoUsuari) {
        this.fotoUsuari = fotoUsuari;
    }

    public String getIdUsuari() {
        return idUsuari;
    }

    public void setIdUsuari(String idUsuari) {
        this.idUsuari = idUsuari;
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

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }
}
