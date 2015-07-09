package pe.tgestiona.logistica.inversa.dao;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.ContactoBean;

public interface ContactoDao {
	public void grabarContacto(ContactoBean contactoBean);
	public ContactoBean obtenerDatos(String correo);
	public List<ContactoBean> lstContactoEntidad(String codEntidad);
}
