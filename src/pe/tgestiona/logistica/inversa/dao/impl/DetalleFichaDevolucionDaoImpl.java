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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.DataValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import pe.tgestiona.logistica.inversa.bean.ConstantesGenerales;
import pe.tgestiona.logistica.inversa.bean.DetalleFichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.ErroresBean;
import pe.tgestiona.logistica.inversa.bean.FichaDevolucionBean;
import pe.tgestiona.logistica.inversa.dao.DetalleFichaDevolucionDao;
import pe.tgestiona.logistica.inversa.util.Util;

@Repository
public class DetalleFichaDevolucionDaoImpl implements DetalleFichaDevolucionDao {

	@Autowired
	private DriverManagerDataSource dataSource;

	@Override
	public void grabarDetalleFichaDevolucion(String nroTicket,List<DetalleFichaDevolucionBean> lista,FichaDevolucionBean fichaDevolucionBean) {
		String sql = "";
		String rubro="";
		Connection cn = null;
		PreparedStatement pstmt = null;
		try {
			cn = dataSource.getConnection();
			for (int i = 0; i < lista.size(); i++) {
				if(fichaDevolucionBean.isRetaceria() && fichaDevolucionBean.getTipoDevolucion().equals(ConstantesGenerales.MotivoDevolucionLiquidacion.SOBRESTOCK.getTipoValor())){
					if(lista.get(i).getRubro().equals(ConstantesGenerales.RubroMaterial.CABLE.getTipoValor())){
						rubro=ConstantesGenerales.RubroMaterial.RETACERIA.getTipoValor();						
					}
				}else{
					rubro=lista.get(i).getRubro();
				}
					
				sql = "INSERT INTO DETALLEFICHADEVOLUCION(NROTICKET,NRO,DESCRIPCION,SERIE,CODSAP,TIPO,RUBRO,TECNOLOGIA,CANTIDAD,PESO_RRSS,VOLUMEN_RRSS,DIAGNOSTICO,DIRECCIONMAC,CODIGOPARTE,NROREQUERIMIENTO,NROCIRCUITODIGITAL,ESTADO,INDBOD,INDLOT,INDGAR) VALUES ('"						
						+ nroTicket + "',"+ (i+1) + ",'" + lista.get(i).getDescripcion().trim() + "','" + lista.get(i).getSerie() + "','" + lista.get(i).getCodSAP() + "','" + lista.get(i).getTipo() + "','" + rubro + "','"
						+ lista.get(i).getTecnologia() + "'," + lista.get(i).getCantMateriales() + ",'" + lista.get(i).getPesoRRSS() + "','" + lista.get(i).getVolumenRRSS() + "','" + lista.get(i).getDiagnostico() + "','" +  lista.get(i).getDireccionMac() + "','" 
						+ lista.get(i).getCodigoParte() + "','" + lista.get(i).getNroRequerimiento() + "','" + lista.get(i).getNroCircuitoDigital() + "','" + ConstantesGenerales.EstadoValidacionActa.SINVALIDAR.getTipoValor() + "','" + ConstantesGenerales.EstadoValidacionActa.SINVALIDAR.getTipoValor() + "','" + ConstantesGenerales.EstadoValidacionActa.SINVALIDAR.getTipoValor() + "','" + ConstantesGenerales.EstadoValidacionActa.SINVALIDAR.getTipoValor() + "')";				
			
				cn.setAutoCommit(false);
				pstmt=cn.prepareStatement(sql);
				pstmt.executeUpdate();
				cn.commit();
			}

		} catch (SQLException ex) {
			System.out.println("Ha surgido la siguiente Excepcion:" + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				System.out.println("Ha surgido la siguiente Excepcion:" + e.getMessage());
			}
		}
	}

	
	@Override
	public void actualizarDetalleFichaDevolucion(List<DetalleFichaDevolucionBean> lista,String estadoValidacion,String motivo) {
		String sql="";
		String sqlPendienteSAP="";
		String sqlPendienteSINSAP="";
		String sqlUpdateRepetidos="";
		Connection cn = null;
		PreparedStatement pstmt = null;
		Util util=new Util();
		try {
			cn=dataSource.getConnection();	
			for(int i=0;i<lista.size();i++){
				if(estadoValidacion.equals(ConstantesGenerales.EstadoValidacionActa.SINVALIDAR.getTipoValor())){
					
					sql="UPDATE DETALLEFICHADEVOLUCION SET " + 
							"SERIESAP_6_0='" + lista.get(i).getSerieSAP_6_0() + "'," + 
							"CENTROSAP_6_0='" + lista.get(i).getCentroSAP_6_0() + "'," +
							"ALMACENSAP_6_0='" + lista.get(i).getAlmacen_6_0() + "'," + 
							"CODMATERIALSAP_6_0='" + lista.get(i).getCodMaterialSAP_6_0() + "'," + 
							"DESMATERIALSAP_6_0='" + lista.get(i).getDesMaterialSAP_6_0() + "'," +
							"LOTESAP_6_0='" + lista.get(i).getLote_6_0() + "'," + 
							"TIPOSTOCKS_6_0='" + lista.get(i).getTipoStocks_6_0() + "'," + 
							"STATUSISTEMA_6_0='" + lista.get(i).getStatusSistema_6_0() + "'," +
							"STATUSUSUARIO_6_0='" + lista.get(i).getStatusUsuario_6_0() + "'," + 
							"FECHAULTIMOMOVSAP_6_0='" + lista.get(i).getFechaUltimoMovSAP_6_0() + "'," + 
							"MODIFICADOPOR_6_0='" + lista.get(i).getModificadoPor_6_0() + "'," + 
							"FECHAINGRESOSAP_6_0='" + lista.get(i).getFechaIngresoSAP_6_0() + "'," + 
							"CREADOPOR_6_0='" + lista.get(i).getCreadoPor_6_0() + "'," + 
							"STATUSSAP_6_0='" + lista.get(i).getStatusSAP_6_0() + "'," + 
							"SERIESAP_4_7='" + lista.get(i).getSerieSAP_4_7() + "'," + 
							"CENTROSAP_4_7='" + lista.get(i).getCentroSAP_4_7() + "'," +
							"ALMACENSAP_4_7='" + lista.get(i).getAlmacen_4_7() + "'," + 
							"CODMATERIALSAP_4_7='" + lista.get(i).getCodMaterialSAP_4_7() + "'," + 
							"DESMATERIALSAP_4_7='" + lista.get(i).getDesMaterialSAP_4_7() + "'," +
							"LOTESAP_4_7='" + lista.get(i).getLote_4_7() + "'," + 
							"TIPOSTOCKS_4_7='" + lista.get(i).getTipoStocks_4_7() + "'," + 
							"STATUSISTEMA_4_7='" + lista.get(i).getStatusSistema_4_7() + "'," +
							"STATUSUSUARIO_4_7='" + lista.get(i).getStatusUsuario_4_7() + "'," + 
							"FECHAULTIMOMOVSAP_4_7='" + lista.get(i).getFechaUltimoMovSAP_4_7() + "'," + 
							"MODIFICADOPOR_4_7='" + lista.get(i).getModificadoPor_4_7() + "'," + 
							"FECHAINGRESOSAP_4_7='" + lista.get(i).getFechaIngresoSAP_4_7() + "'," + 
							"CREADOPOR_4_7='" + lista.get(i).getCreadoPor_4_7() + "'," + 
							"STATUSSAP_4_7='" + lista.get(i).getStatusSAP_4_7() + "'," + 
							"MOTIVORECHAZODEVOLUCION='" + lista.get(i).getMotivoRechazoDevolucion() + "'," + 
							"OBSERVACIONAPROBADO='" + lista.get(i).getObservacionAprobado() + "'," +
							"ESTADO='" + lista.get(i).getEstado() + "'," +
							"VALIDACIONGARANTIA='" + lista.get(i).getValidacionGarantia() + "'," + 
							"FECHAREMOZADOTALLER='" + lista.get(i).getFechaRemozadoTaller() + "'," +
							"REMOZADO_REPARADO='" + lista.get(i).getRemozado_Reparado() + "'," +
							"NEGOCIO='"+ lista.get(i).getNegocio() + "'," + 
							"PESO='"+ lista.get(i).getPeso() + "'," + 
							"TIPO='"+ lista.get(i).getTipo() + "'," + 
							"RUBRO='"+ lista.get(i).getRubro() + "'," + 
							"TECNOLOGIA='"+ lista.get(i).getTecnologia() + "'," + 							
							"PRECIOEQUIPO='" + lista.get(i).getPrecio() + "'," + 
							"VOLUNITARIO='" + lista.get(i).getVolUnitario() + "'," + 
							"SOCIEDAD='" + lista.get(i).getSociedad() + "'," + 
							"SERIADO='" + lista.get(i).getSeriado() + "'," + 	
							"PROVISION='" + lista.get(i).getProvision() + "'," +
							"DIASGARANTIA='" + lista.get(i).getDiasGarantia() + "'," +
							"FECHAVALIDACION=TO_DATE('" + lista.get(i).getFechaValidacion() + "','DD/MM/YYYY HH24:MI:SS')," + 
							"LIQ_NOLIQ='" + lista.get(i).getLiq_noliq() + "'," +
							"MESLIQUIDACIONAVERIAS='" + lista.get(i).getMesLiquidacion_Averias() + "', " + 
							"MODORECOJO='" + lista.get(i).getModoRecojo() + "', " +
							"TRAMOSMINIMOS='" + lista.get(i).getTramosMinimos() + "'," + 
							"FECDESPACHOALMACEN='" + lista.get(i).getFechaDespachoAlmacen() + "'," +
							"INDBOD='" + lista.get(i).getIndBod() + "'," +
							"INDLOT='" + lista.get(i).getIndLote() + "'," +
							"INDGAR='" + lista.get(i).getIndGar() + "'," +
							"OBSBODAPROB='" + lista.get(i).getObsBodAprob() + "'," +
							"OBSLOTAPROB='" + lista.get(i).getObsLoteAprob() + "'," +
							"OBSGARAPROB='" + lista.get(i).getObsGarAprob() + "'," + 
							"OBSBODRECHZ='" + lista.get(i).getObsBodRechaz() + "'," +
							"OBSLOTRECHZ='" + lista.get(i).getObsLoteRechaz() + "'," +
							"OBSGARRECHZ='" + lista.get(i).getObsGarRechaz() + "'," + 
							"OBSERVACIONES='" + lista.get(i).getObservacion() + "' ";
							
					
							if(motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.SOBRESTOCK.getTipoValor())){
								sql=sql + " WHERE NROTICKET='" + lista.get(i).getNroTicket() + "' AND LPAD(TRIM(UPPER(CODSAP)),20,'0')='" + util.rellenar(lista.get(i).getCodSAP().trim().toUpperCase(), 20)  + "' AND NRO=" + lista.get(i).getNro();									
							}else{
								sql=sql + " WHERE NROTICKET='" + lista.get(i).getNroTicket() + "' AND LPAD(TRIM(UPPER(SERIE)),25,'0')='" + util.rellenar(lista.get(i).getSerie().trim().toUpperCase(), 25)  + "' AND NRO=" + lista.get(i).getNro();									
							}

				}

				if(estadoValidacion.equals(ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor())){			
					sqlPendienteSAP="CENTROSAP_6_0='" + lista.get(i).getCentroSAP_6_0() + "'," +
								    "ALMACENSAP_6_0='" + lista.get(i).getAlmacen_6_0() + "'," + 
								    "LOTESAP_6_0='" + lista.get(i).getLote_6_0() + "'," + 
								    "CENTROSAP_4_7='" + lista.get(i).getCentroSAP_4_7() + "'," +
								    "ALMACENSAP_4_7='" + lista.get(i).getAlmacen_4_7() + "'," + 
								    "LOTESAP_4_7='" + lista.get(i).getLote_4_7() + "',"+
									"TRAMOSMINIMOS='" + lista.get(i).getTramosMinimos() + "'"; 
					
					sqlPendienteSINSAP="INDBOD='" + lista.get(i).getIndBod() + "'," +
									   "INDLOT='" + lista.get(i).getIndLote() + "'," +
									   "INDGAR='" + lista.get(i).getIndGar() + "'," +
									   "OBSBODAPROB='" + lista.get(i).getObsBodAprob() + "'," +
									   "OBSLOTAPROB='" + lista.get(i).getObsLoteAprob() + "'," +
									   "OBSGARAPROB='" + lista.get(i).getObsGarAprob() + "'," + 
									   "OBSBODRECHZ='" + lista.get(i).getObsBodRechaz() + "'," +
									   "OBSLOTRECHZ='" + lista.get(i).getObsLoteRechaz() + "'," +
									   "OBSGARRECHZ='" + lista.get(i).getObsGarRechaz() + "'," + 
									   "MOTIVORECHAZODEVOLUCION='" + lista.get(i).getMotivoRechazoDevolucion() + "'," + 
									   "OBSERVACIONAPROBADO='" + lista.get(i).getObservacionAprobado() + "'," +
									   "OBSERVACIONES='" + lista.get(i).getObservacion() + "'," +
									   "ESTADO='" + lista.get(i).getEstado() + "'," + 
									   "FECDESPACHOALMACEN='" + lista.get(i).getFechaDespachoAlmacen() + "'";
					
					sql="UPDATE DETALLEFICHADEVOLUCION SET ";
					
					if(lista.get(i).getCodMaterialSAP_6_0().equals(ConstantesGenerales.GUION) && lista.get(i).getCodMaterialSAP_4_7().equals(ConstantesGenerales.GUION)){
						sql=sql+ sqlPendienteSINSAP + " WHERE NROTICKET='" + lista.get(i).getNroTicket() + "' AND LPAD(TRIM(UPPER(SERIE)),25,'0')='" + util.rellenar(lista.get(i).getSerie().trim().toUpperCase(), 25)  + "'";						
					}else{
						
						if(motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.SOBRESTOCK.getTipoValor())){
							sql=sql + sqlPendienteSAP + " , " +  sqlPendienteSINSAP + " WHERE NROTICKET='" + lista.get(i).getNroTicket() + "' AND LPAD(TRIM(UPPER(CODSAP)),20,'0')='" + util.rellenar(lista.get(i).getCodSAP().trim().toUpperCase(), 20)  + "' AND NRO=" + lista.get(i).getNro();									
						}else{
							sql=sql + sqlPendienteSAP + " , " +  sqlPendienteSINSAP + " WHERE NROTICKET='" + lista.get(i).getNroTicket() + "' AND LPAD(TRIM(UPPER(SERIE)),25,'0')='" + util.rellenar(lista.get(i).getSerie().trim().toUpperCase(), 25)  + "' AND NRO=" + lista.get(i).getNro();									
						}
					}	
							
				}
				
				cn.setAutoCommit(false);
				pstmt=cn.prepareStatement(sql);
				pstmt.executeUpdate();
				cn.commit();
			}
			
			//COLOCO COMO RECHAZADO AQUELLAS QUE SE ENCUENTRAN SI SE ENCUENTRAN SERIES REPETIDAS EN LA MISMA ACTA
			
			if(estadoValidacion.equals(ConstantesGenerales.EstadoValidacionActa.SINVALIDAR.getTipoValor())){
				
				for(int m=0;m<lista.size();m++){
					int cont=0;
					for(int n=0;n<lista.size();n++){
						if((lista.get(m).getSerie().equals(lista.get(n).getSerie())) && (lista.get(m).getTipo().equals(lista.get(n).getTipo())) && (lista.get(m).getRubro().equals(lista.get(n).getRubro())) && (lista.get(m).getTecnologia().equals(lista.get(n).getTecnologia())) && !(lista.get(m).getSerie().equals("SINSERIE"))){
							cont++;
							if(cont>1){
								sqlUpdateRepetidos="UPDATE DETALLEFICHADEVOLUCION SET ESTADO='RECHAZADO', MOTIVORECHAZODEVOLUCION='RECHAZADO POR TENER SERIES REPETIDAS'," + 
												   "OBSERVACIONAPROBADO='' WHERE NROTICKET='" + lista.get(m).getNroTicket() + "' AND LPAD(TRIM(UPPER(SERIE)),25,'0')='" + util.rellenar(lista.get(n).getSerie().trim().toUpperCase(), 25)  + "' AND NRO=" + lista.get(n).getNro();	
								
								cn.setAutoCommit(false);
								pstmt=cn.prepareStatement(sqlUpdateRepetidos);
								pstmt.executeUpdate();
								cn.commit();
							}
						}
					}
					
					
					
					
					
				}
				
				ResultSet rsMaterialSerie = null;
				Statement stMaterialSerie = cn.createStatement();
				String sqlUpdateMaterialSerie="";
				String sqlMaterialSerie="";
				for(int i=0;i<lista.size();i++){
					sqlMaterialSerie="SELECT CODMATERIAL FROM MATERIAL WHERE LPAD(TRIM(UPPER(CODMATERIAL)),25,'0')='" + util.rellenar(lista.get(i).getSerie().trim().toUpperCase(), 25) + "'";
					rsMaterialSerie=stMaterialSerie.executeQuery(sqlMaterialSerie);
					while(rsMaterialSerie.next()){
						sqlUpdateMaterialSerie="UPDATE DETALLEFICHADEVOLUCION SET ESTADO='RECHAZADO', MOTIVORECHAZODEVOLUCION='RECHAZADO YA QUE LA SERIE COINCIDE CON EL CODIGO DE MATERIAL'," + 
								   "OBSERVACIONAPROBADO='' WHERE NROTICKET='" + lista.get(i).getNroTicket() + "' AND LPAD(TRIM(UPPER(SERIE)),25,'0')='" + util.rellenar(lista.get(i).getSerie().trim().toUpperCase(), 25)  + "' AND NRO=" + lista.get(i).getNro();	
						cn.setAutoCommit(false);
						pstmt=cn.prepareStatement(sqlUpdateMaterialSerie);
						pstmt.executeUpdate();
						cn.commit();
					}
				}
				
			}	

		} catch (SQLException e) {
			System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());
            e.printStackTrace();
			
		}finally{
			try{
				cn.close();
			}catch(Exception e){
				System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());
	            e.printStackTrace();
			}
		}				
	}
	
	@Override
	public void actualizarDetalleFichaDevolucionDevoluci(List<FichaDevolucionBean> lista) {
		String sql="";
		Util util=new Util();
		Connection cn = null;
		PreparedStatement pstmt = null;
		try{
			cn=dataSource.getConnection();	
			Statement stQueryGroup = cn.createStatement();
			Statement stQueryFind = cn.createStatement();
			Statement stExisteSAP = cn.createStatement();

			ResultSet rsGroup=null;
			ResultSet rsFind=null;
			ResultSet rsExisteSAP=null;

			for(int i=0;i<lista.size();i++){
				
				sql="SELECT NROTICKET,CODSAP,TRAMOSMINIMOS,SUM(CANTIDAD) FROM DETALLEFICHADEVOLUCION GROUP BY CODSAP,TRAMOSMINIMOS,NROTICKET HAVING NROTICKET='" + lista.get(i).getNroTicket()  + "' AND TRAMOSMINIMOS='" + ConstantesGenerales.EstadoBusqueda.EXISTEP.getTipoValor() + "'";
				rsGroup=stQueryGroup.executeQuery(sql);
				while(rsGroup.next()){
					String nroTicket="";
					String codSAP="";
					double cantMateriales=0.0;
					
					nroTicket=rsGroup.getString(1)==null?"":rsGroup.getString(1);
					codSAP=rsGroup.getString(2)==null?"":rsGroup.getString(2);
					cantMateriales=rsGroup.getDouble(4);

					sql="SELECT CODMATERIAL FROM MATERIALSAP WHERE LPAD(TRIM(UPPER(CODMATERIAL)),20,'0')='" + util.rellenar(codSAP.trim().toUpperCase(), 20) + "'";
					rsExisteSAP=stExisteSAP.executeQuery(sql);
					if(rsExisteSAP.next()){
						sql="SELECT CODMATERIAL FROM MATERIALSAP WHERE LPAD(TRIM(UPPER(CODMATERIAL)),20,'0')='" + util.rellenar(codSAP.trim().toUpperCase(), 20) + "' AND LIBRE_UTILIZACION>=" + cantMateriales;
						rsFind=stQueryFind.executeQuery(sql);
						if(rsFind.next()){
							sql="UPDATE DETALLEFICHADEVOLUCION SET ESTADO='" + ConstantesGenerales.EstadoValidacionActa.APROBADO.getTipoValor() + "', TRAMOSMINIMOS='EXISTEA',OBSERVACIONAPROBADO='Se aprueba por estar dentro de la Libre Utilizacion' WHERE NROTICKET='" + nroTicket + "' AND CODSAP='" + codSAP + "' AND TRAMOSMINIMOS='" + ConstantesGenerales.EstadoBusqueda.EXISTEP.getTipoValor() + "'";
							cn.setAutoCommit(false);
							pstmt=cn.prepareStatement(sql);
							pstmt.executeUpdate();
							cn.commit();

						}else{
							sql="UPDATE DETALLEFICHADEVOLUCION SET ESTADO='" + ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor() + "', TRAMOSMINIMOS='EXISTEP',MOTIVORECHAZODEVOLUCION='No coincide ya que no se encuentra dentro de la Libre Utilizacion' WHERE NROTICKET='" + nroTicket + "' AND LPAD(TRIM(UPPER(CODSAP)),20,'0')='" + util.rellenar(codSAP.trim().toUpperCase(), 20) + "' AND TRAMOSMINIMOS='" + ConstantesGenerales.EstadoBusqueda.EXISTEP.getTipoValor() + "'";
							cn.setAutoCommit(false);
							pstmt=cn.prepareStatement(sql);
							pstmt.executeUpdate();
							cn.commit();
						}
					}else{
						sql="UPDATE DETALLEFICHADEVOLUCION SET ESTADO='" + ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor() + "', TRAMOSMINIMOS='EXISTEP',MOTIVORECHAZODEVOLUCION='El Material NO EXISTE EN SAP' WHERE NROTICKET='" + nroTicket + "' AND CODSAP='" + codSAP + "' AND TRAMOSMINIMOS='" + ConstantesGenerales.EstadoBusqueda.EXISTEP.getTipoValor() + "'";
						cn.setAutoCommit(false);
						pstmt=cn.prepareStatement(sql);
						pstmt.executeUpdate();
						cn.commit();
					}
					
				}
				
			}
		}catch (SQLException e) {
			System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());
			
		}finally{
			try{
				cn.close();
			}catch(Exception e){
				System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());
			}
		}	
		
	}

	@Override
	public byte[] descargaExcel(String nroTicket) {
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
        
/*        String sql="SELECT DISTINCT TO_CHAR(DFD.FECHAVALIDACION,'DD/MM/YYYY HH24:MI:SS'),FD.MESGESTION,TO_CHAR(FD.FECHASOLICITUD,'DD/MM/YYYY HH24:MI:SS'),GESTOR.NOMGESTOR||' <'||FD.CORREO||'>',UBI.ZONAL,(SELECT NOMBRE FROM UBIGEO  WHERE CODDEP=SUBSTR(ENT.CODUBIGEO, 0, 2) AND CODPROV='00' AND CODDIST='00') AS DEPARTAMENTO," + 
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

        */
        
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
     		   "WHERE FD.CODENTIDAD=ENT.CODENTIDAD AND ENT.CODUBIGEO=UBI.CODUBIGEO AND FD.NROTICKET=DFD.NROTICKET AND ENT.CODCANAL=CA.CODCANAL AND TDEVOL.CODTIPODEVOL=FD.CODTIPODEVOL AND GESTOR.CORREO=FD.CORREO AND FD.NROTICKET IN(" + nroTicket + ")" ;

        
        
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
			System.out.println("Ha suscitado una excepcion:" + e.getMessage());
		}
	    byte [] outArray = outByteStream.toByteArray();
		return outArray;
	}
	
	@Override
	public byte[] descargaSeriesPendiente(String nroTicket) {
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
		
		celda1.setCellValue(new HSSFRichTextString("NROTICKET"));
		celda2.setCellValue(new HSSFRichTextString("FECHA SOLICITUD"));
		celda3.setCellValue(new HSSFRichTextString("ENTIDAD"));
		celda4.setCellValue(new HSSFRichTextString("CODIGO MATERIAL(SAP)"));
		celda5.setCellValue(new HSSFRichTextString("DESCRIPCION MATERIAL (SAP)"));
		celda6.setCellValue(new HSSFRichTextString("SERIE"));
		celda7.setCellValue(new HSSFRichTextString("CE"));
		celda8.setCellValue(new HSSFRichTextString("ALM"));
		celda9.setCellValue(new HSSFRichTextString("LOTE"));
		celda10.setCellValue(new HSSFRichTextString("FECHA DE CORREO"));
		celda11.setCellValue(new HSSFRichTextString("FECHA APROBACION ACTA"));
		celda12.setCellValue(new HSSFRichTextString("MES DE LIQUIDACION"));
		celda13.setCellValue(new HSSFRichTextString("FECHA INGRESO SAP(FECHA DE CREACION)"));
		celda14.setCellValue(new HSSFRichTextString("FECHA REMOZADO TALLER"));
		celda15.setCellValue(new HSSFRichTextString("OBSERVACION APROBADO"));
		celda16.setCellValue(new HSSFRichTextString("MOTIVO RECHAZO DEVOLUCION"));
		
		 // Style Font in Cell 2B 
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.DARK_BLUE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFFont hSSFFont = workbook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 11);
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
        
		String sql="SELECT FD.NROTICKET,TO_CHAR(DFD.FECHAVALIDACION,'DD/MM/YYYY HH24:MI:SS'),ENT.NOMENTIDAD," + 
				   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.CODMATERIALSAP_4_7 ELSE DFD.CODMATERIALSAP_6_0 END) AS CODMATERIAL," + 
				   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.DESMATERIALSAP_4_7 ELSE DFD.DESMATERIALSAP_6_0 END) AS DESMATERIAL," + 
				   "DFD.SERIE AS SERIE," + 
				   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.CENTROSAP_4_7 ELSE DFD.CENTROSAP_6_0 END) AS CENTRO," + 
				   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.ALMACENSAP_4_7 ELSE DFD.ALMACENSAP_6_0 END) AS ALMACEN,"+
				   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.LOTESAP_4_7 ELSE DFD.LOTESAP_6_0 END) AS LOTE, "+
				   "TO_CHAR(FD.FECHASOLICITUD,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(DFD.FECHAVALIDACION,'DD/MM/YYYY HH24:MI:SS'),FD.MESGESTION," + 
				   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.FECHAINGRESOSAP_4_7 ELSE DFD.FECHAINGRESOSAP_6_0 END) AS FECHAINGRESOSAP," + 
				   "DFD.FECHAREMOZADOTALLER,DFD.OBSERVACIONAPROBADO,DFD.MOTIVORECHAZODEVOLUCION " + 
				   "FROM FICHADEVOLUCION FD,DETALLEFICHADEVOLUCION DFD,ENTIDAD ENT " + 
				   "WHERE FD.NROTICKET=DFD.NROTICKET AND FD.CODENTIDAD=ENT.CODENTIDAD AND " + 
				   "FD.NROTICKET IN(" + nroTicket + ") AND DFD.ESTADO='" + ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor() + "' ORDER BY FD.NROTICKET,DFD.SERIE DESC";
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
		        row1.createCell((short)8).setCellValue(rs.getString(9));
		        row1.createCell((short)9).setCellValue(rs.getString(10));
		        row1.createCell((short)10).setCellValue(rs.getString(11));
		        row1.createCell((short)11).setCellValue(rs.getString(12));
		        row1.createCell((short)12).setCellValue(rs.getString(13));
		        row1.createCell((short)13).setCellValue(rs.getString(14));
		        row1.createCell((short)14).setCellValue(rs.getString(15));
		        row1.createCell((short)15).setCellValue(rs.getString(16));
			}
			
			for(int k=0;k<16;k++){
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
			System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());
		}
	    byte [] outArray = outByteStream.toByteArray();
		return outArray;
	}
	
	@Override
	public byte[] descargaSeriesPendienteGarantia(String nroTicket) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		int i=0;
		HSSFRow row = sheet.createRow((short)0);
		DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(new String[] { ConstantesGenerales.BaseGarantia.RESPONSABILIDAD_PUNTO.getTipoValor(), ConstantesGenerales.BaseGarantia.RESPONSABILIDAD_TELEFONICA.getTipoValor() });
		
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
		
		
		celda1.setCellValue(new HSSFRichTextString("NRO TICKET"));
		celda2.setCellValue(new HSSFRichTextString("FECHA SOLICITUD"));
		celda3.setCellValue(new HSSFRichTextString("ENTIDAD"));
		celda4.setCellValue(new HSSFRichTextString("CODIGO MATERIAL(SAP)"));
		celda5.setCellValue(new HSSFRichTextString("DESCRIPCION MATERIAL (SAP)"));
		celda6.setCellValue(new HSSFRichTextString("SERIE"));
		celda7.setCellValue(new HSSFRichTextString("CE"));
		celda8.setCellValue(new HSSFRichTextString("ALM"));
		celda9.setCellValue(new HSSFRichTextString("LOTE"));
		celda10.setCellValue(new HSSFRichTextString("FECHA DE CORREO"));
		celda11.setCellValue(new HSSFRichTextString("FECHA APROBACION ACTA"));
		celda12.setCellValue(new HSSFRichTextString("MES DE LIQUIDACION"));
		celda13.setCellValue(new HSSFRichTextString("FECHA INGRESO SAP(FECHA DE CREACION)"));
		celda14.setCellValue(new HSSFRichTextString("FECHA REMOZADO TALLER"));
		celda15.setCellValue(new HSSFRichTextString("FECHA PIERDE GARANTIA"));
		celda16.setCellValue(new HSSFRichTextString("OBSERVACION APROBADO"));
		celda17.setCellValue(new HSSFRichTextString("MOTIVO RECHAZO DEVOLUCION"));
		celda18.setCellValue(new HSSFRichTextString("OBSERVACION"));
		
		 // Style Font in Cell 2B 
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.DARK_BLUE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFFont hSSFFont = workbook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 11);
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
        
		String sql="SELECT FD.NROTICKET,TO_CHAR(DFD.FECHAVALIDACION,'DD/MM/YYYY HH24:MI:SS'),ENT.NOMENTIDAD," + 
				   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.CODMATERIALSAP_4_7 ELSE DFD.CODMATERIALSAP_6_0 END) AS CODMATERIAL," + 
				   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.DESMATERIALSAP_4_7 ELSE DFD.DESMATERIALSAP_6_0 END) AS DESMATERIAL," + 
				   "DFD.SERIE AS SERIE," + 
				   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.CENTROSAP_4_7 ELSE DFD.CENTROSAP_6_0 END) AS CENTRO," + 
				   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.ALMACENSAP_4_7 ELSE DFD.ALMACENSAP_6_0 END) AS ALMACEN,"+
				   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.LOTESAP_4_7 ELSE DFD.LOTESAP_6_0 END) AS LOTE, "+
				   "TO_CHAR(FD.FECHASOLICITUD,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(DFD.FECHAVALIDACION,'DD/MM/YYYY HH24:MI:SS'),FD.MESGESTION," + 
				   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.FECHAINGRESOSAP_4_7 ELSE DFD.FECHAINGRESOSAP_6_0 END) AS FECHAINGRESOSAP," + 
				   "DFD.FECHAREMOZADOTALLER,DFD.OBSERVACIONAPROBADO,DFD.MOTIVORECHAZODEVOLUCION " + 
				   "FROM FICHADEVOLUCION FD,DETALLEFICHADEVOLUCION DFD,ENTIDAD ENT " + 
				   "WHERE FD.NROTICKET=DFD.NROTICKET AND FD.CODENTIDAD=ENT.CODENTIDAD AND " + 
				   "FD.NROTICKET IN(" + nroTicket + ") AND DFD.ESTADO='" + ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor() + "' AND DFD.INDGAR='" + ConstantesGenerales.IndicadorCriterios.PENDIENTE.getTipoValor() + "' AND VALIDACIONGARANTIA='" + ConstantesGenerales.EstadoValidacionGarantia.NO.getTipoValor() + "' ORDER BY FD.NROTICKET,DFD.SERIE DESC"; 
		System.out.println(sql);
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				i++;
				HSSFRow row1 = sheet.createRow((short)i);

				CellRangeAddressList addressList = new CellRangeAddressList(1,i,17,17);
				DataValidation dataValidation = new HSSFDataValidation(addressList,dvConstraint);
				dataValidation.setSuppressDropDownArrow(false);
				sheet.addValidationData(dataValidation);
				
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
		        row1.createCell((short)14).setCellValue("");		        
		        row1.createCell((short)15).setCellValue(rs.getString(15));
		        row1.createCell((short)16).setCellValue(rs.getString(16));
		        row1.createCell((short)17).setCellValue("-------------------------------------------------------");
			}
			
			for(int k=0;k<18;k++){
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
			System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());
		}
	    byte [] outArray = outByteStream.toByteArray();
		return outArray;

	}
	
	

	@Override
	public byte[] descargaSeriesPendienteValidacionExterna(String nroTicket) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		int i=0;
		HSSFRow row = sheet.createRow((short)0);
		DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(new String[] { ConstantesGenerales.EstadoValidacionActa.APROBADO.getTipoValor(), ConstantesGenerales.EstadoValidacionActa.RECHAZADO.getTipoValor() });
		
		HSSFCell celda1=row.createCell(0);
		HSSFCell celda2=row.createCell(1);
		HSSFCell celda3=row.createCell(2);
		HSSFCell celda4=row.createCell(3);
		HSSFCell celda5=row.createCell(4);
		HSSFCell celda6=row.createCell(5);

		celda1.setCellValue(new HSSFRichTextString("NRO TICKET"));
		celda2.setCellValue(new HSSFRichTextString("CODMATERIAL"));
		celda3.setCellValue(new HSSFRichTextString("DESCRIPCION"));
		celda4.setCellValue(new HSSFRichTextString("SERIE"));
		celda5.setCellValue(new HSSFRichTextString("ESTADO"));
		celda6.setCellValue(new HSSFRichTextString("OBSERVACION"));
		
		 // Style Font in Cell 2B 
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.DARK_BLUE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFFont hSSFFont = workbook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 11);
        hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        hSSFFont.setColor(HSSFColor.WHITE.index);
        
        cellStyle.setFont(hSSFFont);
        
        celda1.setCellStyle(cellStyle);
        celda2.setCellStyle(cellStyle);
        celda3.setCellStyle(cellStyle);
        celda4.setCellStyle(cellStyle);
        celda5.setCellStyle(cellStyle);
        celda6.setCellStyle(cellStyle);
        
		String sql="SELECT FD.NROTICKET,(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.CODMATERIALSAP_4_7 ELSE DFD.CODMATERIALSAP_6_0 END) AS CODMATERIAL," + 
				   "(CASE WHEN DFD.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFD.DESMATERIALSAP_4_7 ELSE DFD.DESMATERIALSAP_6_0 END) AS DESMATERIAL," + 
				   "DFD.SERIE AS SERIE FROM FICHADEVOLUCION FD,DETALLEFICHADEVOLUCION DFD WHERE FD.NROTICKET=DFD.NROTICKET AND " + 
				   "FD.NROTICKET IN(" + nroTicket + ") AND DFD.ESTADO='" + ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor() + "' ORDER BY FD.NROTICKET,DFD.SERIE DESC";
		System.out.println(sql);
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				i++;
				HSSFRow row1 = sheet.createRow((short)i);
				CellRangeAddressList addressList = new CellRangeAddressList(1,i,4,4);
				DataValidation dataValidation = new HSSFDataValidation(addressList,dvConstraint);
				dataValidation.setSuppressDropDownArrow(false);
				sheet.addValidationData(dataValidation);
				
				row1.createCell((short)0).setCellValue(rs.getString(1));
		        row1.createCell((short)1).setCellValue(rs.getString(2));
		        row1.createCell((short)2).setCellValue(rs.getString(3));
		        row1.createCell((short)3).setCellValue(rs.getString(4));
		        row1.createCell((short)4).setCellValue("");
		        row1.createCell((short)5).setCellValue("");
			}
			
			for(int k=0;k<6;k++){
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
			System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());
		}
	    byte [] outArray = outByteStream.toByteArray();
		return outArray;
	}
	
	@Override
	public List<DetalleFichaDevolucionBean> leerarchivoExcel(InputStream file) {
		List<DetalleFichaDevolucionBean> listFormato = null;
		Util util=new Util();
		try {
			Workbook workbook = Workbook.getWorkbook(file); //Pasamos el excel que vamos a leer
			Sheet sheet = workbook.getSheet(0); //Seleccionamos la hoja que vamos a leer
			String dato;
			int cantColumnas=sheet.getColumns();
			boolean invalido=false;
			listFormato = new ArrayList<DetalleFichaDevolucionBean>();
			
			if(cantColumnas==ConstantesGenerales.CANTIDAD_COLUMNAS_FICHA){
				for (int fila = 1; fila < sheet.getRows(); fila++) { //recorremos las filas
					DetalleFichaDevolucionBean detalleFichaDevolucionBean=new DetalleFichaDevolucionBean();

					for (int columna = 0; columna < sheet.getColumns(); columna++) { //recorremos las columnas
						
						dato = sheet.getCell(columna, fila).getContents(); //setear la celda leida a nombre
						detalleFichaDevolucionBean.setCantColumnasFicha(sheet.getColumns());						
						switch(columna){
						    case 0: dato=util.eliminarEspaciosBlanco(dato);
						    	
						    		if((dato.trim().length()>=6 && dato.trim().length()<=18) || dato.trim().length()==0){
										detalleFichaDevolucionBean.setSerie(dato.trim().toUpperCase());										
						    		}else{
										if(dato.trim().length()<6){
											detalleFichaDevolucionBean.setSerie(util.rellenar(dato.trim().toUpperCase(), 6));										
										}
										if(dato.trim().length()>18){
											invalido=util.caracterInvalido(dato.trim().toUpperCase());
											if(invalido){
												detalleFichaDevolucionBean.setSerie(dato.trim().toUpperCase());	
											}else{
												detalleFichaDevolucionBean.setSerie(util.quitarDigitosIzquierda(dato.trim().toUpperCase(), 18));
											}											
										}
									}
									break;
							case 1: detalleFichaDevolucionBean.setDescripcion(dato.trim());
									break;								
							case 2: if(dato.trim().equals("")){
										detalleFichaDevolucionBean.setCodSAP(ConstantesGenerales.GUION);
									}else{
										detalleFichaDevolucionBean.setCodSAP(dato.trim());
									}
									break;
							case 3: detalleFichaDevolucionBean.setTipo(dato.trim());
									break;
							case 4: detalleFichaDevolucionBean.setRubro(dato.trim());
									break; 
							case 5: detalleFichaDevolucionBean.setTecnologia(dato.trim());
									break;
							case 6:if(dato.trim().equals("")){
										detalleFichaDevolucionBean.setCantMateriales(1);	
									}else{
										dato=dato.replace("," ,".");									
										detalleFichaDevolucionBean.setCantMateriales(Double.parseDouble(dato.trim()));
									}
									break;
							case 7:if(dato.trim().equals("")){
										detalleFichaDevolucionBean.setPesoRRSS(ConstantesGenerales.GUION);	
									}else{
										detalleFichaDevolucionBean.setPesoRRSS(dato.trim());
									}
									break;								
							case 8:if(dato.trim().equals("")){
										detalleFichaDevolucionBean.setVolumenRRSS(ConstantesGenerales.GUION);	
									}else{
										detalleFichaDevolucionBean.setVolumenRRSS(dato.trim());
									}
									break;			
							case 9:if(dato.trim().equals("")){
										detalleFichaDevolucionBean.setDiagnostico(ConstantesGenerales.GUION);	
									}else{
										detalleFichaDevolucionBean.setDiagnostico(dato.trim());
									}
									break;	
							case 10:if(dato.trim().equals("")){
										detalleFichaDevolucionBean.setDireccionMac(ConstantesGenerales.GUION);	
									}else{
										detalleFichaDevolucionBean.setDireccionMac(dato.trim());
									}
									break;		
							case 11:if(dato.trim().equals("")){
										detalleFichaDevolucionBean.setCodigoParte(ConstantesGenerales.GUION);	
									}else{
										detalleFichaDevolucionBean.setCodigoParte(dato.trim());
									}
									break;
							case 12:if(dato.trim().equals("")){
										detalleFichaDevolucionBean.setNroRequerimiento(ConstantesGenerales.GUION);	
									}else{
										detalleFichaDevolucionBean.setNroRequerimiento(dato.trim());
									}
									break;
							case 13:if(dato.trim().equals("")){
										detalleFichaDevolucionBean.setNroCircuitoDigital(ConstantesGenerales.GUION);	
									}else{
										detalleFichaDevolucionBean.setNroCircuitoDigital(dato.trim());
									}
									break;
									
						}
						
					}
					listFormato.add(detalleFichaDevolucionBean);
				}
			}else{
				DetalleFichaDevolucionBean detalleFichaDevolucionBean=new DetalleFichaDevolucionBean();
				detalleFichaDevolucionBean.setCantColumnasFicha(sheet.getColumns());
				listFormato.add(detalleFichaDevolucionBean);
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

	@Override
	public List<String> validarCamposVacios(List<DetalleFichaDevolucionBean> lista) {
		int contSerie=0;
		int contTipo=0;
		int contRubro=0;
		int contTecnologia=0;
		int contCantidad=0;
		
		List<String> listCampos=new ArrayList<String>();
		try {
			if(lista!=null){
				for (int i = 0; i < lista.size(); i++) {
					
					//Verificamos si el campo Serie se encuentra vacio
					if((lista.get(i).getSerie()==null ||lista.get(i).getSerie().trim().equals("") || lista.get(i).getSerie().trim().length()==0) &&
					   (lista.get(0).getCantColumnasFicha()==ConstantesGenerales.CANTIDAD_COLUMNAS_FICHA)){
						contSerie++;
						if(contSerie==1){
							listCampos.add("Serie");						
						}
					}
					
					//Verificamos si el campo Tipo de Material se encuentra vacio
					if((lista.get(i).getTipo()==null ||lista.get(i).getTipo().trim().equals("") || lista.get(i).getTipo().trim().length()==0) &&
					   (lista.get(0).getCantColumnasFicha()==ConstantesGenerales.CANTIDAD_COLUMNAS_FICHA)){
						contTipo++;
						if(contTipo==1){
							listCampos.add("Tipo de Material");						
						}
					}
					//Verificamos si el campo Rubro se encuentra vacio
					if((lista.get(i).getRubro()==null ||lista.get(i).getRubro().trim().equals("") || lista.get(i).getRubro().trim().length()==0) &&
					   (lista.get(0).getCantColumnasFicha()==ConstantesGenerales.CANTIDAD_COLUMNAS_FICHA)){
						contRubro++;
						if(contRubro==1){
							listCampos.add("Rubro");						
						}

					}
					//Verificamos si el campo Tecnologia se encuentra vacio
					if((lista.get(i).getTecnologia()==null ||lista.get(i).getTecnologia().trim().equals("") || lista.get(i).getTecnologia().trim().length()==0) &&
						(lista.get(0).getCantColumnasFicha()==ConstantesGenerales.CANTIDAD_COLUMNAS_FICHA)){
						contTecnologia++;
						if(contTecnologia==1){
							listCampos.add("Tecnologia");						
						}
					}
					
					//Verificamos si el campo Cantidad se encuentra vacio
					if(lista.get(i).getCantMateriales()==0){
						contCantidad++;
						if(contCantidad==1){
							listCampos.add("Cantidad");						
						}
					}	
				}				
			}
									
		} finally {
			try {
				
			} catch (Exception e) {
				System.out.println("Ha surgido la siguiente Excepcion:" + e.getMessage());
			}
		}
		return listCampos;
	}

	@Override
	public List<String> validarSeriesCaracteres(List<DetalleFichaDevolucionBean> lista) {
		List<String> listaSeriesCaracteres=null;
		Util util=new Util();
		int contador=0;
		String serie="";
		String serieSinEspacios="";
		String letra="";
		boolean valido=true;
		
		String caracteresValidos[]={"1","2","3","4","5","6","7","8","9","0","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","/"};
		listaSeriesCaracteres = new ArrayList<String>();
		
		for(int i=0;i<lista.size();i++){
			serie=lista.get(i).getSerie().trim().toUpperCase();

			//serieSinEspacios=util.eliminarEspaciosBlanco(serie);			
			
			valido=false;
			for(int l=0;l<serie.length();l++){
				letra=serie.substring(l, (l+1));
				contador=0;
				for(int c=0;c<caracteresValidos.length;c++){
					if(letra.equals(caracteresValidos[c])){
						contador=contador+1;
						break;
					}
				}
				
				if(contador==0){
					valido=true;
				}
			}
			
			if(valido){
				listaSeriesCaracteres.add("La serie: " + serie + " tiene caracteres no permitidos en la Fila: " + (i+1));
			}
		}
		return listaSeriesCaracteres;
	}

	@Override
	public List<String> validarContenido(List<DetalleFichaDevolucionBean> lista) {
		List<String> listaObsTipoMaterial=null;
		List<String> listaObsRubro=null;
		List<String> listaObsTecnologia=null;
		List<String> listaContenido=null;
		String sqlTipoMat = null;
		String sqlRubro = null;
		String sqlTecnologia = null;
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rsTipoMat = null;
			ResultSet rsRubro = null;
			ResultSet rsTecnologia = null;
			listaObsTipoMaterial = new ArrayList<String>();
			listaObsRubro = new ArrayList<String>();
			listaObsTecnologia = new ArrayList<String>();
			listaContenido = new ArrayList<String>();
									
			for(int i=0;i<lista.size();i++){
				sqlTipoMat="SELECT NOMTIPOMATERIAL FROM TIPOMATERIAL WHERE TRIM(NOMTIPOMATERIAL)='" + lista.get(i).getTipo().trim().toUpperCase() + "'";
				rsTipoMat=st.executeQuery(sqlTipoMat);
				if(!rsTipoMat.next() && lista.get(i).getTipo().trim().length()>0){
					listaObsTipoMaterial.add("El Tipo de Material: " + lista.get(i).getTipo().trim() + " con serie: " + lista.get(i).getSerie().trim() +  " en la fila: " + (i+1) + " no existe");
				}
			}
			
			for(int i=0;i<lista.size();i++){
				sqlRubro="SELECT NOMBRERUBRO FROM RUBRO WHERE TRIM(NOMBRERUBRO)='" + lista.get(i).getRubro().trim().toUpperCase() + "'";
				rsRubro=st.executeQuery(sqlRubro);
				if(!rsRubro.next() && lista.get(i).getRubro().trim().length()>0){
					listaObsRubro.add("El Rubro del Material: " + lista.get(i).getRubro().trim() + " con serie: " + lista.get(i).getSerie().trim() +  " en la fila: " + (i+1) + " no existe");
				}
			}
			
			for(int i=0;i<lista.size();i++){
				sqlTecnologia="SELECT NOMTECNOLOGIA FROM TECNOLOGIA WHERE TRIM(NOMTECNOLOGIA)='" + lista.get(i).getTecnologia().trim().toUpperCase() + "'";
				rsTecnologia=st.executeQuery(sqlTecnologia);
				if(!rsTecnologia.next() && lista.get(i).getTecnologia().trim().length()>0){
					listaObsTecnologia.add("La Tecnologia del Material: " + lista.get(i).getTecnologia().trim() + " con serie: " + lista.get(i).getSerie().trim() +  " en la fila: " + (i+1) + " no existe");
				}
			}
			
			listaContenido.addAll(listaObsTipoMaterial);
			listaContenido.addAll(listaObsRubro);
			listaContenido.addAll(listaObsTecnologia);
		} catch (SQLException ex) {
			listaContenido = null;
			System.out.println("Ha surgido la siguiente Excepcion:" + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				System.out.println("Ha surgido la siguiente Excepcion:" + e.getMessage());
			}
		}
		return listaContenido;
	}

	@Override
	public List<String> validarCorrespondencia(List<DetalleFichaDevolucionBean> lista) {
		List<String> listaObsCorrespondencia=null;
		String sql="";

		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = null;
			listaObsCorrespondencia=new ArrayList<String>();
			for(int i=0;i<lista.size();i++){
				sql="SELECT TRIM(TP.NOMTIPOMATERIAL)||TRIM(RB.NOMBRERUBRO)||TRIM(TC.NOMTECNOLOGIA) FROM MATERIAL MT, TIPOMATERIAL TP, RUBRO RB, TECNOLOGIA TC" +
					" WHERE TP.CODTIPOMATERIAL=MT.CODTIPOMATERIAL AND RB.CODRUBRO=MT.CODRUBRO AND TC.CODTECNOLOGIA=MT.CODTECNOLOGIA AND TRIM(TP.NOMTIPOMATERIAL)='" + lista.get(i).getTipo().trim().toUpperCase() + "' AND trim(RB.NOMBRERUBRO)='" +  lista.get(i).getRubro().trim().toUpperCase() + "' AND TRIM(TC.NOMTECNOLOGIA)='" + lista.get(i).getTecnologia().trim().toUpperCase() + "'";
				rs=st.executeQuery(sql);
				int found=0;
				while(rs.next()){
					found=1;
					break;
				}
				
				if(found==0){
					listaObsCorrespondencia.add(lista.get(i).getTipo() + " - " + lista.get(i).getRubro() + " - " + lista.get(i).getTecnologia() + " - Fila: " + (i+1));
				}
			}

		} catch (SQLException ex) {
			listaObsCorrespondencia = null;
			System.out.println("Ha surgido la siguiente Excepcion:" + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				System.out.println("Ha surgido la siguiente Excepcion:" + e.getMessage());
			}
		}
		return listaObsCorrespondencia;
	}

	@Override
	public List<ErroresBean> obtenerTiposErroresFicha(List<DetalleFichaDevolucionBean> lista,String formato) {
		List<ErroresBean> lstTiposErrores=null;
		lstTiposErrores=new ArrayList<ErroresBean>();
		
		List<String> lstCantCampos=new ArrayList<String>();
		List<String> lstCamposVacios=new ArrayList<String>();
		List<String> lstCaracteresSeries=new ArrayList<String>();
		List<String> lstContenido=new ArrayList<String>();
		List<String> lstCorrespondencia=new ArrayList<String>();
		List<String> lstCantidadSeries=new ArrayList<String>();
		List<String> lstLongitudSeries=new ArrayList<String>();
		
		
		
		if(lista.get(0).getCodSAP()==null || (lista.get(0).getCantColumnasFicha()>ConstantesGenerales.CANTIDAD_COLUMNAS_FICHA || lista.get(0).getCantColumnasFicha()<ConstantesGenerales.CANTIDAD_COLUMNAS_FICHA)){
			lstCantCampos=validarCantidadCampos(lista);			
		}
		
		if(lista.get(0).getCantColumnasFicha()==ConstantesGenerales.CANTIDAD_COLUMNAS_FICHA){
			lstCamposVacios=validarCamposVacios(lista);
			lstCaracteresSeries=validarSeriesCaracteres(lista);
			lstContenido=validarContenido(lista);
			lstCorrespondencia=validarCorrespondencia(lista);
			lstLongitudSeries=validarLongitudSeries(lista);
			if(formato.equals(ConstantesGenerales.TipoFormatoDevolucion.OTROS.getTipoValor())){
				lstCantidadSeries=validarCantidadSeries(lista);
			}
		}
		
		ErroresBean erroresBean=null;
		if(lstCamposVacios.size()>0){
			erroresBean=new ErroresBean();
			erroresBean.setCodError(ConstantesGenerales.TipoErroresFicha.CAMPOSVACIOS.getTipoValor());
			erroresBean.setNomError("Campos Vacios");
			lstTiposErrores.add(erroresBean);
		}
		
		
		if(lstCaracteresSeries.size()>0){
			erroresBean=new ErroresBean();
			erroresBean.setCodError(ConstantesGenerales.TipoErroresFicha.CARACTERESSERIE.getTipoValor());
			erroresBean.setNomError("La serie contiene caracteres extraños");
			lstTiposErrores.add(erroresBean);
		}
		
		if(lstContenido.size()>0){
			erroresBean=new ErroresBean();
			erroresBean.setCodError(ConstantesGenerales.TipoErroresFicha.CONTENIDO.getTipoValor());
			erroresBean.setNomError("Los siguientes campos tienen contenido errado");
			lstTiposErrores.add(erroresBean);
		}
		
		if(lstCorrespondencia.size()>0){
			erroresBean=new ErroresBean();
			erroresBean.setCodError(ConstantesGenerales.TipoErroresFicha.CORRESPONDENCIA.getTipoValor());
			erroresBean.setNomError("Los siguientes campos tienen correspondencia errada");
			lstTiposErrores.add(erroresBean);
		}
		
		if(lstCantCampos.size()>0){
			erroresBean=new ErroresBean();
			erroresBean.setCodError(ConstantesGenerales.TipoErroresFicha.CANTIDADCAMPOS.getTipoValor());
			erroresBean.setNomError("Cantidad de Campos Incorrecto");
			lstTiposErrores.add(erroresBean);
		}

		if(lstCantidadSeries.size()>0){
			erroresBean=new ErroresBean();
			erroresBean.setCodError(ConstantesGenerales.TipoErroresFicha.CANTIDADSERIES.getTipoValor());
			erroresBean.setNomError("Cantidad de Series Invalidas");
			lstTiposErrores.add(erroresBean);
		}
		
		if(lstLongitudSeries.size()>0){
			erroresBean=new ErroresBean();
			erroresBean.setCodError(ConstantesGenerales.TipoErroresFicha.LONGITUDSERIES.getTipoValor());
			erroresBean.setNomError("Longitud de Series Invalidas");
			lstTiposErrores.add(erroresBean);
		}
				
		return lstTiposErrores;
	}

	@Override
	public List<String> obtenerResumenValidacion(String[] tickets) {
		Connection cn = null;
		int cantAprobados=0;
		int cantRechazados=0;
		int cantPendientes=0;
		int cantidadItems=0;
		String estado="";
		String nroTicket="";
		String formato="";
		String motivoDevol="";
		String entidad="";
		String sql=null;
		String sqlFicha=null;
		List<String> lstResumen=new ArrayList<String>();
		
		try {
			cn=dataSource.getConnection();	
			Statement st = cn.createStatement();
			ResultSet rs= null;
			for(int i=0;i<tickets.length;i++){
				cantAprobados=0;
				cantRechazados=0;
				cantPendientes=0;
				cantidadItems=0;

				sql="SELECT ESTADO FROM DETALLEFICHADEVOLUCION WHERE NROTICKET=" +  tickets[i];
				
				sqlFicha="SELECT FDEVOL.NROTICKET,TPFORM.NOMFORM,TDEVOL.NOMTIPODEVOL,ENT.NOMENTIDAD FROM FICHADEVOLUCION FDEVOL,TIPODEVOLUCION TDEVOL,TIPO_FORMATO TPFORM,ENTIDAD ENT " +   
						 "WHERE TDEVOL.CODTIPODEVOL=FDEVOL.CODTIPODEVOL AND TDEVOL.CODFORM=TPFORM.CODFORM AND FDEVOL.CODENTIDAD=ENT.CODENTIDAD AND FDEVOL.NROTICKET=" + tickets[i];
				
				rs=st.executeQuery(sql);
				
				while(rs.next()){
					cantidadItems=cantidadItems+1;
					if(rs.getString(1)==null){
						estado="";
					}else{
						estado=rs.getString(1);
					}
					
					if(estado.equals(ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor())){
						cantPendientes=cantPendientes+1;
					}

					if(estado.equals(ConstantesGenerales.EstadoValidacionActa.APROBADO.getTipoValor())){
						cantAprobados=cantAprobados+1;	
					}
					
					if(estado.equals(ConstantesGenerales.EstadoValidacionActa.RECHAZADO.getTipoValor())){
						cantRechazados=cantRechazados+1;	
					}
					
				}
				
				rs=null;
				rs=st.executeQuery(sqlFicha);
				if(rs.next()){
					nroTicket=rs.getString(1)==null?"":rs.getString(1);
					formato=rs.getString(2)==null?"":rs.getString(2);
					motivoDevol=rs.getString(3)==null?"":rs.getString(3);
					entidad=rs.getString(4)==null?"":rs.getString(4);
				}
				lstResumen.add("<tr><td>" + nroTicket + "</td><td>" + formato + " - " + motivoDevol + "</td><td>" + entidad + "</td><td>" +  cantAprobados + "</td><td>" + cantRechazados + "</td><td>" + cantPendientes + "</td><td>" + cantidadItems + "</td></tr>");				
				
			}

		} catch (SQLException e) {
			System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());			
		}finally{
			try{
				cn.close();
			}catch(Exception e){
				System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());
			}
		}
		
		return lstResumen;
	}

	@Override
	public List<String> validarCantidadCampos(List<DetalleFichaDevolucionBean> lista) {
		List<String> lstCantCamposFicha=new ArrayList<String>();
		if(lista.size()==1){
			if(lista.get(0).getCantColumnasFicha()!=ConstantesGenerales.CANTIDAD_COLUMNAS_FICHA){
				lstCantCamposFicha.add("La cantidad de columnas necesarias para la Ficha son: <strong>" + ConstantesGenerales.CANTIDAD_COLUMNAS_FICHA + " </strong> pero ha ingresado <strong>" + lista.get(0).getCantColumnasFicha() + " </strong> columnas");
			}
		}

		return lstCantCamposFicha;

	}


	@Override
	public void actualizarDetallexStatusxAcopio(List<DetalleFichaDevolucionBean> listaDeta, List<FichaDevolucionBean> listaFicha) {
		//VERIFICO LA EXISTENCIA EN STATUSTRX Y LUEGO EN ACOPIO		
		Connection cn = null;
		PreparedStatement pstmt = null;
		Util util=new Util();
		SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
		
		String serie=null;
		String sqlQry=null;
		String sqlExistenciaStatus=null;
		String sqlExistenciaAcopio=null;
		String sqlUpdateStatusAcopio=null;
		String fechaRecepcion=null;
		String fechaRecepcionPostAcopio=null;
		String fechaValidacion=null;
		String fechaSolicitud=null;
		String ticketStatus=null;
		String estado=null;
		
		Date fechaRecepcionPostAcopioDate = new Date();
		Date fechaSolicitudDate = new Date();
		
		int contAprobado=0;
		int contRechazado=0;
		int contPendiente=0;
		int cantidadItems=0;
		try{
			ResultSet rs=null;
			cn=dataSource.getConnection();
			Statement stQry=cn.createStatement();
			
			for(int f=0;f<listaFicha.size();f++){
				if(listaFicha.get(f).getTipoFormato().equals(ConstantesGenerales.TipoFormatoDevolucion.SERIADOS.getTipoValor())){					
					for(int x=0;x<listaDeta.size();x++){
						fechaValidacion=listaDeta.get(x).getFechaValidacion().substring(0, 10);
						fechaSolicitud=listaDeta.get(x).getFechaSolicitud().substring(0,10);
						sqlExistenciaStatus="SELECT SERIE,TICKET FROM STATUSTRXHISTORIAL WHERE LPAD(TRIM(UPPER(SERIE)),25,'0')='" + util.rellenar(listaDeta.get(x).getSerie().trim().toUpperCase(), 25) + "' AND TRIM(ESTADO)='" + ConstantesGenerales.EstadoValidacionActa.APROBADO.getTipoValor() + "' AND " +
								  "TO_DATE(fechavali,'DD/MM/YY') BETWEEN TO_DATE('" + util.obtenerFechaAnteriorHistorial(3, fechaValidacion) + "','DD/MM/YY') AND TO_DATE('" + util.obtenerFechaAnteriorHistorial(fechaValidacion) + "','DD/MM/YY') AND SERIE NOT IN('" + ConstantesGenerales.SINSERIE + "')";
						rs=stQry.executeQuery(sqlExistenciaStatus);
						if(rs.next()){
							serie=rs.getString(1);
							ticketStatus=rs.getString(2);
							rs=null;
							sqlExistenciaAcopio="SELECT FECHARECEP FROM BDACOPIO WHERE LPAD(TRIM(UPPER(SERIEPROG)),25,'0')='" + util.rellenar(serie.trim().toUpperCase(), 25) +  "' "; 
							rs=stQry.executeQuery(sqlExistenciaAcopio);
							if(rs.next()){//Verificamos si existe la serie en la Base Arturo, sino existiera le avisamos a Arturo.
								rs=null;
								sqlExistenciaAcopio="SELECT FECHARECEP FROM BDACOPIO WHERE LPAD(TRIM(UPPER(SERIEPROG)),25,'0')='" + util.rellenar(serie.trim().toUpperCase(), 25) +  "' " + 
													"AND RECEPCIONADO='" + ConstantesGenerales.RecepcionAcopio.REPEPCIONADO.getTipoValor() + "' ORDER BY TO_DATE(fecharecep,'DD/MM/YY') DESC";
								rs=stQry.executeQuery(sqlExistenciaAcopio);
								if(rs.next()){
									fechaRecepcion=rs.getString(1);	
									fechaRecepcionPostAcopio=util.obtenerFechaPosteriorAcopio(3,fechaRecepcion);
									fechaRecepcionPostAcopioDate=formatoFecha.parse(fechaRecepcionPostAcopio);
									fechaSolicitudDate=formatoFecha.parse(fechaSolicitud);
									if(fechaRecepcionPostAcopioDate.after(fechaSolicitudDate)){
										sqlUpdateStatusAcopio="UPDATE DETALLEFICHADEVOLUCION SET ESTADO='" + ConstantesGenerales.EstadoValidacionActa.RECHAZADO.getTipoValor() + "', MOTIVORECHAZODEVOLUCION='RECHAZADO PORQUE YA ESTA RECEPCIONADO EN ACOPIO CON EL TICKET: " + ticketStatus  + "'," + 
														   	  "OBSERVACIONAPROBADO='' WHERE NROTICKET='" + listaDeta.get(x).getNroTicket() + "' AND LPAD(TRIM(UPPER(SERIE)),25,'0')='" + util.rellenar(listaDeta.get(x).getSerie().trim().toUpperCase(), 25)  + "' AND NRO=" + listaDeta.get(x).getNro();										
										cn.setAutoCommit(false);
										pstmt=cn.prepareStatement(sqlUpdateStatusAcopio);
										pstmt.executeUpdate();
										cn.commit();
									}
								}
							}else{
								sqlUpdateStatusAcopio="UPDATE DETALLEFICHADEVOLUCION SET ESTADO='" + ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor() + "', MOTIVORECHAZODEVOLUCION='PENDIENTE YA QUE EL MATERIAL NO SE ENCUENTRA EN LA BASE DE ACOPIO. AVISAR AL ENCARGADO', " + 
							   		   			  "OBSERVACIONAPROBADO='' WHERE NROTICKET='" + listaDeta.get(x).getNroTicket() + "' AND LPAD(TRIM(UPPER(SERIE)),25,'0')='" + util.rellenar(listaDeta.get(x).getSerie().trim().toUpperCase(), 25)  + "' AND NRO=" + listaDeta.get(x).getNro();										
								cn.setAutoCommit(false);
								pstmt=cn.prepareStatement(sqlUpdateStatusAcopio);
								pstmt.executeUpdate();
								cn.commit();
							}
						}else{
							sqlExistenciaStatus="SELECT SERIE,NROTICKET FROM DETALLEFICHADEVOLUCION WHERE LPAD(TRIM(UPPER(SERIE)),25,'0')='" + util.rellenar(listaDeta.get(x).getSerie().trim().toUpperCase(), 25) + "' AND TRIM(ESTADO)='" + ConstantesGenerales.EstadoValidacionActa.APROBADO.getTipoValor() + "' AND " +
											  "TO_DATE(FECHAVALIDACION,'DD/MM/YY') BETWEEN TO_DATE('" + util.obtenerFechaAnteriorHistorial(3, fechaValidacion) + "','DD/MM/YY') AND TO_DATE('" + util.obtenerFechaAnteriorHistorial(fechaValidacion) + "','DD/MM/YY') AND SERIE NOT IN('" + ConstantesGenerales.SINSERIE + "')";
							rs=stQry.executeQuery(sqlExistenciaStatus);
							if(rs.next()){
								serie=rs.getString(1);
								ticketStatus=rs.getString(2);
								rs=null;
								sqlExistenciaAcopio="SELECT FECHARECEP FROM BDACOPIO WHERE LPAD(TRIM(UPPER(SERIEPROG)),25,'0')='" + util.rellenar(serie.trim().toUpperCase(), 25) +  "' "; 
								rs=stQry.executeQuery(sqlExistenciaAcopio);
								if(rs.next()){//Verificamos si existe la serie en la Base Arturo, sino existiera le avisamos a Arturo.
									rs=null;
									sqlExistenciaAcopio="SELECT FECHARECEP FROM BDACOPIO WHERE LPAD(TRIM(UPPER(SERIEPROG)),25,'0')='" + util.rellenar(serie.trim().toUpperCase(), 25) +  "' " + 
														"AND RECEPCIONADO='" + ConstantesGenerales.RecepcionAcopio.REPEPCIONADO.getTipoValor() + "' ORDER BY TO_DATE(fecharecep,'DD/MM/YY') DESC";
									rs=stQry.executeQuery(sqlExistenciaAcopio);
									if(rs.next()){
										fechaRecepcion=rs.getString(1);	
										fechaRecepcionPostAcopio=util.obtenerFechaPosteriorAcopio(3,fechaRecepcion);
										fechaRecepcionPostAcopioDate=formatoFecha.parse(fechaRecepcionPostAcopio);
										fechaSolicitudDate=formatoFecha.parse(fechaSolicitud);
										if(fechaRecepcionPostAcopioDate.after(fechaSolicitudDate)){
											sqlUpdateStatusAcopio="UPDATE DETALLEFICHADEVOLUCION SET ESTADO='" + ConstantesGenerales.EstadoValidacionActa.RECHAZADO.getTipoValor() + "', MOTIVORECHAZODEVOLUCION='RECHAZADO PORQUE YA ESTA RECEPCIONADO EN ACOPIO CON EL TICKET: " + ticketStatus  + "'," + 
																"OBSERVACIONAPROBADO='' WHERE NROTICKET='" + listaDeta.get(x).getNroTicket() + "' AND LPAD(TRIM(UPPER(SERIE)),25,'0')='" + util.rellenar(listaDeta.get(x).getSerie().trim().toUpperCase(), 25)  + "' AND NRO=" + listaDeta.get(x).getNro();										
											cn.setAutoCommit(false);
											pstmt=cn.prepareStatement(sqlUpdateStatusAcopio);
											pstmt.executeUpdate();
											cn.commit();
										}
									}
								}else{
									sqlUpdateStatusAcopio="UPDATE DETALLEFICHADEVOLUCION SET ESTADO='" + ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor() + "', MOTIVORECHAZODEVOLUCION='PENDIENTE YA QUE EL MATERIAL NO SE ENCUENTRA EN LA BASE DE ACOPIO. AVISAR AL ENCARGADO', " + 
								   		   			  "OBSERVACIONAPROBADO='' WHERE NROTICKET='" + listaDeta.get(x).getNroTicket() + "' AND LPAD(TRIM(UPPER(SERIE)),25,'0')='" + util.rellenar(listaDeta.get(x).getSerie().trim().toUpperCase(), 25)  + "' AND NRO=" + listaDeta.get(x).getNro();										
									cn.setAutoCommit(false);
									pstmt=cn.prepareStatement(sqlUpdateStatusAcopio);
									pstmt.executeUpdate();
									cn.commit();
								}		
							}									
						}		
					}
				}
			}
		
	} catch (SQLException e) {
		System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());
		
	} catch (ParseException e) {
		System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());
	}finally{
		try{
			cn.close();
		}catch(Exception e){
			System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());
		}
	}
		
	}


	@Override
	public List<String> validarCantidadSeries(List<DetalleFichaDevolucionBean> lista) {
		List<String> lstCantidadSeries=new ArrayList<String>();
		if(lista.size()>ConstantesGenerales.CANTIDAD_SERIES_TEMPRESAS){
			lstCantidadSeries.add("Para el caso de TEMPRESA la cantidad maxima de registros son: <strong>" + ConstantesGenerales.CANTIDAD_SERIES_TEMPRESAS + " </strong> pero ha ingresado <strong>" + lista.size() + " </strong> columnas");
		}

		return lstCantidadSeries;
	}


	@Override
	public List<String> obtenerResumenValidacionMsg(String ticket) {
		Connection cn = null;
		int cantidadItems=0;
		String estado="";
		String nroTicket="";
		String sql="";
		List<String> lstResumen=new ArrayList<String>();
		
		try {
			cn=dataSource.getConnection();	
			Statement st = cn.createStatement();
			ResultSet rs= null;
			
			sql="SELECT ESTADO,COUNT(ESTADO) FROM DETALLEFICHADEVOLUCION WHERE NROTICKET='" + ticket  + "' GROUP BY ESTADO ORDER BY ESTADO"; 
			
			rs=st.executeQuery(sql);
			
			while(rs.next()){
				estado=rs.getString(1)==null?"":rs.getString(1);
				cantidadItems=rs.getInt(2);
				lstResumen.add(estado + " : " + cantidadItems);	
			}

		} catch (SQLException e) {
			System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());			
		}finally{
			try{
				cn.close();
			}catch(Exception e){
				System.out.println("Se suscito la siguiente Excepcion:" + e.getMessage());
			}
		}
		
		return lstResumen;
	}


	@Override
	public List<String> validarLongitudSeries(List<DetalleFichaDevolucionBean> lista) {
		List<String> lstLongitudSeries=new ArrayList<String>();
		String serie="";
		for(int i=0;i<lista.size();i++){
			serie=lista.get(i).getSerie().trim().toUpperCase();
			if(serie.length()>ConstantesGenerales.LONGITUD_SERIES_MAX){
				lstLongitudSeries.add("La longitud maxima para el ingreso de Series es de: <strong>" + ConstantesGenerales.LONGITUD_SERIES_MAX + "</strong>, pero ha ingresado <strong>" + serie.length() + "</strong> series" );
			}			
		}		

		return lstLongitudSeries;
	}

	
}

