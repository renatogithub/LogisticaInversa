package pe.tgestiona.logistica.inversa.service;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.EntidadBean;

public interface EntidadService {
	public List<EntidadBean> listaEntidad(String canal);
	public EntidadBean obtenerDatos(String entidad);
}
