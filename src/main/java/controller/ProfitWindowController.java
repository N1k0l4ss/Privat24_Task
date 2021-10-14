package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import logic.Profit;
import logic.ProfitCalculator;
import model.Company;

import java.util.List;

public class ProfitWindowController {
    @FXML private TableView<Profit> profitTable;
    @FXML private TableColumn<Profit, String> infoColumn;
    @FXML private TableColumn<Profit, String> hourColumn;
    @FXML private TableColumn<Profit, String> moneyColumn;

    public void initialize(){
        infoColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        hourColumn.setCellValueFactory(new PropertyValueFactory<>("hourProfit"));
        moneyColumn.setCellValueFactory(new PropertyValueFactory<>("dollarProfit"));
    }

    public void init(Company company) {
        ProfitCalculator pc = new ProfitCalculator(company);
        List<Profit> profitList = pc.getProfitList();
        profitTable.setItems(FXCollections.observableArrayList(profitList));
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()){
            case ESCAPE:
                closeStage();
                break;
        }
    }

    private void closeStage(){
        Stage stage = (Stage) profitTable.getScene().getWindow();
        stage.close();
    }
}
