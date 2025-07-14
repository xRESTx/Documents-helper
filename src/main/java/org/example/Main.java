package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.ui.StartUI;

import java.io.File;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        StartUI startUI = new StartUI();
        startUI.launchUI(primaryStage);
    }

    public static void main(String[] args) {
        launch();
    }
}
