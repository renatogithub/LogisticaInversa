package pe.tgestiona.logistica.inversa.dao.impl;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import pe.tgestiona.logistica.inversa.bean.BaseGarantiaBean;
import pe.tgestiona.logistica.inversa.bean.ConstantesGenerales;
import pe.tgestiona.logistica.inversa.dao.BaseGarantiaDao;

@Repository
public class BaseGarantiaDaoImpl implements BaseGarantiaDao{

	@Autowired
	private DriverManagerDataSource dataSource;
	
	@Override
	public List<BaseGarantiaBean> leerarchivoExcelValidarPendientes(InputStream file) {
		List<BaseGarantiaBean> listGarantiaPendientes = null;
	
		try {
			Workbook workbook = Workbook.getWorkbook(file); //Pasamos el excel que vamos a leer
			Sheet sheet = workbook.getSheet(0); //Seleccionamos la hoja que vamos a leer
			String dato;
			listGarantiaPendientes = new ArrayList<BaseGarantiaBean>();
			
			for (int fila = 1; fila < sheet.getRows(); fila++) { //recorremos las filas
				BaseGarantiaBean baseGarantiaBean=new BaseGarantiaBean();
				for (int columna = 0; columna < sheet.getColumns(); columna++) { //recorremos las columnas
					
					dato = sheet.getCell(columna, fila).getContents(); //setear la celda leida a nombre
					if(dato==null){
						dato="";
					}
					switch(columna){
						case 0:	baseGarantiaBean.setNroTicket(dato);
				    			break;
					    case 1:	baseGarantiaBean.setfProceso(dato);
					    		break;
					    case 2: baseGarantiaBean.setEntidad(dato);
					    		break;
						case 3: baseGarantiaBean.setCodMaterialSAP(dato);
								break;								
						case 4: baseGarantiaBean.setDescripcionSAP(dato);		
								break;
						case 5: baseGarantiaBean.setSerie(dato);
								break;
						case 6: baseGarantiaBean.setCentro(dato);
								break;
						case 7: baseGarantiaBean.setAlmacen(dato);
								break; 
						case 8: baseGarantiaBean.setLote(dato);
								break; 		
						case 9: baseGarantiaBean.setfSolicitud(dato);
								break;
						case 10: baseGarantiaBean.setfAprobacion(dato);
								break;
						case 11: baseGarantiaBean.setMesLiquidacion(dato);
								break;
						case 12: baseGarantiaBean.setfIngresoSAP(dato);
								break;
						case 13: baseGarantiaBean.setfRemozado(dato);
								break;
						case 14: baseGarantiaBean.setfDespachoSAP(dato);
								break;
						case 15: baseGarantiaBean.setObsAprobado(dato);
								break;
						case 16: baseGarantiaBean.setMotivoRechazo(dato);
								break;	
						case 17: baseGarantiaBean.setObservacion(dato);
								break;			

					}
					
				}
				listGarantiaPendientes.add(baseGarantiaBean);
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Se suscito la siguiente Excepcion: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Se suscito la siguiente Excepcion: " + e.getMessage());
		} catch (BiffException e) {
			System.out.println("Se suscito la siguiente Excepcion: " + e.getMessage());
		}
			
		return listGarantiaPendientes;
	}

	@Override
	public void grabarBaseGarantia(List<BaseGarantiaBean> lstBaseGarantia,String usuario) {
		String sql = "";
		Connection cn = null;
		PreparedStatement pstmt = null;
		try {
			cn = dataSource.getConnection();
			for (int i = 0; i < lstBaseGarantia.size(); i++) {
				if(lstBaseGarantia.get(i).getObservacion().equals(ConstantesGenerales.BaseGarantia.RESPONSABILIDAD_PUNTO.getTipoValor()) ||
				   lstBaseGarantia.get(i).getObservacion().equals(ConstantesGenerales.BaseGarantia.RESPONSABILIDAD_TELEFONICA.getTipoValor())){
					sql="INSERT INTO BASEGARANTIA(NroTicket,FSolicitud,Entidad,CodMaterial,DesMaterial,Serie,Centro,Almacen,Lote,FCorreo,FAprobacion,MesLiquidacion,FIngresoSAP,FRemozado,FDespacho,ObsAprobado,ObsRechazado,Observacion,USUARIO) VALUES('" + 
							lstBaseGarantia.get(i).getNroTicket() + "','" + lstBaseGarantia.get(i).getfSolicitud() + "','" + lstBaseGarantia.get(i).getEntidad() + "','" + lstBaseGarantia.get(i).getCodMaterialSAP() + "','"	 + lstBaseGarantia.get(i).getDescripcionSAP() + "','" + 
							lstBaseGarantia.get(i).getSerie() + "','" + lstBaseGarantia.get(i).getCentro() + "','" + lstBaseGarantia.get(i).getAlmacen() + "','" + lstBaseGarantia.get(i).getLote() + "','" + lstBaseGarantia.get(i).getfSolicitud() + "','" + 
							lstBaseGarantia.get(i).getfAprobacion() + "','" + lstBaseGarantia.get(i).getMesLiquidacion() + "','" + lstBaseGarantia.get(i).getfIngresoSAP() + "','" + lstBaseGarantia.get(i).getfRemozado() + "','" + lstBaseGarantia.get(i).getfDespachoSAP() + "','" + 
							lstBaseGarantia.get(i).getObsAprobado() + "','" + lstBaseGarantia.get(i).getMotivoRechazo() + "','" + lstBaseGarantia.get(i).getObservacion() + "','" + usuario + "')";
						 
					cn.setAutoCommit(false);
					pstmt = cn.prepareStatement(sql);
					pstmt.executeUpdate();
					cn.commit();
				}
			}
		} catch (SQLException ex) {
			System.out.println("Se suscito la siguiente Excepcion: " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				System.out.println("Se suscito la siguiente Excepcion: " + e.getMessage());
			}
		}
	}

	@Override
	public void eliminaDatosBaseGarantia(String usuario) {
		String sql="DELETE FROM BASEGARANTIA WHERE USUARIO='" + usuario + "'";
		Connection cn = null;
		PreparedStatement pstmt = null;
		try {
			cn=dataSource.getConnection();
			cn.setAutoCommit(false);
			pstmt = cn.prepareStatement(sql);
			pstmt.executeUpdate();
			cn.commit();
			
		} catch (SQLException e) {
			System.out.println("Se suscito la siguiente Excepcion: " + e.getMessage());
		} finally{
			try{
				cn.close();
			}catch(Exception e){
				System.out.println("Se suscito la siguiente Excepcion: " + e.getMessage());
			}
		}
		
	}

}
