package org.example.repositories;

import org.example.model.Department;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class DepartmentRepository extends JdbcRepository<Department, Integer> {
    private static final String SELECT_QUERY = "SELECT * FROM departments;";


    @Override
    public List<Department> findAll() {
        List<Department> departmentList = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SELECT_QUERY);
            while (resultSet.next()) {
                Department department = getResultSet(resultSet);
                departmentList.add(department);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return departmentList;
    }

    @Override
    public Department save(Department entity) {
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {

            Integer idDepartment = entity.getIdDepartment();
            String departmentName = entity.getDepartmentName();
            Integer head = entity.getHead();
            String number = entity.getNumber();
            String email = entity.getEmail();

            String SAVE = String.format("INSERT INTO departments VALUES ('%d', '%s', '%d', '%s', '%s')", idDepartment, departmentName, head, number, email);
            statement.executeUpdate(SAVE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return entity;
    }

    @Override
    public Optional<Department> findById(Integer integer) {
        Optional<Department> departmentOptional = Optional.empty();
        List<Department> departmentList = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            String SELECT_BY_ID = "SELECT * FROM departments WHERE department_id = " + integer + ";";
            ResultSet resultSet = statement.executeQuery(SELECT_BY_ID);
            while (resultSet.next()) {
                Department department = getResultSet(resultSet);
                departmentList.add(department);
            }

            departmentOptional = Optional.of(departmentList.get(0));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return departmentOptional;
    }

    @Override
    public void deleteById(Integer integer) {
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            String SELECT_BY_ID = "DELETE * FROM departments WHERE department_id = " + integer + " LIMIT 1;";

            statement.executeQuery(SELECT_BY_ID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Department entity) {
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            String SELECT_BY_ID = "SELECT * FROM departments WHERE department_id = " + entity.getIdDepartment() + " LIMIT 1;";

            statement.executeQuery(SELECT_BY_ID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Department getResultSet(ResultSet resultSet) throws SQLException {
        Integer idDepartment = resultSet.getInt("department_id");
        String departmentName = resultSet.getString("department_name");
        Integer head = resultSet.getInt("head");
        String number = resultSet.getString("number");
        String email = resultSet.getString("email");

        Department department = new Department();

        department.setIdDepartment(idDepartment);
        department.setDepartmentName(departmentName);
        department.setHead(head);
        department.setNumber(number);
        department.setEmail(email);

        return department;
    }
}
