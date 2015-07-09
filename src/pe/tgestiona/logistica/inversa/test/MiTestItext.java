package pe.tgestiona.logistica.inversa.test;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class MiTestItext {

	private static String strNombreDelPDF = "MiPrimerPDF.pdf"; 
	private static Font fuente01 = new Font(FontFamily.TIMES_ROMAN,14,Font.BOLD);
    
	public static void main(String[] args) {
		Document document = new Document(); 
        try {
			PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(strNombreDelPDF));
/*			PdfContentByte cb = writer.getDirectContent(); 
			
			//Dibujar el cuadro color ROJO             cb.setColorFill(Color.RED); 
            cb.rectangle(100, 500, 150, 150); 
            cb.fillStroke();             
  
            //Dibujar los dos circulos encima del cuadro, uno blanco y otro negro dentro del primero             cb.setColorFill(Color.WHITE);  cb.setColorStroke(Color.WHITE); 
            cb.circle(175, 580,60); 
            cb.fillStroke(); 
            cb.circle(155, 560,20); 
            cb.fillStroke(); 
              
            //Dibujar una pequenia casa             cb.setColorStroke(Color.ORANGE);  
            cb.setLineWidth(1); 
            cb.moveTo(350,500); 
            cb.lineTo(350,600); 
            cb.lineTo(400,650); 
            cb.lineTo(450,600); 
            cb.lineTo(450,500); 
            cb.lineTo(420,500); 
            cb.lineTo(420,530); 
            cb.lineTo(380,530); 
            cb.lineTo(380,500); 
            cb.lineTo(350,500);          
  
            cb.moveTo(400,650); 
            cb.lineTo(500,700); 
            cb.lineTo(550,650); 
  
            cb.moveTo(450,600); 
            cb.lineTo(550,650); 
            cb.lineTo(550,550); 
            cb.lineTo(450,500);          
  
            cb.stroke();        */     
  
            agregarMetaDatos(document); 
            agregarContenido(document); 
  
            document.close(); 
      
            System.out.println("Se ha generado el PDF: "+ strNombreDelPDF); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        // that listens to the document                     document, 
                // and directs a PDF-stream to a file                     new FileOutputStream(strNombreDelPDF)); 
        document.open(); 

	}
	
	private static void agregarMetaDatos(Document document)  
    { 
        document.addTitle("Mi primer PDF"); 
        document.addSubject("Usando iText"); 
        document.addKeywords("Java, PDF, iText"); 
        document.addAuthor("Gonzalo Silverio"); 
        document.addCreator("Gonzalo Silverio"); 
    } 
  
    private static void agregarContenido(Document document) throws DocumentException 
    { 
        Paragraph ParrafoHoja = new Paragraph(); 
  
        // Agregamos una linea en blanco         agregarLineasEnBlanco(ParrafoHoja, 1); 
  
        // Colocar un encabezado         ParrafoHoja.add(new Paragraph("Instituto Tecnologico de Zacatepec", fuenteAzul25) ); 
  
        agregarLineasEnBlanco(ParrafoHoja, 1); 
        ParrafoHoja=new Paragraph("Practica realizada por: Gonzalo Silverio para una demostracion de la libreria iText.", fuente01); 
        agregarLineasEnBlanco(ParrafoHoja, 2); 
        ParrafoHoja=new Paragraph("A continuacion se muestran algunas figuras que se pueden crear con iText! ", fuente01); 
        agregarLineasEnBlanco(ParrafoHoja, 14); 
        ParrafoHoja=new Paragraph("EL OJO SAGRADO ", fuente01);      
        agregarLineasEnBlanco(ParrafoHoja, 1); 
  
        try
        { 
//            Image im = Image.getInstance("guerrero.gif"); 
  //          im.setAlignment(Image.ALIGN_RIGHT | Image.TEXTWRAP ); 
    //      ParrafoHoja.add(im); 
        } 
        catch(Exception e) 
        { 
            System.out.print(e.getMessage()); 
        } 
        agregarLineasEnBlanco(ParrafoHoja, 3); 
        ParrafoHoja=new Paragraph("La creacion de tablas con iText tambien es posible y de una manera muy facil, a continuacion un ejemplo:",   fuente01); 
  
        // Creamos la tabla         crearTabla(ParrafoHoja); 
        document.add(ParrafoHoja); 
    } 
  
    private static void crearTabla(Paragraph subCatPart) throws BadElementException  
    { 
        PdfPTable tabla = new PdfPTable(2);     //Numero de columnas 
        tabla.setWidthPercentage(70); // Porcentaje de la pagina que ocupa         tabla.setHorizontalAlignment(Element.ALIGN_CENTER);//Alineacion horizontal 
        PdfPCell cell = new PdfPCell(new Paragraph("Esta columna esta expandida en 2")); 
        cell.setColspan(2); 
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); 
        tabla.addCell(cell); 
          
        tabla.addCell("Nombre"); 
        tabla.addCell("Calificacion (%)");       
  
        tabla.addCell("Xavier"); 
        tabla.addCell("90"); 
        tabla.addCell("Martita"); 
        tabla.addCell("60"); 
        tabla.addCell("Andreita"); 
        tabla.addCell("90"); 
        tabla.addCell("Gonzalo Silverio"); 
        tabla.addCell("100 (wao)"); 
  
        subCatPart.add(tabla); 
    } 
  
    private static void agregarLineasEnBlanco(Paragraph parrafo, int nLineas)  
    { 
        for (int i = 0; i < nLineas; i++)  
            parrafo.add(new Paragraph(" ")); 
    } 

}
