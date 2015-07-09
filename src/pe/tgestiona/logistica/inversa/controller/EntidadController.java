package pe.tgestiona.logistica.inversa.controller;

import java.util.List;

import org.jboss.logging.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.tgestiona.logistica.inversa.bean.EntidadBean;
import pe.tgestiona.logistica.inversa.service.EntidadService;

@Controller
public class EntidadController {
	@Autowired
	private EntidadService entidadService;
	
	@ResponseBody @RequestMapping(value = "listaEntidades")
	public List<EntidadBean> listaEntidades(@Param String codCanal,Model model) {
		List<EntidadBean> list = null;
		list=entidadService.listaEntidad(codCanal);
		model.addAttribute("lista_canales",list);
		return list;
	}
	
	@ResponseBody @RequestMapping(value = "datosEntidad")
	public EntidadBean datosEntidad(@Param String codEntidad,Model model) {
		EntidadBean entidadBean = null;
		entidadBean=entidadService.obtenerDatos(codEntidad);
		model.addAttribute("entidadBean",entidadBean);
		return entidadBean;
	}
}
