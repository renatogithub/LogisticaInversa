package pe.tgestiona.logistica.inversa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.tgestiona.logistica.inversa.bean.TipoBodegaBean;
import pe.tgestiona.logistica.inversa.service.TipoBodegaService;

@Controller
public class TipoBodegaController {

	@Autowired
	private TipoBodegaService tipoBodegaService;
	
	@ResponseBody @RequestMapping(value = "listaTipoBodegas", method = RequestMethod.GET)
	public List<TipoBodegaBean> listaTipoBodegas() {
		List<TipoBodegaBean> list = null;
		list=tipoBodegaService.lstTipoBodegas();
		return list;
	}
}
