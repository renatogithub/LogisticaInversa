package pe.tgestiona.logistica.inversa.dao;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.TecnologiaMaterialBean;

public interface TecnologiaMaterialDao {
	public List<TecnologiaMaterialBean> lstTecnologiaMaterial();
	public TecnologiaMaterialBean obtenerDatos(String codTecnologia); 
}
