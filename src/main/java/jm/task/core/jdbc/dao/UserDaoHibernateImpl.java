package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getConnection();

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.createNativeQuery("CREATE TABLE IF NOT EXISTS mydb.users" +
                    " (id mediumint not null auto_increment, name VARCHAR(50), " +
                    "lastname VARCHAR(50), " +
                    "age tinyint, " +
                    "PRIMARY KEY (id))").executeUpdate();
            session.getTransaction().commit();
            System.out.println("Таблица создана");
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.beginTransaction().rollback();
            }
        }finally {
            session.close();
        }
    }


    @Override
    public void dropUsersTable() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.createNativeQuery("DROP TABLE IF EXISTS mydb.users").executeUpdate();
            session.getTransaction().commit();
            System.out.println("Таблица удалена");
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.beginTransaction().rollback();
            }
        }finally {
            session.close();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
            System.out.println("User сохранен");
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.beginTransaction().rollback();
            }
        }finally {
            session.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.delete(session.get(User.class, id));
            session.getTransaction().commit();
            System.out.println("User удален");
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.beginTransaction().rollback();
            }
        }finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = null;
        Session session = sessionFactory.openSession();
        try {
            CriteriaQuery<User> criteriaQuery = session.getCriteriaBuilder().createQuery(User.class);
            criteriaQuery.from(User.class);
            session.beginTransaction();
            userList = session.createQuery(criteriaQuery).getResultList();
            session.getTransaction().commit();
            return userList;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.beginTransaction().rollback();
            }
        }finally {
            session.close();
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE mydb.users;").executeUpdate();
            session.getTransaction().commit();
            System.out.println("Таблица очищена");
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.beginTransaction().rollback();
            }
        }finally {
            session.close();
        }
    }
}
