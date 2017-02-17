package br.uece.goes.iga;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Random;

import br.uece.goes.iga.core.InteractiveEntity;
/**
 * This Class is used as a simulator of different types of profile for evaluating solutions. 
 * This way, depending on the profile chosen, the evaluation of a  given  can  be different.   
 * The evaluation works on the following way: First of all one of the available profiles  is
 * chosen. Then according to the chosen profile  the  requirements  will  be  added  in  the
 * target-individual. The number of requirements to be added is set with a given  percentage. 
 * Thus, a solution is evaluated according to the  similarity between it and the target-individual.  
 * 
 * @since 03-31-2015  
 * @author --
 *
 */
public class HumanSimulator implements InteractiveEntity{
	
	/**
	 * Stores Target Solution
	 */
	private int[] targetSolution;
	
	/**
	 * Evaluate a given individual according to the target solution
	 * @param individual
	 * @return "subjective" evaluation
	 */
	@Override
	public double evaluate(int[] solution){
		double humanEvaluation = 0;
		double numberOfSimilaritiesInIndividual = getNumberOfSimilaritiesInIndividual(solution);
		
		humanEvaluation = numberOfSimilaritiesInIndividual / (double)(targetSolution.length);
		
		return humanEvaluation;
	}
	/**
	 * 
	 * @return the target solution
	 */
	public int[] getTargetSolution(){
		return this.targetSolution;
	}
	
	
	public void setTargetSolution(File file) throws IOException {
		
		BufferedReader rd = new BufferedReader(new FileReader(file));
		String [] ch = rd.readLine().split(" ");
		targetSolution = new int[ch.length];
		for (int i = 0; i < ch.length; i++) {
			targetSolution[i] = Integer.parseInt(ch[i]);
		}
	}
	
	/**
	 * 
	 * @param individual
	 * @return
	 */
	public double getNumberOfSimilaritiesInIndividual(int[] individual){
		int numberOfSimilaritiesInIndividual = 0;
		
		for(int i = 0; i <= targetSolution.length - 1; i++){
			if(targetSolution[i] == individual[i]){
				numberOfSimilaritiesInIndividual++;
			}
		}
		
		return numberOfSimilaritiesInIndividual;
	}
	
}
