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
        dao = ObjectDAO.getInstance();
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
        user = dao.getUser(email, TransformaStringMD5.md5(password));

        if (user != null) {

            INRP.etapaBeginNonInteractive = false;
            INRP.etapaBeginInteractive = false;
            INRP.etapaEvaluateSolutions = false;

            System.out.print(user.getId());
            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().put("loggedUser", user);
            return "/restricted/index.xhtml";
        }
        FacesContext.getCurrentInstance().validationFailed();
        FacesMessage fm = new FacesMessage("Email and password do not match");
        fm.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage("Fail to login", fm);

        return "";
    }

    public String change(String newPassword) {

        user = dao.getUser(email);

        user.setPassword(TransformaStringMD5.md5(newPassword));
        dao.update(user);

        FacesMessage fm = new FacesMessage("Password Change");
        fm.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("Password Changed", fm);

        return "/restricted/account.xhtml";
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml";
    }

}
