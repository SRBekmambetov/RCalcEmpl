package ru.tsc.srb.calcempl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Department {

    private String name;
    private List<Employee> employeesList;

    public Department(String name, List<Employee> employeesList) {
        this.name = name;
        this.employeesList = employeesList;
    }

    public String getName() {
        return name;
    }

    public List<Employee> getEmployeesList() {
        return employeesList;
    }

    public void setEmployeesList(List<Employee> employeesList) {
        this.employeesList = employeesList;
    }

    public BigDecimal calcAverageSalaryDepartments(Department department) {
        BigDecimal sumSalary = new BigDecimal(0);
        for (Employee employee: department.getEmployeesList()) {
            BigDecimal salary = employee.getSalary();
            sumSalary = sumSalary.add(salary);
        }
        BigDecimal averageSalary = sumSalary.divide(new BigDecimal(department.getEmployeesList().size()), 4, RoundingMode.HALF_UP);
        return averageSalary;
    }
}
