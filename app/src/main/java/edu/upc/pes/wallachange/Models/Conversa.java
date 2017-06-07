package edu.upc.pes.wallachange.Models;

/**
 * Created by carlota on 6/6/17.
 */

public class Conversa {
    private String id_owner;
    private String id_other;
    private String conv_id;
    private String elem1;
    private String elem2;
    private String nomUserOther;
    private String nomElem1;
    private String nomElem2;
    private String lastMessage;

    public Conversa () {

    }

    public Conversa(String id_owner, String id_other, String conv_id) {
        this.id_other = id_other;
        this.id_owner = id_owner;
        this.conv_id = conv_id;
    }

    public String getId_owner() {
        return id_owner;
    }

    public void setId_owner(String id_owner) {
        this.id_owner = id_owner;
    }

    public String getId_other() {
        return id_other;
    }

    public void setId_other(String id_other) {
        this.id_other = id_other;
    }

    public String getConv_id() {
        return conv_id;
    }

    public void setConv_id(String conv_id) {
        this.conv_id = conv_id;
    }

    public void setElem1 (String elem1) {
        this.elem1 = elem1;
    }
    public String getElem1() {
        return elem1;
    }
    public void setElem2 (String elem2) {
        this.elem2 = elem2;
    }
    public String getElem2() {
        return elem2;
    }

    public void setNomUserOther (String nomUserOther) {
        this.nomUserOther = nomUserOther;
    }
    public String getNomUserOther() {
        return nomUserOther;
    }

    public void setNomElem1 (String nomElem1) {
        this.nomElem1 = nomElem1;
    }
    public String getNomElem1() {
        return nomElem1;
    }

    public void setNomElem2 (String nomElem2) {
        this.nomElem2 = nomElem2;
    }
    public String getNomElem2() {
        return nomElem2;
    }
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
    public String getLastMessage() {
        return lastMessage;
    }
}
