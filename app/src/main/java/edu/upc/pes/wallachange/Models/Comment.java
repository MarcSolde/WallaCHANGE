package edu.upc.pes.wallachange.Models;

/**
 * Created by Usuario on 28/04/2017.
 */

public class Comment {
    // foto, nom d'usuari, text del comentari, data
    private String fotoUsuari;
    private String nomUsuari;
    private String textComentari;
    private String data;

    public Comment(String fotoUsuari, String nomUsuari, String textComentari, String data) {
        this.fotoUsuari = fotoUsuari;
        this.nomUsuari = nomUsuari;
        this.textComentari = textComentari;
        this.data = data;
    }

    public String getFotoUsuari() {
        return fotoUsuari;
    }

    public void setFotoUsuari(String fotoUsuari) {
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
