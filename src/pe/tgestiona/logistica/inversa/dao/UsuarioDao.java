package pe.tgestiona.logistica.inversa.dao;

import pe.tgestiona.logistica.inversa.bean.UsuarioBean;

public interface UsuarioDao {
	public UsuarioBean iniciarSesion(String usu,String pass,String ip);
	public void cerrarSesion(String cuenta);
}
