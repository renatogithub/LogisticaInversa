package pe.tgestiona.logistica.inversa.dao;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.RubroMaterialBean;

public interface RubroMaterialDao{
	public List<RubroMaterialBean> lstRubroMaterial();
	public RubroMaterialBean obtenerDatos(String codRubro);
}
