package udev.jsp.kmeroun.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import udev.jsp.kmeroun.models.Dish;
import udev.jsp.kmeroun.utils.HibernateSessionFactory;

import java.util.List;

public class DishDao implements DaoInterface<Dish, Integer> {

    @Override
    public void save(Dish dish) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.save(dish);
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
    public void update(Dish dish) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.update(dish);
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
            session.createQuery("delete from Dish where id = :id");
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
    public Dish get(Integer id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        Dish dish = null;
        try{
            tx = session.beginTransaction();
            dish = session.load(Dish.class, id);
            tx.commit();
        } catch(Exception e){
            if(tx != null){
                tx.rollback();
                throw e;
            }
        } finally{
            session.close();
        }
        return dish;
    }

    @Override
    public List<Dish> findAll() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        List<Dish> dishes = null;
        try{
            tx = session.beginTransaction();
            Query query = session.createQuery("from Dish");
            dishes = query.list();
            tx.commit();
        } catch(Exception e){
            if(tx != null){
                tx.rollback();
                throw e;
            }
        } finally{
            session.close();
        }
        return dishes;
    }

    @Override
    public void deleteAll() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.createQuery("delete from Dish");
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
