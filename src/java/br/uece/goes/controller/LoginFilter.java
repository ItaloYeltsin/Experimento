/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.goes.controller;
  import java.io.IOException;
  import br.uece.goes.model.User;
  import javax.servlet.Filter;
  import javax.servlet.FilterChain;
  import javax.servlet.FilterConfig;
  import javax.servlet.ServletException;
  import javax.servlet.ServletRequest;
  import javax.servlet.ServletResponse;
  import javax.servlet.http.HttpServletRequest;
  import javax.servlet.http.HttpServletResponse;
  import javax.servlet.http.HttpSession;
   
  public class LoginFilter implements Filter {
   
           public void destroy() {
                     // TODO Auto-generated method stub
   
           }
   
           public void doFilter(ServletRequest request, ServletResponse response,
                              FilterChain chain) throws IOException, ServletException {
               User user = null;
               HttpSession sess = ((HttpServletRequest) request).getSession(false);
               
               if (sess != null){
                     user = (User) sess.getAttribute("loggedUser");
               }      
   
                     if (user == null) {
                              String contextPath = ((HttpServletRequest) request)
                                                 .getContextPath();
                              ((HttpServletResponse) response).sendRedirect(contextPath
                                                 + "faces/index.xhtml");
                     } else {
                              chain.doFilter(request, response);
                     }
   
           }
   
           public void init(FilterConfig arg0) throws ServletException {
                     // TODO Auto-generated method stub
   
           }
   
  }