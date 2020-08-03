package sample.activation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import sample.DBConnect;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

import static sample.activation.WinRegistry.HKEY_CURRENT_USER;

public class ActivityController implements Initializable {
    @FXML
    private AnchorPane activivationPage;

    public static String serkey = "";

    @FXML
    private TextField cdKey;

    @FXML
    private Label finalKey;

    @FXML
    private Button hideButton;

    Connection connection;
    ActionEvent event1 = new ActionEvent();

    @FXML
    void putIfenBetweenText(KeyEvent event) {

    }


    String URL_OpenWeatherMap_weather_London_uk = "http://mitrasoftwares.co.in/billingsecurity/gettingAllTheData.php";


    @FXML
    void sendThisDataToServer(ActionEvent event) throws Exception {
        System.out.println("event  ---- " + event);

        try {
            InetAddress ipAddr = InetAddress.getLocalHost();
            System.out.println("IP address = " + ipAddr.getHostAddress());
            URL_OpenWeatherMap_weather_London_uk = URL_OpenWeatherMap_weather_London_uk + "?cdKey=" + cdKey.getText() + "&ipAddr=" + ipAddr.getHostAddress();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
        getJsonData(event);
    }

    void getJsonData(ActionEvent event) throws Exception {

        finalKey.setText("Loading Please Wait...");
        String result = "";

        try {
            connection = DBConnect.connect();
            URL url_weather = new URL(URL_OpenWeatherMap_weather_London_uk);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url_weather.openConnection();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStreamReader inputStreamReader =
                        new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader =
                        new BufferedReader(inputStreamReader, 8192);
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                bufferedReader.close();

                String weatherResult = ParseResult(result);

                System.out.println(weatherResult);


                if (serkey.equals("You already used this key")) {
                    finalKey.setText(serkey);
                } else {


                    String query1 = "Insert into activate(cd_key_server,activity_key,status) values(?,?,?)";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                    preparedStatement1.setString(1, cdKey.getText());
                    preparedStatement1.setString(2, serkey);
                    preparedStatement1.setString(3, "success");

                    preparedStatement1.executeUpdate();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Registration process success");

                    ButtonType buttonOne = new ButtonType("Continue");
                    ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(buttonOne, buttonCancel);
                    Optional<ButtonType> results = alert.showAndWait();
                    if (results.get() == buttonOne) {

                        Stage window = new Stage();
                        window.close();
                        ((Node) event.getSource()).getScene().getWindow().hide();
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../sample.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();

                        Stage stage = new Stage();
                        stage.setTitle("Login");
                        stage.setScene(new Scene(root1, 900, 900));
                        stage.show();
                        try {

                            WinRegistry.createKey(HKEY_CURRENT_USER,"IRSRCB");
                            WinRegistry.writeStringValue(HKEY_CURRENT_USER, "IRSRCB", "Dial", "independent");
                        } catch (Exception e) {
                            Alert alert1 = new Alert(Alert.AlertType.ERROR);
                            alert1.setTitle("Error");
                            alert1.setTitle(e.getMessage());
                            alert1.show();
                        }

                    }

                }

            } else {
                System.out.println("Error in httpURLConnection.getResponseCode()!!!");
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static private String ParseResult(String json) throws ParseException, FileNotFoundException {

        try {

            JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(json);
            // get the error
            System.out.println(genreJsonObject.get("error"));

            //Get Array Values
            JSONArray genreArray = (JSONArray) genreJsonObject.get("SerialNumber");
            // get the first genre
            JSONObject firstGenre = (JSONObject) genreArray.get(0);

            System.out.println("get(0) == " + firstGenre);
            System.out.println(firstGenre.get("cd"));
            // get the Second
            JSONObject firstGenre1 = (JSONObject) genreArray.get(0);
            serkey = String.valueOf(firstGenre1.get("serialNo"));
            System.out.println(firstGenre1.get("serialNo"));
            // get the third


        } catch (ParseException e) {
            e.printStackTrace();
        }


        return "a";

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Connection con = null;
        try {

            con = DBConnect.connect();

            Statement statement = con.createStatement();


            ResultSet res = statement.executeQuery("Select * from activate where status='success'");
            int i = 0;


            while (res.next()) {

                i++;
            }


            if (i > 0) {
                hideButton.fire();

                finalKey.setText("Activation Success");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../sample.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();

                Stage stage = new Stage();
                stage.setTitle("Login");
                stage.setScene(new Scene(root1, 900, 900));
                stage.show();


                activivationPage.setOnMouseEntered(event -> {
                    ((Node) event.getSource()).getScene().getWindow().hide();
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void hideButton(ActionEvent event) throws Exception {
        System.out.println("hide clicked automatically");

    }

}

