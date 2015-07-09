package pe.tgestiona.logistica.inversa.dao.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import pe.tgestiona.logistica.inversa.bean.ConstantesGenerales;
import pe.tgestiona.logistica.inversa.bean.PlaneamientoBean;
import pe.tgestiona.logistica.inversa.dao.PlaneamientoDao;

@Repository
public class PlaneamientoDaoImpl implements PlaneamientoDao{

	@Autowired
	private DriverManagerDataSource dataSource;
	
	@Override
	public byte[] descargarFuentePlaneamiento(String acta) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();

		int i=0;
		
		HSSFRow row = sheet.createRow((short)0);
	    
		HSSFCell celda1=row.createCell(0);
		HSSFCell celda2=row.createCell(1);
		HSSFCell celda3=row.createCell(2);
		HSSFCell celda4=row.createCell(3);
		HSSFCell celda5=row.createCell(4);
		HSSFCell celda6=row.createCell(5);
		HSSFCell celda7=row.createCell(6);
		
		celda1.setCellValue(new HSSFRichTextString("N° Acta"));
		celda2.setCellValue(new HSSFRichTextString("N° Ticket"));
		celda3.setCellValue(new HSSFRichTextString("Serie"));
		celda4.setCellValue(new HSSFRichTextString("ID"));
		celda5.setCellValue(new HSSFRichTextString("FECHA PROG. DEVOL. LURIN"));
		celda6.setCellValue(new HSSFRichTextString("FECHA REPROGRAMACION"));
		celda7.setCellValue(new HSSFRichTextString("OBSERVACION"));

		HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.DARK_BLUE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFFont hSSFFont = workbook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 10);
        hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        hSSFFont.setColor(HSSFColor.WHITE.index);
        
        cellStyle.setFont(hSSFFont);
        
        celda1.setCellStyle(cellStyle);
        celda2.setCellStyle(cellStyle);
        celda3.setCellStyle(cellStyle);
        celda4.setCellStyle(cellStyle);
        celda5.setCellStyle(cellStyle);
        celda6.setCellStyle(cellStyle);
        celda7.setCellStyle(cellStyle);
		
		String sql="SELECT DAD.NROACTA,DAD.NROTICKET,DAD.NROSERIE,DAD.ID FROM DETALLEACTADEVOLUCION DAD WHERE DAD.NROACTA IN (" + acta + ") ORDER BY NROACTA,NROTICKET,ID,NROSERIE DESC";
		System.out.println(sql);
		
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				i++;
				HSSFRow row1 = sheet.createRow((short)i);
				
				row1.createCell((short)0).setCellValue(rs.getString(1));
		        row1.createCell((short)1).setCellValue(rs.getString(2));
		        row1.createCell((short)2).setCellValue(rs.getString(3));
		        row1.createCell((short)3).setCellValue(rs.getString(4));
		        row1.createCell((short)4).setCellValue("");
		        row1.createCell((short)5).setCellValue("");
		        row1.createCell((short)6).setCellValue("");
		        
			}
			
			for(int k=0;k<7;k++){
				sheet.autoSizeColumn(k);
			}
		} catch (SQLException ex) {
			
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
			}
		}
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
	    try {
	    	workbook.write(outByteStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    byte [] outArray = outByteStream.toByteArray();
		return outArray;
	}

	@Override
	public void grabarCargaPlaneamiento(List<PlaneamientoBean> lstPlaneamiento) {
		String sql = "";
		int ctos = 0;
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			for (int i = 0; i < lstPlaneamiento.size(); i++) {

				sql="UPDATE PLANEAMIENTO SET " + 
					"FPROGRAMACIONDEVOLUCIONLURIN='" + lstPlaneamiento.get(i).getfProgDevolLurin() + "'," + 
					"FREPROGRAMACION='" + lstPlaneamiento.get(i).getfRProg() + "'," + 
					"OBSERVACION='" + lstPlaneamiento.get(i).getObservacion() + "' " + 
					"WHERE " + 
					"NROACTA='" + lstPlaneamiento.get(i).getNroActa() + "' AND " + 
					"NROTICKET='" + lstPlaneamiento.get(i).getNroTicket() + "' AND " + 
					"NROSERIE='" + lstPlaneamiento.get(i).getNroSerie() + "' AND " + 
					"ID=" + (i+1); 	
				
				
				System.out.println(sql);
				ctos = st.executeUpdate(sql);
				if (ctos == 0) {
					System.out.println("No afecto a ninguna fila");
				}
			}

		} catch (SQLException ex) {
			ex.getMessage();
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				e.getMessage();
			}
		}
	}

	@Override
	public List<PlaneamientoBean> leerarchivoExcel(InputStream file) {
		List<PlaneamientoBean> listFormato = null;
		try {
			Workbook workbook = Workbook.getWorkbook(file); //Pasamos el excel que vamos a leer
			Sheet sheet = workbook.getSheet(0); //Seleccionamos la hoja que vamos a leer
			String dato;
			int cantColumnas=sheet.getColumns();
			listFormato = new ArrayList<PlaneamientoBean>();
			
			if(cantColumnas==ConstantesGenerales.CANTIDAD_COLUMNAS_PLANEAMIENTO){
				for (int fila = 1; fila < sheet.getRows(); fila++) { //recorremos las filas
					PlaneamientoBean planeamientoBean=new PlaneamientoBean();

					for (int columna = 0; columna < sheet.getColumns(); columna++) { //recorremos las columnas
						
						dato = sheet.getCell(columna, fila).getContents(); //setear la celda leida a nombre
						planeamientoBean.setCantColumnasPlaneamiento(sheet.getColumns());					
						switch(columna){
						    case 0: planeamientoBean.setNroActa(dato);
						    		break;
							case 1: planeamientoBean.setNroTicket(dato);
									break;								
							case 2: planeamientoBean.setNroSerie(dato);
									break;
							case 3: planeamientoBean.setNro(Integer.parseInt(dato));
									break;		
							case 4: if(dato.trim().equals("")){
										planeamientoBean.setfProgDevolLurin(ConstantesGenerales.GUION);
									}else{
										planeamientoBean.setfProgDevolLurin(dato);
									}
									break;
							case 5: if(dato.trim().equals("")){
										planeamientoBean.setfRProg(ConstantesGenerales.GUION);
									}else{
										planeamientoBean.setfRProg(dato);
									}
									break;
							case 6: if(dato.trim().equals("")){
										planeamientoBean.setObservacion(ConstantesGenerales.GUION);
									}else{
										planeamientoBean.setObservacion(dato);
									}
									break;	
						}
						
					}
					listFormato.add(planeamientoBean);
				}
			}else{
				PlaneamientoBean planeamientoBean=new PlaneamientoBean();
				planeamientoBean.setCantColumnasPlaneamiento(sheet.getColumns());
				listFormato.add(planeamientoBean);
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		}

		return listFormato;
	}

}
