package com.example.stick;


import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    private double initialX;
    private double initialY ;
    private double initialWidth;
    private double initialHeight;

    @FXML
    private void handleKeyPress(KeyEvent event) {
        initialY = myRectangle.getY();
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
    private ImageView player;
    @FXML
    private void handleKeyRelease(KeyEvent event) {
        if (event.getCode() == KeyCode.W && wKeyPressed) {
            // Reset the flag
            wKeyPressed = false;

            // Rotate the rectangle around its bottom-right point
            double pivotX = myRectangle.getX() + myRectangle.getWidth();
            double pivotY = myRectangle.getY() + myRectangle.getHeight();
            myRectangle.setRotate(myRectangle.getRotate() + 90.0);

            // Swap width and height, and increase length towards the right
            double newWidth = myRectangle.getHeight(); // swap width and height
            double newHeight = myRectangle.getWidth() + 10.0; // increase length towards the right

            // Save initial position and size
            if (initialX == 0 && initialY == 0) {
                initialX = myRectangle.getX();
                initialY = myRectangle.getY();
                initialWidth = myRectangle.getWidth();
                initialHeight = myRectangle.getHeight();
            }

            // Set the rotated rectangle properties
            myRectangle.setWidth(newWidth);
            myRectangle.setHeight(newHeight);
            myRectangle.setY(pivotY - newWidth);

            // Reset rotation to 0 for the next key press
            myRectangle.setRotate(0.0);

            // Bring the rectangle down by the same amount it was increased
            myRectangle.setY(myRectangle.getY()-initialY + 19); // Adjust the value accordingly
            myRectangle.setHeight(10);

            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), player);

            // Set the destination X coordinate to be the right side of the myRectangle
            double destinationX = myRectangle.getX() + myRectangle.getWidth();

            // Set the translation
            translateTransition.setToX(destinationX);

            // Play the animation
            translateTransition.play();
            myRectangle.setX(myRectangle.getX()+myRectangle.getWidth());
            initialX= myRectangle.getX();

        }
        myRectangle.setWidth(10.0);

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