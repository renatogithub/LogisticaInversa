package pe.tgestiona.logistica.inversa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.EntidadBean;
import pe.tgestiona.logistica.inversa.dao.EntidadDao;
import pe.tgestiona.logistica.inversa.service.EntidadService;

@Service
public class EntidadServiceImpl implements EntidadService{

	@Autowired
	private EntidadDao entidadDao;
	
	@Override
	public List<EntidadBean> listaEntidad(String canal) {
		return entidadDao.listaEntidad(canal);
	}

	@Override
	public EntidadBean obtenerDatos(String entidad) {
		return entidadDao.obtenerDatos(entidad);
	}

}
