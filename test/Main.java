
import br.goes.uece.model.ObjectDAO;
import br.goes.uece.model.User;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author italo
 */
public class Main {
    public static void main(String [] asas) {
        ObjectDAO dao = new ObjectDAO();
        User u = new User();
        u.setName("Wwqwqw");
        u.setEmail("br.yeltsin@gmail.com");
        u.setPassword("ewewe");
        dao.save(u);
    }
}
