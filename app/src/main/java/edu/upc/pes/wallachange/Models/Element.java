package edu.upc.pes.wallachange.Models;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private ArrayList<String> tags;
    private ArrayList<Comment> comentaris;
    private ArrayList<Coordenades> coordenades;
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

    public void setTagsArray(JSONArray tagsArray) {
        ArrayList<String> list = new ArrayList<String>();
        JSONArray jsonArray = (JSONArray) tagsArray;
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i = 0; i < len; i++) {
                try {
                    list.add(jsonArray.get(i).toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        this.tags = list;
    }

    public void setFotografiesArray(JSONArray tagsArray) {
        ArrayList<Uri> list = new ArrayList<>();
        JSONArray jsonArray = tagsArray;
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i = 0; i < len; i++) {
                try {
                    Uri uri = Uri.parse(jsonArray.get(i).toString());
                    list.add(uri);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        this.fotografies = list;
    }

    public void setComentarisArray(JSONArray comentaris) {
        ArrayList<Comment> list = new ArrayList<>();
        JSONArray jsonArray = comentaris;
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i = 0; i < len; i++) {
                try {
                    Comment comment = new Comment(jsonArray.getJSONObject(i).getString("nom_user"), jsonArray.getJSONObject(i).getString("text"));
                    list.add(comment);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        this.comentaris = list;
    }

    public void setCoordenadesArray(JSONArray coords) {
        ArrayList<Coordenades> list = new ArrayList<>();
        JSONArray jsonArray = coords;
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i = 0; i < len; i++) {
                try {
                    Coordenades cords = new Coordenades();
                    cords.setCoords(jsonArray.getJSONObject(i).getInt("x"), jsonArray.getJSONObject(i).getInt("y"));
                    list.add(cords);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        this.coordenades = list;
    }

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
