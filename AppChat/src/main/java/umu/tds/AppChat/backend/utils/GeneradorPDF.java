package umu.tds.AppChat.backend.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GeneradorPDF {
    
    public static String generarPDF(String nombreArchivo, List<String> mensajes) {
        try {
            // Obtener la ruta del directorio de usuario
            String userHome = System.getProperty("user.home");
            String filePath = userHome + File.separator + nombreArchivo + ".pdf";

            // Crear el documento PDF
            Document documento = new Document();
            PdfWriter.getInstance(documento, new FileOutputStream(filePath));
            documento.open();

            // Añadir título
            documento.add(new Paragraph("Historial de Chat", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK)));
            documento.add(new Paragraph("\n"));

            // Añadir mensajes al documento
            for (String mensaje : mensajes) {
                documento.add(new Paragraph(mensaje));
            }

            documento.close();
            return filePath; // Retorna la ruta del archivo generado
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
