package pe.tgestiona.logistica.inversa.controller;

import java.util.List;

import org.jboss.logging.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.tgestiona.logistica.inversa.bean.ProvisionPlantaExternaBean;
import pe.tgestiona.logistica.inversa.service.ProvisionPlantaExternaService;

@Controller
public class ProvisionPlantaExternaController {

	@Autowired
	private ProvisionPlantaExternaService provisionPlantaExternaService;
	
	@ResponseBody @RequestMapping(value = "listaprovision", method = RequestMethod.GET)
	public List<ProvisionPlantaExternaBean> listaprovision(Model model) {
		List<ProvisionPlantaExternaBean> list = null;
		list=provisionPlantaExternaService.listaProvision();
		model.addAttribute("listaprovision",list);
		return list;
	}
	
	@ResponseBody @RequestMapping(value = "obtenerDatosProvision", method = RequestMethod.GET)
	public ProvisionPlantaExternaBean obtenerDatosProvision(@Param String codProvision,Model model) {
		ProvisionPlantaExternaBean provisionPlantaExternaBean = null;
		provisionPlantaExternaBean=provisionPlantaExternaService.obtenerDatos(codProvision);
		model.addAttribute("provisionPlantaExternaBean",provisionPlantaExternaBean);
		return provisionPlantaExternaBean;
	}
}
