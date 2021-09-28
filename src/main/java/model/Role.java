package model;

import javax.persistence.*;
import java.util.Collection;

@Entity
@NamedQueries({
        @NamedQuery(name = "Role.findAll", query = "select r from Role r")
})
@Cacheable(false)
public class Role {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @Basic
    private String title;
    @Basic
    private boolean freeWork;
    @ManyToOne(optional = false)
    private Company company;
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private Collection<Employee> employees;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFreeWork() {
        return freeWork;
    }

    public void setFreeWork(boolean freeWork) {
        this.freeWork = freeWork;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Collection<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Collection<Employee> employees) {
        this.employees = employees;
    }
}
