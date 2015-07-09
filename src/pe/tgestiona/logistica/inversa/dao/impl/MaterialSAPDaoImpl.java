package pe.tgestiona.logistica.inversa.dao.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import pe.tgestiona.logistica.inversa.bean.ConstantesGenerales;
import pe.tgestiona.logistica.inversa.bean.ErroresBean;
import pe.tgestiona.logistica.inversa.bean.MaterialBean;
import pe.tgestiona.logistica.inversa.bean.MaterialSAPBean;
import pe.tgestiona.logistica.inversa.dao.MaterialSAPDao;
import pe.tgestiona.logistica.inversa.util.Util;

@Repository
public class MaterialSAPDaoImpl implements MaterialSAPDao{

	@Autowired
	private DriverManagerDataSource dataSource;
	
	@Override
	public void grabarMaterialSAPSeriado(List<MaterialSAPBean> lista,String tipoSAP,String usuario) {
		boolean existe=false;
		boolean existeMaterial=false;
		String sql = "";
		String modificadoEl="";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			cn.setAutoCommit(false);
	        PreparedStatement pstmt = null;

			for (int i = 0; i < lista.size(); i++) {
				if(tipoSAP.equals(ConstantesGenerales.SAP_6_0)){
					if(lista.get(i).getModificadoEl()==null){
						modificadoEl="";
					}else{
						modificadoEl=lista.get(i).getModificadoEl();
					}
					sql = "insert into MaterialSAP(NroSerie,Centro,Almacen,CodMaterial,Denominacion,Lote,TipoStocks,StatusSistema,ModificadoEl,ModificadoPor,CreadoEl,CreadoPor,Tipo,Usuario) values ('"
							+  lista.get(i).getNumeroSerie() + "','" + lista.get(i).getCentro() + "','" + lista.get(i).getAlmacen() + "','" + lista.get(i).getCodMaterial() + "','" 
							+  lista.get(i).getDenominacion() + "','" + lista.get(i).getLote() + "','" + lista.get(i).getTipoStocks() + "','" + lista.get(i).getStatuSistema() + "','"
							+  modificadoEl + "','" + lista.get(i).getModificadoPor() + "','" + lista.get(i).getCreadoEl() +  "','" 
							+  lista.get(i).getCreadoPor() + "','" + tipoSAP + "','" + usuario + "')";					
					
					pstmt = cn.prepareStatement(sql);
		            pstmt.executeUpdate();
		            cn.commit();					
					
				}

				
				if(tipoSAP.equals(ConstantesGenerales.SAP_4_7)){
					existe=existeRubrosPermitidosMaterialSAP47(lista.get(i).getCodMaterial());
					existeMaterial=existeMaterialPermitidosMaterialSAP47("1501543");
					
					if(existe || existeMaterial){
						sql = "insert into MaterialSAP(NroSerie,Centro,Almacen,CodMaterial,Denominacion,Lote,TipoStocks,StatusSistema,ModificadoEl,ModificadoPor,CreadoEl,CreadoPor,Tipo,Usuario) values ('"
								+  lista.get(i).getNumeroSerie() + "','" + lista.get(i).getCentro() + "','" + lista.get(i).getAlmacen() + "','" + lista.get(i).getCodMaterial() + "','" 
								+  lista.get(i).getDenominacion() + "','" + lista.get(i).getLote() + "','" + lista.get(i).getTipoStocks() + "','" + lista.get(i).getStatuSistema() + "','"
								+  lista.get(i).getModificadoEl() + "','" + lista.get(i).getModificadoPor() + "','" + lista.get(i).getCreadoEl() +  "','" 
								+  lista.get(i).getCreadoPor() + "','" + tipoSAP + "','" + usuario + "')";					

						
						pstmt = cn.prepareStatement(sql);
			            pstmt.executeUpdate();
			            cn.commit();					
					}
				}

					
				
					

			}
		} catch (SQLException ex) {
			System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
	}
	
	
	@Override
	public void grabarMaterialSAPNoSeriado(List<MaterialSAPBean> lista,String tipoSAP,String usuario) {
		boolean existe=false;
		String sql = "";
		Connection cn = null;		
		try {
			cn = dataSource.getConnection();
			cn.setAutoCommit(false);
	        PreparedStatement pstmt = null;
			for (int i = 0; i < lista.size(); i++) {
				if(tipoSAP.equals("6.0")){
					sql = "insert into MaterialSAP(CodMaterial,Denominacion,Centro,Almacen,Lote,Libre_Utilizacion,Tipo,Usuario) values ('"
						    +  lista.get(i).getCodMaterial() + "','" + lista.get(i).getDenominacion() + "','" + lista.get(i).getCentro() + "','" + lista.get(i).getAlmacen() + "','" + lista.get(i).getLote() + "'," 
							+  lista.get(i).getLibreUtilizacion() + ",'" + tipoSAP + "','" + usuario + "')";
					pstmt = cn.prepareStatement(sql);
			        pstmt.executeUpdate();
			        cn.commit();
				}
				
				if(tipoSAP.equals("4.7")){
						sql = "insert into MaterialSAP(CodMaterial,Denominacion,Centro,Almacen,Lote,Libre_Utilizacion,Tipo,Usuario) values ('"
							    +  lista.get(i).getCodMaterial() + "','" + lista.get(i).getDenominacion() + "','" + lista.get(i).getCentro() + "','" + lista.get(i).getAlmacen() + "','" + lista.get(i).getLote() + "'," 
								+  lista.get(i).getLibreUtilizacion() + ",'" + tipoSAP + "','" + usuario + "')";
						pstmt = cn.prepareStatement(sql);
				        pstmt.executeUpdate();
				        cn.commit();
				}
				
			}
		} catch (SQLException ex) {
			System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
	}

	@Override
	public List<MaterialSAPBean> leerarchivoSAPSeriado(InputStream file,String tipoExcel) {
		List<MaterialSAPBean> listMaterialSAP = null;
		
		if(tipoExcel.equals(ConstantesGenerales.EXCEL_XLSX)){
			try {
				// Finds the workbook instance for XLSX file
				XSSFWorkbook myWorkBook = new XSSFWorkbook (file);
				// Return first sheet from the XLSX workbook
				XSSFSheet mySheet = myWorkBook.getSheetAt(0);
				// Get iterator to all the rows in current sheet
				XSSFRow fila = mySheet.getRow(0);
				int cantColumnas=fila.getLastCellNum();
				int rownum=1;	
				int cantFilas=mySheet.getLastRowNum();
				
				XSSFCreationHelper createHelper = myWorkBook.getCreationHelper();
				XSSFCellStyle cellStyle         = myWorkBook.createCellStyle();
				cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy")); 

				Format formato = new SimpleDateFormat("dd/MM/yyyy");

				listMaterialSAP = new ArrayList<MaterialSAPBean>();
				
				if(cantColumnas==ConstantesGenerales.CANTIDAD_COLUMNAS_SAP_SERIADO){
					MaterialSAPBean materialSAPBean= null;
					while (rownum<=cantFilas) {
						XSSFRow row = mySheet.getRow(rownum);
						materialSAPBean= new MaterialSAPBean();
						for (int i = 0; i < row.getLastCellNum(); i++) {
							
							XSSFCell cell = row.getCell(i);
							materialSAPBean.setCantColumnaSAP(cantColumnas);
							switch (i) {
								case 0: if(cell==null){
											materialSAPBean.setNumeroSerie("");
										}else{
											materialSAPBean.setNumeroSerie(cell.getStringCellValue());
										}									
										break;
								case 1: if(cell==null){
											materialSAPBean.setCentro("");
										}else{
											materialSAPBean.setCentro(cell.getStringCellValue());
										}										
										break;
								case 2: if(cell==null){
											materialSAPBean.setAlmacen("");
										}else{
											materialSAPBean.setAlmacen(cell.getStringCellValue());
										}										
										break;
								case 3: if(cell==null){
											materialSAPBean.setCodMaterial("");
										}else{
											if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){										
												materialSAPBean.setCodMaterial(String.valueOf(Math.round(cell.getNumericCellValue())));
											}
											if(cell.getCellType()==Cell.CELL_TYPE_STRING){										
												materialSAPBean.setCodMaterial(cell.getStringCellValue());
											}
										}
										break;
								case 4: if(cell==null){
											materialSAPBean.setDenominacion("");
										}else{
											materialSAPBean.setDenominacion(cell.getStringCellValue());											
										}
										break;
								case 5: if(cell==null){
											materialSAPBean.setLote("");
										}else{
											materialSAPBean.setLote(cell.getStringCellValue());											
										}
										break;										
								case 6: if(cell==null){
											materialSAPBean.setTipoStocks("");
										}else{
											materialSAPBean.setTipoStocks(cell.getStringCellValue());											
										}
										break;
								case 7: if(cell==null){
											materialSAPBean.setStatuSistema("");
										}else{
											materialSAPBean.setStatuSistema(cell.getStringCellValue());											
										}
										break;
								case 8: if(cell==null){
											materialSAPBean.setModificadoEl("");
										}else{
											if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
												if (HSSFDateUtil.isCellDateFormatted(cell)) {
						                        	cell.setCellStyle(cellStyle);
													String strFechaModificado = formato.format(cell.getDateCellValue());
													materialSAPBean.setModificadoEl(strFechaModificado);
						                        }
											}
											
											if(cell.getCellType()==Cell.CELL_TYPE_STRING){										
												materialSAPBean.setModificadoEl(cell.getStringCellValue());
											}
										}										
										break;
								case 9: if(cell==null){
											materialSAPBean.setModificadoPor("");
										}else{
											materialSAPBean.setModificadoPor(cell.getStringCellValue());											
										}
										break;
								case 10:if(cell==null){
											materialSAPBean.setCreadoEl("");
										}else{
											if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
												if (HSSFDateUtil.isCellDateFormatted(cell)) {
						                        	cell.setCellStyle(cellStyle);
													String strFechaCreado = formato.format(cell.getDateCellValue());
													materialSAPBean.setCreadoEl(strFechaCreado);
						                        }
											}
											if(cell.getCellType()==Cell.CELL_TYPE_STRING){										
												materialSAPBean.setCreadoEl(cell.getStringCellValue());
											}
										}										
										break;
								case 11:if(cell==null){
											materialSAPBean.setCreadoPor("");
										}else{
											materialSAPBean.setCreadoPor(cell.getStringCellValue());
										} 
										break;
							}					
							
			                    
						}
						listMaterialSAP.add(materialSAPBean);
						rownum++;
					}
				}else{
					MaterialSAPBean materialSAPBean= new MaterialSAPBean();
					materialSAPBean.setCantColumnaSAP(cantColumnas);
					listMaterialSAP.add(materialSAPBean);
				}
				
			} catch (FileNotFoundException e) {
				System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			} catch (IOException e) {
				System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			} 
		}
		
		if(tipoExcel.equals(ConstantesGenerales.EXCEL_XLS)){
			try {
				Workbook workbook = Workbook.getWorkbook(file); //Pasamos el excel que vamos a leer
				
				Sheet sheet = workbook.getSheet(0); //Seleccionamos la hoja que vamos a leer
				SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yy");
				Format formatter1 = new SimpleDateFormat("dd/MM/yyyy");
				

				String nombre;
				int cantColumnas=sheet.getColumns();
				listMaterialSAP = new ArrayList<MaterialSAPBean>();
				if(cantColumnas==ConstantesGenerales.CANTIDAD_COLUMNAS_SAP_SERIADO){
					for (int fila = 1; fila < sheet.getRows(); fila++) { //recorremos las filas
						MaterialSAPBean materialSAPBean= new MaterialSAPBean();
						for (int columna = 0; columna < sheet.getColumns(); columna++) { //recorremos las columnas
							
							nombre = sheet.getCell(columna, fila).getContents(); //setear la celda leida a nombre
							materialSAPBean.setCantColumnaSAP(sheet.getColumns());
							switch(columna){
								case 0: materialSAPBean.setNumeroSerie(nombre);
										break;
								case 1: materialSAPBean.setCentro(nombre);
										break;
								case 2: materialSAPBean.setAlmacen(nombre);
										break;
								case 3: materialSAPBean.setCodMaterial(nombre);
										break;
								case 4: materialSAPBean.setDenominacion(nombre);
										break;
								case 5: materialSAPBean.setLote(nombre);
										break;
								case 6: materialSAPBean.setTipoStocks(nombre);
										break;
								case 7: materialSAPBean.setStatuSistema(nombre);
										break;
								case 8: Date fechaModificado = null;
										fechaModificado = formatoDelTexto.parse(nombre);					
										String strFechaModificado = formatter1.format(fechaModificado);
										materialSAPBean.setModificadoEl(String.valueOf(strFechaModificado));
										break;
								case 9: materialSAPBean.setModificadoPor(nombre);
										break;
								case 10:Date fechaCreado = null;
										fechaCreado = formatoDelTexto.parse(nombre);					
										String strFechaCreado = formatter1.format(fechaCreado);
										materialSAPBean.setCreadoEl(String.valueOf(strFechaCreado));
										break;
								case 11: materialSAPBean.setCreadoPor(nombre);
										break;
							}
						}
						
						listMaterialSAP.add(materialSAPBean);
					}			
				}else{
					MaterialSAPBean materialSAPBean= new MaterialSAPBean();
					materialSAPBean.setCantColumnaSAP(sheet.getColumns());
					listMaterialSAP.add(materialSAPBean);
				}
			} catch (FileNotFoundException e) {
				System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			} catch (IOException e) {
				System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			} catch (BiffException e) {
				System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			} catch (ParseException e) {
				System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		

		return listMaterialSAP;
	}

	@Override
	public void eliminaDatosSapSeriado(String usuario) {
		String sql="DELETE FROM MATERIALSAP WHERE USUARIO='" + usuario + "'";
		Connection cn = null;
		try {
			cn=dataSource.getConnection();
			cn.setAutoCommit(false);
	        PreparedStatement pstmt = null;
	        pstmt = cn.prepareStatement(sql);
            pstmt.executeUpdate();
            cn.commit();	
			
		} catch (SQLException e) {
			System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
		} finally{
			try{
				cn.close();
			}catch(Exception e){
				System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}

		
	}

	@Override
	public List<String> validarCampoCantidad(List<MaterialSAPBean> lista) {		
		List<String> lstCampos=new ArrayList<String>();
		if(lista!=null){
			for(int i=0;i<lista.size();i++){
				if(lista.get(i).getLibreUtilizacion()==0 || lista.get(i).getLibreUtilizacion()==null){
					lstCampos.add("El campo Cantidad se encuentra vacio en la fila: " + (i+1));
				}
			}
		}	
		return lstCampos;
	}

	@Override
	public List<String> validarCampoCaracteresCantidad(List<MaterialSAPBean> lista) {
		List<String> lstCamposCaracteres=new ArrayList<String>();
		boolean valido=true;
		String cantidad;
		int contador=0;
		String caracter;
		String caracteresValidos[]={"1","2","3","4","5","6","7","8","9","0",","};
		if(lista!=null){
			for(int i=0;i<lista.size();i++){
				cantidad=String.valueOf(lista.get(i).getLibreUtilizacion());
				valido=false;
				for(int j=0;j<cantidad.length();j++){
					caracter=cantidad.substring(j, (j+1));
					contador=0;
					for(int c=0;c<caracteresValidos.length;c++){
						if(caracter.equals(caracteresValidos[c])){
							contador=contador+1;
							break;
						}
					}
					
					if(contador==0){
						valido=true;
					}
				}
				
				if(valido){
					lstCamposCaracteres.add("El campo cantidad: " + cantidad + " tiene caracteres erroneos en la Fila: " + (i+1));
				}
				
			}
		}	
		return lstCamposCaracteres;
	}

	@Override
	public List<String> validarCantidadCamposSeriado(List<MaterialSAPBean> lista) {
		List<String> lstCantidadCampos=new ArrayList<String>();
		if(lista.size()==1){
			if(lista.get(0).getCantColumnaSAP()!=ConstantesGenerales.CANTIDAD_COLUMNAS_SAP_SERIADO){
				lstCantidadCampos.add("La cantidad de columnas necesarias para el SAP Seriado son: <strong>" + ConstantesGenerales.CANTIDAD_COLUMNAS_SAP_SERIADO + " </strong> pero ha ingresado <strong>" + lista.get(0).getCantColumnaSAP() + " </strong> columnas");
			}
		}

		return lstCantidadCampos;
	}
	
	
	public List<String> validarCantidadCamposNoSeriado(List<MaterialSAPBean> lista) {
		List<String> lstCantidadCampos=new ArrayList<String>();
		if(lista.size()==1){
			if(lista.get(0).getCantColumnaSAP()!=ConstantesGenerales.CANTIDAD_COLUMNAS_SAP_NOSERIADO){
				lstCantidadCampos.add("La cantidad de columnas necesarias para el SAP Seriado son: <strong>" + ConstantesGenerales.CANTIDAD_COLUMNAS_SAP_NOSERIADO + " </strong> pero ha ingresado <strong>" + lista.get(0).getCantColumnaSAP() + " </strong> columnas");
			}
		}

		return lstCantidadCampos;
	}


	@Override
	public List<String[]> lstMaterial(String material,
			String descripcion, String peso, String volumen, String seriado,
			String tipo, String rubro, String tecnologia, String negocio,
			String prov, String sociedad, String precioEquipo, String lotizable,int pag,int regMostrar,int pagIntervalo) {
		
		List<String[]> lstMaterial = null;
		Util util=new Util();
		int pagInicio=0;
//		int pagActual=0;
		int pagFin=0;		
		
		String sql="SELECT * FROM (SELECT A.*,ROWNUM RNUM FROM (SELECT MAT.CODMATERIAL,MAT.DESCRIPCION,MAT.UMD,MAT.PESO,MAT.VOLUNITARIO,MAT.SERIADO,MAT.PRECIOEQUIPO,MAT.LOTIZABLE,PROV.NOMPROV,NEG.NOMNEGOCIO,SOC.ABRSOCIEDAD,TPMAT.NOMTIPOMATERIAL,RBMAT.NOMBRERUBRO,TCMAT.NOMTECNOLOGIA  " +
		"FROM MATERIAL MAT,PROVISION_PLANTAEXTERNA PROV,NEGOCIO NEG,SOCIEDAD SOC,TIPOMATERIAL TPMAT,RUBRO RBMAT,TECNOLOGIA TCMAT " +
		"WHERE PROV.CODPROV=MAT.CODPROV AND NEG.CODNEGOCIO=MAT.CODNEGOCIO AND SOC.CODSOCIEDAD=MAT.SOCIEDAD AND TPMAT.CODTIPOMATERIAL=MAT.CODTIPOMATERIAL AND RBMAT.CODRUBRO=MAT.CODRUBRO AND TCMAT.CODTECNOLOGIA=MAT.CODTECNOLOGIA ";
		
		
		String sqlGeneral="";
		String sqlCamposCondicion="";
		String sqlCamposCondicionGeneral="";
		String sqlCamposCondicionTotal="";
		String sqlCantidadTotal="";
		
		if(pag>=1){
//			pagActual=pag;
			pagInicio=((pag)-1)*regMostrar;
			pagFin=pag*regMostrar;
		}else{
			pagInicio=0;
			pagFin=regMostrar;
//			pagActual=1;
		}
		
		
		if(material.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " LPAD(UPPER(TRIM(MAT.CODMATERIAL)),20,'0')='" + util.rellenar(material.trim().toUpperCase(), 20)  + "' AND ";
		}
		
		if(descripcion.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " UPPER(TRIM(MAT.DESCRIPCION)) LIKE '%" + descripcion.trim().toUpperCase() + "%' AND ";
		}
		
		if(peso.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " TRIM(MAT.PESO)='" + peso.trim()  + "' AND ";
		}
		
		if(volumen.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " TRIM(MAT.VOLUNITARIO)='" + volumen.trim()  + "' AND ";			
		}
		
		if(seriado.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " TRIM(MAT.SERIADO)='" + seriado.trim()  + "' AND ";			
		}
		
		if(tipo.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " UPPER(TRIM(TPMAT.NOMTIPOMATERIAL)) LIKE '%" + tipo.trim().toUpperCase()   + "%' AND ";
		}
		
		if(rubro.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " UPPER(TRIM(RBMAT.NOMBRERUBRO)) LIKE '%" + rubro.trim().toUpperCase()   + "%' AND ";
		}
		
		if(tecnologia.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " UPPER(TRIM(TCMAT.NOMTECNOLOGIA)) LIKE '%" + tecnologia.trim().toUpperCase()   + "%' AND ";
		}
		
		if(negocio.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " UPPER(TRIM(NEG.NOMNEGOCIO)) LIKE '%" + negocio.trim().toUpperCase()   + "%' AND ";
		}
		
		if(prov.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " UPPER(TRIM(PROV.NOMPROV)) LIKE '%" + prov.trim().toUpperCase()  + "%' AND ";
		}
		
		if(sociedad.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " UPPER(TRIM(SOC.ABRSOCIEDAD)) LIKE '%" + sociedad.trim().toUpperCase()  + "%' AND ";
		}
		
		if(precioEquipo.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " TRIM(MAT.PRECIOEQUIPO)='" + precioEquipo.trim()  + "' AND ";
		}
		
		if(lotizable.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " TRIM(MAT.LOTIZABLE)='" + lotizable.trim()  + "' AND ";
		}
		
		
		if(sqlCamposCondicion.length()>0){
			sqlCamposCondicionGeneral=" AND " + sqlCamposCondicion.substring(0, sqlCamposCondicion.length()-4) + " ) A WHERE ROWNUM<=" + pagFin + ") WHERE RNUM>" + pagInicio;
			
			sqlCamposCondicionTotal=" AND " + sqlCamposCondicion.substring(0, sqlCamposCondicion.length()-4) + " ) A )"; 
		}
		
		Connection cn = null;
		sqlGeneral=sql + sqlCamposCondicionGeneral;
		sqlCantidadTotal=sql + sqlCamposCondicionTotal;
		
		System.out.println("SQL-GENERAL:" + sqlGeneral);
		System.out.println("SQL-CANTIDAD:" + sqlCantidadTotal);
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			Statement stTotal = cn.createStatement();
			ResultSet rs = st.executeQuery(sqlGeneral);
			ResultSet rsTotal=stTotal.executeQuery(sqlCantidadTotal);
			int columnCount = rs.getMetaData().getColumnCount();
			int rowCountTotal=0;
			lstMaterial=new ArrayList<String[]>();
			while(rsTotal.next()){
				rowCountTotal=rowCountTotal+1;
			}
			
			while (rs.next()) {
				String[] row = new String[columnCount+1]; //Le adicione uno para agregarle los totales al final
					for (int i=0; i <columnCount ; i++){
				       row[i] = rs.getString(i + 1);
				    }
					row[columnCount]=String.valueOf(rowCountTotal);
					lstMaterial.add(row);
			}
		} catch (SQLException ex) {
			lstMaterial = null;
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
			}
		}
		
		return lstMaterial;
	}

	@Override
	public List<MaterialSAPBean> leerarchivoSAPNoSeriado(InputStream file,String tipoExcel) {
		
		List<MaterialSAPBean> listMaterialSAP = null;
		
		if(tipoExcel.equals(ConstantesGenerales.EXCEL_XLSX)){
			try {
				// Finds the workbook instance for XLSX file
				XSSFWorkbook myWorkBook = new XSSFWorkbook (file);
				// Return first sheet from the XLSX workbook
				XSSFSheet mySheet = myWorkBook.getSheetAt(0);
				// Get iterator to all the rows in current sheet
				XSSFRow fila = mySheet.getRow(0);
				int cantColumnas=fila.getLastCellNum();
				int rownum=1;	
				int cantFilas=mySheet.getLastRowNum();
				
				listMaterialSAP = new ArrayList<MaterialSAPBean>();
				
				if(cantColumnas==ConstantesGenerales.CANTIDAD_COLUMNAS_SAP_NOSERIADO){
					MaterialSAPBean materialSAPBean= null;
					while (rownum<=cantFilas) {
						XSSFRow row = mySheet.getRow(rownum);
						materialSAPBean= new MaterialSAPBean();
						for (int i = 0; i < row.getLastCellNum(); i++) {
							XSSFCell cell = row.getCell(i);
							materialSAPBean.setCantColumnaSAP(row.getLastCellNum());
							switch (i) {								
								case 0: if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){										
											materialSAPBean.setCodMaterial(String.valueOf(Math.round(cell.getNumericCellValue())));
										}
										if(cell.getCellType()==Cell.CELL_TYPE_STRING){										
											materialSAPBean.setCodMaterial(cell.getStringCellValue());
										}
										break;									
								case 1: materialSAPBean.setDenominacion(cell.getStringCellValue());
										break;
								case 2: materialSAPBean.setCentro(cell.getStringCellValue());
										break;
								case 3: materialSAPBean.setAlmacen(cell.getStringCellValue());
										break;
								case 4: materialSAPBean.setLote(cell.getStringCellValue());
										break;
								case 5: if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){										
				                        	materialSAPBean.setLibreUtilizacion(cell.getNumericCellValue());
										}									
										break;
							}					
							
			                    
						}
						listMaterialSAP.add(materialSAPBean);
						rownum++;
					}
				}else{
					MaterialSAPBean materialSAPBean= new MaterialSAPBean();
					materialSAPBean.setCantColumnaSAP(cantColumnas);
					listMaterialSAP.add(materialSAPBean);
				}
				
			} catch (FileNotFoundException e) {
				System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			} catch (IOException e) {
				System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			} 
		}
		
		if(tipoExcel.equals(ConstantesGenerales.EXCEL_XLS)){
			try {
				Workbook workbook = Workbook.getWorkbook(file); //Pasamos el excel que vamos a leer
				
				Sheet sheet = workbook.getSheet(0); //Seleccionamos la hoja que vamos a leer
				SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yy");
				Format formatter1 = new SimpleDateFormat("dd/MM/yyyy");
				

				String nombre;
				int cantColumnas=sheet.getColumns();
				listMaterialSAP = new ArrayList<MaterialSAPBean>();
				if(cantColumnas==ConstantesGenerales.CANTIDAD_COLUMNAS_SAP_NOSERIADO){
					for (int fila = 1; fila < sheet.getRows(); fila++) { //recorremos las filas
						MaterialSAPBean materialSAPBean= new MaterialSAPBean();
						for (int columna = 0; columna < sheet.getColumns(); columna++) { //recorremos las columnas
							
							nombre = sheet.getCell(columna, fila).getContents(); //setear la celda leida a nombre
							materialSAPBean.setCantColumnaSAP(sheet.getColumns());
							switch(columna){
								case 0: materialSAPBean.setCodMaterial(nombre);
										break;
								case 1: materialSAPBean.setDenominacion(nombre);
										break;
								case 2: materialSAPBean.setCentro(nombre);
										break;
								case 3: materialSAPBean.setAlmacen(nombre);
										break;
								case 4: materialSAPBean.setLote(nombre);
										break;
								case 5: nombre=nombre.replace("," ,".");									
										materialSAPBean.setLibreUtilizacion(Double.parseDouble(nombre));
										break;
							}
						}
						
						listMaterialSAP.add(materialSAPBean);
					}			
				}else{
					MaterialSAPBean materialSAPBean= new MaterialSAPBean();
					materialSAPBean.setCantColumnaSAP(sheet.getColumns());
					listMaterialSAP.add(materialSAPBean);
				}
			} catch (FileNotFoundException e) {
				System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			} catch (IOException e) {
				System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			} catch (BiffException e) {
				System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		

		return listMaterialSAP;
		
		
		
/*		List<MaterialSAPBean> listMaterialSAP = null;		
		try {
			Workbook workbook = Workbook.getWorkbook(file); //Pasamos el excel que vamos a leer
			Sheet sheet = workbook.getSheet(0); //Seleccionamos la hoja que vamos a leer
			String nombre;
			int cantColumnas=sheet.getColumns();
			listMaterialSAP = new ArrayList<MaterialSAPBean>();
			if(cantColumnas==ConstantesGenerales.CANTIDAD_COLUMNAS_SAP_NOSERIADO){
				for (int fila = 1; fila < sheet.getRows(); fila++) { //recorremos las filas
					MaterialSAPBean materialSAPBean=new MaterialSAPBean();
					for (int columna = 0; columna < sheet.getColumns(); columna++) { //recorremos las columnas
						
						nombre = sheet.getCell(columna, fila).getContents(); //setear la celda leida a nombre
						materialSAPBean.setCantColumnaSAP(sheet.getColumns());
						System.out.println(nombre);
						switch(columna){
							case 0: materialSAPBean.setCodMaterial(nombre);
									break;
							case 1: materialSAPBean.setDenominacion(nombre);
									break;
							case 2: materialSAPBean.setCentro(nombre);
									break;
							case 3: materialSAPBean.setAlmacen(nombre);
									break;
							case 4: materialSAPBean.setLote(nombre);
									break;
							case 5: nombre=nombre.replace("," ,".");									
									materialSAPBean.setLibreUtilizacion(Double.parseDouble(nombre));
									break;
						}
					}
					
					listMaterialSAP.add(materialSAPBean);
				}			
			}else{
				MaterialSAPBean materialSAPBean= new MaterialSAPBean();
				materialSAPBean.setCantColumnaSAP(sheet.getColumns());
				listMaterialSAP.add(materialSAPBean);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());
		} catch (BiffException e) {
			System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());
		}

		return listMaterialSAP;*/
		
	}

	@Override
	public void eliminaDatosSapNoSeriado(String usuario) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void grabarMaterial(MaterialBean materialBean) {
		String sql = "";
		Connection cn = null;
		PreparedStatement pstmt = null;
		try {
			cn = dataSource.getConnection();
			sql = "INSERT INTO MATERIAL(CODMATERIAL,DESCRIPCION,UMD,PESO,VOLUNITARIO,SERIADO,CODPROV,CODNEGOCIO,CODRUBRO,CODTIPOMATERIAL,CODTECNOLOGIA,SOCIEDAD,PRECIOEQUIPO,LOTIZABLE) VALUES ('"	+ 
				   materialBean.getCodigo() + "','" + materialBean.getDescripcion() + "','" + materialBean.getUmd() + "'," + materialBean.getPeso() + "," + materialBean.getVolUnitario() + ",'" +
				   materialBean.getSeriado() + "','" + materialBean.getProvision() + "','" + materialBean.getNegocio() + "','" +  materialBean.getRubro() + "','" + materialBean.getTipoMaterial() + "','" + 
				   materialBean.getTecnologia() + "','" + materialBean.getSociedad() + "'," + materialBean.getPrecio() + ",'" + materialBean.getLotizable() + "')";				
			cn.setAutoCommit(false);
			pstmt = cn.prepareStatement(sql);
			pstmt.executeUpdate();
			cn.commit();

		} catch (SQLException ex) {
			System.out.println("Se suscito la siguiente Excepcion:" + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());
			}
		}
		
	}


	@Override
	public MaterialBean obtenerDatosMaterial(String codMaterial) {
		MaterialBean materialBean=null;
		Util util=new Util();
		String sql="SELECT CODMATERIAL,DESCRIPCION,UMD,PESO,VOLUNITARIO,SERIADO,LOTIZABLE FROM MATERIAL WHERE LPAD(TRIM(UPPER(CODMATERIAL)),20,'0')='" + util.rellenar(codMaterial.toUpperCase().trim(), 20) + "'";
		
		Connection cn=null;
		try {
			cn=dataSource.getConnection();

			Statement st=cn.createStatement();
			ResultSet rs=st.executeQuery(sql);
		
			if(rs.next()){
				materialBean=new MaterialBean();
				materialBean.setCodigo(rs.getString(1));
				materialBean.setDescripcion(rs.getString(2));
				materialBean.setUmd(rs.getString(3));
				materialBean.setPeso(rs.getString(4));
				materialBean.setVolUnitario(rs.getString(5));
				materialBean.setSeriado(rs.getString(6));
				materialBean.setLotizable(rs.getString(7));
			}
		} catch (SQLException e) {
			materialBean=null;
		} finally{
			try{
				cn.close();
			}catch(Exception e){
				System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());
			}
		}
		
		return materialBean;
	}


	public List<String> validarLongitudCodigoMaterial(List<MaterialSAPBean> lista,String tipoSAP) {
		List<String> lstLongitudCodigoMaterial=new ArrayList<String>();
		String codMaterial;
		int longitud=0;
		int contObsLongSAP60=0;
		int contObsLongSAP47=0;
		if(lista!=null){
			for(int i=0;i<lista.size();i++){
				codMaterial=lista.get(i).getCodMaterial();
				longitud=codMaterial.length();
				if(tipoSAP.equals(ConstantesGenerales.SAP_6_0)){
					if(longitud<ConstantesGenerales.LONGITUD_MATERIAL_SAP_6_0A){
						contObsLongSAP60++;
						if(contObsLongSAP60==1){
							lstLongitudCodigoMaterial.add("El codigo de Material para SAP 6.0 solo permite una longitud entre : <strong> " + ConstantesGenerales.LONGITUD_MATERIAL_SAP_6_0A + " </strong> y <strong>" + ConstantesGenerales.LONGITUD_MATERIAL_SAP_6_0B + "</strong> caracteres");												
						}

					}	
				}
				
				if(tipoSAP.equals(ConstantesGenerales.SAP_4_7)){
					if(longitud!=ConstantesGenerales.LONGITUD_MATERIAL_SAP_4_7A && longitud!=ConstantesGenerales.LONGITUD_MATERIAL_SAP_4_7B){
						contObsLongSAP47++;
						if(contObsLongSAP47==1){
							lstLongitudCodigoMaterial.add("El codigo de Material para SAP 4.7 solo permite una longitud entre : <strong> " + ConstantesGenerales.LONGITUD_MATERIAL_SAP_4_7A + " </strong> y <strong>" + ConstantesGenerales.LONGITUD_MATERIAL_SAP_4_7B + "</strong> caracteres");																			
						}

					}							
				}
								
			}
		}	
		return lstLongitudCodigoMaterial;
	}


	@Override
	public List<ErroresBean> obtenerTiposErroresSAP(List<MaterialSAPBean> lista,String tipoSAP,boolean seriado) {
		List<ErroresBean> lstTiposErrores=null;
		lstTiposErrores=new ArrayList<ErroresBean>();
		List<String> lstCantCampos=new ArrayList<String>();
		List<String> lstLongCampoMaterial=new ArrayList<String>();
		
		if(seriado){			
			if(lista.get(0).getCodMaterial()==null || (lista.get(0).getCantColumnaSAP()>ConstantesGenerales.CANTIDAD_COLUMNAS_SAP_SERIADO || lista.get(0).getCantColumnaSAP()<ConstantesGenerales.CANTIDAD_COLUMNAS_SAP_SERIADO)){
				lstCantCampos=validarCantidadCamposSeriado(lista);			
			}
			
			if(lista.get(0).getCantColumnaSAP()==ConstantesGenerales.CANTIDAD_COLUMNAS_SAP_SERIADO){
				lstLongCampoMaterial=validarLongitudCodigoMaterial(lista,tipoSAP);
			}
		}else{
			if(lista.get(0).getCodMaterial()==null || (lista.get(0).getCantColumnaSAP()>ConstantesGenerales.CANTIDAD_COLUMNAS_SAP_NOSERIADO || lista.get(0).getCantColumnaSAP()<ConstantesGenerales.CANTIDAD_COLUMNAS_SAP_NOSERIADO)){
				lstCantCampos=validarCantidadCamposNoSeriado(lista);			
			}

			if(lista.get(0).getCantColumnaSAP()==ConstantesGenerales.CANTIDAD_COLUMNAS_SAP_NOSERIADO){
				lstLongCampoMaterial=validarLongitudCodigoMaterial(lista,tipoSAP);
			}

		}
		

		
		ErroresBean erroresBean=null;
		
		if(lstCantCampos.size()>0){
			erroresBean=new ErroresBean();
			erroresBean.setCodError(ConstantesGenerales.TipoErroresSAP.CANTIDADCAMPOS.getTipoValor());
			erroresBean.setNomError("Cantidad de Campos Incorrecto");
			lstTiposErrores.add(erroresBean);
		}
		
		if(lstLongCampoMaterial.size()>0){
			erroresBean=new ErroresBean();
			erroresBean.setCodError(ConstantesGenerales.TipoErroresSAP.LONGITUDCAMPOMATERIAL.getTipoValor());
			erroresBean.setNomError("Longitud de Codigo de Material no coincide");
			lstTiposErrores.add(erroresBean);
		}
		
		return lstTiposErrores;
	}


	@Override
	public boolean existeRubrosPermitidosMaterialSAP47(String material) {
		Util util=new Util();
		boolean existe=false;
		
		String sql="SELECT RUB.NOMBRERUBRO FROM MATERIAL MAT,RUBRO RUB WHERE MAT.CODRUBRO=RUB.CODRUBRO AND LPAD(TRIM(UPPER(MAT.CODMATERIAL)),20,'0')='" + util.rellenar(material, 20).trim().toUpperCase() + "' " + 
				   "AND RUB.CODRUBRO IN('" + ConstantesGenerales.RubroPermitidos47.CABLEMODEM.getTipoValor() + "','" + ConstantesGenerales.RubroPermitidos47.DECO.getTipoValor() + "')";
		
		Connection cn=null;
		try {
			cn=dataSource.getConnection();

			Statement st=cn.createStatement();
			ResultSet rs=st.executeQuery(sql);
			
			if(rs.next()){
				existe=true;
			}
		} catch (SQLException e) {
			existe=false;
			System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
		} finally{
			try{
				cn.close();
			}catch(Exception e){
				System.out.println("Se suscito la siguiente excepcion " + e.getMessage());					
			}
		}
			
			return existe;
	}


	@Override
	public boolean existeMaterialPermitidosMaterialSAP47(String material) {
		Util util=new Util();
		boolean existe=false;
		
		String sql="SELECT MAT.CODMATERIAL FROM MATERIAL MAT WHERE LPAD(TRIM(UPPER(MAT.CODMATERIAL)),20,'0') IN('" + util.rellenar(material, 20).trim().toUpperCase() + "')";
		Connection cn=null;
		try {
			cn=dataSource.getConnection();

			Statement st=cn.createStatement();
			ResultSet rs=st.executeQuery(sql);
			
			if(rs.next()){
				existe=true;
			}
		} catch (SQLException e) {
			existe=false;
			System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
		} finally{
			try{
				cn.close();
			}catch(Exception e){
				System.out.println("Se suscito la siguiente excepcion " + e.getMessage());					
			}
		}
			
			return existe;
	}


}
