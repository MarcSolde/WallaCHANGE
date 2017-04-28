package edu.upc.pes.wallachange;

import android.net.Uri;

import java.util.ArrayList;



public class Element {
    private String id;
    private String titol;
    private String descripcio;
    private String categoria;
    private String tipusProducte;
    private String tipusIntercanvi;
    private String temporalitat;
    private String user;
    private ArrayList<Uri> fotografies;

    public Element(String titol, String descripcio, String categoria, String tipusProducte, String tipusIntercanvi, String temporalitat, ArrayList<Uri> fotografies) {
        this.titol = titol;
        this.descripcio = descripcio;
        this.categoria = categoria;
        this.tipusProducte = tipusProducte;
        this.tipusIntercanvi = tipusIntercanvi;
        this.temporalitat = temporalitat;
        this.fotografies = fotografies;
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

    public String getTipusIntercanvi() {
        return tipusIntercanvi;
    }

    public void setTipusIntercanvi(String tipusIntercanvi) {
        this.tipusIntercanvi = tipusIntercanvi;
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
                ", tipusIntercanvi='" + tipusIntercanvi + '\'' +
                ", temporalitat='" + temporalitat + '\'' +
                ", fotografies=" + fotografies +
                '}';
    }
}
