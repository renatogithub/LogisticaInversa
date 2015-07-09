package pe.tgestiona.logistica.inversa.service;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.ContactoBean;

public interface ContactoService {
	public void grabarContacto(ContactoBean contactoBean);
	public ContactoBean obtenerDatos(String correo);
	public List<ContactoBean> lstContactoEntidad(String codEntidad);
}
