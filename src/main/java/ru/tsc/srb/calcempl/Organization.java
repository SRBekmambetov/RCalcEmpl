package ru.tsc.srb.calcempl;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Organization {

    private Map<Integer, Department> departmentsMap;

    private List<List<Integer>> combinationIndexesList;
    private List<List<Employee>> transfersEmployeesList;

    public Organization() {
        this.departmentsMap = new HashMap<>();
        this.combinationIndexesList = new ArrayList<>();
        this.transfersEmployeesList = new ArrayList<>();
    }

    public void createEmployeeFromFile(String fileName) throws IOException, NumberFormatException {
        File file = new File(fileName);
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = "";
        int z = 1;
        while ((line = bufferedReader.readLine()) != null) {
            String[] arr = line.split("\\|");
            if (arr.length != 6) {
                System.out.println("Строка " + z + "введена некорректно");
                continue;
            }
            String firstName = arr[0];
            String secondName = arr[1];
            String middleName = arr[2];
            int idDepartment = Integer.parseInt(arr[3]);
            String nameDepartment = arr[4];
            BigDecimal salary = new BigDecimal(arr[5]);
            Employee employee = new Employee(firstName, secondName, middleName, salary);
            z++;
            if (salary.compareTo(new BigDecimal(0)) == -1) {
                System.out.println("Введена отрицательная зарплата в строке " + z);
                continue;
            }
            addEmployeeInMap(idDepartment, nameDepartment, employee);
        }
    }

    private void addEmployeeInMap(int idDepartment, String nameDepartment, Employee employee) {
        if (departmentsMap.containsKey(idDepartment)) {
            Department department = departmentsMap.get(idDepartment);
            List<Employee> employeesList = department.getEmployeesList();
            employeesList.add(employee);
            department.setEmployeesList(employeesList);
            departmentsMap.put(idDepartment, department);
        } else {
            List<Employee> employeesList = new ArrayList<>();
            employeesList.add(employee);
            Department department = new Department(nameDepartment, employeesList);
            departmentsMap.put(idDepartment, department);
        }
    }

    public void printDepartmentsInfo() {
        for (Map.Entry<Integer, Department> entry: departmentsMap.entrySet()) {
            int idDepartment = entry.getKey();
            Department department = entry.getValue();
            System.out.println("Department: ");
            System.out.printf("| %-2s | %-5s | \n", "id", "name");
            System.out.printf("| %-2s | %-5s | \n", "--", "-----");
            System.out.printf("| %-2s | %-5s | \n", idDepartment, department.getName());
            System.out.println();
            System.out.println("Employees: ");
            System.out.printf("| %-7s | %-7s | %-11s | %-4s | \n", "FirstName", "LastName", "MiddleName", "Salary");
            System.out.printf("| %-7s | %-7s | %-11s | %-4s | \n", "---------", "--------", "-----------", "------");
            for (Employee employee: department.getEmployeesList()) {
                System.out.printf("| %-9s | %-8s | %-11s | %-4s | \n", employee.getFirstName(), employee.getSecondName(), employee.getMiddleName(), employee.getSalary());
            }
            System.out.println();
            System.out.println();
        }
        System.out.println();
    }

    public void printAverageSalaryDepartments() {
        for (Map.Entry<Integer, Department> entry: departmentsMap.entrySet()) {
            Department department = entry.getValue();
            System.out.println("Average salary department " + department.getName() + " = " + department.calcAverageSalaryDepartments(department));
        }
        System.out.println();
        System.out.println();
    }

    public void calcAndPrintTransfersEmployees(int idDepartment1, int idDepartment2) throws IdNotFoundException {
        Department department1 = departmentsMap.get(idDepartment1);
        Department department2 = departmentsMap.get(idDepartment2);
        if (department1 == null || department2 == null) {
            throw new IdNotFoundException();
        }
        BigDecimal initialAverageAmount1 = department1.calcAverageSalaryDepartments(department1);
        BigDecimal initialAverageAmount2 = department2.calcAverageSalaryDepartments(department2);
        addSubset(department1.getEmployeesList().size() - 1);
        List<List<Employee>> subsetResult = createASubsetOfEmployees(department1.getEmployeesList());
        calcAverageSalaryFromList(subsetResult, initialAverageAmount1, initialAverageAmount2);
        printTransfersEmployees(department1, department2);
    }

    private void calcAverageSalaryFromList(List<List<Employee>> subsetResult, BigDecimal initialAverageAmount1, BigDecimal initialAverageAmount2) {
        for (List<Employee> employeesList: subsetResult) {
            BigDecimal sumSalary = new BigDecimal(0);
            for (Employee employee: employeesList) {
                BigDecimal salary = employee.getSalary();
                sumSalary = sumSalary.add(salary);
            }
            BigDecimal averageSalary = sumSalary.divide(new BigDecimal(employeesList.size()), 4, RoundingMode.HALF_UP);
            if (initialAverageAmount1.compareTo(averageSalary) == 1 && initialAverageAmount2.compareTo(averageSalary) == -1) {
                transfersEmployeesList.add(employeesList);
            }
        }
    }

    private void printTransfersEmployees(Department department1, Department department2) {
        if (!transfersEmployeesList.isEmpty()) {
            System.out.println("Transfers emloyees:");
            System.out.println(department1.getName() + " --> " + department2.getName());
            System.out.println();
            for (List<Employee> employeesList: transfersEmployeesList) {
                System.out.printf("| %-7s | %-7s | %-11s | %-4s | \n", "FirstName", "LastName", "MiddleName", "Salary");
                System.out.printf("| %-7s | %-7s | %-11s | %-4s | \n", "---------", "--------", "-----------", "------");
                for (Employee employee : employeesList) {
                    System.out.printf("| %-9s | %-8s | %-11s | %-4s | \n", employee.getFirstName(), employee.getSecondName(), employee.getMiddleName(), employee.getSalary());
                }
                System.out.println();
                System.out.println();
            }
        } else {
            System.out.println("Переводы сотрудников не увеличат среднюю зарплату");
        }
    }

    private void addSubset(int n) {
        int z = combinationIndexesList.size();
        for (int i = 0; i < z; i++) {
            List<Integer> list = combinationIndexesList.get(i);
            List<Integer> newList = new ArrayList<>(list);
            newList.add(n);
            combinationIndexesList.add(newList);
        }
        List<Integer> newList = new ArrayList<>();
        newList.add(n);
        combinationIndexesList.add(newList);
        if (n > 0) {
            addSubset(n - 1);
        }
    }

    private List<List<Employee>> createASubsetOfEmployees(List<Employee> employeeList) {
        List<List<Employee>> subsetResult = new ArrayList<>();
        for (int i = 0; i < combinationIndexesList.size(); i++) {
            subsetResult.add(new ArrayList<>());
            for (int j = 0; j < combinationIndexesList.get(i).size(); j++) {
                subsetResult.get(i).add(employeeList.get(combinationIndexesList.get(i).get(j)));
            }
        }
        return subsetResult;
    }
}
