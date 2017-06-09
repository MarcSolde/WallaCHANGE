package edu.upc.pes.wallachange.Models;

import java.util.ArrayList;

/**
 * Created by Sergi on 17/05/2017.
 */

public class FilterElement {
    private String temporalitat;
    private String es_producte; //si es experiencia o producte
    private String tags;

    public FilterElement(){
        this.tags = "";
        this.temporalitat = "";
        this.es_producte = "";
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTipus_element() {
        return es_producte;
    }

    public void setEs_producte(String tipus_element) {
        this.es_producte = tipus_element;
    }

    public String getTemporalitat() {
        return temporalitat;
    }

    public void setTemporalitat(String temporalitat) {
        this.temporalitat = temporalitat;
    }
}
