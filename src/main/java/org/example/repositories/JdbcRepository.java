package org.example.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public abstract class JdbcRepository<T, ID> {

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
            "    position_id   INTEGER REFERENCES positions(id)" +
            ");";

    private static final String SQL_CREATE_TABLE_DEPARTMENTS = "CREATE TABLE IF NOT EXISTS departments" +
            "(" +
            "    department_id     INTEGER PRIMARY KEY," +
            "    department_name   VARCHAR(255)," +
            "    head   INTEGER REFERENCES employees (id)," +
            "    number VARCHAR(20)," +
            "    email  VARCHAR(255)" +
            ");";
    private static final String SQL_ADD_REFERENCES_TO_EMPLOYEES = "ALTER TABLE employees ADD FOREIGN KEY (department_id)" +
            "REFERENCES departments(id);";


    public JdbcRepository() {
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

    abstract List<T> findAll();

    abstract T save(T entity);

    abstract Optional<T> findById(ID id);

    abstract void deleteById(ID id);

    abstract void delete(T entity);

}
