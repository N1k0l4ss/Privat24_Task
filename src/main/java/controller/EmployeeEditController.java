package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import singleTone.SingleTone;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeEditController {
    @FXML private TextField nameField;
    @FXML private Spinner hourSpinner;
    @FXML private Spinner minutesSpinner;
    @FXML private ChoiceBox preferenceBox;
    @FXML private ChoiceBox companyBox;
    @FXML private ChoiceBox roleBox;
    @FXML private ChoiceBox departmentBox;
    //
    private Employee editedEmployee;
    private List<Company> companies;
    private List workModes;
    private Optional<Integer> employeeId = Optional.empty();
// todo add time range fro two tables
    public void initialize(){
        workModes = SingleTone.getSingleTone().getEm().createQuery("Select p from Preference p").getResultList();
        // Set spinner limits
        SpinnerValueFactory<Integer> minutesValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        SpinnerValueFactory<Integer> hoursValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 14);
        minutesSpinner.setValueFactory(minutesValueFactory);
        hourSpinner.setValueFactory(hoursValueFactory);
        companyBox.valueProperty().addListener(x-> {
            roleBox.getItems().clear();
            Company company = (Company) companyBox.getSelectionModel().getSelectedItem();
            roleBox.setItems(FXCollections.observableArrayList(company.getRoles()));
            departmentBox.setItems(FXCollections.observableArrayList(company.getDepartaments()));
        });
        DepartmentEditController.preferenceListener(preferenceBox, hourSpinner, minutesSpinner);
    }

    public void init(List<Company> companies){
        this.companies = new ArrayList<>(companies);
        companyBox.setItems(FXCollections.observableArrayList(this.companies));
        preferenceBox.setItems(FXCollections.observableArrayList(workModes));
    }

    // Receives employee, if we wanna edit it
    public void sendEmployee(Employee employee) {
        if (employee == null) return;
        companyBox.setDisable(true);
        preferenceBox.setValue(employee.getPreference());
        companyBox.setValue(employee.getCompany());
        roleBox.setValue(employee.getRole());
        if (employee.getStartTime() != null){
            hourSpinner.getValueFactory().setValue(employee.getStartTime().getHour());
            minutesSpinner.getValueFactory().setValue(employee.getStartTime().getMinute());
        }
        departmentBox.setValue(employee.getDepartament());
        nameField.setText(employee.getName());
        employeeId = Optional.of(employee.getId());
    }

    // Initialize edited employee, doesn't matters if we create new employee or edited old
    public void okClicked() {
        if (
                (!companyBox.isDisable() && companyBox.getSelectionModel().getSelectedItem() == null) ||
                        preferenceBox.getSelectionModel().getSelectedItem() == null ||
                        roleBox.getSelectionModel().getSelectedItem() == null ||
                        departmentBox.getSelectionModel().getSelectedItem() == null ||
                        nameField.getText().isEmpty()
        ) return;
        editedEmployee = new Employee();
        editedEmployee.setPreference((Preference) preferenceBox.getSelectionModel().getSelectedItem());
        editedEmployee.setCompany((Company) companyBox.getSelectionModel().getSelectedItem());
        editedEmployee.setRole((Role) roleBox.getSelectionModel().getSelectedItem());
        if (((Preference) preferenceBox.getSelectionModel().getSelectedItem()).getId() == 3)
            editedEmployee.setStartTime(null);
        else
            editedEmployee.setStartTime(LocalTime.of((int)hourSpinner.getValue(), (int)minutesSpinner.getValue()));
        editedEmployee.setName(nameField.getText());
        editedEmployee.setDepartament((Department) departmentBox.getSelectionModel().getSelectedItem());
        employeeId.ifPresent(id -> editedEmployee.setId(id));
        closeStage();
    }

    public void cancelClicked() {
        closeStage();
    }

    private void closeStage(){
        Stage stage = (Stage) hourSpinner.getScene().getWindow();
        stage.close();
    }

    // Getter for edited or created employee
    public Employee getEditedEmployee() {
        return editedEmployee;
    }
}
