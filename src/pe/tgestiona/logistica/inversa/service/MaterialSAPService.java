package pe.tgestiona.logistica.inversa.service;

import java.io.InputStream;
import java.util.List;

import pe.tgestiona.logistica.inversa.bean.ErroresBean;
import pe.tgestiona.logistica.inversa.bean.MaterialBean;
import pe.tgestiona.logistica.inversa.bean.MaterialSAPBean;

public interface MaterialSAPService {
	public void grabarMaterialSAPSeriado(List<MaterialSAPBean> lista,String tipo,String usuario);
	public void grabarMaterialSAPNoSeriado(List<MaterialSAPBean> lista,String tipo,String usuario);
	public List<MaterialSAPBean> leerarchivoSAPSeriado(InputStream file,String tipoExcel);
	public List<MaterialSAPBean> leerarchivoSAPNoSeriado(InputStream file,String tipoExcel);
	public void eliminaDatosSapSeriado(String usuario);	
	public List<String> validarCampoCantidad(List<MaterialSAPBean> lista);
	public List<String> validarCampoCaracteresCantidad(List<MaterialSAPBean> lista);
	public List<String> validarCantidadCamposSeriado(List<MaterialSAPBean> lista);
	public List<String> validarCantidadCamposNoSeriado(List<MaterialSAPBean> lista);
	public List<String> validarLongitudCodigoMaterial(List<MaterialSAPBean> lista,String tipoSAP);
	public List<String[]> lstMaterial(String material,
			 String descripcion, String peso, String volumen, String seriado,
			 String tipo, String rubro, String tecnologia, String negocio,
			 String prov, String sociedad, String precioEquipo, String lotizable,int pag,int regMostrar,int pagIntervalo);
	
	public void grabarMaterial(MaterialBean materialBean);
	public MaterialBean obtenerDatosMaterial(String codMaterial);
	public List<ErroresBean> obtenerTiposErroresSAP(List<MaterialSAPBean> lista,String tipoSAP,boolean seriado);
	
}
