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

import pe.tgestiona.logistica.inversa.bean.CanalBean;
import pe.tgestiona.logistica.inversa.dao.CanalDao;

@Repository
public class CanalDaoImpl implements CanalDao{

	@Autowired
	private DriverManagerDataSource dataSource;
	
	@Override
	public List<CanalBean> listaCanales() {
		List<CanalBean> list = null;
		String sql = "SELECT CODCANAL,NOMCANAL FROM CANAL";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			list = new ArrayList<CanalBean>();
			while (rs.next()) {
				CanalBean canalBean = new CanalBean();

				canalBean.setCodCanal(rs.getString(1));
				canalBean.setNomCanal(rs.getString(2));
				
				list.add(canalBean);
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

}
