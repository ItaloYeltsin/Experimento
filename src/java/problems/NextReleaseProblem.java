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
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.util.JMException;
import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

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
        budget = (budget * 0.4);

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
            budget = (budget * 0.4);

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

        Binary variable;
        double score = 0;
        double cost = 0;
        double she = 0;

        variable = ((Binary) solution.getDecisionVariables()[0]);

        for (int i = 0; i < variable.getNumberOfBits(); i++) {
            if (variable.bits_.get(i)) {
                score += reqList.get(i).getImportance();
                cost += reqList.get(i).getCost();
            }
        }

        if (model != null) {
            she = evaluateShe(solution, dataset);
            System.out.println("valor de She: " + she);
        }

        double fitness = parameters[0] * score + parameters[1] * she;

        // NRP is a maximization problem: multiply by -1 to minimize
        if (cost <= budget) {
            solution.setObjective(0, -1.0 * fitness);
            solution.setObjective(1, cost);
        } else {
            solution.setObjective(0, 90000);
            solution.setObjective(1, cost);
        }

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

    
}
