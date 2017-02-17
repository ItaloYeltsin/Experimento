package br.uece.goes.instance;
import java.io.File;
import java.io.ObjectInputStream.GetField;
import java.util.Random;

public class InstanceGeneratorTest {
	public static void main(String[] args){
		int[] sequence = {50, 100, 150, 200}; 
		int[] costumers= {2, 5, 10, 15, 20, 30, 50, 70};
		double[] depedencies_sequence = {0.5};
		for (int i = 0; i < sequence.length; i++) {
			for (int j = 0; j < depedencies_sequence.length; j++) {
				System.out.println("Gerando Instancia: I_S"+sequence[i]+"_"+depedencies_sequence[j]);
				InstanceGenerator test = new InstanceGenerator(costumers[i], sequence[i], depedencies_sequence[j], 10, 10);
				test.generateInstance();
			}
		}
		
				
		
	}	
}