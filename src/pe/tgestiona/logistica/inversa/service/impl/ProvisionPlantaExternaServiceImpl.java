package pe.tgestiona.logistica.inversa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.ProvisionPlantaExternaBean;
import pe.tgestiona.logistica.inversa.dao.ProvisionPlantaExternaDao;
import pe.tgestiona.logistica.inversa.service.ProvisionPlantaExternaService;

@Service
public class ProvisionPlantaExternaServiceImpl implements ProvisionPlantaExternaService{

	@Autowired
	private ProvisionPlantaExternaDao provisionPlantaExternaDao;
	
	@Override
	public List<ProvisionPlantaExternaBean> listaProvision() {
		return provisionPlantaExternaDao.listaProvision();
	}

	@Override
	public ProvisionPlantaExternaBean obtenerDatos(String codProvision) {
		return provisionPlantaExternaDao.obtenerDatos(codProvision);
	}

}
