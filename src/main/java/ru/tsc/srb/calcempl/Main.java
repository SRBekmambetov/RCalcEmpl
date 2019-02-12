package ru.tsc.srb.calcempl;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Некорректно введены параметры на вход");
            return;
        }
        String fileName = args[0];
        int idDepartment1 = Integer.parseInt(args[1]);
        int idDepartment2 = Integer.parseInt(args[2]);
        Organization organization = new Organization();
        try {
            organization.createEmployeeFromFile(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
            return;
        } catch (IOException e) {
            System.out.println("Файл не доступен для чтения");
            return;
        } catch (NumberFormatException e) {
            System.out.println("Зарплата введена неверно");
            return;
        }
        organization.printDepartmentsInfo();
        organization.printAverageSalaryDepartments();
        try {
            organization.calcAndPrintTransfersEmployees(idDepartment1, idDepartment2);
        } catch (IdNotFoundException e) {
            System.out.println("Отдел не найден");
            return;
        }
    }
}
