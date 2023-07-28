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

    abstract List<T> findAll();

    abstract T save(T entity);

    abstract Optional<T> findById(ID id);

    abstract void deleteById(ID id);

    abstract void delete(T entity);

}
