package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class reg3 {

    public TextField s_quantity;

    @FXML
    Label printStock;

    @FXML
    private Pane RemainingStock;

    public void quntity_wise_display(ActionEvent actionEvent) throws Exception {
        String quntity=s_quantity.getText();
        printStock.setText(quntity);
        String SQL = "select * from stock where stockquantity <='"+quntity+"'";
        LoadingDataToTableView.Welcome(SQL, RemainingStock);
    }

    @FXML
    void printStocks_byQyantity(ActionEvent event) throws Exception {

        String q=s_quantity.getText();
        String query ="select * from stock where stockquantity <='"+q+"'";

        File dir = new File(P.drive_name()+FilePath.FOLDER_PATH);
        dir.mkdir();
        File dir2=new File(P.drive_name()+FilePath.FOLDER_PATH+"Stock Quantity Report");
        dir2.mkdir();
        String path=P.drive_name()+FilePath.FOLDER_PATH+"/Stock Quantity Report/Stock_report.csv";

        Controller.export_excel(query,path);
    }

}
