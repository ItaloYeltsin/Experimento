/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problems;

import br.uece.goes.model.ObjectDAO;
import br.uece.goes.model.Requirement;
import br.uece.goes.util.DataSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.util.JMException;
import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;

/**
 *
 * @author Raphael
 */
public class NextReleaseProblem extends Problem {

    private List<Requirement> reqList;
    private Classifier model;
    private DataSet dataset;
    private double budget;
    private double[] parameters = {1, 1};
    private double maxScore = 0;
    private double maxShe = 0;

    public NextReleaseProblem(String solutionType, Integer numberOfBits) {

        reqList = ObjectDAO.getInstance().getAllReq();
        numberOfVariables_ = 1;
        numberOfObjectives_ = 2;
        numberOfConstraints_ = 0;
        problemName_ = "NRP";
        solutionType_ = new BinarySolutionType(this);

        for (int i = 0; i < numberOfBits; i++) {
            budget += reqList.get(i).getCost();
        }
        budget = (budget * 0.1);

        length_ = new int[numberOfVariables_];
        length_[0] = numberOfBits;

        if (solutionType.compareTo("Binary") == 0) {
            solutionType_ = new BinarySolutionType(this);
        } else {
            System.out.println("NRP: solution type " + solutionType + " invalid");
            System.exit(-1);
        }
    }

    public NextReleaseProblem(String solutionType, DataSet data, Integer numberOfBits) {

        try {
            dataset = data;
            model = new MultilayerPerceptron();
            model.buildClassifier(dataset.getInstances());
            reqList = ObjectDAO.getInstance().getAllReq();
            numberOfVariables_ = 1;
            numberOfObjectives_ = 2;
            numberOfConstraints_ = 0;
            problemName_ = "NRP";
            solutionType_ = new BinarySolutionType(this);

            for (int i = 0; i < numberOfBits; i++) {
                budget += reqList.get(i).getCost();
            }
            budget = (budget * 0.1);

            length_ = new int[numberOfVariables_];
            length_[0] = numberOfBits;

            if (solutionType.compareTo("Binary") == 0) {
                solutionType_ = new BinarySolutionType(this);
            } else {
                System.out.println("NRP: solution type " + solutionType + " invalid");
                System.exit(-1);
            }

        } catch (Exception ex) {
            Logger.getLogger(NextReleaseProblem.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void evaluate(Solution solution) throws JMException {

        double score = evaluateScore(solution);
        double cost = evaluateCost(solution);
        double she = 0;

        if (model != null) {
            she = evaluateShe(solution, dataset);
            System.out.println("valor de She: " + she);
        }

        //System.out.println("MaxScore: " + maxScore);
        //System.out.println("MaxShe: " + maxShe);
        double fitness = parameters[0] * (score / maxScore) + parameters[1] * (she / maxShe);

        System.out.println("fitness: " + fitness);
        System.out.println("cost: " + cost);
        System.out.println("budget: " + budget);

        // NRP is a maximization problem: multiply by -1 to minimize
        if (cost <= budget) {
            solution.setObjective(0, -1.0 * fitness);
            solution.setObjective(1, cost);
        } else {
            solution.setObjective(0, 90000);
            solution.setObjective(1, cost);
        }

    }

    public double evaluateScore(Solution solution) {

        Binary variable;
        double score = 0;

        variable = ((Binary) solution.getDecisionVariables()[0]);

        for (int i = 0; i < variable.getNumberOfBits(); i++) {
            if (variable.bits_.get(i)) {
                score += reqList.get(i).getImportance();
            }
        }

        return score;
    }

    public double evaluateCost(Solution solution) {

        Binary variable;
        double cost = 0;

        variable = ((Binary) solution.getDecisionVariables()[0]);

        for (int i = 0; i < variable.getNumberOfBits(); i++) {
            if (variable.bits_.get(i)) {
                cost += reqList.get(i).getCost();
            }
        }

        return cost;
    }

    public double evaluateShe(Solution solution, DataSet dataset) {
        try {
            return model.classifyInstance(dataset.getInstance(solution));
        } catch (Exception ex) {
            Logger.getLogger(NextReleaseProblem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;

    }

    public void setParameters(double p1, double p2) {
        this.parameters[0] = p1;
        this.parameters[1] = p2;
    }

    public double getBudget() {
        return budget;
    }

    public void getMaxScore(SolutionSet population) {

        for (int i = 0; i < population.size(); i++) {

            double score = 0;

            Binary variable = ((Binary) population.get(i).getDecisionVariables()[0]);

            for (int j = 0; j < variable.getNumberOfBits(); j++) {
                if (variable.bits_.get(j)) {
                    score += reqList.get(j).getImportance();
                }
            }

            if (score > maxScore) {
                maxScore = score;
            }

        }

    }

    public void getMaxShe(SolutionSet population) {

        for (int i = 0; i < population.size(); i++) {

            double she = 1;

            if (model != null) {
                she = evaluateShe(population.get(i), dataset);
            }

            if (she > maxShe) {
                maxShe = she;
            }

        }

    }

    
    //não está completo
    public void repare(Solution solution) {

        Binary variable = ((Binary) solution.getDecisionVariables()[0]);
        Random random = new Random();
        double cost = evaluateCost(solution);
        //double score = solution.getObjective(0);
        //double cost = solution.getObjective(1);

        while (cost > budget) {
            int x = random.nextInt(reqList.size());
            if (variable.bits_.get(x)) {
                variable.setIth(x, false);
                cost = cost - reqList.get(x).getCost();
                //score = score - (reqList.get(x).getImportance()/maxScore);
            }
        }
        
        Variable var[] = new Variable[numberOfVariables_];
        var[0] = variable;

        solution.setDecisionVariables(var);
        //solution.setObjective(0, score);
        //solution.setObjective(1, cost);
        

    }

}
