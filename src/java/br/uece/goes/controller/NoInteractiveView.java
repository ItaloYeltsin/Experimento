/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.goes.controller;

import br.uece.goes.model.ObjectDAO;
import br.uece.goes.model.Requirement;
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
 
@ManagedBean
@SessionScoped
public class NoInteractiveView implements Serializable {
     
    private List<Requirement> requirements;
    private List<Requirement> selectedRequiriments;
    private Requirement selectedRequirement;
    SolutionSet population;
    private ObjectDAO dao;
    private int rate;
    private int preference;
    private int index;
    Algorithm algorithm ; 
    Operator  crossover ;         // Crossover operator
    Operator  mutation  ;         // Mutation operator
    Operator  selection ;         // Selection operator
    HashMap  parameters ;         // Operator parameters
     
    @PostConstruct
    public void init() {
    
        try {
        dao = new ObjectDAO();  
        requirements = dao.getAllReq();
        rate=50;
        index=0;
        
        Problem problem = new NextReleaseProblem("Binary",50);
        algorithm = new gGA(problem);   
        algorithm.setInputParameter("populationSize",100);
        algorithm.setInputParameter("maxEvaluations", 25000);

        
        parameters = new HashMap();
        parameters.put("probability", 0.9);
        crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover", parameters); 
        parameters = new HashMap();
        parameters.put("probability", 0.01);
        mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);
        parameters = null;
        selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters) ;
     
        algorithm.addOperator("crossover",crossover);
        algorithm.addOperator("mutation",mutation);
        algorithm.addOperator("selection",selection);

        
        selectedRequiriments = ConvertBiSolutionForArray(algorithm.execute().get(0),requirements);
    
    population = new SolutionSet(100) ;
    Solution newIndividual;
    for (int i = 0; i < 100; i++) {
      newIndividual = new Solution(problem);                    
      problem.evaluate(newIndividual);
      population.add(newIndividual);
    }    
        
        

        } catch (JMException | ClassNotFoundException ex) {
            Logger.getLogger(NoInteractiveView.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }

    public String getNominalRate() {
        String status ="";
        if(rate<=20){
          status = "very bad";  
        }else if(rate>20 && rate<=40){
          status = "bad";  
        }else if(rate>40 && rate<=60){
          status = "medium"; 
        }else if(rate>60 && rate<=80){
           status = "good"; 
        }else if(rate>80 && rate<=100){
           status = "very good";
        }
        
       return status;
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

    public int getPreference() {
        return preference;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
    
  

    public void setPreference(int preference) {
        this.preference = preference;
    }


    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Requirement Selected", ((Requirement) event.getObject()).getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
 
    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage("Requirement Unselected", ((Requirement) event.getObject()).getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void execute(){      
        selectedRequiriments = ConvertBiSolutionForArray(population.get(index),requirements);
        index++;
    }
    
    ArrayList<Requirement> ConvertBiSolutionForArray(Solution solution, List<Requirement> Req){
        
        ArrayList<Requirement> selReq= new ArrayList<>();

        Binary variable = ((Binary)solution.getDecisionVariables()[0]) ;

               for (int i = 0; i < variable.getNumberOfBits() ; i++){
                    if (variable.bits_.get(i)){
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
