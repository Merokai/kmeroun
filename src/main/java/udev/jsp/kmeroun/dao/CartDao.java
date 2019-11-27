package udev.jsp.kmeroun.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import udev.jsp.kmeroun.enums.OrderStatus;
import udev.jsp.kmeroun.models.Cart;
import udev.jsp.kmeroun.models.User;
import udev.jsp.kmeroun.utils.HibernateSessionFactory;

import java.util.List;

public class CartDao implements DaoInterface<Cart, Integer> {

    @Override
    public void save(Cart cart) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.save(cart);
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
    public void update(Cart cart) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.update(cart);
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
            session.createQuery("delete from Cart where id = :id");
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
    public Cart get(Integer id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        Cart cart = null;
        try{
            tx = session.beginTransaction();
            cart = session.load(Cart.class, id);
            tx.commit();
            return cart;
        } catch(Exception e){
            if(tx != null){
                tx.rollback();
            }
        } finally{
            session.close();
        }
        return cart;
    }

    public Cart findCurrent(User user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        Cart cart = null;
        try{
            tx = session.beginTransaction();
            Query query = session.createQuery("from Cart where customer.id = :u and order is null");
            query.setParameter("u", user.getId());
            cart = (Cart) query.getSingleResult();
            tx.commit();
        } catch(Exception e){
            if(tx != null){
                tx.rollback();
            }
        } finally{
            session.close();
        }
        return cart;
    }

    @Override
    public List findAll() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        List carts = null;
        try{
            tx = session.beginTransaction();
            Query query = session.createQuery("from Cart");
            carts = query.getResultList();
            tx.commit();
        } catch(Exception e){
            if(tx != null){
                tx.rollback();
            }
        } finally{
            session.close();
        }
        return carts;
    }

    @Override
    public void deleteAll() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.createQuery("delete from Cart");
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
