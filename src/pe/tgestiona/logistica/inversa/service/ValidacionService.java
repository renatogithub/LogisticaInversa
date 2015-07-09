package pe.tgestiona.logistica.inversa.service;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.BaseGarantiaBean;
import pe.tgestiona.logistica.inversa.bean.BaseValidacionExternaBean;
import pe.tgestiona.logistica.inversa.bean.DespachoRRSSBean;
import pe.tgestiona.logistica.inversa.bean.DetalleFichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.FichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.MaterialSAPBean;
import pe.tgestiona.logistica.inversa.bean.TallerBean;
import pe.tgestiona.logistica.inversa.bean.TipoBodegaBean;

public interface ValidacionService {
	public List<DetalleFichaDevolucionBean> validarItems(List<FichaDevolucionBean> listaFichaDevol,String estadoValidacion,String usuario);
	public MaterialSAPBean obtenerDatosSAP(String serie, String tipo, String rubro,String tecnologia, String tipoSAP,String garantia,String descripcionActa,String usuario);
	public TipoBodegaBean obtenerTipoBodega(String centro, String almacen,String entidad);
	public boolean esLotizable(String material);
	public BaseGarantiaBean obtenerDatosBaseGarantia(String material,String serie,String usuario);
	public BaseValidacionExternaBean obtenerBaseValidacionExterna(String ticket,String material,String serie,String usuario);
	public TallerBean existeGarantiaTaller(String serie,String material);	
	public DespachoRRSSBean existeRRSS(String serie,String material);
	public String baseNegocioGarantia(String material);
	
	//Validaciones para Devoluci 
	
	public String esTramosMinimos(String material,int libreUtilizacion);
	public String esLibreUtilizacion(String material);
}
