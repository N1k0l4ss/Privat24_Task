package model;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Collection;

@Entity
@NamedQueries({
        @NamedQuery(name = "Department.findAll", query = "select d from Department d")
})
@Cacheable(false)
public class Department {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @Basic
    private String title;
    @Basic
    private boolean freeWork;
    @ManyToOne(optional = false)
    private Company company;
    @OneToOne(optional = false)
    private Preference workMode;
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private Collection<Employee> employees;
    @Basic
    private LocalTime startTime;

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

    public Preference getWorkMode() {
        return workMode;
    }

    public void setWorkMode(Preference workMode) {
        this.workMode = workMode;
    }

    public Collection<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Collection<Employee> employees) {
        this.employees = employees;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public String getPreferenceName(){
        if (workMode == null)
            return "";
        return workMode.getPreferenceName();
    }

    public String getWorkTimeString() {
        if (startTime == null) return "";
        return startTime.toString() + "-" + startTime.plusHours(9).toString(); }

    @Override
    public String toString(){ return title; }
}
