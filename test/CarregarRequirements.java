
import br.uece.goes.model.ObjectDAO;
import br.uece.goes.model.Requirement;
import br.uece.goes.util.ReaderInstance;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Raphael
 */
public class CarregarRequirements {
    
        public static void main(String [] arg) {
            
            ReaderInstance instance;
            instance = new ReaderInstance("src/instances/I_S_50_50.txt");                
            ObjectDAO dao = new ObjectDAO();      
            String desc[] = instance.getDescription();
            double importance[] = instance.getValueReqPod();
            double cost[] = instance.getCost();
        
            for (int i = 0; i < instance.getnRequirements(); i++) {
                Requirement r = new Requirement(desc[i],importance[i],cost[i]);  
                dao.save(r);                   
            }  
        }
        
}
