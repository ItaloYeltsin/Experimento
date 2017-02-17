package br.uece.goes.iga.gui;

import java.io.IOException;
import java.util.HashMap;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

import br.uece.goes.iga.DataSet;
import br.uece.goes.iga.InteractiveGeneticAlgorithm;
import br.uece.goes.instance.InstanceReader;

public class test {
	public static void main(String[] args) throws Exception {
		
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		/*
		 * Instance Dialog File
		 */
		InstanceFileDialogWindow fileDialog = new InstanceFileDialogWindow();
		/*
		 * Solution shower
		 */
		InteractionGUI igui = new InteractionGUI();
		/*
		 * Interactive Genetic Algorithm
		 */
		InteractiveGeneticAlgorithm iga;
		/*
		 * Instance Reader
		 */
		InstanceReader reader;
		
		fileDialog.setVisible(true);
		
		reader = new InstanceReader(fileDialog.getInstance());
		
		int numberOfRequirements = reader.getNumberOfRequirements();
		
		igui.setDescription(fileDialog.getDescriptions());

		DataSet dataset = new DataSet(1000, numberOfRequirements);
		/*
		 * Begining
		 */
		iga = new InteractiveGeneticAlgorithm(reader, "LMS", igui);
		iga.setnSubjectiveEvaluations(0);
		int [][] population = iga.createInitialPopulation();
		iga.setPopulation(population);
		HashMap<String, Object> hash = iga.solve();
		
		//double nonInteractiveSHE = igui.evaluate((int[])hash.get("bestIndividual"));
		//System.out.println(nonInteractiveSHE);
		int nSubjectiveEvaluations = 10;
		
		for (int i = 10; i <= 10; i+= nSubjectiveEvaluations) {
			// Least Mean Square Model
			System.out.println(i);
			
			// Multi-Layer Perceptron
			iga = new InteractiveGeneticAlgorithm(reader, "MLP", igui);
			iga.setDataSet(dataset);
			iga.setPopulation(population);
			iga.setnSubjectiveEvaluations(i);
			System.out.println(iga.solve());

			iga = new InteractiveGeneticAlgorithm(reader, "LMS", igui);
			iga.setDataSet(dataset);
			iga.setnSubjectiveEvaluations(i);
			iga.setPopulation(population);
			System.out.println(iga.solve());
		}
		System.exit(0);
	}
	
	
}
