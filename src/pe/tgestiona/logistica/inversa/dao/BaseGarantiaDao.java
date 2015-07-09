package pe.tgestiona.logistica.inversa.dao;

import java.io.InputStream;
import java.util.List;

import pe.tgestiona.logistica.inversa.bean.BaseGarantiaBean;

public interface BaseGarantiaDao {
	public List<BaseGarantiaBean> leerarchivoExcelValidarPendientes(InputStream file);
	public void grabarBaseGarantia(List<BaseGarantiaBean> lstBaseGarantia,String usuario);
	public void eliminaDatosBaseGarantia(String usuario);
}
