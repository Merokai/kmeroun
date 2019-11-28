package udev.jsp.kmeroun.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import udev.jsp.kmeroun.models.User;
import udev.jsp.kmeroun.utils.HibernateSessionFactory;

import java.util.List;

public class UserDao implements DaoInterface<User, Integer> {

    public User getByUsername(String username) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        User user = null;
        try{
            tx = session.beginTransaction();
            Query query = session.createQuery("from User as u where u.username = :username");
            query.setParameter("username", username);
            user = (User) query.getSingleResult();
            tx.commit();
        } catch(Exception e){
            if(tx != null){
                tx.rollback();
                throw e;
            }
        } finally{
            session.close();
        }
        return user;
    }

    @Override
    public void save(User user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
        } catch(Exception e){
            if(tx != null){
                tx.rollback();
                throw e;
            }
        } finally{
            session.close();
        }
    }

    @Override
    public void update(User user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.update(user);
            tx.commit();
        } catch(Exception e){
            if(tx != null){
                tx.rollback();
                throw e;
            }
        } finally{
            session.close();
        }
    }

    @Override
    public void delete(User user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.delete(user);
            tx.commit();
        } catch(Exception e){
            if(tx != null){
                tx.rollback();
                throw e;
            }
        } finally{
            session.close();
        }
    }

    @Override
    public User get(Integer id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        User user = null;
        try{
            tx = session.beginTransaction();
            user = session.load(User.class, id);
            tx.commit();
        } catch(Exception e){
            if(tx != null){
                tx.rollback();
                throw e;
            }
        } finally{
            session.close();
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        List<User> users = null;
        try{
            tx = session.beginTransaction();
            Query query = session.createQuery("select u.username, u.firstname, u.lastname from User as u");
            users = query.list();
            tx.commit();
        } catch(Exception e){
            if(tx != null){
                tx.rollback();
                throw e;
            }
        } finally{
            session.close();
        }
        return users;
    }

    @Override
    public void deleteAll() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.createQuery("delete from User");
            tx.commit();
        } catch(Exception e){
            if(tx != null){
                tx.rollback();
                throw e;
            }
        } finally{
            session.close();
        }
    }
}
