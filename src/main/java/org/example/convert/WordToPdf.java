package org.example.convert;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WordToPdf {

    public void convert(String inputFile, String outputFile) {
        try {
            // Формируем PowerShell скрипт как строку
            String psScript =
                    "$word = New-Object -ComObject Word.Application; " +
                            "$word.Visible = $false; " +
                            "$doc = $word.Documents.Open('" + inputFile.replace("\\", "\\\\") + "'); " +
                            "$pdfPath = '" + outputFile.replace("\\", "\\\\") + "'; " +
                            // Сохраняем как PDF (wdFormatPDF = 17)
                            "$doc.SaveAs([ref] $pdfPath, [ref] 17); " +
                            "$doc.Close(); " +
                            "$word.Quit();";

            // Команда запуска powershell
            String command = "powershell.exe -NoProfile -Command " + psScript;

            // Запуск процесса
            Process powerShellProcess = Runtime.getRuntime().exec(command);

            // Считываем вывод скрипта (для отладки)
            try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(powerShellProcess.getInputStream()));
                 BufferedReader stdError = new BufferedReader(new InputStreamReader(powerShellProcess.getErrorStream()))) {

                String s;
                while ((s = stdInput.readLine()) != null) {
                    System.out.println(s);
                }
                while ((s = stdError.readLine()) != null) {
                    System.err.println(s);
                }
            }

            int exitCode = powerShellProcess.waitFor();
            if (exitCode == 0) {
                System.out.println("Conversion completed: " + outputFile);
            } else {
                System.err.println("PowerShell script exited with code " + exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
