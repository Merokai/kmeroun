package udev.jsp.kmeroun.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import udev.jsp.kmeroun.models.Dish;
import udev.jsp.kmeroun.models.Order;
import udev.jsp.kmeroun.models.User;
import udev.jsp.kmeroun.utils.HibernateSessionFactory;
import udev.jsp.kmeroun.utils.SerializableArrayList;

import java.util.List;

public class OrderDao {
    public void saveOrder(Order order){
        Transaction transaction = null;
        try(Session session = HibernateSessionFactory.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
        } catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateOrder(Order order) {
        Transaction transaction = null;
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(order);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteOrder(int id) {

        Transaction transaction = null;
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Order order = session.get(Order.class, id);
            if (order != null) {
                session.delete(order);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Order getOrder(int id) {

        Transaction transaction = null;
        Order order = null;
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            order = session.get(Order.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return order;
    }

    public SerializableArrayList<Order> getOrders() {
        SerializableArrayList<Order> orderList = new SerializableArrayList<>();

        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            orderList.addAll(session.createQuery(" from Order", Order.class).list());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public Order getCurrentOrder(String username){
        Order order = null;
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Query query = session.createQuery(" from Order WHERE customer_username = :c AND (status = 'CREATION' OR status = 'PREPARATION' OR status = 'WAITING_DELIVERY')", Order.class);
            query.setParameter("c", username);
            order = (Order) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }
}
