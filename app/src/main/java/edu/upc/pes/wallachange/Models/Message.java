package edu.upc.pes.wallachange.Models;

/**
 * Created by carlota on 4/6/17.
 */

public class Message {
    private String message;
    private String owner;
//    private String time;
    //1 = currentuser, 0 = persona amb qui parlem

    public Message() {

    }
    public Message(String message, String owner) {
        this.message = message;
        this.owner = owner;
    }
    public Message(String message, String owner, String time) {
        this.message = message;
        this.owner = owner;
//        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getOwner() {
        return owner;
    }

//    public String getTime() {
//        return time;
//    }
}
