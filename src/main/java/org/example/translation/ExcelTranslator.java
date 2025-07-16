package org.example.translation;

import javafx.stage.FileChooser;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ExcelTranslator {

    public void translateExcel(File inputFile) {
        if (inputFile == null) {
            System.out.println("Файл не выбран");
            return;
        }

        boolean isXls = inputFile.getName().endsWith(".xls");

        try (FileInputStream fis = new FileInputStream(inputFile);
             Workbook workbook = isXls ? new HSSFWorkbook(fis) : new XSSFWorkbook(fis)) {

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
                        // Стили и формат остаются, так как это оригинальный файл
                    }
                }
            }

            // Сохраняем новый файл с _translated суффиксом
            String extension = isXls ? ".xls" : ".xlsx";
            String baseName = inputFile.getName().replaceAll("\\.(xls|xlsx)$", "");
            String outputPath = inputFile.getParent() + File.separator + baseName + "_translated" + extension;

            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                workbook.write(fos);
                System.out.println("Переведённый файл сохранён: " + outputPath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                System.out.println("Original: " + originalText);
                System.out.println("API Response: " + result);

                int start = result.indexOf("\"translatedText\":\"") + 18;
                int end = result.indexOf("\"", start);
                String rawTranslated = result.substring(start, end);

                // Декодируем Unicode
                String decoded = StringEscapeUtils.unescapeJava(rawTranslated);
                System.out.println("Translated: " + decoded);

                return decoded;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return originalText;
    }
}
