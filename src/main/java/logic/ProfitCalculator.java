package logic;

import model.*;
import singleTone.SingleTone;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;


/**
 * 1. Work another time - coefficient 0.2
 * 2. Work default time - coefficient 0
 * 3. Work at home - coefficient 0.1
 * */
public class ProfitCalculator
{ // todo salaryS
    private final LocalTime defaultTime = LocalTime.of(9,00);
    private final double defaultHourSalary = 10;
    private Company company;
    private List preferences;
    private Collection<Employee> employees;


    public ProfitCalculator(Company company) {
        this.company = company;
        this.preferences = SingleTone.getSingleTone().getEm().createQuery("select p from Preference p").getResultList();
        if (this.company != null){
            employees = company.getEmployees();
        }
        //todo temporary
        calcProfit();
    }

    private void calcProfit(){
        if (company == null) return;
        System.out.println(company);
        double hourCounter = 0;
        for (Employee employee : employees) {
            hourCounter += employeeHourCalc(employee);
        }
        DecimalFormat df = new DecimalFormat("###.###");
        String format = df.format(hourCounter);
        System.out.println("Hour profit = " + format + " employee/hours");
        System.out.println();
    }

    private double employeeHourCalc(Employee employee) {
        Department employeeDepartment = employee.getDepartament();
        LocalTime departmentStartTime = employeeDepartment.getStartTime();
        LocalTime employeeStartTime = employee.getStartTime();
        Preference employeePreference = employee.getPreference();
        Preference departmentWorkMode = employeeDepartment.getWorkMode();
        Role employeeRole = employee.getRole();
        double minutesOffset;
        double profitHour;
        double res = 0;
        // Receive time offset
        if (employeeStartTime != null){
            if (departmentWorkMode == null || employeeStartTime.equals(departmentStartTime))
                minutesOffset = getMinutesOffset(defaultTime, employeeStartTime);
            else
                minutesOffset = getMinutesOffset(departmentStartTime, employeeStartTime);
            profitHour = employeePreference.getCoefficient() * (minutesOffset / 60);
        } else profitHour = 8 * employeePreference.getCoefficient();
        // Defines if this profit is positive or negative
        if ((departmentStartTime != null && !employeeStartTime.equals(departmentStartTime)) || !employeeRole.isFreeWork())
            res -= profitHour;
        else res += profitHour;
    return res;
}

    private double getMinutesOffset(LocalTime timeA, LocalTime timeB){
         double timeAmins = timeA.getMinute() + timeA.getHour() * 60;
         double timeBmins = timeB.getMinute() + timeB.getHour() * 60;
         return Math.abs(timeAmins - timeBmins);
    }
}
