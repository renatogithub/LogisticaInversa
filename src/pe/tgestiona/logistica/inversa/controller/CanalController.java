package pe.tgestiona.logistica.inversa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import pe.tgestiona.logistica.inversa.service.CanalService;
import pe.tgestiona.logistica.inversa.bean.CanalBean;

@Controller
public class CanalController {
	
	@Autowired
	private CanalService canalService;
	
	@ResponseBody @RequestMapping(value = "listacanales", method = RequestMethod.GET)
	public List<CanalBean> lista_canales(Model model) {
		List<CanalBean> list = null;
		list=canalService.listaCanales();
		model.addAttribute("lista_canales",list);
		return list;
	}
}
