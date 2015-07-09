package pe.tgestiona.logistica.inversa.service;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.SociedadMaterialBean;

public interface SociedadMaterialService {
	public List<SociedadMaterialBean> listasociedad();
	public SociedadMaterialBean obtenerDatos(String codSociedadMaterial);
}
