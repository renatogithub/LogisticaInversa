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
import pe.tgestiona.logistica.inversa.bean.DistribucionBean;
import pe.tgestiona.logistica.inversa.dao.DistribucionDao;

@Repository
public class DistribucionDaoImpl implements DistribucionDao{

	@Autowired
	private DriverManagerDataSource dataSource;

	
	@Override
	public byte[] descargarFuenteDistribucion(String acta) {
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
		HSSFCell celda8=row.createCell(7);
		HSSFCell celda9=row.createCell(8);
		HSSFCell celda10=row.createCell(9);
		HSSFCell celda11=row.createCell(10);
		HSSFCell celda12=row.createCell(11);
		HSSFCell celda13=row.createCell(12);
		HSSFCell celda14=row.createCell(13);
		HSSFCell celda15=row.createCell(14);
		HSSFCell celda16=row.createCell(15);
		HSSFCell celda17=row.createCell(16);
		HSSFCell celda18=row.createCell(17);
		HSSFCell celda19=row.createCell(18);
		HSSFCell celda20=row.createCell(19);
		HSSFCell celda21=row.createCell(20);
		HSSFCell celda22=row.createCell(21);
		
		celda1.setCellValue(new HSSFRichTextString("N° Acta"));
		celda2.setCellValue(new HSSFRichTextString("N° Ticket"));
		celda3.setCellValue(new HSSFRichTextString("Serie"));
		celda4.setCellValue(new HSSFRichTextString("ID"));
		celda5.setCellValue(new HSSFRichTextString("FECHA SOLICITUD DE TRANSPORTE"));
		celda6.setCellValue(new HSSFRichTextString("EMPRESA TRANSPORTE"));
		celda7.setCellValue(new HSSFRichTextString("LEAD TIME"));
		celda8.setCellValue(new HSSFRichTextString("STATUS DE RECOJO"));
		celda9.setCellValue(new HSSFRichTextString("N° GUIA DE REMISIÓN"));
		celda10.setCellValue(new HSSFRichTextString("FECHA DE REAL RECOJO (TRANSPORTISTA)"));
		celda11.setCellValue(new HSSFRichTextString("CANTIDAD DE MATERIALES RECOGIDOS"));
		celda12.setCellValue(new HSSFRichTextString("OBSERVACION TRANSPORTISTA (RECOJO)"));
		celda13.setCellValue(new HSSFRichTextString("ACTA DE INCIDENCIA"));
		celda14.setCellValue(new HSSFRichTextString("MOTIVO DE LA INCIDENCIA"));
		celda15.setCellValue(new HSSFRichTextString("CANTIDAD DE LA INCIDENCIA"));
		celda16.setCellValue(new HSSFRichTextString("VALORIZADO DE LA INCIDENCIA"));
		celda17.setCellValue(new HSSFRichTextString("FECHA DE LLEGADA A BASE(LIMA)"));
		celda18.setCellValue(new HSSFRichTextString("FECHA DE SOLICITUD DE PROGRAMACION DEVOLUCION CD"));
		celda19.setCellValue(new HSSFRichTextString("TIPO CARGA"));
		celda20.setCellValue(new HSSFRichTextString("SERVICIO ADICIONAL"));
		celda21.setCellValue(new HSSFRichTextString("MONTO SERVICIO ADICIONAL"));
		celda22.setCellValue(new HSSFRichTextString("AUTORIZADO POR"));

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
        celda8.setCellStyle(cellStyle);
        celda9.setCellStyle(cellStyle);
        celda10.setCellStyle(cellStyle);
        celda11.setCellStyle(cellStyle);
        celda12.setCellStyle(cellStyle);
        celda13.setCellStyle(cellStyle);
        celda14.setCellStyle(cellStyle);
        celda15.setCellStyle(cellStyle);
        celda16.setCellStyle(cellStyle);
        celda17.setCellStyle(cellStyle);
        celda18.setCellStyle(cellStyle);
        celda19.setCellStyle(cellStyle);
        celda20.setCellStyle(cellStyle);
        celda21.setCellStyle(cellStyle);
        celda22.setCellStyle(cellStyle);
		
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
		        row1.createCell((short)7).setCellValue("");
		        row1.createCell((short)8).setCellValue("");
		        row1.createCell((short)9).setCellValue("");
		        row1.createCell((short)10).setCellValue("");
		        row1.createCell((short)11).setCellValue("");
		        row1.createCell((short)12).setCellValue("");
		        row1.createCell((short)13).setCellValue("");
		        row1.createCell((short)14).setCellValue("");
		        row1.createCell((short)15).setCellValue("");
		        row1.createCell((short)16).setCellValue("");
		        row1.createCell((short)17).setCellValue("");
		        row1.createCell((short)18).setCellValue("");
		        row1.createCell((short)19).setCellValue("");
		        row1.createCell((short)20).setCellValue("");		        
		        row1.createCell((short)21).setCellValue("");		        
		        
			}
			
			for(int k=0;k<22;k++){
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
	public void grabarCargaDistribucion(List<DistribucionBean> lstDistribucion) {
		String sql = "";
		int ctos = 0;
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			for (int i = 0; i < lstDistribucion.size(); i++) {
									
				sql="UPDATE DISTRIBUCION SET " + 
					"FSOLTRANS='" + lstDistribucion.get(i).getfSolTrans() + "'," + 
					"EMPTRANS='" + lstDistribucion.get(i).getEmpTrans() + "'," + 
					"LEADTIME='" + lstDistribucion.get(i).getLeadTime() + "'," + 
					"STATUSRECOJO='" + lstDistribucion.get(i).getStatusRecojo() + "'," + 
					"NROGUIAREMI='" + lstDistribucion.get(i).getNroGuiaRemision() + "'," + 
					"FREALRECOJO='" + lstDistribucion.get(i).getFecRealRecojo() + "'," + 
					"CANTMATERIALRECOJO='" + lstDistribucion.get(i).getCantMaterialRecojo() + "'," + 
					"OBSTRANSPORTISTARECOJO='" + lstDistribucion.get(i).getObsTransportistaRecojo() + "'," + 
					"ACTAINCIDENCIA='" + lstDistribucion.get(i).getActaIncidencia() + "'," + 
					"MOTIVOINCIDENCIA='" + lstDistribucion.get(i).getMotivoIncidencia() + "'," + 
					"CANTDELAINCIDENCIA='" + lstDistribucion.get(i).getCantIncidencia() + "'," + 
					"VALORIZADODELAINCIDENCIA='" + lstDistribucion.get(i).getValorizadoIncidencia() + "'," + 
					"FLLEGADABASE_LIMA='" + lstDistribucion.get(i).getfLlegadaBaseLima() + "'," + 
					"FSOLPROGDEVOLUCION_CD='" + lstDistribucion.get(i).getfSolProgDevolucion_CD() + "'," + 
					"TIPOCARGA='" + lstDistribucion.get(i).getTipoCarga() + "'," + 
					"SERVICIOADICIONAL='" + lstDistribucion.get(i).getServicioAdicional() + "'," + 
					"MONTOSERVADICIONAL='" + lstDistribucion.get(i).getMontoServAdicional() + "'," + 
					"AUTORIZADOPOR='" + lstDistribucion.get(i).getAutorizadoPor() + "' " + 
					"WHERE " + 
					"NROACTA='" + lstDistribucion.get(i).getNroActa() + "' AND " + 
					"NROTICKET='" + lstDistribucion.get(i).getNroTicket() + "' AND " + 
					"NROSERIE='" + lstDistribucion.get(i).getNroSerie() + "' AND " + 
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
	public List<DistribucionBean> leerarchivoExcel(InputStream file) {
		List<DistribucionBean> listFormato = null;
		try {
			Workbook workbook = Workbook.getWorkbook(file); //Pasamos el excel que vamos a leer
			Sheet sheet = workbook.getSheet(0); //Seleccionamos la hoja que vamos a leer
			String dato;
			int cantColumnas=sheet.getColumns();
			listFormato = new ArrayList<DistribucionBean>();
			
			if(cantColumnas==ConstantesGenerales.CANTIDAD_COLUMNAS_DISTRIBUCION){
				for (int fila = 1; fila < sheet.getRows(); fila++) { //recorremos las filas
					DistribucionBean distribucionBean=new DistribucionBean();

					for (int columna = 0; columna < sheet.getColumns(); columna++) { //recorremos las columnas
						
						dato = sheet.getCell(columna, fila).getContents(); //setear la celda leida a nombre
						distribucionBean.setCantColumnasDistribucion(sheet.getColumns());					
						switch(columna){
						    case 0: distribucionBean.setNroActa(dato);
						    		break;
							case 1: distribucionBean.setNroTicket(dato);
									break;								
							case 2: distribucionBean.setNroSerie(dato);
									break;
							case 3: distribucionBean.setNro(Integer.parseInt(dato));
									break;		
							case 4: if(dato.trim().equals("")){
										distribucionBean.setfSolTrans(ConstantesGenerales.GUION);
									}else{
										distribucionBean.setfSolTrans(dato);
									}
									break;
							case 5: if(dato.trim().equals("")){
										distribucionBean.setEmpTrans(ConstantesGenerales.GUION);
									}else{
										distribucionBean.setEmpTrans(dato);
									}
									break;
							case 6: if(dato.trim().equals("")){
										distribucionBean.setLeadTime(ConstantesGenerales.GUION);
									}else{
										distribucionBean.setLeadTime(dato);
									}
									break;
							case 7: if(dato.trim().equals("")){
										distribucionBean.setStatusRecojo(ConstantesGenerales.GUION);
									}else{
										distribucionBean.setStatusRecojo(dato);
									}
									break;
							case 8: if(dato.trim().equals("")){
										distribucionBean.setNroGuiaRemision(ConstantesGenerales.GUION);
									}else{
										distribucionBean.setNroGuiaRemision(dato);
									}
									break;								
							case 9:if(dato.trim().equals("")){
										distribucionBean.setFecRealRecojo(ConstantesGenerales.GUION);
									}else{
										distribucionBean.setFecRealRecojo(dato);
									}
									break;
							case 10:if(dato.trim().equals("")){
										distribucionBean.setCantMaterialRecojo(ConstantesGenerales.GUION);
									}else{
										distribucionBean.setCantMaterialRecojo(dato);
									}
									break;
							case 11:if(dato.trim().equals("")){
										distribucionBean.setObsTransportistaRecojo(ConstantesGenerales.GUION);	
									}else{
										distribucionBean.setObsTransportistaRecojo(dato);
									}
									break;
							case 12:if(dato.trim().equals("")){
										distribucionBean.setActaIncidencia(ConstantesGenerales.GUION);	
									}else{
										distribucionBean.setActaIncidencia(dato);
									}
									break;								
							case 13:if(dato.trim().equals("")){
										distribucionBean.setMotivoIncidencia(ConstantesGenerales.GUION);	
									}else{
										distribucionBean.setMotivoIncidencia(dato);
									}
									break;										
							case 14:if(dato.trim().equals("")){
										distribucionBean.setCantIncidencia(ConstantesGenerales.GUION);	
									}else{
										distribucionBean.setCantIncidencia(dato);
									}
									break;	
							case 15:if(dato.trim().equals("")){
										distribucionBean.setValorizadoIncidencia(ConstantesGenerales.GUION);	
									}else{
										distribucionBean.setValorizadoIncidencia(dato);
									}
									break;
							case 16:if(dato.trim().equals("")){
										distribucionBean.setfLlegadaBaseLima(ConstantesGenerales.GUION);	
									}else{
										distribucionBean.setfLlegadaBaseLima(dato);
									}
									break;
							case 17:if(dato.trim().equals("")){
										distribucionBean.setfSolProgDevolucion_CD(ConstantesGenerales.GUION);	
									}else{
										distribucionBean.setfSolProgDevolucion_CD(dato);
									}
									break;
							case 18:if(dato.trim().equals("")){
										distribucionBean.setTipoCarga(ConstantesGenerales.GUION);	
									}else{
										distribucionBean.setTipoCarga(dato);
									}
									break;
							case 19:if(dato.trim().equals("")){
										distribucionBean.setServicioAdicional(ConstantesGenerales.GUION);	
									}else{
										distribucionBean.setServicioAdicional(dato);
									}
									break;
							case 20:if(dato.trim().equals("")){
										distribucionBean.setMontoServAdicional(ConstantesGenerales.GUION);	
									}else{
										distribucionBean.setMontoServAdicional(dato);
									}
									break;		
							case 21:if(dato.trim().equals("")){
										distribucionBean.setAutorizadoPor(ConstantesGenerales.GUION);	
									}else{
										distribucionBean.setAutorizadoPor(dato);
									}
									break;							
	
						}
						
					}
					listFormato.add(distribucionBean);
				}
			}else{
				DistribucionBean distribucionBean=new DistribucionBean();
				distribucionBean.setCantColumnasDistribucion(sheet.getColumns());
				listFormato.add(distribucionBean);
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
