package pe.tgestiona.logistica.inversa.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.tgestiona.logistica.inversa.bean.DespachoRRSSBean;
import pe.tgestiona.logistica.inversa.service.DespachoRRSSService;

@Controller
public class DespachoRRSSController {

	@Autowired
	private DespachoRRSSService despachoRRSSService;
	
	@ResponseBody @RequestMapping(value = "lstRRSS")
	public List<DespachoRRSSBean> lstDespachoRRSS(@Param String opcion,@Param String vMaterial,@Param String vSerie,@Param int vPag,Model model,HttpServletRequest req){
		List<DespachoRRSSBean> lstDespachoRRSS = null;
		lstDespachoRRSS=despachoRRSSService.lstDespachoRRSS(opcion, vMaterial,vSerie,vPag);
		model.addAttribute("lstRRSS",lstDespachoRRSS);
		return lstDespachoRRSS;
	}
	
}
