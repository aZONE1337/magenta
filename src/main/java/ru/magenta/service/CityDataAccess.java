package ru.magenta.service;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.magenta.util.HibernateFactory;
import ru.magenta.entity.CityEntity;

import java.util.List;

public class CityDataAccess implements EntityDataAccess<Integer, CityEntity> {
    private static CityDataAccess instance;

    private CityDataAccess() {

    }

    public static synchronized CityDataAccess getInstance() {
        if (instance == null) {
            instance = new CityDataAccess();
        }
        return instance;
    }

    @Override
    public List<CityEntity> getAll() {
        Transaction transaction = null;
        List<CityEntity> cities = null;

        try (Session session = HibernateFactory.getSession()) {
            transaction = session.beginTransaction();

            cities = session.createQuery("FROM CityEntity").list();

            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) transaction.rollback();
        }

        return cities;
    }

    @Override
    public CityEntity get(Integer key) {
        Transaction transaction = null;
        CityEntity city = null;

        try (Session session = HibernateFactory.getSession()) {
            transaction = session.beginTransaction();

            city = session.get(CityEntity.class, key);

            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) transaction.rollback();
        }

        return city;
    }

    @Override
    public void save(CityEntity entity) {
        Transaction transaction = null;

        try (Session session = HibernateFactory.getSession()) {
            transaction = session.beginTransaction();

            session.save(entity);

            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) transaction.rollback();
        }
    }

    @Override
    public void update(Integer keyOld, CityEntity newEntity) {
        Transaction transaction = null;
        CityEntity oldCity;

        try (Session session = HibernateFactory.getSession()) {
            transaction = session.beginTransaction();

            oldCity = session.get(CityEntity.class, keyOld);
            oldCity.setName(newEntity.getName());
            oldCity.setLatitude(newEntity.getLatitude());
            oldCity.setLongitude(newEntity.getLongitude());

            session.update(oldCity);

            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) transaction.rollback();
        }
    }

    @Override
    public void delete(Integer key) {
        Transaction transaction = null;
        CityEntity city;

        try (Session session = HibernateFactory.getSession()) {
            transaction = session.beginTransaction();

            city = session.get(CityEntity.class, key);
            session.delete(city);

            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) transaction.rollback();
        }
    }
}
