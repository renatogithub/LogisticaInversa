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

import pe.tgestiona.logistica.inversa.bean.RubroMaterialBean;
import pe.tgestiona.logistica.inversa.dao.RubroMaterialDao;

@Repository
public class RubroMaterialDaoImpl implements RubroMaterialDao{

	@Autowired
	private DriverManagerDataSource dataSource;

	@Override
	public List<RubroMaterialBean> lstRubroMaterial() {
		List<RubroMaterialBean> list = null;
		String sql = "SELECT CODRUBRO,NOMBRERUBRO,ABRRUBRO FROM RUBRO";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			list = new ArrayList<RubroMaterialBean>();
			while (rs.next()) {
				RubroMaterialBean rubroMaterialBean=new RubroMaterialBean();
				rubroMaterialBean.setCodRubro(rs.getString(1));
				rubroMaterialBean.setNomRubro(rs.getString(2));
				rubroMaterialBean.setAbrRubro(rs.getString(3));
				list.add(rubroMaterialBean);
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
	public RubroMaterialBean obtenerDatos(String codRubro) {
		RubroMaterialBean rubroMaterialBean=null;
		String sql="SELECT CODRUBRO,NOMBRERUBRO,ABRRUBRO FROM RUBRO WHERE CODRUBRO='" + codRubro + "'";
		
		Connection cn=null;
		try {
			cn=dataSource.getConnection();

			Statement st=cn.createStatement();
			ResultSet rs=st.executeQuery(sql);
		
			if(rs.next()){
				rubroMaterialBean=new RubroMaterialBean();
				rubroMaterialBean.setCodRubro(rs.getString(1));
				rubroMaterialBean.setNomRubro(rs.getString(2));
				rubroMaterialBean.setAbrRubro(rs.getString(3));
			}
		} catch (SQLException e) {
			rubroMaterialBean=null;
		} finally{
			try{
				cn.close();
			}catch(Exception e){
				
			}
		}
		
		return rubroMaterialBean;
	}
}
