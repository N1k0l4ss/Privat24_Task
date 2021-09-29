package service;

import model.Department;
import model.Employee;
import singleTone.SingleTone;

import javax.persistence.EntityManager;

public class EmployeeService {
    public static void createEmployee(Employee employee){
        EntityManager em = SingleTone.getSingleTone().getEm();
        em.getTransaction().begin();
        em.persist(employee);
        em.getTransaction().commit();
    }

    public static void deleteEmployee(Employee employee){
        EntityManager em = SingleTone.getSingleTone().getEm();
        em.getTransaction().begin();
        em.remove(employee);
        em.getTransaction().commit();
    }

    public static void updateEmployee(Employee editedEmployee){
        EntityManager em = SingleTone.getSingleTone().getEm();
        em.getTransaction().begin();

        Employee employee = em.find(Employee.class, editedEmployee.getId());
        // todo setters

        em.getTransaction().commit();
    }
}
