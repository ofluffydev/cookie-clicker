package com.kadenfrisk.cookieclicker;

import com.kadenfrisk.cookieclicker.views.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CookieClickerApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage.setOnCloseRequest(event -> System.exit(0));

        MainView mainView = new MainView();
        Scene scene = new Scene(mainView, 800, 600);
        stage.setTitle("Cookie Clicker");
        stage.setScene(scene);
        stage.show();
    }
}