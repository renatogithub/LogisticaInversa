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

import pe.tgestiona.logistica.inversa.bean.NegocioBean;
import pe.tgestiona.logistica.inversa.dao.NegocioDao;

@Repository
public class NegocioDaoImpl implements NegocioDao {

	@Autowired
	private DriverManagerDataSource dataSource;
	
	@Override
	public List<NegocioBean> listaNegocio() {
		List<NegocioBean> list = null;
		String sql = "SELECT CODNEGOCIO,NOMNEGOCIO FROM NEGOCIO";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			list = new ArrayList<NegocioBean>();
			while (rs.next()) {
				NegocioBean negocioBean = new NegocioBean();

				negocioBean.setCodNegocio(rs.getString(1));
				negocioBean.setNomNegocio(rs.getString(2));				
				list.add(negocioBean);
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
	public NegocioBean obtenerDatos(String codNegocio) {
		NegocioBean negocioBean=null;
		String sql="SELECT CODNEGOCIO,NOMNEGOCIO FROM NEGOCIO WHERE CODNEGOCIO='" + codNegocio + "'";
		
		Connection cn=null;
		try {
			cn=dataSource.getConnection();

			Statement st=cn.createStatement();
			ResultSet rs=st.executeQuery(sql);
		
			if(rs.next()){
				negocioBean=new NegocioBean();
				negocioBean.setCodNegocio(rs.getString(1));
				negocioBean.setNomNegocio(rs.getString(2));
			}
		} catch (SQLException e) {
			negocioBean=null;
		} finally{
			try{
				cn.close();
			}catch(Exception e){
				
			}
		}
		
		return negocioBean;
	}

}
