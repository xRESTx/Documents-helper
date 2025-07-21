package org.example.ui;

import javafx.application.Platform;
import javafx.concurrent.Task;
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
import org.example.convert.ExcelToPdf;
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
                WordTemplateProcessor processor = new WordTemplateProcessor();
                processor.generateDocument("src/main/resources/templates/template_ru.docx", values, "src/main/resources/output/output_ru.docx");
                processor.generateDocument("src/main/resources/templates/template_en.docx", values, "src/main/resources/output/output_en.docx");

                WordToPdf wordToPdf = new WordToPdf();
                wordToPdf.convert(new File("src/main/resources/output/output_ru.docx").getAbsolutePath(), new File("src/main/resources/output/output_ru.pdf").getAbsolutePath());
                wordToPdf.convert(new File("src/main/resources/output/output_en.docx").getAbsolutePath(), new File("src/main/resources/output/output_en.pdf").getAbsolutePath());

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
        filePathField.setPromptText("Путь к выбранному файлу (.docx, .doc, .xlsx)");

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
        saveFolderField.setPromptText("Папка, куда сохранить PDF");

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
        Tab tab = new Tab("Из Excel в PDF");
        tab.setClosable(false);

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        /* список файлов с кастомной ячейкой */
        ListView<File> fileList = new ListView<>();
        fileList.setPrefHeight(250);

        /* прогресс-бар и лог */
        ProgressBar progressBar = new ProgressBar(0);
        ListView<String> logList = new ListView<>();

        /* кнопки */
        Button selectBtn   = new Button("Выбрать Excel файлы");
        Button convertBtn  = new Button("Конвертировать");
        Button clearBtn    = new Button("Очистить всё");
        convertBtn.setDisable(true);

        /* выбор файлов */
        selectBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Выберите Excel файлы");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls"));
            fc.setInitialDirectory(new File(System.getProperty("user.home")));

            List<File> files = fc.showOpenMultipleDialog(null);
            if (files != null) {
                fileList.getItems().addAll(files);
                convertBtn.setDisable(fileList.getItems().isEmpty());
            }
        });

        /* очистка всего списка */
        clearBtn.setOnAction(e -> {
            fileList.getItems().clear();
            convertBtn.setDisable(true);
            progressBar.progressProperty().unbind();
            progressBar.setProgress(0);
            logList.getItems().clear();
        });

        /* удаление одного файла (×) */
        fileList.setCellFactory(lv -> new ListCell<>() {
            private final Button removeBtn = new Button("×");
            private final HBox hBox = new HBox(10, removeBtn);

            {
                removeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-font-weight: bold;");
                removeBtn.setOnAction(e -> {
                    File item = getItem();
                    if (item != null) {
                        fileList.getItems().remove(item);
                        convertBtn.setDisable(fileList.getItems().isEmpty());
                    }
                });
            }

            @Override
            protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(item.getName());
                    setGraphic(hBox);
                }
            }
        });

        /* конвертация */
        convertBtn.setOnAction(e -> {
            List<File> files = fileList.getItems();
            if (files.isEmpty()) return;

            progressBar.progressProperty().unbind();
            progressBar.setProgress(0);
            logList.getItems().clear();
            convertBtn.setDisable(true);
            clearBtn.setDisable(true);

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    int total = files.size();
                    int done = 0;
                    for (File src : files) {
                        String baseName = src.getName().replaceFirst("\\.[^.]+$", "");
                        File out = new File(src.getParentFile(), baseName + ".pdf");

                        updateMessage("Конвертирую: " + src.getName());
                        ExcelToPdf.convert(src.getAbsolutePath(), out.getAbsolutePath());

                        Platform.runLater(() -> logList.getItems().add("✓ " + out.getName()));
                        updateProgress(++done, total);
                    }
                    return null;
                }

                @Override
                protected void succeeded() {
                    convertBtn.setDisable(false);
                    clearBtn.setDisable(false);
                }

                @Override
                protected void failed() {
                    convertBtn.setDisable(false);
                    clearBtn.setDisable(false);
                    showAlert(Alert.AlertType.ERROR, "Ошибка конвертации:\n" + getException().getMessage());
                }
            };

            progressBar.progressProperty().bind(task.progressProperty());
            new Thread(task).start();
        });

        /* компоновка */
        HBox buttonBar = new HBox(10, selectBtn, convertBtn, clearBtn);
        root.getChildren().addAll(buttonBar,
                new Label("Выбранные файлы:"),
                fileList,
                progressBar,
                logList);
        tab.setContent(root);
        return tab;
    }

    private Tab createTranslateTab() {
        Tab tab = new Tab("Переводы");
        tab.setClosable(false);

        TextField filePathField = new TextField();
        filePathField.setEditable(false);
        filePathField.setPrefWidth(300);
        filePathField.setPromptText("Путь к выбранному Excel-файлу");

        Button chooseFileBtn = new Button("Выбрать Excel-файл");
        chooseFileBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Выберите Excel-файл");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel", "*.xls", "*.xlsx"));
            File selectedFile = chooser.showOpenDialog(null);
            if (selectedFile != null) {
                filePathField.setText(selectedFile.getAbsolutePath());
            }
        });

        TextField translatedNameField = new TextField();
        translatedNameField.setPromptText("Имя переведённого файла");

        TextField saveFolderField = new TextField();
        saveFolderField.setEditable(false);
        saveFolderField.setPrefWidth(300);
        saveFolderField.setPromptText("Папка, куда сохранить переведённый файл");

        Button chooseFolderBtn = new Button("Выбрать папку");
        chooseFolderBtn.setOnAction(ev -> {
            DirectoryChooser dirChooser = new DirectoryChooser();
            dirChooser.setTitle("Выберите папку");
            File folder = dirChooser.showDialog(null);
            if (folder != null) {
                saveFolderField.setText(folder.getAbsolutePath());
            }
        });

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);

        Button translateBtn = new Button("Перевести");

        translateBtn.setOnAction(e -> {
            String filePath = filePathField.getText().trim();
            String folderPath = saveFolderField.getText().trim();
            String outputName = translatedNameField.getText().trim();

            File inputFile = new File(filePath);

            // Если папка не указана — используем папку исходного файла
            if (folderPath.isEmpty()) {
                folderPath = inputFile.getParent();
            }

            // Если имя файла пустое — используем имя исходного + _translated
            if (outputName.isEmpty()) {
                String baseName = inputFile.getName().replaceAll("\\.(xls|xlsx)$", "");
                outputName = baseName + "_translated";
            }

            File outputFile = new File(folderPath + File.separator + outputName + getExtension(inputFile));

            Task<Void> translationTask = new Task<>() {
                @Override
                protected Void call() {
                    ExcelTranslator translator = new ExcelTranslator();
                    translator.translateExcel(inputFile, outputFile, (double v) -> updateProgress(v, 1.0));

                    Platform.runLater(() ->
                            showAlert(Alert.AlertType.INFORMATION, "Файл успешно переведён:\n" + outputFile.getName()));
                    return null;
                }
            };

            progressBar.progressProperty().bind(translationTask.progressProperty());
            new Thread(translationTask).start();
        });


        GridPane form = new GridPane();
        form.setVgap(12);
        form.setHgap(10);
        form.setPadding(new Insets(20));

        form.add(new Label("Исходный Excel-файл:"), 0, 0);
        form.add(filePathField, 1, 0);
        form.add(chooseFileBtn, 2, 0);

        form.add(new Label("Папка для сохранения:"), 0, 1);
        form.add(saveFolderField, 1, 1);
        form.add(chooseFolderBtn, 2, 1);

        form.add(new Label("Имя переведённого файла:"), 0, 2);
        form.add(translatedNameField, 1, 2);

        form.add(translateBtn, 1, 3);
        GridPane.setColumnSpan(translateBtn, 2);

        form.add(progressBar, 1, 4);
        GridPane.setColumnSpan(progressBar, 2);

        tab.setContent(form);
        return tab;
    }


    private String getExtension(File file) {
        if (file.getName().endsWith(".xls")) return ".xls";
        if (file.getName().endsWith(".xlsx")) return ".xlsx";
        return ".xlsx"; // безопасный дефолт
    }




    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
