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

import pe.tgestiona.logistica.inversa.bean.SociedadMaterialBean;
import pe.tgestiona.logistica.inversa.dao.SociedadMaterialDao;

@Repository
public class SociedadMaterialDaoImpl implements SociedadMaterialDao{

	@Autowired
	private DriverManagerDataSource dataSource;

	@Override
	public List<SociedadMaterialBean> listasociedad() {
		List<SociedadMaterialBean> list = null;
		String sql = "SELECT CODSOCIEDAD,ABRSOCIEDAD,NOMSOCIEDAD FROM SOCIEDAD";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			list = new ArrayList<SociedadMaterialBean>();
			while (rs.next()) {
				SociedadMaterialBean sociedadMaterialBean=new SociedadMaterialBean();
				sociedadMaterialBean.setCodSociedad(rs.getString(1));
				sociedadMaterialBean.setAbrvSociedad(rs.getString(2));
				sociedadMaterialBean.setNomSociedad(rs.getString(3));
				list.add(sociedadMaterialBean);
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
	public SociedadMaterialBean obtenerDatos(String codSociedadMaterial) {
		SociedadMaterialBean sociedadMaterialBean=null;
		String sql="SELECT CODSOCIEDAD,NOMSOCIEDAD FROM SOCIEDAD WHERE CODSOCIEDAD='" + codSociedadMaterial + "'";
		
		Connection cn=null;
		try {
			cn=dataSource.getConnection();

			Statement st=cn.createStatement();
			ResultSet rs=st.executeQuery(sql);
		
			if(rs.next()){
				sociedadMaterialBean=new SociedadMaterialBean();
				sociedadMaterialBean.setCodSociedad(rs.getString(1));
				sociedadMaterialBean.setNomSociedad(rs.getString(2));
			}
		} catch (SQLException e) {
			sociedadMaterialBean=null;
		} finally{
			try{
				cn.close();
			}catch(Exception e){
				
			}
		}
		
		return sociedadMaterialBean;
	}
}
