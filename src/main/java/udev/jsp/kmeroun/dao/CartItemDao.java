package udev.jsp.kmeroun.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import udev.jsp.kmeroun.models.Cart;
import udev.jsp.kmeroun.models.CartItem;
import udev.jsp.kmeroun.models.User;
import udev.jsp.kmeroun.utils.HibernateSessionFactory;

import java.util.List;

public class CartItemDao implements DaoInterface<CartItem, Integer> {

    @Override
    public void save(CartItem cartItem) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.save(cartItem);
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
    public void update(CartItem cartItem) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.update(cartItem);
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
            session.createQuery("delete from CartItem where id = :id");
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
    public CartItem get(Integer id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        CartItem cartitem = null;
        try{
            tx = session.beginTransaction();
            cartitem = session.load(CartItem.class, id);
            tx.commit();
        } catch(Exception e){
            if(tx != null){
                tx.rollback();
            }
        } finally{
            session.close();
        }
        return cartitem;
    }

    @Override
    public List findAll() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        List carts = null;
        try{
            tx = session.beginTransaction();
            Query query = session.createQuery("from CartItem");
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
            session.createQuery("delete from CartItem");
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
