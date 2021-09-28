package service;

import model.Company;
import singleTone.SingleTone;

import javax.persistence.EntityManager;
import java.util.List;

public class CompanyService {
    public static List<Company> findAll(){
        return SingleTone.getSingleTone().getEm().createNamedQuery("Company.findAll", Company.class).getResultList();
    }

    public static void createCompany(Company company){
        EntityManager em = SingleTone.getSingleTone().getEm();
        em.getTransaction().begin();
        em.persist(company);
        em.getTransaction().commit();
    }

    public static void deleteCompany(Company company){
        EntityManager em = SingleTone.getSingleTone().getEm();
        em.getTransaction().begin();
        em.remove(company);
        em.getTransaction().commit();
    }
}
