package edu.upc.pes.wallachange.Models;

import java.util.ArrayList;

/**
 * Created by Sergi on 17/05/2017.
 */

public class FilterElement {
    private String localitat;
    private Boolean temporalitat;
    private Boolean es_producte; //si es experiencia o producte
    private String [] tags;

    public FilterElement(){

    }

    public String [] getTags() {
        return tags;
    }

    public void setTags(String [] tags) {
        this.tags = tags;
    }

    public Boolean getTipus_element() {
        return es_producte;
    }

    public void setEs_producte(Boolean tipus_element) {
        this.es_producte = tipus_element;
    }

    public Boolean getTemporalitat() {
        return temporalitat;
    }

    public void setTemporalitat(Boolean temporalitat) {
        this.temporalitat = temporalitat;
    }

    public String getLocalitat() {
        return localitat;
    }

    public void setLocalitat(String localitat) {
        this.localitat = localitat;
    }
}
