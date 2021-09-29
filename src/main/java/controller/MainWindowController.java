package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import service.CompanyService;
import service.DepartmentService;
import service.RoleService;
import singleTone.SingleTone;

import java.util.Optional;

public class MainWindowController {
    // Employee table
    @FXML private TableView<Employee> employeeTable;
    @FXML private TableColumn<Employee, String> employeeName;
    @FXML private TableColumn<Employee, String> employeePreference;
    @FXML private TableColumn<Employee, String> employeeDepartment;
    @FXML private TableColumn<Employee, String> employeeTime;
    @FXML private TableColumn<Employee, String> employeeRole;
    // Role table
    @FXML private TableView<Role> roleTable;
    @FXML private TableColumn<Role, String> roleTitle;
    @FXML private TableColumn<Role, Boolean> roleFreeWork;
    // Department table
    @FXML private TableView<Department> departmentTable;
    @FXML private TableColumn<Department, String> departmentTitle;
    @FXML private TableColumn<Department, String> departmentWorkTime;
    @FXML private TableColumn<Department, Boolean> departmentFreeWork;
    @FXML private TableColumn<Department, String> departmentWorkMode;
    @FXML private ListView<Company> companyListView;
    // Other variables
    private SingleTone singleTone;
    // Window controls
    public void initialize(){
        singleTone = SingleTone.getSingleTone();
        // Employee settings
        employeeName.setCellValueFactory(new PropertyValueFactory<>("name"));
        employeePreference.setCellValueFactory(new PropertyValueFactory<>("preferenceString"));
        employeeDepartment.setCellValueFactory(new PropertyValueFactory<>("departmentString"));
        employeeTime.setCellValueFactory(new PropertyValueFactory<>("timeString"));
        employeeRole.setCellValueFactory(new PropertyValueFactory<>("roleString"));
        // Department settings
        departmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        departmentFreeWork.setCellValueFactory(new PropertyValueFactory<>("freeWork"));
        departmentWorkTime.setCellValueFactory(new PropertyValueFactory<>("workTimeString"));
        departmentWorkMode.setCellValueFactory(new PropertyValueFactory<>("preferenceName"));
        // Role settings
        roleTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        roleFreeWork.setCellValueFactory(new PropertyValueFactory<>("freeWork"));
        // Get company
        updateTables();
        //
        companyListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldV, newV) -> {
                    if (newV == null)
                        return;
                    departmentTable.setItems(FXCollections.observableArrayList(newV.getDepartaments()));
                    roleTable.setItems(FXCollections.observableArrayList(newV.getRoles()));
                    employeeTable.setItems(FXCollections.observableArrayList(newV.getEmployees()));
                }
        );
    }

    private void updateTables() {
        int selectedIndex = companyListView.getSelectionModel().getSelectedIndex();
        roleTable.getItems().clear();
        companyListView.getItems().clear();
        employeeTable.getItems().clear();
        departmentTable.getItems().clear();
        companyListView.setItems(FXCollections.observableArrayList(CompanyService.findAll()));
        companyListView.getSelectionModel().select(selectedIndex);
    }

    public void displayError(String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.F5)
            updateTables();
    }

    // Company controls
    public void newCompanyClicked() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create a company");
        dialog.setHeaderText("Please enter your company name:");
        dialog.setContentText(null);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            Company company = new Company();
            company.setTitle(result.get());
            CompanyService.createCompany(company);
            updateTables();
        }
    }

    public void deleteCompanyClicked() {
        Company company = companyListView.getSelectionModel().getSelectedItem();
        if (company != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete company");
            alert.setHeaderText("Do you really want to delete company?");
            alert.setContentText(company.getTitle());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                CompanyService.deleteCompany(company);
                updateTables();
            }
        }
    }

    // Department controls
    public void newDepartmentClicked() {
        if (companyListView.getSelectionModel().getSelectedItem() == null) return;
        Department editedDepartment = getEditedDepartment(null);
        if (editedDepartment == null) return;
        DepartmentService.createDepartment(editedDepartment);
        updateTables();
    }

    public void editDepartmentClicked() {
        Department department = departmentTable.getSelectionModel().getSelectedItem();
        if (department == null) return;
        Department editedDepartment = getEditedDepartment(department);
        if (editedDepartment == null) return;
        DepartmentService.updateDepartment(editedDepartment);
        updateTables();
    }

    public void deleteDepartmentClicked() {
        Department department = departmentTable.getSelectionModel().getSelectedItem();
        if (department != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete department");
            alert.setHeaderText("Do you really want to delete department?");
            alert.setContentText(department.getTitle());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                DepartmentService.deleteDepartment(department);
                updateTables();
            }
        }
    }

    private Department getEditedDepartment(Department department) {
        Department editedDepartment = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../main/departmentEdit.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Department");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root1));
            DepartmentEditController controller = fxmlLoader.getController();
            controller.init(companyListView.getItems());
            controller.sendDepartment(department);
            stage.setResizable(false);
            stage.showAndWait();
            editedDepartment = controller.getEditedDepartment();
        } catch (Exception e) {
            displayError(e.toString());
        }
        return editedDepartment;
    }

    // Role controls
    public void newRoleClicked() {
    }

    public void editRoleClicked() {
    }

    public void deleteRoleClicked() {
        Role role = roleTable.getSelectionModel().getSelectedItem();
        if (role != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete role");
            alert.setHeaderText("Do you really want to delete role?");
            alert.setContentText(role.getTitle());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                RoleService.deleteRole(role);
                updateTables();
            }
        }
    }
}
