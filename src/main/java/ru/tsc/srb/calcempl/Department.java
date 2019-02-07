package ru.tsc.srb.calcempl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Department {

    private int id;
    private String name;
    private List<Employee> employeesList;

    public Department(int id) {
        this.id = id;
        this.employeesList = new ArrayList<>();
    }

    public Department(int id, String name) {
        this.id = id;
        this.name = name;
        this.employeesList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployeesList() {
        return employeesList;
    }

    public void setEmployeesList(List<Employee> employeesList) {
        this.employeesList = employeesList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void addEmployee(String firstName, String secondName, String middleName, BigDecimal salary) {
        Employee employee = new Employee(firstName, secondName, middleName, salary);
        employeesList.add(employee);
    }
}
