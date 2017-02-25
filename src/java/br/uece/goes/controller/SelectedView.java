/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.goes.controller;

import br.uece.goes.model.ObjectDAO;
import br.uece.goes.model.Requirement;
import br.uece.goes.model.User;
import br.uece.goes.util.DataSet;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.encodings.variable.Binary;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.gGA;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.event.UnselectEvent;
import problems.NextReleaseProblem;
import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;

@ManagedBean
@SessionScoped
public class SelectedView implements Serializable {

    private Classifier model;
    private DataSet dataset;
    private List<Requirement> requirements;
    private List<Requirement> selectedRequiriments;
    private List<Requirement> noSelectedRequiriments;
    private Requirement selectedRequirement;
    SolutionSet population;
    private ObjectDAO dao;
    private int rate;
    private int index;
    private boolean stop;
    private boolean avaliar;
    private boolean flag;
    private String text;

    Algorithm algorithm;
    Operator crossover;         // Crossover operator
    Operator mutation;         // Mutation operator
    Operator selection;         // Selection operator
    HashMap parameters;         // Operator parameters

    @PostConstruct
    public void init() {

        try {
            text = "Automatic Solution";
            stop = true;
            avaliar = false;
            model = new MultilayerPerceptron();
            dataset = new DataSet(100, 50);
            dao = new ObjectDAO();
            requirements = dao.getAllReq();
            noSelectedRequiriments = dao.getAllReq();
            rate = 50;
            index = -1;

            Problem problem = new NextReleaseProblem("Binary", 50);
            algorithm = new gGA(problem);
            algorithm.setInputParameter("populationSize", 100);
            algorithm.setInputParameter("maxEvaluations", 25000);

            parameters = new HashMap();
            parameters.put("probability", 0.9);
            crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover", parameters);
            parameters = new HashMap();
            parameters.put("probability", 0.01);
            mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);
            parameters = null;
            selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);

            algorithm.addOperator("crossover", crossover);
            algorithm.addOperator("mutation", mutation);
            algorithm.addOperator("selection", selection);

            selectedRequiriments = ConvertBiSolutionForArray(algorithm.execute().get(0), requirements);

            selectedRequiriments.stream().forEach((requirement) -> {
                noSelectedRequiriments.remove(requirement);
            });

            population = new SolutionSet(100);
            Solution newIndividual;
            for (int i = 0; i < 100; i++) {
                newIndividual = new Solution(problem);
                problem.evaluate(newIndividual);
                population.add(newIndividual);
            }

        } catch (JMException | ClassNotFoundException ex) {
            Logger.getLogger(SelectedView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<Requirement> requirements) {
        this.requirements = requirements;
    }

    public List<Requirement> getSelectedRequiriments() {
        return selectedRequiriments;
    }

    public void setSelectedRequiriments(List<Requirement> selectedRequiriments) {
        this.selectedRequiriments = selectedRequiriments;
    }

    public Requirement getSelectedRequirement() {
        return selectedRequirement;
    }

    public void setSelectedRequirement(Requirement selectedRequirement) {
        this.selectedRequirement = selectedRequirement;
    }

    public List<Requirement> getNoSelectedRequiriments() {
        return noSelectedRequiriments;
    }

    public void setNoSelectedRequiriments(List<Requirement> noSelectedRequiriments) {
        this.noSelectedRequiriments = noSelectedRequiriments;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public boolean isAvaliar() {
        return avaliar;
    }

    public void setAvaliar(boolean avaliar) {
        this.avaliar = avaliar;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    
    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Requirement Selected", ((Requirement) event.getObject()).getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage("Requirement Unselected", ((Requirement) event.getObject()).getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String execute(User user) {

        if (index == -1) {
            user.setNonInterativeEvaluation(rate);
            dao.update(user);
            index++;
            text = "Solution";
            return "beginInteractive.xhtml";
        } else if (index > -1 && index < 99) {
            if (index == 9) {
                stop = false;
            }
            selectedRequiriments = ConvertBiSolutionForArray(population.get(index), requirements);
            dataset.insert(population.get(index), rate);
            noSelectedRequiriments = dao.getAllReq();
            selectedRequiriments.stream().forEach((requirement) -> {
                noSelectedRequiriments.remove(requirement);
            });
            index++;
            text = "Solution";
            if(flag==true)
                return "beginInteractive.xhtml";

        } else {
            avaliar = true;
            if(flag==true)
                return "beginInteractive.xhtml";
        }
        return "";
    }
    
    public String stop(User user) {
            avaliar = false;
            stop = true;
            flag = true;
            index = -1;
            text = "Final Solution";
            selectedRequiriments = null;
            noSelectedRequiriments = null;
            user.setInterativeEvaluation(rate);
            dao.update(user);
            return "";
    }

    ArrayList<Requirement> ConvertBiSolutionForArray(Solution solution, List<Requirement> Req) {

        ArrayList<Requirement> selReq = new ArrayList<>();

        Binary variable = ((Binary) solution.getDecisionVariables()[0]);

        for (int i = 0; i < variable.getNumberOfBits(); i++) {
            if (variable.bits_.get(i)) {
                selReq.add(Req.get(i));
            }
        }
        return selReq;
    }

    public void onSlideEnd(SlideEndEvent event) {

        FacesMessage message = new FacesMessage("Slide Ended", "Value: " + event.getValue());
        UIComponent p = FacesContext.getCurrentInstance().getViewRoot();
        String id = p.getClientId();
        int i = 1;
    }

}
