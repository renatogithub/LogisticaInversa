package pe.tgestiona.logistica.inversa.controller;

import java.util.List;

import org.jboss.logging.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.tgestiona.logistica.inversa.bean.NegocioBean;
import pe.tgestiona.logistica.inversa.service.NegocioService;

@Controller
public class NegocioController {

	@Autowired
	private NegocioService negocioService;
	
	@ResponseBody @RequestMapping(value = "listanegocio", method = RequestMethod.GET)
	public List<NegocioBean> listanegocio(Model model) {
		List<NegocioBean> list = null;
		list=negocioService.listaNegocio();
		model.addAttribute("listanegocio",list);
		return list;
	}
	
	@ResponseBody @RequestMapping(value = "obtenerDatosNegocio", method = RequestMethod.GET)
	public NegocioBean obtenerDatosNegocio(@Param String codNegocio,Model model) {
		NegocioBean negocioBean=null;
		negocioBean=negocioService.obtenerDatos(codNegocio);
		model.addAttribute("provisionPlantaExternaBean",negocioBean);
		return negocioBean;
	}
}
