package br.uece.goes.instance;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;

public class InstanceGenerator {

	private int numberOfCustomers;
	private int numberOfRequirements;
	private int importanceUpperBound;
	private int costUpperBound;
	private double percentageOfDependencies;
	private Random random;
	
	public InstanceGenerator(int numberOfCustomers, int numberOfRequirements, double percentageOfDependencies, int importanceUpperBound, int costUpperBound){
		this.numberOfCustomers = numberOfCustomers;
		this.numberOfRequirements = numberOfRequirements;
		this.importanceUpperBound = importanceUpperBound;
		this.costUpperBound = costUpperBound;
		this.percentageOfDependencies = percentageOfDependencies;
		
		this.random = new Random();
	}
	
	public void generateInstance(){
		
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("instances/I_S_" + numberOfRequirements +
					"_"+(int)(percentageOfDependencies*100.0)+".txt")));
			
			writer.write(numberOfCustomers + " " + numberOfRequirements + "\n");
			writer.write("\n");
			
			writer.write(getCustomersImportance(numberOfCustomers) + "\n");
			writer.write("\n");
			
			byte[][] requires = getAcyclicGraph();
			for (int i = 0; i < numberOfRequirements; i++) {
				for (int j = 0; j < requires.length; j++) {
					writer.write(requires[i][j]+" ");
				}
				writer.write("\n");
			}
			writer.write("\n");
			/*DecimalFormat df = new DecimalFormat("0.#");
			
			double [][] icost = getDoubleMatrix();
			for (int i = 0; i < numberOfRequirements; i++) {
				for (int j = 0; j < icost.length; j++) {
					writer.write(Double.valueOf(icost[i][j]+"")+" ");
				}
				writer.write("\n");
			}
			writer.write("\n");
			
			double [][] cvalue = getDoubleMatrix();
			for (int i = 0; i < numberOfRequirements; i++) {
				for (int j = 0; j < cvalue.length; j++) {
					writer.write(cvalue[i][j]+" ");
				}
				writer.write("\n");
			}
			writer.write("\n");*/
			
			for(int i = 0; i <= numberOfCustomers - 1; i++){
				writer.write(getRequirementsImportances(numberOfRequirements) + "\n");
			}
			writer.write("\n");
			
			writer.write(getRequirementsCosts(numberOfRequirements));
			
			writer.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	
	private byte [][] getAcyclicGraph() {
		
		byte [][] graph = new byte[numberOfRequirements][numberOfRequirements];
		int rate = (int) (percentageOfDependencies*(double)numberOfRequirements);
		int [] set = new int[numberOfRequirements];
		boolean [] hasAnd = new boolean[numberOfRequirements];
		boolean[] alreadySet = new boolean[numberOfRequirements];
		int andRate = (int) (0.1*rate)+ 1;
		int count=0;
		for (int i = 0; i < alreadySet.length; i++) {
			set[i] = i;
		}
		int a, b;
		// Algorithm Union-Find
		for (int i = 0; i < rate;) {
			a = random.nextInt(numberOfRequirements);
			b = random.nextInt(numberOfRequirements);
			
			if (!isTheSameSet(a, b, set)) {
				Union(a, b, set);
				graph[a][b] = 1;
				if (!hasAnd[a] && !hasAnd[b] && count++ <= andRate) {
					hasAnd[a] = true;
					hasAnd[b] = true;
					graph[b][a] = 1;
				}
				if(!alreadySet[a]) {
					alreadySet[a] = true;
					i++;
				}
			}
		}
		return graph;
	}
	
	public double[][] getDoubleMatrix() {
		double [][] matrix = new double[numberOfRequirements][numberOfRequirements];
		boolean [] alreadySet = new boolean[numberOfRequirements];
		int a,b;
		
		int count = (int)((double)numberOfRequirements*percentageOfDependencies);
		for (int i = 0; i < count;) {
			
			a = random.nextInt(numberOfRequirements);
			b = random.nextInt(numberOfRequirements);
			if (matrix[a][b] != 0.0 || a == b) continue;
			matrix[a][b] = Math.pow(-1.0, (double)random.nextInt(2))*getUniformNumber();
			if(!alreadySet[a]) {
				i++;
				alreadySet[a] = true;
			}
		}
		return matrix;
	}
	
	private double getUniformNumber () {
		double p = (double)random.nextInt(11)/10.0;
		return p;
	}
	
	private void Union(int a, int b, int [] set) {
		set[findSet(a, set)] = findSet(b, set);		
	}

	private boolean isTheSameSet(int a, int b, int[] set) {
		return findSet(a, set) == findSet(b, set);
	}
	
	private int findSet (int a, int [] set) {
		return a == set[a]? a: (set[a] = findSet(set[a], set));
	}
	

	
	public void suffleArray(int[] array){
		Random rgen = new Random();  // Random number generator			
 
		for (int i=0; i<array.length; i++) {
		    int randomPosition = rgen.nextInt(array.length);
		    int temp = array[i];
		    array[i] = array[randomPosition];
		    array[randomPosition] = temp;
		}
	}
	

	private String getCustomersImportance(int numberOfCustomers){
		String customersImportance = null;
		double[] randomNumbers = new double[numberOfCustomers];
		double randomNumbersSum = 0;

		for(int i = 0; i <= randomNumbers.length - 1; i++){
			randomNumbers[i] = random.nextDouble();
			randomNumbersSum += randomNumbers[i];
		}

		
		for(int i = 0; i <= randomNumbers.length - 1; i++){
			if(customersImportance == null){
				customersImportance = randomNumbers[i] / randomNumbersSum + "";
			}
			else{
				customersImportance += " " + randomNumbers[i] / randomNumbersSum;
			}
		}

		return customersImportance;
	}
	
	private String getRequirementsImportances(int numberOfRequirements){
		String requirementsImportances = null;
		
		for(int i = 0; i <= numberOfRequirements - 1; i++){
			if(i == 0){
				requirementsImportances = (random.nextInt(importanceUpperBound) + 1) + "";
			}
			else{
				requirementsImportances += " " + (random.nextInt(importanceUpperBound) + 1);
			}
		}
		
		return requirementsImportances;
	}
	
	private String getRequirementsCosts(int numberOfRequirements){
		String requirementsCosts = null;
		
		for(int i = 0; i <= numberOfRequirements - 1; i++){
			if(i == 0){
				requirementsCosts = (random.nextInt(costUpperBound) + 1) + "";
			}
			else{
				requirementsCosts += " " + (random.nextInt(costUpperBound) + 1);
			}
		}
		
		return requirementsCosts;
	}
}