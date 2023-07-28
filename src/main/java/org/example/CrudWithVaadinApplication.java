package org.example;

import org.example.repositories.CreateTable;
import org.example.repositories.EmployeeRepository;
import org.example.ui.MainView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrudWithVaadinApplication {
    public static void main(String[] args) {
        CreateTable createTable = new CreateTable();
        SpringApplication.run(CrudWithVaadinApplication.class, args);
    }
}