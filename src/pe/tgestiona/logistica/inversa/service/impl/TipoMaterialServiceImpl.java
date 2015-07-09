package pe.tgestiona.logistica.inversa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.TipoMaterialBean;
import pe.tgestiona.logistica.inversa.dao.TipoMaterialDao;
import pe.tgestiona.logistica.inversa.service.TipoMaterialService;

@Service
public class TipoMaterialServiceImpl implements TipoMaterialService{

	@Autowired
	private TipoMaterialDao tipoMaterialDao;

	@Override
	public List<TipoMaterialBean> lstTipoMaterial() {
		return tipoMaterialDao.lstTipoMaterial();
	}

	@Override
	public TipoMaterialBean obtenerDatos(String codTipoMaterial) {
		return tipoMaterialDao.obtenerDatos(codTipoMaterial);
	}
	
	
}
