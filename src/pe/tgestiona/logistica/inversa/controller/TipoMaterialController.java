package pe.tgestiona.logistica.inversa.controller;

import java.util.List;

import org.jboss.logging.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.tgestiona.logistica.inversa.bean.TipoMaterialBean;
import pe.tgestiona.logistica.inversa.service.TipoMaterialService;

@Controller
public class TipoMaterialController {

	@Autowired
	private TipoMaterialService tipoMaterialService;
	
	@ResponseBody @RequestMapping(value = "listatipomaterial", method = RequestMethod.GET)
	public List<TipoMaterialBean> listatipomaterial(Model model) {
		List<TipoMaterialBean> list = null;
		list=tipoMaterialService.lstTipoMaterial();
		model.addAttribute("listatipomaterial",list);
		return list;
	}
	
	@ResponseBody @RequestMapping(value = "obtenerDatosTipoMaterial", method = RequestMethod.GET)
	public TipoMaterialBean obtenerDatosNegocio(@Param String codTipoMaterial,Model model) {
		TipoMaterialBean tipoMaterialBean=null;
		tipoMaterialBean=tipoMaterialService.obtenerDatos(codTipoMaterial);
		model.addAttribute("tipoMaterialBean",tipoMaterialBean);
		return tipoMaterialBean;
	}
}
