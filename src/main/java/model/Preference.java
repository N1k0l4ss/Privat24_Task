package model;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@NamedQueries({
        @NamedQuery(name = "Preference.findAll", query = "select p from Preference p")
})
public class Preference {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @Basic
    private double coefficient;
    @Basic
    private String preferenceName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public String getPreferenceName() {
        return preferenceName;
    }

    public void setPreferenceName(String preferenceType) {
        this.preferenceName = preferenceType;
    }

    @Override
    public String toString() {
        return preferenceName;
    }
}
