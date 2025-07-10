package org.example.logic;

import org.apache.poi.xwpf.usermodel.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class WordTemplateProcessor {

    public static void generateDocument(String templateName, Map<String, String> values, String outputPath) throws IOException {
        InputStream templateStream = WordTemplateProcessor.class.getClassLoader()
                .getResourceAsStream("templates/" + templateName);
        if (templateStream == null) throw new FileNotFoundException("Шаблон не найден: " + templateName);

        XWPFDocument doc = new XWPFDocument(templateStream);

        // Заменяем в параграфах
        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            replaceInParagraph(paragraph, values, false);
        }

        // Заменяем в таблицах
        for (XWPFTable table : doc.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        replaceInParagraph(paragraph, values, true);
                    }
                }
            }
        }

        try (FileOutputStream out = new FileOutputStream(outputPath)) {
            doc.write(out);
            /*System.out.println("Документ сохранён: " + outputPath);*/
        }
        doc.close();
    }
    private static void replaceInParagraph(XWPFParagraph paragraph, Map<String, String> values, boolean fromTable) {
        String fullText = paragraph.getText();
        if (fullText == null || fullText.isEmpty()) return;

        boolean hasPlaceholder = false;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            if (fullText.contains(placeholder)) {
                fullText = fullText.replace(placeholder, entry.getValue());
                hasPlaceholder = true;
                /*if (fromTable) {
                    System.out.println("[Таблица] Найден плейсхолдер: " + placeholder + " → " + entry.getValue());
                } else {
                    System.out.println("Абзац → заменено: " + placeholder + " → " + entry.getValue());
                }*/
            }
        }


        if (hasPlaceholder) {
            // Удаляем все runs безопасно
            int runCount = paragraph.getRuns().size();
            for (int i = runCount - 1; i >= 0; i--) {
                paragraph.removeRun(i);
            }
            XWPFRun run = paragraph.createRun();
            run.setText(fullText);
        }
    }
}
