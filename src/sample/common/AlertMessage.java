package sample.common;

import javafx.scene.control.Alert;

/**
 * Created by Mitra on 03/14/18.
 */
public class AlertMessage {
    public  static Alert alert;

    public  static void error(String error){
        alert=new Alert(Alert.AlertType.ERROR);
        alert.setContentText(error);
        alert.showAndWait();
    }

    public  static void inform(String inform){
        alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(inform);
        alert.showAndWait();
    }

}
