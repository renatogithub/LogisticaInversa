package pe.tgestiona.logistica.inversa.service;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.TipoMaterialBean;

public interface TipoMaterialService {
	public List<TipoMaterialBean> lstTipoMaterial();
	public TipoMaterialBean obtenerDatos(String codTipoMaterial);
}
