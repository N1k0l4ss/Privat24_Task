package model;

import javax.persistence.*;
import java.util.Collection;

@Entity
@NamedQueries({
        @NamedQuery(name = "Company.findAll", query = "select c from Company c")
})
@Cacheable(false)
public class Company {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @Basic
    private String title;
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Collection<Department> departments;
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Collection<Role> roles;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Collection<Department> getDepartaments() {
        return departments;
    }

    public void setDepartaments(Collection<Department> departments) {
        this.departments = departments;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    @OneToMany(mappedBy = "company")
    private Collection<Employee> employees;

    public Collection<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Collection<Employee> employees) {
        this.employees = employees;
    }
}
