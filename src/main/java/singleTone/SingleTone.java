package singleTone;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class SingleTone {
    private static SingleTone singleTone;
    private EntityManagerFactory factory;
    private EntityManager em;
    private SingleTone() {
        factory = Persistence.createEntityManagerFactory("MyPU");
        em = factory.createEntityManager();
    }

    public static SingleTone getSingleTone() {
        if (singleTone == null)
            singleTone = new SingleTone();
        return singleTone;
    }

    public EntityManagerFactory getFactory() {
        return factory;
    }

    public EntityManager getEm() {
        return em;
    }
}
