package logic;

import model.*;
import singleTone.SingleTone;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 1. Work another time - coefficient 0.2
 * 2. Work default time - coefficient 0
 * 3. Work at home - coefficient 0.1
 * */
public class ProfitCalculator
{
    private final LocalTime defaultTime = LocalTime.of(8,00);
    private final double defaultHourSalary = 10;
    private Company company;
    private List preferences;
    private Collection<Employee> employees;
    private DecimalFormat df = new DecimalFormat("###.##");
    private List<Profit> profitList = new ArrayList<>();

    public ProfitCalculator(Company company) {
        this.company = company;
        this.preferences = SingleTone.getSingleTone().getEm().createQuery("select p from Preference p").getResultList();
        if (this.company != null){
            employees = company.getEmployees();
        }
    }

    private void initCompanyProfit(){
        double sum = 0;
        for (Employee employee : employees)
            sum += employeeHourCalc(employee);
        String companyName = "Company: " + company.getTitle();
        String hourProfit = df.format(sum);
        String dollarsProfit = df.format(defaultHourSalary * sum);
        profitList.add(new Profit(companyName, hourProfit, dollarsProfit));
    }

    private void initDepartmentsProfit(){
        Collection<Department> departments = company.getDepartaments();
        for (Department department : departments) {
            Collection<Employee> employees = department.getEmployees();
            double sum = 0;
            for (Employee employee : employees) {
                sum += employeeHourCalc(employee);
            }
            String departmentTitle = "Department: " + department.getTitle();
            String hourProfit = df.format(sum);
            String dollarsProfit = df.format(defaultHourSalary * sum);
            profitList.add(new Profit(departmentTitle, hourProfit, dollarsProfit));
        }
    }

    private void initEmployeesProfit(){
        StringBuilder employeesProfit = new StringBuilder();
        for (Employee employee : employees) {
            double sum = employeeHourCalc(employee);
            String employeeName = "Employee: " + employee.getName();
            String hourProfit = df.format(sum);
            String dollarsProfit = df.format(defaultHourSalary * sum);
            profitList.add(new Profit(employeeName, hourProfit, dollarsProfit));
        }
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

    public List<Profit> getProfitList() {
        initCompanyProfit();
        initDepartmentsProfit();
        initEmployeesProfit();
        return profitList;
    }
}
