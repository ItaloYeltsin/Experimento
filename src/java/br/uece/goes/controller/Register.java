/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.goes.controller;

import br.uece.goes.model.ObjectDAO;
import br.uece.goes.model.User;
import br.uece.goes.util.TransformaStringMD5;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Raphael
 */
@ManagedBean
@SessionScoped
public class Register {
    
        private String name;
        private String email;
        private String password;
        private User user;
        private String educationLevel;
        private String profession;
        private int expTime;
        private String tIExpClassif;
        private String rpClassif;
        
        private ObjectDAO dao;
    
    public Register() {
        dao = new ObjectDAO();
    }

    public String getTIExpClassif() {
        return tIExpClassif;
    }

    public void setTIExpClassif(String tIExpClassif) {
        this.tIExpClassif = tIExpClassif;
    }
    

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getExpTime() {
        return expTime;
    }

    public void setExpTime(int expTime) {
        this.expTime = expTime;
    }


    public String getRpClassif() {
        return rpClassif;
    }

    public void setRpClassif(String rpClassif) {
        this.rpClassif = rpClassif;
    }

    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ObjectDAO getDao() {
        return dao;
    }

    public void setDao(ObjectDAO dao) {
        this.dao = dao;
    }
    
   public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
        
    
    
    public String save(){

        user = dao.getUser(email, TransformaStringMD5.md5(password));
    
        if(user == null){      
            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(TransformaStringMD5.md5(password));
            user.setEducationLevel(educationLevel);
            user.setProfession(profession);
            user.setExpTime(expTime);
            user.setRpClassif(rpClassif);
            user.settIExpClassif(tIExpClassif);
            dao.save(this.user); 
            FacesMessage fm = new FacesMessage("User successfully registred!"); 
            fm.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("Successful registration", fm);
        } else {    
            FacesMessage fm = new FacesMessage("Someone already has that username."); 
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("Someone already has that username.", fm);
           return "";
        }

    return "/index.xhtml";
    
    }
    
}
