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

import pe.tgestiona.logistica.inversa.bean.ContactoBean;
import pe.tgestiona.logistica.inversa.dao.ContactoDao;

@Repository
public class ContactoDaoImpl implements ContactoDao{

	@Autowired
	private DriverManagerDataSource dataSource;
	
	@Override
	public void grabarContacto(ContactoBean contactoBean) {
		String sql = "";
		Connection cn = null;
		PreparedStatement pstmt = null;
		try {
			cn = dataSource.getConnection();
				sql = "INSERT INTO CONTACTO(CORREO,NOMCONTACTO,TLF_ANEXO,RPM,CODENTIDAD) VALUES('"	+ 
						contactoBean.getCorreo() + "','" + contactoBean.getNombre() + "','" + 
						contactoBean.getTlfAnexo() + "','" + contactoBean.getRpm() + "','" + contactoBean.getEntidad() + "')";
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
	public ContactoBean obtenerDatos(String correo) {
		ContactoBean contactoBean=null;
		String sql="SELECT CORREO,NOMCONTACTO,TLF_ANEXO,RPM,CODENTIDAD FROM CONTACTO WHERE CORREO='" + correo + "'";
		System.out.println(sql);
		Connection cn=null;
		try {
			cn=dataSource.getConnection();

			Statement st=cn.createStatement();
			ResultSet rs=st.executeQuery(sql);
		
			if(rs.next()){
				contactoBean=new ContactoBean();
				contactoBean.setCorreo(rs.getString(1));
				contactoBean.setNombre(rs.getString(2));
				contactoBean.setTlfAnexo(rs.getString(3));
				contactoBean.setRpm(rs.getString(4));
				contactoBean.setEntidad(rs.getString(5));
			}
		} catch (SQLException e) {
			contactoBean=null;
			System.out.println("Se ha presentado la siguiente Excepcion:" + e.getMessage());
		} finally{
			try{
				cn.close();
			}catch(Exception e){
				System.out.println("Se ha presentado la siguiente Excepcion:" + e.getMessage());
			}
		}
		
		return contactoBean;
	}

	@Override
	public List<ContactoBean> lstContactoEntidad(String codEntidad) {
		List<ContactoBean> lstContactoEntidad=null;
		lstContactoEntidad=new ArrayList<ContactoBean>();
		
		String sql="SELECT CORREO,NOMCONTACTO,TLF_ANEXO,RPM,CODENTIDAD FROM CONTACTO WHERE CODENTIDAD='" + codEntidad  + "'";
		
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			lstContactoEntidad = new ArrayList<ContactoBean>();
			while (rs.next()) {
				ContactoBean contactoBean=new ContactoBean();
				contactoBean.setCorreo(rs.getString(1));
				contactoBean.setNombre(rs.getString(2));
				contactoBean.setTlfAnexo(rs.getString(3));
				contactoBean.setRpm(rs.getString(4));
				contactoBean.setEntidad(rs.getString(5));
				
				lstContactoEntidad.add(contactoBean);
			}
		} catch (SQLException ex) {
			lstContactoEntidad = null;
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
			}
		}
			return lstContactoEntidad;
	}

}
