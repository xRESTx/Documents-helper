package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.ui.StartUI;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        StartUI startUI = new StartUI();
        startUI.launchUI(primaryStage);
        String dllPath = new java.io.File("src/jacob-1.14.3-x64.dll").getAbsolutePath();
        System.setProperty("jacob.dll.path", dllPath);
        com.jacob.com.LibraryLoader.loadJacobLibrary();
    }

    public static void main(String[] args) {
        launch();
    }
}
