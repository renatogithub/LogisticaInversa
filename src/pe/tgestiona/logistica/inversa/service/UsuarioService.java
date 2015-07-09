package pe.tgestiona.logistica.inversa.service;

import pe.tgestiona.logistica.inversa.bean.UsuarioBean;

public interface UsuarioService {
	public UsuarioBean iniciarSesion(String usu,String pass,String ip);
	public void cerrarSesion(String cuenta);
}
