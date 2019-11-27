package udev.jsp.kmeroun.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import udev.jsp.kmeroun.models.Order;
import udev.jsp.kmeroun.models.User;
import udev.jsp.kmeroun.utils.HibernateSessionFactory;

import java.util.List;

public class OrderDao implements DaoInterface<Order, Integer> {

    @Override
    public void save(Order order) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.save(order);
            session.update(order.getCart());
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
    public void update(Order order) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.update(order);
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
    public void delete(Integer id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.createQuery("delete from Order where id = :id");
            session.setProperty("id", id);
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
    public Order get(Integer id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        Order order = null;
        try{
            tx = session.beginTransaction();
            order = session.load(Order.class, id);
            tx.commit();
            return order;
        } catch(Exception e){
            if(tx != null){
                tx.rollback();
                throw e;
            }
        } finally{
            session.close();
        }
        return order;
    }

    public List findCurrent(User user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        List orders = null;
        try{
            tx = session.beginTransaction();
            Query query = session.createQuery("from Order where customer.id = :u");
            query.setParameter("u", user.getId());
            orders = query.list();
            tx.commit();
        } catch(Exception e){
            if(tx != null){
                tx.rollback();
                throw e;
            }
        } finally{
            session.close();
        }
        return orders;
    }

    @Override
    public List findAll() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        List orders = null;
        try{
            tx = session.beginTransaction();
            Query query = session.createQuery("from Order");
            orders = query.getResultList();
            tx.commit();
        } catch(Exception e){
            if(tx != null){
                tx.rollback();
                throw e;
            }
        } finally{
            session.close();
        }
        return orders;
    }

    public List findAllForUser(User user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        List orders = null;
        try{
            tx = session.beginTransaction();
            Query query = session.createQuery("from Order inner join Cart where customer.id = :u");
            query.setParameter("u", user.getId());
            orders = query.getResultList();
            tx.commit();
        } catch(Exception e){
            if(tx != null){
                tx.rollback();
                throw e;
            }
        } finally{
            session.close();
        }
        return orders;
    }

    @Override
    public void deleteAll() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.createQuery("delete from Order");
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
