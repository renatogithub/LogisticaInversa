package pe.tgestiona.logistica.inversa.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import pe.tgestiona.logistica.inversa.bean.ConfiguracionBean;
import pe.tgestiona.logistica.inversa.dao.ConfiguracionDao;

@Repository
public class ConfiguracionDaoImpl implements ConfiguracionDao{


	@Autowired
	private DriverManagerDataSource dataSource;
	
	
	@Override
	public List<ConfiguracionBean> listaConfiguracion(String criterio) {
		List<ConfiguracionBean> list = null;
		String sql = "SELECT CRITERIO,CODCONF,VALOR,PARAMETRO,EXISTENCIA FROM DETALLECONFIGURACION WHERE CRITERIO='" + criterio + "'";
		System.out.println(sql);
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			list = new ArrayList<ConfiguracionBean>();
			while (rs.next()) {
				ConfiguracionBean configuracionBean = new ConfiguracionBean();

				configuracionBean.setCodCriterio(rs.getString(1));
				configuracionBean.setCodConfiguracion(rs.getString(2));
				configuracionBean.setValor(rs.getString(3));
				configuracionBean.setParametro(rs.getString(4));
				configuracionBean.setExistencia(rs.getString(5));
				list.add(configuracionBean);
			}
		} catch (SQLException ex) {
			list = null;
            System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
	            System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		return list;
	}

	@Override
	public List<String> listaTipoBodegas() {
		List<String> listaTipoBodegas=null;
		String sql = null;
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = null;
			listaTipoBodegas = new ArrayList<String>();

			sql="select distinct tipobodega from bodegas";
			rs=st.executeQuery(sql);
			while(rs.next()){
				listaTipoBodegas.add(rs.getString(1));
			}
			
		} catch (SQLException ex) {
			listaTipoBodegas = null;
            System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
	            System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		return listaTipoBodegas;
	}

	@Override
	public void guardarConfiguracion(String criterio, String configuracion, String valor,String parametro,String existencia) {
		String sql=null;
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			cn.setAutoCommit(false);
	        PreparedStatement pstmt = null;
			sql="INSERT INTO DETALLECONFIGURACION(CRITERIO,CODCONF,VALOR,PARAMETRO,EXISTENCIA) values ('" + criterio + "','" + configuracion + "','" + valor + "','" + parametro + "','" + existencia + "')";			
			pstmt = cn.prepareStatement(sql);
            pstmt.executeUpdate();
            cn.commit();
		} catch (SQLException ex) {
			System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
	}

	@Override
	public void eliminarConfiguracion(String criterio,String configuracion) {
		String sql=null;
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			cn.setAutoCommit(false);
	        PreparedStatement pstmt = null;
			sql="DELETE FROM DETALLECONFIGURACION WHERE CRITERIO='" + criterio + "' AND CODCONF='" + configuracion + "'";
			pstmt = cn.prepareStatement(sql);
            pstmt.executeUpdate();
            cn.commit();			
		} catch (SQLException ex) {
			System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
	}

	@Override
	public String obtenerHoraCierre(String criterio) {
		String horaCierre = null;
		String sql = "SELECT VALOR FROM DETALLECONFIGURACION WHERE CRITERIO='" + criterio + "' AND CODCONF='CONF07'";
		System.out.println(sql);
		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				horaCierre=rs.getString(1);
			}
		} catch (SQLException ex) {
			horaCierre = null;
			System.out.println("Se suscito la siguiente excepcion " + ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
			}
		}
		return horaCierre;
	}

}


