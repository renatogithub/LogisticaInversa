package pe.tgestiona.logistica.inversa.dao.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

import pe.tgestiona.logistica.inversa.bean.ConfiguracionBean;
import pe.tgestiona.logistica.inversa.bean.ConstantesGenerales;
import pe.tgestiona.logistica.inversa.bean.FichaDevolucionBean;
import pe.tgestiona.logistica.inversa.dao.FichaDevolucionDao;
import pe.tgestiona.logistica.inversa.util.Util;

@Repository
public class FichaDevolucionDaoImpl implements FichaDevolucionDao {

	@Autowired
	private DriverManagerDataSource dataSource;
	@Autowired
	private ValidacionDaoImpl validacionDaoImpl;

	@Override
	public String grabarCabeceraFichaDevolucion(int cantReg,FichaDevolucionBean fichaDevolucionBean) {
		Util util=new Util();
		String cantMax="";
		String formatoTicket=null;
		String fechaTicket=util.obtenerFechaTicket();
		String sql="select count(nroticket)+1 from fichadevolucion where to_char(fechacarga,'dd/mm/yyyy')=to_char(sysdate,'dd/mm/yyyy')";
		String idPedEntr="";
		String motivo=fichaDevolucionBean.getTipoDevolucion();
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			cn.setAutoCommit(false);
	        PreparedStatement pstmt = null;

			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			List<ConfiguracionBean> lstConfPedido=validacionDaoImpl.obtenerConfiguraciones(motivo, ConstantesGenerales.TipoConfiguracion.PEDIDOENTREGA.getTipoValor());
			if(lstConfPedido.size()>0){
				if(lstConfPedido.get(0).getExistencia().equals(ConstantesGenerales.SI)){
					idPedEntr=ConstantesGenerales.EstadoPedidoActa.EXISTEPEDIDO.getTipoValor();					
				}
				if(lstConfPedido.get(0).getExistencia().equals(ConstantesGenerales.NO)){
					idPedEntr=ConstantesGenerales.EstadoPedidoActa.NOEXISTEPEDIDO.getTipoValor();					
				}
			}else{
				idPedEntr=ConstantesGenerales.EstadoPedidoActa.NOEXISTEPEDIDO.getTipoValor();
			}

			if(rs.next()){
				cantMax=(rs.getInt(1)>9)?String.valueOf(rs.getInt(1)):"0"+String.valueOf(rs.getInt(1));
			}
			
			formatoTicket=fechaTicket+"-"+cantMax;
			
			sql="insert into fichadevolucion(NroTicket,FechaCarga,CodUsuario,CodTipoDevol,CodEntidad,cantitem,FechaSolicitud,Nombre,MesGestion,Estado,Enviado,Destino,correo,esPedido,conPedido,prefijoActa,mesDevolucion,fechaDevolucion) values ('" + 
											formatoTicket + "',TO_DATE('"+ util.obtenerFecha() + " " + util.obtenerHora() + "','DD/MM/YYYY HH24:MI:SS'),'" + 
											fichaDevolucionBean.getUsuario() + "','" + fichaDevolucionBean.getTipoDevolucion() + "','" + fichaDevolucionBean.getEntidad() + "'," +           
											cantReg + ",to_date('" + fichaDevolucionBean.getFechaSolicitud() + " " + fichaDevolucionBean.getHoraSolicitud() + "','DD/MM/YYYY HH24:MI:SS'),'" +      
											fichaDevolucionBean.getNombre() + "','" + util.obtenerMesLetras() + "','" +  ConstantesGenerales.EstadoValidacionActa.SINPROCESAR.getTipoValor() + "','" + 
											fichaDevolucionBean.getEnviado() + "','" + fichaDevolucionBean.getDestino() + "','" + fichaDevolucionBean.getGestorActa()+ "','" + idPedEntr + "','" + ConstantesGenerales.EstadoPedidoActa.SINPEDIDO.getTipoValor() + "','" + fichaDevolucionBean.getPrefijoActa() + "','" + fichaDevolucionBean.getMesDevolucion() + "','" + fichaDevolucionBean.getFechaDevolucion() + "')";
			
            pstmt = cn.prepareStatement(sql);
            pstmt.executeUpdate();
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
		return formatoTicket;

	}

	
	@Override
	public List<FichaDevolucionBean> listTicketSinProcesar(String formato,String usuario) {
		List<FichaDevolucionBean> list = null;

		String sql = "SELECT FD.NROTICKET,ENT.NOMENTIDAD,TPFOR.NOMFORM,TPDEVOL.CODTIPODEVOL,TPDEVOL.NOMTIPODEVOL,TO_CHAR(FD.FECHACARGA,'DD/MM/YYYY HH24:MI:SS'),FD.CANTITEM FROM FICHADEVOLUCION FD, ENTIDAD ENT, TIPODEVOLUCION TPDEVOL,TIPO_FORMATO TPFOR " + 
					 "WHERE FD.ESTADO='" + ConstantesGenerales.EstadoValidacionActa.SINPROCESAR.getTipoValor() + "' AND FD.CODENTIDAD=ENT.CODENTIDAD AND TPDEVOL.CODTIPODEVOL=FD.CODTIPODEVOL AND TPFOR.CODFORM=TPDEVOL.CODFORM AND TPFOR.CODFORM='" + formato + "' AND FD.CODUSUARIO='" + usuario + "' " +  
					 "AND TO_DATE(FD.FECHACARGA,'DD/MM/YY')=TO_DATE(SYSDATE,'DD/MM/YY') ORDER BY FD.NROTICKET DESC";		
		
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			list = new ArrayList<FichaDevolucionBean>();
			while (rs.next()) {
				FichaDevolucionBean fichaDevolucionBean=new FichaDevolucionBean();
				
				fichaDevolucionBean.setNroTicket(rs.getString(1));
				fichaDevolucionBean.setEntidad(rs.getString(2));
				fichaDevolucionBean.setTipoFormato(rs.getString(3));
				fichaDevolucionBean.setCodTipoDevolucion(rs.getString(4));
				fichaDevolucionBean.setTipoDevolucion(rs.getString(5));
				fichaDevolucionBean.setFechaCarga(rs.getString(6));
				fichaDevolucionBean.setCantidadItem(rs.getInt(7));
				
				list.add(fichaDevolucionBean);
			}
		} catch (SQLException ex) {
			list = null;
			 System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				 System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		return list;
	}

	@Override
	public List<FichaDevolucionBean> listTicketSinProcesarFechas(String formato, String f1, String f2,String usuario) {
		List<FichaDevolucionBean> list = null;
		String sql = "SELECT FD.NROTICKET,ENT.NOMENTIDAD,TPFOR.NOMFORM,TPDEVOL.CODTIPODEVOL,TPDEVOL.NOMTIPODEVOL,TO_CHAR(FD.FECHACARGA,'DD/MM/YYYY HH24:MI:SS'),FD.CANTITEM FROM FICHADEVOLUCION FD, ENTIDAD ENT, TIPODEVOLUCION TPDEVOL,TIPO_FORMATO TPFOR " + 
					 "WHERE FD.ESTADO='" + ConstantesGenerales.EstadoValidacionActa.SINPROCESAR.getTipoValor() + "' AND FD.CODENTIDAD=ENT.CODENTIDAD AND TPDEVOL.CODTIPODEVOL=FD.CODTIPODEVOL AND TPFOR.CODFORM=TPDEVOL.CODFORM AND TPFOR.CODFORM='" + formato + "' AND FD.CODUSUARIO='" + usuario + "' " + 			
					 "AND TO_DATE(FD.FECHACARGA,'DD/MM/YY') BETWEEN TO_DATE('" + f1 + "','DD/MM/YY') AND TO_DATE('" + f2 + "','DD/MM/YY')  ORDER BY FD.NROTICKET DESC";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			list = new ArrayList<FichaDevolucionBean>();
			while (rs.next()) {
				FichaDevolucionBean fichaDevolucionBean=new FichaDevolucionBean();
				
				fichaDevolucionBean.setNroTicket(rs.getString(1));
				fichaDevolucionBean.setEntidad(rs.getString(2));
				fichaDevolucionBean.setTipoFormato(rs.getString(3));
				fichaDevolucionBean.setCodTipoDevolucion(rs.getString(4));
				fichaDevolucionBean.setTipoDevolucion(rs.getString(5));
				fichaDevolucionBean.setFechaCarga(rs.getString(6));
				fichaDevolucionBean.setCantidadItem(rs.getInt(7));
				
				list.add(fichaDevolucionBean);
			}
		} catch (SQLException ex) {
			list = null;
			 System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				 System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		return list;
	}

	@Override
	public List<FichaDevolucionBean> listTicketPendiente(String formato,String usuario) {
		List<FichaDevolucionBean> list = null;
		
		String sql = "SELECT FD.NROTICKET,ENT.NOMENTIDAD,TPFOR.NOMFORM,TPDEVOL.CODTIPODEVOL,TPDEVOL.NOMTIPODEVOL,TO_CHAR(FD.FECHACARGA,'DD/MM/YYYY HH24:MI:SS'),FD.CANTITEM  " + 
					 "FROM FICHADEVOLUCION FD, ENTIDAD ENT, TIPODEVOLUCION TPDEVOL,TIPO_FORMATO TPFOR " + 
					 "WHERE FD.CODENTIDAD=ENT.CODENTIDAD AND TPDEVOL.CODTIPODEVOL=FD.CODTIPODEVOL AND TPFOR.CODFORM=TPDEVOL.CODFORM AND FD.ESTADO='" + ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor() + "' AND TPFOR.CODFORM='" + formato + "'  AND FD.CODUSUARIO='" + usuario + "' " + 
					 "AND TO_DATE(FD.FECHACARGA,'DD/MM/YY')=TO_DATE(SYSDATE,'DD/MM/YY')  ORDER BY FD.NROTICKET DESC";
		
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			list = new ArrayList<FichaDevolucionBean>();
			while (rs.next()) {
				FichaDevolucionBean fichaDevolucionBean = new FichaDevolucionBean();
				fichaDevolucionBean.setNroTicket(rs.getString(1));
				fichaDevolucionBean.setEntidad(rs.getString(2));
				fichaDevolucionBean.setTipoFormato(rs.getString(3));
				fichaDevolucionBean.setCodTipoDevolucion(rs.getString(4));
				fichaDevolucionBean.setTipoDevolucion(rs.getString(5));
				fichaDevolucionBean.setFechaCarga(rs.getString(6));
				fichaDevolucionBean.setCantidadItem(rs.getInt(7));
								
				list.add(fichaDevolucionBean);
			}
		} catch (SQLException ex) {
			list = null;
			 System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				 System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		return list;
	}
	
	@Override
	public List<FichaDevolucionBean> listTicketPendienteFechas(String formato,String f1, String f2,String usuario) {
		List<FichaDevolucionBean> list = null;
		
		String sql = "SELECT FD.NROTICKET,ENT.NOMENTIDAD,TPFOR.NOMFORM,TPDEVOL.CODTIPODEVOL,TPDEVOL.NOMTIPODEVOL,TO_CHAR(FD.FECHACARGA,'DD/MM/YYYY HH24:MI:SS'),FD.CANTITEM " + 
					 "FROM FICHADEVOLUCION FD, ENTIDAD ENT, TIPODEVOLUCION TPDEVOL,TIPO_FORMATO TPFOR " + 
					 "WHERE FD.CODENTIDAD=ENT.CODENTIDAD AND TPDEVOL.CODTIPODEVOL=FD.CODTIPODEVOL AND TPFOR.CODFORM=TPDEVOL.CODFORM AND FD.ESTADO='" + ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor() + "' AND TPFOR.CODFORM='" + formato + "' AND FD.CODUSUARIO='" + usuario + "' " + 		
					 "AND TO_DATE(FD.FECHACARGA,'DD/MM/YY') BETWEEN TO_DATE('" + f1 + "','DD/MM/YY') AND TO_DATE('" + f2 + "','DD/MM/YY') ORDER BY FD.NROTICKET DESC";		
		
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			list = new ArrayList<FichaDevolucionBean>();
			while (rs.next()) {
				FichaDevolucionBean fichaDevolucionBean=new FichaDevolucionBean();
				
				fichaDevolucionBean.setNroTicket(rs.getString(1));
				fichaDevolucionBean.setEntidad(rs.getString(2));
				fichaDevolucionBean.setTipoFormato(rs.getString(3));
				fichaDevolucionBean.setCodTipoDevolucion(rs.getString(4));
				fichaDevolucionBean.setTipoDevolucion(rs.getString(5));
				fichaDevolucionBean.setFechaCarga(rs.getString(6));
				fichaDevolucionBean.setCantidadItem(rs.getInt(7));
				
				list.add(fichaDevolucionBean);
			}
		} catch (SQLException ex) {
			list = null;
			 System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				 System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		return list;
	}
	
	@Override
	public List<FichaDevolucionBean> listFichaDevolucion(String[] tickets) {
		List<FichaDevolucionBean> list = null;
		
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = null;
			String sql = "";
			list = new ArrayList<FichaDevolucionBean>();
			
			for(int i=0;i<tickets.length;i++){
				sql="SELECT FD.NROTICKET,FD.CODTIPODEVOL,FD.CODENTIDAD,FD.ESTADO,FD.ESPEDIDO,UBI.ZONAL,TPFORMATO.CODFORM,FD.ENVIADO,TO_CHAR(FD.FECHASOLICITUD,'DD/MM/YYYY'),FD.FECHADEVOLUCION,FD.MESDEVOLUCION " + 
					"FROM FICHADEVOLUCION FD, UBIGEO UBI, ENTIDAD ENT,TIPODEVOLUCION TPDEVOL,TIPO_FORMATO TPFORMATO " +
					"WHERE FD.CODENTIDAD=ENT.CODENTIDAD AND UBI.CODUBIGEO=ENT.CODUBIGEO AND FD.CODTIPODEVOL=TPDEVOL.CODTIPODEVOL AND TPFORMATO.CODFORM=TPDEVOL.CODFORM AND FD.NROTICKET=" + tickets[i];
				rs=st.executeQuery(sql);
				while (rs.next()) {
					FichaDevolucionBean fichaDevolucionBean=new FichaDevolucionBean();
					
					fichaDevolucionBean.setNroTicket(rs.getString(1));
					fichaDevolucionBean.setTipoDevolucion(rs.getString(2));
					fichaDevolucionBean.setEntidad(rs.getString(3));
					fichaDevolucionBean.setEstado(rs.getString(4));
					fichaDevolucionBean.setEsPedido(rs.getString(5));
					fichaDevolucionBean.setZonal(rs.getString(6));
					fichaDevolucionBean.setTipoFormato(rs.getString(7));
					fichaDevolucionBean.setEnviado(rs.getString(8));
					fichaDevolucionBean.setFechaSolicitud(rs.getString(9));
					fichaDevolucionBean.setFechaDevolucion(rs.getString(10));
					fichaDevolucionBean.setMesDevolucion(rs.getString(11));
					
					list.add(fichaDevolucionBean);
				}
			}
		} catch (SQLException ex) {
			list = null;
			System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				 System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		return list;
	}

	@Override
	public void anularFichaDevolucion(String ticket, String observacion) {
		Connection cn = null;
		PreparedStatement pstmt = null;
		try {
			cn = dataSource.getConnection();
			String sqlUpdate="UPDATE FICHADEVOLUCION SET ESTADO='" + ConstantesGenerales.EstadoValidacionActa.ANULADO.getTipoValor() + "', OBSERVACIONANULADO='" + observacion + "' " + 
						     "WHERE NROTICKET='" + ticket + "'";
			cn.setAutoCommit(false);
			pstmt=cn.prepareStatement(sqlUpdate);
			pstmt.executeUpdate();
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
	public void actualizarFichaDevolucion(List<FichaDevolucionBean> lista) {
		String sql="";
		String sqlActualizar="";
		String estado="";
		Connection cn = null;
		PreparedStatement pstmt = null;
		Util util=new Util();		
		int contAprobado=0;
		int contRechazado=0;
		int contPendiente=0;
		int cantidadItems=0;

		try {
			cn=dataSource.getConnection();	
			Statement st = cn.createStatement();
			ResultSet rs= null;
			for(int i=0;i<lista.size();i++){
				contAprobado=0;
				contRechazado=0;
				contPendiente=0;
				cantidadItems=0;
				estado=lista.get(i).getEstado();
				sql="SELECT ESTADO FROM DETALLEFICHADEVOLUCION WHERE NROTICKET='" +  lista.get(i).getNroTicket() + "'";
				rs=st.executeQuery(sql);

				while(rs.next()){
					cantidadItems=cantidadItems+1;
					
					if(rs.getString(1)==null){
						estado="";
					}else{
						estado=rs.getString(1);
					}
					
					if(estado.equals(ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor())){
						contPendiente=contPendiente+1;
					}

					if(estado.equals(ConstantesGenerales.EstadoValidacionActa.APROBADO.getTipoValor())){
						contAprobado=contAprobado+1;	
					}
					
					if(estado.equals(ConstantesGenerales.EstadoValidacionActa.RECHAZADO.getTipoValor())){
						contRechazado=contRechazado+1;	
					}
					
				}
				
				if(contPendiente>0){
					sqlActualizar="UPDATE FICHADEVOLUCION SET ESTADO='" + ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor() + "',FECHAPROCESO=TO_DATE('" + util.obtenerFecha() + " " + util.obtenerHora() + "','DD/MM/YYYY HH24:MI:SS') WHERE NROTICKET='" + lista.get(i).getNroTicket() + "'";					
				}
				
				if(contAprobado==cantidadItems){
					sqlActualizar="UPDATE FICHADEVOLUCION SET ESTADO='" + ConstantesGenerales.EstadoValidacionActa.APROBADO.getTipoValor() + "',FECHAPROCESO=TO_DATE('" + util.obtenerFecha() + " " + util.obtenerHora() + "','DD/MM/YYYY HH24:MI:SS') WHERE NROTICKET='" + lista.get(i).getNroTicket() + "'";									
				}
				
				if(contRechazado==cantidadItems){
					sqlActualizar="UPDATE FICHADEVOLUCION SET ESTADO='" + ConstantesGenerales.EstadoValidacionActa.RECHAZADO.getTipoValor() + "',FECHAPROCESO=TO_DATE('" + util.obtenerFecha() + " " + util.obtenerHora() + "','DD/MM/YYYY HH24:MI:SS') WHERE NROTICKET='" + lista.get(i).getNroTicket() + "'";										
				}
				
				
				if(contPendiente==0){
					if(contAprobado>0 && contRechazado>0){
						sqlActualizar="UPDATE FICHADEVOLUCION SET ESTADO='" + ConstantesGenerales.EstadoValidacionActa.PROCESADO.getTipoValor() + "',FECHAPROCESO=TO_DATE('" + util.obtenerFecha() + " " + util.obtenerHora() + "','DD/MM/YYYY HH24:MI:SS') WHERE NROTICKET='" + lista.get(i).getNroTicket() + "'";					
					}
				}
				
				cn.setAutoCommit(false);
				pstmt=cn.prepareStatement(sqlActualizar);
				pstmt.executeUpdate();
				cn.commit();
			}

		} catch (SQLException e) {
			 System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
		}finally{
			try{
				cn.close();
			}catch(Exception e){
				 System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
	}
	
	
	@Override
	public byte[] descargarSeries(String ticket) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();

		int i=0;
		
		HSSFRow row = sheet.createRow((short)0);
	    
		HSSFCell celda1=row.createCell(0);
		HSSFCell celda2=row.createCell(1);
		
		celda1.setCellValue(new HSSFRichTextString("N° Ticket"));
		celda2.setCellValue(new HSSFRichTextString("Serie"));
		
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
        
		String sql="SELECT DFD.NROTICKET,DFD.SERIE FROM DETALLEFICHADEVOLUCION DFD WHERE DFD.NROTICKET IN (" + ticket + ") AND DFD.SERIE NOT IN ('" + ConstantesGenerales.SINSERIE + "') ORDER BY DFD.NROTICKET DESC";
		
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
			}
			
			for(int k=0;k<2;k++){
				sheet.autoSizeColumn(k);
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
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
	    try {
	    	workbook.write(outByteStream);
		} catch (IOException e) {
			 System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
		}
	    byte [] outArray = outByteStream.toByteArray();
		return outArray;
	}



	@Override
	public FichaDevolucionBean obtenerFichaDevolucionAnular(String nroTicket) {
		FichaDevolucionBean fichaDevolucionBean = null;
		String sql="SELECT FICHADEVOL.NROTICKET,TO_CHAR(FICHADEVOL.FECHACARGA,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(FICHADEVOL.FECHASOLICITUD,'DD/MM/YYYY HH24:MI:SS'),CAN.NOMCANAL,ENT.NOMENTIDAD, " + 
				   "TPFORM.NOMFORM,TPDEVOL.NOMTIPODEVOL,FICHADEVOL.CANTITEM,FICHADEVOL.ESTADO,FICHADEVOL.ENVIADO,FICHADEVOL.DESTINO " + 
				   "FROM FICHADEVOLUCION FICHADEVOL,ENTIDAD ENT,TIPODEVOLUCION TPDEVOL, CANAL CAN, TIPO_FORMATO TPFORM " + 
				   "WHERE FICHADEVOL.CODENTIDAD=ENT.CODENTIDAD AND FICHADEVOL.CODTIPODEVOL=TPDEVOL.CODTIPODEVOL AND " + 
				   "ENT.CODCANAL=CAN.CODCANAL AND TPFORM.CODFORM=TPDEVOL.CODFORM AND FICHADEVOL.NROTICKET='" + nroTicket + "'";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			fichaDevolucionBean = new FichaDevolucionBean();
			if (rs.next()) {
				fichaDevolucionBean.setNroTicket(rs.getString(1));
				fichaDevolucionBean.setFechaCarga(rs.getString(2));
				fichaDevolucionBean.setFechaSolicitud(rs.getString(3));
				fichaDevolucionBean.setCanal(rs.getString(4));
				fichaDevolucionBean.setEntidad(rs.getString(5));
				fichaDevolucionBean.setTipoFormato(rs.getString(6));
				fichaDevolucionBean.setTipoDevolucion(rs.getString(7));
				fichaDevolucionBean.setCantidadItem(rs.getInt(8));
				fichaDevolucionBean.setEstado(rs.getString(9));
				fichaDevolucionBean.setEnviado(rs.getString(10));
				fichaDevolucionBean.setDestino(rs.getString(11));
			}
		} catch (SQLException ex) {
			fichaDevolucionBean = null;
			 System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				 System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		return fichaDevolucionBean;
	}


	@Override
	public byte[] descargarPlantillaxls() {
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

		
		celda1.setCellValue(new HSSFRichTextString("Serie"));
		celda2.setCellValue(new HSSFRichTextString("Descripcion del Modelo"));
		celda3.setCellValue(new HSSFRichTextString("Codigo SAP"));
		celda4.setCellValue(new HSSFRichTextString("Tipo"));
		celda5.setCellValue(new HSSFRichTextString("Rubro"));
		celda6.setCellValue(new HSSFRichTextString("Tecnologia"));
		celda7.setCellValue(new HSSFRichTextString("Cantidad"));
		celda8.setCellValue(new HSSFRichTextString("Peso"));
		celda9.setCellValue(new HSSFRichTextString("Volumen"));
		celda10.setCellValue(new HSSFRichTextString("Diagnostico"));
		celda11.setCellValue(new HSSFRichTextString("Nro de MAC"));
		celda12.setCellValue(new HSSFRichTextString("Codigo Parte"));
		celda13.setCellValue(new HSSFRichTextString("Nro Requerimiento"));
		celda14.setCellValue(new HSSFRichTextString("Nro Circuito Digital"));	
		
		
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
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

        for(int k=0;k<14;k++){
			sheet.autoSizeColumn(k);
		}
        
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
	    try {
	    	workbook.write(outByteStream);
		} catch (IOException e) {
			 System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
		}
	    byte [] outArray = outByteStream.toByteArray();
		return outArray;
	}


	@Override
	public byte[] descargarMaterial(String ticket) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();

		int i=0;
		
		HSSFRow row = sheet.createRow((short)0);
	    
		HSSFCell celda1=row.createCell(0);
		HSSFCell celda2=row.createCell(1);
		
		celda1.setCellValue(new HSSFRichTextString("N° Ticket"));
		celda2.setCellValue(new HSSFRichTextString("CodMaterial"));
		
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
        
		String sql="SELECT DFD.NROTICKET,DFD.CODSAP FROM DETALLEFICHADEVOLUCION DFD WHERE DFD.NROTICKET IN (" + ticket + ") ORDER BY DFD.NROTICKET DESC";
		
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
			}
			
			for(int k=0;k<2;k++){
				sheet.autoSizeColumn(k);
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
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
	    try {
	    	workbook.write(outByteStream);
		} catch (IOException e) {
			 System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
		}
	    byte [] outArray = outByteStream.toByteArray();
		return outArray;
	}



}