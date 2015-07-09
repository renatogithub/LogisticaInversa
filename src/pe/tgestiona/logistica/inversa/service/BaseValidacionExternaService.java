package pe.tgestiona.logistica.inversa.service;

import java.io.InputStream;
import java.util.List;

import pe.tgestiona.logistica.inversa.bean.BaseValidacionExternaBean;

public interface BaseValidacionExternaService {
	public List<BaseValidacionExternaBean> leerarchivoExcelValidacionExterna(InputStream file);
	public void grabarBaseValidacionExterna(List<BaseValidacionExternaBean> lista,String usuario);
	public void eliminaDatosBaseValidacionExterna();
}
