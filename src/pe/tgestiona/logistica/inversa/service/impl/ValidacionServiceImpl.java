package pe.tgestiona.logistica.inversa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.BaseGarantiaBean;
import pe.tgestiona.logistica.inversa.bean.BaseValidacionExternaBean;
import pe.tgestiona.logistica.inversa.bean.ConfiguracionBean;
import pe.tgestiona.logistica.inversa.bean.DespachoRRSSBean;
import pe.tgestiona.logistica.inversa.bean.DetalleFichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.FichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.MaterialSAPBean;
import pe.tgestiona.logistica.inversa.bean.TallerBean;
import pe.tgestiona.logistica.inversa.bean.TipoBodegaBean;
import pe.tgestiona.logistica.inversa.dao.ValidacionDao;
import pe.tgestiona.logistica.inversa.service.ValidacionService;

@Service
public class ValidacionServiceImpl implements ValidacionService{

	@Autowired
	private ValidacionDao validacionDao;

	@Override
	public DespachoRRSSBean existeRRSS(String serie, String material) {
		return validacionDao.existeRRSS(serie, material);
	}

	@Override
	public TallerBean existeGarantiaTaller(String serie, String material) {
		return validacionDao.existeGarantiaTaller(serie, material);
	}


	@Override
	public TipoBodegaBean obtenerTipoBodega(String centro, String almacen,String entidad) {
		return validacionDao.obtenerTipoBodega(centro, almacen,entidad);
	}

	@Override
	public boolean esLotizable(String material) {
		return validacionDao.esLotizable(material);
	}

	@Override
	public String baseNegocioGarantia(String material) {
		return validacionDao.baseNegocioGarantia(material);
	}


	@Override
	public String esTramosMinimos(String material, int libreUtilizacion) {
		return validacionDao.esTramosMinimos(material, libreUtilizacion);
	}

	@Override
	public String esLibreUtilizacion(String material) {
		return null;
	}

	@Override
	public List<DetalleFichaDevolucionBean> validarItems(
			List<FichaDevolucionBean> listaFichaDevol, String estadoValidacion,
			String usuario) {
		return validacionDao.validarItems(listaFichaDevol, estadoValidacion, usuario);
	}

	@Override
	public MaterialSAPBean obtenerDatosSAP(String serie, String tipo,
			String rubro, String tecnologia, String tipoSAP, String garantia,
			String descripcionActa, String usuario) {
		return validacionDao.obtenerDatosSAP(serie, tipo, rubro, tecnologia, tipoSAP, garantia, descripcionActa, usuario);
	}

	@Override
	public BaseGarantiaBean obtenerDatosBaseGarantia(String material,
			String serie, String usuario) {
		return validacionDao.obtenerDatosBaseGarantia(material, serie, usuario);
	}

	@Override
	public BaseValidacionExternaBean obtenerBaseValidacionExterna(
			String ticket, String material, String serie, String usuario) {
		return validacionDao.obtenerBaseValidacionExterna(ticket, material, serie, usuario);
	}


	
}
