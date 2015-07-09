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

import pe.tgestiona.logistica.inversa.bean.MotivoDevolucionBean;
import pe.tgestiona.logistica.inversa.dao.MotivoDevolucionDao;

@Repository
public class MotivoDevolucionDaoImpl implements MotivoDevolucionDao {

	@Autowired
	private DriverManagerDataSource dataSource;
	
	@Override
	public List<MotivoDevolucionBean> listaMotivos(String formato) {
		List<MotivoDevolucionBean> list = null;
		String sql = "SELECT CODTIPODEVOL,initcap(NOMTIPODEVOL) FROM TIPODEVOLUCION WHERE CODFORM='" + formato + "'";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			list = new ArrayList<MotivoDevolucionBean>();
			while (rs.next()) {
				MotivoDevolucionBean motivoDevolucionBean = new MotivoDevolucionBean();
				motivoDevolucionBean.setCodDevol(rs.getString(1));
				motivoDevolucionBean.setNomDevol(rs.getString(2));				
				list.add(motivoDevolucionBean);
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
	public MotivoDevolucionBean obtenerMotivo(String codigo) {
		String sql = "SELECT CODTIPODEVOL,NOMTIPODEVOL,ABRTIPODEVOL FROM TIPODEVOLUCION WHERE CODTIPODEVOL='"+codigo + "'";
		System.out.println(sql);
		MotivoDevolucionBean motivoDevolucionBean=null;
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				motivoDevolucionBean=new MotivoDevolucionBean();
				motivoDevolucionBean.setCodDevol(rs.getString(1));
				motivoDevolucionBean.setNomDevol(rs.getString(2));
				motivoDevolucionBean.setAbrvMotivo(rs.getString(3));
			}
		} catch (SQLException ex) {
			motivoDevolucionBean = null;
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
			}
		}
		return motivoDevolucionBean;
	}

	@Override
	public List<MotivoDevolucionBean> listaMotivos() {
		List<MotivoDevolucionBean> list = null;
		String sql="SELECT TPDEVOL.CODTIPODEVOL,INITCAP(TPDEVOL.NOMTIPODEVOL),TPFRT.NOMFORM,TPFRT.CODFORM FROM TIPODEVOLUCION TPDEVOL,TIPO_FORMATO TPFRT " + 
					" WHERE TPDEVOL.CODFORM=TPFRT.CODFORM";
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			list = new ArrayList<MotivoDevolucionBean>();
			while (rs.next()) {
				MotivoDevolucionBean motivoDevolucionBean = new MotivoDevolucionBean();
				motivoDevolucionBean.setCodDevol(rs.getString(1));
				motivoDevolucionBean.setNomDevol(rs.getString(2));	
				motivoDevolucionBean.setTipoFormato(rs.getString(3));
				motivoDevolucionBean.setCodTipoFormato(rs.getString(4));
				list.add(motivoDevolucionBean);
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
