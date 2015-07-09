package pe.tgestiona.logistica.inversa.service;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.ConfiguracionBean;

public interface ConfiguracionService {
	public void guardarConfiguracion(String criterio,String configuracion,String valor,String parametro,String existencia);
	public void eliminarConfiguracion(String criterio,String configuracion);
	public List<ConfiguracionBean> listaConfiguracion(String criterio);
	public List<String> listaTipoBodegas();
	public String obtenerHoraCierre(String criterio);
}
