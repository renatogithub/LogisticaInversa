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

import pe.tgestiona.logistica.inversa.bean.TecnologiaMaterialBean;
import pe.tgestiona.logistica.inversa.dao.TecnologiaMaterialDao;

@Repository
public class TecnologiaMaterialDaoImpl implements TecnologiaMaterialDao{

	@Autowired
	private DriverManagerDataSource dataSource;
	
	@Override
	public List<TecnologiaMaterialBean> lstTecnologiaMaterial() {
		List<TecnologiaMaterialBean> list = null;
		String sql = "SELECT CODTECNOLOGIA,NOMTECNOLOGIA FROM TECNOLOGIA";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			list = new ArrayList<TecnologiaMaterialBean>();
			while (rs.next()) {
				TecnologiaMaterialBean tecnologiaMaterialBean = new TecnologiaMaterialBean();

				tecnologiaMaterialBean.setCodTecnologia(rs.getString(1));
				tecnologiaMaterialBean.setNomTecnologia(rs.getString(2));
				
				list.add(tecnologiaMaterialBean);
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
	public TecnologiaMaterialBean obtenerDatos(String codTecnologia) {
		TecnologiaMaterialBean tecnologiaMaterialBean=null;
		String sql="SELECT CODTECNOLOGIA,NOMTECNOLOGIA FROM TECNOLOGIA WHERE CODTECNOLOGIA='" + codTecnologia + "'";
		
		Connection cn=null;
		try {
			cn=dataSource.getConnection();

			Statement st=cn.createStatement();
			ResultSet rs=st.executeQuery(sql);
		
			if(rs.next()){
				tecnologiaMaterialBean=new TecnologiaMaterialBean();
				tecnologiaMaterialBean.setCodTecnologia(rs.getString(1));
				tecnologiaMaterialBean.setNomTecnologia(rs.getString(2));
			}
		} catch (SQLException e) {
			tecnologiaMaterialBean=null;
		} finally{
			try{
				cn.close();
			}catch(Exception e){
				
			}
		}
		
		return tecnologiaMaterialBean;
	}

}
