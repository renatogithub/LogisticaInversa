package pe.tgestiona.logistica.inversa.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.tgestiona.logistica.inversa.bean.MotivoDevolucionBean;
import pe.tgestiona.logistica.inversa.service.MotivoDevolucionService;

@Controller
public class MotivoDevolucionController {

	
	
	
	@Autowired
	private MotivoDevolucionService motivoDevolucionService;
	
	@ResponseBody @RequestMapping(value = "listaDevolucion", method = RequestMethod.GET)
	public List<MotivoDevolucionBean> lista_devolucion(HttpServletRequest req,HttpServletResponse res) {
		String formato=req.getParameter("formato");
		List<MotivoDevolucionBean> list = null;
		list=motivoDevolucionService.listaMotivos(formato);
		return list;
	}
	
	@ResponseBody @RequestMapping(value = "listaDevolucionMotivos", method = RequestMethod.GET)
	public List<MotivoDevolucionBean> listaDevolucionMotivos() {
		List<MotivoDevolucionBean> list = null;
		list=motivoDevolucionService.listaMotivos();
		return list;
	}
	
	@ResponseBody @RequestMapping(value = "obtenerDatosMotivo", method = RequestMethod.GET)
	public MotivoDevolucionBean obtenerDatosMotivo(HttpServletRequest request) {
		MotivoDevolucionBean motivoDevolucionBean = null;
		String codigo=request.getParameter("cboDevolucion");
		
		motivoDevolucionBean=motivoDevolucionService.obtenerMotivo(codigo);
		return motivoDevolucionBean;
	}
}
