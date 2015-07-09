package pe.tgestiona.logistica.inversa.dao;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.GestorActaBean;

public interface GestorActaDao {
	public void grabarGestorActa(GestorActaBean gestorActaBean);
	public GestorActaBean obtenerDatos(String correo);
	public List<GestorActaBean> lstGestorEntidad(String codEntidad);
}
