package pe.tgestiona.logistica.inversa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.ContactoBean;
import pe.tgestiona.logistica.inversa.dao.ContactoDao;
import pe.tgestiona.logistica.inversa.service.ContactoService;

@Service
public class ContactoServiceImpl implements ContactoService{

	@Autowired
	private ContactoDao contactoDao;
	
	@Override
	public void grabarContacto(ContactoBean contactoBean) {
		contactoDao.grabarContacto(contactoBean);		
	}

	@Override
	public ContactoBean obtenerDatos(String correo) {
		return contactoDao.obtenerDatos(correo);
	}

	@Override
	public List<ContactoBean> lstContactoEntidad(String codEntidad) {
		return contactoDao.lstContactoEntidad(codEntidad);
	}

}
