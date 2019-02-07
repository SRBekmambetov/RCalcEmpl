import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Organization {

    private static List<Department> departmentsList;
    private static List<List<Employee>> transfersEmployeesList;

    public Organization() {
        this.departmentsList = new ArrayList<>();
        this.transfersEmployeesList = new ArrayList<>();
    }

    public List<Department> getDepartmentsList() {
        return departmentsList;
    }

    public void setDepartmentsList(List<Department> departmentsList) {
        this.departmentsList = departmentsList;
    }

    public void readFile(String nameFile) throws IOException, NumberFormatException {
        File file = new File(nameFile);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            String[] arr = line.split("\\|");
            if (arr.length != 6) {
                continue;
            }
            String firstName = arr[0];
            String secondName = arr[1];
            String middleName = arr[2];
            int idDepartment = Integer.parseInt(arr[3]);
            String nameDepartment = arr[4];
            BigDecimal salary = new BigDecimal(arr[5]);
            if (salary.compareTo(new BigDecimal(0)) == -1) {
                continue;
            }
            processData(firstName, secondName, middleName, idDepartment, nameDepartment, salary);
        }
    }

    private static void processData(String firstName, String secondName, String middleName, int idDepartment, String nameDepartment, BigDecimal salary) {
        Department department = new Department(idDepartment, nameDepartment);
        int index = departmentsList.indexOf(department);
        if (index == -1) {
            departmentsList.add(department);
        } else {
            department = departmentsList.get(index);
        }
        department.addEmployee(firstName, secondName, middleName, salary);
    }

    public void printDepartmentsInfo() {
        for (Department department: departmentsList) {
            System.out.println(department.getName());
            for (Employee employee: department.getEmployeesList()) {
                System.out.printf("| %-7s | %-7s | %-14s | %-4s | \n", employee.getFirstName(), employee.getSecondName(), employee.getMiddleName(), employee.getSalary());
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printAverageSalaryDepartments() {
        for (Department department: departmentsList) {
            System.out.println("Average salary department " + department.getName() + " = " + calcAverageSalaryDepartments(department));
        }
        System.out.println();
        System.out.println();
    }

    private static BigDecimal calcAverageSalaryDepartments(Department department) {
        BigDecimal sumSalary = new BigDecimal(0);
        for (Employee employee: department.getEmployeesList()) {
            BigDecimal salary = employee.getSalary();
            sumSalary = sumSalary.add(salary);
        }
        BigDecimal averageSalary = sumSalary.divide(new BigDecimal(department.getEmployeesList().size()), 4, RoundingMode.HALF_UP);
        return averageSalary;
    }

    public void calcAndPrintTransfersEmployees(int idDepartment1, int idDepartment2) throws Exception {
        Department department1 = new Department(idDepartment1);
        Department department2 = new Department(idDepartment2);
        int indexDepartment1 = departmentsList.indexOf(department1);
        int indexDepartment2 = departmentsList.indexOf(department2);
        if (indexDepartment1 == -1 || indexDepartment2 == -1) {
            throw new Exception();
        }
        department1 = departmentsList.get(indexDepartment1);
        department2 = departmentsList.get(indexDepartment2);
        BigDecimal initialAverageAmount1 = calcAverageSalaryDepartments(department1);
        BigDecimal initialAverageAmount2 = calcAverageSalaryDepartments(department2);
        for (int k = 1; k <= department1.getEmployeesList().size(); k++) {
            List<List<Employee>> subsetResult = getSubsets(department1.getEmployeesList(), k);
            calcAverageSalaryFromList(subsetResult, initialAverageAmount1, initialAverageAmount2);
        }
        printTransfersEmployees(department1, department2);
    }

    private static void printTransfersEmployees(Department department1, Department department2) {
        if (!transfersEmployeesList.isEmpty()) {
            System.out.println("Transfers emloyees:");
            System.out.println(department1.getName() + " --> " + department2.getName());
            System.out.println();
            for (List<Employee> employeesList: transfersEmployeesList) {
                for (Employee employee : employeesList) {
                    System.out.printf("| %-7s | %-5s | %-14s | %-2s | \n", employee.getFirstName(), employee.getSecondName(), employee.getMiddleName(), employee.getSalary());
                }
                System.out.println();
            }
        } else {
            System.out.println("Переводы сотрудников не увеличат среднюю зарплату");
        }
    }

    private static void calcAverageSalaryFromList(List<List<Employee>> subsetResult, BigDecimal initialAverageAmount1, BigDecimal initialAverageAmount2) {
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

    private static List<List<Employee>> getSubsets(List<Employee> input, int k) {
        List<List<Employee>> subsets = new ArrayList<>();
        int[] s = new int[k];               //Здесь мы будем хранить индексы
        if (k <= input.size()) {
            // Первая индексная последовательность: 0, 1, 2, ...
            for (int i = 0; (s[i] = i) < k - 1; i++) ;
            subsets.add(getSubset(input, s));
            for (; ; ) {
                int i;
                // Найдем позицию элемента, которую можно инкрементировать
                for (i = k - 1; i >= 0 && s[i] == input.size() - k + i; i--) ;
                if (i < 0) {
                    break;
                } else {
                    s[i]++;                 // Инкрементируем этот элемент
                    for (++i; i < k; i++) { // Заполняем оставшиеся элементы
                        s[i] = s[i - 1] + 1;
                    }
                    subsets.add(getSubset(input, s));
                }
            }
        }
        return subsets;
    }

    // Генерируем подмножество по порядку индекса
    private static List<Employee> getSubset(List<Employee> input, int[] subset) {
        List<Employee> result = new ArrayList<>(subset.length);
        for (int s : subset) {
            result.add(input.get(s));
        }
        return result;
    }
}