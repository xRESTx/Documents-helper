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
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                createTemplateTab(),
                createPdfTab(),
                createExcelTab(),
                createTranslateTab()
        );

        Scene scene = new Scene(tabPane, 600, 500);
        stage.setScene(scene);
        stage.setTitle("Documents Helper");
        stage.show();
    }

    //Задача 1 - Заполнение шаблона(Сделано)
    /**
     * Окно для работы с шаблоном, заполнения полей и подставление значений в файлы с сохранением
     * @return
     */
    private Tab createTemplateTab() {
        Tab tab = new Tab("Шаблон");
        tab.setClosable(false);

        TextField dateField = new TextField();
        dateField.setPromptText("Введите дату");

        TextField invoiceField = new TextField();
        invoiceField.setPromptText("Введите номер счёта");

        TextField hoursField = new TextField();
        hoursField.setPromptText("Введите количество часов");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Введите количество услуг");

        TextField totalField = new TextField();
        totalField.setPromptText("Введите итоговую сумму");

        Button saveBtn = new Button("Сохранить");

        saveBtn.setOnAction(e -> {
            if (dateField.getText().trim().isEmpty() ||
                    invoiceField.getText().trim().isEmpty() ||
                    hoursField.getText().trim().isEmpty() ||
                    quantityField.getText().trim().isEmpty() ||
                    totalField.getText().trim().isEmpty()) {

                showAlert(Alert.AlertType.WARNING, "Пожалуйста, заполните все поля");
                return;
            }

            Map<String, String> values = new HashMap<>();
            values.put("date", dateField.getText());
            values.put("invoice", invoiceField.getText());
            values.put("hours", hoursField.getText());
            values.put("quantity", quantityField.getText());
            values.put("total", totalField.getText());

            try {
                WordTemplateProcessor.generateDocument("template_ru.docx", values, "output_ru.docx");
                WordTemplateProcessor.generateDocument("template_en.docx", values, "output_en.docx");
                showAlert(Alert.AlertType.INFORMATION, "Документы успешно созданы!");
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Ошибка при создании документов");
            }
        });

        VBox box = new VBox(10, dateField, invoiceField, hoursField, quantityField, totalField, saveBtn);
        box.setPadding(new Insets(15));
        tab.setContent(box);
        return tab;
    }

    //Задача 2 - Из Word в Pdf
    /**
     * Выбор файла docx для преобразования в pdf
     * @return
     */
    private Tab createPdfTab() {
        Tab tab = new Tab("Из Word в PDF");
        tab.setClosable(false);

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
            File selectedFile = fileChooser.showOpenDialog(null);
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
            File folder = dirChooser.showDialog(null);
            if (folder != null) {
                saveFolderField.setText(folder.getAbsolutePath());
            }
        });

        Button convertBtn = new Button("Конвертировать");
        convertBtn.setOnAction(ev -> {
            String filePath = filePathField.getText().trim();
            String saveFolder = saveFolderField.getText().trim();
            String pdfName = pdfNameField.getText().trim();

            if (filePath.isEmpty() || saveFolder.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Пожалуйста, выберите исходный файл и папку для сохранения.");
                return;
            }

            System.out.println("Конвертация в PDF...");
            System.out.println("Исходный файл: " + filePath);
            System.out.println("Папка сохранения: " + saveFolder);
            System.out.println("Имя PDF: " + (pdfName.isEmpty() ? "по умолчанию" : pdfName));
        });

        VBox box = new VBox(10,
                fileLabel, chooseFileBtn, filePathField,
                pdfNameField,
                chooseFolderBtn, saveFolderField,
                convertBtn
        );
        box.setPadding(new Insets(15));
        tab.setContent(box);
        return tab;
    }

    // Задача 3 - Из Excel в Pdf
    /**
     * Выбор нескольких excel файлов для преобразования в pdf
     * @return
     */
    private Tab createTranslateTab() {
        Tab tab = new Tab("Переводы");
        tab.setClosable(false);

        Label label = new Label("Функция будет добавлена позже.");
        VBox box = new VBox(label);
        box.setPadding(new Insets(20));
        tab.setContent(box);
        return tab;
    }

    // Задача 4 - Переводы(ничего нет)
    /**
     * Выбор файла excel для перевода содержимого
     * @return
     */
    private Tab createExcelTab() {
        Tab tab = new Tab("Загрузка Excel");
        tab.setClosable(false);

        VBox fileListBox = new VBox(10);
        Button selectBtn = new Button("Выбрать Excel файлы");

        selectBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Выберите Excel файлы");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls"));
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

            List<File> files = fileChooser.showOpenMultipleDialog(null);
            fileListBox.getChildren().clear();
            if (files != null) {
                for (File file : files) {
                    fileListBox.getChildren().add(new Label(file.getName()));
                }
            }
        });

        VBox box = new VBox(10, selectBtn, fileListBox);
        box.setPadding(new Insets(15));
        tab.setContent(box);
        return tab;
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

}
