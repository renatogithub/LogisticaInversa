package pe.tgestiona.logistica.inversa.dao;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.SociedadMaterialBean;

public interface SociedadMaterialDao {
	public List<SociedadMaterialBean> listasociedad();
	public SociedadMaterialBean obtenerDatos(String codSociedadMaterial);
}
