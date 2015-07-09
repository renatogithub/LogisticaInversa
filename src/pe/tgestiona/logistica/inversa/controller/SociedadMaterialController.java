package pe.tgestiona.logistica.inversa.controller;

import java.util.List;

import org.jboss.logging.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.tgestiona.logistica.inversa.bean.SociedadMaterialBean;
import pe.tgestiona.logistica.inversa.service.SociedadMaterialService;

@Controller
public class SociedadMaterialController {

	@Autowired
	private SociedadMaterialService sociedadService;
	
	@ResponseBody @RequestMapping(value = "listasociedadmaterial", method = RequestMethod.GET)
	public List<SociedadMaterialBean> listasociedadmaterial(Model model) {
		List<SociedadMaterialBean> list = null;
		list=sociedadService.listasociedad();
		model.addAttribute("listasociedadmaterial",list);
		return list;
	}
	
	@ResponseBody @RequestMapping(value = "obtenerDatosSociedadMaterial", method = RequestMethod.GET)
	public SociedadMaterialBean obtenerDatosSociedadMaterial(@Param String codSociedadMaterial,Model model) {
		SociedadMaterialBean sociedadMaterialBean=null;
		sociedadMaterialBean=sociedadService.obtenerDatos(codSociedadMaterial);
		model.addAttribute("sociedadMaterialBean",sociedadMaterialBean);
		return sociedadMaterialBean;
	}
}
