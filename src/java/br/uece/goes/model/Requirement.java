/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.goes.model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Raphael
 */
/**/@Entity
public class Requirement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String Description;
    private Double ValueReq;
    private Double Cost;

    public Requirement() {
    }
    
    public Requirement(String Description) {
        this.Description = Description;
    }

    public Requirement(String Description, Double Value, Double Cost) {
        this.Description = Description;
        this.ValueReq = Value;
        this.Cost = Cost;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public Double getValue() {
        return ValueReq;
    }

    public void setValue(Double Value) {
        this.ValueReq = Value;
    }

    public Double getCost() {
        return Cost;
    }

    public void setCost(Double Cost) {
        this.Cost = Cost;
    }

    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Requirement other = (Requirement) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.goes.uece.model.Requirement[ id=" + id + " ]";
    }

}
