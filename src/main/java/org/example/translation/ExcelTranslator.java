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

        try (FileInputStream fis = new FileInputStream(inputFile);
             Workbook inputBook = inputFile.getName().endsWith(".xls")
                     ? new HSSFWorkbook(fis)
                     : new XSSFWorkbook(fis);
             XSSFWorkbook outputBook = new XSSFWorkbook()) {

            for (int i = 0; i < inputBook.getNumberOfSheets(); i++) {
                Sheet inputSheet = inputBook.getSheetAt(i);
                Sheet outputSheet = outputBook.createSheet(inputSheet.getSheetName());

                for (Row row : inputSheet) {
                    Row newRow = outputSheet.createRow(row.getRowNum());
                    for (Cell cell : row) {
                        Cell newCell = newRow.createCell(cell.getColumnIndex());

                        if (cell.getCellType() == CellType.STRING) {
                            String original = cell.getStringCellValue();
                            String translated = translateWithMyMemory(original);
                            newCell.setCellValue(translated);
                        } else {
                            newCell.setCellValue(cell.toString());
                        }
                    }
                }
            }

            String outputPath = inputFile.getParent() + File.separator +
                    inputFile.getName().replaceAll("\\.(xls|xlsx)$", "_translated.xlsx");

            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                outputBook.write(fos);
                System.out.println("Готово: " + outputPath);
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

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
