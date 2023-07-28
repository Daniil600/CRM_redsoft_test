package org.example.repositories;

import org.example.model.Position;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PositionRepository extends JdbcRepository<Position, Integer>{
    private static final String SELECT_QUERY = "SELECT * FROM positions;";

    @Override
    List<Position> findAll() {
        List<Position> positionList = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {

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

    @Override
    Position save(Position entity) {
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
    Optional<Position> findById(Integer integer) {
        Optional<Position> positionOptional = Optional.empty();
        try (
                Connection connection = DriverManager.getConnection(URL_DRIVER, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            String SELECT_BY_ID = "SELECT * FROM positions WHERE position_id = " + integer + " LIMIT 1;";
            ResultSet resultSet = statement.executeQuery(SELECT_BY_ID);

            positionOptional = Optional.of(getResultSet(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return positionOptional;
    }

    @Override
    void deleteById(Integer integer) {
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
    void delete(Position entity) {
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
