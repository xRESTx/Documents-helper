package org.example.translation;

import javafx.stage.FileChooser;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.concurrent.Task;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.function.DoubleConsumer;
/**
 * Класс ExcelTranslator отвечает за перевод текстового содержимого Excel-файлов
 * (форматов .xls и .xlsx) с армянского языка на русский с помощью MyMemory API.
 *
 * Сохраняет переведённый файл либо по указанному пути, либо рядом с исходным,
 * добавляя суффикс "_translated".
 */
public class ExcelTranslator {
    /**
     * Выполняет перевод содержимого Excel-файла с армянского на русский язык.
     *
     * @param inputFile         исходный Excel-файл (.xls или .xlsx)
     * @param outputFile        файл назначения для сохранения результата перевода;
     *                          если null — файл создаётся рядом с исходным
     * @param progressCallback  функция, вызываемая при обновлении прогресса;
     *                          принимает значение от 0.0 до 1.0
     */
    public void translateExcel(File inputFile, File outputFile, DoubleConsumer progressCallback) {
        if (inputFile == null) {
            System.out.println("Файл не выбран");
            return;
        }

        boolean isXls = inputFile.getName().endsWith(".xls");

        try (FileInputStream fis = new FileInputStream(inputFile);
             Workbook workbook = isXls ? new HSSFWorkbook(fis) : new XSSFWorkbook(fis)) {

            // Подсчёт всех строк для прогресс-бара
            int totalRows = 0;
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                totalRows += sheet.getLastRowNum() + 1;
            }
            int current = 0;
            // Проход по всем листам
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);

                sheet.setAutobreaks(true);
                sheet.setFitToPage(true);

                PrintSetup printSetup = sheet.getPrintSetup();
                printSetup.setFitWidth((short) 1);   // Все столбцы — на одной странице
                printSetup.setFitHeight((short) 0);  // Высота — автоматическая (много страниц)


                for (Row row : sheet) {
                    for (Cell cell : row) {
                        if (cell.getCellType() == CellType.STRING) {
                            String originalText = cell.getStringCellValue();
                            String translatedText = translateWithMyMemory(originalText);

                            cell.setCellValue(translatedText); //  Перезаписываем значение
                        }

                    }
                    // Обновление прогресса по строкам
                    current++;
                    if (progressCallback != null && totalRows > 0) {
                        progressCallback.accept((double) current / totalRows);
                    }
                }
            }

            String extension = isXls ? ".xls" : ".xlsx";
            String outputPath;
            if (outputFile != null) {
                outputPath = outputFile.getAbsolutePath();
            } else {
                String baseName = inputFile.getName().replaceAll("\\.(xls|xlsx)$", "");
                outputPath = inputFile.getParent() + File.separator + baseName + "_translated" + extension;
            }

            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                workbook.write(fos);
                System.out.println("Переведённый файл сохранён: " + outputPath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Выполняет перевод строки текста с армянского на русский
     * через API сервиса MyMemory.
     *
     * @param originalText текст на армянском языке
     * @return переведённая строка на русском; если перевод не удался — возвращается оригинал
     */
    private String translateWithMyMemory(String originalText) {
        if (originalText == null || originalText.trim().isEmpty())
            return originalText;

        try {
            String encodedText = URLEncoder.encode(originalText, "UTF-8");
            String urlStr = "https://api.mymemory.translated.net/get?q=" + encodedText + "&langpair=hy|ru";

            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null)
                    response.append(line);

                String result = response.toString();

                // Отладочный вывод
                //System.out.println("Original: " + originalText);
                //System.out.println("API Response: " + result);

                int start = result.indexOf("\"translatedText\":\"") + 18;
                int end = result.indexOf("\"", start);
                String rawTranslated = result.substring(start, end);

                // Декодируем Unicode
                String decoded = StringEscapeUtils.unescapeJava(rawTranslated);
                //System.out.println("Translated: " + decoded);

                return decoded;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return originalText;
    }
}
