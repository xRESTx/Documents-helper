package org.example.convert;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class ExcelToPdf {

    public static void convert(String excelPath, String pdfPath)
            throws IOException, InterruptedException {

        if (pdfPath == null || pdfPath.trim().isEmpty()) {
            pdfPath = transliterate(excelPath.replaceFirst("\\.[^.]+$", "")) + ".pdf";
        }

        Path ps = Files.createTempFile("excel2pdf_", ".ps1");

        // пишем скрипт в UTF-8 с BOM
        try (BufferedWriter w = Files.newBufferedWriter(ps, StandardCharsets.UTF_8)) {
            w.write('\uFEFF');        // BOM
            w.write("$ErrorActionPreference = 'Stop'\n");
            w.write("$baseName = [IO.Path]::GetFileNameWithoutExtension('" + escape(excelPath) + "')\n");
            w.write("$baseName = $baseName -replace '[^A-Za-z0-9._-]', '_'\n");
            w.write("$pdfPath  = Join-Path (Split-Path '" + escape(excelPath) + "') ($baseName + '.pdf')\n");
            w.write("$excel = New-Object -ComObject Excel.Application\n");
            w.write("$excel.Visible = $false\n");
            w.write("$excel.DisplayAlerts = $false\n");
            w.write("try {\n");
            w.write("  $wb = $excel.Workbooks.Open('" + escape(excelPath) + "')\n");
            w.write("  $ws = $wb.ActiveSheet\n");
            w.write("  $used = $ws.UsedRange\n");
            w.write("  $used.EntireRow.AutoFit()\n");
            w.write("  $used.WrapText = $true\n");
            w.write("  $ws.PageSetup.PrintArea = $used.Address()\n");
            w.write("  $ws.ResetAllPageBreaks()\n");
            w.write("  $ws.PageSetup.Zoom = $false\n");
            w.write("  $ws.PageSetup.FitToPagesWide = 1\n");
            w.write("  $ws.PageSetup.FitToPagesTall = 1\n");
            w.write("  $wb.ExportAsFixedFormat([Microsoft.Office.Interop.Excel.XlFixedFormatType]::xlTypePDF, $pdfPath)\n");
            w.write("} finally {\n");
            w.write("  if ($wb)   { $wb.Close($false) }\n");
            w.write("  if ($excel) { $excel.Quit() }\n");
            w.write("}\n");
        }

        ProcessBuilder pb = new ProcessBuilder(
                "powershell.exe",
                "-NoProfile",
                "-ExecutionPolicy", "Bypass",
                "-File", ps.toString());
        pb.redirectErrorStream(true);

        Process proc = pb.start();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(proc.getInputStream()))) {
            br.lines().forEach(System.out::println);
        }

        int exit = proc.waitFor();
        Files.deleteIfExists(ps);

        if (exit != 0) {
            throw new IOException("PowerShell вернул код " + exit);
        }
    }

    private static String escape(String s) {
        return s.replace("'", "''");
    }

    private static String transliterate(String src) {
        String[] rus = {"а","б","в","г","д","е","ё","ж","з","и","й","к","л","м","н","о","п","р","с","т","у","ф","х","ц","ч","ш","щ","ъ","ы","ь","э","ю","я",
                "А","Б","В","Г","Д","Е","Ё","Ж","З","И","Й","К","Л","М","Н","О","П","Р","С","Т","У","Ф","Х","Ц","Ч","Ш","Щ","Ъ","Ы","Ь","Э","Ю","Я"};
        String[] lat = {"a","b","v","g","d","e","yo","zh","z","i","y","k","l","m","n","o","p","r","s","t","u","f","kh","c","ch","sh","shch","","y","","e","yu","ya",
                "A","B","V","G","D","E","Yo","Zh","Z","I","Y","K","L","M","N","O","P","R","S","T","U","F","Kh","C","Ch","Sh","Shch","","Y","","E","Yu","Ya"};
        for (int i = 0; i < rus.length; i++) src = src.replace(rus[i], lat[i]);
        return src.replaceAll("[^A-Za-z0-9._-]", "_");
    }
}