package pe.tgestiona.logistica.inversa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.tgestiona.logistica.inversa.bean.TipoFormatoBean;
import pe.tgestiona.logistica.inversa.service.TipoFormatoService;

@Controller
public class TipoFormatoController {
	@Autowired
	private TipoFormatoService tipoFormatoService;
	
	@ResponseBody @RequestMapping(value = "listaFormato", method = RequestMethod.GET)
	public List<TipoFormatoBean> lista_devolucion() {
		List<TipoFormatoBean> list = null;
		list=tipoFormatoService.listaFormato();
		return list;
	}
	
}
