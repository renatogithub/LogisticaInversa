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

import pe.tgestiona.logistica.inversa.bean.ProvisionPlantaExternaBean;
import pe.tgestiona.logistica.inversa.dao.ProvisionPlantaExternaDao;

@Repository
public class ProvisionPlantaExternaDaoImpl implements ProvisionPlantaExternaDao{
	
	@Autowired
	private DriverManagerDataSource dataSource;

	@Override
	public List<ProvisionPlantaExternaBean> listaProvision() {
		List<ProvisionPlantaExternaBean> list = null;
		String sql = "SELECT CODPROV,NOMPROV FROM PROVISION_PLANTAEXTERNA";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			list = new ArrayList<ProvisionPlantaExternaBean>();
			while (rs.next()) {
				ProvisionPlantaExternaBean provisionPlantaExternaBean = new ProvisionPlantaExternaBean();

				provisionPlantaExternaBean.setCodProvisionPlantaExterna(rs.getString(1));
				provisionPlantaExternaBean.setNomProvisionPlantaExterna(rs.getString(2));				
				list.add(provisionPlantaExternaBean);
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
	public ProvisionPlantaExternaBean obtenerDatos(String codProvision) {
		ProvisionPlantaExternaBean provisionPlantaExternaBean=null;
		String sql="SELECT CODPROV,NOMPROV FROM PROVISION_PLANTAEXTERNA WHERE CODPROV='" + codProvision + "'";
		
		Connection cn=null;
		try {
			cn=dataSource.getConnection();

			Statement st=cn.createStatement();
			ResultSet rs=st.executeQuery(sql);
		
			if(rs.next()){
				provisionPlantaExternaBean=new ProvisionPlantaExternaBean();
				provisionPlantaExternaBean.setCodProvisionPlantaExterna(rs.getString(1));
				provisionPlantaExternaBean.setNomProvisionPlantaExterna(rs.getString(2));
			}
		} catch (SQLException e) {
			provisionPlantaExternaBean=null;
		} finally{
			try{
				cn.close();
			}catch(Exception e){
				
			}
		}
		
		return provisionPlantaExternaBean;
	}

}
