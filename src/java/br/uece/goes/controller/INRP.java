/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.goes.controller;

import br.uece.goes.model.Experiment;
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
import problems.NextReleaseProblem;

@ManagedBean
@SessionScoped
public class INRP implements Serializable {

    private Experiment exp;
    private DataSet dataset;
    public static List<Requirement> selectedRequiriments;
    public static List<Requirement> noSelectedRequiriments;
    SolutionSet population;
    private ObjectDAO dao;
    private int rate;
    private int index;
    private boolean stop;
    private boolean avaliar;

    @PostConstruct
    public void init() {

        stop = true;
        avaliar = false;
        dataset = new DataSet(100, 50);
        dao = new ObjectDAO();
        rate = 50;
        index = 1;

    }

    public String beginExperiment(User user) {

        if (dao.getExperiment(user.getId()) == null) {
            exp = new Experiment();
            exp.setUserId(user.getId());
            exp.setNonInterativeEvaluation(-1);
            exp.setInterativeEvaluation(-1);
            exp.setStep(1);
            dao.save(exp);
        } else {
            exp = dao.getExperiment(user.getId());
        }

        return "beginNonInteractive.xhtml";
    }

    public String fowardNonInterative(User user) {

        Problem problem = new NextReleaseProblem("Binary", 50);
        Solution solution = executeAlgorithm(problem);
        selectedRequiriments = ConvertBiSolutionForArray(solution, dao.getAllReq());
        noSelectedRequiriments = dao.getAllReq();
        selectedRequiriments.stream().forEach((requirement) -> {
            noSelectedRequiriments.remove(requirement);
        });

        return "experimentNonInteractive.xhtml";

    }

    public String executeNonInteractive(User user) {
        exp = dao.getExperiment(user.getId());
        exp.setNonInterativeEvaluation(rate);
        exp.setStep(2);
        dao.update(exp);
        return "beginInteractive.xhtml";
    }

    public String fowardInterative(User user) {

        Problem problem = new NextReleaseProblem("Binary", 50);
        population = createPopulation((NextReleaseProblem) problem);
        selectedRequiriments = ConvertBiSolutionForArray(population.get(index - 1), dao.getAllReq());
        noSelectedRequiriments = dao.getAllReq();
        selectedRequiriments.stream().forEach((requirement) -> {
            noSelectedRequiriments.remove(requirement);
        });
         
        return "evaluateSolutions.xhtml";
    }

    public void evaluateSol(User user) {
        if (index <= 99) {
            if (index == 10) {
                stop = false;
            }
            selectedRequiriments = ConvertBiSolutionForArray(population.get(index - 1), dao.getAllReq());
            dataset.insert(population.get(index - 1), rate);
            noSelectedRequiriments = dao.getAllReq();
            selectedRequiriments.stream().forEach((requirement) -> {
                noSelectedRequiriments.remove(requirement);
            });
            index++;
        } else {
            avaliar = true;
        }
    }

    public String stop(User user) {

        Problem problem;

        if ((user.getId() % 2) == 0) {
            problem = new NextReleaseProblem("Binary", 50);
        } else {
            problem = new NextReleaseProblem("Binary", dataset, 50);
        }

        Solution solution = executeAlgorithm(problem);
        selectedRequiriments = ConvertBiSolutionForArray(solution, dao.getAllReq());
        selectedRequiriments.stream().forEach((requirement) -> {
            noSelectedRequiriments.remove(requirement);
        });

        return "experimentInteractive.xhtml";
    }

    public String executeInteractive(User user) {
        exp = dao.getExperiment(user.getId());
        exp.setInterativeEvaluation(rate);
        exp.setStep(3);
        dao.update(exp);
        return "feedback.xhtml";
    }

    public String end(User user) {

        exp = dao.getExperiment(user.getId());
        exp.setStep(4);
        dao.update(exp);
        return "thanks.xhtml";
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

    public Solution executeAlgorithm(Problem problem) {

        Operator crossover;         // Crossover operator
        Operator mutation;         // Mutation operator
        Operator selection;         // Selection operator
        HashMap parameters;         // Operator parameters
        Algorithm algorithm = new gGA(problem);
        Solution solution = null;

        try {
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

            solution = algorithm.execute().get(0);

        } catch (JMException | ClassNotFoundException ex) {
            Logger.getLogger(INRP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return solution;
    }

    public SolutionSet createPopulation(NextReleaseProblem problem) {

        SolutionSet populationInit = new SolutionSet(100);
        Solution newIndividual;

        try {
            for (int i = 0; i < 100; i++) {
                do {
                    newIndividual = new Solution(problem);
                    problem.evaluate(newIndividual);
                } while (newIndividual.getObjective(1) > problem.getBudget());
                populationInit.add(newIndividual);
            }
        } catch (JMException | ClassNotFoundException ex) {
            Logger.getLogger(INRP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return populationInit;

    }

    public List<Requirement> getSelectedRequiriments() {
        return selectedRequiriments;
    }

    public void setSelectedRequiriments(List<Requirement> selectedRequiriments) {
        this.selectedRequiriments = selectedRequiriments;
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
