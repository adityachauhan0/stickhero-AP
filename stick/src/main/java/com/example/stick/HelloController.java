package com.example.stick;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    private Stage stage;
    @FXML
    private Text cherriesText;
    private Scene scene;
    private Parent parent;

    public void switchToScene2(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("scene2.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }
    public void switchToHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToSettings(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("settings.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToShop(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("shop.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void Exit(ActionEvent event) throws IOException {
        Platform.exit();
    }

    @FXML
    private Rectangle myRectangle;
    private boolean wKeyPressed = false;

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.W) {
            // Set flag to indicate W key is pressed
            wKeyPressed = true;

            // Increase rectangle size and move it upwards
            double newHeight = myRectangle.getHeight() + 10.0;
            myRectangle.setHeight(newHeight);

            double newY = myRectangle.getY() - 10.0;
            myRectangle.setY(newY);
        }
    }

    @FXML
    private void handleKeyRelease(KeyEvent event) {
        if (event.getCode() == KeyCode.W && wKeyPressed) {
            // Reset the flag
            wKeyPressed = false;

            // Rotate the rectangle to simulate falling towards the right
            myRectangle.setRotate(45.0);  // Adjust the angle based on your preference
        }
    }


    // Add a method to set the cherries count
    public void setCherriesCount(int count) {
        cherriesText.setText(Integer.toString(count));
    }

    public int getCherriesCount() {
        return Integer.parseInt(cherriesText.getText());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}