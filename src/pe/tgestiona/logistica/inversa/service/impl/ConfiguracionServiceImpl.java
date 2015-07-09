package pe.tgestiona.logistica.inversa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.ConfiguracionBean;
import pe.tgestiona.logistica.inversa.dao.ConfiguracionDao;
import pe.tgestiona.logistica.inversa.service.ConfiguracionService;

@Service
public class ConfiguracionServiceImpl implements ConfiguracionService{

	@Autowired
	private ConfiguracionDao configuracionDao;
	
	@Override
	public List<ConfiguracionBean> listaConfiguracion(String criterio) {
		return configuracionDao.listaConfiguracion(criterio);
	}

	@Override
	public List<String> listaTipoBodegas() {
		return configuracionDao.listaTipoBodegas();
	}

	@Override
	public String obtenerHoraCierre(String criterio) {
		return configuracionDao.obtenerHoraCierre(criterio);
	}

	@Override
	public void guardarConfiguracion(String criterio, String configuracion,String valor, String parametro, String existencia) {
		configuracionDao.guardarConfiguracion(criterio, configuracion, valor, parametro, existencia);
	}

	@Override
	public void eliminarConfiguracion(String criterio, String configuracion) {
		configuracionDao.eliminarConfiguracion(criterio, configuracion);		
	}

}
