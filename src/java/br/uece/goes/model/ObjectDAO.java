/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.goes.model;


import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author italo
 */
public class ObjectDAO {
    
   SessionFactory sessionFactory;

    public ObjectDAO() {
         try {
           setUp();
       } catch (Exception ex) {
           ex.printStackTrace();
       }    
    }
   
   
   
   protected void setUp() throws Exception {
	// A SessionFactory is set up once for an application!
	final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
			.configure() // configures settings from hibernate.cfg.xml
			.build();
	try {
		sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
	}
	catch (Exception e) {
		// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
		// so destroy it manually.
                e.printStackTrace();
		StandardServiceRegistryBuilder.destroy( registry );
	}
}
    
    public void save(Object p) {
      
        Session s = sessionFactory.openSession();
        s.beginTransaction();
        s.persist(p);
        s.getTransaction().commit();
        s.close();
    }
    
        public void update(Object p) {
      
        Session s = sessionFactory.openSession();
        s.beginTransaction();
        s.update(p);
        s.getTransaction().commit();
        s.close();
    }

    
    public User getUser(String email, String password) {
        Session s = sessionFactory.openSession();
        EntityManager entityManager = s.getEntityManagerFactory().createEntityManager();
        User user = null;
        try{
            user = entityManager.createQuery(
            "select u " +
            "from User u " +
            "where " +
            "    u.email = :email and " +
            "    u.password = :password",
            User.class)
            .setParameter( "email", email)
                .setParameter( "password", password)
                    .getSingleResult();
        }catch(NoResultException e) {
            
        }
         return user;
    }
    
    public User getUser(String email) {
        Session s = sessionFactory.openSession();
        EntityManager entityManager = s.getEntityManagerFactory().createEntityManager();
        User user = null;
        try{
            user = entityManager.createQuery(
            "select u " +
            "from User u " +
            "where " +
            "    u.email = :email",
            User.class)
            .setParameter( "email", email)
                    .getSingleResult();
        }catch(NoResultException e) {
            
        }
         return user;
    }
          
    public String getPassword(String email) {    
        Session s = sessionFactory.openSession();
        EntityManager entityManager = s.getEntityManagerFactory().createEntityManager();
        User user = null;
        try{
            user = entityManager.createQuery(
            "select u " +
            "from User u " +
            "where " +
            "    u.email = :email",
            User.class)
            .setParameter( "email", email)
                    .getSingleResult();
        }catch(NoResultException e) {
            
        }
         return user.getPassword();  
    }
 
    public void delete(Object object) {
        SessionFactory sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        Session s = sessionFactory.openSession();
        s.beginTransaction();
        s.delete(object);
        s.getTransaction().commit();
        s.close();
    }

      public List<Requirement> getAllReq() {
        Session s = sessionFactory.openSession();
        EntityManager entityManager = s.getEntityManagerFactory().createEntityManager();
        List<Requirement> reqs = null;
        try{
            reqs = entityManager.createQuery(
            "select u " +
            "from Requirement u ",
            Requirement.class)
                    .getResultList();
        }catch(NoResultException e) {
            
        }
         return reqs;
    }  
    

}