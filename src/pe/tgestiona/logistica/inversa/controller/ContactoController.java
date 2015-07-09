package pe.tgestiona.logistica.inversa.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.tgestiona.logistica.inversa.bean.ContactoBean;
import pe.tgestiona.logistica.inversa.service.ContactoService;

@Controller
public class ContactoController {

	@Autowired
	private ContactoService contactoService;
	
	@RequestMapping(value = "addContacto", method = RequestMethod.GET)
	public String addContacto() {
		return "transacciones/addContacto";
	}
	
	@ResponseBody @RequestMapping(value = "listacontactosentidad", method = RequestMethod.GET)
	public List<ContactoBean> listacontactosentidad(@Param String codEntidad,Model model) {
		List<ContactoBean> list = null;
		list=contactoService.lstContactoEntidad(codEntidad);
		model.addAttribute("listacontactosentidad",list);
		return list;
	}
	
	@ResponseBody @RequestMapping(value = "obtenerDatosContacto", method = RequestMethod.GET)
	public ContactoBean obtenerDatosContacto(@Param String correo,Model model) {
		ContactoBean contactoBean = null;
		contactoBean=contactoService.obtenerDatos(correo);
		model.addAttribute("contactoBean",contactoBean);
		return contactoBean;
	}

	@RequestMapping(value = "registrarContacto", method = RequestMethod.POST)
	public String registrarMaterial(HttpServletRequest req,Model model){
        String correo = req.getParameter("t_correo");
        String nombre = req.getParameter("t_nombre");
        String tlfAnexo = req.getParameter("t_tlfAnexo");
        String rpm=req.getParameter("t_rpm");        
        String entidad=req.getParameter("t_entidad");
        
        if(correo!=null)correo=correo.trim();
        
        if(nombre!=null)nombre=nombre.trim();

        if(tlfAnexo!=null)tlfAnexo=tlfAnexo.trim();

        if(rpm!=null)rpm=rpm.trim();

        if(entidad!=null)entidad=entidad.trim();
        
        ContactoBean contactoBean=new ContactoBean();

        contactoBean.setCorreo(correo);
        contactoBean.setNombre(nombre);
        contactoBean.setTlfAnexo(tlfAnexo);
        contactoBean.setRpm(rpm);
        contactoBean.setEntidad(entidad);
        
        contactoService.grabarContacto(contactoBean);
        
        return "transacciones/addContacto";

	}
	
	
}
