package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Company;
import model.Department;
import singleTone.SingleTone;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class DepartmentEditController {
    @FXML private RadioButton radioTrue;
    @FXML private RadioButton radioFalse;
    @FXML private TextField titleField;
    @FXML private ChoiceBox workModeBox;
    @FXML private ChoiceBox companyBox;
    @FXML private Spinner hourSpinner;
    @FXML private Spinner minutesSpinner;
    private Department editedDepartment;
    private List<Company> companies;

    public void initialize(){
        SpinnerValueFactory<Integer> minutesValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        SpinnerValueFactory<Integer> hoursValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 14);
        minutesSpinner.setValueFactory(minutesValueFactory);
        hourSpinner.setValueFactory(hoursValueFactory);
    }

    public void init(List<Company> companies){
        this.companies = new ArrayList<>(companies);
        companyBox.setItems(FXCollections.observableArrayList(this.companies));
        workModeBox.setItems(FXCollections.observableArrayList(SingleTone.getSingleTone().getEm().createQuery("Select p from Preference p").getResultList()));
    }

    public void sendDepartment(Department department) {
        if (department == null) return;
        //
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


    }

    public Department getEditedDepartment() {
        return null;
    }

    public void okClicked() {

    }

    public void cancelClicked() {
        closeStage();
    }

    private void closeStage(){
        Stage stage = (Stage) hourSpinner.getScene().getWindow();
        stage.close();
    }
}
