package udev.jsp.kmeroun.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import udev.jsp.kmeroun.models.Dish;
import udev.jsp.kmeroun.utils.HibernateSessionFactory;
import udev.jsp.kmeroun.utils.SerializableArrayList;


public class DishDao {
    public void saveDish(Dish dish){
        Transaction transaction = null;
        try(Session session = HibernateSessionFactory.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.save(dish);
            transaction.commit();
        } catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateDish(Dish dish) {
        Transaction transaction = null;
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(dish);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteDish(int id) {

        Transaction transaction = null;
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Dish dish = session.get(Dish.class, id);
            if (dish != null) {
                session.delete(dish);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Dish getDish(int id) {

        Transaction transaction = null;
        Dish dish = null;
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            dish = session.get(Dish.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return dish;
    }

    public SerializableArrayList<Dish> getDishes() {
        SerializableArrayList<Dish> dishList = new SerializableArrayList<>();

        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            dishList.addAll(session.createQuery(" FROM Dish", Dish.class).list());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dishList;
    }
}
