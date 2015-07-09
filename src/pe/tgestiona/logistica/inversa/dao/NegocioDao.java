package pe.tgestiona.logistica.inversa.dao;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.NegocioBean;

public interface NegocioDao {
	public List<NegocioBean> listaNegocio();
	public NegocioBean obtenerDatos(String codNegocio);
}
