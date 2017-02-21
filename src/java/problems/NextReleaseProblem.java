/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problems;

import br.uece.goes.model.ObjectDAO;
import br.uece.goes.model.Requirement;
import java.util.List;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.util.JMException;

/**
 *
 * @author Raphael
 */
public class NextReleaseProblem extends Problem{

    List<Requirement> reqList;
    
    
    public NextReleaseProblem(String solutionType, Integer numberOfBits) {
    
    reqList = new ObjectDAO().getAllReq();    
    numberOfVariables_  = 1;
    numberOfObjectives_ = 1;
    numberOfConstraints_= 0;
    problemName_        = "NRP";             
    solutionType_ = new BinarySolutionType(this) ;
   	    
    length_       = new int[numberOfVariables_];
    length_      [0] = numberOfBits ;
    
    if (solutionType.compareTo("Binary") == 0)
    	solutionType_ = new BinarySolutionType(this) ;
    else {
    	System.out.println("NRP: solution type " + solutionType + " invalid") ;
    	System.exit(-1) ;
    }  
    
  
    }

    @Override
    public void evaluate(Solution solution) throws JMException {
        
        Binary variable;
        double  sumValue  ;
        double  sumCost   ;
        double  budget;

        variable = ((Binary)solution.getDecisionVariables()[0]) ;

        sumValue = 0 ;
        sumCost = 0;
        budget = 0;

        for (int i = 0; i < variable.getNumberOfBits() ; i++) {
          if (variable.bits_.get(i)){
            sumValue += reqList.get(i).getValue();
            sumCost  += reqList.get(i).getCost();
          }
        budget += reqList.get(i).getCost(); 
        }
        
        budget = (budget * 0.4);
        
         // NRP is a maximization problem: multiply by -1 to minimize
        if(sumCost<=budget){
           solution.setObjective(0, -1.0*sumValue);  
        }else{
           solution.setObjective(0, 90000);      
        }
 
    }  
    
}
