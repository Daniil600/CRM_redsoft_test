package org.example.repositories;

import org.example.model.Employee;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Repository
public class EmployeeRepository extends JdbcRepository<Employee, Integer>{


    @Override
    public List<Employee> findAll() {
    final String SELECT_QUERY = "SELECT * FROM employees;";
        List<Employee> employeeList = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SELECT_QUERY);
            while (resultSet.next()) {
                Employee employee = getResultSet(resultSet);
                employeeList.add(employee);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employeeList;
    }

    @Override
    public Employee save(Employee entity) {
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {

            Integer idEmployee = entity.getIdEmployee();
            String firstName = entity.getFirstName();
            String lastName = entity.getLastName();
            Integer departmentId = entity.getDepartmentId();
            Integer positionId = entity.getPositionId();

            String SAVE = String.format("INSERT INTO employees VALUES ('%d', '%s', '%s', '%d', '%d')", idEmployee, firstName, lastName, departmentId, positionId);
            statement.executeUpdate(SAVE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return entity;
    }

    @Override
    public Optional<Employee> findById(Integer integer) {
        Optional<Employee> employeeOtional = Optional.empty();
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            String SELECT_BY_ID = "SELECT * FROM employees WHERE employee_id = " + integer + " LIMIT 1;";
            ResultSet resultSet = statement.executeQuery(SELECT_BY_ID);

            employeeOtional = Optional.of(getResultSet(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return employeeOtional;
    }

    @Override
    public void deleteById(Integer integer) {
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            String SELECT_BY_ID = "SELECT * FROM employees WHERE employee_id = " + integer + " LIMIT 1;";

            statement.executeQuery(SELECT_BY_ID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Employee entity) {
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            String DELETE_BY_ID = "DELETE * FROM employees WHERE employee_id = " + entity.getIdEmployee() + " LIMIT 1;";

            statement.executeQuery(DELETE_BY_ID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Employee getResultSet(ResultSet resultSet) throws SQLException {
        Integer idEmployee = resultSet.getInt("employee_id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        Integer departmentId = resultSet.getInt("department_id");
        Integer positionId = resultSet.getInt("position_id");

        Employee employee = new Employee();

        employee.setIdEmployee(idEmployee);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setDepartmentId(departmentId);
        employee.setPositionId(positionId);

        return employee;
    }
}
