/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.goes.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 *
 * @author Raphael
 */
/**/@Entity
public class Requirement {

    @Id
    @SequenceGenerator(name = "requirement_id_seq", sequenceName = "requirement_id_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "requirement_id_seq")
    @Column(name = "id")
    private Integer id;
    private String Description;
    private Double Importance;
    private Double Cost;

    public Requirement() {
    }
    
    public Requirement(String Description) {
        this.Description = Description;
    }

    public Requirement(String Description, Double Importance, Double Cost) {
        this.Description = Description;
        this.Importance = Importance;
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

    public Double getImportance() {
        return Importance;
    }

    public void setImportance(Double Importance) {
        this.Importance = Importance;
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
