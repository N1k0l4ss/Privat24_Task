package model;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Cacheable(false)
public class Employee {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @OneToOne(optional = false)
    private Preference preference;
    @ManyToOne(optional = false)
    private Department department;
    @Basic
    private String name;
    @ManyToOne(optional = false)
    private Role role;
    @Basic
    private LocalTime startTime;
    @ManyToOne(optional = false)
    private Company company;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }

    public Department getDepartament() {
        return department;
    }

    public void setDepartament(Department department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public String getPreferenceString(){
        return preference.getPreferenceName();
    }

    public String getDepartmentString(){
        return department.getTitle();
    }

    public String getRoleString(){
        return role.getTitle();
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getTimeString() { if (startTime == null) return ""; return startTime.toString() + "-" + startTime.plusHours(9).toString(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return id == employee.id && Objects.equals(preference, employee.preference) && Objects.equals(name, employee.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, preference, name);
    }
}
