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

import pe.tgestiona.logistica.inversa.bean.TipoMaterialBean;
import pe.tgestiona.logistica.inversa.dao.TipoMaterialDao;

@Repository
public class TipoMaterialDaoImpl implements TipoMaterialDao{

	@Autowired
	private DriverManagerDataSource dataSource;
	
	@Override
	public List<TipoMaterialBean> lstTipoMaterial() {
		List<TipoMaterialBean> list = null;
		String sql = "SELECT CODTIPOMATERIAL,NOMTIPOMATERIAL FROM TIPOMATERIAL";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			list = new ArrayList<TipoMaterialBean>();
			while (rs.next()) {
				TipoMaterialBean tipoMaterialBean=new TipoMaterialBean();
				tipoMaterialBean.setCodTipoMaterial(rs.getString(1));
				tipoMaterialBean.setNomTipoMaterial(rs.getString(2));				
				list.add(tipoMaterialBean);
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
	public TipoMaterialBean obtenerDatos(String codTipoMaterial) {
		TipoMaterialBean tipoMaterialBean=null;
		String sql="SELECT CODTIPOMATERIAL,NOMTIPOMATERIAL FROM TIPOMATERIAL WHERE CODTIPOMATERIAL='" + codTipoMaterial + "'";
		
		Connection cn=null;
		try {
			cn=dataSource.getConnection();

			Statement st=cn.createStatement();
			ResultSet rs=st.executeQuery(sql);
		
			if(rs.next()){
				tipoMaterialBean=new TipoMaterialBean();
				tipoMaterialBean.setCodTipoMaterial(rs.getString(1));
				tipoMaterialBean.setNomTipoMaterial(rs.getString(2));
			}
		} catch (SQLException e) {
			tipoMaterialBean=null;
		} finally{
			try{
				cn.close();
			}catch(Exception e){
				
			}
		}
		
		return tipoMaterialBean;
	}


}
