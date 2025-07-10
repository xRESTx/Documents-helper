package org.example.ui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.geometry.Insets;
import org.example.logic.WordTemplateProcessor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartUI {

    public void launchUI(Stage stage) {
        Button btn1 = new Button("Шаблон");
        Button btn2 = new Button("Из Word в Pdf");
        Button btn3 = new Button("Загрузка Excel файлов");
        Button btn4 = new Button("Переводы");

        VBox root = new VBox(10, btn1, btn2, btn3, btn4);
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
            saveBtn.setOnAction(event -> {
                if (dateField.getText().trim().isEmpty() ||
                        invoiceField.getText().trim().isEmpty() ||
                        hoursField.getText().trim().isEmpty() ||
                        quantityField.getText().trim().isEmpty() ||
                        totalField.getText().trim().isEmpty()) {

                    Alert alert = new Alert(Alert.AlertType.WARNING, "Пожалуйста, заполните все поля");
                    alert.showAndWait();
                    return;
                }

                Map<String, String> values = new HashMap<>();
                values.put("date", dateField.getText());
                values.put("invoice", invoiceField.getText());
                values.put("hours", hoursField.getText());
                values.put("quantity", quantityField.getText());
                values.put("total", totalField.getText());

                try{
                    WordTemplateProcessor.generateDocument("template_ru.docx", values, "output_ru.docx");
                    WordTemplateProcessor.generateDocument("template_en.docx", values, "output_en.docx");
                    Alert success = new Alert(Alert.AlertType.INFORMATION, "Документы успешно созданы!");
                    success.showAndWait();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Alert error = new Alert(Alert.AlertType.ERROR, "Ошибка при создании документов");
                    error.showAndWait();
                }

            });

            VBox inputBox = new VBox(8, dateField, invoiceField, hoursField, quantityField, totalField, saveBtn);
            inputBox.setPadding(new Insets(10));


            root.getChildren().setAll(btn1, btn2, btn3, new Separator(), inputBox);
        });

        // Кнопка 2 — загрузка файлов
        btn2.setOnAction(e -> {
            Label fileLabel = new Label("Исходный файл:");
            Button chooseFileBtn = new Button("Выбрать файл");
            TextField filePathField = new TextField();
            filePathField.setEditable(false);

            chooseFileBtn.setOnAction(ev -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Выберите файл");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Документы", "*.docx", "*.doc", "*.xlsx")
                );
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {
                    filePathField.setText(selectedFile.getAbsolutePath());
                }
            });

            TextField pdfNameField = new TextField();
            pdfNameField.setPromptText("Имя PDF-файла");

            TextField saveFolderField = new TextField();
            saveFolderField.setPromptText("Папка для сохранения");

            Button chooseFolderBtn = new Button("Выбрать папку");
            chooseFolderBtn.setOnAction(ev -> {
                DirectoryChooser dirChooser = new DirectoryChooser();
                dirChooser.setTitle("Выберите папку");
                File folder = dirChooser.showDialog(stage);
                if (folder != null) {
                    saveFolderField.setText(folder.getAbsolutePath());
                }
            });

            Button convertBtn = new Button("Конвертировать");

            convertBtn.setOnAction(ev -> {
                String filePath = filePathField.getText().trim();
                String saveFolder = saveFolderField.getText().trim();
                String pdfName = pdfNameField.getText().trim(); // не обязательно, но можно использовать

                // Проверка обязательных полей
                if (filePath.isEmpty() || saveFolder.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Недостаточно данных");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, выберите исходный файл и папку для сохранения.");
                    alert.showAndWait();
                    return;
                }

                // Если всё ок — продолжаем
                System.out.println("Конвертация в PDF...");
                System.out.println("Исходный файл: " + filePath);
                System.out.println("Папка сохранения: " + saveFolder);
                System.out.println("Имя PDF: " + (pdfName.isEmpty() ? "по умолчанию" : pdfName));
            });


            VBox convertBox = new VBox(8,
                    fileLabel, chooseFileBtn, filePathField,
                    pdfNameField,
                    chooseFolderBtn, saveFolderField,
                    convertBtn
            );
            convertBox.setPadding(new Insets(10));
            root.getChildren().setAll(btn1, btn2, btn3, new Separator(), convertBox);
        });

        // Кнопка 3 — загрузка файлов
        btn3.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Выберите Excel файлы");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx","*.xls"));
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

        // Кнопка 4 — пока не реализована
        btn4.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Функция будет добавлена позже");
            alert.showAndWait();
        });

        stage.setScene(new Scene(root, 500, 500));
        stage.setTitle("Documents Helper");
        stage.show();
    }
}
