package pe.tgestiona.logistica.inversa.controller;

import java.util.List;

import org.jboss.logging.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.tgestiona.logistica.inversa.bean.RubroMaterialBean;
import pe.tgestiona.logistica.inversa.service.RubroMaterialService;

@Controller
public class RubroMaterialController {

	@Autowired
	private RubroMaterialService rubroMaterialService;
	
	@ResponseBody @RequestMapping(value = "listarubromaterial", method = RequestMethod.GET)
	public List<RubroMaterialBean> listarubromaterial(Model model) {
		List<RubroMaterialBean> list = null;
		list=rubroMaterialService.lstRubroMaterial();
		model.addAttribute("listarubromaterial",list);
		return list;
	}
	
	@ResponseBody @RequestMapping(value = "obtenerDatosRubroMaterial", method = RequestMethod.GET)
	public RubroMaterialBean obtenerDatosRubroMaterial(@Param String codRubroMaterial,Model model) {
		RubroMaterialBean rubroMaterialBean=null;
		rubroMaterialBean=rubroMaterialService.obtenerDatos(codRubroMaterial);
		model.addAttribute("rubroMaterialBean",rubroMaterialBean);
		return rubroMaterialBean;
	}
	
}
