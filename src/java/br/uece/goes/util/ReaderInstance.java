/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.goes.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.StringTokenizer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raphael
 */
public class ReaderInstance {
    
    Integer nClient;
    Integer nRequirements;
    double importance[];
    double valueReq[][];
    double[] valueReqPond;
    double cost[];
    String description[];

    public ReaderInstance(String filename) {
        
        Scanner scn;
        try {
            scn = new Scanner(new File(filename));
            DecimalFormat fmt = new DecimalFormat("0.00");
            StringTokenizer tokens;
            String caracter = "#";

            while (caracter.contains("#")) {
                caracter = scn.nextLine();
            }

            nClient = Integer.parseInt(caracter);

            caracter = jumpLine(scn);

            nRequirements = Integer.parseInt(caracter);

            caracter = jumpLine(scn);

            importance = new double[nClient];
            tokens = new StringTokenizer(caracter);
            for (int i = 0; i < importance.length; i++) {
                importance[i] = Double.parseDouble(tokens.nextToken().trim());
            }

            caracter = jumpLine(scn);
          
            valueReq = new  double[nClient][nRequirements];
            for (int i = 0; i < nClient; i++) {
                tokens = new StringTokenizer(caracter);
                    for (int j = 0; j < nRequirements; j++) {
                        valueReq[i][j] = Integer.parseInt(tokens.nextToken().trim());
                    }
                        caracter = scn.nextLine();
            }   
            valueReqPond = PondValue(valueReq, importance, nRequirements, nClient);
            
            caracter = jumpLine(scn);

            cost = new double[nRequirements];
            tokens = new StringTokenizer(caracter);
            for (int i = 0; i < nRequirements; i++) {
                cost[i] = Integer.parseInt(tokens.nextToken().trim());
            }

            caracter = jumpLine(scn);

            description = new String[nRequirements];
            description[0] = caracter;
            for (int i = 1; i < nRequirements; i++) {
                description[i] = scn.nextLine();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReaderInstance.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
    
    public String jumpLine(Scanner scn) {

		String caracter = scn.nextLine();
		while (caracter.contains("#")) {
			caracter = scn.nextLine();
		}

		return caracter;
	}

    public double[] PondValue(double [][] value, double importance[] ,int nReq, int nClient) {
        
        double[]  valuePond = new double[nRequirements]; 
       
            for (int i = 0; i < nReq; i++) {
              for (int j = 0; j < nClient; j++) {
                valuePond[i] += (value[j][i]*importance[j]);
              }
              valuePond[i] = valuePond[i]/nClient;
            }
            
        return valuePond;
    }
    
    public Integer getnClient() {
        return nClient;
    }

    public void setnClient(Integer nClient) {
        this.nClient = nClient;
    }

    public Integer getnRequirements() {
        return nRequirements;
    }

    public void setnRequirements(Integer nRequirements) {
        this.nRequirements = nRequirements;
    }

    public double[] getImportance() {
        return importance;
    }

    public void setImportance(double[] importance) {
        this.importance = importance;
    }

    public  double[][] getValueReq() {
        return valueReq;
    }

    public void setValueReq(double[][] valueReq) {
        this.valueReq = valueReq;
    }

    public  double[] getCost() {
        return cost;
    }

    public void setCost(double[] cost) {
        this.cost = cost;
    }

    public String[] getDescription() {
        return description;
    }

    public void setDescription(String[] description) {
        this.description = description;
    }

    public double[] getValueReqPod() {
        return valueReqPond;
    }

    public void setValueReqPod(double[] valueReqPod) {
        this.valueReqPond = valueReqPod;
    }

}
