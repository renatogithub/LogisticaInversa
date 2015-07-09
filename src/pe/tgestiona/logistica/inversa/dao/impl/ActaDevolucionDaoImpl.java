package pe.tgestiona.logistica.inversa.dao.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

import pe.tgestiona.logistica.inversa.bean.ActaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.ConstantesGenerales;
import pe.tgestiona.logistica.inversa.bean.DetalleFichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.FichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.PedidoEntregaBean;
import pe.tgestiona.logistica.inversa.dao.ActaDevolucionDao;
import pe.tgestiona.logistica.inversa.util.Util;

@Repository
public class ActaDevolucionDaoImpl implements ActaDevolucionDao {

	@Autowired
	private DriverManagerDataSource dataSource;
	
	@Override
	public void generarActaDevolucion(List<FichaDevolucionBean> lista) {
		String nroActa="";
		Connection cn = null;
		PreparedStatement pstmt = null;
		List<DetalleFichaDevolucionBean> lstDetaFichaAprobado=null;
		try {
			cn = dataSource.getConnection();
			for(int i=0;i<lista.size();i++){
				
				nroActa=generaCodigoActaDevolucion(lista.get(i).getPrefijoActa());
				
				lstDetaFichaAprobado=lstDetalleAprobado(lista.get(i).getNroTicket());
				String sqlInsert="INSERT INTO ACTADEVOLUCION(NROACTA,NROTICKET,CANTIDAD,FECACTA,INDRECOJO) VALUES('" +  nroActa + "','" + 
								  lista.get(i).getNroTicket() + "'," + lstDetaFichaAprobado.size() + ",SYSDATE,'0')";
				
				cn.setAutoCommit(false);
				pstmt = cn.prepareStatement(sqlInsert);
				pstmt.executeUpdate();
				cn.commit();
				generarDetalleActaDevolucion(lstDetaFichaAprobado,nroActa);
			}			
		} catch (SQLException ex) {
			ex.getMessage();
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
			}
		}
		
	}

	@Override
	public String generaCodigoActaDevolucion(String prefijo) {
		Util util=new Util();
		String cantMax="";
		String formatoActa=null;
		Connection cn = null;	
		
		String sql="SELECT COUNT(NROACTA)+1 FROM ACTADEVOLUCION WHERE NROTICKET IN (SELECT NROTICKET FROM FICHADEVOLUCION WHERE PREFIJOACTA='" + prefijo + "')";
		
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if(rs.next()){
				cantMax=String.valueOf(rs.getInt(1));
				cantMax=util.rellenar(cantMax, 4);
			}
			
			formatoActa=prefijo + "-" + cantMax;
		
		} catch (SQLException ex) {
			formatoActa=ex.getMessage();
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
			}
		}
		
		return formatoActa;
	}

	@Override
	public List<DetalleFichaDevolucionBean> lstDetalleAprobado(String nroTicket) {
		List<DetalleFichaDevolucionBean> lstDetaFicha=null;
		lstDetaFicha=new ArrayList<DetalleFichaDevolucionBean>();
		
		String sql="SELECT SERIE,NROTICKET,CODSAP FROM DETALLEFICHADEVOLUCION WHERE ESTADO='" + ConstantesGenerales.EstadoValidacionActa.APROBADO.getTipoValor() + "' AND NROTICKET='" + nroTicket + "'";
		
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			lstDetaFicha = new ArrayList<DetalleFichaDevolucionBean>();
			while (rs.next()) {
				DetalleFichaDevolucionBean detalleFichaDevolucionBean=new DetalleFichaDevolucionBean();
				detalleFichaDevolucionBean.setSerie(rs.getString(1));
				detalleFichaDevolucionBean.setNroTicket(rs.getString(2));
				detalleFichaDevolucionBean.setCodSAP(rs.getString(3));
				
				lstDetaFicha.add(detalleFichaDevolucionBean);
			}
		} catch (SQLException ex) {
			lstDetaFicha = null;
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
			}
		}
			return lstDetaFicha;

	}

	@Override
	public List<ActaDevolucionBean> lstActaDevolucion(String formato) {
		List<ActaDevolucionBean> lstActaDevolucion = null;
		lstActaDevolucion=new ArrayList<ActaDevolucionBean>();
		String sql="SELECT ADEVOL.NROACTA,ADEVOL.NROTICKET,ENT.NOMENTIDAD,TPFOR.NOMFORM,TPDEVOL.NOMTIPODEVOL,ADEVOL.CANTIDAD " +  
				   "FROM ACTADEVOLUCION ADEVOL,FICHADEVOLUCION FDEVOL,TIPO_FORMATO TPFOR,TIPODEVOLUCION TPDEVOL,ENTIDAD ENT " + 
				   "WHERE FDEVOL.CODTIPODEVOL=TPDEVOL.CODTIPODEVOL AND TPFOR.CODFORM=TPDEVOL.CODFORM AND ADEVOL.NROTICKET=FDEVOL.NROTICKET AND FDEVOL.CODENTIDAD=ENT.CODENTIDAD AND TPFOR.CODFORM='" + formato + "' " + 
				   "AND TO_DATE(FDEVOL.FECHACARGA,'DD/MM/YY')=TO_DATE(SYSDATE,'DD/MM/YY') ORDER BY FDEVOL.NROTICKET DESC";

		System.out.println("sql:" + sql);
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			lstActaDevolucion = new ArrayList<ActaDevolucionBean>();
			while (rs.next()) {
				ActaDevolucionBean actaDevolucionBean=new ActaDevolucionBean();				
				actaDevolucionBean.setNroActa(rs.getString(1));
				actaDevolucionBean.setNroTicket(rs.getString(2));
				actaDevolucionBean.setEntidad(rs.getString(3));
				actaDevolucionBean.setTipoFormato(rs.getString(4));
				actaDevolucionBean.setTipoDevolucion(rs.getString(5));
				actaDevolucionBean.setCantItems(rs.getInt(6));
				
				lstActaDevolucion.add(actaDevolucionBean);
			}
		} catch (SQLException ex) {
			lstActaDevolucion = null;
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
			}
		}
		
		return lstActaDevolucion;
	}
	
	
	@Override
	public byte[] descargaFuentePedido(String nroActa,String tipoFormato) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		String sql="";
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
		
		celda1.setCellValue(new HSSFRichTextString("Nro Acta"));
		celda2.setCellValue(new HSSFRichTextString("NroTicket"));
		celda3.setCellValue(new HSSFRichTextString("Codigo Material SAP"));
		celda4.setCellValue(new HSSFRichTextString("Descripcion Material SAP"));
		celda5.setCellValue(new HSSFRichTextString("Cantidad"));
		celda6.setCellValue(new HSSFRichTextString("Centro"));
		celda7.setCellValue(new HSSFRichTextString("Almacen"));
		celda8.setCellValue(new HSSFRichTextString("Lote"));
		celda9.setCellValue(new HSSFRichTextString("Pedido"));
		celda10.setCellValue(new HSSFRichTextString("Posicion"));
		celda11.setCellValue(new HSSFRichTextString("Entrega"));
		celda12.setCellValue(new HSSFRichTextString("Fec.Creacion Vales"));
		
		 // Style Font in Cell 2B 
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
        
        if(tipoFormato.equals(ConstantesGenerales.TipoFormatoDevolucion.SERIADOS.getTipoValor())){
            sql="SELECT ACTDEVOL.NROACTA,ACTDEVOL.NROTICKET,(CASE WHEN DETFICHA.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DETFICHA.CODMATERIALSAP_4_7 ELSE DETFICHA.CODMATERIALSAP_6_0 END) AS CODMATERIAL," + 
                    "(CASE WHEN DETFICHA.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DETFICHA.DESMATERIALSAP_4_7 ELSE DETFICHA.DESMATERIALSAP_6_0 END) AS DESMATERIAL," +
                    "COUNT(DETACTA.NROSERIE) AS CANTSERIES," + 		
                    "(CASE WHEN DETFICHA.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DETFICHA.CENTROSAP_4_7 ELSE DETFICHA.CENTROSAP_6_0 END) AS CENTRO," +  
                    "(CASE WHEN DETFICHA.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DETFICHA.ALMACENSAP_4_7 ELSE DETFICHA.ALMACENSAP_6_0 END) AS ALMACEN," + 
                    "(CASE WHEN DETFICHA.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DETFICHA.LOTESAP_4_7 ELSE DETFICHA.LOTESAP_6_0 END) AS LOTE " + 
                    "FROM ACTADEVOLUCION ACTDEVOL,DETALLEACTADEVOLUCION DETACTA,DETALLEFICHADEVOLUCION DETFICHA " + 
                    "WHERE DETACTA.NROTICKET=ACTDEVOL.NROTICKET AND ACTDEVOL.NROACTA=DETACTA.NROACTA AND DETFICHA.NROTICKET=DETACTA.NROTICKET AND DETACTA.NROSERIE=DETFICHA.SERIE AND ACTDEVOL.NROACTA='" + nroActa + "' " + 
                    "GROUP BY ACTDEVOL.NROACTA,ACTDEVOL.NROTICKET,DETFICHA.CODMATERIALSAP_6_0,DETFICHA.CODMATERIALSAP_4_7,DETFICHA.DESMATERIALSAP_4_7,DETFICHA.DESMATERIALSAP_6_0," +
                    "DETFICHA.CENTROSAP_6_0,DETFICHA.CENTROSAP_4_7,DETFICHA.ALMACENSAP_6_0,DETFICHA.ALMACENSAP_4_7,DETFICHA.LOTESAP_6_0,DETFICHA.LOTESAP_4_7 " +
                    "ORDER BY DETFICHA.CODMATERIALSAP_6_0,DETFICHA.CODMATERIALSAP_4_7";        	
        }
        
        if(tipoFormato.equals(ConstantesGenerales.TipoFormatoDevolucion.NOSERIADOS.getTipoValor())){
        	
        	sql="SELECT ACTDEVOL.NROACTA, ACTDEVOL.NROTICKET,DETFICHA.CODSAP," + 
	        		"(CASE WHEN DETFICHA.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DETFICHA.DESMATERIALSAP_4_7 ELSE DETFICHA.DESMATERIALSAP_6_0 END) AS DESCRIPCION,"	 + 
	        		"SUM(DETFICHA.CANTIDAD) AS CANTIDAD," + 
	        		"(CASE WHEN DETFICHA.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DETFICHA.CENTROSAP_4_7 ELSE DETFICHA.CENTROSAP_6_0 END) AS CENTRO, " + 
	        		"(CASE WHEN DETFICHA.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DETFICHA.ALMACENSAP_4_7 ELSE DETFICHA.ALMACENSAP_6_0 END) AS ALMACEN," +
	        		"(CASE WHEN DETFICHA.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DETFICHA.LOTESAP_4_7 ELSE DETFICHA.LOTESAP_6_0 END) AS LOTE " +
	        		"FROM DETALLEFICHADEVOLUCION DETFICHA,ACTADEVOLUCION ACTDEVOL " + 
	        		"WHERE ACTDEVOL.NROACTA='" + nroActa + "' AND DETFICHA.ESTADO='APROBADO' " +
	        		"AND ACTDEVOL.NROTICKET=DETFICHA.NROTICKET " + 
	        		"GROUP BY ACTDEVOL.NROTICKET,DETFICHA.CODSAP,ACTDEVOL.NROACTA,DETFICHA.CODMATERIALSAP_6_0, " + 
	        		"DETFICHA.DESMATERIALSAP_4_7,DETFICHA.DESMATERIALSAP_6_0," +
	        		"DETFICHA.CENTROSAP_4_7,DETFICHA.CENTROSAP_6_0,DETFICHA.ALMACENSAP_4_7,DETFICHA.ALMACENSAP_6_0, " + 
	        		"DETFICHA.LOTESAP_4_7,DETFICHA.LOTESAP_6_0 " + 
	        		"ORDER BY DETFICHA.CODSAP";
       }
        
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
				row1.createCell((short)4).setCellValue(rs.getString(5));
		        row1.createCell((short)5).setCellValue(rs.getString(6));
		        row1.createCell((short)6).setCellValue(rs.getString(7));
		        row1.createCell((short)7).setCellValue(rs.getString(8));
		        row1.createCell((short)8).setCellValue("");
		        row1.createCell((short)9).setCellValue("");
		        row1.createCell((short)10).setCellValue("");
		        row1.createCell((short)11).setCellValue("");
			}
			
			for(int k=0;k<12;k++){
				sheet.autoSizeColumn(k);
			}
		} catch (SQLException ex) {
			System.out.println("Se suscito la siguiente Excepcion:" + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				System.out.println("Ha suscitado una excepcion:" + e.getMessage());
			}
		}
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
	    try {
	    	workbook.write(outByteStream);
		} catch (IOException e) {
			System.out.println("Ha suscitado una excepcion:" + e.getMessage());
		}
	    byte [] outArray = outByteStream.toByteArray();
		return outArray;
	}
	
	@Override
	public List<PedidoEntregaBean> leerArchivoPedidoEntrega(InputStream file) {
		List<PedidoEntregaBean> lstPedidoEntrega = null;
		
		try {
			Workbook workbook = Workbook.getWorkbook(file); //Pasamos el excel que vamos a leer
			Sheet sheet = workbook.getSheet(0); //Seleccionamos la hoja que vamos a leer
			String dato;
			lstPedidoEntrega = new ArrayList<PedidoEntregaBean>();
			
			for (int fila = 1; fila < sheet.getRows(); fila++) { //recorremos las filas
				PedidoEntregaBean pedidoEntregaBean=new PedidoEntregaBean();
				for (int columna = 0; columna < sheet.getColumns(); columna++) { //recorremos las columnas
					
					dato = sheet.getCell(columna, fila).getContents(); //setear la celda leida a nombre
					if(dato==null){
						dato="";
					}
					switch(columna){
					    case 0:	pedidoEntregaBean.setNroActa(dato);
					    		break;
					    case 1: pedidoEntregaBean.setNroTicket(dato);
					    		break;
						case 2: pedidoEntregaBean.setCodMaterial(dato);
								break;								
						case 3: pedidoEntregaBean.setDesMaterial(dato);
								break;
						case 4: if(dato.equals("")){
									pedidoEntregaBean.setCantSeries(0);
								}else{
									pedidoEntregaBean.setCantSeries(Integer.parseInt(dato));									
								}

								break;
						case 5: pedidoEntregaBean.setCentro(dato);
								break; 
						case 6: pedidoEntregaBean.setAlmacen(dato);
								break; 		
						case 7: pedidoEntregaBean.setLote(dato);
								break;
						case 8: pedidoEntregaBean.setPedido(dato);
								break;	
						case 9: pedidoEntregaBean.setPosicion(dato);
								break;			
						case 10: pedidoEntregaBean.setEntrega(dato);
								 break;			
						case 11: pedidoEntregaBean.setFecCreacionVale(dato);
								 break;			
					}
					
				}
				lstPedidoEntrega.add(pedidoEntregaBean);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		}
			
		return lstPedidoEntrega;
		
		
	}
	
	

	@Override
	public void asignarPedidoEntregaSeriado(List<PedidoEntregaBean> lstPedidoEntrega){ //Esto procedimiento solo se puede hacer por acta
		String sqlDetaActa="";
		String sqlFicha="";
		Connection cn = null;
		PreparedStatement pstmt = null;
		try {
			cn=dataSource.getConnection();	
			for(int i=0;i<lstPedidoEntrega.size();i++){				
				sqlDetaActa="UPDATE DETALLEACTADEVOLUCION DAD SET " + 
							"DAD.NROPEDIDO='" + lstPedidoEntrega.get(i).getPedido() + "'," +  
							"DAD.NROPOSICION='" + lstPedidoEntrega.get(i).getPosicion() + "'," + 
							"DAD.NROENTREGA='" + lstPedidoEntrega.get(i).getEntrega() + "', " +		
							"DAD.FECPEDIDOENTREGA='" + lstPedidoEntrega.get(i).getFecCreacionVale() + "' " +	
							"WHERE (DAD.NROTICKET||DAD.NROSERIE) IN (SELECT DFD.NROTICKET||DFD.SERIE FROM DETALLEFICHADEVOLUCION DFD WHERE " + 
							"(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.CODMATERIALSAP_4_7 ELSE DFD.CODMATERIALSAP_6_0 END)='" + lstPedidoEntrega.get(i).getCodMaterial() +  "' AND " + 
							"(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.DESMATERIALSAP_4_7 ELSE DFD.DESMATERIALSAP_6_0 END)='" + lstPedidoEntrega.get(i).getDesMaterial() + "' AND " + 
							"(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.CENTROSAP_4_7 ELSE DFD.CENTROSAP_6_0 END)='" + lstPedidoEntrega.get(i).getCentro() + "' AND " +
							"(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.ALMACENSAP_4_7 ELSE DFD.ALMACENSAP_6_0 END)='" + lstPedidoEntrega.get(i).getAlmacen() + "' AND " + 
							"(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.LOTESAP_4_7 ELSE DFD.LOTESAP_6_0 END)='" + lstPedidoEntrega.get(i).getLote() +  "' AND DFD.NROTICKET='" + lstPedidoEntrega.get(i).getNroTicket() +"')";

				cn.setAutoCommit(false);
				pstmt=cn.prepareStatement(sqlDetaActa);
				pstmt.executeUpdate();
				cn.commit();
			}
			
			sqlFicha="UPDATE FICHADEVOLUCION FDEVOL SET " + 
					 "FDEVOL.CONPEDIDO='" + ConstantesGenerales.EstadoPedidoActa.CONPEDIDO.getTipoValor() + "' " + 
					 "WHERE FDEVOL.NROTICKET='" +lstPedidoEntrega.get(0).getNroTicket() + "'";

			cn.setAutoCommit(false);
			pstmt=cn.prepareStatement(sqlFicha);
			pstmt.executeUpdate();
			cn.commit();
		} catch (SQLException e) {
			System.out.println("Se suscito la siguiente Excepcion:" +  e.getMessage());			
			pstmt=null;
		}finally{
			try{
				cn.close();
				pstmt=null;
			}catch(Exception e){
				System.out.println("Se suscito la siguiente Excepcion:" +  e.getMessage());	
			}
		}	
		
	}

	@Override
	public List<FichaDevolucionBean> obtenerFichasFuenteActas(List<FichaDevolucionBean> listaFicha) {
		List<FichaDevolucionBean> lstFichaDevol = null;
		String sqlConsulta="";		
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = null;
			for(int i=0;i<listaFicha.size();i++){
				sqlConsulta="SELECT TPFOR.NOMFORM,TPDEVOL.NOMTIPODEVOL,FDEVOL.NROTICKET,TPDEVOL.ABRTIPODEVOL,FDEVOL.PREFIJOACTA FROM FICHADEVOLUCION FDEVOL,TIPODEVOLUCION TPDEVOL,TIPO_FORMATO TPFOR WHERE FDEVOL.CODTIPODEVOL=TPDEVOL.CODTIPODEVOL AND TPFOR.CODFORM=TPDEVOL.CODFORM " + 
						    "AND (FDEVOL.ESTADO='" + ConstantesGenerales.EstadoValidacionActa.APROBADO.getTipoValor() + "' OR FDEVOL.ESTADO='" + ConstantesGenerales.EstadoValidacionActa.PROCESADO.getTipoValor() + "') AND FDEVOL.NROTICKET NOT IN (SELECT NROTICKET FROM ACTADEVOLUCION)";
	
				rs = st.executeQuery(sqlConsulta);
				lstFichaDevol = new ArrayList<FichaDevolucionBean>();
					while (rs.next()) {
						FichaDevolucionBean fichaDevolucionBean = new FichaDevolucionBean();
						fichaDevolucionBean.setTipoFormato(rs.getString(1));
						fichaDevolucionBean.setTipoDevolucion(rs.getString(2));
						fichaDevolucionBean.setNroTicket(rs.getString(3));
						fichaDevolucionBean.setTipoDevolucionAbvr(rs.getString(4));
						fichaDevolucionBean.setPrefijoActa(rs.getString(5));
						lstFichaDevol.add(fichaDevolucionBean);
					}
			}
		} catch (SQLException ex) {
			lstFichaDevol = null;
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
			}
		}
		return lstFichaDevol;
	}

	@Override
	public void generarDetalleActaDevolucion(List<DetalleFichaDevolucionBean> lista,String nroActa) {
		Connection cn = null;
		PreparedStatement pstmt01 = null;
		PreparedStatement pstmt02 = null;
		PreparedStatement pstmt03 = null;
		
		try {
			cn = dataSource.getConnection();
			for(int i=0;i<lista.size();i++){
				String sqlInsertDetalleActa="INSERT INTO DETALLEACTADEVOLUCION(NROACTA,NROTICKET,NROSERIE,CODMATERIAL,ID) VALUES('" +  nroActa + "','" + 
											 lista.get(i).getNroTicket() + "','" + lista.get(i).getSerie() + "','" + lista.get(i).getCodSAP() + "'," + (i+1) + ")";
				cn.setAutoCommit(false);
				pstmt01 = cn.prepareStatement(sqlInsertDetalleActa);
				pstmt01.executeUpdate();
				cn.commit();
			
			}			
			
			for(int j=0;j<lista.size();j++){
				String sqlInsertDetalleDistribucion="INSERT INTO DISTRIBUCION(NROACTA,NROTICKET,NROSERIE,ID) VALUES('" +  nroActa + "','" + 
											lista.get(j).getNroTicket() + "','" + lista.get(j).getSerie() + "'," + (j+1) + ")";
				cn.setAutoCommit(false);
				pstmt02 = cn.prepareStatement(sqlInsertDetalleDistribucion);
				pstmt02.executeUpdate();
				cn.commit();			
			}			

			
			for(int k=0;k<lista.size();k++){
				String sqlInsertDetallePlaneamiento="INSERT INTO PLANEAMIENTO(NROACTA,NROTICKET,NROSERIE,ID) VALUES('" +  nroActa + "','" + 
											lista.get(k).getNroTicket() + "','" + lista.get(k).getSerie() + "'," + (k+1) + ")";
				cn.setAutoCommit(false);
				pstmt03 = cn.prepareStatement(sqlInsertDetallePlaneamiento);
				pstmt03.executeUpdate();
				cn.commit();
			
			}			

			
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {
				cn.close();
				pstmt01.close();
				pstmt02.close();
				pstmt03.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
	}

	@Override
	public List<String[]> lstActasGeneradas(String acta,String nroTicket,String material,String serie,String descrip,String estado,String f1,String f2,String estadoActa,String pag) {
		List<String[]> lstDetaFicha=null;
		int pagInicio=0;
		int pagFin=0;		
		int regMostrar=20;
		
		String sql="SELECT * FROM(SELECT A.*,ROWNUM RNUM FROM (SELECT DECODE(ACTDEVOL.NROACTA,NULL,'NO TIENE NRO DE ACTA',ACTDEVOL.NROACTA),DECODE(DETFICHA.NROTICKET,NULL,'" + ConstantesGenerales.GUION + "',DETFICHA.NROTICKET),DECODE((CASE WHEN DETFICHA.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DETFICHA.CODMATERIALSAP_4_7 ELSE DETFICHA.CODMATERIALSAP_6_0 END),NULL,'" + ConstantesGenerales.GUION + "',(CASE WHEN DETFICHA.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DETFICHA.CODMATERIALSAP_4_7 ELSE DETFICHA.CODMATERIALSAP_6_0 END)) AS CODMATERIAL,DECODE(DETFICHA.SERIE,NULL,'" + ConstantesGenerales.GUION + "',DETFICHA.SERIE),(CASE WHEN DETFICHA.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DETFICHA.DESMATERIALSAP_4_7 ELSE DETFICHA.DESMATERIALSAP_6_0 END) AS DESMATERIAL,DECODE(DETFICHA.ESTADO,NULL,'" + ConstantesGenerales.GUION + "',DETFICHA.ESTADO) " + 
				   "FROM DETALLEFICHADEVOLUCION DETFICHA LEFT OUTER JOIN ACTADEVOLUCION ACTDEVOL ON ACTDEVOL.NROTICKET=DETFICHA.NROTICKET,FICHADEVOLUCION FD ";
		
		String sqlGeneral="";
		String sqlCamposCondicion="";
		String sqlCamposCondicionGeneral="";
		String sqlCamposCondicionTotal="";
		String sqlCantidadTotal="";
		
		if(Integer.parseInt(pag)>=1){
			pagInicio=(Integer.parseInt(pag)-1)*regMostrar;
			pagFin=(Integer.parseInt(pag))*regMostrar;
		}else{
			pagInicio=0;
			pagFin=regMostrar;
		}
		
		if(acta.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " UPPER(TRIM(ACTDEVOL.NROACTA))='" + acta.trim().toUpperCase() + "' AND ";
		}
		
		if(nroTicket.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " UPPER(TRIM(DETFICHA.NROTICKET)) LIKE '%" + nroTicket.trim().toUpperCase() + "%' AND ";
		}
		
		if(material.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " (UPPER(TRIM(DETFICHA.CODMATERIALSAP_4_7))='" + material.trim().toUpperCase() + "' OR UPPER(TRIM(DETFICHA.CODMATERIALSAP_6_0))='" + material.trim().toUpperCase() + "') AND ";
		}
		
		if(serie.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " UPPER(TRIM(DETFICHA.SERIE))='" + serie.trim().toUpperCase() + "' AND ";
		}
		
		if(descrip.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " (UPPER(TRIM(DETFICHA.DESMATERIALSAP_4_7)) LIKE '%" + descrip.trim().toUpperCase() + "%' OR UPPER(TRIM(DETFICHA.DESMATERIALSAP_6_0)) LIKE '%" + descrip.trim().toUpperCase() + "%') AND";
		}
		
		if(estado.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " UPPER(TRIM(DETFICHA.ESTADO))='" + estado.trim().toUpperCase() + "' AND ";
		}

		if((f1.length()>0) && (f2.length()>0)){
			sqlCamposCondicion=sqlCamposCondicion + " TO_DATE(FD.FECHASOLICITUD,'DD/MM/YY') BETWEEN TO_DATE('" + f1 + "','DD/MM/YY') AND TO_DATE('" + f2 + "','DD/MM/YY') AND ";
		}else{
			if(f1.length()>0){
				sqlCamposCondicion=sqlCamposCondicion + " TO_DATE(FD.FECHASOLICITUD,'DD/MM/YY')=TO_DATE('" + f1 + "','DD/MM/YY')  AND ";
			}

			
			if(f2.length()>0){
				sqlCamposCondicion=sqlCamposCondicion + " TO_DATE(FD.FECHASOLICITUD,'DD/MM/YY')=TO_DATE('" + f2 + "','DD/MM/YY')  AND ";
			}

		}		

		if(estadoActa.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " UPPER(TRIM(FD.ESTADO))='" + estadoActa.trim().toUpperCase() + "' AND ";	
		}
		
		if(sqlCamposCondicion.length()>0){
			sqlCamposCondicionGeneral=" WHERE " + sqlCamposCondicion + " FD.NROTICKET=DETFICHA.NROTICKET ORDER BY DETFICHA.NROTICKET,NRO ) A WHERE ROWNUM<=" + pagFin + ") WHERE RNUM>" + pagInicio;			
			sqlCamposCondicionTotal=" WHERE " + sqlCamposCondicion + " FD.NROTICKET=DETFICHA.NROTICKET ORDER BY DETFICHA.NROTICKET,NRO ) A )"; 
		}
		
		Connection cn = null;
		sqlGeneral=sql + sqlCamposCondicionGeneral;
		sqlCantidadTotal=sql + sqlCamposCondicionTotal;
		
		System.out.println(sqlGeneral);
		System.out.println(sqlCantidadTotal);
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			Statement stTotal = cn.createStatement();
			ResultSet rs = st.executeQuery(sqlGeneral);
			ResultSet rsTotal=stTotal.executeQuery(sqlCantidadTotal);
			int columnCount = rs.getMetaData().getColumnCount();
			int rowCountTotal=0;
			lstDetaFicha=new ArrayList<String[]>();
			while(rsTotal.next()){
				rowCountTotal=rowCountTotal+1;
			}
			
			while (rs.next()) {
				String[] row = new String[columnCount+1]; //Le adicione uno para agregarle los totales al final
					for (int i=0; i <columnCount ; i++){
				       row[i] = rs.getString(i + 1);
				    }
					row[columnCount]=String.valueOf(rowCountTotal);
				lstDetaFicha.add(row);
			}
		} catch (SQLException ex) {
			lstDetaFicha = null;
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
			}
		}
			return lstDetaFicha;
	}

	
	@Override
	public byte[] lstActasGeneradasExcel(String acta,String nroTicket,String material,String serie,String descrip,String estado,String pag) {
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
		HSSFCell celda23=row.createCell(22);
		HSSFCell celda24=row.createCell(23);
		HSSFCell celda25=row.createCell(24);
		HSSFCell celda26=row.createCell(25);
		HSSFCell celda27=row.createCell(26);
		HSSFCell celda28=row.createCell(27);
		HSSFCell celda29=row.createCell(28);
		HSSFCell celda30=row.createCell(29);
		HSSFCell celda31=row.createCell(30);
		HSSFCell celda32=row.createCell(31);
		HSSFCell celda33=row.createCell(32);
		HSSFCell celda34=row.createCell(33);
		HSSFCell celda35=row.createCell(34);
		HSSFCell celda36=row.createCell(35);
		HSSFCell celda37=row.createCell(36);
		HSSFCell celda38=row.createCell(37);
		HSSFCell celda39=row.createCell(38);
		HSSFCell celda40=row.createCell(39);
		HSSFCell celda41=row.createCell(40);
		HSSFCell celda42=row.createCell(41);
		HSSFCell celda43=row.createCell(42);
		HSSFCell celda44=row.createCell(43);
		HSSFCell celda45=row.createCell(44);
		HSSFCell celda46=row.createCell(45);
		HSSFCell celda47=row.createCell(46);
		HSSFCell celda48=row.createCell(47);
		HSSFCell celda49=row.createCell(48);
		HSSFCell celda50=row.createCell(49);
		HSSFCell celda51=row.createCell(50);
		HSSFCell celda52=row.createCell(51);
		HSSFCell celda53=row.createCell(52);
		HSSFCell celda54=row.createCell(53);
		HSSFCell celda55=row.createCell(54);
		HSSFCell celda56=row.createCell(55);
		HSSFCell celda57=row.createCell(56);
		HSSFCell celda58=row.createCell(57);
		HSSFCell celda59=row.createCell(58);
		HSSFCell celda60=row.createCell(59);
		HSSFCell celda61=row.createCell(60);
		HSSFCell celda62=row.createCell(61);
		HSSFCell celda63=row.createCell(62);
		HSSFCell celda64=row.createCell(63);
		HSSFCell celda65=row.createCell(64);
		HSSFCell celda66=row.createCell(65);
		HSSFCell celda67=row.createCell(66);
		HSSFCell celda68=row.createCell(67);
		HSSFCell celda69=row.createCell(68);
		HSSFCell celda70=row.createCell(69);
		HSSFCell celda71=row.createCell(70);
		HSSFCell celda72=row.createCell(71);
		HSSFCell celda73=row.createCell(72);
		HSSFCell celda74=row.createCell(73);
		HSSFCell celda75=row.createCell(74);
		HSSFCell celda76=row.createCell(75);
		HSSFCell celda77=row.createCell(76);
		HSSFCell celda78=row.createCell(77);
		HSSFCell celda79=row.createCell(78);
		HSSFCell celda80=row.createCell(79);
		HSSFCell celda81=row.createCell(80);
		HSSFCell celda82=row.createCell(81);
		HSSFCell celda83=row.createCell(82);
		HSSFCell celda84=row.createCell(83);
		HSSFCell celda85=row.createCell(84);
		HSSFCell celda86=row.createCell(85);
		HSSFCell celda87=row.createCell(86);
		
		celda1.setCellValue(new HSSFRichTextString("FECHA"));
		celda2.setCellValue(new HSSFRichTextString("MES DE GESTION"));		
		celda3.setCellValue(new HSSFRichTextString("FECHA SOLICITUD ACTA"));
		celda4.setCellValue(new HSSFRichTextString("CORREO"));
		celda5.setCellValue(new HSSFRichTextString("ZONA(LIMA/PROVINCIA)"));	
		celda6.setCellValue(new HSSFRichTextString("DEPARTAMENTO"));
		celda7.setCellValue(new HSSFRichTextString("PROVINCIA"));
		celda8.setCellValue(new HSSFRichTextString("CANAL"));
		celda9.setCellValue(new HSSFRichTextString("ENTIDAD"));
		celda10.setCellValue(new HSSFRichTextString("TICKET"));
		celda11.setCellValue(new HSSFRichTextString("ACTA GENERADA"));
		celda12.setCellValue(new HSSFRichTextString("MOTIVO DE DEVOLUCION"));
		celda13.setCellValue(new HSSFRichTextString("PROVISION/PLANTA EXTERNA"));
		celda14.setCellValue(new HSSFRichTextString("NEGOCIO"));
		celda15.setCellValue(new HSSFRichTextString("TIPO DE MATERIAL"));
		celda16.setCellValue(new HSSFRichTextString("RUBRO DE MATERIAL"));	
		celda17.setCellValue(new HSSFRichTextString("TECNOLOGIA"));		
		celda18.setCellValue(new HSSFRichTextString("SERIADO / NO SERIADO"));
		celda19.setCellValue(new HSSFRichTextString("MES LIQUIDACION EE.CC-MC-AGENCIA (AVERIAS)"));		
		celda20.setCellValue(new HSSFRichTextString("DIAGNOSTICO"));		
		celda21.setCellValue(new HSSFRichTextString("ITEM DE ACTA"));
		celda22.setCellValue(new HSSFRichTextString("SERIE"));
		celda23.setCellValue(new HSSFRichTextString("MAC ADRESS(SOLO MODEM AVERIADOS)"));
		celda24.setCellValue(new HSSFRichTextString("CODIGO MATERIAL ACTA"));		
		celda25.setCellValue(new HSSFRichTextString("CODIGO DE PARTE"));
		celda26.setCellValue(new HSSFRichTextString("N° CIRCUITO DIGITAL"));
		celda27.setCellValue(new HSSFRichTextString("NÚMERO DE REQUERIMIENTO"));		
		celda28.setCellValue(new HSSFRichTextString("SOCIEDAD"));
		celda29.setCellValue(new HSSFRichTextString("CODIGO MATERIAL(SAP 6.0)"));
		celda30.setCellValue(new HSSFRichTextString("DESCRIPCION MATERIAL (SAP 6.0)"));				
		celda31.setCellValue(new HSSFRichTextString("CENTRO (SAP 6.0)"));
		celda32.setCellValue(new HSSFRichTextString("ALMACEN (SAP 6.0)"));
		celda33.setCellValue(new HSSFRichTextString("LOTE (SAP 6.0)"));				
		celda34.setCellValue(new HSSFRichTextString("TIPO STOCKS (SAP 6.0)"));
		celda35.setCellValue(new HSSFRichTextString("STATUS SISTEMAS (SAP 6.0)"));
		celda36.setCellValue(new HSSFRichTextString("FECHA ULTIMO MOV SAP(FECHA MODIFICACION SAP 6.0)"));
		celda37.setCellValue(new HSSFRichTextString("MODIFICADO POR (SAP 6.0)"));
		celda38.setCellValue(new HSSFRichTextString("FECHA INGRESO SAP(FECHA DE CREACION SAP 6.0)"));
		celda39.setCellValue(new HSSFRichTextString("CREADO POR (SAP 6.0)"));
		celda40.setCellValue(new HSSFRichTextString("STATUS (SAP 6.0)"));		
		celda41.setCellValue(new HSSFRichTextString("CODIGO MATERIAL(SAP 4.7)"));
		celda42.setCellValue(new HSSFRichTextString("DESCRIPCION MATERIAL (SAP 4.7)"));				
		celda43.setCellValue(new HSSFRichTextString("CENTRO (SAP 4.7)"));
		celda44.setCellValue(new HSSFRichTextString("ALMACEN (SAP 4.7)"));
		celda45.setCellValue(new HSSFRichTextString("LOTE (SAP 4.7)"));				
		celda46.setCellValue(new HSSFRichTextString("TIPO STOCKS (SAP 4.7)"));
		celda47.setCellValue(new HSSFRichTextString("STATUS SISTEMAS (SAP 4.7)"));
		celda48.setCellValue(new HSSFRichTextString("FECHA ULTIMO MOV SAP(FECHA MODIFICACION SAP 4.7)"));
		celda49.setCellValue(new HSSFRichTextString("MODIFICADO POR (SAP 4.7)"));
		celda50.setCellValue(new HSSFRichTextString("FECHA INGRESO SAP(FECHA DE CREACION SAP 4.7)"));
		celda51.setCellValue(new HSSFRichTextString("CREADO POR (SAP 4.7)"));
		celda52.setCellValue(new HSSFRichTextString("STATUS (SAP 4.7)"));
		celda53.setCellValue(new HSSFRichTextString("CODIGO SAP"));
		celda54.setCellValue(new HSSFRichTextString("DESCRIPCION MATERIAL SAP"));
		celda55.setCellValue(new HSSFRichTextString("CENTRO SAP"));
		celda56.setCellValue(new HSSFRichTextString("ALMACEN SAP"));
		celda57.setCellValue(new HSSFRichTextString("LOTE SAP"));
		celda58.setCellValue(new HSSFRichTextString("TIPO STOCKS SAP"));
		celda59.setCellValue(new HSSFRichTextString("STATUS SISTEMAS SAP"));
		celda60.setCellValue(new HSSFRichTextString("FECHA ULTIMO MOV SAP(FECHA MODIFICACION SAP)"));
		celda61.setCellValue(new HSSFRichTextString("MODIFICADO POR SAP"));
		celda62.setCellValue(new HSSFRichTextString("FECHA INGRESO SAP(FECHA CREACION SAP)"));
		celda63.setCellValue(new HSSFRichTextString("CREADO POR SAP"));
		celda64.setCellValue(new HSSFRichTextString("STATUS SAP"));
		celda65.setCellValue(new HSSFRichTextString("CANT MATERIALES"));		
		celda66.setCellValue(new HSSFRichTextString("LIQUIDADA / NO LIQUIDADA (AVERIAS Y BAJAS)"));	
		celda67.setCellValue(new HSSFRichTextString("VALIDACION DE GARANTIA  PROVEEDOR Y TALLER"));		
		celda68.setCellValue(new HSSFRichTextString("REMOZADO/ REPARADO"));
		celda69.setCellValue(new HSSFRichTextString("DIAS DE GARANTIA SAP/TALLER"));		
		celda70.setCellValue(new HSSFRichTextString("FECHA REMOZADO TALLER"));
		celda71.setCellValue(new HSSFRichTextString("Fecha SAP de despacho al almacén de la EECC."));		
		celda72.setCellValue(new HSSFRichTextString("VALIDACION DEL ACTA"));
		celda73.setCellValue(new HSSFRichTextString("FECHA VALIDACION ACTA"));
		celda74.setCellValue(new HSSFRichTextString("OBSERVACIONES"));
		celda75.setCellValue(new HSSFRichTextString("MOTIVO DE RECHAZO O DEVOLUCIÓN"));
		celda76.setCellValue(new HSSFRichTextString("MOTIVO DE APROBACION O DEVOLUCIÓN"));
		celda77.setCellValue(new HSSFRichTextString("PEDIDO(EQUIPOS CARGADOS)"));
		celda78.setCellValue(new HSSFRichTextString("POSICION PEDIDO VALE"));
		celda79.setCellValue(new HSSFRichTextString("N° ENTREGA(EQUIPOS CARGADOS)"));
		celda80.setCellValue(new HSSFRichTextString("FECHA DE CREACION PEDIDOS / VALES"));
		celda81.setCellValue(new HSSFRichTextString("ENVIADO"));
		celda82.setCellValue(new HSSFRichTextString("DESTINO FISICO"));				
		celda83.setCellValue(new HSSFRichTextString("FECHA ENVIO PARA GESTION DF/PL"));
		celda84.setCellValue(new HSSFRichTextString("PESO(kg)"));
		celda85.setCellValue(new HSSFRichTextString("VOLUMEN (m3)"));
		celda86.setCellValue(new HSSFRichTextString("PRECIO EQUIPO(TRASLADO DF)"));				
		celda87.setCellValue(new HSSFRichTextString("MODO DE RECOJO"));
       
		 // Estilo01
        HSSFCellStyle cellStyle01 = workbook.createCellStyle();
        cellStyle01 = workbook.createCellStyle();
        cellStyle01.setFillForegroundColor(HSSFColor.DARK_BLUE.index);
        cellStyle01.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle01.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        // Estilo02
        HSSFCellStyle cellStyle02 = workbook.createCellStyle();
        cellStyle02 = workbook.createCellStyle();
        cellStyle02.setFillForegroundColor(HSSFColor.VIOLET.index);
        cellStyle02.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle02.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        // Estilo03
        HSSFCellStyle cellStyle03 = workbook.createCellStyle();
        cellStyle03 = workbook.createCellStyle();
        cellStyle03.setFillForegroundColor(HSSFColor.GREEN.index);
        cellStyle03.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle03.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        HSSFFont hSSFFont = workbook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 11);
        hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        hSSFFont.setColor(HSSFColor.WHITE.index);
        
        cellStyle01.setFont(hSSFFont);
        cellStyle02.setFont(hSSFFont);
        cellStyle03.setFont(hSSFFont);
        
        celda1.setCellStyle(cellStyle01);
        celda2.setCellStyle(cellStyle01);
        celda3.setCellStyle(cellStyle01);
        celda4.setCellStyle(cellStyle01);
        celda5.setCellStyle(cellStyle01);
        celda6.setCellStyle(cellStyle01);
        celda7.setCellStyle(cellStyle01);
        celda8.setCellStyle(cellStyle01);
        celda9.setCellStyle(cellStyle01);
        celda10.setCellStyle(cellStyle01);
        celda11.setCellStyle(cellStyle02);
        celda12.setCellStyle(cellStyle01);
        celda13.setCellStyle(cellStyle01);
        celda14.setCellStyle(cellStyle01);
        celda15.setCellStyle(cellStyle01);
        celda16.setCellStyle(cellStyle01);
        celda17.setCellStyle(cellStyle01);
        celda18.setCellStyle(cellStyle01);
        celda19.setCellStyle(cellStyle01);
        celda20.setCellStyle(cellStyle01);
        celda21.setCellStyle(cellStyle01);
        celda22.setCellStyle(cellStyle01);
        celda23.setCellStyle(cellStyle01);
        celda24.setCellStyle(cellStyle01);
        celda25.setCellStyle(cellStyle03);
        celda26.setCellStyle(cellStyle03);        
        celda27.setCellStyle(cellStyle03);        
        celda28.setCellStyle(cellStyle02);        
        celda29.setCellStyle(cellStyle02);
        celda30.setCellStyle(cellStyle02);
        celda31.setCellStyle(cellStyle02);
        celda32.setCellStyle(cellStyle02);
        celda33.setCellStyle(cellStyle02);
        celda34.setCellStyle(cellStyle02);
        celda35.setCellStyle(cellStyle02);
        celda36.setCellStyle(cellStyle02);
        celda37.setCellStyle(cellStyle02);
        celda38.setCellStyle(cellStyle02);
        celda39.setCellStyle(cellStyle02);
        celda40.setCellStyle(cellStyle02);
        celda41.setCellStyle(cellStyle02);
        celda42.setCellStyle(cellStyle02);
        celda43.setCellStyle(cellStyle02);
        celda44.setCellStyle(cellStyle02);
        celda45.setCellStyle(cellStyle02);
        celda46.setCellStyle(cellStyle02);
        celda47.setCellStyle(cellStyle02);
        celda48.setCellStyle(cellStyle02);
        celda49.setCellStyle(cellStyle02);
        celda50.setCellStyle(cellStyle02);
        celda51.setCellStyle(cellStyle02);
        celda52.setCellStyle(cellStyle02);
        celda53.setCellStyle(cellStyle02);
        celda54.setCellStyle(cellStyle02);
        celda55.setCellStyle(cellStyle02);
        celda56.setCellStyle(cellStyle02);
        celda57.setCellStyle(cellStyle02);
        celda58.setCellStyle(cellStyle02);
        celda59.setCellStyle(cellStyle02);
        celda60.setCellStyle(cellStyle02);
        celda61.setCellStyle(cellStyle02);
        celda62.setCellStyle(cellStyle02);
        celda63.setCellStyle(cellStyle02);
        celda64.setCellStyle(cellStyle02);
        celda65.setCellStyle(cellStyle02);
        celda66.setCellStyle(cellStyle02);
        celda67.setCellStyle(cellStyle02);
        celda68.setCellStyle(cellStyle02);
        celda69.setCellStyle(cellStyle02);
        celda70.setCellStyle(cellStyle02);
        celda71.setCellStyle(cellStyle02);
        celda72.setCellStyle(cellStyle02);
        celda73.setCellStyle(cellStyle02);
        celda74.setCellStyle(cellStyle02);
        celda75.setCellStyle(cellStyle02);
        celda76.setCellStyle(cellStyle02);
        celda77.setCellStyle(cellStyle02);
        celda78.setCellStyle(cellStyle02);
        celda79.setCellStyle(cellStyle02);
        celda80.setCellStyle(cellStyle02);
        celda81.setCellStyle(cellStyle02);
        celda82.setCellStyle(cellStyle02);
        celda83.setCellStyle(cellStyle02);
        celda84.setCellStyle(cellStyle02);
        celda85.setCellStyle(cellStyle02); 
        celda86.setCellStyle(cellStyle02);
        celda87.setCellStyle(cellStyle02); 
        
        String sql="SELECT DISTINCT TO_CHAR(DFD.FECHAVALIDACION,'DD/MM/YYYY HH24:MI:SS'),FD.MESGESTION,TO_CHAR(FD.FECHASOLICITUD,'DD/MM/YYYY HH24:MI:SS'),GESTOR.NOMGESTOR||' <'||FD.CORREO||'>',UBI.ZONAL,(SELECT NOMBRE FROM UBIGEO  WHERE CODDEP=SUBSTR(ENT.CODUBIGEO, 0, 2) AND CODPROV='00' AND CODDIST='00') AS DEPARTAMENTO," + 
        		   "(SELECT NOMBRE FROM UBIGEO  WHERE CODDEP=SUBSTR(ENT.CODUBIGEO, 0, 2) AND CODPROV=SUBSTR(ENT.CODUBIGEO, 3, 2) AND CODDIST='00') AS PROVINCIA," + 	
        		   "CA.NOMCANAL,ENT.NOMENTIDAD,FD.NROTICKET,DECODE(DETACTA.NROACTA,NULL,'" + ConstantesGenerales.GUION + "',DETACTA.NROACTA),TDEVOL.NOMTIPODEVOL,DFD.PROVISION,DFD.NEGOCIO,DFD.TIPO,DFD.RUBRO,DFD.TECNOLOGIA,DFD.SERIADO,DFD.MESLIQUIDACIONAVERIAS,DFD.DIAGNOSTICO,DFD.NRO,DFD.SERIE," + 
        		   "DECODE(DFD.DIRECCIONMAC,NULL,'" + ConstantesGenerales.GUION + "',DFD.DIRECCIONMAC),DFD.CODSAP,DFD.CODIGOPARTE,DFD.NROREQUERIMIENTO,DFD.NROCIRCUITODIGITAL,DFD.SOCIEDAD, " + 
        		   "DFD.CODMATERIALSAP_6_0,DFD.DESMATERIALSAP_6_0,DFD.CENTROSAP_6_0,DFD.ALMACENSAP_6_0,DFD.LOTESAP_6_0,DFD.TIPOSTOCKS_6_0,DFD.STATUSISTEMA_6_0,DFD.FECHAULTIMOMOVSAP_6_0,DFD.MODIFICADOPOR_6_0,DFD.FECHAINGRESOSAP_6_0,DFD.CREADOPOR_6_0,DFD.STATUSSAP_6_0," +
        		   "DFD.CODMATERIALSAP_4_7,DFD.DESMATERIALSAP_4_7,DFD.CENTROSAP_4_7,DFD.ALMACENSAP_4_7,DFD.LOTESAP_4_7,DFD.TIPOSTOCKS_4_7,DFD.STATUSISTEMA_4_7,DFD.FECHAULTIMOMOVSAP_4_7,DFD.MODIFICADOPOR_4_7,DFD.FECHAINGRESOSAP_4_7,DFD.CREADOPOR_4_7,DFD.STATUSSAP_4_7," + 
        		   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.CODMATERIALSAP_4_7 ELSE DFD.CODMATERIALSAP_6_0 END) AS CODIGOSAP," +      		         		   
        		   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.DESMATERIALSAP_4_7 ELSE DFD.DESMATERIALSAP_6_0 END) AS DESCRIPCION," +  
        		   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.CENTROSAP_4_7 ELSE DFD.CENTROSAP_6_0 END) AS CENTRO," + 
        		   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.ALMACENSAP_4_7 ELSE DFD.ALMACENSAP_6_0 END) AS ALMACEN,"+
        		   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.LOTESAP_4_7 ELSE DFD.LOTESAP_6_0 END) AS LOTE, "+
        		   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.TIPOSTOCKS_4_7 ELSE DFD.TIPOSTOCKS_6_0 END) AS TIPOSTOCKS," +      		         		   
        		   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.STATUSISTEMA_4_7 ELSE DFD.STATUSISTEMA_6_0 END) AS STATUSISTEMA," +  
        		   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.FECHAULTIMOMOVSAP_4_7 ELSE DFD.FECHAULTIMOMOVSAP_6_0 END) AS FECHAULTIMOMOVIMIENTOSAP," + 
        		   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.MODIFICADOPOR_4_7 ELSE DFD.MODIFICADOPOR_6_0 END) AS MODIFICADOPORSAP,"+
        		   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.FECHAINGRESOSAP_4_7 ELSE DFD.FECHAINGRESOSAP_6_0 END) AS FECHAINGRESOSAP, "+     		   
        		   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.CREADOPOR_4_7 ELSE DFD.CREADOPOR_6_0 END) AS CREADOPORSAP," +      		         		   
        		   "DECODE(DFD.STATUSSAP_6_0,'" + ConstantesGenerales.ExistenciaSAP.EXISTESAP.getTipoValor() + "','" + ConstantesGenerales.ExistenciaSAP.EXISTESAP.getTipoValor() + "',DECODE(DFD.STATUSSAP_4_7,'" + ConstantesGenerales.ExistenciaSAP.NOEXISTESAP.getTipoValor() + "','"  +  ConstantesGenerales.ExistenciaSAP.NOEXISTESAP.getTipoValor() + "','" +  ConstantesGenerales.ExistenciaSAP.EXISTESAP.getTipoValor() +"')), DFD.CANTIDAD," +  
        		   "DFD.LIQ_NOLIQ,DFD.VALIDACIONGARANTIA,DFD.REMOZADO_REPARADO,DFD.DIASGARANTIA,DFD.FECHAREMOZADOTALLER,DFD.FECDESPACHOALMACEN,DFD.ESTADO,TO_CHAR(DFD.FECHAVALIDACION,'DD/MM/YYYY HH24:MI:SS'),DFD.OBSERVACIONES,DFD.MOTIVORECHAZODEVOLUCION,DFD.OBSERVACIONAPROBADO, "+
        		   "DECODE(FD.ESPEDIDO,'" + ConstantesGenerales.EstadoPedidoActa.NOEXISTEPEDIDO.getTipoValor() + "','" + ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor() + "','" + ConstantesGenerales.EstadoPedidoActa.EXISTEPEDIDO.getTipoValor() + "',DECODE(DETACTA.NROPEDIDO,NULL,'" + ConstantesGenerales.GUION + "',DETACTA.NROPEDIDO),DETACTA.NROPEDIDO), " + 
        		   "DECODE(FD.ESPEDIDO,'" + ConstantesGenerales.EstadoPedidoActa.NOEXISTEPEDIDO.getTipoValor() + "','" + ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor() + "','" + ConstantesGenerales.EstadoPedidoActa.EXISTEPEDIDO.getTipoValor() + "',DECODE(DETACTA.NROPOSICION,NULL,'" + ConstantesGenerales.GUION + "',DETACTA.NROPOSICION),DETACTA.NROPOSICION), " + 
        		   "DECODE(FD.ESPEDIDO,'" + ConstantesGenerales.EstadoPedidoActa.NOEXISTEPEDIDO.getTipoValor() + "','" + ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor() + "','" + ConstantesGenerales.EstadoPedidoActa.EXISTEPEDIDO.getTipoValor() + "',DECODE(DETACTA.NROENTREGA,NULL,'" + ConstantesGenerales.GUION + "',DETACTA.NROENTREGA),DETACTA.NROENTREGA), " + 
        		   "DECODE(FD.ESPEDIDO,'" + ConstantesGenerales.EstadoPedidoActa.NOEXISTEPEDIDO.getTipoValor() + "','" + ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor() + "','" + ConstantesGenerales.EstadoPedidoActa.EXISTEPEDIDO.getTipoValor() + "',DECODE(DETACTA.FECPEDIDOENTREGA,NULL,'" + ConstantesGenerales.GUION + "',DETACTA.FECPEDIDOENTREGA),DETACTA.FECPEDIDOENTREGA), " +  
        		   "DECODE(DFD.ESTADO,'" + ConstantesGenerales.EstadoValidacionActa.RECHAZADO.getTipoValor() + "','" + ConstantesGenerales.GUION + "',FD.ENVIADO),DECODE(DFD.ESTADO,'" + ConstantesGenerales.EstadoValidacionActa.RECHAZADO.getTipoValor() + "','" + ConstantesGenerales.GUION + "',FD.DESTINO),DECODE(DFD.ESTADO,'" + ConstantesGenerales.EstadoValidacionActa.RECHAZADO.getTipoValor() + "','" + ConstantesGenerales.GUION + "',DFD.FECGESTION_DF_PL)," + 
        		   "DECODE(DFD.ESTADO,'" + ConstantesGenerales.EstadoValidacionActa.RECHAZADO.getTipoValor() + "','" + ConstantesGenerales.GUION + "',DFD.PESO),DECODE(DFD.ESTADO,'" + ConstantesGenerales.EstadoValidacionActa.RECHAZADO.getTipoValor() + "','" + ConstantesGenerales.GUION + "',DFD.VOLUNITARIO),DECODE(DFD.ESTADO,'" + ConstantesGenerales.EstadoValidacionActa.RECHAZADO.getTipoValor() + "','" + ConstantesGenerales.GUION + "',DFD.PRECIOEQUIPO),"+
        		   "DECODE(DFD.ESTADO,'" + ConstantesGenerales.EstadoValidacionActa.RECHAZADO.getTipoValor() + "','" + ConstantesGenerales.GUION + "',DFD.MODORECOJO) " +
        		   "FROM UBIGEO UBI,GESTORACTA GESTOR,ENTIDAD ENT,CANAL CA,TIPODEVOLUCION TDEVOL,FICHADEVOLUCION FD,DETALLEFICHADEVOLUCION DFD LEFT OUTER JOIN ACTADEVOLUCION AD ON AD.NROTICKET=DFD.NROTICKET LEFT OUTER JOIN DETALLEACTADEVOLUCION DETACTA ON (AD.NROACTA=DETACTA.NROACTA AND DETACTA.NROSERIE=DFD.SERIE)  " + 
        		   "WHERE FD.CODENTIDAD=ENT.CODENTIDAD AND ENT.CODUBIGEO=UBI.CODUBIGEO AND FD.NROTICKET=DFD.NROTICKET AND ENT.CODCANAL=CA.CODCANAL AND TDEVOL.CODTIPODEVOL=FD.CODTIPODEVOL AND GESTOR.CORREO=FD.CORREO ";

		
		String sqlGeneral="";
		String sqlCamposCondicion="";

		String sqlCamposCondicionTotal="";
		
		if(acta.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " AND UPPER(TRIM(AD.NROACTA))='" + acta.trim().toUpperCase() + "' AND ";
		}
		
		if(nroTicket.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " AND UPPER(TRIM(DFD.NROTICKET)) LIKE '%" + nroTicket.trim().toUpperCase() + "%' AND ";
		}
		
		if(material.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " AND (UPPER(TRIM(DFD.CODMATERIALSAP_4_7))='" + material.trim().toUpperCase() + "' OR UPPER(TRIM(DETFICHA.CODMATERIALSAP_6_0))='" + material.trim().toUpperCase() + "') AND ";
		}
		
		if(serie.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " AND UPPER(TRIM(DFD.SERIE))='" + serie.trim().toUpperCase() + "' AND ";
		}
		
		if(descrip.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " AND (UPPER(TRIM(DFD.DESMATERIALSAP_4_7)) LIKE '%" + descrip.trim().toUpperCase() + "%' OR UPPER(TRIM(DFD.DESMATERIALSAP_6_0)) LIKE '%" + descrip.trim().toUpperCase() + "%') AND";
		}
		
		if(estado.length()>0){
			sqlCamposCondicion=sqlCamposCondicion + " AND UPPER(TRIM(DFD.ESTADO))='" + estado.trim().toUpperCase() + "' AND ";
		}
		
		if(sqlCamposCondicion.length()>0){			
			sqlCamposCondicionTotal=sqlCamposCondicion.substring(0, sqlCamposCondicion.length()-4) + " ORDER BY NROTICKET,NRO"; 		
		}
		
		Connection cn = null;
		sqlGeneral=sql + sqlCamposCondicionTotal;
		
		System.out.println(sqlGeneral);
		
		System.out.println(sqlCamposCondicionTotal);
		
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sqlGeneral);
			while (rs.next()) {
				i++;
				HSSFRow row1 = sheet.createRow((short)i);
				
				row1.createCell((short)0).setCellValue(rs.getString(1));
		        row1.createCell((short)1).setCellValue(rs.getString(2));
		        row1.createCell((short)2).setCellValue(rs.getString(3));
		        row1.createCell((short)3).setCellValue(rs.getString(4));
				row1.createCell((short)4).setCellValue(rs.getString(5));
		        row1.createCell((short)5).setCellValue(rs.getString(6));
		        row1.createCell((short)6).setCellValue(rs.getString(7));
		        row1.createCell((short)7).setCellValue(rs.getString(8));
		        row1.createCell((short)8).setCellValue(rs.getString(9));
		        row1.createCell((short)9).setCellValue(rs.getString(10));
		        row1.createCell((short)10).setCellValue(rs.getString(11));
		        row1.createCell((short)11).setCellValue(rs.getString(12));
		        row1.createCell((short)12).setCellValue(rs.getString(13));
		        row1.createCell((short)13).setCellValue(rs.getString(14));
		        row1.createCell((short)14).setCellValue(rs.getString(15));
		        row1.createCell((short)15).setCellValue(rs.getString(16));
		        row1.createCell((short)16).setCellValue(rs.getString(17));
		        row1.createCell((short)17).setCellValue(rs.getString(18));
		        row1.createCell((short)18).setCellValue(rs.getString(19));
		        row1.createCell((short)19).setCellValue(rs.getString(20));
		        row1.createCell((short)20).setCellValue(rs.getString(21));
		        row1.createCell((short)21).setCellValue(rs.getString(22));
		        row1.createCell((short)22).setCellValue(rs.getString(23));
		        row1.createCell((short)23).setCellValue(rs.getString(24));
		        row1.createCell((short)24).setCellValue(rs.getString(25));
		        row1.createCell((short)25).setCellValue(rs.getString(26));
		        row1.createCell((short)26).setCellValue(rs.getString(27));
		        row1.createCell((short)27).setCellValue(rs.getString(28));
		        row1.createCell((short)28).setCellValue(rs.getString(29));
		        row1.createCell((short)29).setCellValue(rs.getString(30));
		        row1.createCell((short)30).setCellValue(rs.getString(31));
		        row1.createCell((short)31).setCellValue(rs.getString(32));
		        row1.createCell((short)32).setCellValue(rs.getString(33));
		        row1.createCell((short)33).setCellValue(rs.getString(34));
		        row1.createCell((short)34).setCellValue(rs.getString(35));
		        row1.createCell((short)35).setCellValue(rs.getString(36));
		        row1.createCell((short)36).setCellValue(rs.getString(37));
		        row1.createCell((short)37).setCellValue(rs.getString(38));
		        row1.createCell((short)38).setCellValue(rs.getString(39));
		        row1.createCell((short)39).setCellValue(rs.getString(40));
		        row1.createCell((short)40).setCellValue(rs.getString(41));
		        row1.createCell((short)41).setCellValue(rs.getString(42));
		        row1.createCell((short)42).setCellValue(rs.getString(43));
		        row1.createCell((short)43).setCellValue(rs.getString(44));
		        row1.createCell((short)44).setCellValue(rs.getString(45));
		        row1.createCell((short)45).setCellValue(rs.getString(46));
		        row1.createCell((short)46).setCellValue(rs.getString(47));
		        row1.createCell((short)47).setCellValue(rs.getString(48));
		        row1.createCell((short)48).setCellValue(rs.getString(49));
		        row1.createCell((short)49).setCellValue(rs.getString(50));
		        row1.createCell((short)50).setCellValue(rs.getString(51));
		        row1.createCell((short)51).setCellValue(rs.getString(52));
		        //DATOS SAP 
		        row1.createCell((short)52).setCellValue(rs.getString(53));
		        row1.createCell((short)53).setCellValue(rs.getString(54));
		        row1.createCell((short)54).setCellValue(rs.getString(55));
		        row1.createCell((short)55).setCellValue(rs.getString(56));
		        row1.createCell((short)56).setCellValue(rs.getString(57));
		        row1.createCell((short)57).setCellValue(rs.getString(58));
		        row1.createCell((short)58).setCellValue(rs.getString(59));
		        row1.createCell((short)59).setCellValue(rs.getString(60));
		        row1.createCell((short)60).setCellValue(rs.getString(61));
		        row1.createCell((short)61).setCellValue(rs.getString(62));
		        row1.createCell((short)62).setCellValue(rs.getString(63));
		        //
		        row1.createCell((short)63).setCellValue(rs.getString(64));
		        row1.createCell((short)64).setCellValue(rs.getString(65));
		        row1.createCell((short)65).setCellValue(rs.getString(66));
		        row1.createCell((short)66).setCellValue(rs.getString(67));
		        row1.createCell((short)67).setCellValue(rs.getString(68));
		        if(rs.getString(69)!=null){//Dias de Garantia
		        	row1.createCell((short)68).setCellValue(rs.getString(69));
		        }else{
		        	row1.createCell((short)68).setCellValue(ConstantesGenerales.GUION);
		        }		        
		        
		        row1.createCell((short)69).setCellValue(rs.getString(70));
		        row1.createCell((short)70).setCellValue(rs.getString(71));
		        row1.createCell((short)71).setCellValue(rs.getString(72));
		        row1.createCell((short)72).setCellValue(rs.getString(73));
		        row1.createCell((short)73).setCellValue(rs.getString(74));
		        row1.createCell((short)74).setCellValue(rs.getString(75));
		        row1.createCell((short)75).setCellValue(rs.getString(76));
		        row1.createCell((short)76).setCellValue(rs.getString(77));
		        row1.createCell((short)77).setCellValue(rs.getString(78));
		        row1.createCell((short)78).setCellValue(rs.getString(79));
		        row1.createCell((short)79).setCellValue(rs.getString(80));
		        row1.createCell((short)80).setCellValue(rs.getString(81));
		        row1.createCell((short)81).setCellValue(rs.getString(82));
		        row1.createCell((short)82).setCellValue(rs.getString(83));
		        row1.createCell((short)83).setCellValue(rs.getString(84));
		        row1.createCell((short)84).setCellValue(rs.getString(85));
		        row1.createCell((short)85).setCellValue(rs.getString(86));
		        row1.createCell((short)86).setCellValue(rs.getString(87));

			}
			for(int k=0;k<87;k++){
				sheet.autoSizeColumn(k);
			}
		} catch (SQLException ex) {
			System.out.println("Se suscito la siguiente Excepcion:" + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());
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
	public boolean esProvinciaActa(String nroTicket) {
		boolean esProvincia=false;
		
		String sql="SELECT UBI.ZONAL FROM FICHADEVOLUCION FDEVOL,ENTIDAD ENT,UBIGEO UBI " + 
				   "WHERE FDEVOL.CODENTIDAD=ENT.CODENTIDAD AND ENT.CODUBIGEO=UBI.CODUBIGEO AND FDEVOL.NROTICKET='" +  nroTicket + "'";
		
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				esProvincia=true;
			}
		} catch (SQLException ex) {
			esProvincia=false;
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
			}
		}
		return esProvincia;
		
	}

	@Override
	public List<ActaDevolucionBean> lstActaDevolucionPedidoPendiente(String formato) {
		List<ActaDevolucionBean> lstActaDevolucion = null;
		lstActaDevolucion=new ArrayList<ActaDevolucionBean>();
		String sql="SELECT ADEVOL.NROACTA,ADEVOL.NROTICKET,ENT.NOMENTIDAD,TPFOR.NOMFORM,TPDEVOL.NOMTIPODEVOL,ADEVOL.CANTIDAD " +  
				   "FROM ACTADEVOLUCION ADEVOL,FICHADEVOLUCION FDEVOL,TIPO_FORMATO TPFOR,TIPODEVOLUCION TPDEVOL,ENTIDAD ENT " + 
				   "WHERE FDEVOL.CODTIPODEVOL=TPDEVOL.CODTIPODEVOL AND TPFOR.CODFORM=TPDEVOL.CODFORM AND ADEVOL.NROTICKET=FDEVOL.NROTICKET " + 
				   "AND FDEVOL.CODENTIDAD=ENT.CODENTIDAD AND TPFOR.CODFORM='" + formato + "' AND FDEVOL.ESPEDIDO='" + ConstantesGenerales.EstadoPedidoActa.EXISTEPEDIDO.getTipoValor() + "' " + 
				   "AND FDEVOL.CONPEDIDO='" + ConstantesGenerales.EstadoPedidoActa.SINPEDIDO.getTipoValor() + "'";

		System.out.println("sql:" + sql);
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			lstActaDevolucion = new ArrayList<ActaDevolucionBean>();
			while (rs.next()) {
				ActaDevolucionBean actaDevolucionBean=new ActaDevolucionBean();				
				actaDevolucionBean.setNroActa(rs.getString(1));
				actaDevolucionBean.setNroTicket(rs.getString(2));
				actaDevolucionBean.setEntidad(rs.getString(3));
				actaDevolucionBean.setTipoFormato(rs.getString(4));
				actaDevolucionBean.setTipoDevolucion(rs.getString(5));
				actaDevolucionBean.setCantItems(rs.getInt(6));
				
				lstActaDevolucion.add(actaDevolucionBean);
			}
		} catch (SQLException ex) {
			lstActaDevolucion = null;
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
			}
		}
		
		return lstActaDevolucion;
	}
	
	@Override
	public List<ActaDevolucionBean> lstActaDevolucionRecojoPendiente(String formato) {
		List<ActaDevolucionBean> lstActaDevolucion = null;
		lstActaDevolucion=new ArrayList<ActaDevolucionBean>();
		String sql="SELECT ADEVOL.NROACTA,ADEVOL.NROTICKET,ENT.NOMENTIDAD,TPFOR.NOMFORM,TPDEVOL.NOMTIPODEVOL,ADEVOL.CANTIDAD " +  
				   "FROM ACTADEVOLUCION ADEVOL,FICHADEVOLUCION FDEVOL,TIPO_FORMATO TPFOR,TIPODEVOLUCION TPDEVOL,ENTIDAD ENT " + 
				   "WHERE FDEVOL.CODTIPODEVOL=TPDEVOL.CODTIPODEVOL AND TPFOR.CODFORM=TPDEVOL.CODFORM AND ADEVOL.NROTICKET=FDEVOL.NROTICKET " + 
				   "AND FDEVOL.CODENTIDAD=ENT.CODENTIDAD AND TPFOR.CODFORM='" + formato + "' AND ADEVOL.INDRECOJO='" + ConstantesGenerales.IndicadorModoRecojo.NO.getTipoValor() + "' ";

		System.out.println("sql Recojo:" + sql);
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			lstActaDevolucion = new ArrayList<ActaDevolucionBean>();
			while (rs.next()) {
				ActaDevolucionBean actaDevolucionBean=new ActaDevolucionBean();				
				actaDevolucionBean.setNroActa(rs.getString(1));
				actaDevolucionBean.setNroTicket(rs.getString(2));
				actaDevolucionBean.setEntidad(rs.getString(3));
				actaDevolucionBean.setTipoFormato(rs.getString(4));
				actaDevolucionBean.setTipoDevolucion(rs.getString(5));
				actaDevolucionBean.setCantItems(rs.getInt(6));
				
				lstActaDevolucion.add(actaDevolucionBean);
			}
		} catch (SQLException ex) {
			lstActaDevolucion = null;
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
			}
		}
		
		return lstActaDevolucion;
	}

	@Override
	public List<ActaDevolucionBean> lstActaDevolucionFechas(String formato,String f1, String f2) {
		List<ActaDevolucionBean> list = null;
		
		String sql="SELECT ADEVOL.NROACTA,ADEVOL.NROTICKET,ENT.NOMENTIDAD,TPFOR.NOMFORM,TPDEVOL.NOMTIPODEVOL,ADEVOL.CANTIDAD " +  
				   "FROM ACTADEVOLUCION ADEVOL,FICHADEVOLUCION FDEVOL,TIPO_FORMATO TPFOR,TIPODEVOLUCION TPDEVOL,ENTIDAD ENT " + 
				   "WHERE FDEVOL.CODTIPODEVOL=TPDEVOL.CODTIPODEVOL AND TPFOR.CODFORM=TPDEVOL.CODFORM AND ADEVOL.NROTICKET=FDEVOL.NROTICKET AND FDEVOL.CODENTIDAD=ENT.CODENTIDAD AND TPFOR.CODFORM='" + formato + "' " + 
				   "AND TO_DATE(FDEVOL.FECHACARGA,'DD/MM/YY') BETWEEN TO_DATE('" + f1 + "','DD/MM/YY') AND TO_DATE('" + f2 + "','DD/MM/YY') ORDER BY FDEVOL.NROTICKET DESC";
		
		System.out.println(sql);
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			list = new ArrayList<ActaDevolucionBean>();
			while (rs.next()) {
				ActaDevolucionBean actaDevolucionBean=new ActaDevolucionBean();
								
				actaDevolucionBean.setNroActa(rs.getString(1));
				actaDevolucionBean.setNroTicket(rs.getString(2));
				actaDevolucionBean.setEntidad(rs.getString(3));
				actaDevolucionBean.setTipoFormato(rs.getString(4));
				actaDevolucionBean.setTipoDevolucion(rs.getString(5));
				actaDevolucionBean.setCantItems(rs.getInt(6));
				
				list.add(actaDevolucionBean);
			}
		} catch (SQLException ex) {
			list = null;
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	@Override
	public String obtenerRubroFichaDevolucion(String nroTicket) {
		String sql="SELECT RUBRO FROM DETALLEFICHADEVOLUCION WHERE NROTICKET='" + nroTicket + "'";
		String rubro="";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				rubro=rs.getString(1);
			}
		} catch (SQLException ex) {
			rubro="";
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
			}
		}
		return rubro;
	}

	@Override
	public void asignarFechaRecojo(String nroTickets,String fecha) {
		Connection cn = null;
		PreparedStatement pstmtActa = null;
		PreparedStatement pstmtDetalleActa = null;
		try {
			cn = dataSource.getConnection();
			String sqlUpdateActa="UPDATE ACTADEVOLUCION SET INDRECOJO ='" + ConstantesGenerales.IndicadorModoRecojo.SI.getTipoValor() + "' WHERE NROTICKET IN(" + nroTickets + ")";
			
			String sqlUpdateDetalle="UPDATE DETALLEFICHADEVOLUCION SET FECGESTION_DF_PL='" + fecha + "' WHERE ESTADO='" + ConstantesGenerales.EstadoValidacionActa.APROBADO.getTipoValor() + "' AND " + 
						     "NROTICKET IN(" + nroTickets + ")";
			cn.setAutoCommit(false);
			pstmtActa=cn.prepareStatement(sqlUpdateActa);
			pstmtActa.executeUpdate();
			cn.commit();
			pstmtDetalleActa=cn.prepareStatement(sqlUpdateDetalle);
			pstmtDetalleActa.executeUpdate();
			cn.commit();
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
	public void asignarPedidoEntregaNoSeriado(List<PedidoEntregaBean> lstPedidoEntrega) {
		String sqlDetaActa="";
		String sqlFicha="";
		Connection cn = null;
		PreparedStatement pstmt = null;
		try {
			cn=dataSource.getConnection();	
			for(int i=0;i<lstPedidoEntrega.size();i++){				
				sqlDetaActa="UPDATE DETALLEACTADEVOLUCION DAD SET " + 
							"DAD.NROPEDIDO='" + lstPedidoEntrega.get(i).getPedido() + "'," +  
							"DAD.NROPOSICION='" + lstPedidoEntrega.get(i).getPosicion() + "'," + 
							"DAD.NROENTREGA='" + lstPedidoEntrega.get(i).getEntrega() + "', " +		
							"DAD.FECPEDIDOENTREGA='" + lstPedidoEntrega.get(i).getFecCreacionVale() + "' " +	
							"WHERE (DAD.NROTICKET||DAD.NROSERIE||DAD.CODMATERIAL) IN (SELECT DFD.NROTICKET||DFD.SERIE||DFD.CODSAP FROM DETALLEFICHADEVOLUCION DFD WHERE " + 
							"(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.CODMATERIALSAP_4_7 ELSE DFD.CODMATERIALSAP_6_0 END)='" + lstPedidoEntrega.get(i).getCodMaterial() +  "' AND " + 
							"(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.DESMATERIALSAP_4_7 ELSE DFD.DESMATERIALSAP_6_0 END)='" + lstPedidoEntrega.get(i).getDesMaterial() + "' AND " + 
							"(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.CENTROSAP_4_7 ELSE DFD.CENTROSAP_6_0 END)='" + lstPedidoEntrega.get(i).getCentro() + "' AND " +
							"(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.ALMACENSAP_4_7 ELSE DFD.ALMACENSAP_6_0 END)='" + lstPedidoEntrega.get(i).getAlmacen() + "' AND " + 
							"(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.LOTESAP_4_7 ELSE DFD.LOTESAP_6_0 END)='" + lstPedidoEntrega.get(i).getLote() +  "' AND DFD.NROTICKET='" + lstPedidoEntrega.get(i).getNroTicket() +"')";

				cn.setAutoCommit(false);
				pstmt=cn.prepareStatement(sqlDetaActa);
				pstmt.executeUpdate();
				cn.commit();
			}
			
			sqlFicha="UPDATE FICHADEVOLUCION FDEVOL SET " + 
					 "FDEVOL.CONPEDIDO='" + ConstantesGenerales.EstadoPedidoActa.CONPEDIDO.getTipoValor() + "' " + 
					 "WHERE FDEVOL.NROTICKET='" +lstPedidoEntrega.get(0).getNroTicket() + "'";

			cn.setAutoCommit(false);
			pstmt=cn.prepareStatement(sqlFicha);
			pstmt.executeUpdate();
			cn.commit();
		} catch (SQLException e) {
			System.out.println("Se suscito la siguiente Excepcion:" +  e.getMessage());			
			pstmt=null;
		}finally{
			try{
				cn.close();
				pstmt=null;
			}catch(Exception e){
				System.out.println("Se suscito la siguiente Excepcion:" +  e.getMessage());	
			}
		}	
		
	}


	
	
}
