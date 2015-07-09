package pe.tgestiona.logistica.inversa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.TipoFormatoBean;
import pe.tgestiona.logistica.inversa.dao.TipoFormatoDao;
import pe.tgestiona.logistica.inversa.service.TipoFormatoService;

@Service
public class TipoFormatoServiceImpl implements TipoFormatoService{

	@Autowired
	private TipoFormatoDao tipoFormatoDao;
	
	@Override
	public List<TipoFormatoBean> listaFormato() {
		return tipoFormatoDao.listaFormato();
	}

}
