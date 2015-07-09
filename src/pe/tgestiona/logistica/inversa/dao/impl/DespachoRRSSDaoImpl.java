package pe.tgestiona.logistica.inversa.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import pe.tgestiona.logistica.inversa.bean.DespachoRRSSBean;
import pe.tgestiona.logistica.inversa.dao.DespachoRRSSDao;
import pe.tgestiona.logistica.inversa.util.Util;

@Repository
public class DespachoRRSSDaoImpl implements DespachoRRSSDao {

	@Autowired
	private DriverManagerDataSource dataSource;
	
	@Override
	public List<DespachoRRSSBean> lstDespachoRRSS(String opcion,String material,String serie,int pag) {
		List<DespachoRRSSBean> lstDespachoRRSS = null;
		Util util=new Util();
		if(material==null){
			material="";
		}

		if(serie==null){
			serie="";
		}
		
		int pagInicio=0;
		int pagActual=0;
		int pagFin=0;		
		int regMostrar=20;

		
		if(pag>=1){
			pagActual=pag;
			pagInicio=(pag-1)*regMostrar;
			pagFin=pag*regMostrar;
		}else{
			pagInicio=0;
			pagFin=regMostrar;
			pagActual=1;
		}
		
		
		String sql="SELECT * FROM(SELECT A.*,ROWNUM RNUM FROM (SELECT RRSS.ANIO,RRSS.SERIE,RRSS.MATERIAL,RRSS.DESCRIPCION,RRSS.DESTINO,RRSS.TIPO,RRSS.GEMISION,RRSS.FENVIO,RRSS.OBSERVACION FROM DESPACHORRSS RRSS ";
				
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			
			lstDespachoRRSS = new ArrayList<DespachoRRSSBean>();

			if(opcion.equals("serie")){
				sql=sql + " WHERE LPAD(UPPER(TRIM(SERIE)),25,'0')='" + util.rellenar(serie.trim().toUpperCase(), 25) + "')";
			}

			if(opcion.equals("material")){
				sql=sql + " WHERE LPAD(UPPER(TRIM(MATERIAL)),20,'0')='" + util.rellenar(material.trim().toUpperCase(), 20) + "')";
			}

			if(opcion.equals("material_serie")){
				sql=sql + " WHERE LPAD(UPPER(TRIM(SERIE)),25,'0')='" + util.rellenar(serie.trim().toUpperCase(), 25) + "' AND LPAD(UPPER(TRIM(MATERIAL)),20,'0')='" + util.rellenar(material.trim().toUpperCase(), 20) + "')";
			}
			
			sql=sql + " A WHERE ROWNUM<=" + pagFin + ") WHERE RNUM>" + pagInicio;
			
			ResultSet rs = st.executeQuery(sql);
			System.out.println(sql);
			
			while (rs.next()) {
				DespachoRRSSBean despachoRRSSBean = new DespachoRRSSBean();
				
				int cantReg=cantRegistros(opcion, material, serie);
				despachoRRSSBean.setAnio(rs.getString(1));
				despachoRRSSBean.setSerie(rs.getString(2));
				despachoRRSSBean.setMaterial(rs.getString(3));
				despachoRRSSBean.setDescripcion(rs.getString(4));
				despachoRRSSBean.setDestino(rs.getString(5));
				despachoRRSSBean.setTipo(rs.getString(6));
				despachoRRSSBean.setgEmision(rs.getString(7));
				despachoRRSSBean.setfEnvio(rs.getString(8));
				despachoRRSSBean.setObservacion(rs.getString(9));
				despachoRRSSBean.setCantReg(cantReg);
				
				lstDespachoRRSS.add(despachoRRSSBean);
			}
		} catch (SQLException ex) {
			lstDespachoRRSS = null;
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
			}
		}
		return lstDespachoRRSS;
	}

	@Override
	public void guardarDespachoRRSS(DespachoRRSSBean despachoRRSSBean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int cantRegistros(String opcion, String material, String serie) {
		String sql="SELECT * FROM(SELECT A.*,ROWNUM RNUM FROM (SELECT RRSS.ANIO,RRSS.SERIE,RRSS.MATERIAL,RRSS.DESCRIPCION,RRSS.DESTINO,RRSS.TIPO,RRSS.GEMISION,RRSS.FENVIO,RRSS.OBSERVACION FROM DESPACHORRSS RRSS ";
		Connection cn = null;
		Util util=new Util();
		int cantReg=0;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();

			if(opcion.equals("serie")){
				sql=sql + " WHERE LPAD(UPPER(TRIM(SERIE)),25,'0')='" + util.rellenar(serie.trim().toUpperCase(), 25) + "')";
			}

			if(opcion.equals("material")){
				sql=sql + " WHERE LPAD(UPPER(TRIM(MATERIAL)),20,'0')='" + util.rellenar(material.trim().toUpperCase(), 20) + "')";
			}

			if(opcion.equals("material_serie")){
				sql=sql + " WHERE LPAD(UPPER(TRIM(SERIE)),25,'0')='" + util.rellenar(serie.trim().toUpperCase(), 25) + "' AND LPAD(UPPER(TRIM(MATERIAL)),20,'0')='" + util.rellenar(material.trim().toUpperCase(), 20) + "')";
			}
			
			sql=sql + " A )";
			
			ResultSet rs = st.executeQuery(sql);
			System.out.println(sql);
			
			while (rs.next()) {
				cantReg++;
			}
		} catch (SQLException ex) {
			cantReg = 0;
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
			}
		}
		
		return cantReg;
	}

/*	@Override
	public List<String> listaPaginas(String opcion,String material,String serie,int pag) {
		List<String> lstPaginacion = null;

		Util util=new Util();
		if(material==null){
			material="";
		}

		if(serie==null){
			serie="";
		}
		
		int cantReg=0;
		
		String sql="SELECT MATERIAL,SERIE,ANIO,DESCRIPCION,DESTINO,TIPO,GEMISION,FENVIO,OBSERVACION FROM (SELECT rownum r, rrss.* from DESPACHORRSS rrss ";
		
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();

			lstPaginacion=new ArrayList<String>();			

			if(opcion.equals("serie")){
				sql=sql + " WHERE LPAD(TRIM(SERIE),25,'0')='" + util.rellenar(serie.trim(), 25) + "')";
			}

			if(opcion.equals("material")){
				sql=sql + " WHERE LPAD(TRIM(MATERIAL),20,'0')='" + util.rellenar(material.trim(), 20) + "')";
			}

			if(opcion.equals("material_serie")){
				sql=sql + " WHERE LPAD(TRIM(SERIE),25,'0')='" + util.rellenar(serie.trim(), 25) + "' AND LPAD(TRIM(MATERIAL),20,'0')='" + util.rellenar(material.trim(), 20) + "')";
			}
			
			
			ResultSet rs = st.executeQuery(sql);
			System.out.println(sql);
			
			while (rs.next()) {
				cantReg++;
			}
		} catch (SQLException ex) {
			cantReg = 0;
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
			}
		}
		return cantReg;
	}*/

}
