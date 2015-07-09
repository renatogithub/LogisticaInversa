package pe.tgestiona.logistica.inversa.dao;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.ProvisionPlantaExternaBean;

public interface ProvisionPlantaExternaDao {
	public List<ProvisionPlantaExternaBean> listaProvision();
	public ProvisionPlantaExternaBean obtenerDatos(String codProvision);
}
