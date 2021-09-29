package service;

import model.Role;
import singleTone.SingleTone;

import javax.persistence.EntityManager;

public class RoleService {
    public static void createRole(Role role){
        EntityManager em = SingleTone.getSingleTone().getEm();
        em.getTransaction().begin();
        em.persist(role);
        em.getTransaction().commit();
    }

    public static void deleteRole(Role role) {
        EntityManager em = SingleTone.getSingleTone().getEm();
        em.getTransaction().begin();
        em.remove(role);
        em.getTransaction().commit();
    }
}
