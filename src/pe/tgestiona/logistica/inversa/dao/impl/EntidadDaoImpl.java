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

import pe.tgestiona.logistica.inversa.bean.EntidadBean;
import pe.tgestiona.logistica.inversa.dao.EntidadDao;

@Repository
public class EntidadDaoImpl implements EntidadDao {

	@Autowired
	private DriverManagerDataSource dataSource;
	
	@Override
	public List<EntidadBean> listaEntidad(String canal) {
		List<EntidadBean> list = null;
		String sql = "SELECT CODENTIDAD,NOMENTIDAD FROM ENTIDAD WHERE CODCANAL='" + canal + "'";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			list = new ArrayList<EntidadBean>();
			while (rs.next()) {
				EntidadBean entidadBean = new EntidadBean();

				entidadBean.setCodEntidad(rs.getString(1));
				entidadBean.setNomEntidad(rs.getString(2));
								
				list.add(entidadBean);
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
	public EntidadBean obtenerDatos(String entidad) {
		EntidadBean entidadBean=null;
		String sql="SELECT CODENTIDAD,NOMENTIDAD,DIRECCION FROM ENTIDAD WHERE CODENTIDAD='" + entidad + "'";
		
		Connection cn=null;
		try {
			cn=dataSource.getConnection();

			Statement st=cn.createStatement();
			ResultSet rs=st.executeQuery(sql);
		
			if(rs.next()){
				entidadBean=new EntidadBean();
				entidadBean.setCodEntidad(rs.getString(1));
				entidadBean.setNomEntidad(rs.getString(2));
				entidadBean.setDireccion(rs.getString(3));	
			}
		} catch (SQLException e) {
			entidadBean=null;
		} finally{
			try{
				cn.close();
			}catch(Exception e){
				
			}
		}
		
		return entidadBean;
	}

}
