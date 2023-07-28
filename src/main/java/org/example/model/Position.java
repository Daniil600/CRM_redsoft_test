package org.example.model;

import java.math.BigDecimal;

public class Position {
    private Integer idPosition;
    private String namePosition;
    private BigDecimal salary;

    public Position() {
    }

    public Position(String namePosition, BigDecimal salary) {
        this.namePosition = namePosition;
        this.salary = salary;
    }

    public Integer getIdPosition() {
        return idPosition;
    }

    public void setIdPosition(Integer idPosition) {
        this.idPosition = idPosition;
    }

    public String getNamePosition() {
        return namePosition;
    }

    public void setNamePosition(String namePosition) {
        this.namePosition = namePosition;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Position{" +
                "idPosition=" + idPosition +
                ", namePosition='" + namePosition + '\'' +
                ", salary=" + salary +
                '}';
    }
}
