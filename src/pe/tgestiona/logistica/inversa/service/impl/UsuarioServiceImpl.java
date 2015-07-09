package pe.tgestiona.logistica.inversa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.UsuarioBean;
import pe.tgestiona.logistica.inversa.dao.UsuarioDao;
import pe.tgestiona.logistica.inversa.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	@Autowired
	private UsuarioDao usuarioDao;
	
	
	@Override
	public UsuarioBean iniciarSesion(String usu,String pass,String ip) {
		return usuarioDao.iniciarSesion(usu, pass, ip);
	}


	@Override
	public void cerrarSesion(String cuenta) {
		usuarioDao.cerrarSesion(cuenta);		
	}



}
