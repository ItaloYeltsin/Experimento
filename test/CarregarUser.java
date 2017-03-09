
import br.uece.goes.model.ObjectDAO;
import br.uece.goes.model.User;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author italo
 */
public class CarregarUser {
    public static void main(String [] asas) {
        ObjectDAO dao = ObjectDAO.getInstance();
        User u = new User();
        u.setName("Italo Yeltsin Medeiros Bruno");
        u.setEmail("br.yeltsin@gmail.com");
        u.setPassword("123");
        dao.save(u);
    }
}
