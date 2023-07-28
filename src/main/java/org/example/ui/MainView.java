package org.example.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.example.model.Employee;
import org.example.repositories.DepartmentRepository;
import org.example.repositories.EmployeeRepository;
import org.example.repositories.PositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Route
@Service
public class MainView extends VerticalLayout {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    Grid<Employee> grid;

    private Button newBtn = new Button("New");
    private Button deleteBtn = new Button("Delete");
    private Button saveBtn = new Button("Save");
    private HorizontalLayout btnLayout = new HorizontalLayout();
    private HorizontalLayout fieldsLayout = new HorizontalLayout();

    private IntegerField idField = new IntegerField("ID");
    private TextField firstName = new TextField("First Name");
    private TextField lastName = new TextField("Last Name");
    private TextField department = new TextField("Department");
    private TextField position = new TextField("Position");

    private String MAX_WIDTH = "800px";
    private String BUTTON_WIDTH = "123px";

    public MainView(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, PositionRepository positionRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.grid = new Grid<>(Employee.class, false);
        grid.addColumn(Employee::getIdEmployee).setHeader("ID").setSortable(true).setWidth("20px");
        grid.addColumn(Employee::getFirstName).setHeader("First name").setSortable(true);
        grid.addColumn(Employee::getLastName).setHeader("Last name").setSortable(true);
        grid.addColumn(employee -> departmentRepository.findById(employee.getDepartmentId()).get().getDepartmentName()).setHeader("Department").setSortable(true);
        grid.addColumn(employee -> positionRepository.findById(employee.getPositionId()).get().getNamePosition()).setHeader("Position").setSortable(true);
        grid.setMaxWidth(MAX_WIDTH);

        deleteBtn.setEnabled(false);

        newBtn.setWidth(BUTTON_WIDTH);
        deleteBtn.setWidth(BUTTON_WIDTH);
        saveBtn.setWidth(BUTTON_WIDTH);

        btnLayout.add(newBtn, deleteBtn, saveBtn);
        btnLayout.setMaxWidth(MAX_WIDTH);
        fieldsLayout.add(firstName, lastName, department, position);

        add(btnLayout);
        add(fieldsLayout);
        add(grid);
        refreshTableData();
        addButtonsActionListeners();
    }

    private void addButtonsActionListeners() {
        grid.addSelectionListener(selected -> {
            if (selected.getAllSelectedItems().isEmpty()) {
                deleteBtn.setEnabled(false);
                clearInputFields();
            } else {
                deleteBtn.setEnabled(true);
                Employee selectedCustomer = selected.getFirstSelectedItem().get();
                firstName.setValue(selectedCustomer.getFirstName());
                lastName.setValue(selectedCustomer.getLastName());
                idField.setValue(selectedCustomer.getIdEmployee());
            }
        });

        newBtn.addClickListener(click -> {
            clearInputFields();
            grid.select(null);
        });

        deleteBtn.addClickListener(click -> {
            employeeRepository.delete(grid.getSelectedItems().stream().toList().get(0));
            refreshTableData();
            clearInputFields();
        });

        saveBtn.addClickListener(click -> {
            Employee customer = new Employee();
            customer.setFirstName(firstName.getValue());
            customer.setLastName(lastName.getValue());
            customer.setIdEmployee(idField.getValue());
            employeeRepository.save(customer);
            clearInputFields();
            refreshTableData();
        });
    }

    private void refreshTableData() {
        List<Employee> employeeList = employeeRepository.findAll();
        grid.setItems(employeeList);
    }

    private void clearInputFields() {
        firstName.clear();
        lastName.clear();
        idField.clear();
    }
}
