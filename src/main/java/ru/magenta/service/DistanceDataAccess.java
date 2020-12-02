package ru.magenta.service;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.magenta.backend.HibernateFactory;
import ru.magenta.entity.CityEntity;
import ru.magenta.entity.DistanceEntity;
import ru.magenta.exception.NoSuchDistanceMatrixRecord;

import java.util.List;

public class DistanceDataAccess implements EntityDataAccess<Integer, DistanceEntity> {
    private static DistanceDataAccess instance;

    private DistanceDataAccess() {

    }

    public static synchronized DistanceDataAccess getInstance() {
        if (instance == null) {
            return new DistanceDataAccess();
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

    public double getDistance(CityEntity from, CityEntity to) {
        Transaction transaction = null;
        double distance = 0;

        try (Session session = HibernateFactory.getSession()) {
            transaction = session.beginTransaction();

            int idFrom = from.getId();
            int idTo = to.getId();

            String sql = "SELECT distance " +
                    "FROM distance " +
                    "WHERE city_from = " + idFrom + " " +
                    "AND city_to = " + idTo;

            List result = session.createSQLQuery(sql).list();

            if (result.size() > 1) {
                throw new HibernateException("Returned more than 1 row");
            }

            if (result == null) {
                throw new NoSuchDistanceMatrixRecord("No records in Distance Matrix about these arguments");
            }

            distance = (Double) result.get(0);

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
        }

        return distance;
    }
}
