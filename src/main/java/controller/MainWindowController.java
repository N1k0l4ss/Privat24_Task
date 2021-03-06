package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import service.CompanyService;
import service.DepartmentService;
import service.EmployeeService;
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
        displayHelp();
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

    public void displayInfo(String contentText, String title, String textHeader){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(textHeader);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private void displayHelp(){
        String info = "The program is written by Nikita Bielonozhko. \n" +
                "To update tables, please, press F5. \n" +
                "To reveal profit, firstly, choose the company, then press F4. \n" +
                "To reveal help again, please, press F1";
        displayInfo(info, "Help", "About program");
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()){
            case F1:
                displayHelp();
                break;
            case F4:
                displayProfit();
                break;
            case F5:
                updateTables();
                break;
        }
    }

    private void displayProfit() {
        if (companyListView.getSelectionModel().getSelectedItem() == null) {
            displayError("Select company to look at profit");
            return;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/profitWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Company profit");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root1));
            ProfitWindowController controller = fxmlLoader.getController();
            controller.init(companyListView.getSelectionModel().getSelectedItem());
            stage.setResizable(false);
            stage.showAndWait();
        } catch (Exception e) {
            displayError(e.toString());
        }
    }

    // Company controls
    public void newCompanyClicked() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("??ompany");
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
        } else displayError("Select company to delete");
    }

    // Department controls
    public void newDepartmentClicked() {
        Department editedDepartment = getEditedDepartment(null);
        if (editedDepartment == null) return;
        DepartmentService.createDepartment(editedDepartment);
        updateTables();
    }

    public void editDepartmentClicked() {
        Department department = departmentTable.getSelectionModel().getSelectedItem();
        if (department == null) {
            displayError("Select department to edit");
            return;
        }
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
        } else displayError("Select department to delete");
    }

    private Department getEditedDepartment(Department department) {
        Department editedDepartment = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/departmentEdit.fxml"));
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
        if (companyListView.getSelectionModel().getSelectedItem() == null) {
            displayError("Select company to insert role");
            return;
        }
        TextField newRoleField = new TextField();
        CheckBox freeGraphicCheckBox = new CheckBox();
        boolean okPressed = editRoleDialog(newRoleField, freeGraphicCheckBox);
        if (okPressed) {
            Role role = new Role();
            role.setTitle(newRoleField.getText());
            role.setFreeWork(freeGraphicCheckBox.isSelected());
            role.setCompany(companyListView.getSelectionModel().getSelectedItem());
            RoleService.createRole(role);
            updateTables();
        }
    }
    // Returns true if pressed ok button
    private boolean editRoleDialog(TextField newRoleField, CheckBox freeGraphicCheckBox) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        newRoleField.setPromptText("Enter role name");
        freeGraphicCheckBox.setText("Free graphic");
        Separator separator = new Separator();
        separator.setVisible(false);
        GridPane expContent = new GridPane();
        expContent.add(newRoleField, 0, 0);
        expContent.add(separator, 1, 0);
        expContent.add(freeGraphicCheckBox, 2, 0);
        alert.getDialogPane().setContent(expContent);
        alert.setHeaderText("Role information");
        alert.setTitle("Role");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        }
        return false;
    }

    public void editRoleClicked() {
        Role selectedRole = roleTable.getSelectionModel().getSelectedItem();
        if (selectedRole == null) {
            displayError("Select role to edit");
            return;
        }
        // Init field
        TextField newRoleField = new TextField(selectedRole.getTitle());
        CheckBox freeGraphicCheckBox = new CheckBox();
        freeGraphicCheckBox.setSelected(selectedRole.isFreeWork());
        boolean okPressed = editRoleDialog(newRoleField, freeGraphicCheckBox);
        if (okPressed) {
            Role editedRole = new Role();
            editedRole.setId(selectedRole.getId());
            editedRole.setTitle(newRoleField.getText());
            editedRole.setFreeWork(freeGraphicCheckBox.isSelected());
            editedRole.setCompany(companyListView.getSelectionModel().getSelectedItem());
            RoleService.updateRole(editedRole);
            updateTables();
        }
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
        } else displayError("Select role to delete");
    }

    // Employee controls
    public void newEmployeeClicked() {
        Employee editedEmployee = getEditedEmployee(null);
        if (editedEmployee == null) return;
        EmployeeService.createEmployee(editedEmployee);
        updateTables();
    }

    public void editEmployeeClicked() {
        Employee employee = employeeTable.getSelectionModel().getSelectedItem();
        if (employee == null) {
            displayError("Select employee to edit");
            return;
        }
        Employee editedEmployee = getEditedEmployee(employee);
        if (editedEmployee == null) return;
        EmployeeService.updateEmployee(editedEmployee);
        updateTables();
    }

    public void deleteEmployeeClicked() {
        Employee employee = employeeTable.getSelectionModel().getSelectedItem();
        if (employee != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete employee");
            alert.setHeaderText("Do you really want to delete employee?");
            alert.setContentText(employee.getName());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                EmployeeService.deleteEmployee(employee);
                updateTables();
            }
        } else displayError("Select employee to delete");
    }

    private Employee getEditedEmployee(Employee employee) {
        Employee editedEmployee = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/employeeEdit.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Employee");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root1));
            EmployeeEditController controller = fxmlLoader.getController();
            controller.init(companyListView.getItems());
            controller.sendEmployee(employee);
            stage.setResizable(false);
            stage.showAndWait();
            editedEmployee = controller.getEditedEmployee();
        } catch (Exception e) {
            displayError(e.toString());
        }
        return editedEmployee;
    }
}
