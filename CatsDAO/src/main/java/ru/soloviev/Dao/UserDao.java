package ru.soloviev.Dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.soloviev.Entities.User;
import ru.soloviev.HibernateSessionFactoryUtil;

import java.util.List;

public class UserDao implements Dao<User> {

    @Override
    public User find(Integer id) {
        try (var session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            return session.get(User.class, id);
        }
    }

    @Override
    public User save(User user){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.persist(user);
        tx1.commit();
        session.close();
        return user;
    }

    @Override
    public List<User> findAll(){
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT a FROM User a", User.class).getResultList();
        }
    }

    @Override
    public User update(User user){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.merge(user);
        tx1.commit();
        session.close();
        return user;
    }

    @Override
    public User delete(User user){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.remove(user);
        tx1.commit();
        session.close();
        return user;
    }
}
