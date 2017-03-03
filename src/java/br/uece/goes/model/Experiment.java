/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.goes.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Raphael
 */
@Entity
public class Experiment implements Serializable {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private int nonInterativeEvaluation;
    private int interativeEvaluation;
    private int step;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getNonInterativeEvaluation() {
        return nonInterativeEvaluation;
    }

    public void setNonInterativeEvaluation(int nonInterativeEvaluation) {
        this.nonInterativeEvaluation = nonInterativeEvaluation;
    }

    public int getInterativeEvaluation() {
        return interativeEvaluation;
    }

    public void setInterativeEvaluation(int interativeEvaluation) {
        this.interativeEvaluation = interativeEvaluation;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.userId);
        hash = 17 * hash + this.nonInterativeEvaluation;
        hash = 17 * hash + this.interativeEvaluation;
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
        final Experiment other = (Experiment) obj;
        if (this.nonInterativeEvaluation != other.nonInterativeEvaluation) {
            return false;
        }
        if (this.interativeEvaluation != other.interativeEvaluation) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.userId, other.userId)) {
            return false;
        }
        return true;
    }
    
    
    
}
