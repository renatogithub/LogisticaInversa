package pe.tgestiona.logistica.inversa.dao;

import java.io.InputStream;
import java.util.List;

import pe.tgestiona.logistica.inversa.bean.PlaneamientoBean;


public interface PlaneamientoDao {
	public byte[] descargarFuentePlaneamiento(String acta);
	public void grabarCargaPlaneamiento(List<PlaneamientoBean> lstPlaneamiento);
	public List<PlaneamientoBean> leerarchivoExcel(InputStream file);
}
