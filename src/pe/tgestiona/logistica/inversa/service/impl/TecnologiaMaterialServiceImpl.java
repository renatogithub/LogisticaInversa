package pe.tgestiona.logistica.inversa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.TecnologiaMaterialBean;
import pe.tgestiona.logistica.inversa.dao.TecnologiaMaterialDao;
import pe.tgestiona.logistica.inversa.service.TecnologiaMaterialService;

@Service
public class TecnologiaMaterialServiceImpl implements TecnologiaMaterialService{

	@Autowired
	private TecnologiaMaterialDao tecnologiaMaterialDao;

	@Override
	public List<TecnologiaMaterialBean> lstTecnologiaMaterial() {
		return tecnologiaMaterialDao.lstTecnologiaMaterial();
	}

	@Override
	public TecnologiaMaterialBean obtenerDatos(String codTecnologia) {
		return tecnologiaMaterialDao.obtenerDatos(codTecnologia);
	}
	
	
}
