package pe.tgestiona.logistica.inversa.dao;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.BaseGarantiaBean;
import pe.tgestiona.logistica.inversa.bean.BaseValidacionExternaBean;
import pe.tgestiona.logistica.inversa.bean.ConfiguracionBean;
import pe.tgestiona.logistica.inversa.bean.DespachoRRSSBean;
import pe.tgestiona.logistica.inversa.bean.DetalleFichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.FichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.MaterialBean;
import pe.tgestiona.logistica.inversa.bean.MaterialSAPBean;
import pe.tgestiona.logistica.inversa.bean.TallerBean;
import pe.tgestiona.logistica.inversa.bean.TipoBodegaBean;

public interface ValidacionDao {
	//Validaciones Seriados y No Seriados
	public List<DetalleFichaDevolucionBean> validarItems(List<FichaDevolucionBean> listaFichaDevol,String estadoValidacion,String usuario);
	public MaterialSAPBean obtenerDatosSAP(String serie, String tipo, String rubro,String tecnologia, String tipoSAP,String garantia,String descripcionActa,String usuario);
	public MaterialBean obtenerDatosMaterial(String material,String tipo,String rubro,String tecnologia);
	public TipoBodegaBean obtenerTipoBodega(String centro, String almacen,String entidad);	
	public boolean esBodegaLiquidada(String centro,String almacen);
	public boolean esLotizable(String material);
	public BaseGarantiaBean obtenerDatosBaseGarantia(String material,String serie,String usuario);
	public BaseValidacionExternaBean obtenerBaseValidacionExterna(String ticket,String material,String serie,String usuario);
	public TallerBean existeGarantiaTaller(String serie,String material);	
	public DespachoRRSSBean existeRRSS(String serie,String material);
	public String baseNegocioGarantia(String material);
	public String obtenerNombreBodegaConfiguracion(String tipoBodega);

	
	//Validaciones para Devoluci 
	
	public String esTramosMinimos(String material,int libreUtilizacion);
	public MaterialSAPBean obtenerDatosSAPDevoluci(String material,String tipoSAP,String tipo,String rubro, String tecnologia,String usuario);
	List<ConfiguracionBean> obtenerConfiguraciones(String motivo,String tipoConf);

}
