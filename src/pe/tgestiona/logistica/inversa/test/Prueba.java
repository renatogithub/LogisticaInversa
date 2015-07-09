package pe.tgestiona.logistica.inversa.test;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.springframework.web.jsf.FacesContextUtils;

import jxl.write.Font;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class Prueba {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Document document = new Document(); 
		try {
			Document myDocument=new Document(PageSize.A4,50,50,50,50);
			PdfWriter writer=PdfWriter.getInstance(myDocument,new FileOutputStream("D:\\miPrimerPDF.pdf"));
			myDocument.open();
			Paragraph paragraph = new Paragraph("Texto del Parrafo",FontFactory.getFont(FontFactory.COURIER_BOLD));
			
			Chunk c=new Chunk();
			document.add(paragraph);
			//myDocument.close();
		} catch (FileNotFoundException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
