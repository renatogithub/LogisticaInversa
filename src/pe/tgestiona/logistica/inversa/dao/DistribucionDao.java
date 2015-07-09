package pe.tgestiona.logistica.inversa.dao;

import java.io.InputStream;
import java.util.List;

import pe.tgestiona.logistica.inversa.bean.DistribucionBean;

public interface DistribucionDao {
	public byte[] descargarFuenteDistribucion(String acta);
	public void grabarCargaDistribucion(List<DistribucionBean> lstDistribucion);
	public List<DistribucionBean> leerarchivoExcel(InputStream file);
}
