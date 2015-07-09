package pe.tgestiona.logistica.inversa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.RubroMaterialBean;
import pe.tgestiona.logistica.inversa.dao.RubroMaterialDao;
import pe.tgestiona.logistica.inversa.service.RubroMaterialService;

@Service
public class RubroMaterialServiceImpl implements RubroMaterialService{

	@Autowired
	private RubroMaterialDao rubroMaterialDao;
	
	@Override
	public List<RubroMaterialBean> lstRubroMaterial() {
		return rubroMaterialDao.lstRubroMaterial();
	}

	@Override
	public RubroMaterialBean obtenerDatos(String codRubro) {
		return rubroMaterialDao.obtenerDatos(codRubro);
	}

}
