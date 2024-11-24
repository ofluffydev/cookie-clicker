package com.kadenfrisk.cookieclicker.views;

import com.kadenfrisk.cookieclicker.CookieClickerApplication;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Random;

import static java.util.Objects.requireNonNull;
import static javafx.application.Platform.runLater;

public class MainView extends SplitPane {

    private double cookies = 0;
    private final Label cookieCount = new Label("Cookies: " + cookies);
    private int autoClickers = 0;

    public MainView() {
        // Left side
        VBox leftSide = new VBox();
        leftSide.setStyle("-fx-background-color: #2E0854; -fx-border-color: purple; -fx-padding: 10px;"); // Really dark purple background and purple border
        HBox.setHgrow(leftSide, Priority.ALWAYS);

        // Add an auto clicker purchase button
        Button buyAutoClickerButton = getBuyAutoClickerButton();
        buyAutoClickerButton.setOnAction(event -> {
            if (cookies >= 10) {
                cookies -= 10;
                autoClickers++;
                updateCookieCount();
                checkForUpgrades(buyAutoClickerButton);
            }
        });
        buyAutoClickerButton.setVisible(false); // Initially hide the button
        leftSide.getChildren().add(buyAutoClickerButton);

        // Right side with image and label
        VBox rightSide = new VBox();
        rightSide.setPrefWidth(0.25 * 800); // Assuming total width is 800
        rightSide.setAlignment(Pos.CENTER); // Center all content vertically
        rightSide.setStyle("-fx-background-color: #2E0854; -fx-border-color: purple;"); // Really dark purple background and purple border

        String imagePath = requireNonNull(CookieClickerApplication.class.getResource("rooster_8bit.png")).toExternalForm();
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(0.25 * 800);
        imageView.setPreserveRatio(true);

        cookieCount.setAlignment(Pos.CENTER); // Center the text under the image
        cookieCount.setStyle("-fx-text-fill: white; -fx-font-size: 16px;"); // White text color

        rightSide.getChildren().addAll(imageView, cookieCount);

        // Add sides to the SplitPane
        this.getItems().addAll(leftSide, rightSide);
        this.setDividerPositions(0.75); // Set the divider position to 75% from the left

        // Slowly rotate the image infinitely
        imageView.setRotate(0);
        new Thread(() -> {
            while (true) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    //noinspection CallToPrintStackTrace
                    e.printStackTrace();
                }
                imageView.setRotate(imageView.getRotate() + 1);
            }
        }).start();

        // Thread for auto-clickers
        new Thread(() -> {
            while (true) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    //noinspection CallToPrintStackTrace
                    e.printStackTrace();
                }
                if (autoClickers > 0) {
                    cookies += autoClickers * 0.2;
                    runLater(this::updateCookieCount);

                    // Check if the user can buy more auto-clickers
                    runLater(() -> checkForUpgrades(buyAutoClickerButton));
                }
            }
        }).start();

        // Add 1 to cookies when the image is clicked
        imageView.setOnMouseClicked(event -> {
            cookies++;
            updateCookieCount();
            checkForUpgrades(buyAutoClickerButton);
        });

        // Rotate the image when dragged
        imageView.setOnMouseDragged(event -> {
            imageView.setRotate(imageView.getRotate() + event.getSceneX() - imageView.getLayoutX());

            // Hue Shift the image randomly
            imageView.setEffect(new ColorAdjust(new Random().nextDouble(), 0, 0, 0));

            // Add a smaller amount of cookies when dragged
            cookies += 0.01;
            updateCookieCount();
            checkForUpgrades(buyAutoClickerButton);
        });
    }

    private Button getBuyAutoClickerButton() {
        // Return a button to buy an auto-clicker, with cute purple style
        Button buyAutoClickerButton = new Button("Buy Auto-Clicker (10 cookies)");
        buyAutoClickerButton.setStyle("-fx-background-color: #800080; -fx-text-fill: white;");
        return buyAutoClickerButton;
    }

    private void updateCookieCount() {
        // Update the cookie count label, round to 2 decimal places
        cookieCount.setText("Toasters: %.2f".formatted(cookies));
    }

    private void checkForUpgrades(Button buyAutoClickerButton) {
        // If the user has 10 or more cookies, allow them to buy an auto-clicker from the left side
        buyAutoClickerButton.setVisible(cookies >= 10);
    }
}