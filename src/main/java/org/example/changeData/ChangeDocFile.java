package org.example.changeData;

import org.apache.poi.xwpf.usermodel.*;
import org.example.models.DataWord;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeDocFile {

    public void changeWord(String src) throws IOException {
        FileInputStream fis = new FileInputStream(src);
        XWPFDocument document = new XWPFDocument(fis);
        String date = "01.02.2005";
        String invoices = "2";
        String hours = "3";
        String amount = "5";
        String cost = "1000";

        DataWord dataWord = new DataWord(date, invoices, hours, amount, cost);

        Map<String, String> replacements = Map.of(
                "fieldData", dataWord.getDate(),
                "fieldInvoices", dataWord.getInvoices(),
                "fieldHour", dataWord.getHours(),
                "fieldQuantity", dataWord.getAmount(),
                "fieldCost", dataWord.getCost()
        );


        // Проходим по всем абзацам
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                for (int i = 0; i < run.getCTR().sizeOfTArray(); i++) {
                    String textPart = run.getText(i);
                    if (textPart != null) {
                        // например, замена поля
                        for (Map.Entry<String, String> entry : replacements.entrySet()) {
                            if (textPart.contains(entry.getKey())) {
                                textPart = textPart.replace(entry.getKey(), entry.getValue());
                                run.setText(textPart, i);
                            }
                        }
                    }
                }
            }
        }
        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        for (XWPFRun run : paragraph.getRuns()) {
                            for (int i = 0; i < run.getCTR().sizeOfTArray(); i++) {
                                String textPart = run.getText(i);
                                if (textPart != null) {
                                    // например, замена поля
                                    for (Map.Entry<String, String> entry : replacements.entrySet()) {
                                        if (textPart.contains(entry.getKey())) {
                                            textPart = textPart.replace(entry.getKey(), entry.getValue());
                                            run.setText(textPart, i);
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }

        // Сохраняем изменения в новый файл
        FileOutputStream out = new FileOutputStream("output.docx");
        document.write(out);
        out.close();
        document.close();
        fis.close();
    }
}
