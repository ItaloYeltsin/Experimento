/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.goes.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Raphael
 */
@ManagedBean
@SessionScoped
public class RedirectPage {

    public void redirectAccount() {
        try {
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String contextPath = ((HttpServletRequest) request)
                    .getContextPath();
            response.sendRedirect(contextPath + "/faces/restricted/account.xhtml");
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException ex) {
            Logger.getLogger(RedirectPage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void redirectAbout() {
        try {
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String contextPath = ((HttpServletRequest) request)
                    .getContextPath();
            response.sendRedirect(contextPath + "/faces/restricted/about.xhtml");
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException ex) {
            Logger.getLogger(RedirectPage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
;
}
