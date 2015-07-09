package pe.tgestiona.logistica.inversa.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfWriter;

public class TestItext {

	public static void main(String[] args) throws DocumentException, MalformedURLException, IOException {
//		Document documento=new Document();
		Document documento=new Document(PageSize.A4_LANDSCAPE,50,50,50,50);
		   try {
		        PdfWriter.getInstance(documento,new FileOutputStream("D:/CreateChapter.pdf"));          
		        documento.open();            
		        addChapter(documento);               
		        documento.close();
		    } catch (FileNotFoundException | DocumentException e) {
		        e.printStackTrace();
		    }  


	}


    private static void addChapter(Document document) 
            throws DocumentException, MalformedURLException, IOException {

    Image logo=Image.getInstance("D:/logotgestiona.png");	
    logo.scalePercent(10);
    Anchor anchor = new Anchor("First Chapter");
    Chunk ck=new Chunk(logo,230,0);
    
    anchor.setName("First Chapter");
    Chapter chapter = new Chapter(new Paragraph(anchor), 1);        
    Paragraph paragraph = new Paragraph("Sub-Heading");
    Section section = chapter.addSection(paragraph);

    paragraph = new Paragraph("Paragraph in center");
    paragraph.setAlignment(Element.ALIGN_CENTER);
    section.add(paragraph);     

    section.add(new Paragraph("Paragraph no alignment"));
    section.add(new Paragraph(" "));        
    List list = new List(false, true, 15);
    list.add(new ListItem("List points lettered"));
    list.add(new ListItem("List points lettered"));
    list.add(new ListItem("List points lettered"));
    section.add(list);      

    list = new List(true, false, 15);
    list.add(new ListItem("List points numbered"));
    list.add(new ListItem("List points numbered"));
    list.add(new ListItem("List points numbered"));
    section.add(new Paragraph(" "));
    section.add(list);
    section.add(new Paragraph(" "));
    paragraph = new Paragraph("Sub-Heading");
    section = chapter.addSection(paragraph);        
    section.add(new Paragraph("Paragraph not align"));
    section.add(new Paragraph(" "));        
    list = new List(true, false, 15);
    list.add(new ListItem("List points numbered"));
    list.add(new ListItem("List points numbered"));
    list.add(new ListItem("List points numbered"));

    section.add(list);

    document.add(chapter);      

    }
	
}
