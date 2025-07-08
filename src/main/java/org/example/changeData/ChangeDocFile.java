package org.example.changeData;

import org.apache.poi.xwpf.usermodel.*;
import java.io.*;

public class ChangeDocFile {

    public void changeWord(String src) throws IOException {
        FileInputStream fis = new FileInputStream("input.docx");
        XWPFDocument document = new XWPFDocument(fis);

        String date = "01.02.2005";
        String invoices = "2";
        String hours = "3";
        String amount = "5";
        String cost = "1000";
        String[] dataList = {
                "fieldData",
                "fieldInvoices",
                "fieldHour",
                "fieldQuantity",
                "fieldCost"
        };
        // Проходим по всем абзацам
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(0);
                for(String data : dataList){
                    if(text!=null && text.contains(data)){

                    }
                }
                if (text != null && text.contains("старый текст")) {
                    text = text.replace("старый текст", "новый текст");
                    run.setText(text, 0); // заменяем текст
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
