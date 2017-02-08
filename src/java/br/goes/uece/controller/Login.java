/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.goes.uece.controller;

import br.goes.uece.model.ObjectDAO;
import br.goes.uece.model.User;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


/**
 *
 * @author italo
 */
@ManagedBean
@SessionScoped
public class Login {
    private String email;
    private String password;
    private User user;
    private ObjectDAO dao;
    
    public Login() {
        dao = new ObjectDAO();
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
    
    public String validate() {
        user = dao.getUser(email, password);
        
        if(user != null) {
            System.out.print(user.getId());
            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().put("loggedUser", user);
            return "/index.xhtml?faces-redirect=true";
        }
        FacesContext.getCurrentInstance().validationFailed();
        FacesMessage fm = new FacesMessage("Email and password do not match");
        fm.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage("Fail to login", fm);
                
        return "";
    }
    
}
