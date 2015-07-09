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

import pe.tgestiona.logistica.inversa.bean.TipoBodegaBean;
import pe.tgestiona.logistica.inversa.dao.TipoBodegaDao;

@Repository
public class TipoBodegaDaoImpl implements TipoBodegaDao{

	@Autowired
	private DriverManagerDataSource dataSource;
	
	@Override
	public List<TipoBodegaBean> lstTipoBodegas() {
		List<TipoBodegaBean> list = null;
		String sql = "select codtipobodega,initcap(nomtipobodega) from TipoBodega order by codtipobodega";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			list = new ArrayList<TipoBodegaBean>();
			while (rs.next()) {
				TipoBodegaBean tipoFormatoBean=new TipoBodegaBean();
				tipoFormatoBean.setCodTipoBodegas(rs.getString(1));
				tipoFormatoBean.setNomTipoBodegas(rs.getString(2));
				
				list.add(tipoFormatoBean);
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
