package pe.tgestiona.logistica.inversa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.GestorActaBean;
import pe.tgestiona.logistica.inversa.dao.GestorActaDao;
import pe.tgestiona.logistica.inversa.service.GestorActaService;

@Service
public class GestorActaServiceImpl implements GestorActaService{

	@Autowired
	private GestorActaDao gestorActaDao;
	
	@Override
	public void grabarGestorActa(GestorActaBean gestorActaBean) {
		gestorActaDao.grabarGestorActa(gestorActaBean);		
	}

	@Override
	public GestorActaBean obtenerDatos(String correo) {
		return gestorActaDao.obtenerDatos(correo);
	}

	@Override
	public List<GestorActaBean> lstGestorEntidad(String codEntidad) {
		return gestorActaDao.lstGestorEntidad(codEntidad);
	}

}
