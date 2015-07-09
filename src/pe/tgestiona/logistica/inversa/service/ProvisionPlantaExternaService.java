package pe.tgestiona.logistica.inversa.service;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.ProvisionPlantaExternaBean;

public interface ProvisionPlantaExternaService {
	public List<ProvisionPlantaExternaBean> listaProvision();
	public ProvisionPlantaExternaBean obtenerDatos(String codProvision);
}
