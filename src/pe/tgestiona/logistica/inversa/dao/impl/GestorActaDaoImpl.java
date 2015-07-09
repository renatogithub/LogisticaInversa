package pe.tgestiona.logistica.inversa.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import pe.tgestiona.logistica.inversa.bean.GestorActaBean;
import pe.tgestiona.logistica.inversa.dao.GestorActaDao;

@Repository
public class GestorActaDaoImpl implements GestorActaDao{

	@Autowired
	private DriverManagerDataSource dataSource;

	@Override
	public void grabarGestorActa(GestorActaBean gestorActaBean) {
		String sql = "";
		Connection cn = null;
		PreparedStatement pstmt = null;
		try {
			cn = dataSource.getConnection();
			sql = "INSERT INTO GESTORACTA(CORREO,NOMGESTOR,TLF_ANEXO,RPM,CODENTIDAD) VALUES('"	+ 
					gestorActaBean.getCorreo() + "','" + gestorActaBean.getNombre() + "','" + 
					gestorActaBean.getTlfAnexo() + "','" + gestorActaBean.getRpm() + "','" + gestorActaBean.getEntidad() + "')";
			cn.setAutoCommit(false);
			pstmt = cn.prepareStatement(sql);
			pstmt.executeUpdate();
			cn.commit();
				
		} catch (SQLException ex) {
			System.out.println("Se ha presentado la siguiente Excepcion:" + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				System.out.println("Se ha presentado la siguiente Excepcion:" + e.getMessage());
			}
		}	
		
	}

	@Override
	public GestorActaBean obtenerDatos(String correo) {
		GestorActaBean gestorActaBean=null;
		String sql="SELECT CORREO,NOMGESTOR,TLF_ANEXO,RPM,CODENTIDAD FROM GESTORACTA WHERE CORREO='" + correo + "'";
		System.out.println(sql);
		Connection cn=null;
		try {
			cn=dataSource.getConnection();

			Statement st=cn.createStatement();
			ResultSet rs=st.executeQuery(sql);
		
			if(rs.next()){
				gestorActaBean=new GestorActaBean();
				gestorActaBean.setCorreo(rs.getString(1));
				gestorActaBean.setNombre(rs.getString(2));
				gestorActaBean.setTlfAnexo(rs.getString(3));
				gestorActaBean.setRpm(rs.getString(4));
				gestorActaBean.setEntidad(rs.getString(5));
			}
		} catch (SQLException e) {
			gestorActaBean=null;
			System.out.println("Se ha presentado la siguiente Excepcion:" + e.getMessage());
		} finally{
			try{
				cn.close();
			}catch(Exception e){
				System.out.println("Se ha presentado la siguiente Excepcion:" + e.getMessage());
			}
		}
		
		return gestorActaBean;
	}

	@Override
	public List<GestorActaBean> lstGestorEntidad(String codEntidad) {
		List<GestorActaBean> lstGestorActaEntidad=null;
		lstGestorActaEntidad=new ArrayList<GestorActaBean>();
		
		String sql="SELECT CORREO,NOMGESTOR,TLF_ANEXO,RPM,CODENTIDAD FROM GESTORACTA WHERE CODENTIDAD='" + codEntidad  + "'";
		
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			lstGestorActaEntidad = new ArrayList<GestorActaBean>();
			while (rs.next()) {
				GestorActaBean gestorActaBean=new GestorActaBean();
				gestorActaBean.setCorreo(rs.getString(1));
				gestorActaBean.setNombre(rs.getString(2));
				gestorActaBean.setTlfAnexo(rs.getString(3));
				gestorActaBean.setRpm(rs.getString(4));
				gestorActaBean.setEntidad(rs.getString(5));
				
				lstGestorActaEntidad.add(gestorActaBean);
			}
		} catch (SQLException ex) {
			lstGestorActaEntidad = null;
			System.out.println("Se ha presentado la siguiente Excepcion:" + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				System.out.println("Se ha presentado la siguiente Excepcion:" + e.getMessage());
			}
		}
			return lstGestorActaEntidad;
	}
	


}
