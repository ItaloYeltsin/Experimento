/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.goes.model;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private static ObjectDAO oDAO;
    private SessionFactory sessionFactory;

    private ObjectDAO() {
        try {
            setUp();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ObjectDAO getInstance() {
        if (oDAO == null) {
            oDAO = new ObjectDAO();
        }
        return oDAO;
    }
            
    protected void setUp() throws Exception {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    private Session openSession(){
        if(sessionFactory == null) {
            try {
                setUp();
            } catch (Exception ex) {
                Logger.getLogger(ObjectDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sessionFactory.openSession();
    }
    
    public void save(Object p) {
        
        Session s = openSession();
        s.beginTransaction();
        s.persist(p);
        s.getTransaction().commit();
        s.close();
    }

    public void update(Object p) {

        Session s = openSession();
        s.beginTransaction();
        s.update(p);
        s.getTransaction().commit();
        s.close();
    }

    public User getUser(String email, String password) {
        Session s = openSession();
        EntityManager entityManager = s.getEntityManagerFactory().createEntityManager();
        User user = null;
        try {
            user = entityManager.createQuery(
                    "select u "
                    + "from User u "
                    + "where "
                    + "    u.email = :email and "
                    + "    u.password = :password",
                    User.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (NoResultException e) {

        }
        return user;
    }

    public User getUser(String email) {
        Session s = openSession();
        EntityManager entityManager = s.getEntityManagerFactory().createEntityManager();
        User user = null;
        try {
            user = entityManager.createQuery(
                    "select u "
                    + "from User u "
                    + "where "
                    + "    u.email = :email",
                    User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {

        }
        return user;
    }

    public Experiment getExperiment(Long userId) {
        Session s = openSession();
        EntityManager entityManager = s.getEntityManagerFactory().createEntityManager();
        Experiment experiment = null;
        try {
            experiment = entityManager.createQuery(
                    "select u "
                    + "from Experiment u "
                    + "where "
                    + "    u.userId = :userId",
                    Experiment.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {

        }
        return experiment;
    }

    public String getPassword(String email) {
        Session s = openSession();
        EntityManager entityManager = s.getEntityManagerFactory().createEntityManager();
        User user = null;
        try {
            user = entityManager.createQuery(
                    "select u "
                    + "from User u "
                    + "where "
                    + "    u.email = :email",
                    User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {

        }
        return user.getPassword();
    }

    public void delete(Object object) {
        Session s = openSession();
        s.beginTransaction();
        s.delete(object);
        s.getTransaction().commit();
        s.close();
    }

    public List<Requirement> getAllReq() {
        Session s = openSession();
        EntityManager entityManager = s.getEntityManagerFactory().createEntityManager();
        List<Requirement> reqs = null;
        try {
            reqs = entityManager.createQuery(
                    "select u "
                    + "from Requirement u",
                    Requirement.class)
                    .getResultList();
        } catch (NoResultException e) {
            
        }
        return reqs;
    }

}
