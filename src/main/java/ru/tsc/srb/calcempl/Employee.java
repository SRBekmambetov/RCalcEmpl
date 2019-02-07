package ru.tsc.srb.calcempl;

import java.math.BigDecimal;

public class Employee {

    private String firstName;
    private String secondName;
    private String middleName;
    private BigDecimal salary;

    public Employee(String firstName, String secondName, String middleName, BigDecimal salary) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.middleName = middleName;
        this.salary = salary;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}
