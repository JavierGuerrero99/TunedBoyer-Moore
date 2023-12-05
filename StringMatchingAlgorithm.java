import java.io.*;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDFont;

public class StringMatchingAlgorithm {
    public static void main(String[] args) {
        // Ejemplo de uso
        String archivoTexto = "ruta/al/archivo.txt";
        String patron = "patrón";

        try {
            int cantidad = buscarPatronEnArchivo(archivoTexto, patron);
            System.out.println("El patrón aparece " + cantidad + " veces en el archivo.");

            crearPDFConPatrones(archivoTexto, patron);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int buscarPatronEnArchivo(String archivo, String patron) throws IOException {
        // Abrir el archivo de texto
        BufferedReader reader = new BufferedReader(new FileReader(archivo));

        int cantidad = 0;
        int longitudPatron = patron.length();
        int longitudLinea;
        char[] buffer = new char[4096]; // Buffer para almacenar los caracteres leídos

        String linea;
        while ((linea = reader.readLine()) != null) {
            longitudLinea = linea.length();
            int i = 0;

            while (i <= longitudLinea - longitudPatron) {
                int j = 0;

                while (j < longitudPatron && patron.charAt(j) == linea.charAt(i + j)) {
                    j++;
                }

                if (j == longitudPatron) {
                    cantidad++; // Se encontró el patrón en esta posición
                    i += longitudPatron; // Salta la longitud del patrón para continuar la búsqueda
                } else {
                    i++; // Avanza un carácter en la línea
                }
            }
        }

        reader.close();
        return cantidad;
    }

    public static void crearPDFConPatrones(String archivo, String patron) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(archivo));

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        PDFont font = PDType1Font.HELVETICA_BOLD;

        String linea;
        float y = 700;

        while ((linea = reader.readLine()) != null) {
            int longitudLinea = linea.length();
            int longitudPatron = patron.length();
            int i = 0;

            while (i <= longitudLinea - longitudPatron) {
                int j = 0;

                while (j < longitudPatron && patron.charAt(j) == linea.charAt(i + j)) {
                    j++;
                }

                if (j == longitudPatron) {
                    contentStream.beginText();
                    contentStream.setFont(font, 12);
                    contentStream.newLineAtOffset(100, y);
                    contentStream.showText(linea.substring(i, i + longitudPatron));
                    contentStream.endText();
                }

                i++;
            }

            y -= 20; // Ajusta la posición para la siguiente línea en el PDF
        }

        contentStream.close();
        document.save("ruta/del/nuevoArchivo.pdf");
        document.close();
        reader.close();
    }
}