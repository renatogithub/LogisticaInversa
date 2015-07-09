package pe.tgestiona.logistica.inversa.service.impl;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.BaseGarantiaBean;
import pe.tgestiona.logistica.inversa.dao.BaseGarantiaDao;
import pe.tgestiona.logistica.inversa.service.BaseGarantiaService;

@Service
public class BaseGarantiaServiceImpl implements BaseGarantiaService {

	@Autowired
	private BaseGarantiaDao baseGarantiaDao;
	
	@Override
	public List<BaseGarantiaBean> leerarchivoExcelValidarPendientes(InputStream file) {
		return baseGarantiaDao.leerarchivoExcelValidarPendientes(file);
	}

	@Override
	public void eliminaDatosBaseGarantia(String usuario) {
		baseGarantiaDao.eliminaDatosBaseGarantia(usuario);		
	}

	@Override
	public void grabarBaseGarantia(List<BaseGarantiaBean> lstBaseGarantia,String usuario) {
		baseGarantiaDao.grabarBaseGarantia(lstBaseGarantia, usuario);		
	}

}
