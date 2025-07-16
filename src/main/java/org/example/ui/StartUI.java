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
import org.example.convert.WordToPdf;
import org.example.logic.WordTemplateProcessor;
import org.example.translation.ExcelTranslator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ... [пакет и импорты как у вас выше]

public class StartUI {

    public void launchUI(Stage stage) {
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                createTemplateTab(),
                createPdfTab(),
                createExcelTab(),
                createTranslateTab()
        );

        Scene scene = new Scene(tabPane, 700, 500);
        stage.setScene(scene);
        stage.setTitle("Documents Helper");
        stage.show();
    }

    private Tab createTemplateTab() {
        Tab tab = new Tab("Шаблон");
        tab.setClosable(false);

        TextField dateField = new TextField();
        TextField invoiceField = new TextField();
        TextField hoursField = new TextField();
        TextField quantityField = new TextField();
        TextField totalField = new TextField();

        dateField.setPromptText("Дата");
        invoiceField.setPromptText("Номер счёта");
        hoursField.setPromptText("Часы");
        quantityField.setPromptText("Количество услуг");
        totalField.setPromptText("Итоговая сумма");

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
                WordTemplateProcessor.generateDocument("templates/template_ru.docx", values, "output/output_ru.docx");
                WordTemplateProcessor.generateDocument("templates/template_en.docx", values, "output/output_en.docx");

                WordToPdf wordToPdf = new WordToPdf();
                wordToPdf.convert(new File("output/output_ru.docx").getAbsolutePath(), new File("output/output_ru.pdf").getAbsolutePath());
                wordToPdf.convert(new File("output/output_en.docx").getAbsolutePath(), new File("output/output_en.pdf").getAbsolutePath());

                showAlert(Alert.AlertType.INFORMATION, "Документы успешно созданы!");
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Ошибка при создании документов");
            }
        });

        GridPane form = new GridPane();
        form.setVgap(10);
        form.setHgap(10);
        form.setPadding(new Insets(15));
        form.addRow(0, new Label("Дата:"), dateField);
        form.addRow(1, new Label("Номер счёта:"), invoiceField);
        form.addRow(2, new Label("Часы:"), hoursField);
        form.addRow(3, new Label("Количество услуг:"), quantityField);
        form.addRow(4, new Label("Итоговая сумма:"), totalField);
        form.add(saveBtn, 1, 5);

        tab.setContent(form);
        return tab;
    }

    private Tab createPdfTab() {
        Tab tab = new Tab("Из Word в PDF");
        tab.setClosable(false);

        TextField filePathField = new TextField();
        filePathField.setEditable(false);
        filePathField.setPrefWidth(300);

        Button chooseFileBtn = new Button("Выбрать файл");
        chooseFileBtn.setOnAction(ev -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Выберите файл");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Документы", "*.docx", "*.doc", "*.xlsx"));
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                filePathField.setText(selectedFile.getAbsolutePath());
            }
        });

        TextField pdfNameField = new TextField();
        pdfNameField.setPromptText("Имя PDF-файла");

        TextField saveFolderField = new TextField();
        saveFolderField.setEditable(false);
        saveFolderField.setPrefWidth(300);

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
            String pdfName = pdfNameField.getText().trim().isEmpty() ? "converted" : pdfNameField.getText().trim();

            if (filePath.isEmpty() || saveFolder.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Пожалуйста, выберите файл и папку для сохранения.");
                return;
            }

            WordToPdf wordToPdf = new WordToPdf();
            wordToPdf.convert(filePath, saveFolder + File.separator + pdfName + ".pdf");

            showAlert(Alert.AlertType.INFORMATION, "Файл успешно конвертирован!");
        });

        GridPane form = new GridPane();
        form.setVgap(12);
        form.setHgap(10);
        form.setPadding(new Insets(20));
        form.add(new Label("Исходный файл:"), 0, 0);
        form.add(filePathField, 1, 0);
        form.add(chooseFileBtn, 2, 0);

        form.add(new Label("Папка для сохранения:"), 0, 1);
        form.add(saveFolderField, 1, 1);
        form.add(chooseFolderBtn, 2, 1);

        form.add(new Label("Имя PDF-файла для сохранения:"), 0, 2);
        form.add(pdfNameField, 1, 2);

        form.add(convertBtn, 1, 3);
        GridPane.setColumnSpan(convertBtn, 2);

        tab.setContent(form);
        return tab;
    }

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

        VBox box = new VBox(15, selectBtn, fileListBox);
        box.setPadding(new Insets(20));
        tab.setContent(box);
        return tab;
    }

    private Tab createTranslateTab() {
        Tab tab = new Tab("Переводы");
        tab.setClosable(false);

        Label fileLabel = new Label("Файл не выбран");
        Button chooseFileBtn = new Button("Выбрать Excel-файл");
        Button translateBtn = new Button("Перевести");

        File[] selectedFile = new File[1]; // Храним выбранный файл

        chooseFileBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"));
            File file = chooser.showOpenDialog(tab.getTabPane().getScene().getWindow());
            if (file != null) {
                selectedFile[0] = file;
                fileLabel.setText("Выбран: " + file.getName());
            }
        });

        translateBtn.setOnAction(e -> {
            if (selectedFile[0] != null) {
                new ExcelTranslator().translateExcel(selectedFile[0]);
                showAlert(Alert.AlertType.INFORMATION, "Файл успешно переведён:\n" + selectedFile[0].getName().replace(".xlsx", "_translated.xlsx"));
            } else {
                showAlert(Alert.AlertType.WARNING, "Сначала выберите файл");
            }
        });


        VBox box = new VBox(15, chooseFileBtn, fileLabel, translateBtn);
        box.setPadding(new Insets(20));
        tab.setContent(box);
        return tab;
    }


    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
