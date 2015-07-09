package pe.tgestiona.logistica.inversa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.NegocioBean;
import pe.tgestiona.logistica.inversa.dao.NegocioDao;
import pe.tgestiona.logistica.inversa.service.NegocioService;

@Service
public class NegocioServiceImpl implements NegocioService{

	@Autowired
	private NegocioDao negocioDao;
	
	@Override
	public List<NegocioBean> listaNegocio() {
		return negocioDao.listaNegocio();
	}

	@Override
	public NegocioBean obtenerDatos(String codNegocio) {
		return negocioDao.obtenerDatos(codNegocio);
	}

}
