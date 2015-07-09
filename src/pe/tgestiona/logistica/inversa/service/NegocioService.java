package pe.tgestiona.logistica.inversa.service;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.NegocioBean;

public interface NegocioService {
	public List<NegocioBean> listaNegocio();
	public NegocioBean obtenerDatos(String codNegocio);
}
