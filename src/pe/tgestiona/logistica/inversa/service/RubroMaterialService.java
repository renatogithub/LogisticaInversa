package pe.tgestiona.logistica.inversa.service;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.RubroMaterialBean;

public interface RubroMaterialService {
	public List<RubroMaterialBean> lstRubroMaterial();
	public RubroMaterialBean obtenerDatos(String codRubro);
}
