package ru.magenta.service;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.magenta.util.HibernateFactory;
import ru.magenta.entity.DistanceEntity;

import java.util.List;

public class DistanceDataAccess implements EntityDataAccess<Integer, DistanceEntity> {
    private static DistanceDataAccess instance;

    private DistanceDataAccess() {

    }

    public static synchronized DistanceDataAccess getInstance() {
        if (instance == null) {
            instance = new DistanceDataAccess();
        }
        return instance;
    }

    @Override
    public List<DistanceEntity> getAll() {
        Transaction transaction = null;
        List<DistanceEntity> distances = null;

        try (Session session = HibernateFactory.getSession()) {
            transaction = session.beginTransaction();

            distances = session.createQuery("FROM DistanceEntity").list();

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
        }

        return distances;
    }

    @Override
    public DistanceEntity get(Integer key) {
        Transaction transaction = null;
        DistanceEntity distance = null;

        try (Session session = HibernateFactory.getSession()) {
            transaction = session.beginTransaction();

            distance = session.get(DistanceEntity.class, key);

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
        }

        return distance;
    }

    @Override
    public void save(DistanceEntity entity) {
        Transaction transaction = null;

        try (Session session = HibernateFactory.getSession()) {
            transaction = session.beginTransaction();

            session.save(entity);

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
        }
    }

    @Override
    public void update(Integer keyOld, DistanceEntity newEntity) {
        Transaction transaction = null;
        DistanceEntity oldDistance;

        try (Session session = HibernateFactory.getSession()) {
            transaction = session.beginTransaction();

            oldDistance = session.get(DistanceEntity.class, keyOld);
            oldDistance.setCityFrom(newEntity.getCityFrom());
            oldDistance.setCityTo(newEntity.getCityTo());
            oldDistance.setDistance(newEntity.getDistance());

            session.update(oldDistance);

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
        }
    }

    @Override
    public void delete(Integer key) {
        Transaction transaction = null;
        DistanceEntity city;

        try (Session session = HibernateFactory.getSession()) {
            transaction = session.beginTransaction();

            city = session.get(DistanceEntity.class, key);
            session.delete(city);

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
        }
    }

}
