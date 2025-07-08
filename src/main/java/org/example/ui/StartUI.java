package org.example.ui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.geometry.Insets;
import java.io.File;
import java.util.List;

public class StartUI {

    public void launchUI(Stage stage) {
        Button btn1 = new Button("Шаблон");
        Button btn2 = new Button("Загрузка Excel файлов");
        Button btn3 = new Button("Переводы");

        VBox root = new VBox(10, btn1, btn2, btn3);
        root.setPadding(new javafx.geometry.Insets(20));

        // Кнопка 1 — поля ввода
        btn1.setOnAction(e -> {
            TextField dateField = new TextField();
            dateField.setPromptText("Введите дату");

            TextField invoiceField = new TextField();
            invoiceField.setPromptText("Введите номер счёта");

            TextField hoursField = new TextField();
            hoursField.setPromptText("Введите количество часов");

            TextField quantityField = new TextField();
            quantityField.setPromptText("Введите количество");

            TextField totalField = new TextField();
            totalField.setPromptText("Введите итоговую сумму");

            Button saveBtn = new Button("Сохранить");

            VBox inputBox = new VBox(8, dateField, invoiceField, hoursField, quantityField, totalField, saveBtn);
            inputBox.setPadding(new Insets(10));


            root.getChildren().setAll(btn1, btn2, btn3, new Separator(), inputBox);
        });

        // Кнопка 2 — загрузка файлов
        btn2.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Выберите Excel файлы");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

            List<File> files = fileChooser.showOpenMultipleDialog(stage);
            if (files != null) {
                VBox fileList = new VBox(5);
                for (File file : files) {
                    fileList.getChildren().add(new Label(file.getName()));
                }
                root.getChildren().setAll(btn1, btn2, btn3, new Separator(), fileList);
            }
        });

        // Кнопка 3 — пока не реализована
        btn3.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Функция будет добавлена позже");
            alert.showAndWait();
        });

        stage.setScene(new Scene(root, 400, 400));
        stage.setTitle("Documents Helper");
        stage.show();
    }
}
