package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

import static java.time.temporal.ChronoUnit.DAYS;

public class return_stock_view_controler implements Initializable {

    public DatePicker date1;
    public DatePicker date2;

    @FXML
    private Pane view;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String query="Select * from return_stock";
        try {
            LoadingDataToTableView.Welcome(query, view,300,300);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void view_returnStock(ActionEvent event) throws Exception {

        String query = "Select * from return_stock";

        if(date1.getValue()!=null && date2.getValue()!=null)
        {
            long daysBetween = DAYS.between(date1.getValue(), date2.getValue());

            if(daysBetween>0)
            {
                query = "Select * from return_stock where return_date>='" + date1.getValue() + "' " +
                        "and return_date<='" + date2.getValue() + "'";
            }
        }

        try {
            LoadingTableViewDataSelectedRowName.Welcome(query, view,300, 800);
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Logger.log(exceptionAsString);
        }
    }

    public void return_excel_formate(ActionEvent event) throws Exception {

        String query = "Select * from return_stock";

        if(date1.getValue()!=null && date2.getValue()!=null)
        {
            long daysBetween = DAYS.between(date1.getValue(), date2.getValue());
            if(daysBetween>0)
            {
                query = "Select * from return_stock where return_date>='" + date1.getValue() + "' " +
                        "and return_date<='" + date2.getValue() + "'";
            }
        }

        File dir = new File(P.drive_name()+FilePath.FOLDER_PATH);
        dir.mkdir();
        File dir2=new File(P.drive_name()+FilePath.FOLDER_PATH+"RETURN_EXPORT");
        dir2.mkdir();
        String path=P.drive_name()+FilePath.FOLDER_PATH+"/RETURN_EXPORT/RETURN_STOCK.csv";

        Controller.export_excel(query,path);
    }
}