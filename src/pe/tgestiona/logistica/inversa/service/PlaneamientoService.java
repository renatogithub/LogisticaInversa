package pe.tgestiona.logistica.inversa.service;

import java.io.InputStream;
import java.util.List;

import pe.tgestiona.logistica.inversa.bean.PlaneamientoBean;

public interface PlaneamientoService {
	public byte[] descargarFuentePlaneamiento(String acta);
	public void grabarCargaPlaneamiento(List<PlaneamientoBean> lstPlaneamiento);
	public List<PlaneamientoBean> leerarchivoExcel(InputStream file);
}
