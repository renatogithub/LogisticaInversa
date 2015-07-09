package pe.tgestiona.logistica.inversa.dao;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.ConfiguracionBean;

public interface ConfiguracionDao {
	public void guardarConfiguracion(String criterio,String configuracion,String valor,String parametro,String existencia);
	public void eliminarConfiguracion(String criterio,String configuracion);
	public List<ConfiguracionBean> listaConfiguracion(String criterio);
	public List<String> listaTipoBodegas();
	public String obtenerHoraCierre(String criterio);
}
