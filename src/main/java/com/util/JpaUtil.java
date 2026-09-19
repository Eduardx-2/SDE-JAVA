package com.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class JpaUtil {
    public static final String PERSISTENCE_UNIT = "SistemaPaquetesPU";
    private static EntityManagerFactory entityManagerFactory;
    private JpaUtil() {
    }
    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        return entityManagerFactory;
    }
    public static EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }
    public static synchronized void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
        entityManagerFactory = null;
    }
}
