package udev.jsp.kmeroun.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import udev.jsp.kmeroun.models.Order;
import udev.jsp.kmeroun.models.User;
import udev.jsp.kmeroun.utils.HibernateSessionFactory;

import java.util.List;

public class UserDao {

    public User get(String username) {
        User user = null;
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            user = session.get(User.class, username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public void save(User user) {
            Transaction transaction = null;
            try(Session session = HibernateSessionFactory.getSessionFactory().openSession()){
                transaction = session.beginTransaction();
                session.save(user);
                transaction.commit();
            } catch (Exception e){
                if(transaction != null){
                    transaction.rollback();
                }
                e.printStackTrace();
            }
    }
}
