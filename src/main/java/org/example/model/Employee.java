package org.example.model;

public class Employee {
    private Integer idEmployee;
    private String firstName;
    private String lastName;
    private Integer positionId;
    private Integer departmentId;

    public Employee() {
    }

    public Employee(String firstName, String lastName, Integer positionId, Integer departmentId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.positionId = positionId;
        this.departmentId = departmentId;
    }

    public Integer getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Integer idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "idEmployee=" + idEmployee +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", positionId=" + positionId +
                ", departmentId=" + departmentId +
                '}';
    }
}
