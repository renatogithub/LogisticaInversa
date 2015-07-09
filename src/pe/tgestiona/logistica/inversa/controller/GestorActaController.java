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

import pe.tgestiona.logistica.inversa.bean.GestorActaBean;
import pe.tgestiona.logistica.inversa.service.GestorActaService;

@Controller
public class GestorActaController {
	@Autowired
	private GestorActaService gestorActaService;
	
	
	@RequestMapping(value = "addGestor", method = RequestMethod.GET)
	public String addGestor() {
		return "transacciones/addGestorActa";
	}
	
	
	@ResponseBody @RequestMapping(value = "listagestoresentidad", method = RequestMethod.GET)
	public List<GestorActaBean> listagestoresentidad(@Param String codEntidad,Model model) {
		List<GestorActaBean> list = null;
		list=gestorActaService.lstGestorEntidad(codEntidad);
		model.addAttribute("listagestoresentidad",list);
		return list;
	}
	
	@ResponseBody @RequestMapping(value = "obtenerDatosGestor", method = RequestMethod.GET)
	public GestorActaBean obtenerDatosGestor(@Param String correo,Model model) {
		GestorActaBean gestorActaBean = null;
		gestorActaBean=gestorActaService.obtenerDatos(correo);
		model.addAttribute("gestorActaBean",gestorActaBean);
		return gestorActaBean;
	}
	
	
	@RequestMapping(value = "registrarGestorActa", method = RequestMethod.POST)
	public String registrarGestorActa(HttpServletRequest req,Model model){
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
        
        GestorActaBean gestorActaBean=new GestorActaBean();

        gestorActaBean.setCorreo(correo);
        gestorActaBean.setNombre(nombre);
        gestorActaBean.setTlfAnexo(tlfAnexo);
        gestorActaBean.setRpm(rpm);
        gestorActaBean.setEntidad(entidad);
        
        gestorActaService.grabarGestorActa(gestorActaBean);
        
        return "transacciones/addGestorActa";

	}
}
