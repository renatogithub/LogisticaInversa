package pe.tgestiona.logistica.inversa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.SociedadMaterialBean;
import pe.tgestiona.logistica.inversa.dao.SociedadMaterialDao;
import pe.tgestiona.logistica.inversa.service.SociedadMaterialService;

@Service
public class SociedadMaterialServiceImpl implements SociedadMaterialService{

	@Autowired
	private SociedadMaterialDao sociedadDao;

	@Override
	public List<SociedadMaterialBean> listasociedad() {
		return sociedadDao.listasociedad();
	}

	@Override
	public SociedadMaterialBean obtenerDatos(String codSociedadMaterial) {
		return sociedadDao.obtenerDatos(codSociedadMaterial);
	}
}
