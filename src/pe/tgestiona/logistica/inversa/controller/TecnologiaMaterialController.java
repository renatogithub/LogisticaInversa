package pe.tgestiona.logistica.inversa.controller;

import java.util.List;

import org.jboss.logging.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.tgestiona.logistica.inversa.bean.TecnologiaMaterialBean;
import pe.tgestiona.logistica.inversa.service.TecnologiaMaterialService;

@Controller
public class TecnologiaMaterialController {

	@Autowired
	private TecnologiaMaterialService tecnologiaMaterialService;
	
	@ResponseBody @RequestMapping(value = "listatecnologiamaterial", method = RequestMethod.GET)
	public List<TecnologiaMaterialBean> listatecnologiamaterial(Model model) {
		List<TecnologiaMaterialBean> list = null;
		list=tecnologiaMaterialService.lstTecnologiaMaterial();
		model.addAttribute("listatecnologiamaterial",list);
		return list;
	}
	
	@ResponseBody @RequestMapping(value = "obtenerDatosTecnologiaMaterial", method = RequestMethod.GET)
	public TecnologiaMaterialBean obtenerDatosTecnologiaMaterial(@Param String codTecnologiaMaterial,Model model) {
		TecnologiaMaterialBean tecnologiaMaterialBean=null;
		tecnologiaMaterialBean=tecnologiaMaterialService.obtenerDatos(codTecnologiaMaterial);
		model.addAttribute("tecnologiaMaterialBean",tecnologiaMaterialBean);
		return tecnologiaMaterialBean;
	}
	
}
