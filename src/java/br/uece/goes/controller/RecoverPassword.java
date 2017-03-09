/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.goes.controller;

import br.uece.goes.model.ObjectDAO;
import br.uece.goes.model.User;
import br.uece.goes.util.JavaMailApp;
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
public class RecoverPassword {
    
    private String email;
    private String password;
    private User user;
    private ObjectDAO dao;
    
    
    public RecoverPassword() {
        dao = ObjectDAO.getInstance();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public ObjectDAO getDao() {
        return dao;
    }

    public void setDao(ObjectDAO dao) {
        this.dao = dao;
    }
    
    public String recover(){
        
        try{
            user = dao.getUser(email);
            password = TransformaStringMD5.getRandomPass(6);
            user.setPassword(TransformaStringMD5.md5(password));  
            dao.update(user);
            
            JavaMailApp.message(email,password);
            FacesMessage fm = new FacesMessage("Password sent for: "+email); 
            fm.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("Password sent for: "+email, fm);
            
        } catch(Exception e) { 
            FacesMessage fm = new FacesMessage(email+": Email invalid"); 
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("Email invalid", fm);
           return "";
        }

    return "/index.xhtml";
    }
    
}
