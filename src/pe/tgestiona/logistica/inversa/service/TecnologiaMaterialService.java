package pe.tgestiona.logistica.inversa.service;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.TecnologiaMaterialBean;

public interface TecnologiaMaterialService {
	public List<TecnologiaMaterialBean> lstTecnologiaMaterial();
	public TecnologiaMaterialBean obtenerDatos(String codTecnologia); 
}
