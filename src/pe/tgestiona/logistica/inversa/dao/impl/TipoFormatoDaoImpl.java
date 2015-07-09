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

import pe.tgestiona.logistica.inversa.bean.TipoFormatoBean;
import pe.tgestiona.logistica.inversa.dao.TipoFormatoDao;

@Repository
public class TipoFormatoDaoImpl implements TipoFormatoDao{

	@Autowired
	private DriverManagerDataSource dataSource;
	
	@Override
	public List<TipoFormatoBean> listaFormato() {
		List<TipoFormatoBean> list = null;
		String sql = "SELECT CODFORM,INITCAP(NOMFORM) FROM TIPO_FORMATO";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			list = new ArrayList<TipoFormatoBean>();
			while (rs.next()) {
				TipoFormatoBean tipoFormatoBean=new TipoFormatoBean();
				tipoFormatoBean.setCodTipoFormato(rs.getString(1));
				tipoFormatoBean.setNomTipoFormato(rs.getString(2));
				
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
