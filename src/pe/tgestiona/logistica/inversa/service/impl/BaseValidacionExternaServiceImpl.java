package pe.tgestiona.logistica.inversa.service.impl;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.BaseValidacionExternaBean;
import pe.tgestiona.logistica.inversa.dao.BaseValidacionExternaDao;
import pe.tgestiona.logistica.inversa.service.BaseValidacionExternaService;

@Service
public class BaseValidacionExternaServiceImpl implements BaseValidacionExternaService{

	@Autowired
	private BaseValidacionExternaDao baseValidacionExternaDao;
	
	@Override
	public List<BaseValidacionExternaBean> leerarchivoExcelValidacionExterna(InputStream file) {
		return baseValidacionExternaDao.leerarchivoExcelValidacionExterna(file);
	}

	@Override
	public void grabarBaseValidacionExterna(List<BaseValidacionExternaBean> lista, String usuario) {
		baseValidacionExternaDao.grabarBaseValidacionExterna(lista, usuario);		
	}

	@Override
	public void eliminaDatosBaseValidacionExterna() {
		baseValidacionExternaDao.eliminaDatosBaseValidacionExterna();
	}

}
