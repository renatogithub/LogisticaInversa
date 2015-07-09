package pe.tgestiona.logistica.inversa.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.tgestiona.logistica.inversa.bean.MaterialBean;
import pe.tgestiona.logistica.inversa.service.MaterialSAPService;

@Controller
public class MaterialSAPController {
	
	@Autowired
	private MaterialSAPService materialSAPService;
	
	@ResponseBody @RequestMapping(value = "consultaMaterial")
	public List<String[]> consultaMaterial(@Param String vMaterial,@Param String vDescripcion,@Param String vPeso,@Param String vVolumen,@Param String vSeriado,@Param String vTipo,@Param String vRubro,@Param String vTecnologia,@Param String vNegocio,@Param String vProvincia,@Param String vSociedad,@Param String vPrecioEquipo,@Param String vLotizable,@Param int vPag,@Param int vRegMostrar,@Param int vPagIntervalo,Model model,HttpServletRequest req){
		List<String[]> lstMaterial=null;
		lstMaterial=materialSAPService.lstMaterial(vMaterial, vDescripcion, vPeso, vVolumen, vSeriado, vTipo, vRubro, vTecnologia, vNegocio, vProvincia, vSociedad, vPrecioEquipo, vLotizable, vPag, vRegMostrar, vPagIntervalo);
		model.addAttribute("lstMaterial",lstMaterial);
		return lstMaterial;
	}
	
	@RequestMapping(value = "obtenerMaterial",params="accion=get")
	public String obtenerMaterual(HttpServletRequest req,Model model) {
		String codMaterial=req.getParameter("t_codigo");
		MaterialBean materialBean=null;
		if(codMaterial!=null){
			materialBean=materialSAPService.obtenerDatosMaterial(codMaterial);
		}
		
		model.addAttribute("materialBean",materialBean);

		return "material/updMaterial";
	}
	
	
	
	@RequestMapping(value = "addMaterial", method = RequestMethod.GET)
	public String addMaterial() {
		return "material/addMaterial";
	}
	
	@ResponseBody @RequestMapping(value = "obtenerDatosMaterial", method = RequestMethod.GET)
	public MaterialBean obtenerDatosMaterial(@Param String codMaterial,Model model) {
		MaterialBean materialBean=null;
		materialBean=materialSAPService.obtenerDatosMaterial(codMaterial);
		model.addAttribute("materialBean",materialBean);
		return materialBean;
	}
	
	@RequestMapping(value = "consultarMaterial", method = RequestMethod.GET)
	public String consultarMaterial() {
		return "material/materialConsulta";
	}
	
	@RequestMapping(value = "registrarMaterial", method = RequestMethod.POST)
	public String registrarMaterial(HttpServletRequest req,Model model){
        String codMaterial = req.getParameter("t_codigo");
        String desMaterial = req.getParameter("t_descripcion");
        String umd = req.getParameter("t_umd");
        String peso=req.getParameter("t_peso");        
        String volUnitario=req.getParameter("t_volUnitario");
        String precio=req.getParameter("t_precio");
        String serNoSer=req.getParameter("rdbSerNoSer");
        String lotizable=req.getParameter("rdbLotizable");
        String provision=req.getParameter("h_Prov");
        String negocio=req.getParameter("h_Negocio");
        String tipoMat=req.getParameter("h_TipoMat");
        String rubroMat=req.getParameter("h_RubMat");
        String tecMat=req.getParameter("h_TecMat");
        String socMat=req.getParameter("h_SocMat");
        
        MaterialBean materialBean=new MaterialBean();
        
        materialBean.setCodigo(codMaterial);
        materialBean.setDescripcion(desMaterial);
        materialBean.setUmd(umd);
        materialBean.setPeso(peso);
        materialBean.setVolUnitario(volUnitario);
        materialBean.setPrecio(precio);
        materialBean.setSeriado(serNoSer);
        materialBean.setLotizable(lotizable);
        materialBean.setProvision(provision);
        materialBean.setNegocio(negocio);
        materialBean.setTipoMaterial(tipoMat);        
        materialBean.setRubro(rubroMat);
        materialBean.setTecnologia(tecMat);
        materialBean.setSociedad(socMat);
        
        materialSAPService.grabarMaterial(materialBean);
        
		return "material/addMaterial";
	}
}
