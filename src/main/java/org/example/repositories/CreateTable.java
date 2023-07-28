package org.example.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateTable {
    public static final String DRIVER = "org.postgresql.Driver";
    public static final String DB = "crm_DB";
    public static final String URL_DRIVER = "jdbc:postgresql://localhost:5432/" + DB;
    public static final String USER = "crm_test";
    public static final String PASSWORD = "123";

    private static final String SQL_CREATE_TABLE_POSITIONS = "CREATE TABLE IF NOT EXISTS positions" +
            "(" +
            "    position_id       INTEGER PRIMARY KEY," +
            "    position_name VARCHAR(255)," +
            "    salary DECIMAL(10, 2)" +
            ");";

    private static final String SQL_CREATE_TABLE_EMPLOYEES = "CREATE TABLE IF NOT EXISTS employees" +
            "(" +
            "    employee_id            INTEGER PRIMARY KEY," +
            "    first_name          VARCHAR(255)," +
            "    last_name          VARCHAR(255)," +
            "    department_id INTEGER," +
            "    position_id   INTEGER REFERENCES positions(position_id)" +
            ");";

    private static final String SQL_CREATE_TABLE_DEPARTMENTS = "CREATE TABLE IF NOT EXISTS departments" +
            "(" +
            "    department_id     INTEGER PRIMARY KEY," +
            "    department_name   VARCHAR(255)," +
            "    head   INTEGER REFERENCES employees (employee_id)," +
            "    number VARCHAR(20)," +
            "    email  VARCHAR(255)" +
            ");";
    private static final String SQL_ADD_REFERENCES_TO_EMPLOYEES = "ALTER TABLE employees ADD FOREIGN KEY (department_id)" +
            "REFERENCES departments(department_id);";


    public CreateTable() {
        createTable();
    }

    private static void createTable() {
        try (Connection connection = DriverManager.getConnection(
                URL_DRIVER,
                USER,
                PASSWORD
        );
             Statement statement = connection.createStatement();
        ){
            statement.executeUpdate(SQL_CREATE_TABLE_POSITIONS);
            statement.executeUpdate(SQL_CREATE_TABLE_EMPLOYEES);
            statement.executeUpdate(SQL_CREATE_TABLE_DEPARTMENTS);
            statement.executeUpdate(SQL_ADD_REFERENCES_TO_EMPLOYEES);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Connected to database successfully");
    }
}
