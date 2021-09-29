package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Company;
import model.Department;
import model.Preference;
import singleTone.SingleTone;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class DepartmentEditController {
    @FXML private ToggleGroup freeWorkToggleGroup;
    @FXML private RadioButton radioTrue;
    @FXML private RadioButton radioFalse;
    @FXML private TextField titleField;
    @FXML private ChoiceBox workModeBox;
    @FXML private ChoiceBox companyBox;
    @FXML private Spinner hourSpinner;
    @FXML private Spinner minutesSpinner;
    private Department editedDepartment;
    private List<Company> companies;
    private List workModes;
    private int departmentId;

    public void initialize(){
        workModes = SingleTone.getSingleTone().getEm().createQuery("Select p from Preference p").getResultList(); // Get available preferences
        // Disable time fields by selected work mode
        preferenceListener(workModeBox, hourSpinner, minutesSpinner);
        // Disable field, that doesnt belong to free graphic
        freeWorkToggleGroup.selectedToggleProperty().addListener(x-> {
            if (radioTrue.isSelected()) {
                workModeBox.setDisable(true);
                minutesSpinner.setDisable(true);
                hourSpinner.setDisable(true);
            } else{
                workModeBox.setDisable(false);
                minutesSpinner.setDisable(false);
                hourSpinner.setDisable(false);
            }
        });
    }

    static void preferenceListener(ChoiceBox workModeBox, Spinner hourSpinner, Spinner minutesSpinner) {
        // Set spinner limits
        SpinnerValueFactory<Integer> minutesValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        SpinnerValueFactory<Integer> hoursValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 14);
        minutesSpinner.setValueFactory(minutesValueFactory);
        hourSpinner.setValueFactory(hoursValueFactory);
        //
        workModeBox.getSelectionModel().selectedItemProperty().addListener(x -> {
            Preference workMode = (Preference) workModeBox.getSelectionModel().getSelectedItem();
            hourSpinner.setDisable(false);
            minutesSpinner.setDisable(false);
            hourSpinner.getValueFactory().setValue(9);
            minutesSpinner.getValueFactory().setValue(0);
            if (workMode.getId() != 1) {
                hourSpinner.setDisable(true);
                minutesSpinner.setDisable(true);
            }
        });
    }

    public void init(List<Company> companies){
        this.companies = new ArrayList<>(companies);
        companyBox.setItems(FXCollections.observableArrayList(this.companies));
        workModeBox.setItems(FXCollections.observableArrayList(workModes));
    }

    // Receives department, if we wanna edit it
    public void sendDepartment(Department department) {
        if (department == null) return;
        //
        companyBox.setDisable(true);
        if (department.isFreeWork())
            radioTrue.setSelected(true);
        else
            radioFalse.setSelected(true);
        //
        titleField.setText(department.getTitle());
        if (department.getStartTime() != null){
            hourSpinner.getValueFactory().setValue(department.getStartTime().getHour());
            minutesSpinner.getValueFactory().setValue(department.getStartTime().getMinute());
        } else {
                hourSpinner.getValueFactory().setValue(9);
                minutesSpinner.getValueFactory().setValue(0);
        }
        //
        companyBox.setValue(department.getCompany());
        workModeBox.setValue(department.getWorkMode());
        departmentId = department.getId();
    }


    // Initialize edited department, doesnt matters if we create new department or edited old
    public void okClicked() {
        if (titleField.getText().isEmpty()
                || (workModeBox.getSelectionModel().getSelectedItem() == null && !workModeBox.isDisable())
                || companyBox.getSelectionModel().getSelectedItem() == null
                || (!radioFalse.isSelected() && !radioTrue.isSelected()))
            return;
        editedDepartment = new Department();
        editedDepartment.setFreeWork(radioTrue.isSelected());
        editedDepartment.setTitle(titleField.getText());
        // if graphic is free set null
        if (radioTrue.isSelected())
            editedDepartment.setWorkMode(null);
        else
            editedDepartment.setWorkMode((Preference) workModeBox.getSelectionModel().getSelectedItem());
        //
        editedDepartment.setCompany((Company) companyBox.getSelectionModel().getSelectedItem());
        // Set time by work mode
        Preference workMode = (Preference) workModeBox.getSelectionModel().getSelectedItem();
        if (radioTrue.isSelected() || workMode.getId() == 3)
            editedDepartment.setStartTime(null);
        else
            editedDepartment.setStartTime(LocalTime.of((int)hourSpinner.getValue(), (int)minutesSpinner.getValue()));
        // Set id only if we wanna edit object that already exists
        if (!companies.isEmpty())
            editedDepartment.setId(departmentId);
        closeStage();
    }

    public void cancelClicked() {
        closeStage();
    }

    private void closeStage(){
        Stage stage = (Stage) hourSpinner.getScene().getWindow();
        stage.close();
    }

    // Getter for edited or created department
    public Department getEditedDepartment() {
        return editedDepartment;
    }
}
