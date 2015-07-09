package pe.tgestiona.logistica.inversa.service.impl;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.PlaneamientoBean;
import pe.tgestiona.logistica.inversa.dao.PlaneamientoDao;
import pe.tgestiona.logistica.inversa.service.PlaneamientoService;

@Service
public class PlaneamientoServiceImpl implements PlaneamientoService{

	@Autowired
	private PlaneamientoDao planeamientoDao;
	
	@Override
	public byte[] descargarFuentePlaneamiento(String acta) {
		return planeamientoDao.descargarFuentePlaneamiento(acta);
	}

	@Override
	public void grabarCargaPlaneamiento(List<PlaneamientoBean> lstPlaneamiento) {
		planeamientoDao.grabarCargaPlaneamiento(lstPlaneamiento);		
	}

	@Override
	public List<PlaneamientoBean> leerarchivoExcel(InputStream file) {
		return planeamientoDao.leerarchivoExcel(file);
	}

}
