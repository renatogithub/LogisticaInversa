package pe.tgestiona.logistica.inversa.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import pe.tgestiona.logistica.inversa.bean.BaseGarantiaBean;
import pe.tgestiona.logistica.inversa.bean.BaseValidacionExternaBean;
import pe.tgestiona.logistica.inversa.bean.ConfiguracionBean;
import pe.tgestiona.logistica.inversa.bean.ConstantesGenerales;
import pe.tgestiona.logistica.inversa.bean.DespachoRRSSBean;
import pe.tgestiona.logistica.inversa.bean.DetalleFichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.FichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.MaterialBean;
import pe.tgestiona.logistica.inversa.bean.MaterialSAPBean;
import pe.tgestiona.logistica.inversa.bean.TallerBean;
import pe.tgestiona.logistica.inversa.bean.TipoBodegaBean;
import pe.tgestiona.logistica.inversa.dao.ValidacionDao;
import pe.tgestiona.logistica.inversa.util.Util;

@Repository
public class ValidacionDaoImpl implements ValidacionDao{

	@Autowired
	private DriverManagerDataSource dataSource;

	@Override
	public DespachoRRSSBean existeRRSS(String serie, String material) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TallerBean existeGarantiaTaller(String serie, String material) {
		Util util=new Util();
		TallerBean tallerBean=null;		
		String sql="SELECT FECHARECEP,REMOZ_REPARADO FROM TALLER WHERE LPAD(TRIM(UPPER(SERIE)),25,'0')='" + util.rellenar(serie, 25) + "' AND LPAD(TRIM(UPPER(MATERIAL)),20,'0')='" + util.rellenar(material.trim(), 20) + "' AND REMOZ_REPARADO IN ('REMOZADO','REPARADO') ORDER BY FECHARECEP DESC";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				tallerBean = new TallerBean();
				tallerBean.setFechaRecep(rs.getString(1));
				tallerBean.setRemoz_reparado(rs.getString(2));
			}
		} catch (SQLException ex) {
			tallerBean = null;
            System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
	            System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		return tallerBean;
	}
	
	@Override
	public MaterialSAPBean obtenerDatosSAP(String serie, String tipo,String rubro, String tecnologia,String tipoSAP,String garantia,String descripcionActa,String usuario) {
		
		String sql = "SELECT MSAP.NROSERIE,MSAP.CENTRO,MSAP.ALMACEN,MSAP.CODMATERIAL,MSAP.DENOMINACION,MSAP.LOTE,MSAP.TIPOSTOCKS,MSAP.STATUSSISTEMA,MSAP.STATUSUSUARIO,MSAP.MODIFICADOEL,MSAP.MODIFICADOPOR,MSAP.CREADOEL,MSAP.CREADOPOR," + 
			 	 	 "MSAP.LIBRE_UTILIZACION FROM MATERIALSAP MSAP,MATERIAL MAT,NEGOCIO NEG,RUBRO RUB,TIPOMATERIAL TPMAT,TECNOLOGIA TEC,SOCIEDAD SOC,PROVISION_PLANTAEXTERNA PROV " +  
			 	 	 "WHERE LPAD(TRIM(UPPER(MSAP.CODMATERIAL)),20,'0')=LPAD(TRIM(MAT.CODMATERIAL),20,'0') AND MAT.CODNEGOCIO=NEG.CODNEGOCIO AND MAT.CODRUBRO=RUB.CODRUBRO AND MAT.CODTECNOLOGIA=TEC.CODTECNOLOGIA AND MAT.CODTIPOMATERIAL=TPMAT.CODTIPOMATERIAL AND SOC.CODSOCIEDAD=MAT.SOCIEDAD AND PROV.CODPROV=MAT.CODPROV AND MSAP.TIPO='" + tipoSAP + "' AND MSAP.USUARIO='" + usuario + "' ";				
		
		String sqlFiltro=null;
		String sqlOrdena=null;
		String sqlTotal=null;
		MaterialSAPBean materialSAPBean=null;
		Util util=new Util();
		
		if(garantia.equals(ConstantesGenerales.SIN_GARANTIA_SAP)){
			sqlOrdena=" ORDER BY MSAP.NROSERIE,TO_DATE(MSAP.MODIFICADOEL) DESC";
		}
		
		if(garantia.equals(ConstantesGenerales.GARANTIA_SAP)){
			sqlOrdena=" ORDER BY MSAP.NROSERIE,TO_DATE(MSAP.CREADOEL) ASC";
		}
		
		String sqlStandar="SELECT MAT.DESCRIPCION_ESTANDAR FROM MATERIAL MAT,TECNOLOGIA TEC,RUBRO RUB,TIPOMATERIAL TPMAT WHERE MAT.CODRUBRO=RUB.CODRUBRO AND MAT.CODTIPOMATERIAL=TPMAT.CODTIPOMATERIAL AND MAT.CODTECNOLOGIA=TEC.CODTECNOLOGIA " + 
						  " AND MAT.CODMATERIAL LIKE 'MST%'";
		
		Connection cn = null;
	
			try {
				cn = dataSource.getConnection();
				Statement st = cn.createStatement();
				ResultSet rsFiltro = null;
				ResultSet rsStandar = null;
				
				sqlFiltro=" AND LPAD(TRIM(UPPER(MSAP.NROSERIE)),25,'0')='" + util.rellenar(serie.toUpperCase().trim(), 25) + "' AND TRIM(UPPER(TPMAT.NOMTIPOMATERIAL))=TRIM(UPPER('" + tipo + "')) AND TRIM(UPPER(RUB.NOMBRERUBRO))=TRIM(UPPER('" + rubro  + "')) AND TRIM(UPPER(TEC.NOMTECNOLOGIA))=TRIM(UPPER('" +  tecnologia + "'))  ";					

				sqlTotal=sql+ sqlFiltro + sqlOrdena;
				rsFiltro=st.executeQuery(sqlTotal);
				
				if(rsFiltro.next()){
					materialSAPBean=new MaterialSAPBean();
					
										
					if(rsFiltro.getString(1)==null){
						materialSAPBean.setNumeroSerie(ConstantesGenerales.GUION);						
					}else{
						materialSAPBean.setNumeroSerie(rsFiltro.getString(1));						
					}
					if(rsFiltro.getString(2)==null){
						materialSAPBean.setCentro(ConstantesGenerales.GUION);
					}else{
						materialSAPBean.setCentro(rsFiltro.getString(2));						
					}
					
					if(rsFiltro.getString(3)==null){
						materialSAPBean.setAlmacen(ConstantesGenerales.GUION);
					}else{
						materialSAPBean.setAlmacen(rsFiltro.getString(3));
					}

					if(rsFiltro.getString(4)==null){
						materialSAPBean.setCodMaterial(ConstantesGenerales.GUION);
					}else{
						materialSAPBean.setCodMaterial(rsFiltro.getString(4));
					}
					
					if(rsFiltro.getString(5)==null){
						materialSAPBean.setDenominacion(ConstantesGenerales.GUION);
					}else{
						materialSAPBean.setDenominacion(rsFiltro.getString(5));
					}
					
					if(rsFiltro.getString(6)==null){
						materialSAPBean.setLote(ConstantesGenerales.GUION);
					}else{
						materialSAPBean.setLote(rsFiltro.getString(6));
					}
					
					if(rsFiltro.getString(7)==null){
						materialSAPBean.setTipoStocks(ConstantesGenerales.GUION);
					}else{
						materialSAPBean.setTipoStocks(rsFiltro.getString(7));
					}
					
					if(rsFiltro.getString(8)==null){
						materialSAPBean.setStatuSistema(ConstantesGenerales.GUION);
					}else{
						materialSAPBean.setStatuSistema(rsFiltro.getString(8));
					}
					
					if(rsFiltro.getString(9)==null){
						materialSAPBean.setStatusUsuario(ConstantesGenerales.GUION);
					}else{
						materialSAPBean.setStatusUsuario(rsFiltro.getString(9));
					}
					
					if(rsFiltro.getString(10)==null){
						materialSAPBean.setModificadoEl(ConstantesGenerales.GUION);
					}else{
						materialSAPBean.setModificadoEl(rsFiltro.getString(10));
					}
					
					if(rsFiltro.getString(11)==null){
						materialSAPBean.setModificadoPor(ConstantesGenerales.GUION);
					}else{
						materialSAPBean.setModificadoPor(rsFiltro.getString(11));
					}
					
					if(rsFiltro.getString(12)==null){
						materialSAPBean.setCreadoEl(ConstantesGenerales.GUION);
					}else{
						materialSAPBean.setCreadoEl(rsFiltro.getString(12));
					}
					
					if(rsFiltro.getString(13)==null){
						materialSAPBean.setCreadoPor(ConstantesGenerales.GUION);
					}else{
						materialSAPBean.setCreadoPor(rsFiltro.getString(13));
					}		
					
					if(rsFiltro.getString(14)==null){
						materialSAPBean.setLibreUtilizacion(1.0);
					}else{
						materialSAPBean.setLibreUtilizacion(Double.parseDouble(rsFiltro.getString(14)));
					}							
					
					materialSAPBean.setStatusSAP(ConstantesGenerales.ExistenciaSAP.EXISTESAP.getTipoValor());
					materialSAPBean.setObservacionBusqueda(ConstantesGenerales.GUION);
				}else{
					if(rubro.equals(ConstantesGenerales.RUBRO_DECO) || rubro.equals(ConstantesGenerales.RUBRO_TELEFONO)){
						sqlFiltro=" AND LPAD(TRIM(UPPER(MSAP.NROSERIE)),25,'0')='" + util.rellenar(serie.toUpperCase().trim(), 25) + "' AND TRIM(UPPER(TPMAT.NOMTIPOMATERIAL))=TRIM(UPPER('" + tipo + "')) AND TRIM(UPPER(RUB.NOMBRERUBRO))=TRIM(UPPER('" + rubro  + "')) ";					
						sqlTotal=sql + sqlFiltro + sqlOrdena;
						rsFiltro=st.executeQuery(sqlTotal);
						if(rsFiltro.next()){
							materialSAPBean=new MaterialSAPBean();
												
							if(rsFiltro.getString(1)==null){
								materialSAPBean.setNumeroSerie(ConstantesGenerales.GUION);						
							}else{
								materialSAPBean.setNumeroSerie(rsFiltro.getString(1));						
							}
							if(rsFiltro.getString(2)==null){
								materialSAPBean.setCentro(ConstantesGenerales.GUION);
							}else{
								materialSAPBean.setCentro(rsFiltro.getString(2));						
							}
							
							if(rsFiltro.getString(3)==null){
								materialSAPBean.setAlmacen(ConstantesGenerales.GUION);
							}else{
								materialSAPBean.setAlmacen(rsFiltro.getString(3));
							}

							if(rsFiltro.getString(4)==null){
								materialSAPBean.setCodMaterial(ConstantesGenerales.GUION);
							}else{
								materialSAPBean.setCodMaterial(rsFiltro.getString(4));
							}
							
							if(rsFiltro.getString(5)==null){
								materialSAPBean.setDenominacion(ConstantesGenerales.GUION);
							}else{
								materialSAPBean.setDenominacion(rsFiltro.getString(5));
							}
							
							if(rsFiltro.getString(6)==null){
								materialSAPBean.setLote(ConstantesGenerales.GUION);
							}else{
								materialSAPBean.setLote(rsFiltro.getString(6));
							}
							
							if(rsFiltro.getString(7)==null){
								materialSAPBean.setTipoStocks(ConstantesGenerales.GUION);
							}else{
								materialSAPBean.setTipoStocks(rsFiltro.getString(7));
							}
							
							if(rsFiltro.getString(8)==null){
								materialSAPBean.setStatuSistema(ConstantesGenerales.GUION);
							}else{
								materialSAPBean.setStatuSistema(rsFiltro.getString(8));
							}
							
							if(rsFiltro.getString(9)==null){
								materialSAPBean.setStatusUsuario(ConstantesGenerales.GUION);
							}else{
								materialSAPBean.setStatusUsuario(rsFiltro.getString(9));
							}
							
							if(rsFiltro.getString(10)==null){
								materialSAPBean.setModificadoEl(ConstantesGenerales.GUION);
							}else{
								materialSAPBean.setModificadoEl(rsFiltro.getString(10));
							}
							
							if(rsFiltro.getString(11)==null){
								materialSAPBean.setModificadoPor(ConstantesGenerales.GUION);
							}else{
								materialSAPBean.setModificadoPor(rsFiltro.getString(11));
							}
							
							if(rsFiltro.getString(12)==null){
								materialSAPBean.setCreadoEl(ConstantesGenerales.GUION);
							}else{
								materialSAPBean.setCreadoEl(rsFiltro.getString(12));
							}
							
							if(rsFiltro.getString(13)==null){
								materialSAPBean.setCreadoPor(ConstantesGenerales.GUION);
							}else{
								materialSAPBean.setCreadoPor(rsFiltro.getString(13));
							}		
							
							if(rsFiltro.getString(14)==null){
								materialSAPBean.setLibreUtilizacion(1.0);
							}else{
								materialSAPBean.setLibreUtilizacion(Double.parseDouble(rsFiltro.getString(14)));
							}							
							
							materialSAPBean.setStatusSAP(ConstantesGenerales.ExistenciaSAP.EXISTESAP.getTipoValor());
							materialSAPBean.setObservacionBusqueda("ACTA INDICA: " +  descripcionActa + " Y EL SAP:" + materialSAPBean.getDenominacion());
						}else{//Se estandariza la busqueda
							sqlTotal=sqlStandar + " AND TRIM(UPPER(TPMAT.NOMTIPOMATERIAL))=TRIM(UPPER('" + tipo + "')) AND TRIM(UPPER(RUB.NOMBRERUBRO))=TRIM(UPPER('" + rubro  + "')) AND TRIM(UPPER(TEC.NOMTECNOLOGIA))=TRIM(UPPER('" +  tecnologia + "'))  ";	
							rsStandar=st.executeQuery(sqlTotal);
							materialSAPBean=new MaterialSAPBean();	

							if(rsStandar.next()){																					
								materialSAPBean.setDenominacion(rsStandar.getString(1));
							}else{
								sqlTotal=sqlStandar + " AND TRIM(UPPER(TPMAT.NOMTIPOMATERIAL))=TRIM(UPPER('" + tipo + "')) AND TRIM(UPPER(RUB.NOMBRERUBRO))=TRIM(UPPER('" + rubro  + "')) ";
								rsStandar=st.executeQuery(sqlTotal);
								materialSAPBean=new MaterialSAPBean();
								if(rsStandar.next()){						
									materialSAPBean.setDenominacion(rsStandar.getString(1));
								}else{
									materialSAPBean.setDenominacion(ConstantesGenerales.GUION);	
								}
							}
							
							materialSAPBean.setNumeroSerie(ConstantesGenerales.GUION);
							materialSAPBean.setCentro(ConstantesGenerales.GUION);
							materialSAPBean.setAlmacen(ConstantesGenerales.GUION);
							materialSAPBean.setCodMaterial(ConstantesGenerales.GUION);						
							materialSAPBean.setLote(ConstantesGenerales.GUION);
							materialSAPBean.setTipoStocks(ConstantesGenerales.GUION);
							materialSAPBean.setStatuSistema(ConstantesGenerales.GUION);
							materialSAPBean.setStatusUsuario(ConstantesGenerales.GUION);
							materialSAPBean.setModificadoEl(ConstantesGenerales.GUION);
							materialSAPBean.setModificadoPor(ConstantesGenerales.GUION);					
							materialSAPBean.setCreadoEl(ConstantesGenerales.GUION);
							materialSAPBean.setCreadoPor(ConstantesGenerales.GUION);
							materialSAPBean.setLibreUtilizacion(0.0);
							materialSAPBean.setStatusSAP(ConstantesGenerales.ExistenciaSAP.NOEXISTESAP.getTipoValor());
							materialSAPBean.setObservacionBusqueda(ConstantesGenerales.GUION);
						}					
					}else{
						sqlTotal=sqlStandar + " AND TRIM(UPPER(TPMAT.NOMTIPOMATERIAL))=TRIM(UPPER('" + tipo + "')) AND TRIM(UPPER(RUB.NOMBRERUBRO))=TRIM(UPPER('" + rubro  + "')) AND TRIM(UPPER(TEC.NOMTECNOLOGIA))=TRIM(UPPER('" +  tecnologia + "'))  ";	
						rsStandar=st.executeQuery(sqlTotal);
						materialSAPBean=new MaterialSAPBean();	

						if(rsStandar.next()){																					
							materialSAPBean.setDenominacion(rsStandar.getString(1));
						}else{
							sqlTotal=sqlStandar + " AND TRIM(UPPER(TPMAT.NOMTIPOMATERIAL))=TRIM(UPPER('" + tipo + "')) AND TRIM(UPPER(RUB.NOMBRERUBRO))=TRIM(UPPER('" + rubro  + "')) ";
							rsStandar=st.executeQuery(sqlTotal);
							materialSAPBean=new MaterialSAPBean();
							if(rsStandar.next()){						
								materialSAPBean.setDenominacion(rsStandar.getString(1));
							}else{
								materialSAPBean.setDenominacion(ConstantesGenerales.GUION);	
							}
						}
						
						materialSAPBean.setNumeroSerie(ConstantesGenerales.GUION);
						materialSAPBean.setCentro(ConstantesGenerales.GUION);
						materialSAPBean.setAlmacen(ConstantesGenerales.GUION);
						materialSAPBean.setCodMaterial(ConstantesGenerales.GUION);						
						materialSAPBean.setLote(ConstantesGenerales.GUION);
						materialSAPBean.setTipoStocks(ConstantesGenerales.GUION);
						materialSAPBean.setStatuSistema(ConstantesGenerales.GUION);
						materialSAPBean.setStatusUsuario(ConstantesGenerales.GUION);
						materialSAPBean.setModificadoEl(ConstantesGenerales.GUION);
						materialSAPBean.setModificadoPor(ConstantesGenerales.GUION);					
						materialSAPBean.setCreadoEl(ConstantesGenerales.GUION);
						materialSAPBean.setCreadoPor(ConstantesGenerales.GUION);
						materialSAPBean.setLibreUtilizacion(0.0);
						materialSAPBean.setStatusSAP(ConstantesGenerales.ExistenciaSAP.NOEXISTESAP.getTipoValor());
						materialSAPBean.setObservacionBusqueda(ConstantesGenerales.GUION);
					}
				}			
				
			} catch (SQLException e) {
	            System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
			
		
		
		return materialSAPBean;
	}
	
	@Override
	public List<ConfiguracionBean> obtenerConfiguraciones(String motivo,String tipoConf) {
		List<ConfiguracionBean> lstConf=null;
		String sql="SELECT CRITERIO,CODCONF,VALOR,PARAMETRO,EXISTENCIA FROM DETALLECONFIGURACION WHERE CRITERIO='" + motivo + "' AND CODCONF='" + tipoConf + "'";
		System.out.println(sql);
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			lstConf=new ArrayList<ConfiguracionBean>();
			while (rs.next()) {
				ConfiguracionBean configuracionBean = new ConfiguracionBean();
				configuracionBean.setCodCriterio(rs.getString(1));
				configuracionBean.setCodConfiguracion(rs.getString(2));
				configuracionBean.setValor(rs.getString(3));
				configuracionBean.setParametro(rs.getString(4));
				configuracionBean.setExistencia(rs.getString(5));
				lstConf.add(configuracionBean);
			}
		} catch (SQLException ex) {
			lstConf = null;
            System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
	            System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
				
		return lstConf;
		
	}
	
	
	//Este es el Validar
	@Override
	public List<DetalleFichaDevolucionBean> validarItems(List<FichaDevolucionBean> listaFichaDevol,String estadoValidacion,String usuario) {
		Util util=new Util();
		//Creamos los objetos para almacenar la data de SAP 6.0 y 4.7 y de Garantia
		MaterialSAPBean materialSAPBean6_0=null; //Obtenemos los Datos del SAP 6.0 (el de la fecha mas reciente) (12 columnas)
		MaterialSAPBean materialSAPBean4_7=null; //Obtenemos los Datos del SAP 4.7 (el de la fecha mas reciente) (12 columnas)
		MaterialSAPBean materialSAPBean6_0_Devoluci=null; //Obtenemos los Datos del SAP 6.0 Devoluci (6 columnas)
		MaterialSAPBean materialSAPBean4_7_Devoluci=null; //Obtenemos los Datos del SAP 4.7 Devoluci (6 columnas)
		MaterialSAPBean materialSAPBeanGarantia=null; //Obtenemos la informacion del SAP para la Garantia (el de la fecha mas antigua)
		MaterialBean materialBean=null; //Obtenemos los Datos de la Tabla Material		
		Date fDevol_Correo_Proveedor=null;
		Date fDevol_Correo_Taller=null;
		Date fCreadoEl=null;
		Date fTaller=null;

		DateFormat formatDate = new SimpleDateFormat("dd/MM/yy"); //Fecha Formato
		DetalleFichaDevolucionBean detalleFichaDevolucionBean=null; //El objeto donde se almacenara la informacion del Detalle
		List<DetalleFichaDevolucionBean> listaDetalleFicha=new ArrayList<DetalleFichaDevolucionBean>();		
		long dif=0; 
		long dias=0;		
		int cont=0;
		String sql="";

		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs= null;
			
			for(int t=0;t<listaFichaDevol.size();t++){ //Recorremos cada uno de los items de la Ficha
				//Capturamos los datos de Cabecera
				cont=0;
				String formato=listaFichaDevol.get(t).getTipoFormato();
				String motivo=listaFichaDevol.get(t).getTipoDevolucion();
				String entidad=listaFichaDevol.get(t).getEntidad();
				String esPedido=listaFichaDevol.get(t).getEsPedido();
				String enviado=listaFichaDevol.get(t).getEnviado();
				String fechaSolicitud=listaFichaDevol.get(t).getFechaSolicitud();
				String fechaDevolucion=listaFichaDevol.get(t).getFechaDevolucion();
				String mesDevolucion=listaFichaDevol.get(t).getMesDevolucion();
				//Capturamos las configuraciones
				List<ConfiguracionBean> lstConfBodegas=obtenerConfiguraciones(motivo, ConstantesGenerales.TipoConfiguracion.BODEGA.getTipoValor());
				List<ConfiguracionBean> lstConfLote=obtenerConfiguraciones(motivo, ConstantesGenerales.TipoConfiguracion.LOTE.getTipoValor());
				List<ConfiguracionBean> lstConfGarantiaProveedor=obtenerConfiguraciones(motivo, ConstantesGenerales.TipoConfiguracion.GARANTIAPROVEEDORES.getTipoValor());
				List<ConfiguracionBean> lstConfGarantiaTaller=obtenerConfiguraciones(motivo, ConstantesGenerales.TipoConfiguracion.GARANTIATALLER.getTipoValor());
				List<ConfiguracionBean> lstConfExistenciaSAP=obtenerConfiguraciones(motivo, ConstantesGenerales.TipoConfiguracion.EXISTENCIASAP.getTipoValor());					
				List<ConfiguracionBean> lstConfModoRecojo=obtenerConfiguraciones(motivo, ConstantesGenerales.TipoConfiguracion.MODORECOJO.getTipoValor());

				/*
				 * Si el estado es "SIN VALIDAR" obtenemos los datos del detalle
				 * Si el estado es "PENDIENTE" obtenemos los datos del detalle donde su campo estado="PENDIENTE" para que solo nos filtre aquellos que deben volver a revalidarse
				 * */
								
				sql="SELECT DFDEVOL.NROTICKET,DFDEVOL.NRO,DFDEVOL.SERIE,DFDEVOL.DESCRIPCION,DFDEVOL.CODSAP,DFDEVOL.TIPO,DFDEVOL.RUBRO,DFDEVOL.TECNOLOGIA,DFDEVOL.CANTIDAD,DFDEVOL.PESO_RRSS,DFDEVOL.VOLUMEN_RRSS,";
				
				if(estadoValidacion.equals(ConstantesGenerales.EstadoValidacionActa.SINVALIDAR.getTipoValor())){ //Validado por Primera Vez
					sql=sql+"DFDEVOL.INDBOD,DFDEVOL.INDLOT,DFDEVOL.INDGAR,DFDEVOL.OBSBODAPROB,DFDEVOL.OBSLOTAPROB,DFDEVOL.OBSGARAPROB,DFDEVOL.OBSBODRECHZ,DFDEVOL.OBSLOTRECHZ,DFDEVOL.OBSGARRECHZ " +
						"FROM DETALLEFICHADEVOLUCION DFDEVOL WHERE DFDEVOL.NROTICKET='" + listaFichaDevol.get(t).getNroTicket() + "' ORDER BY DFDEVOL.CODSAP,DFDEVOL.NRO";
				}else{ //Validando Pendientes
					sql=sql+"(CASE WHEN DFDEVOL.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFDEVOL.CODMATERIALSAP_4_7 ELSE DFDEVOL.CODMATERIALSAP_6_0 END) AS CODMATSAP," +
						"(CASE WHEN DFDEVOL.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFDEVOL.CENTROSAP_4_7 ELSE DFDEVOL.CENTROSAP_6_0 END) AS CENTRO," + 
						"(CASE WHEN DFDEVOL.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFDEVOL.ALMACENSAP_4_7 ELSE DFDEVOL.ALMACENSAP_6_0 END) AS ALMACEN,"+
						"(CASE WHEN DFDEVOL.CODMATERIALSAP_6_0='" + ConstantesGenerales.GUION + "' THEN DFDEVOL.LOTESAP_4_7 ELSE DFDEVOL.LOTESAP_6_0 END) AS LOTE," + 
						"DFDEVOL.INDBOD,DFDEVOL.INDLOT,DFDEVOL.INDGAR,"+
						"DFDEVOL.OBSBODAPROB,DFDEVOL.OBSLOTAPROB,DFDEVOL.OBSGARAPROB,DFDEVOL.OBSBODRECHZ,DFDEVOL.OBSLOTRECHZ,DFDEVOL.OBSGARRECHZ,DFDEVOL.ESTADO " +	
						"FROM DETALLEFICHADEVOLUCION DFDEVOL WHERE DFDEVOL.NROTICKET='" + listaFichaDevol.get(t).getNroTicket() + "' AND DFDEVOL.ESTADO='" + estadoValidacion + "' ORDER BY DFDEVOL.NRO";
				}				
				
				rs=st.executeQuery(sql); //Ejecutamos el Query
				while(rs.next()){
					String nro="";					
					String nroTicket="";
					String serie="";
					String tipo="";
					String rubro="";
					String tecnologia="";
					String material="";
					String codSAPActa="";
					String descripcionSAPActa="";
					String pesoRRSS="";
					String volumenRRSS="";
					String centro="";
					String almacen="";
					String lote="";
					String negocio="";
					String peso="";
					String volUnitario="";
					String precio="";
					String sociedad="";
					String seriado="";
					String provision="";
					int cantMateriales=0;
					String indBod="";
					String indLote="";
					String indGara="";
					String obsBodAprob="";
					String obsBodRechz="";
					String obsLotAprob="";
					String obsLotRechz="";
					String obsGarAprob="";
					String obsGarRechz="";
					String estadoDetalle="";
					
					nroTicket=rs.getString(1)==null?"":rs.getString(1);
					nro=rs.getString(2)==null?"":rs.getString(2);
					serie=rs.getString(3)==null?"":rs.getString(3);
					descripcionSAPActa=rs.getString(4)==null?"":rs.getString(4);
					codSAPActa=rs.getString(5)==null?"":rs.getString(5);
					tipo=rs.getString(6)==null?"":rs.getString(6);
					rubro=rs.getString(7)==null?"":rs.getString(7);
					tecnologia=rs.getString(8)==null?"":rs.getString(8);	
					cantMateriales=rs.getInt(9);
					pesoRRSS=rs.getString(10)==null?"":rs.getString(10);
					volumenRRSS=rs.getString(11)==null?"":rs.getString(11);					
					
					if(estadoValidacion.equals(ConstantesGenerales.EstadoValidacionActa.SINVALIDAR.getTipoValor())){
						indBod=rs.getString(12)==null?"":rs.getString(12);
						indLote=rs.getString(13)==null?"":rs.getString(13);
						indGara=rs.getString(14)==null?"":rs.getString(14);
						obsBodAprob=rs.getString(15)==null?"":rs.getString(15);
						obsLotAprob=rs.getString(16)==null?"":rs.getString(16);
						obsGarAprob=rs.getString(17)==null?"":rs.getString(17);
						obsBodRechz=rs.getString(18)==null?"":rs.getString(18);
						obsLotRechz=rs.getString(19)==null?"":rs.getString(19);
						obsGarRechz=rs.getString(20)==null?"":rs.getString(20);
					}
					if(estadoValidacion.equals(ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor())){//Si es validacion PENDIENTE
						material=rs.getString(12)==null?"":rs.getString(12); //Codigo Material SAP
						centro=rs.getString(13)==null?"":rs.getString(13); //Centro SAP
						almacen=rs.getString(14)==null?"":rs.getString(14); //Almacen SAP					
						lote=rs.getString(15)==null?"":rs.getString(15); //Lote SAP
						indBod=rs.getString(16)==null?"":rs.getString(16);  //Indicador de Bodega (A,R,P)
						indLote=rs.getString(17)==null?"":rs.getString(17); //Indicador de Lote (A,R,P)
						indGara=rs.getString(18)==null?"":rs.getString(18); //Indicador de Garantia (A,R,P)
						obsBodAprob=rs.getString(19)==null?"":rs.getString(19); //Observacion de Aprobacion Bodega
						obsLotAprob=rs.getString(20)==null?"":rs.getString(20); //Observacion de Aprobacion Lote
						obsGarAprob=rs.getString(21)==null?"":rs.getString(21); //Observacion de Aprobacion Garantia
						obsBodRechz=rs.getString(22)==null?"":rs.getString(22); //Observacion de Rechazo Bodega
						obsLotRechz=rs.getString(23)==null?"":rs.getString(23); //Observacion de Rechazo Lote
						obsGarRechz=rs.getString(24)==null?"":rs.getString(24); //Observacion de Rechazo Garantia
						estadoDetalle=rs.getString(25)==null?"":rs.getString(25); //Estado 
					}
										
					materialSAPBeanGarantia=null;
					
					detalleFichaDevolucionBean=new DetalleFichaDevolucionBean();
					
					materialSAPBean6_0_Devoluci=obtenerDatosSAPDevoluci(codSAPActa, ConstantesGenerales.SAP_6_0, tipo, rubro, tecnologia,usuario); //Obtenemos los Datos SAP Devoluci 6.0, se le envia codSAP del punto, el tipo,rubro y tecnologia (obtenidas de material)
					materialSAPBean4_7_Devoluci=obtenerDatosSAPDevoluci(codSAPActa, ConstantesGenerales.SAP_4_7, tipo, rubro, tecnologia,usuario); //Obtenemos los Datos SAP Devoluci 4.7, se le envia codSAP del punto, el tipo,rubro y tecnologia (obtenidas de material)											
					
					//Los datos del SAP se obtienen eligiendo el de la fecha de modificacion mas reciente
					materialSAPBean6_0=obtenerDatosSAP(serie, tipo, rubro, tecnologia,ConstantesGenerales.SAP_6_0,ConstantesGenerales.SIN_GARANTIA_SAP,descripcionSAPActa,usuario); //Obtenemos los Datos SAP 6.0, se la serie del punto, el tipo,rubro y tecnologia (obtenidas de material), SIN GARANTIA SAP
					materialSAPBean4_7=obtenerDatosSAP(serie, tipo, rubro, tecnologia,ConstantesGenerales.SAP_4_7,ConstantesGenerales.SIN_GARANTIA_SAP,descripcionSAPActa,usuario); //Obtenemos los Datos SAP 4.7, se la serie del punto, el tipo,rubro y tecnologia (obtenidas de material), SIN GARANTIA SAP					
					
					cont=cont+1; //Contador de items

					System.out.println("Item: " + cont + " Se esta procesando la serie: " + serie + " con el Ticket: " + listaFichaDevol.get(t).getNroTicket());
					double libreUtilizacion=0;
					
					//Solo podemos utilizar los datos de un SAP, por eso comparamos con cual SAP trabajaremos, se le da la preferencia al 6.0
					
					if(motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.SOBRESTOCK.getTipoValor())){ //Para SobreStock NO SERIADO
						if(!materialSAPBean6_0_Devoluci.getStatusSAP().equals(ConstantesGenerales.ExistenciaSAP.EXISTESAP.getTipoValor())){
							if(materialSAPBean4_7_Devoluci.getStatusSAP().equals(ConstantesGenerales.ExistenciaSAP.EXISTESAP.getTipoValor())){
								libreUtilizacion=materialSAPBean4_7_Devoluci.getLibreUtilizacion(); //Obtenemos la libre utilizacion (cantidad)
							}
						}else{
							libreUtilizacion=materialSAPBean6_0_Devoluci.getLibreUtilizacion(); //Obtenemos la libre utilizacion (cantidad)
						}
					}else{//Si no es SobreStock NO SERIADO
						if(!materialSAPBean6_0.getStatusSAP().equals(ConstantesGenerales.ExistenciaSAP.EXISTESAP.getTipoValor())){
							if(materialSAPBean4_7.getStatusSAP().equals(ConstantesGenerales.ExistenciaSAP.EXISTESAP.getTipoValor())){
								serie=materialSAPBean4_7.getNumeroSerie(); 
								material=materialSAPBean4_7.getCodMaterial();
								centro=materialSAPBean4_7.getCentro();
								almacen=materialSAPBean4_7.getAlmacen();							
								lote=materialSAPBean4_7.getLote();
								detalleFichaDevolucionBean.setObservacion(materialSAPBean4_7.getObservacionBusqueda());
								//Internamente buscaremos los Datos para SAP Garantia
								//Los datos del SAP Garantia se obtienen eligiendo el de la fecha de creacion mas antigua
								//materialSAPBeanGarantia=obtenerDatosSAP(serie, tipo, rubro, tecnologia,ConstantesGenerales.SAP_4_7,ConstantesGenerales.GARANTIA_SAP);
							}
						}else{						
							serie=materialSAPBean6_0.getNumeroSerie();
							material=materialSAPBean6_0.getCodMaterial();
							centro=materialSAPBean6_0.getCentro();
							almacen=materialSAPBean6_0.getAlmacen();						
							lote=materialSAPBean6_0.getLote();
							detalleFichaDevolucionBean.setObservacion(materialSAPBean6_0.getObservacionBusqueda());
						}
						
						if(materialSAPBean6_0.getStatusSAP().equals(ConstantesGenerales.ExistenciaSAP.EXISTESAP.getTipoValor())){
							materialSAPBeanGarantia=obtenerDatosSAP(serie, tipo, rubro, tecnologia,ConstantesGenerales.SAP_6_0,ConstantesGenerales.GARANTIA_SAP,descripcionSAPActa,usuario);
							if(materialSAPBean4_7.getStatusSAP().equals(ConstantesGenerales.ExistenciaSAP.EXISTESAP.getTipoValor())){
								materialSAPBeanGarantia=obtenerDatosSAP(serie, tipo, rubro, tecnologia,ConstantesGenerales.SAP_4_7,ConstantesGenerales.GARANTIA_SAP,descripcionSAPActa,usuario);
							}
						}else{
							if(materialSAPBean4_7.getStatusSAP().equals(ConstantesGenerales.ExistenciaSAP.EXISTESAP.getTipoValor())){
								materialSAPBeanGarantia=obtenerDatosSAP(serie, tipo, rubro, tecnologia,ConstantesGenerales.SAP_4_7,ConstantesGenerales.GARANTIA_SAP,descripcionSAPActa,usuario);
							}
						}
					}	
					
					//Asignamos los valores al objeto DetalleFichaDevolucion
					
					detalleFichaDevolucionBean.setLiq_noliq(ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor());
					detalleFichaDevolucionBean.setValidacionGarantia(ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor());
					detalleFichaDevolucionBean.setMesLiquidacion_Averias(ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor());
					detalleFichaDevolucionBean.setNroTicket(listaFichaDevol.get(t).getNroTicket());
					detalleFichaDevolucionBean.setSerie(serie);
					detalleFichaDevolucionBean.setTipo(tipo);
					detalleFichaDevolucionBean.setRubro(rubro);
					detalleFichaDevolucionBean.setTecnologia(tecnologia);
					detalleFichaDevolucionBean.setNro(Integer.parseInt(nro));
					detalleFichaDevolucionBean.setCodSAP(codSAPActa);
					detalleFichaDevolucionBean.setCantMateriales(cantMateriales);
					detalleFichaDevolucionBean.setVolumenRRSS(volumenRRSS);
					detalleFichaDevolucionBean.setPesoRRSS(pesoRRSS);
					detalleFichaDevolucionBean.setObservacion(ConstantesGenerales.GUION);
					detalleFichaDevolucionBean.setFechaSolicitud(fechaSolicitud);
					//Datos del SAP 6.0					
					
					detalleFichaDevolucionBean.setSerieSAP_6_0(materialSAPBean6_0.getNumeroSerie());
					//Si es SobreStock - No Seriado en el objeto detalle colocamos los valores de materialsapdevoluci sino 
					//en todo caso colocamos los de materialSAP
					if(motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.SOBRESTOCK.getTipoValor())){
						detalleFichaDevolucionBean.setCodMaterialSAP_6_0(materialSAPBean6_0_Devoluci.getCodMaterial());
						detalleFichaDevolucionBean.setDesMaterialSAP_6_0(materialSAPBean6_0_Devoluci.getDenominacion());
						detalleFichaDevolucionBean.setCentroSAP_6_0(materialSAPBean6_0_Devoluci.getCentro());
						detalleFichaDevolucionBean.setAlmacen_6_0(materialSAPBean6_0_Devoluci.getAlmacen());										
						detalleFichaDevolucionBean.setLote_6_0(materialSAPBean6_0_Devoluci.getLote());
						detalleFichaDevolucionBean.setStatusSAP_6_0(materialSAPBean6_0_Devoluci.getStatusSAP());
					}else{
						detalleFichaDevolucionBean.setCodMaterialSAP_6_0(materialSAPBean6_0.getCodMaterial());
						detalleFichaDevolucionBean.setDesMaterialSAP_6_0(materialSAPBean6_0.getDenominacion());	
						detalleFichaDevolucionBean.setCentroSAP_6_0(materialSAPBean6_0.getCentro());
						detalleFichaDevolucionBean.setAlmacen_6_0(materialSAPBean6_0.getAlmacen());										
						detalleFichaDevolucionBean.setLote_6_0(materialSAPBean6_0.getLote());
						detalleFichaDevolucionBean.setStatusSAP_6_0(materialSAPBean6_0.getStatusSAP());
					}
					
					detalleFichaDevolucionBean.setTipoStocks_6_0(materialSAPBean6_0.getTipoStocks());
					detalleFichaDevolucionBean.setStatusSistema_6_0(materialSAPBean6_0.getStatuSistema());
					detalleFichaDevolucionBean.setStatusUsuario_6_0(materialSAPBean6_0.getStatusUsuario());
					detalleFichaDevolucionBean.setFechaUltimoMovSAP_6_0(materialSAPBean6_0.getModificadoEl());
					detalleFichaDevolucionBean.setModificadoPor_6_0(materialSAPBean6_0.getModificadoPor());
					detalleFichaDevolucionBean.setCreadoPor_6_0(materialSAPBean6_0.getCreadoPor());
					

					//Si existe Garantia, se la Fecha de Ingreso se considerara del Creado del SAP 6.0 Garantia, en todo caso la obtenida del SAP 6.0 sin Garantia
					if(materialSAPBeanGarantia!=null){
						if(materialSAPBean6_0.getStatusSAP().equals(ConstantesGenerales.ExistenciaSAP.NOEXISTESAP.getTipoValor())){
							detalleFichaDevolucionBean.setFechaIngresoSAP_6_0(ConstantesGenerales.GUION);							
						}else{
							detalleFichaDevolucionBean.setFechaIngresoSAP_6_0(materialSAPBeanGarantia.getCreadoEl());						
						}
					}else{
						detalleFichaDevolucionBean.setFechaIngresoSAP_6_0(materialSAPBean6_0.getCreadoEl());						
					}
					
					//Datos del SAP 4.7
					
					detalleFichaDevolucionBean.setSerieSAP_4_7(materialSAPBean4_7.getNumeroSerie());
					//Si es SobreStock - No Seriado en el objeto detalle colocamos los valores de materialsapdevoluci sino 
					//en todo caso colocamos los de materialSAP
					if(motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.SOBRESTOCK.getTipoValor())){
						detalleFichaDevolucionBean.setCodMaterialSAP_4_7(materialSAPBean4_7_Devoluci.getCodMaterial());
						detalleFichaDevolucionBean.setDesMaterialSAP_4_7(materialSAPBean4_7_Devoluci.getDenominacion());
						detalleFichaDevolucionBean.setCentroSAP_4_7(materialSAPBean4_7_Devoluci.getCentro());
						detalleFichaDevolucionBean.setAlmacen_4_7(materialSAPBean4_7_Devoluci.getAlmacen());										
						detalleFichaDevolucionBean.setLote_4_7(materialSAPBean4_7_Devoluci.getLote());						
						detalleFichaDevolucionBean.setStatusSAP_4_7(materialSAPBean4_7_Devoluci.getStatusSAP());
					}else{
						detalleFichaDevolucionBean.setCodMaterialSAP_4_7(materialSAPBean4_7.getCodMaterial());						
						detalleFichaDevolucionBean.setDesMaterialSAP_4_7(materialSAPBean4_7.getDenominacion());
						detalleFichaDevolucionBean.setCentroSAP_4_7(materialSAPBean4_7.getCentro());
						detalleFichaDevolucionBean.setAlmacen_4_7(materialSAPBean4_7.getAlmacen());										
						detalleFichaDevolucionBean.setLote_4_7(materialSAPBean4_7.getLote());
						detalleFichaDevolucionBean.setStatusSAP_4_7(materialSAPBean4_7.getStatusSAP());
					}										
				
					detalleFichaDevolucionBean.setTipoStocks_4_7(materialSAPBean4_7.getTipoStocks());
					detalleFichaDevolucionBean.setStatusSistema_4_7(materialSAPBean4_7.getStatuSistema());
					detalleFichaDevolucionBean.setStatusUsuario_4_7(materialSAPBean4_7.getStatusUsuario());
					detalleFichaDevolucionBean.setFechaUltimoMovSAP_4_7(materialSAPBean4_7.getModificadoEl());
					detalleFichaDevolucionBean.setModificadoPor_4_7(materialSAPBean4_7.getModificadoPor());	
					detalleFichaDevolucionBean.setCreadoPor_4_7(materialSAPBean4_7.getCreadoPor());
					
					//Si existe Garantia, se la Fecha de Ingreso se considerara del Creado del SAP 4.7 Garantia, en todo caso la obtenida del SAP 4.7 sin Garantia
					if(materialSAPBeanGarantia!=null){
						if(materialSAPBean4_7.getStatusSAP().equals(ConstantesGenerales.ExistenciaSAP.NOEXISTESAP.getTipoValor())){
							detalleFichaDevolucionBean.setFechaIngresoSAP_4_7(ConstantesGenerales.GUION);							
						}else{
							detalleFichaDevolucionBean.setFechaIngresoSAP_4_7(materialSAPBeanGarantia.getCreadoEl());						
						}
					}else{
						detalleFichaDevolucionBean.setFechaIngresoSAP_4_7(materialSAPBean4_7.getCreadoEl());						
					}
					
					//Cuando son Residuos Operativos o SobreStock No Seriados el codigo material sera el que envia el punto
					if(motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.RESIDUOSOPERATIVOS.getTipoValor()) || 
				   	   motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.SOBRESTOCK.getTipoValor())){
					   material=detalleFichaDevolucionBean.getCodSAP();
					}
					
					//El objeto material obtendra los datos del Material por medio del valor material,tipo,rubro,tecnologia
					materialBean=obtenerDatosMaterial(material,tipo,rubro,tecnologia);

					/*Si el Tipo de Formato "Otros" para los campos: Provision, Negocio, Sociedad, Seriado, Tipo, Rubro, Tecnologia tendran el valor: NO APLICA
					 * en todo caso
					 * */
					if(formato.equals(ConstantesGenerales.TipoFormatoDevolucion.OTROS.getTipoValor())){
						provision=ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor();
						negocio=ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor();
						sociedad=ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor();
						seriado=ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor();						
						tipo=ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor();
						rubro=ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor();
						tecnologia=ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor();
					}else{
						provision=materialBean.getProvision();
						negocio=materialBean.getNegocio();
						sociedad=materialBean.getSociedad();
						seriado=materialBean.getSeriado();
						tipo=materialBean.getTipoMaterial();
						//Si no es SobreStock mantendra el valor obtenido en la consulta, en todo caso sera el de la tabla material
						if(!motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.SOBRESTOCK.getTipoValor())){
							rubro=materialBean.getRubro();							
						}
						tecnologia=materialBean.getTecnologia();
					}
					
					/*Si el Tipo de Ficha fuese de LIMA el campos: Peso, VolUnitario y Precio tendran el valor: NO APLICA
					 *en todo caso(PROVINCIA) obtendra el valor de la base Material					 			
					 */
								
					
					if(enviado.equals(ConstantesGenerales.Enviado.PLANEAMIENTO.getTipoValor())){
						peso=ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor();
						volUnitario=ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor();
						precio=ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor();
					}else{
						peso=materialBean.getPeso();
						volUnitario=materialBean.getVolUnitario();
						precio=materialBean.getPrecio();						
					}
					
					
					
					if(provision.equals("")){
						detalleFichaDevolucionBean.setProvision(ConstantesGenerales.GUION);
					}else{
						detalleFichaDevolucionBean.setProvision(provision);
					}	
					
					if(negocio.equals("")){
						detalleFichaDevolucionBean.setNegocio(ConstantesGenerales.GUION);
					}else{
						detalleFichaDevolucionBean.setNegocio(negocio);
					}
					
					if(sociedad.equals("")){
						detalleFichaDevolucionBean.setSociedad(ConstantesGenerales.GUION);
					}else{
						detalleFichaDevolucionBean.setSociedad(sociedad);
					}
					
					if(seriado.equals("")){
						detalleFichaDevolucionBean.setSeriado(ConstantesGenerales.GUION);
					}else{
						detalleFichaDevolucionBean.setSeriado(seriado);
					}
					
					if(tipo.equals("")){
						detalleFichaDevolucionBean.setTipo(ConstantesGenerales.GUION);
					}else{
						detalleFichaDevolucionBean.setTipo(tipo);
					}						
					
					if(rubro.equals("")){
						detalleFichaDevolucionBean.setRubro(ConstantesGenerales.GUION);
					}else{
						detalleFichaDevolucionBean.setRubro(rubro);
					}	
					
					if(tecnologia.equals("")){
						detalleFichaDevolucionBean.setTecnologia(ConstantesGenerales.GUION);
					}else{
						detalleFichaDevolucionBean.setTecnologia(tecnologia);
					}	
							
					if(peso.equals("")){
						detalleFichaDevolucionBean.setPeso(ConstantesGenerales.GUION);
					}else{
						detalleFichaDevolucionBean.setPeso(peso);
					}	
					
					if(volUnitario.equals("")){
						detalleFichaDevolucionBean.setVolUnitario(ConstantesGenerales.GUION);
					}else{
						detalleFichaDevolucionBean.setVolUnitario(volUnitario);
					}	
					
					if(precio.equals("")){
						detalleFichaDevolucionBean.setPrecio(ConstantesGenerales.GUION);
					}else{
						detalleFichaDevolucionBean.setPrecio(precio);
					}					
					
					//Si la ficha NO se le esta permitido la existencia de Pedido, si no existe los campos: 
					//NroPedido, PosicionPedido, NroEntrea y Fecha Creacion de Pedido tendra el valor: NO APLICA
					
					if(esPedido.equals(ConstantesGenerales.EstadoPedidoActa.NOEXISTEPEDIDO.getTipoValor())){
						detalleFichaDevolucionBean.setNroPedido(ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor());
						detalleFichaDevolucionBean.setPosPedido(ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor());
						detalleFichaDevolucionBean.setNroEntrega(ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor());
						detalleFichaDevolucionBean.setfCreacionPedido(ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor());						
					}
					
					//Si la ficha se le esta permitido la existencia de Pedido, si no existe los campos: 
					//NroPedido, PosicionPedido, NroEntrea y Fecha Creacion de Pedido tendra el valor: "-"
					
					if(esPedido.equals(ConstantesGenerales.EstadoPedidoActa.EXISTEPEDIDO.getTipoValor())){
						detalleFichaDevolucionBean.setNroPedido(ConstantesGenerales.GUION);
						detalleFichaDevolucionBean.setPosPedido(ConstantesGenerales.GUION);
						detalleFichaDevolucionBean.setNroEntrega(ConstantesGenerales.GUION);
						detalleFichaDevolucionBean.setfCreacionPedido(ConstantesGenerales.GUION);						
					}
					
					
					if(motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.RESIDUOSOPERATIVOS.getTipoValor())){
						detalleFichaDevolucionBean.setDesMaterialSAP_6_0(descripcionSAPActa);
						detalleFichaDevolucionBean.setDesMaterialSAP_4_7(descripcionSAPActa);
						detalleFichaDevolucionBean.setPeso(detalleFichaDevolucionBean.getPesoRRSS());
						detalleFichaDevolucionBean.setVolUnitario(detalleFichaDevolucionBean.getVolumenRRSS());
					}else{
						if(peso.equals("")){
							detalleFichaDevolucionBean.setPeso(ConstantesGenerales.GUION);
						}else{
							detalleFichaDevolucionBean.setPeso(peso);
						}	
						
						if(volUnitario.equals("")){
							detalleFichaDevolucionBean.setVolUnitario(ConstantesGenerales.GUION);
						}else{
							detalleFichaDevolucionBean.setVolUnitario(volUnitario);
						}
					}
					
					if(materialSAPBean6_0.getStatusSAP().equals(ConstantesGenerales.ExistenciaSAP.NOEXISTESAP.getTipoValor()) &&
					   materialSAPBean4_7.getStatusSAP().equals(ConstantesGenerales.ExistenciaSAP.NOEXISTESAP.getTipoValor())){
						if(formato.equals(ConstantesGenerales.TipoFormatoDevolucion.OTROS.getTipoValor())){
							detalleFichaDevolucionBean.setDesMaterialSAP_6_0(descripcionSAPActa);
							detalleFichaDevolucionBean.setDesMaterialSAP_4_7(descripcionSAPActa);						 
						}
						
					}										
					
					if(motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.AVERIAS.getTipoValor()) || motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.BAJAS.getTipoValor()) || motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.ANULACIONVENTA.getTipoValor())){
						detalleFichaDevolucionBean.setMesLiquidacion_Averias(mesDevolucion);
					}
					
					/*****INICIAMOS LAS VALIDACIONES DE TRANSACCIONES*****/
					
					/*****Validacion de Bodegas*****/
					
					TipoBodegaBean tipoBodegaBean=null;
					boolean aplicaBodega=true;
					
					// Como es el primer ingreso los consideraremos SIN CONFIGURACION, y si no encuentra alguna condicion 
					//se cambiara el estado y observacion (ES SOLO UN ESTADO INICIAL O ESTANDAR sino encuentra la condicion)
					if(estadoValidacion.equals(ConstantesGenerales.EstadoValidacionActa.SINVALIDAR.getTipoValor())){ 
						indBod=ConstantesGenerales.IndicadorCriterios.NONE.getTipoValor();
						detalleFichaDevolucionBean.setIndBod(indBod);
						detalleFichaDevolucionBean.setObsBodAprob("No es sujeto a Bodegas");
						detalleFichaDevolucionBean.setObsBodRechaz(ConstantesGenerales.GUION);
						
						if(motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.DOAS.getTipoValor())){
							tipoBodegaBean=obtenerTipoBodega(centro, almacen, entidad);
							if(tipoBodegaBean.getCodTipoBodegas().equals(ConstantesGenerales.TipoBodega.LIQUIDADO.getTipoValor())){
							   	indBod=ConstantesGenerales.IndicadorCriterios.RECHAZADO.getTipoValor();
							   	detalleFichaDevolucionBean.setIndBod(indBod);
							   	detalleFichaDevolucionBean.setObsBodAprob(ConstantesGenerales.GUION);
							   	detalleFichaDevolucionBean.setObsBodRechaz("Serie fuera de Bodega");						
							   	aplicaBodega=false;
							}
							
							if(serie.equals(ConstantesGenerales.SINSERIE)){
								indBod=ConstantesGenerales.IndicadorCriterios.PENDIENTE.getTipoValor();
								detalleFichaDevolucionBean.setIndBod(indBod);
								detalleFichaDevolucionBean.setObsBodAprob(ConstantesGenerales.GUION);
								detalleFichaDevolucionBean.setObsGarRechaz("Se encuentra Bodega Pendiente por ser DOA - SIN SERIE");	
							}
						}
					}else{//Si fuera una revalidacion los valores los obtendra de su anterior validacion 
						detalleFichaDevolucionBean.setIndBod(indBod);
						detalleFichaDevolucionBean.setObsBodAprob(obsBodAprob);
						detalleFichaDevolucionBean.setObsBodRechaz(obsBodRechz);
					}
					
										
					//Si el indicador de Bodega NO esta APROBADO entonces podemos validarlo o revalidarlo
					if(!indBod.equals(ConstantesGenerales.IndicadorCriterios.APROBADO.getTipoValor()) && aplicaBodega){
						//if(!existeAcopio){//Sino existe en acopio y ni en Status TRX Previamente
							if(lstConfBodegas!=null){//Si la configuracion de Bodegas no es NULA(Existe Configuracion para Bodega)
								if(lstConfBodegas.size()>0 && !lstConfBodegas.get(0).getExistencia().equals("NO")){//Si existe configuracion de Bodega guardada para el motivo de devolucion									
									tipoBodegaBean=obtenerTipoBodega(centro, almacen, entidad);	//Obtenemos el Tipo de Bodega con los parametros: Centro, Almacen y Entidad														
									if(lstConfExistenciaSAP.size()>0){//Vemos si existe configuracion de Existencia en SAP(Nos indica que la existencia en SAP es obligatoria)
										if(materialSAPBean6_0.getStatusSAP().equals(ConstantesGenerales.ExistenciaSAP.NOEXISTESAP.getTipoValor()) &&
										   materialSAPBean4_7.getStatusSAP().equals(ConstantesGenerales.ExistenciaSAP.NOEXISTESAP.getTipoValor())){
											//SI NO EXISTE EN NINGUN SAP
											if(estadoValidacion.equals(ConstantesGenerales.EstadoValidacionActa.SINVALIDAR.getTipoValor())){//Si es un item sin Validar
											//SI EXIGE EXISTENCIA SE RECHAZA	
											   if(lstConfExistenciaSAP.get(0).getExistencia().equals(ConstantesGenerales.SI)){
												   	indBod=ConstantesGenerales.IndicadorCriterios.RECHAZADO.getTipoValor();
												   	detalleFichaDevolucionBean.setIndBod(indBod);
												   	detalleFichaDevolucionBean.setObsBodAprob(ConstantesGenerales.GUION);
												   	detalleFichaDevolucionBean.setObsBodRechaz("Se rechaza Bodega ya que no existe en SAP y se considera la Existencia en SAP");
												}
											//SI NO EXIGE EXISTENCIA SE APRUEBA	
											   if(lstConfExistenciaSAP.get(0).getExistencia().equals(ConstantesGenerales.NO)){
												   	indBod=ConstantesGenerales.IndicadorCriterios.APROBADO.getTipoValor();
												   	detalleFichaDevolucionBean.setIndBod(indBod);
												   	detalleFichaDevolucionBean.setObsBodAprob("Se aprueba la Bodega ya que NO CONSIDERA LA EXISTENCIA EN SAP a pesar que no existe en SAP");
												   	detalleFichaDevolucionBean.setObsBodRechaz(ConstantesGenerales.GUION);
												}
											}										
										}else{
											//SI EXISTE EN ALGUN SAP EVALUA VALORES DE CONFIGURACION
											//SI ES AVERIAS Y BAJAS Y SU BODEGA ES LIQUIDADO ENTONCES SU VALORES(LIQUIDADO=SI) EN TODO CASO SERA "NO"
											if(motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.AVERIAS.getTipoValor()) || motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.BAJAS.getTipoValor())){
												if(tipoBodegaBean.getCodTipoBodegas().equals(ConstantesGenerales.TipoBodega.LIQUIDADO.getTipoValor())){
													detalleFichaDevolucionBean.setLiq_noliq(ConstantesGenerales.SI);												
												}else{
													detalleFichaDevolucionBean.setLiq_noliq(ConstantesGenerales.NO);
												}
											}else{//SI NO FUERA AVERIAS Y BAJAS EL VALOR SERA "NO APLICA"
												detalleFichaDevolucionBean.setLiq_noliq(ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor());
											}
											indBod=ConstantesGenerales.IndicadorCriterios.PENDIENTE.getTipoValor();
										   	detalleFichaDevolucionBean.setIndBod(indBod);
										   	detalleFichaDevolucionBean.setObsBodAprob(ConstantesGenerales.GUION);
											String msgRechazoBodega="";
											String valoresTipoBodega="";
											msgRechazoBodega="No se encuentra en la Bodega correcta. Se encuentra en el Centro: " + centro + " y Almacen:" + almacen + " y su Bodega es del Tipo:" + tipoBodegaBean.getNomTipoBodegas() + ". Su Bodega deberia ser del Tipo: ";
											
											for(int i=0;i<lstConfBodegas.size();i++){
												if(lstConfBodegas.get(i).getExistencia().equals(ConstantesGenerales.SI)){
													valoresTipoBodega=valoresTipoBodega + obtenerNombreBodegaConfiguracion(lstConfBodegas.get(i).getValor()) + ","; 
												}	
											}
											
											valoresTipoBodega = valoresTipoBodega.substring(0, valoresTipoBodega.length()-1); 
											msgRechazoBodega=msgRechazoBodega + valoresTipoBodega + ".";											
											detalleFichaDevolucionBean.setObsBodRechaz(msgRechazoBodega);
											
										   	for(int j=0;j<lstConfBodegas.size();j++){//Recorremos los valores guardado en la configuracion
										   		if(lstConfBodegas.get(j).getExistencia().equals(ConstantesGenerales.SI)){
													if(tipoBodegaBean.getCodTipoBodegas().equals(lstConfBodegas.get(j).getValor())){ //Si coinciden el valor de Conf. Bodegas con la Bodega del Acta
														indBod=ConstantesGenerales.IndicadorCriterios.APROBADO.getTipoValor();
														detalleFichaDevolucionBean.setIndBod(ConstantesGenerales.IndicadorCriterios.APROBADO.getTipoValor());
													   	detalleFichaDevolucionBean.setObsBodAprob("Se encuentra en la Bodega correcta. (Existe en SAP)");
													   	detalleFichaDevolucionBean.setObsBodRechaz(ConstantesGenerales.GUION);
														break;
													}
										   		}	
											}
										}
									}
								}
							}
						//}	
					}
					
										
					/*****Validacion de Lote*****/
					
					boolean aplicaLote=true;
					
					if(estadoValidacion.equals(ConstantesGenerales.EstadoValidacionActa.SINVALIDAR.getTipoValor())){
						indLote=ConstantesGenerales.IndicadorCriterios.NONE.getTipoValor();
						detalleFichaDevolucionBean.setIndLote(indLote);
						detalleFichaDevolucionBean.setObsLoteAprob("No es sujeto a Lote");
						detalleFichaDevolucionBean.setObsLoteRechaz(ConstantesGenerales.GUION);
						
						
						if(tipo.equals(ConstantesGenerales.TIPO_SMARTCARD) && rubro.equals(ConstantesGenerales.RUBRO_DECO) && tecnologia.equals(ConstantesGenerales.TECNOLOGIA_SMARTCARD)){
							indLote=ConstantesGenerales.IndicadorCriterios.NONE.getTipoValor();
							detalleFichaDevolucionBean.setIndLote(indLote);
							detalleFichaDevolucionBean.setObsLoteAprob("No es sujeto a Lote - No Aplica a Tarjetas Inteligentes");
							detalleFichaDevolucionBean.setObsLoteRechaz(ConstantesGenerales.GUION);
							aplicaLote=false;
						}
						
						if(tipo.equals(ConstantesGenerales.TIPO_DISCODURO) && rubro.equals(ConstantesGenerales.RUBRO_DECO) && tecnologia.equals(ConstantesGenerales.TECNOLOGIA_NOAPLICA)){
							indLote=ConstantesGenerales.IndicadorCriterios.NONE.getTipoValor();
							detalleFichaDevolucionBean.setIndLote(indLote);
							detalleFichaDevolucionBean.setObsLoteAprob("No es sujeto a Lote - No Aplica a Discos Duros");
							detalleFichaDevolucionBean.setObsLoteRechaz(ConstantesGenerales.GUION);
							aplicaLote=true;
						}
											
						
						if(motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.DOAS.getTipoValor())){						
							if(serie.equals(ConstantesGenerales.SINSERIE)){
								indLote=ConstantesGenerales.IndicadorCriterios.PENDIENTE.getTipoValor();
								detalleFichaDevolucionBean.setIndLote(indLote);
								detalleFichaDevolucionBean.setObsLoteAprob(ConstantesGenerales.GUION);
								detalleFichaDevolucionBean.setObsLoteRechaz("Se encuentra Lote Pendiente por ser DOA - SIN SERIE");	
							}
						}
						
					}else{
						detalleFichaDevolucionBean.setIndLote(indLote);
						detalleFichaDevolucionBean.setObsLoteAprob(obsLotAprob);
						detalleFichaDevolucionBean.setObsLoteRechaz(obsLotRechz);
					}
					
					
					boolean esLotizable=esLotizable(material); //SI EL MATERIAL TIENE EL VALOR LOTIZABLE="SI" ES TRUE EM TODO CASO FALSE
					
					if(!indLote.equals(ConstantesGenerales.IndicadorCriterios.APROBADO.getTipoValor()) && aplicaLote){
						//if(!existeAcopio){
							if(lstConfLote!=null){//Si la configuracion de Lote no es NULA
								if(lstConfLote.size()>0 && !lstConfLote.get(0).getExistencia().equals("NO")){//Si existe configuracion de Lote guardada para el motivo de devolucion							
									if(lstConfExistenciaSAP.size()>0){//Vemos si existe configuracion de Existencia en SAP(es obligatorio)
										if(materialSAPBean6_0.getStatusSAP().equals(ConstantesGenerales.ExistenciaSAP.NOEXISTESAP.getTipoValor()) &&
										   materialSAPBean4_7.getStatusSAP().equals(ConstantesGenerales.ExistenciaSAP.NOEXISTESAP.getTipoValor())){//Si no existe el material en ningun SAP(Se rechaza la validacion de Lote)										
											if(estadoValidacion.equals(ConstantesGenerales.EstadoValidacionActa.SINVALIDAR.getTipoValor())){
												if(lstConfExistenciaSAP.get(0).getExistencia().equals(ConstantesGenerales.SI)){//Si exige la existencia en SAP
													indLote=ConstantesGenerales.IndicadorCriterios.RECHAZADO.getTipoValor();
													detalleFichaDevolucionBean.setIndLote(indLote);
													detalleFichaDevolucionBean.setObsLoteAprob(ConstantesGenerales.GUION);
													detalleFichaDevolucionBean.setObsLoteRechaz("Se rechaza Lote ya que no existe en SAP y se considera existencia en SAP");
												}
											   
												if(lstConfExistenciaSAP.get(0).getExistencia().equals(ConstantesGenerales.NO)){//Si exige la existencia en SAP
													indLote=ConstantesGenerales.IndicadorCriterios.APROBADO.getTipoValor();
													detalleFichaDevolucionBean.setIndLote(indLote);
													detalleFichaDevolucionBean.setObsLoteAprob("Se aprueba el Lote ya que NO CONSIDERA LA EXISTENCIA EN SAP a pesar que no existe en SAP");
													detalleFichaDevolucionBean.setObsLoteRechaz(ConstantesGenerales.GUION);
												}
											}   
										}else{//Si existe el material en algun SAP(Se evalua coincidencia con los valores guardados en la configuracion)
											indLote=ConstantesGenerales.IndicadorCriterios.PENDIENTE.getTipoValor();
											detalleFichaDevolucionBean.setIndLote(indLote);
											if(esLotizable){//Si es lotizable se evalua su existencia
												indLote=ConstantesGenerales.IndicadorCriterios.PENDIENTE.getTipoValor();
												detalleFichaDevolucionBean.setIndLote(indLote);
												detalleFichaDevolucionBean.setObsLoteAprob(ConstantesGenerales.GUION);												
												String msgRechazoLote="";
												String valoresLote="";
												msgRechazoLote="No se encuentra en el Lote correcto. Es Lotizable. Se encuentra en el Lote: " + lote + ". Deberia estar en el Lote: ";
												
												for(int i=0;i<lstConfLote.size();i++){
													if(lstConfLote.get(i).getExistencia().equals(ConstantesGenerales.SI)){
														valoresLote=valoresLote + lstConfLote.get(i).getValor() + ","; 
													}	
												}
												
												valoresLote = valoresLote.substring(0, valoresLote.length()-1); 
												msgRechazoLote=msgRechazoLote + valoresLote + ".";											
												detalleFichaDevolucionBean.setObsLoteRechaz(msgRechazoLote);
												
												for(int i=0;i<lstConfLote.size();i++){//Recorre la configuracion de Lote
													if(lstConfLote.get(i).getExistencia().equals(ConstantesGenerales.SI)){
														if(lote.equals(lstConfLote.get(i).getValor())){//Si coincide con los valores guardados en la configuracion entonces se aprueba
															indLote=ConstantesGenerales.IndicadorCriterios.APROBADO.getTipoValor();
															detalleFichaDevolucionBean.setIndLote(indLote);
															detalleFichaDevolucionBean.setObsLoteAprob("Se encuentra en el Lote correcto. Es Lotizable");
															detalleFichaDevolucionBean.setObsLoteRechaz(ConstantesGenerales.GUION);
															break;
														}
													}
												}													
											  }else{//Si es no lotizable entonces se aprueba con el valor indicado en el acta
												indLote=ConstantesGenerales.IndicadorCriterios.APROBADO.getTipoValor();
												detalleFichaDevolucionBean.setIndLote(indLote);
												detalleFichaDevolucionBean.setObsLoteAprob("No se encuentra en el Lote correcto. Pero se acepta ya que es No Lotizable");
												detalleFichaDevolucionBean.setObsLoteRechaz(ConstantesGenerales.GUION);
												
											  }	
												
											}
										}								
									}
								}
							//}
					}
					

					
					/*****Validacion de Garantia*****/

					
					TallerBean tallerBean=null;	//Valor inicial para el objeto Taller(Si se encuentra en la Tabla Taller obtendremos sus datos
					tallerBean=existeGarantiaTaller(detalleFichaDevolucionBean.getSerie(), material);
					String observacionGarantiaNegocio=ConstantesGenerales.GUION;
					BaseGarantiaBean baseGarantiaBean=null; //Contiene la informacion de la Garantia para determinar si es Responsabilidad del Punto o Telefonica
					baseGarantiaBean=obtenerDatosBaseGarantia(material, serie,usuario);//Base Garantia
					boolean aplicaGarantia=true;
					
					if(estadoValidacion.equals(ConstantesGenerales.EstadoValidacionActa.SINVALIDAR.getTipoValor())){
						indGara=ConstantesGenerales.IndicadorCriterios.NONE.getTipoValor();
						detalleFichaDevolucionBean.setIndGar(indGara);
						detalleFichaDevolucionBean.setObsGarAprob("No es sujeto a Garantia");
						detalleFichaDevolucionBean.setObsGarRechaz(ConstantesGenerales.GUION);								
					}else{
						detalleFichaDevolucionBean.setIndGar(indGara);						
						detalleFichaDevolucionBean.setObsGarAprob(obsGarAprob);
						detalleFichaDevolucionBean.setObsGarRechaz(obsGarRechz);
					}
					
					
					//Se asignara el valor de Fecha Remozado Taller y Fecha Despacho
					detalleFichaDevolucionBean.setFechaRemozadoTaller(ConstantesGenerales.GUION);
					detalleFichaDevolucionBean.setFechaDespachoAlmacen(ConstantesGenerales.GUION);
					detalleFichaDevolucionBean.setRemozado_Reparado(ConstantesGenerales.GUION);
					
					//LLamamos a la funcion existe en Taller para saber si tiene algun registro en taller
					
					if(!indGara.equals(ConstantesGenerales.IndicadorCriterios.APROBADO.getTipoValor())){
						//if(!existeAcopio){
							if(lstConfGarantiaProveedor!=null && lstConfGarantiaTaller!=null){
								if((lstConfGarantiaProveedor.size()>0 && !lstConfGarantiaProveedor.get(0).getExistencia().equals("NO")) || (lstConfGarantiaTaller.size()>0 && !lstConfGarantiaTaller.get(0).getExistencia().equals("NO"))){
									if(lstConfExistenciaSAP.size()>0){//Vemos si existe configuracion de Existencia en SAP(es obligatorio)								
										if(materialSAPBeanGarantia==null){//Si no existe el material en ningun SAP(Se rechaza la validacion de Garantia)
											if(estadoValidacion.equals(ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor())){
												if(baseGarantiaBean!=null){
													if(baseGarantiaBean.getObservacion().equals(ConstantesGenerales.BaseGarantia.RESPONSABILIDAD_TELEFONICA.getTipoValor())){
														indGara=ConstantesGenerales.IndicadorCriterios.APROBADO.getTipoValor();
														detalleFichaDevolucionBean.setIndGar(indGara);
														detalleFichaDevolucionBean.setObsGarAprob("Aprobado - Responsabilidad de Telefonica - Base Garantia - Proveedor");
														detalleFichaDevolucionBean.setObsGarRechaz(ConstantesGenerales.GUION);
													}
														
													if(baseGarantiaBean.getObservacion().equals(ConstantesGenerales.BaseGarantia.RESPONSABILIDAD_PUNTO.getTipoValor())){
														indGara=ConstantesGenerales.IndicadorCriterios.RECHAZADO.getTipoValor();
														detalleFichaDevolucionBean.setIndGar(indGara);
														detalleFichaDevolucionBean.setObsGarAprob(ConstantesGenerales.GUION);
														detalleFichaDevolucionBean.setObsGarRechaz("Rechazado - Responsabilidad del Punto - Base Garantia - Proveedor");
													}
													detalleFichaDevolucionBean.setFechaDespachoAlmacen(baseGarantiaBean.getfDespachoSAP());
												}
											}else{
												if(lstConfExistenciaSAP.get(0).getExistencia().equals(ConstantesGenerales.SI)){//Si exige la existencia en SAP
													indGara=ConstantesGenerales.IndicadorCriterios.RECHAZADO.getTipoValor();
													detalleFichaDevolucionBean.setIndGar(indGara);
													detalleFichaDevolucionBean.setObsGarAprob(ConstantesGenerales.GUION);
													detalleFichaDevolucionBean.setObsGarRechaz("Se rechaza la Garantia ya que Considera la Existencia en SAP.");
													detalleFichaDevolucionBean.setValidacionGarantia(ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor());															
												}
													
												if(lstConfExistenciaSAP.get(0).getExistencia().equals(ConstantesGenerales.NO)){//No exige la existencia en SAP
													indGara=ConstantesGenerales.IndicadorCriterios.APROBADO.getTipoValor();
													detalleFichaDevolucionBean.setIndGar(indGara);													
													detalleFichaDevolucionBean.setObsGarAprob("Se aprueba ya que No Considera la Existencia en SAP, NO APLICA Garantia");
													detalleFichaDevolucionBean.setObsGarRechaz(ConstantesGenerales.GUION);
													detalleFichaDevolucionBean.setValidacionGarantia(ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor());		
												}
											}

										}else{//Si existe el material en algun SAP Garantia(Se evalua coincidencia con los valores guardados en la configuracion)
											
											String fechaCreado=materialSAPBeanGarantia.getCreadoEl(); //Fecha creacion del archivo SAP
											String tipoFechaProveedor=lstConfGarantiaProveedor.get(0).getParametro(); //Indica si el tipo de fecha es: Fecha Devolucion o Fecha Correo
											String dias_Proveedor=lstConfGarantiaProveedor.get(0).getValor();
											String tipoFechaTaller=lstConfGarantiaTaller.get(0).getParametro();
											String dias_Taller=lstConfGarantiaTaller.get(0).getValor();
												if(tipoFechaProveedor.equals(ConstantesGenerales.TipoFechaGarantia.FECHADEVOLUCION.getTipoValor())){ 
													fDevol_Correo_Proveedor=formatDate.parse(fechaDevolucion);//Convierte a tipo Fecha (parse)
												}
		
												if(tipoFechaProveedor.equals(ConstantesGenerales.TipoFechaGarantia.FECHACORREO.getTipoValor())){
													fDevol_Correo_Proveedor=formatDate.parse(fechaSolicitud);//Convierte a tipo Fecha (parse)
												}
												
												if(tipoFechaTaller.equals(ConstantesGenerales.TipoFechaGarantia.FECHADEVOLUCION.getTipoValor())){ 
													fDevol_Correo_Taller=formatDate.parse(fechaDevolucion);//Convierte a tipo Fecha (parse)
												}
		
												if(tipoFechaTaller.equals(ConstantesGenerales.TipoFechaGarantia.FECHACORREO.getTipoValor())){
													fDevol_Correo_Taller=formatDate.parse(fechaSolicitud);//Convierte a tipo Fecha (parse)
												}
		
												fCreadoEl=formatDate.parse(fechaCreado);//Convierte a tipo Fecha (parse)
													
												//Si el material no existe en la Base de Taller se compara con la garantia Proveedor
												if(tallerBean==null){//Si no existe en Taller
																										
													dif=fDevol_Correo_Proveedor.getTime() - fCreadoEl.getTime();	//Resta F.Correo o F.Devol(de acuerdo a configuracion) menos la fecha de creacion							
													dias = dif / (1000 * 60 * 60 * 24); //Calcula los dias
													detalleFichaDevolucionBean.setDiasGarantia(String.valueOf(dias));
													if(tipo.equals(ConstantesGenerales.TIPO_EQUIPO) && rubro.equals(ConstantesGenerales.RUBRO_TELEFONO) && tecnologia.equals(ConstantesGenerales.TECNOLOGIA_ESTANDAR)){
														if(serie.trim().substring(0, 2).equals("EQ")){
															dias_Proveedor="330";
														}
													}
													
													if(tipo.equals(ConstantesGenerales.TIPO_EQUIPO) && rubro.equals(ConstantesGenerales.RUBRO_TELEFONO) && tecnologia.equals(ConstantesGenerales.TECNOLOGIA_INTERMEDIO)){
														if(serie.trim().substring(0, 2).equals("RI")){
															dias_Proveedor="330";
														}
													}
													
													if(dias<=Integer.parseInt(dias_Proveedor)){//Compara los dias calculados que sean menor o igual a lo ingresado en el valor configuracion garantia proveedor y asigna SI PROVEEDOR
														indGara=ConstantesGenerales.IndicadorCriterios.APROBADO.getTipoValor();
														detalleFichaDevolucionBean.setIndGar(indGara);
														detalleFichaDevolucionBean.setObsGarAprob("Cumple Garantia Proveedor - Existencia en SAP");
														detalleFichaDevolucionBean.setObsGarRechaz(ConstantesGenerales.GUION);
														detalleFichaDevolucionBean.setValidacionGarantia(ConstantesGenerales.EstadoValidacionGarantia.SIPROVEEDOR.getTipoValor());
														
													}else{//Si no se encuentra dentro del rango entonces no cumple garantia
														indGara=ConstantesGenerales.IndicadorCriterios.RECHAZADO.getTipoValor();
														detalleFichaDevolucionBean.setIndGar(indGara);
														detalleFichaDevolucionBean.setValidacionGarantia(ConstantesGenerales.EstadoValidacionGarantia.NO.getTipoValor());
														if(motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.DOAS.getTipoValor())){
															if(baseGarantiaBean==null){
																indGara=ConstantesGenerales.IndicadorCriterios.PENDIENTE.getTipoValor();
																detalleFichaDevolucionBean.setIndGar(indGara);
																detalleFichaDevolucionBean.setObsGarAprob(ConstantesGenerales.GUION);
																detalleFichaDevolucionBean.setObsGarRechaz("Enviar para validacion de Garantia - Proveedor 1..1");
															}else{
																if(baseGarantiaBean.getObservacion().equals(ConstantesGenerales.BaseGarantia.RESPONSABILIDAD_TELEFONICA.getTipoValor())){
																	indGara=ConstantesGenerales.IndicadorCriterios.APROBADO.getTipoValor();
																	detalleFichaDevolucionBean.setIndGar(indGara);
																	detalleFichaDevolucionBean.setObsGarAprob("Aprobado - Responsabilidad de Telefonica - Base Garantia - Proveedor");
																	detalleFichaDevolucionBean.setObsGarRechaz(ConstantesGenerales.GUION);
																}
																	
																if(baseGarantiaBean.getObservacion().equals(ConstantesGenerales.BaseGarantia.RESPONSABILIDAD_PUNTO.getTipoValor())){
																	indGara=ConstantesGenerales.IndicadorCriterios.RECHAZADO.getTipoValor();
																	detalleFichaDevolucionBean.setIndGar(indGara);
																	detalleFichaDevolucionBean.setObsGarAprob(ConstantesGenerales.GUION);
																	detalleFichaDevolucionBean.setObsGarRechaz("Rechazado - Responsabilidad del Punto - Base Garantia - Proveedor");

																}
															}
														}else{
															indGara=ConstantesGenerales.IndicadorCriterios.APROBADO.getTipoValor();
															detalleFichaDevolucionBean.setIndGar(indGara);
															observacionGarantiaNegocio=baseNegocioGarantia(material);//Se busca la observacion en la Base Negocio
															detalleFichaDevolucionBean.setObsGarAprob(observacionGarantiaNegocio);
															detalleFichaDevolucionBean.setObsGarRechaz(ConstantesGenerales.GUION);
														}
														
													}
															
													detalleFichaDevolucionBean.setFechaRemozadoTaller(ConstantesGenerales.GUION);
															
												}else{//Si existe en la BD Taller
													detalleFichaDevolucionBean.setRemozado_Reparado(tallerBean.getRemoz_reparado());
													if(tallerBean.getRemoz_reparado().equals(ConstantesGenerales.EstadoTaller.REPARADO.getTipoValor())){//Si es REPARADO se evalua con Taller
														fTaller=formatDate.parse(tallerBean.getFechaRecep()); //Se obtiene el valor de la Fecha de Taller (Fecha Recepcion)
														dif=fDevol_Correo_Taller.getTime() - fTaller.getTime(); //Obtiene la diferencia
														dias = dif / (1000 * 60 * 60 * 24); //Convertimos los dias
														detalleFichaDevolucionBean.setDiasGarantia(String.valueOf(dias));
														if(dias<=Integer.parseInt(dias_Taller)){//Si menor o igual al valor asignado en la tabla configuracion, SE APRUEBA y asigna SI TALLER															
															indGara=ConstantesGenerales.IndicadorCriterios.APROBADO.getTipoValor();
															detalleFichaDevolucionBean.setIndGar(indGara);
															detalleFichaDevolucionBean.setObsGarAprob("Cumple Garantia Reparado");	
															detalleFichaDevolucionBean.setObsGarRechaz(ConstantesGenerales.GUION);																													
															detalleFichaDevolucionBean.setValidacionGarantia(ConstantesGenerales.EstadoValidacionGarantia.SITALLER.getTipoValor());															
														}else{
															indGara=ConstantesGenerales.IndicadorCriterios.RECHAZADO.getTipoValor();
															detalleFichaDevolucionBean.setIndGar(indGara);
															detalleFichaDevolucionBean.setValidacionGarantia(ConstantesGenerales.EstadoValidacionGarantia.NO.getTipoValor());
															
															if(motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.DOAS.getTipoValor())){
																if(baseGarantiaBean==null){
																	indGara=ConstantesGenerales.IndicadorCriterios.PENDIENTE.getTipoValor();
																	detalleFichaDevolucionBean.setIndGar(indGara);
																	detalleFichaDevolucionBean.setObsGarAprob(ConstantesGenerales.GUION);
																	detalleFichaDevolucionBean.setObsGarRechaz("Enviar para validacion de Garantia - Taller - Reparado 1..1");
																}else{
																	if(baseGarantiaBean.getObservacion().equals(ConstantesGenerales.BaseGarantia.RESPONSABILIDAD_TELEFONICA.getTipoValor())){
																		indGara=ConstantesGenerales.IndicadorCriterios.APROBADO.getTipoValor();
																		detalleFichaDevolucionBean.setIndGar(indGara);
																		detalleFichaDevolucionBean.setObsGarAprob("Aprobado - Responsabilidad de Telefonica - Base Garantia - Taller - Reparado");
																		detalleFichaDevolucionBean.setObsGarRechaz(ConstantesGenerales.GUION);
																	}
																		
																	if(baseGarantiaBean.getObservacion().equals(ConstantesGenerales.BaseGarantia.RESPONSABILIDAD_PUNTO.getTipoValor())){
																		indGara=ConstantesGenerales.IndicadorCriterios.RECHAZADO.getTipoValor();
																		detalleFichaDevolucionBean.setIndGar(indGara);
																		detalleFichaDevolucionBean.setObsGarAprob("ConstantesGenerales.GUION");
																		detalleFichaDevolucionBean.setObsGarRechaz("Rechazado - Responsabilidad del Punto - Base Garantia - Taller - Reparado");

																	}
																}
															}else{
																indGara=ConstantesGenerales.IndicadorCriterios.APROBADO.getTipoValor();
																detalleFichaDevolucionBean.setIndGar(indGara);
																observacionGarantiaNegocio=baseNegocioGarantia(material);//Se busca la observacion en la Base Negocio
																detalleFichaDevolucionBean.setObsGarAprob(observacionGarantiaNegocio);
																detalleFichaDevolucionBean.setObsGarRechaz(ConstantesGenerales.GUION);
															}
															
														}
																
													}else{//Si es REMOZADO
														fTaller=formatDate.parse(tallerBean.getFechaRecep());
														dif=fDevol_Correo_Taller.getTime() - fTaller.getTime();
														dias = dif / (1000 * 60 * 60 * 24);
														detalleFichaDevolucionBean.setDiasGarantia(String.valueOf(dias));
														detalleFichaDevolucionBean.setValidacionGarantia(ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor());
														indGara=ConstantesGenerales.IndicadorCriterios.APROBADO.getTipoValor();
														detalleFichaDevolucionBean.setIndGar(indGara);
														detalleFichaDevolucionBean.setObsGarAprob("Aprobado - No Aplica los Remozados");																													
														detalleFichaDevolucionBean.setObsGarRechaz(ConstantesGenerales.GUION);	

											}
													detalleFichaDevolucionBean.setFechaRemozadoTaller(tallerBean.getFechaRecep());
										}

									}
								}
							}
						}
					}
					
					

					
					if(estadoValidacion.equals(ConstantesGenerales.EstadoValidacionActa.SINVALIDAR.getTipoValor())){
						if(materialSAPBean6_0.getStatusSAP().equals(ConstantesGenerales.ExistenciaSAP.NOEXISTESAP.getTipoValor()) &&
						   materialSAPBean4_7.getStatusSAP().equals(ConstantesGenerales.ExistenciaSAP.NOEXISTESAP.getTipoValor())){
							detalleFichaDevolucionBean.setDiasGarantia(ConstantesGenerales.GUION);								
						}
						
						if(tipo.equals(ConstantesGenerales.TIPO_SMARTCARD) && rubro.equals(ConstantesGenerales.RUBRO_DECO) && tecnologia.equals(ConstantesGenerales.TECNOLOGIA_SMARTCARD)){
							indGara=ConstantesGenerales.IndicadorCriterios.NONE.getTipoValor();
							detalleFichaDevolucionBean.setIndGar(indGara);
							detalleFichaDevolucionBean.setObsGarAprob("No es sujeto a Garantia - No Aplica Tarjetas Inteligentes");	
							detalleFichaDevolucionBean.setObsGarRechaz(ConstantesGenerales.GUION);	
							detalleFichaDevolucionBean.setValidacionGarantia(ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor());
						}	
						
						
						if(motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.DOAS.getTipoValor())){ //Para el caso de DOAS SIN SERIE						
							if(serie.equals(ConstantesGenerales.SINSERIE)){
								indGara=ConstantesGenerales.IndicadorCriterios.NONE.getTipoValor();
								detalleFichaDevolucionBean.setIndGar(indGara);
								detalleFichaDevolucionBean.setObsGarAprob("No es sujeto a Garantia - No Aplica a DOAS - SIN SERIE");	
								detalleFichaDevolucionBean.setObsGarRechaz(ConstantesGenerales.GUION);	
								detalleFichaDevolucionBean.setValidacionGarantia(ConstantesGenerales.EstadoValidacionGarantia.NOAPLICA.getTipoValor());
								aplicaGarantia=false;
							}
						}
					}
					
					
					/*****Validacion Externa*****/
					
					BaseValidacionExternaBean baseValidacionExternaBean=null; //Contiene la informacion de la Garantia para determinar si es Responsabilidad del Punto o Telefonica
					baseValidacionExternaBean=obtenerBaseValidacionExterna(nroTicket, material, serie,usuario);
					detalleFichaDevolucionBean.setEstado(estadoDetalle);
					if(baseValidacionExternaBean!=null){
			
						if(baseValidacionExternaBean.getEstado().equals(ConstantesGenerales.EstadoValidacionActa.APROBADO.getTipoValor())){
							detalleFichaDevolucionBean.setObservacionAprobado(detalleFichaDevolucionBean.getObsBodAprob() + "/" + detalleFichaDevolucionBean.getObsLoteAprob() + "/" + detalleFichaDevolucionBean.getObsGarAprob() + "/Validación Externa - Aprobado: " + baseValidacionExternaBean.getMotivo());
							detalleFichaDevolucionBean.setMotivoRechazoDevolucion(detalleFichaDevolucionBean.getObsBodRechaz() + "/" + detalleFichaDevolucionBean.getObsLoteRechaz() + "/" + detalleFichaDevolucionBean.getObsGarRechaz());
							detalleFichaDevolucionBean.setEstado(baseValidacionExternaBean.getEstado());
						}
						
						if(baseValidacionExternaBean.getEstado().equals(ConstantesGenerales.EstadoValidacionActa.RECHAZADO.getTipoValor())){
							detalleFichaDevolucionBean.setObservacionAprobado(detalleFichaDevolucionBean.getObsBodAprob() + "/" + detalleFichaDevolucionBean.getObsLoteAprob() + "/" + detalleFichaDevolucionBean.getObsGarAprob());
							detalleFichaDevolucionBean.setMotivoRechazoDevolucion(detalleFichaDevolucionBean.getObsBodRechaz() + "/" + detalleFichaDevolucionBean.getObsLoteRechaz() + "/" + detalleFichaDevolucionBean.getObsGarRechaz() + "/Validación Externa - Rechazo: " + baseValidacionExternaBean.getMotivo());
							detalleFichaDevolucionBean.setEstado(baseValidacionExternaBean.getEstado());
						}						

					}else{//Si no esta lleno validacion externa
						indBod=detalleFichaDevolucionBean.getIndBod();
						indLote=detalleFichaDevolucionBean.getIndLote();
						indGara=detalleFichaDevolucionBean.getIndGar();
						
						if(indGara.equals(ConstantesGenerales.IndicadorCriterios.APROBADO.getTipoValor()) || 
						   indGara.equals(ConstantesGenerales.IndicadorCriterios.NONE.getTipoValor())){
							detalleFichaDevolucionBean.setEstado(ConstantesGenerales.EstadoValidacionActa.APROBADO.getTipoValor());
							if(indBod.equals(ConstantesGenerales.IndicadorCriterios.PENDIENTE.getTipoValor())) {
								detalleFichaDevolucionBean.setEstado(ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor());
							}

							if(indLote.equals(ConstantesGenerales.IndicadorCriterios.PENDIENTE.getTipoValor())) {
								detalleFichaDevolucionBean.setEstado(ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor());									
							}
						} 		
						
						if(indGara.equals(ConstantesGenerales.IndicadorCriterios.PENDIENTE.getTipoValor())) {
							detalleFichaDevolucionBean.setEstado(ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor());									
						}		
						
						if(indBod.equals(ConstantesGenerales.IndicadorCriterios.RECHAZADO.getTipoValor()) || 
						   indLote.equals(ConstantesGenerales.IndicadorCriterios.RECHAZADO.getTipoValor()) || 
						   indGara.equals(ConstantesGenerales.IndicadorCriterios.RECHAZADO.getTipoValor())){
								detalleFichaDevolucionBean.setEstado(ConstantesGenerales.EstadoValidacionActa.RECHAZADO.getTipoValor());
						}

						if(estadoValidacion.equals(ConstantesGenerales.EstadoValidacionActa.SINVALIDAR.getTipoValor())){
							detalleFichaDevolucionBean.setObservacionAprobado(detalleFichaDevolucionBean.getObsBodAprob() + "/" + detalleFichaDevolucionBean.getObsLoteAprob() + "/" + detalleFichaDevolucionBean.getObsGarAprob());
							detalleFichaDevolucionBean.setMotivoRechazoDevolucion(detalleFichaDevolucionBean.getObsBodRechaz() + "/" + detalleFichaDevolucionBean.getObsLoteRechaz() + "/" + detalleFichaDevolucionBean.getObsGarRechaz());
						}

						if(estadoValidacion.equals(ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor())){
							detalleFichaDevolucionBean.setObservacionAprobado(detalleFichaDevolucionBean.getObsBodAprob() + "/" + detalleFichaDevolucionBean.getObsLoteAprob() + "/" + detalleFichaDevolucionBean.getObsGarAprob());
							detalleFichaDevolucionBean.setMotivoRechazoDevolucion(detalleFichaDevolucionBean.getObsBodRechaz() + "/" + detalleFichaDevolucionBean.getObsLoteRechaz() + "/" + detalleFichaDevolucionBean.getObsGarRechaz());							
						}
					}
					
					
					/*****Asignar Modo Recojo*****/
					
					if(lstConfModoRecojo!=null){
						if(lstConfModoRecojo.size()>0){
							String seriadoNoSeriado=lstConfModoRecojo.get(0).getParametro();
							
							if(seriadoNoSeriado.equals(ConstantesGenerales.NO)){
								if(lstConfModoRecojo.get(0).getValor().equals(ConstantesGenerales.ModoRecojo.NIVELSERIE.getCriterio())){
									detalleFichaDevolucionBean.setModoRecojo(ConstantesGenerales.ModoRecojo.NIVELSERIE.getDescripcion());									
								}

								if(lstConfModoRecojo.get(0).getValor().equals(ConstantesGenerales.ModoRecojo.NIVELCANTIDAD.getCriterio())){
									detalleFichaDevolucionBean.setModoRecojo(ConstantesGenerales.ModoRecojo.NIVELCANTIDAD.getDescripcion());									
								}
							}
							
							if(seriadoNoSeriado.equals(ConstantesGenerales.SI)){
								if(seriado.equals(ConstantesGenerales.SI)){
									detalleFichaDevolucionBean.setModoRecojo(ConstantesGenerales.ModoRecojo.NIVELSERIE.getDescripcion());									
								}

								if(seriado.equals(ConstantesGenerales.NO)){
									detalleFichaDevolucionBean.setModoRecojo(ConstantesGenerales.ModoRecojo.NIVELCANTIDAD.getDescripcion());
								}
							}


						}
					}
					
					
										
					/**
					 * Validaciones para Devoluci - No Seriados
					 * **/
					
					
					if(motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.SOBRESTOCK.getTipoValor())){
						detalleFichaDevolucionBean.setEstado("");
						String estado=null;						
						estado=esTramosMinimos(codSAPActa,cantMateriales);
						if(estado.equals(ConstantesGenerales.EstadoBusqueda.EXISTE.getTipoValor())){
							detalleFichaDevolucionBean.setTramosMinimos(ConstantesGenerales.EstadoBusqueda.EXISTEP.getTipoValor());
							detalleFichaDevolucionBean.setMotivoRechazoDevolucion(ConstantesGenerales.GUION);							
						}
						if(estado.equals(ConstantesGenerales.EstadoBusqueda.NOCUMPLE.getTipoValor())){
							detalleFichaDevolucionBean.setTramosMinimos(ConstantesGenerales.EstadoBusqueda.NOCUMPLE.getTipoValor());
							detalleFichaDevolucionBean.setEstado(ConstantesGenerales.EstadoValidacionActa.RECHAZADO.getTipoValor());
							detalleFichaDevolucionBean.setMotivoRechazoDevolucion("No cumple con el Tramo Minimo");							
						}

						if(estado.equals(ConstantesGenerales.EstadoBusqueda.NOEXISTE.getTipoValor())){
							detalleFichaDevolucionBean.setTramosMinimos(ConstantesGenerales.EstadoBusqueda.NOEXISTE.getTipoValor());
							detalleFichaDevolucionBean.setEstado(ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor());
							detalleFichaDevolucionBean.setMotivoRechazoDevolucion("El material no existe en la Base de Tramos Minimos");							
						}

					}

					if(motivo.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.RESIDUOSOPERATIVOS.getTipoValor())){
						detalleFichaDevolucionBean.setEstado(ConstantesGenerales.EstadoValidacionActa.APROBADO.getTipoValor());
					}
					
					
				

					detalleFichaDevolucionBean.setFechaValidacion(util.obtenerFecha() + " " + util.obtenerHora());
					
					
					listaDetalleFicha.add(detalleFichaDevolucionBean);
				}				
				
			}
			
		} catch (SQLException e) {
			listaDetalleFicha=null;
            System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
            e.printStackTrace();
		} catch (ParseException e) {
            System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
            e.printStackTrace();
		}

		return listaDetalleFicha;

	}


	@Override
	public TipoBodegaBean obtenerTipoBodega(String centro, String almacen,String entidad) {
		TipoBodegaBean tipoBodegaBean = null;
		String sql=null;
		if((centro.equals("") && almacen.equals("")) || (centro.equals(ConstantesGenerales.GUION) && almacen.equals(ConstantesGenerales.GUION))){
			sql="SELECT TPBOD.CODTIPOBODEGA,TPBOD.NOMTIPOBODEGA FROM BODEGAS BOD,TIPOBODEGA TPBOD WHERE BOD.TIPOBODEGA=TPBOD.CODTIPOBODEGA AND " + 
				"BOD.CENTRO IS NULL AND BOD.ALMACEN IS NULL AND BOD.CODENTIDAD='" + entidad + "'";	
		}else{
			sql="SELECT TPBOD.CODTIPOBODEGA,TPBOD.NOMTIPOBODEGA FROM BODEGAS BOD,TIPOBODEGA TPBOD WHERE BOD.TIPOBODEGA=TPBOD.CODTIPOBODEGA AND " + 
				"TRIM(BOD.CENTRO)='" + centro.trim() + "' AND TRIM(BOD.ALMACEN)='" + almacen.trim() + "' AND BOD.CODENTIDAD='" + entidad + "'";
		}
			
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				tipoBodegaBean = new TipoBodegaBean();
				tipoBodegaBean.setCodTipoBodegas(rs.getString(1));
				tipoBodegaBean.setNomTipoBodegas(rs.getString(2));
			}else{
				tipoBodegaBean = new TipoBodegaBean();
				tipoBodegaBean.setCodTipoBodegas("");
				tipoBodegaBean.setNomTipoBodegas("");
			}
		} catch (SQLException ex) {
			tipoBodegaBean = null;
            System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
	            System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		return tipoBodegaBean;
	}

	@Override
	public boolean esLotizable(String material) {
		boolean esLotizable=false;
		Util util=new Util();
		String sql="SELECT LOTIZABLE FROM MATERIAL WHERE LPAD(TRIM(CODMATERIAL),20,'0')='" + util.rellenar(material.trim(), 20) + "' AND LOTIZABLE='SI'";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				esLotizable=true;
			}
		} catch (SQLException ex) {
			esLotizable=false;
            System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
	            System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		return esLotizable;
		
	}

	@Override
	public String baseNegocioGarantia(String material) {
		String observacion=ConstantesGenerales.GUION;
		Util util=new Util();
		String sql="SELECT OBSERVACION FROM BASE_GARANTIA_NEGOCIO WHERE LPAD(TRIM(MATERIAL),20,'0')='" + util.rellenar(material.trim(), 20) + "'";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				observacion=rs.getString(1);
			}else{
				observacion=ConstantesGenerales.GUION;
			}
		} catch (SQLException ex) {
			observacion=ConstantesGenerales.GUION;
            System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
	            System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		return observacion;
	}

	@Override
	public BaseGarantiaBean obtenerDatosBaseGarantia(String material,String serie,String usuario) {
		BaseGarantiaBean baseGarantiaBean = null;
		Util util=new Util();
		String observacion="";
		String fDespacho="";
		String sql="SELECT FDESPACHO,OBSERVACION FROM BASEGARANTIA WHERE LPAD(TRIM(CODMATERIAL),20,'0')='" + util.rellenar(material.trim(), 20) + "' AND LPAD(TRIM(SERIE),25,'0')='" + util.rellenar(serie.trim(),25) + "' AND USUARIO='" + usuario + "'";
	
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				baseGarantiaBean = new BaseGarantiaBean();
				fDespacho=rs.getString(1)==null?"":rs.getString(1);
				observacion=rs.getString(2)==null?"":rs.getString(2);
				baseGarantiaBean.setfDespachoSAP(fDespacho);
				baseGarantiaBean.setObservacion(observacion);
			}
		} catch (SQLException ex) {
			baseGarantiaBean = null;
            System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
	            System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		return baseGarantiaBean;
	}

	@Override
	public boolean esBodegaLiquidada(String centro, String almacen) {
		boolean esBodegaLiquidada=false;
		
		String sql="SELECT TPBOD.CODTIPOBODEGA,TPBOD.NOMTIPOBODEGA FROM BODEGAS BOD,TIPOBODEGA TPBOD " + 
				   "WHERE BOD.TIPOBODEGA=TPBOD.CODTIPOBODEGA AND BOD.CENTRO IS NULL AND BOD.ALMACEN IS NULL";

		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				esBodegaLiquidada=true;
			}
		} catch (SQLException ex) {
			esBodegaLiquidada=false;
            System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
	            System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		return esBodegaLiquidada;
	}

	@Override
	public MaterialBean obtenerDatosMaterial(String material,String tipo,String rubro,String tecnologia) {
		Util util=new Util();
		String sql = "SELECT MAT.CODMATERIAL,MAT.DESCRIPCION,MAT.UMD,MAT.PESO,MAT.VOLUNITARIO,MAT.SERIADO,MAT.PRECIOEQUIPO,MAT.LOTIZABLE,PROV.NOMPROV,NEG.NOMNEGOCIO,SOC.ABRSOCIEDAD,TPMAT.NOMTIPOMATERIAL,RUB.NOMBRERUBRO,TEC.NOMTECNOLOGIA " + 
					 "FROM MATERIAL MAT,NEGOCIO NEG,RUBRO RUB,TIPOMATERIAL TPMAT,TECNOLOGIA TEC,SOCIEDAD SOC,PROVISION_PLANTAEXTERNA PROV " +  
					 "WHERE LPAD(TRIM(UPPER(MAT.CODMATERIAL)),20,'0')='" + util.rellenar(material.trim().toUpperCase(),20) + "' AND MAT.CODNEGOCIO=NEG.CODNEGOCIO AND MAT.CODRUBRO=RUB.CODRUBRO AND MAT.CODTECNOLOGIA=TEC.CODTECNOLOGIA AND MAT.CODTIPOMATERIAL=TPMAT.CODTIPOMATERIAL AND SOC.CODSOCIEDAD=MAT.SOCIEDAD AND PROV.CODPROV=MAT.CODPROV";		

		MaterialBean materialBean=null;

		String sqlTotal=null;
				
		Connection cn = null;
		
		String sqlStandar = "SELECT MAT.DESCRIPCION,MAT.UMD,MAT.PESO,MAT.VOLUNITARIO,MAT.SERIADO,MAT.PRECIOEQUIPO,MAT.LOTIZABLE,PROV.NOMPROV,NEG.NOMNEGOCIO,SOC.ABRSOCIEDAD,TPMAT.NOMTIPOMATERIAL,RUB.NOMBRERUBRO,TEC.NOMTECNOLOGIA " + 
				 			"FROM MATERIAL MAT,NEGOCIO NEG,RUBRO RUB,TIPOMATERIAL TPMAT,TECNOLOGIA TEC,SOCIEDAD SOC,PROVISION_PLANTAEXTERNA PROV " +  
				 			"WHERE MAT.CODMATERIAL LIKE 'MST%' AND MAT.CODNEGOCIO=NEG.CODNEGOCIO AND MAT.CODRUBRO=RUB.CODRUBRO AND SOC.CODSOCIEDAD=MAT.SOCIEDAD AND PROV.CODPROV=MAT.CODPROV";		
		
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = null;
			ResultSet rsStandar = null;
			rs=st.executeQuery(sql);			
			if(rs.next()){
				materialBean=new MaterialBean();
				if(rs.getString(1)==null){
					materialBean.setCodigo(ConstantesGenerales.GUION);						
				}else{
					materialBean.setCodigo(rs.getString(1));						
				}
				if(rs.getString(2)==null){
					materialBean.setDescripcion(ConstantesGenerales.GUION);
				}else{
					materialBean.setDescripcion(rs.getString(2));						
				}
				
				if(rs.getString(3)==null){
					materialBean.setUmd(ConstantesGenerales.GUION);
				}else{
					materialBean.setUmd(rs.getString(3));
				}
		
				if(rs.getString(4)==null){
					materialBean.setPeso(ConstantesGenerales.GUION);
				}else{
					materialBean.setPeso(rs.getString(4));
				}
				
				if(rs.getString(5)==null){
					materialBean.setVolUnitario(ConstantesGenerales.GUION);
				}else{
					materialBean.setVolUnitario(rs.getString(5));
				}
				
				if(rs.getString(6)==null){
					materialBean.setSeriado(ConstantesGenerales.GUION);
				}else{
					materialBean.setSeriado(rs.getString(6));
				}
				
				if(rs.getString(7)==null){
					materialBean.setPrecio(ConstantesGenerales.GUION);
				}else{
					materialBean.setPrecio(rs.getString(7));
				}
				
				if(rs.getString(8)==null){
					materialBean.setLotizable(ConstantesGenerales.GUION);
				}else{
					materialBean.setLotizable(rs.getString(8));
				}
				
				if(rs.getString(9)==null){
					materialBean.setProvision(ConstantesGenerales.GUION);
				}else{
					materialBean.setProvision(rs.getString(9));
				}
				
				if(rs.getString(10)==null){
					materialBean.setNegocio(ConstantesGenerales.GUION);
				}else{
					materialBean.setNegocio(rs.getString(10));
				}
				
				if(rs.getString(11)==null){
					materialBean.setSociedad(ConstantesGenerales.GUION);
				}else{
					materialBean.setSociedad(rs.getString(11));
				}
				
				if(rs.getString(12)==null){
					materialBean.setTipoMaterial(ConstantesGenerales.GUION);
				}else{
					materialBean.setTipoMaterial(rs.getString(12));
				}
				
				if(rs.getString(13)==null){
					materialBean.setRubro(ConstantesGenerales.GUION);
				}else{
					materialBean.setRubro(rs.getString(13));
				}		
				
				if(rs.getString(14)==null){
					materialBean.setTecnologia(ConstantesGenerales.GUION);
				}else{
					materialBean.setTecnologia(rs.getString(14));
				}			
			}else{
				sqlTotal=sqlStandar + " AND TRIM(UPPER(TPMAT.NOMTIPOMATERIAL))=TRIM(UPPER('" + tipo + "')) AND TRIM(UPPER(RUB.NOMBRERUBRO))=TRIM(UPPER('" + rubro  + "')) AND TRIM(UPPER(TEC.NOMTECNOLOGIA))=TRIM(UPPER('" + tecnologia + "'))";
				rsStandar=st.executeQuery(sqlTotal);

				materialBean=new MaterialBean();
				materialBean.setCodigo(ConstantesGenerales.GUION);
								
				if(rsStandar.next()){
					if(rsStandar.getString(1)==null){
						materialBean.setDescripcion(ConstantesGenerales.GUION);
					}else{
						materialBean.setDescripcion(rsStandar.getString(1));						
					}
					
					if(rsStandar.getString(2)==null){
						materialBean.setUmd(ConstantesGenerales.GUION);
					}else{
						materialBean.setUmd(rsStandar.getString(2));
					}
			
					if(rsStandar.getString(3)==null){
						materialBean.setPeso(ConstantesGenerales.GUION);
					}else{
						materialBean.setPeso(rsStandar.getString(3));
					}
					
					if(rsStandar.getString(4)==null){
						materialBean.setVolUnitario(ConstantesGenerales.GUION);
					}else{
						materialBean.setVolUnitario(rsStandar.getString(4));
					}
					
					if(rsStandar.getString(5)==null){
						materialBean.setSeriado(ConstantesGenerales.GUION);
					}else{
						materialBean.setSeriado(rsStandar.getString(5));
					}
					
					if(rsStandar.getString(6)==null){
						materialBean.setPrecio(ConstantesGenerales.GUION);
					}else{
						materialBean.setPrecio(rsStandar.getString(6));
					}
					
					if(rsStandar.getString(7)==null){
						materialBean.setLotizable(ConstantesGenerales.GUION);
					}else{
						materialBean.setLotizable(rsStandar.getString(7));
					}
					
					if(rsStandar.getString(8)==null){
						materialBean.setProvision(ConstantesGenerales.GUION);
					}else{
						materialBean.setProvision(rsStandar.getString(8));
					}
					
					if(rsStandar.getString(9)==null){
						materialBean.setNegocio(ConstantesGenerales.GUION);
					}else{
						materialBean.setNegocio(rsStandar.getString(9));
					}
					
					if(rsStandar.getString(10)==null){
						materialBean.setSociedad(ConstantesGenerales.GUION);
					}else{
						materialBean.setSociedad(rsStandar.getString(10));
					}
					
					if(rsStandar.getString(11)==null){
						materialBean.setTipoMaterial(ConstantesGenerales.GUION);
					}else{
						materialBean.setTipoMaterial(rsStandar.getString(11));
					}
					
					if(rsStandar.getString(12)==null){
						materialBean.setRubro(ConstantesGenerales.GUION);
					}else{
						materialBean.setRubro(rsStandar.getString(12));
					}		
					
					if(rsStandar.getString(13)==null){
						materialBean.setTecnologia(ConstantesGenerales.GUION);
					}else{
						materialBean.setTecnologia(rsStandar.getString(13));
					}	

				}else{
					sqlTotal=sqlStandar + " AND TRIM(UPPER(TPMAT.NOMTIPOMATERIAL))=TRIM(UPPER('" + tipo + "')) AND TRIM(UPPER(RUB.NOMBRERUBRO))=TRIM(UPPER('" + rubro  + "')) ";
					rsStandar=st.executeQuery(sqlTotal);

					materialBean=new MaterialBean();
					materialBean.setCodigo(ConstantesGenerales.GUION);
					
					if(rsStandar.next()){
						if(rsStandar.getString(1)==null){
							materialBean.setDescripcion(ConstantesGenerales.GUION);
						}else{
							materialBean.setDescripcion(rsStandar.getString(1));						
						}
						
						if(rsStandar.getString(2)==null){
							materialBean.setUmd(ConstantesGenerales.GUION);
						}else{
							materialBean.setUmd(rsStandar.getString(2));
						}
				
						if(rsStandar.getString(3)==null){
							materialBean.setPeso(ConstantesGenerales.GUION);
						}else{
							materialBean.setPeso(rsStandar.getString(3));
						}
						
						if(rsStandar.getString(4)==null){
							materialBean.setVolUnitario(ConstantesGenerales.GUION);
						}else{
							materialBean.setVolUnitario(rsStandar.getString(4));
						}
						
						if(rsStandar.getString(5)==null){
							materialBean.setSeriado(ConstantesGenerales.GUION);
						}else{
							materialBean.setSeriado(rsStandar.getString(5));
						}
						
						if(rsStandar.getString(6)==null){
							materialBean.setPrecio(ConstantesGenerales.GUION);
						}else{
							materialBean.setPrecio(rsStandar.getString(6));
						}
						
						if(rsStandar.getString(7)==null){
							materialBean.setLotizable(ConstantesGenerales.GUION);
						}else{
							materialBean.setLotizable(rsStandar.getString(7));
						}
						
						if(rsStandar.getString(8)==null){
							materialBean.setProvision(ConstantesGenerales.GUION);
						}else{
							materialBean.setProvision(rsStandar.getString(8));
						}
						
						if(rsStandar.getString(9)==null){
							materialBean.setNegocio(ConstantesGenerales.GUION);
						}else{
							materialBean.setNegocio(rsStandar.getString(9));
						}
						
						if(rsStandar.getString(10)==null){
							materialBean.setSociedad(ConstantesGenerales.GUION);
						}else{
							materialBean.setSociedad(rsStandar.getString(10));
						}
						
						if(rsStandar.getString(11)==null){
							materialBean.setTipoMaterial(ConstantesGenerales.GUION);
						}else{
							materialBean.setTipoMaterial(rsStandar.getString(11));
						}
						
						if(rsStandar.getString(12)==null){
							materialBean.setRubro(ConstantesGenerales.GUION);
						}else{
							materialBean.setRubro(rsStandar.getString(12));
						}		
						
						if(rsStandar.getString(13)==null){
							materialBean.setTecnologia(ConstantesGenerales.GUION);
						}else{
							materialBean.setTecnologia(rsStandar.getString(13));
						}	

					}else{
						materialBean.setCodigo(ConstantesGenerales.GUION);
						materialBean.setDescripcion(ConstantesGenerales.GUION);
						materialBean.setUmd(ConstantesGenerales.GUION);
						materialBean.setPeso(ConstantesGenerales.GUION);
						materialBean.setVolUnitario(ConstantesGenerales.GUION);
						materialBean.setSeriado(ConstantesGenerales.GUION);					
						materialBean.setPrecio(ConstantesGenerales.GUION);
						materialBean.setLotizable(ConstantesGenerales.GUION);
						materialBean.setProvision(ConstantesGenerales.GUION);
						materialBean.setNegocio(ConstantesGenerales.GUION);
						materialBean.setSociedad(ConstantesGenerales.GUION);
						materialBean.setTipoMaterial(ConstantesGenerales.GUION);
						materialBean.setRubro(ConstantesGenerales.GUION);
						materialBean.setTecnologia(ConstantesGenerales.GUION);

					}
					
				}
				
			}				
	
		} catch (SQLException e) {
            System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
		}

		return materialBean;		
		
	}

	@Override
	public String esTramosMinimos(String material,int libreUtilizacion) {
		String estado=ConstantesGenerales.EstadoBusqueda.NOEXISTE.getTipoValor();
		Util util=new Util();
		String sql="SELECT CODMATERIAL,TEXTOBREVE,NUEVAPOLITICA FROM TRAMOS_MINIMOS WHERE LPAD(TRIM(CODMATERIAL),20,'0')='" + util.rellenar(material.trim(), 20) + "'";
		Connection cn = null;
		ResultSet rs=null;
		ResultSet rsFiltro=null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				sql=sql + " AND NUEVAPOLITICA>=" + libreUtilizacion;
				rsFiltro=st.executeQuery(sql);
				estado=ConstantesGenerales.EstadoBusqueda.NOCUMPLE.getTipoValor();
				if(rsFiltro.next()){
					//El mensaje
					estado=ConstantesGenerales.EstadoBusqueda.EXISTE.getTipoValor();
				}
			}
		} catch (SQLException ex) {
            System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
			estado=ConstantesGenerales.EstadoBusqueda.NOEXISTE.getTipoValor();
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
	            System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		return estado;
	}



	@Override
	public MaterialSAPBean obtenerDatosSAPDevoluci(String material,String tipoSAP,String tipo,String rubro, String tecnologia,String usuario) {

		String sql = "SELECT MSAP.CODMATERIAL,MSAP.DENOMINACION,MSAP.CENTRO,MSAP.ALMACEN,MSAP.LOTE,MSAP.LIBRE_UTILIZACION FROM MATERIALSAP MSAP,MATERIAL MAT,NEGOCIO NEG,RUBRO RUB,TIPOMATERIAL TPMAT,TECNOLOGIA TEC,SOCIEDAD SOC,PROVISION_PLANTAEXTERNA PROV " +  
					 "WHERE LPAD(TRIM(UPPER(MSAP.CODMATERIAL)),20,'0')=LPAD(TRIM(MAT.CODMATERIAL),20,'0') AND MAT.CODNEGOCIO=NEG.CODNEGOCIO AND MAT.CODRUBRO=RUB.CODRUBRO AND MAT.CODTECNOLOGIA=TEC.CODTECNOLOGIA AND MAT.CODTIPOMATERIAL=TPMAT.CODTIPOMATERIAL AND SOC.CODSOCIEDAD=MAT.SOCIEDAD AND PROV.CODPROV=MAT.CODPROV AND MSAP.TIPO='" + tipoSAP + "' AND MSAP.USUARIO='" + usuario + "' ";		

		String sqlFiltro=null;
		String sqlTotal=null;
		MaterialSAPBean materialSAPBean=null;
		Util util=new Util();
		
		String sqlStandar="SELECT MAT.DESCRIPCION FROM MATERIAL MAT,RUBRO RUB,TIPOMATERIAL TPMAT WHERE MAT.CODRUBRO=RUB.CODRUBRO AND MAT.CODTIPOMATERIAL=TPMAT.CODTIPOMATERIAL " + 
						  " AND MAT.CODMATERIAL LIKE 'MST%'";
		
		Connection cn = null;
		
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rsFiltro = null;
			ResultSet rsStandar = null;
			sqlFiltro=" AND LPAD(TRIM(UPPER(MSAP.CODMATERIAL)),20,'0')='" + util.rellenar(material.toUpperCase().trim(), 20) + "'";
			sqlTotal=sql+ sqlFiltro;
			rsFiltro=st.executeQuery(sqlTotal);
			
			if(rsFiltro.next()){
				materialSAPBean=new MaterialSAPBean();
	
				if(rsFiltro.getString(1)==null){
					materialSAPBean.setCodMaterial(ConstantesGenerales.GUION);
				}else{
					materialSAPBean.setCodMaterial(rsFiltro.getString(1));
				}
				
				if(rsFiltro.getString(2)==null){
					materialSAPBean.setDenominacion(ConstantesGenerales.GUION);
				}else{
					materialSAPBean.setDenominacion(rsFiltro.getString(2));
				}
				
				if(rsFiltro.getString(3)==null){
					materialSAPBean.setCentro(ConstantesGenerales.GUION);
				}else{
					materialSAPBean.setCentro(rsFiltro.getString(3));						
				}
				
				if(rsFiltro.getString(4)==null){
					materialSAPBean.setAlmacen(ConstantesGenerales.GUION);
				}else{
					materialSAPBean.setAlmacen(rsFiltro.getString(4));
				}
				
				if(rsFiltro.getString(5)==null){
					materialSAPBean.setLote(ConstantesGenerales.GUION);
				}else{
					materialSAPBean.setLote(rsFiltro.getString(5));
				}				
				
				if(rsFiltro.getString(6)==null){
					materialSAPBean.setLibreUtilizacion(0.0);
				}else{
					materialSAPBean.setLibreUtilizacion(Double.parseDouble(rsFiltro.getString(6)));
				}		
				
				materialSAPBean.setStatusSAP(ConstantesGenerales.ExistenciaSAP.EXISTESAP.getTipoValor());
			}else{
				sqlTotal=sqlStandar + " AND TRIM(UPPER(TPMAT.NOMTIPOMATERIAL))=TRIM(UPPER('" + tipo + "')) AND TRIM(UPPER(RUB.NOMBRERUBRO))=TRIM(UPPER('" + rubro  + "')) ";
				rsStandar=st.executeQuery(sqlTotal);
				materialSAPBean=new MaterialSAPBean();
				materialSAPBean.setNumeroSerie(ConstantesGenerales.GUION);
				materialSAPBean.setCentro(ConstantesGenerales.GUION);
				materialSAPBean.setAlmacen(ConstantesGenerales.GUION);
				materialSAPBean.setCodMaterial(ConstantesGenerales.GUION);
				
				if(rsStandar.next()){
					materialSAPBean.setDenominacion(rsStandar.getString(1));						
				}else{
					materialSAPBean.setDenominacion(ConstantesGenerales.GUION);	
				}
		
				materialSAPBean.setLote(ConstantesGenerales.GUION);
				materialSAPBean.setTipoStocks(ConstantesGenerales.GUION);
				materialSAPBean.setStatuSistema(ConstantesGenerales.GUION);
				materialSAPBean.setStatusUsuario(ConstantesGenerales.GUION);
				materialSAPBean.setModificadoEl(ConstantesGenerales.GUION);
				materialSAPBean.setModificadoPor(ConstantesGenerales.GUION);					
				materialSAPBean.setCreadoEl(ConstantesGenerales.GUION);
				materialSAPBean.setCreadoPor(ConstantesGenerales.GUION);
				materialSAPBean.setLibreUtilizacion(0.0);
				materialSAPBean.setStatusSAP(ConstantesGenerales.ExistenciaSAP.NOEXISTESAP.getTipoValor());
			}				
			
		} catch (SQLException e) {
            System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
		}
		
		
		
		return materialSAPBean;
	}

	@Override
	public BaseValidacionExternaBean obtenerBaseValidacionExterna(String ticket, String material, String serie,String usuario) {
		BaseValidacionExternaBean baseValidacionExternaBean = null;
		Util util=new Util();
		String estado="";
		String observacion="";
		
		String sql="SELECT NROTICKET,CODMATERIAL,SERIE,ESTADO,MOTIVO,USUARIO FROM BASEVALIDACIONEXTERNA WHERE LPAD(TRIM(CODMATERIAL),20,'0')='" + util.rellenar(material.trim(), 20) + "' AND LPAD(TRIM(SERIE),25,'0')='" + util.rellenar(serie.trim(),25) + "' AND NROTICKET='" + ticket + "' AND USUARIO='" + usuario + "'";
	
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				baseValidacionExternaBean = new BaseValidacionExternaBean();
				estado=rs.getString(4)==null?"":rs.getString(4);
				observacion=rs.getString(5)==null?"":rs.getString(5);
				baseValidacionExternaBean.setEstado(estado);
				baseValidacionExternaBean.setMotivo(observacion);
			}
		} catch (SQLException ex) {
			baseValidacionExternaBean = null;
            System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
	            System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		return baseValidacionExternaBean;

	}

	@Override
	public String obtenerNombreBodegaConfiguracion(String tipoBodega) {
		String nomTipoBodega="";
		String sql="SELECT NOMTIPOBODEGA FROM TIPOBODEGA WHERE CODTIPOBODEGA='" + tipoBodega + "'";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				nomTipoBodega=rs.getString(1);
			}else{
				nomTipoBodega=ConstantesGenerales.GUION;
			}
		} catch (SQLException ex) {
			nomTipoBodega=ConstantesGenerales.GUION;
            System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
	            System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		return nomTipoBodega;
	}


}
