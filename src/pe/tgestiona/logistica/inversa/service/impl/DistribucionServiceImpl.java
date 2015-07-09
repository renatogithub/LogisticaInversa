package pe.tgestiona.logistica.inversa.service.impl;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.DistribucionBean;
import pe.tgestiona.logistica.inversa.dao.DistribucionDao;
import pe.tgestiona.logistica.inversa.service.DistribucionService;


@Service
public class DistribucionServiceImpl implements DistribucionService{

	@Autowired
	private DistribucionDao distribucionDao;
	
	@Override
	public byte[] descargarFuenteDistribucion(String acta) {
		return distribucionDao.descargarFuenteDistribucion(acta);
	}

	
	@Override
	public void grabarCargaDistribucion(List<DistribucionBean> lstDistribucion) {
		distribucionDao.grabarCargaDistribucion(lstDistribucion);		
	}

	@Override
	public List<DistribucionBean> leerarchivoExcel(InputStream file) {
		return distribucionDao.leerarchivoExcel(file);
	}

}
