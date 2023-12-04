package com.example.stick;


import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
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
    @FXML
    private Text scoretext;
    private Scene scene;
    private Parent parent;
    private Rectangle lastCollidedRectangle = null;

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
            myRectangle.setY(myRectangle.getY() - initialY + 19); // Adjust the value accordingly
            myRectangle.setHeight(10);

            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), player);

            // Set the destination X coordinate to be the right side of the myRectangle
            double destinationX = myRectangle.getX() + myRectangle.getWidth();

            // Set the translation
            translateTransition.setToX(destinationX);

            // Play the animation
            translateTransition.play();
            myRectangle.setX(myRectangle.getX() + myRectangle.getWidth());
            initialX = myRectangle.getX();

            // Check for intersections with rectangles that don't have an fx:id in the last 10 pixels from the right
            for (Node node : ((AnchorPane) myRectangle.getParent()).getChildren()) {
                if (node instanceof Rectangle && ((Rectangle) node).getFill().equals(javafx.scene.paint.Color.valueOf("#2f2f2f"))) {
                    String fxId = node.getId();
                    // Check if the shape has already been scored and is an instance of Rectangle
                    if (intersectsLast10Pixels(node, myRectangle) && !node.equals(player) && (fxId == null || fxId.isEmpty())) {
                        if (!((Rectangle) node).isDisable()) {
                            // Increase scoretext by 1
                            setScoreText(getScoreText() + 1);
                            // Disable the shape to prevent scoring multiple times
                            ((Rectangle) node).setDisable(true);
                        }
                    }
                } else if (node instanceof Rectangle && ((Rectangle) node).getFill().equals(javafx.scene.paint.Color.valueOf("#ff1f1f"))) {
                    String fxId = node.getId();
                    // Check if the shape has already been scored and is an instance of Rectangle
                    if (intersectsLast10Pixels(node, myRectangle) && !node.equals(player) && (fxId == null || fxId.isEmpty())) {
                        if (!((Rectangle) node).isDisable()) {
                            // Increase cherries count by 1
                            setCherriesCount(getCherriesCount() + 1);
                            setScoreText(getScoreText() + 1);
                            // Disable the shape to prevent scoring multiple times
                            ((Rectangle) node).setDisable(true);
                        }
                    }
                }
            }
            if (!stickLandedOnPlatform()) {
                // Stick didn't land on the platform, restart the scene
                restartScene();
            }
        }
        myRectangle.setWidth(10.0);
    }

    // Add a method to check for intersection in the last 10 pixels from the right
    private boolean intersectsLast10Pixels(Node shape, Rectangle myRectangle) {
        double myRectangleRightX = myRectangle.getBoundsInParent().getMaxX();
        double shapeLast10PixelsRightX = shape.getBoundsInParent().getMaxX() - 10;

        return myRectangleRightX >= shapeLast10PixelsRightX && myRectangle.getBoundsInParent().intersects(shape.getBoundsInParent());
    }


    // Add a method to set the scoretext
    public void setScoreText(int count) {
        scoretext.setText(Integer.toString(count));
    }

    public int getScoreText() {
        return Integer.parseInt(scoretext.getText());
    }




    // Add a method to set the cherries count
    public void setCherriesCount(int count) {
        cherriesText.setText(Integer.toString(count));
    }

    public int getCherriesCount() {
        return Integer.parseInt(cherriesText.getText());
    }
    // Add a method to check if the stick landed on the platform
    private boolean stickLandedOnPlatform() {
        AnchorPane anchorPane = (AnchorPane) myRectangle.getParent();

        for (Node node : anchorPane.getChildren()) {
            if (node instanceof Rectangle && ((Rectangle) node).getFill().equals(Color.web("#2f2f2f")) && !node.equals(myRectangle)) {
                Bounds platformBounds = node.getBoundsInParent();
                Bounds stickBounds = myRectangle.getBoundsInParent();

                if (stickBounds.intersects(platformBounds)) {
                    return true; // Stick collided with a platform
                }
            }
        }

        return false; // Stick didn't collide with any platform
    }



    // Add a method to restart the scene
    // Add a method to restart the scene
    private void restartScene() {
        try {
            // Load the FXML file for the current scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();

            // Set up the stage and scene
            stage = (Stage) myRectangle.getScene().getWindow();
            scene = new Scene(root);

            // Set the controller for the FXML file (adjust the controller class accordingly)
            HelloController controller = loader.getController();
            controller.initialize(null, null);

            // Set the scene and show the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}