package br.uece.goes.instance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.concurrent.ForkJoinTask;

import weka.core.Instance;
import br.uece.goes.iga.HumanSimulator;
import br.uece.goes.iga.InteractiveGeneticAlgorithm;

public class TargetSolutionGenerator {
	public static void main(String [] args) throws Exception {
		InteractiveGeneticAlgorithm iga;
		InstanceReader reader;
		String [] instances = {"dataset-1", "dataset-2"/*, "I_S_50_50", "I_S_100_50",
				"I_S_150_50", "I_S_200_50"*/};
		
	
		for (int i = 0; i < instances.length; i++) {
			reader = new InstanceReader(new File("instances/"+instances[i]+".txt"));
			iga = new InteractiveGeneticAlgorithm(reader, "LMS", new HumanSimulator());
			int [] solution = iga.solutionGenerator();
			
			File file = new File("instances/"+instances[i]+".ts");
			FileWriter writer = new FileWriter(file);
			for (int j = 0; j < solution.length; j++) {
				writer.write(solution[j]+" ");
			}
			writer.close();
			
		}
	}
}
