package edu.upc.pes.wallachange.Models;

/**
 * Created by carlota on 6/6/17.
 */

public class Conversa {
    private String id_owner;
    private String id_other;
    private String conv_id;

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
}
