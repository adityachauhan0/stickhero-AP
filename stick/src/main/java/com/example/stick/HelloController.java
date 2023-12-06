package com.example.stick;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.security.cert.PolicyNode;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    private Stage stage;
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

    private boolean wKeyPressed = false;
    private boolean enlargementInProgress = false;
    private Timeline stickEnlargeTimeline;

    @FXML
    private Line stick;

    @FXML
    private void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.W && !wKeyPressed) {
            wKeyPressed = true;
            startStickEnlargeAnimation();
        }
    }

    @FXML
    private void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.W) {
            wKeyPressed = false;
            stopStickEnlargeAnimation();
            if (enlargementInProgress) {
                rotateStick(); // Call the method to initiate rotation after enlargement
            }
            enlargementInProgress = false;
        }
    }

    private void startStickEnlargeAnimation() {
        if (stickEnlargeTimeline == null) {
            stickEnlargeTimeline = new Timeline(
                    new KeyFrame(Duration.millis(5), event -> increaseStickLength())
            );
            stickEnlargeTimeline.setCycleCount(Animation.INDEFINITE);
        }
        enlargementInProgress = true;
        stickEnlargeTimeline.play();
    }

    private void stopStickEnlargeAnimation() {
        if (stickEnlargeTimeline != null) {
            stickEnlargeTimeline.stop();
        }
    }

    private void increaseStickLength() {
        double incrementValue = 0.5;
        double maxStickLength = 100000.0; // Set your desired maximum length

        if (stick.getEndY() < maxStickLength) {
            stick.setEndY(stick.getEndY() - incrementValue);
        } else {
            stopStickEnlargeAnimation(); // Stop the animation when the maximum length is reached
        }

    }
    @FXML
    private AnchorPane anchorPane;
    // Add this method to your controller class
    // Modify checkCollisionWithRectangles method
    private Bounds lastStickBounds;
    private Map<Node, Bounds> lastRectBounds = new HashMap<>();

    @FXML
    private Text scoretext;

    private boolean checkCollisionWithPlatforms() {
        // Get the transformed bounds of the player
        Bounds playerBounds = player.localToScene(player.getBoundsInLocal());

        // Iterate through all children of the AnchorPane
        for (Node node : anchorPane.getChildren()) {
            if (node instanceof Rectangle && node.getId() != null && node.getId().startsWith("platform")) {
                // Get the transformed bounds of the rectangle
                Bounds rectBounds = node.localToScene(node.getBoundsInLocal());

                // Check if the bottom center of the player is within the bounds of the platform
                double playerBottomCenterX = playerBounds.getMinX() + playerBounds.getWidth() / 2.0;
                double playerBottomCenterY = playerBounds.getMaxY();

                if (playerBottomCenterX >= rectBounds.getMinX() && playerBottomCenterX <= rectBounds.getMaxX()
                        && playerBottomCenterY >= rectBounds.getMinY() && playerBottomCenterY <= rectBounds.getMaxY()) {
                    // Collision detected
                    increaseScore();
                    return true;
                }
            }
        }

        return false; // No collision detected
    }

    private void increaseScore() {
        // Assuming scoretext is an instance of Text
        int currentScore = Integer.parseInt(scoretext.getText());
        int newScore = currentScore + 1;
        scoretext.setText(String.valueOf(newScore));
    }









    @FXML
    private ImageView player;

    private void rotateStick() {
        // Calculate the top point of the stick before rotation
        double pivotXBeforeRotation = stick.getStartX() + (stick.getEndX() - stick.getStartX()) / 2.0;
        double pivotYBeforeRotation = stick.getStartY();

        // Create a Rotate transformation for smooth rotation
        Rotate rotate = new Rotate(0, pivotXBeforeRotation, pivotYBeforeRotation);
        stick.getTransforms().add(rotate);

        // Create a Timeline to animate the rotation
        Timeline rotationTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rotate.angleProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(rotate.angleProperty(), 90))
        );

        // Apply rotation to the top point of the stick
        Point2D rotatedTopPoint = rotate.transform(pivotXBeforeRotation, pivotYBeforeRotation);

        // Calculate the end point of the stick after rotation
        double rotatedStickEndX = stick.getEndX();
        double rotatedStickEndY = stick.getEndY();

        // Calculate the player's final position on the stick (only along the x-axis)
        double playerFinalX = rotatedStickEndX - player.getBoundsInLocal().getWidth() / 2.0;

        // Create a TranslateTransition for player's smooth translation along the x-axis
        TranslateTransition translationTransition = new TranslateTransition(Duration.seconds(1), player);
        translationTransition.setToX(-stick.getEndY());


        // Play both animations in sequence
        SequentialTransition sequentialTransition = new SequentialTransition(rotationTimeline, translationTransition);

        sequentialTransition.setOnFinished(event -> handleTransitionFinished(null));


        // Add an event handler to be executed when the transition finishes
        sequentialTransition.setOnFinished(event -> {
            // Check for collision with rectangles
            if (!checkCollisionWithPlatforms()) {
                // No collision, switch to the home screen
                try {
                    System.out.println("No collisions.");
                    switchToHome(new ActionEvent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        sequentialTransition.play();
    }


    private void handleTransitionFinished(ActionEvent event) {
        // Check for collision with rectangles after a delay
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            // Get the transformed bounds of the stick
            Bounds stickBounds = stick.localToScene(stick.getBoundsInLocal());

            // Iterate through all children of the AnchorPane
            for (Node node : anchorPane.getChildren()) {
                if (node instanceof Rectangle && node.getId() != null && node.getId().startsWith("platform")) {
                    // Get the transformed bounds of the rectangle
                    Bounds rectBounds = node.localToScene(node.getBoundsInLocal());

                    // Check for collision using transformed bounds
                    if (stickBounds.intersects(rectBounds)) {
                        // Check if the end of the stick is touching the platform
                        double stickEndX = stick.getEndX();
                        double stickEndY = stick.getEndY();
                        if (stickEndY >= rectBounds.getMinY() && stickEndY <= rectBounds.getMaxY()) {
                            // End of the stick is touching the platform, generate a new stick
                            generateNewStick();
                            startStickEnlargeAnimation();
                            return;
                        }
                    }
                }
            }

            // No collision or end of the stick not touching the platform, switch to the home screen
            try {
                switchToHome(new ActionEvent());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        pause.play();
    }

    private void generateNewStick() {
        Line newStick = new Line();
        newStick.setStartX(player.getX() + 10);
        newStick.setStartY(player.getY());
        newStick.setEndX(player.getX() + 20); // Assuming the original length of the stick is 10px
        newStick.setEndY(-25.0); // Set your original endY value

        // Add the new stick to the anchorPane
        anchorPane.getChildren().add(newStick);

        // Clear any transformations on the new stick
        newStick.getTransforms().clear();
    }




















    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
