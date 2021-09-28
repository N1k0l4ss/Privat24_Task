package service;


import model.Department;
import singleTone.SingleTone;

import javax.persistence.EntityManager;

public class DepartmentService {
    public static void createDepartment(Department department){
        EntityManager em = SingleTone.getSingleTone().getEm();
        em.getTransaction().begin();
        em.persist(department);
        em.getTransaction().commit();
    }

    public static void deleteDepartment(Department department){
        EntityManager em = SingleTone.getSingleTone().getEm();
        em.getTransaction().begin();
        em.remove(department);
        em.getTransaction().commit();
    }

    public static void updateDepartment(Department department){
        EntityManager em = SingleTone.getSingleTone().getEm();
        em.getTransaction().begin();
        department.setTitle("Edited");
        em.getTransaction().commit();
    }
}
