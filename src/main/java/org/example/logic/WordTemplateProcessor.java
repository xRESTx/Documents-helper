package org.example.logic;

import org.apache.poi.xwpf.usermodel.*;
import org.example.convert.WordToPdf;

import java.io.*;
import java.util.Map;

public class WordTemplateProcessor {

    /**
     * Обрабатывает шаблон Word (.docx), подставляя значения из карты по плейсхолдерам.
     * Например, "{{date}}" будет заменено на значение, переданное в коллекции.
     * Поддерживает замену как в абзацах, так и внутри таблиц документа.
     * @param templateName имя шаблона (.docx-файла), находящегося в resources/templates
     * @param values коллекция значений, где ключи соответствуют плейсхолдерам (без фигурных скобок)
     * @param outputPath путь, по которому будет сохранён сгенерированный документ
     * @throws IOException если шаблон не найден или произошла ошибка записи
     */
    public void generateDocument(String templateName, Map<String, String> values, String outputPath) throws IOException {
        InputStream templateStream = new FileInputStream(templateName);

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
        }
        doc.close();
    }

    /**
     * Выполняет замену плейсхолдеров внутри одного абзаца.
     * Если найдены плейсхолдеры, текст абзаца перезаписывается единым run.
     *
     * @param paragraph   абзац, в котором осуществляется поиск и замена
     * @param values      коллекция плейсхолдеров и их значений
     * @param fromTable   флаг, указывающий, находится ли абзац внутри таблицы (для отладки)
     */
    private void replaceInParagraph(XWPFParagraph paragraph, Map<String, String> values, boolean fromTable) {
        String fullText = paragraph.getText();
        if (fullText == null || fullText.isEmpty()) return;

        boolean hasPlaceholder = false;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            if (fullText.contains(placeholder)) {
                fullText = fullText.replace(placeholder, entry.getValue());
                hasPlaceholder = true;
            }
        }

// Удаляем все runs в абзаце, чтобы очистить плейсхолдер полностью.
// Каждый run — это фрагмент текста с уникальным стилем (например, жирный, курсив).
// Удаляем в обратном порядке, чтобы избежать смещения индексов.
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
