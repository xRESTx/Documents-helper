package org.example.convert;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class WordToPdf {
    public static final int wdFormatPDF = 17;

    public static void main(String[] args) {
        String dllPath = new java.io.File("src/jacob-1.14.3-x64.dll").getAbsolutePath();
        System.setProperty("jacob.dll.path", dllPath);
        com.jacob.com.LibraryLoader.loadJacobLibrary();

        WordToPdf wordToPdf = new WordToPdf();
        wordToPdf.convert(
                "C:\\Users\\REST\\Downloads\\Оценка_качества_бинарной_классификации.docx",
                "C:\\Users\\REST\\Downloads\\",
                "converted.pdf"
        );
    }

//    public void convert(String inputPath, String outputPath, String filename) {
//        ActiveXComponent wordApp = new ActiveXComponent("Word.Application");
//        try {
//            wordApp.setProperty("Visible", new Variant(false));
//            Dispatch documents = wordApp.getProperty("Documents").toDispatch();
//
//            String input = inputPath; //"C:\\path\\to\\your\\file.doc"
//            String output = outputPath + filename; //"C:\\path\\to\\your\\file.pdf";
//
//            Dispatch document = Dispatch.call(documents, "Open", input, false, true).toDispatch();
//            Dispatch.call(document, "SaveAs", output, wdFormatPDF);
//            Dispatch.call(document, "Close", false);
//
//            System.out.println("Conversion completed: " + output);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            wordApp.invoke("Quit", 0);
//        }
//    }

    public void convert(String inputPath, String outputPath, String filename) {
        ActiveXComponent wordApp = new ActiveXComponent("Word.Application");
        Dispatch document = null;

        try {
            wordApp.setProperty("Visible", new Variant(false));
            Dispatch documents = wordApp.getProperty("Documents").toDispatch();

            String outputFile = outputPath + filename;

            // MUST use Variant args for Dispatch.call to map correctly to COM method
            document = Dispatch.call(documents, "Open",
                    new Variant(inputPath),
                    new Variant(false), // ConfirmConversions
                    new Variant(true)   // ReadOnly
            ).toDispatch();

            Dispatch.call(document, "SaveAs",
                    new Variant(outputFile),
                    new Variant(wdFormatPDF)
            );

            Dispatch.call(document, "Close", new Variant(false));

            System.out.println("Conversion completed: " + outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                wordApp.invoke("Quit", new Variant[] { new Variant(0) });
            } catch (Exception ex) {
                System.err.println("Error quitting Word: " + ex.getMessage());
            }
        }
    }
}
