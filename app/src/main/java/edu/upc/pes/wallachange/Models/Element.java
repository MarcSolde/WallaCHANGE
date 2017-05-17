package edu.upc.pes.wallachange.Models;

import android.net.Uri;
import java.util.ArrayList;

public class Element {
    private String id;
    private String titol;
    private String descripcio;
    private String categoria;
    private String tipusProducte;
    private Boolean esTemporal;
    private String temporalitat;
    private String user;
    private ArrayList<Uri> fotografies;
    private ArrayList<Comment> comentaris;
    private String localitat;

    public Element(String titol, String descripcio, String categoria, String tipusProducte, Boolean esTemporal, String temporalitat, String user, ArrayList<Uri> fotografies, ArrayList<Comment> comentaris, String localitat) {
        this.titol = titol;
        this.descripcio = descripcio;
        this.categoria = categoria;
        this.tipusProducte = tipusProducte;
        this.esTemporal = esTemporal;
        this.temporalitat = temporalitat;
        this.user = user;
        this.fotografies = fotografies;
        this.comentaris = comentaris;
        this.localitat = localitat;
    }

    public Element() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser(String user) {this.user = user;}

    public String getUser() {
        return user;
    }
    public String getTitol() {
        return titol;
    }
    public void setTitol(String titol) {
        this.titol = titol;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipusProducte() {
        return tipusProducte;
    }

    public void setTipusProducte(String tipusProducte) {
        this.tipusProducte = tipusProducte;
    }

    public Boolean getEsTemporal() {
        return esTemporal;
    }

    public void setEsTemporal(Boolean intercanviTemporal) {
        this.esTemporal = intercanviTemporal;
    }

    public String getTemporalitat() {
        return temporalitat;
    }

    public void setTemporalitat(String temporalitat) {
        this.temporalitat = temporalitat;
    }

    public ArrayList<Comment> getComentaris() {
        return comentaris;
    }

    public void setComentaris(ArrayList<Comment> comentaris) {
        this.comentaris = comentaris;
    }

    public String getLocalitat() {
        return localitat;
    }

    public void setLocalitat(String localitat) {
        this.localitat = localitat;
    }

    public ArrayList<Uri> getFotografies() {
        return fotografies;
    }

//    public Uri getFotografia() {
//        return fotografies[0];
//    }

    public void setFotografies(ArrayList<Uri> fotografies) {
        this.fotografies = fotografies;
    }

    @Override
    public String toString() {
        return "Element{" +
                "id='" + id + '\'' +
                ", titol='" + titol + '\'' +
                ", descripcio='" + descripcio + '\'' +
                ", categoria='" + categoria + '\'' +
                ", tipusProducte='" + tipusProducte + '\'' +
                ", intercanviTemporal=" + esTemporal +
                ", temporalitat='" + temporalitat + '\'' +
                ", user='" + user + '\'' +
                ", fotografies=" + fotografies +
                ", comentaris=" + comentaris +
                ", localitat='" + localitat + '\'' +
                '}';
    }
}
