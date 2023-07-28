package org.example;

import org.example.repositories.CreateTable;
import org.example.repositories.EmployeeRepository;
import org.example.ui.MainView;

public class Main {
    public static void main(String[] args) {
        CreateTable createTable = new CreateTable();
        MainView mainView = new MainView(new EmployeeRepository());
    }
}