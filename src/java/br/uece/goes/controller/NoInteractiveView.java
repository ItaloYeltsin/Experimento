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
import javax.faces.context.FacesContext;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.encodings.variable.Binary;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.gGA;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import problems.NextReleaseProblem;
 
@ManagedBean
@SessionScoped
public class NoInteractiveView implements Serializable {
     
    private List<Requirement> requirements;
    private List<Requirement> selectedRequiriments;
    private Requirement selectedRequirement;
    private ObjectDAO dao;
    private int preference;
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
        preference=50;  
        
        algorithm = new gGA(new NextReleaseProblem("Binary",50));   
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

        } catch (JMException | ClassNotFoundException ex) {
            Logger.getLogger(NoInteractiveView.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }
 
    public List<Requirement> getRequirements() {
        return requirements;
    }

    public Requirement getSelectedRequirement() {
        return selectedRequirement;
    }
 
    public void setSelectedRequirement(Requirement selectedRequirement) {
        this.selectedRequirement = selectedRequirement;
    }
 
    public List<Requirement> getSelectedRequirements() {
        return selectedRequiriments;
    }
 
    public void setSelectedRequirements(List<Requirement> selectedRequirements) {
        this.selectedRequiriments = selectedRequirements;
    }

    public int getPreference() {
        return preference;
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
     
}
