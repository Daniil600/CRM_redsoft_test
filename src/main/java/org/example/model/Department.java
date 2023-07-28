package org.example.model;

public class Department {
    private Integer idDepartment;
    private String departmentName;
    private Integer head;
    private String number;
    private String email;

    public Department() {
    }

    public Department(String departmentName, String number, String email) {
        this.departmentName = departmentName;
        this.number = number;
        this.email = email;
    }

    public Integer getIdDepartment() {
        return idDepartment;
    }

    public Integer getHead() {
        return head;
    }

    public void setHead(Integer head) {
        this.head = head;
    }

    public void setIdDepartment(Integer idDepartment) {
        this.idDepartment = idDepartment;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Department{" +
                "idDepartment=" + idDepartment +
                ", departmentName='" + departmentName + '\'' +
                ", head=" + head +
                ", number='" + number + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
