package pe.tgestiona.logistica.inversa.dao;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.EntidadBean;

public interface EntidadDao {
	public List<EntidadBean> listaEntidad(String canal);
	public EntidadBean obtenerDatos(String entidad);
}
