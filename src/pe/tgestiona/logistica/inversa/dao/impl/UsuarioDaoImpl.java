package pe.tgestiona.logistica.inversa.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import pe.tgestiona.logistica.inversa.bean.UsuarioBean;
import pe.tgestiona.logistica.inversa.dao.UsuarioDao;

@Repository
public class UsuarioDaoImpl implements UsuarioDao{

	@Autowired
	private DriverManagerDataSource dataSource;
	
	@Override
	public UsuarioBean iniciarSesion(String usu, String pass,String ip) {
		UsuarioBean usuarioBean=null;
		
		String sentenciaSQL1="SELECT P.CODPERFIL,u.codusuario,u.apellidos,u.nombres,p.NOMPERFIL,u.ip,u.estado,u.password FROM USUARIO u, PERFIL p " + 		
		"WHERE P.CODPERFIL=U.CODPERFIL AND UPPER(u.codusuario)='" + usu.toUpperCase() + "' and UPPER(u.password)='" + pass.toUpperCase() + "'";
		
		Connection cn=null;
		System.out.println(sentenciaSQL1);
		
		try {
			cn=dataSource.getConnection();

			Statement st=cn.createStatement();
			ResultSet rs=st.executeQuery(sentenciaSQL1);
		
			if(rs.next()){
				usuarioBean=new UsuarioBean();
				usuarioBean.setPerfil(rs.getString(1));
				usuarioBean.setUsuario(rs.getString(2));
				usuarioBean.setApellidos(rs.getString(3));
				usuarioBean.setNombres(rs.getString(4));
				usuarioBean.setArea(rs.getString(5));
				usuarioBean.setIp(rs.getString(6));
				usuarioBean.setEstado(rs.getString(7));	
				usuarioBean.setPassword(rs.getString(8));	
			}
		} catch (SQLException e) {
			usuarioBean=null;
			System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
		} finally{
			try{
				cn.close();
			}catch(Exception e){
				
			}
		}
		
		 if(usuarioBean==null)
			 return null;
		 
		 String sentenciaSQL2 = "";        
	     sentenciaSQL2 = "update USUARIO set ip=?,estado='1' WHERE CODUSUARIO='"+usuarioBean.getUsuario()+"' AND PASSWORD='"+usuarioBean.getPassword()+"'";
	     System.out.println("ingresoUsuario : "+sentenciaSQL2); 
	     
	     try {
	    	 cn=dataSource.getConnection();
	    	 cn.setAutoCommit(false);
	    	 PreparedStatement pstmt = null;
	         pstmt = cn.prepareStatement(sentenciaSQL2);
	         pstmt.setString(1, ip);
	         usuarioBean.setEstado("1");
	         pstmt.executeUpdate();
	         cn.commit();
	        } catch (SQLException e) {
	            System.out.println("Se suscito la siguiente excepcion " +
	                               e.getMessage());
	            try {
	                cn.rollback();
	            } catch (SQLException e1) {
	                // TODO Auto-generated catch block
	                System.out.println("Se suscito la siguiente excepcion " +
	                                   e1.getMessage());
	            }
	            } finally {
	            	try {
						cn.close();
					} catch (SQLException e) {
						System.out.println("Se suscito la siguiente excepcion" + e.getMessage());
					}
	            }
		
		return usuarioBean;
	}

	@Override
	public void cerrarSesion(String cuenta) {
		UsuarioBean usuarioBean=null;
		String sentenciaSQL1="SELECT U.CODUSUARIO,U.PASSWORD FROM USUARIO U WHERE UPPER(u.codusuario)='" + cuenta.toUpperCase() + "'";
		
		Connection cn=null;
		System.out.println(sentenciaSQL1);
		
		try {
			cn=dataSource.getConnection();

			Statement st=cn.createStatement();
			ResultSet rs=st.executeQuery(sentenciaSQL1);
		
			if(rs.next()){
				usuarioBean=new UsuarioBean();
				usuarioBean.setUsuario(rs.getString(1));
				usuarioBean.setPassword(rs.getString(2));
			}
		} catch (SQLException e) {
			usuarioBean=null;
			System.out.println("Se suscito la siguiente excepcion " + e.getMessage());
		} finally{
			try{
				cn.close();
			}catch(Exception e){
				
			}
		}

		 String sentenciaSQL2 = "";        
	     sentenciaSQL2 = "update USUARIO set ip='',estado='0' WHERE CODUSUARIO='"+usuarioBean.getUsuario()+"' AND PASSWORD='"+usuarioBean.getPassword()+"'";
	     System.out.println("ingresoUsuario : "+sentenciaSQL2); 
	     
	     try {
	    	 cn=dataSource.getConnection();
	    	 Statement st=cn.createStatement();
	         int ctos=st.executeUpdate(sentenciaSQL2);
	         if(ctos==0){
	        	 System.out.println("No ha hecho efecto a ninguna fila");
	         }
	         
	        } catch (SQLException e) {
	            System.out.println("Se suscito la siguiente excepcion " +
	                               e.getMessage());
	            try {
	                cn.rollback();
	            } catch (SQLException e1) {
	                System.out.println("Se suscito la siguiente excepcion " +
	                                   e1.getMessage());
	            }
	            } finally {
	            	try {
						cn.close();
					} catch (SQLException e) {
						System.out.println("Se suscito la siguiente excepcion" + e.getMessage());
					}
	            }
		
}
}
