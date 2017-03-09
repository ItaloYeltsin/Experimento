/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.goes.controller;

import br.uece.goes.model.Experiment;
import br.uece.goes.model.ObjectDAO;
import br.uece.goes.model.User;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Raphael
 */
public class INRPFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        User user = null;
        ObjectDAO dao = ObjectDAO.getInstance();
        HttpSession sess = ((HttpServletRequest) request).getSession(false);
        int step;

        if (sess != null) {
            user = (User) sess.getAttribute("loggedUser");
            String contextPath = ((HttpServletRequest) request)
                    .getContextPath();

            if (dao.getExperiment(user.getId()) == null) {
                step = 0;
            } else {
                step = dao.getExperiment(user.getId()).getStep();
            }

            System.out.println("Step::::" + step);

            switch (((HttpServletRequest) request).getPathInfo()) {

                case "/restricted/problems/InteractiveNextReleaseProblem/index.xhtml":
                    if (step == 1) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/beginNonInteractive.xhtml");
                    } else if (step == 2) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/beginInteractive.xhtml");
                    } else if (step == 3) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/feedback.xhtml");
                    } else if (step == 4) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/thanks.xhtml");
                    }

                    break;

                case "/restricted/problems/InteractiveNextReleaseProblem/beginNonInteractive.xhtml":
                    if (step == 2) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/beginInteractive.xhtml");
                    } else if (step == 3) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/feedback.xhtml");
                    } else if (step == 4) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/thanks.xhtml");
                    }

                    break;

                case "/restricted/problems/InteractiveNextReleaseProblem/experimentNonInteractive.xhtml":

                    if (step == 0) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/beginNonInteractive.xhtml");
                    } else if (step == 1 && !INRP.etapaBeginNonInteractive) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/beginNonInteractive.xhtml");
                    } else if (step == 2) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/beginInteractive.xhtml");
                    } else if (step == 3) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/feedback.xhtml");
                    } else if (step == 4) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/thanks.xhtml");
                    }
                    break;

                case "/restricted/problems/InteractiveNextReleaseProblem/beginInteractive.xhtml":
                    if (step == 0) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/begiNonInteractive.xhtml");
                    } else if (step == 1) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/beginNonInteractive.xhtml");
                    } else if (step == 3) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/feedback.xhtml");
                    } else if (step == 4) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/thanks.xhtml");
                    }

                    break;

                case "/restricted/problems/InteractiveNextReleaseProblem/evaluateSolutions.xhtml":

                    if (step == 0) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/begiNonInteractive.xhtml");
                    } else if (step == 1) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/beginNonInteractive.xhtml");
                    } else if (step == 2 && !INRP.etapaBeginInteractive) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/beginInteractive.xhtml");
                    } else if (step == 3) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/feedback.xhtml");
                    } else if (step == 4) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/thanks.xhtml");
                    }

                    break;

                case "/restricted/problems/InteractiveNextReleaseProblem/experimentInteractive.xhtml":

                    if (step == 0) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/begiNonInteractive.xhtml");
                    } else if (step == 1) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/beginNonInteractive.xhtml");
                    } else if (step == 2 && !INRP.etapaEvaluateSolutions) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/beginInteractive.xhtml");
                    } else if (step == 3) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/feedback.xhtml");
                    } else if (step == 4) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/thanks.xhtml");
                    }

                    break;

                case "/restricted/problems/InteractiveNextReleaseProblem/feedback.xhtml":
                    if (step == 0) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/begiNonInteractive.xhtml");
                    } else if (step == 1) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/beginNonInteractive.xhtml");
                    } else if (step == 2) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/beginInteractive.xhtml");
                    } else if (step == 4) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/thanks.xhtml");
                    }

                    break;

                case "/restricted/problems/InteractiveNextReleaseProblem/thanks.xhtml":
                    if (step == 0) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/begiNonInteractive.xhtml");
                    } else if (step == 1) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/beginNonInteractive.xhtml");
                    } else if (step == 2) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/beginInteractive.xhtml");
                    } else if (step == 3) {
                        ((HttpServletResponse) response).sendRedirect(contextPath
                                + "/faces/restricted/problems/InteractiveNextReleaseProblem/feedback.xhtml");
                    }

                    break;
            }
            chain.doFilter(request, response);
        }
    }

}
