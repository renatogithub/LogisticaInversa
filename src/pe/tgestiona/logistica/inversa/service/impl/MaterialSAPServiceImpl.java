package pe.tgestiona.logistica.inversa.service.impl;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.ErroresBean;
import pe.tgestiona.logistica.inversa.bean.MaterialBean;
import pe.tgestiona.logistica.inversa.bean.MaterialSAPBean;
import pe.tgestiona.logistica.inversa.dao.MaterialSAPDao;
import pe.tgestiona.logistica.inversa.service.MaterialSAPService;

@Service
public class MaterialSAPServiceImpl implements MaterialSAPService{

	@Autowired
	private MaterialSAPDao materialSAPDao;
	

	@Override
	public void grabarMaterialSAPSeriado(List<MaterialSAPBean> lista,String tipo,String usuario) {
		materialSAPDao.grabarMaterialSAPSeriado(lista,tipo,usuario);
	}
	
	@Override
	public void grabarMaterialSAPNoSeriado(List<MaterialSAPBean> lista,String tipo, String usuario) {
		materialSAPDao.grabarMaterialSAPNoSeriado(lista,tipo,usuario);
	}


	@Override
	public List<String> validarCampoCantidad(List<MaterialSAPBean> lista) {
		return materialSAPDao.validarCampoCantidad(lista);
	}

	@Override
	public List<String> validarCampoCaracteresCantidad(List<MaterialSAPBean> lista) {
		return materialSAPDao.validarCampoCaracteresCantidad(lista);
	}

/*	@Override
	public List<String> validarCantidadCampos(InputStream file) {
		return materialSAPDao.validarCantidadCampos(file);
	}*/

	public List<String> validarCantidadCamposSeriado(List<MaterialSAPBean> lista){
		return materialSAPDao.validarCantidadCamposSeriado(lista);
	}

	@Override
	public void eliminaDatosSapSeriado(String usuario) {
		materialSAPDao.eliminaDatosSapSeriado(usuario);
	}

	@Override
	public List<String[]> lstMaterial(String material, String descripcion,
			String peso, String volumen, String seriado, String tipo,
			String rubro, String tecnologia, String negocio, String prov,
			String sociedad, String precioEquipo, String lotizable, int pag,
			int regMostrar, int pagIntervalo) {
		return materialSAPDao.lstMaterial(material, descripcion, peso, volumen, seriado, tipo, rubro, tecnologia, negocio, prov, sociedad, precioEquipo, lotizable, pag, regMostrar, pagIntervalo);
	}

	@Override
	public void grabarMaterial(MaterialBean materialBean) {
		materialSAPDao.grabarMaterial(materialBean);		
	}

	@Override
	public MaterialBean obtenerDatosMaterial(String codMaterial) {
		return materialSAPDao.obtenerDatosMaterial(codMaterial);
	}

	@Override
	public List<String> validarLongitudCodigoMaterial(List<MaterialSAPBean> lista, String tipoSAP) {
		return materialSAPDao.validarLongitudCodigoMaterial(lista, tipoSAP);
	}

	@Override
	public List<ErroresBean> obtenerTiposErroresSAP(List<MaterialSAPBean> lista,String tipoSAP,boolean seriado){
		return materialSAPDao.obtenerTiposErroresSAP(lista, tipoSAP, seriado);
	}

	@Override
	public List<String> validarCantidadCamposNoSeriado(List<MaterialSAPBean> lista) {
		return materialSAPDao.validarCantidadCamposNoSeriado(lista);
	}

	@Override
	public List<MaterialSAPBean> leerarchivoSAPSeriado(InputStream file,String tipoExcel) {
		return materialSAPDao.leerarchivoSAPSeriado(file, tipoExcel);
	}

	@Override
	public List<MaterialSAPBean> leerarchivoSAPNoSeriado(InputStream file,String tipoExcel) {
		return materialSAPDao.leerarchivoSAPNoSeriado(file, tipoExcel);
	}

}
