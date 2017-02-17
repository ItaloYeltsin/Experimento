package br.uece.goes.iga;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import br.uece.goes.instance.InstanceReader;

/**
 * 
 * This class was used to perform the presented tests.
 * 
 * @since 03-31-2014
 * @author --
 *
 */

public class InteractiveGeneticAlgorithmTest {

	public static void main(String[] args) throws Exception {
		/*
		 * Stores the results
		 */
		ArrayList<HashMap<String, Object>> listOfResults = null;
		/*
		 * Number of Evaluations Per Test
		 */
		int numberOfExecutions = 30;
		/*
		 * Instance reader
		 */
		InstanceReader reader;
		/*
		 * Evaluating Profile
		 */
		HumanSimulator simulator = new HumanSimulator();
		/*
		 * Solver
		 */
		InteractiveGeneticAlgorithm iga;

		double[][] parameters = { { 1, 0 }, { 1, 0.1 }, { 1, 0.2 }, { 1, 0.3 }, { 1, 0.4 }, { 1, 0.5 }, { 1, 0.6 },
				{ 1, 0.7 }, { 1, 0.8 }, { 1, 0.9 }, { 1, 1 } };
		String[] instances = { "I_S_50_50", "I_S_100_50", "I_S_150_50",
				"I_S_200_50" , "dataset-1", "dataset-2"  };
		
		int [] numberOfInteractions = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500};
		
		for (int m = 0; m < numberOfInteractions.length; m++) {
			System.out.println("Numero de perguntas: "+ numberOfInteractions[m]);
			for (int i = 0; i < instances.length; i++) { // for each instance
				// Instance Reader
				reader = new InstanceReader(new File("instances/" + instances[i] + ".txt"));
				System.out.println("\tInstance: "+instances[i]);
				// IGA
				iga = new InteractiveGeneticAlgorithm(reader, "LMS", simulator);
				iga.setnSubjectiveEvaluations(numberOfInteractions[m]);
				simulator.setTargetSolution(new File("instances/"+ instances[i] + ".ts"));

				for (int k = 0; k < parameters.length; k++) { // for each
																// parameter
																// combination
					File file = new File(
							"results/"
									+ numberOfInteractions[m]+"/"  
									+ instances[i] + "_p_" + parameters[k][0] 
									+ "_" + parameters[k][1] + ".data");
					FileWriter writer = new FileWriter(file);

					iga.setParameters(parameters[k]);
					listOfResults = new ArrayList<HashMap<String, Object>>();

					for (int l = 0; l <= numberOfExecutions - 1; l++) { // execute
																		// numberOfExecutions
																		// times
						listOfResults.add(iga.solve());
					}
					writer.write("$Fitness\t\t$Score\t\t$SE-SME\t\t$SD\t\t$SF\t\t$PP\t\t$ModelError\n");
					for (HashMap<String, Object> r : listOfResults) {
						writer.write(r.get("fitness") + "\t\t" + r.get("bestIndividualScore") + "\t\t"
								+ r.get("subjectiveModelEvaluation") + "\t\t" + r.get("similarityDegree") + "\t\t"
								+ r.get("similarityFactor") + "\t\t" + r.get("preferencePrice") + "\t\t"
								+ r.get("modelError") + "\t\t\n");
					}
					writer.close();

				}
			}
		}
	}
}