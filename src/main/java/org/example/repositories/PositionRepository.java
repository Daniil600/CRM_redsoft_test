package org.example.repositories;

import org.example.model.Position;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@Repository
public class PositionRepository extends JdbcRepository<Position, Integer>{


    @Override
    public List<Position> findAll() {
        List<Position> positionList = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            String SELECT_QUERY = "SELECT * FROM positions;";
            ResultSet resultSet = statement.executeQuery(SELECT_QUERY);
            while (resultSet.next()) {
                Position position = getResultSet(resultSet);
                positionList.add(position);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return positionList;
    }

    public Position findByName(String name) {
        List<Position> positionList = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            String SELECT_QUERY = "SELECT * FROM positions WHERE position_name = '" + name + "';";
            ResultSet resultSet = statement.executeQuery(SELECT_QUERY);
            while (resultSet.next()) {
                Position position = getResultSet(resultSet);
                positionList.add(position);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return positionList.get(0);
    }

    public List<String> findAllPositionName() {
        List<String> positionList = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            String SELECT_NAMES_POSITION = "SELECT position_name from positions";
            ResultSet resultSet = statement.executeQuery(SELECT_NAMES_POSITION);
            while (resultSet.next()) {
                String positionName = resultSet.getString("position_name");
                positionList.add(positionName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return positionList.isEmpty() ? Collections.EMPTY_LIST : positionList;
    }

    @Override
    public Position save(Position entity) {
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {

            Integer idPosition = entity.getIdPosition();
            String name = entity.getNamePosition();
            BigDecimal salary = entity.getSalary();

            String SAVE = String.format("INSERT INTO positions VALUES ('%d', '%s', '%d')", idPosition, name, salary);
            statement.executeUpdate(SAVE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return entity;
    }

    @Override
    public Optional<Position> findById(Integer integer) {
        Optional<Position> positionOptional = Optional.empty();
        List<Position> positionList = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            String SELECT_BY_ID = "SELECT * FROM positions WHERE position_id = " + integer + " LIMIT 1;";
            ResultSet resultSet = statement.executeQuery(SELECT_BY_ID);
            while (resultSet.next()) {
                Position position = getResultSet(resultSet);
                positionList.add(position);
            }
            positionOptional = Optional.of(positionList.get(0));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return positionOptional;
    }

    @Override
    public void deleteById(Integer integer) {
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            String SELECT_BY_ID = "SELECT * FROM positions WHERE position_id = " + integer + " LIMIT 1;";

            statement.executeQuery(SELECT_BY_ID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Position entity) {
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            String DELETE_BY_ID = "DELETE * FROM positions WHERE position_id = " + entity.getIdPosition() + " LIMIT 1;";

            statement.executeQuery(DELETE_BY_ID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Position getResultSet(ResultSet resultSet) throws SQLException {
        Integer positionId = resultSet.getInt("position_id");
        String name = resultSet.getString("position_name");
        BigDecimal salary = resultSet.getBigDecimal("salary");

        Position position = new Position();

        position.setIdPosition(positionId);
        position.setNamePosition(name);
        position.setSalary(salary);

        return position;
    }
}
