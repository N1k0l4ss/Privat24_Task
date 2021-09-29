package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Company;
import model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeEditController {
    @FXML private TextField nameField;
    @FXML private Spinner hourSpinner;
    @FXML private Spinner minutesSpinner;
    @FXML private ChoiceBox workModeBox;
    @FXML private ChoiceBox companyBox;
    @FXML private ChoiceBox roleBox;
    @FXML private ChoiceBox departmentBox;
    //
    private Employee editedEmployee;
    private List<Company> companies;
    private List workModes;

    public void initialize(){

    }

    public void init(List<Company> companies){
        this.companies = new ArrayList<>(companies);
        companyBox.setItems(FXCollections.observableArrayList(this.companies));
        workModeBox.setItems(FXCollections.observableArrayList(workModes));
    }

    // Receives employee, if we wanna edit it
    public void sendEmployee(Employee employee) {

    }


    // Initialize edited employee, doesn't matters if we create new employee or edited old
    public void okClicked() {
        //todo checking for empty field


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
