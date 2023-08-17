package org.example.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.example.exception.NotFoundException;
import org.example.model.Department;
import org.example.model.Employee;
import org.example.model.Position;
import org.example.repositories.DepartmentRepository;
import org.example.repositories.EmployeeRepository;
import org.example.repositories.PositionRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Route
@Service
@Scope("prototype")
public class MainView extends VerticalLayout {
    static Map<Integer, Position> positionMap = new ConcurrentHashMap<>();
    static Map<Integer, Department> departmentMap = new ConcurrentHashMap<>();

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    private Grid<Employee> grid;

    private Button newBtn = new Button("New");
    private Button deleteBtn = new Button("Delete");
    private Button saveBtn = new Button("Save");
    private HorizontalLayout btnLayout = new HorizontalLayout();
    private HorizontalLayout fieldsLayout = new HorizontalLayout();

    private IntegerField idField = new IntegerField("ID");
    private TextField firstName = new TextField("First Name");
    private TextField lastName = new TextField("Last Name");
    private ComboBox<String> department = new ComboBox<>("Department");
    private ComboBox<String> position = new ComboBox<>("Position");

    private String MAX_WIDTH = "817px";
    private String BUTTON_WIDTH = "261px";

    private HorizontalLayout searchLayout = new HorizontalLayout();
    TextField filterTextForFirstName = new TextField();
    TextField filterTextForLastName = new TextField();
    private ComboBox<String> filterBoxForDepartment = new ComboBox<>();
    private ComboBox<String> filterBoxForPosition = new ComboBox<>();

    public MainView(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, PositionRepository positionRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.grid = new Grid<>(Employee.class, false);


        setPositionMap(positionRepository.findAll());
        System.out.println("Reloaded setPositionMap");

        setDepartmentMap(departmentRepository.findAll());
        System.out.println("Reloaded setDepartmentMap");


        grid.addColumn(Employee::getIdEmployee).setHeader("ID").setSortable(true).setWidth("20px");
        grid.addColumn(Employee::getFirstName).setHeader("First name").setSortable(true);
        grid.addColumn(Employee::getLastName).setHeader("Last name").setSortable(true);
        grid.addColumn(employee -> positionMap.get(employee.getPositionId()).getSalary()).setHeader("Salary").setSortable(true);
        grid.addColumn(employee -> departmentMap.get(employee.getDepartmentId()).getDepartmentName()).setHeader("Department").setSortable(true);
        grid.addColumn(employee -> positionMap.get(employee.getPositionId()).getNamePosition()).setHeader("Position").setSortable(true);
        grid.setMaxWidth(MAX_WIDTH);

        department.setItems(departmentRepository.findAllNameDepartment());
        position.setItems(positionRepository.findAllPositionName());
        department.setClearButtonVisible(true);
        position.setClearButtonVisible(true);

        deleteBtn.setEnabled(false);

        newBtn.setWidth(BUTTON_WIDTH);
        deleteBtn.setWidth(BUTTON_WIDTH);
        saveBtn.setWidth(BUTTON_WIDTH);

        btnLayout.add(newBtn, deleteBtn, saveBtn);
        btnLayout.setMaxWidth(MAX_WIDTH);

        fieldsLayout.add(firstName, lastName, department, position);
        add(btnLayout);
        add(fieldsLayout);
        searchLayout.add(getToolbar());
        add(searchLayout);
        add(grid);
        refreshTableData();
        addButtonsActionListeners();
    }

    private static void setPositionMap(List<Position> positionList){
        for (int i = 0; i < positionList.size(); i++) {
            positionMap.put(positionList.get(i).getIdPosition(), positionList.get(i));
        }
    }
    private static void setDepartmentMap(List<Department> departmentList){
        for (int i = 0; i < departmentList.size(); i++) {
            departmentMap.put(departmentList.get(i).getIdDepartment(), departmentList.get(i));
        }
    }

    private List<Component> getToolbar() {
        filterTextForFirstName.setPlaceholder("Filter by first name...");
        filterTextForFirstName.setClearButtonVisible(true);
        filterTextForFirstName.setValueChangeMode(ValueChangeMode.LAZY);
        filterTextForFirstName.addValueChangeListener(e -> refreshTableDataForSearchFirstName());

        filterTextForLastName.setPlaceholder("Filter by last name...");
        filterTextForLastName.setClearButtonVisible(true);
        filterTextForLastName.setValueChangeMode(ValueChangeMode.LAZY);
        filterTextForLastName.addValueChangeListener(e -> refreshTableDataForSearchLastName());

        filterBoxForDepartment.setPlaceholder("Filter by department name...");
        filterBoxForDepartment.setClearButtonVisible(true);
        filterBoxForDepartment.setItems(departmentRepository.findAllNameDepartment());
        filterBoxForDepartment.addValueChangeListener(e -> refreshTableDataForSearchDepartmentName());

        filterBoxForPosition.setPlaceholder("Filter by position name...");
        filterBoxForPosition.setClearButtonVisible(true);
        filterBoxForPosition.setItems(positionRepository.findAllPositionName());
        filterBoxForPosition.addValueChangeListener(e -> refreshTableDataForSearchPositionName());

        var toolbarFirstName = new HorizontalLayout(filterTextForFirstName);
        var toolbarLastName = new HorizontalLayout(filterTextForLastName);
        var toolbarDepartment = new HorizontalLayout(filterBoxForDepartment);
        var toolbarPosition = new HorizontalLayout(filterBoxForPosition);

        List<Component> components = new ArrayList<>();
        components.add(toolbarFirstName);
        components.add(toolbarLastName);
        components.add(toolbarDepartment);
        components.add(toolbarPosition);

        return components;
    }

    public void refreshTableData() {
        grid.setItems(findAllEmployee());
    }
    private List<Employee> findAllEmployee() {
        return employeeRepository.findAll();
    }
    public void refreshTableDataForSearchFirstName() {
        grid.setItems(findAllEmployeeOfFirstName(filterTextForFirstName.getValue()));
    }

    public void refreshTableDataForSearchLastName() {
        grid.setItems(findAllEmployeeOfLastName(filterTextForLastName.getValue()));
    }
    public void refreshTableDataForSearchDepartmentName() {
        grid.setItems(findAllEmployeeOfDepartment(filterBoxForDepartment.getValue()));
    }
    public void refreshTableDataForSearchPositionName() {
        grid.setItems(findAllEmployeeOfPosition(filterBoxForPosition.getValue()));
    }

    private List<Employee> findAllEmployeeOfFirstName(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return employeeRepository.findAll();
        } else {
            return employeeRepository.searchByNameFirstName(stringFilter);
        }
    }
    private List<Employee> findAllEmployeeOfLastName(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return employeeRepository.findAll();
        } else {
            return employeeRepository.searchByNameLastName(stringFilter);
        }
    }
    private List<Employee> findAllEmployeeOfDepartment(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return employeeRepository.findAll();
        } else {
            return employeeRepository.searchByDepartment(stringFilter);
        }
    }

    private List<Employee> findAllEmployeeOfPosition(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return employeeRepository.findAll();
        } else {
            return employeeRepository.searchByPosition(stringFilter);
        }
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
                department.setValue(
                        departmentMap.entrySet().stream().
                        map(departmentValue -> departmentValue.getValue()).
                        filter(departmentValue -> departmentValue.getIdDepartment() == selectedCustomer.getDepartmentId())
                        .findFirst().orElseThrow(() -> new NotFoundException("Not found Department")).getDepartmentName());
                position.setValue(positionMap.entrySet().stream().
                        map(positionMap -> positionMap.getValue()).
                        filter(positionValue -> positionValue.getIdPosition() == selectedCustomer.getPositionId())
                        .findFirst().orElseThrow(()-> new NotFoundException("Not found Position")).getNamePosition());
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
            customer.setIdEmployee(idField.getValue());
            customer.setFirstName(firstName.getValue());
            customer.setLastName(lastName.getValue());
            customer.setDepartmentId(departmentMap.entrySet().stream()
                    .map(departmentStream -> departmentStream.getValue())
                    .filter(departmentStream -> departmentStream.getDepartmentName().equals(department.getValue()))
                    .findFirst().get().getIdDepartment());
            customer.setPositionId(positionMap.entrySet().stream()
                    .map(departmentStream -> departmentStream.getValue())
                    .filter(departmentStream -> departmentStream.getNamePosition().equals(position.getValue()))
                    .findFirst().get().getIdPosition());
            employeeRepository.save(customer);
            clearInputFields();
            refreshTableData();
        });
    }


    private void clearInputFields() {
        firstName.clear();
        lastName.clear();
        idField.clear();
        department.clear();
        position.clear();
        filterTextForLastName.clear();
        filterTextForFirstName.clear();
        filterBoxForDepartment.clear();
        filterBoxForPosition.clear();
    }
}
