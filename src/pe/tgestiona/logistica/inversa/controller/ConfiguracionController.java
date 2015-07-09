package pe.tgestiona.logistica.inversa.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.tgestiona.logistica.inversa.bean.ConfiguracionBean;
import pe.tgestiona.logistica.inversa.bean.ConstantesGenerales;
import pe.tgestiona.logistica.inversa.service.ConfiguracionService;

@Controller
public class ConfiguracionController {


	@Autowired
	private ConfiguracionService configuracionService;
	
	@ResponseBody @RequestMapping(value = "listaConfiguracion", method = RequestMethod.GET)
	public List<ConfiguracionBean> listaConfiguracion(String criterio,Model model) {
		List<ConfiguracionBean> list = null;
		list=configuracionService.listaConfiguracion(criterio);
		model.addAttribute("listaConfiguracion",list);
		return list;
	}
	
	@ResponseBody @RequestMapping(value = "obtenerHoraCierre", method = RequestMethod.GET)
	public String obtenerHoraCierre(String criterio) {
		String horaCierre = null;
		horaCierre=configuracionService.obtenerHoraCierre(criterio);
		return horaCierre;
	}
	
	@RequestMapping(value="registro" , method = RequestMethod.POST)
	public String registroConfiguracion(HttpServletRequest req, HttpServletResponse res,Model model){

		String criterio = req.getParameter("h_devolucion");
		String existeBodega=req.getParameter("rdbExistenciaConfiguracionBodega");
		String existeLote=req.getParameter("rdbExistenciaConfiguracionLote");
		String existeGarantia=req.getParameter("rdbExistenciaConfiguracionGarantia");
		String diasProveedores=req.getParameter("t_diasProveedores");
		String fechaValidacion=req.getParameter("rdbFecha");		
		String diasTaller=req.getParameter("t_diasTaller");
		String existenciaSAP=req.getParameter("rdbExistenciaSAP");
		String modoRecojo=req.getParameter("rdbModoRecojo");
		String chkSeriado=req.getParameter("chkSeriado");
		String existePedidoEntrega=req.getParameter("rdbExistenciaPedidoEntrega");
		
		System.out.println("Existe Bodega:" + existeBodega);
		System.out.println("Existe Lote:" + existeLote);
		System.out.println("Existe Garantia:" + existeGarantia);
		System.out.println("Existe SAP:" + existenciaSAP);
		
		String chkTipoBodega[]=req.getParameterValues("chkTipoBodega[]");
		String valoresLote[]=req.getParameterValues("t_lote[]");
		
		configuracionService.eliminarConfiguracion(criterio, ConstantesGenerales.TipoConfiguracion.BODEGA.getTipoValor());
		configuracionService.eliminarConfiguracion(criterio, ConstantesGenerales.TipoConfiguracion.LOTE.getTipoValor());
		configuracionService.eliminarConfiguracion(criterio, ConstantesGenerales.TipoConfiguracion.GARANTIAPROVEEDORES.getTipoValor());
		configuracionService.eliminarConfiguracion(criterio, ConstantesGenerales.TipoConfiguracion.GARANTIATALLER.getTipoValor());
		configuracionService.eliminarConfiguracion(criterio, ConstantesGenerales.TipoConfiguracion.EXISTENCIASAP.getTipoValor());
		configuracionService.eliminarConfiguracion(criterio, ConstantesGenerales.TipoConfiguracion.MODORECOJO.getTipoValor());
		configuracionService.eliminarConfiguracion(criterio, ConstantesGenerales.TipoConfiguracion.PEDIDOENTREGA.getTipoValor());
		
		
		if(existeBodega.equals("SI")){
			for(int x=0;x<chkTipoBodega.length;x++){	
				configuracionService.guardarConfiguracion(criterio, ConstantesGenerales.TipoConfiguracion.BODEGA.getTipoValor(), chkTipoBodega[x], ConstantesGenerales.GUION, existeBodega);
			}
		}
		
		if(existeBodega.equals("NO")){
			configuracionService.guardarConfiguracion(criterio, ConstantesGenerales.TipoConfiguracion.BODEGA.getTipoValor(),ConstantesGenerales.GUION, ConstantesGenerales.GUION, existeBodega);
		}
		
		if(existeLote.equals("SI")){
			if(valoresLote==null){
				configuracionService.guardarConfiguracion(criterio, ConstantesGenerales.TipoConfiguracion.LOTE.getTipoValor(), ConstantesGenerales.GUION, ConstantesGenerales.GUION, "NO");
			}else{
				for(int x=0;x<valoresLote.length;x++){	
					configuracionService.guardarConfiguracion(criterio, ConstantesGenerales.TipoConfiguracion.LOTE.getTipoValor(), valoresLote[x], ConstantesGenerales.GUION, existeLote);
				}
			}

		}
		
		if(existeLote.equals("NO")){
			configuracionService.guardarConfiguracion(criterio, ConstantesGenerales.TipoConfiguracion.LOTE.getTipoValor(),ConstantesGenerales.GUION, ConstantesGenerales.GUION, existeLote);
		}
		
		
		if(existeGarantia.equals("SI")){
			configuracionService.guardarConfiguracion(criterio,  ConstantesGenerales.TipoConfiguracion.GARANTIAPROVEEDORES.getTipoValor(), diasProveedores,fechaValidacion,existeGarantia);
			configuracionService.guardarConfiguracion(criterio,  ConstantesGenerales.TipoConfiguracion.GARANTIATALLER.getTipoValor(), diasTaller,fechaValidacion,existeGarantia);
		}
		
		if(existeGarantia.equals("NO")){
			configuracionService.guardarConfiguracion(criterio,  ConstantesGenerales.TipoConfiguracion.GARANTIAPROVEEDORES.getTipoValor(), ConstantesGenerales.GUION,ConstantesGenerales.GUION,existeGarantia);
			configuracionService.guardarConfiguracion(criterio,  ConstantesGenerales.TipoConfiguracion.GARANTIATALLER.getTipoValor(), ConstantesGenerales.GUION,ConstantesGenerales.GUION,existeGarantia);
		}

/*		if(existeGarantiaTaller.equals("SI")){
			configuracionService.guardarConfiguracion(criterio,  ConstantesGenerales.TipoConfiguracion.GARANTIATALLER.getTipoValor(), diasTaller,fechaValidacionTaller,existeGarantiaProveedor);
		}
		
		if(existeGarantiaTaller.equals("NO")){
			configuracionService.guardarConfiguracion(criterio,  ConstantesGenerales.TipoConfiguracion.GARANTIATALLER.getTipoValor(), ConstantesGenerales.GUION,ConstantesGenerales.GUION,existeGarantiaProveedor);
		}*/
		
		if(existenciaSAP.equals("SI")){
			configuracionService.guardarConfiguracion(criterio,  ConstantesGenerales.TipoConfiguracion.EXISTENCIASAP.getTipoValor(), ConstantesGenerales.GUION,ConstantesGenerales.GUION,existenciaSAP);
		}
		
		if(existenciaSAP.equals("NO")){
			configuracionService.guardarConfiguracion(criterio,  ConstantesGenerales.TipoConfiguracion.EXISTENCIASAP.getTipoValor(), ConstantesGenerales.GUION,ConstantesGenerales.GUION,existenciaSAP);			
		}
		
		if(chkSeriado==null){
			configuracionService.guardarConfiguracion(criterio, ConstantesGenerales.TipoConfiguracion.MODORECOJO.getTipoValor(), modoRecojo, "NO","SI");									
		}else{
			configuracionService.guardarConfiguracion(criterio, ConstantesGenerales.TipoConfiguracion.MODORECOJO.getTipoValor(), modoRecojo, "SI","SI");	
		}
		
		if(existePedidoEntrega.equals("SI")){
			configuracionService.guardarConfiguracion(criterio, ConstantesGenerales.TipoConfiguracion.PEDIDOENTREGA.getTipoValor(), ConstantesGenerales.GUION,ConstantesGenerales.GUION,existePedidoEntrega);
		}

		if(existePedidoEntrega.equals("NO")){
			configuracionService.guardarConfiguracion(criterio, ConstantesGenerales.TipoConfiguracion.PEDIDOENTREGA.getTipoValor(), ConstantesGenerales.GUION,ConstantesGenerales.GUION,existePedidoEntrega);
		}

		return "transacciones/configuracionSistema";
	}
}
