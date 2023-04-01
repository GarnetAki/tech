package ru.soloviev.Dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.soloviev.Entities.Cat;
import ru.soloviev.HibernateSessionFactoryUtil;

import java.util.List;

public class CatDao implements Dao<Cat> {

    @Override
    public Cat find(Integer id) {
        try (var session =  HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            return session.get(Cat.class, id);
        }
    }

    @Override
    public Cat save(Cat cat){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.persist(cat);
        tx1.commit();
        session.close();
        return cat;
    }

    @Override
    public Cat update(Cat cat){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.merge(cat);
        tx1.commit();
        session.close();
        return cat;
    }

    @Override
    public Cat delete(Cat cat){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.remove(cat);
        tx1.commit();
        session.close();
        return cat;
    }

    @Override
    public List<Cat> findAll(){
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT a FROM Cat a", Cat.class).getResultList();
        }
    }

    public Cat addFriend(Integer catId, Integer friendId) {
        try (var session =  HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx1 = session.beginTransaction();
            Cat cat = session.get(Cat.class, catId);
            Cat friend = session.get(Cat.class, friendId);
            cat.getFriends().add(friend);
            friend.getFriends().add(cat);
            tx1.commit();
            return cat;
        }
    }

    public void removeFriend(Integer petId, Integer friendId) {
        try (var session =  HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            Transaction tx1 = session.beginTransaction();
            Cat cat = session.get(Cat.class, petId);
            Cat friend = session.get(Cat.class, friendId);
            cat.getFriends().remove(friend);
            friend.getFriends().remove(cat);
            tx1.commit();
        }
    }
}