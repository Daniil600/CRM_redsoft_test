package org.example.repositories;

import org.example.dto.EmployeeToDepartmentName;
import org.example.dto.EmployeeToPositionName;
import org.example.model.Employee;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepository extends JdbcRepository<Employee, Integer> {
    PositionRepository positionRepository;

    public EmployeeRepository(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

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
    public List<Employee> searchByPosition(String searchTerm) {

        List<Employee> employeeList = new ArrayList<>();
        String SELECT_QUERY = "SELECT e.employee_id, e.first_name, e.last_name, e.department_id, e.position_id" +
                " FROM employees as e JOIN positions USING(position_id) WHERE LOWER(position_name) LIKE LOWER(?)";

        try (Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_QUERY)) {
            statement.setString(1, "%" + searchTerm + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Employee employee = getResultSet(resultSet);
                employeeList.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        System.out.println(employeeList);
        return employeeList;
    }

    public List<Employee> searchByDepartment(String searchTerm) {

        List<Employee> employeeList = new ArrayList<>();
        String SELECT_QUERY = "SELECT e.employee_id, e.first_name, e.last_name, e.department_id, e.position_id" +
                " FROM employees as e JOIN departments USING(department_id) WHERE LOWER(department_name) LIKE LOWER(?)";

        try (Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_QUERY)) {
            statement.setString(1, "%" + searchTerm + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Employee employee = getResultSet(resultSet);
                employeeList.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        System.out.println(employeeList);
        return employeeList;
    }


    public List<Employee> searchByNameLastName(String searchTerm) {

        List<Employee> employeeList = new ArrayList<>();
        String SELECT_QUERY = "SELECT * FROM employees c WHERE LOWER(c.last_name) LIKE LOWER(?)";

        try (Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_QUERY)) {
            statement.setString(1, "%" + searchTerm + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Employee employee = getResultSet(resultSet);
                employeeList.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        System.out.println(employeeList);
        return employeeList;
    }

    public List<Employee> searchByNameFirstName(String searchTerm) {

        List<Employee> employeeList = new ArrayList<>();
        String SELECT_QUERY = "SELECT * FROM employees c WHERE LOWER(c.first_name) LIKE LOWER(?)";

        try (Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_QUERY)) {
            statement.setString(1, "%" + searchTerm + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Employee employee = getResultSet(resultSet);
                employeeList.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        System.out.println(employeeList);
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

            if (idEmployee == null) {
                String SAVE = String.format("INSERT INTO employees" +
                                "(first_name, last_name, department_id, position_id) VALUES ('%s', '%s', '%d', '%d')"
                        , firstName, lastName, departmentId, positionId);
                statement.executeUpdate(SAVE, Statement.RETURN_GENERATED_KEYS);
                if (positionRepository.findById(positionId).get().getNamePosition().equals("Director")) {
                    ResultSet resultSet = statement.getGeneratedKeys();
                    if (resultSet.next()) {
                        idEmployee = resultSet.getInt(1);
                        System.out.println("UPDATE departments SET head = " + idEmployee + ";");
                        String SQL_UPDATE_DEPARTMENT = "UPDATE departments SET head = " + idEmployee + " WHERE department_id = " + entity.getDepartmentId() + ";";
                        statement.executeUpdate(SQL_UPDATE_DEPARTMENT);
                    } else {
                        throw new SQLException();
                    }
                }
            } else {
                String SAVE = String.format("UPDATE employees SET first_name = '%s', " +
                                "last_name = '%s', department_id = %d, position_id = %d WHERE employee_id = %d",
                        firstName, lastName, departmentId, positionId, idEmployee);
                statement.executeUpdate(SAVE);
                if (positionRepository.findById(positionId).get().getNamePosition().equals("Lead")) {
                    String SQL_UPDATE_DEPARTMENT = "UPDATE departments SET head = "
                            + idEmployee + " WHERE department_id = " + departmentId + ";";
                    statement.executeUpdate(SQL_UPDATE_DEPARTMENT);
                }
            }

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

    public EmployeeToDepartmentName findByIdDepartmentOneEmployee(Integer integer) {
        List<EmployeeToDepartmentName> employeeToDepartmentNames = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            String SELECT_DEPARTMENT_BY_ID = "SELECT department_id, department_name FROM employees " +
                    "JOIN departments USING(department_id)" +
                    " WHERE department_id = " + integer + " LIMIT 1;";
            ResultSet resultSet = statement.executeQuery(SELECT_DEPARTMENT_BY_ID);

            while (resultSet.next()) {
                employeeToDepartmentNames.add(getResultSetDepartment(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employeeToDepartmentNames.get(0);
    }

    public EmployeeToPositionName findByIdPositionOneEmployee(Integer integer) {
        List<EmployeeToPositionName> employeeToPositionNames = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            String SELECT_POSITION_BY_ID = "SELECT position_id, position_name FROM employees " +
                    "JOIN positions USING(position_id)" +
                    " WHERE position_id = " + integer + " LIMIT 1;";
            ResultSet resultSet = statement.executeQuery(SELECT_POSITION_BY_ID);
            while (resultSet.next()) {
                employeeToPositionNames.add(getResultSetPosition(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return employeeToPositionNames.get(0);
    }

    @Override
    public void deleteById(Integer integer) {
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            String SELECT_BY_ID = "SELECT * FROM employees WHERE employee_id = " + integer + ";";

            statement.executeUpdate(SELECT_BY_ID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Employee entity) {
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            if (positionRepository.findById(entity.getPositionId()).get().getNamePosition().equals("Lead")) {
                String DELETE_HEAD_DEVELOPER = "UPDATE departments SET head = null WHERE department_id = " + entity.getDepartmentId() + ";";
                statement.executeUpdate(DELETE_HEAD_DEVELOPER);
            }
            System.out.println(entity.getIdEmployee());
            String DELETE_BY_ID = "DELETE FROM employees WHERE employee_id = " + entity.getIdEmployee() + ";";
            statement.executeUpdate(DELETE_BY_ID);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private EmployeeToPositionName getResultSetPosition(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("position_id");
        String positionName = resultSet.getString("position_name");

        EmployeeToPositionName toPositionName = new EmployeeToPositionName();

        toPositionName.setPositionId(id);
        toPositionName.setPositionName(positionName);


        return toPositionName;
    }

    private EmployeeToDepartmentName getResultSetDepartment(ResultSet resultSet) throws SQLException {
        Integer idDepartment = resultSet.getInt("department_id");
        String departmentName = resultSet.getString("department_name");

        EmployeeToDepartmentName toDepartmentName = new EmployeeToDepartmentName();

        toDepartmentName.setDepartmentid(idDepartment);
        toDepartmentName.setDepartmentName(departmentName);


        return toDepartmentName;
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
