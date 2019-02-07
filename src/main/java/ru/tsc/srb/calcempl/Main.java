package ru.tsc.srb.calcempl;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Organization organization = new Organization();
        String nameFile = args[0];
        int idDepartment1 = Integer.parseInt(args[1]);
        int idDepartment2 = Integer.parseInt(args[2]);
        try {
            organization.readFile(nameFile);
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
            return;
        } catch (IOException e) {
            System.out.println("Файл не доступен для чтения");
            return;
        } catch (NumberFormatException e) {
            System.out.println("Сумма введена неверно");
            return;
        }
        organization.printDepartmentsInfo();
        organization.printAverageSalaryDepartments();
        try {
            organization.calcAndPrintTransfersEmployees(idDepartment1, idDepartment2);
        } catch (Exception e) {
            System.out.println("Отдел не найден");
            return;
        }
    }
}
