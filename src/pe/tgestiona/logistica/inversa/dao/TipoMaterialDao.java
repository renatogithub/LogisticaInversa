package pe.tgestiona.logistica.inversa.dao;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.TipoMaterialBean;

public interface TipoMaterialDao {
	public List<TipoMaterialBean> lstTipoMaterial();
	public TipoMaterialBean obtenerDatos(String codTipoMaterial);
}
