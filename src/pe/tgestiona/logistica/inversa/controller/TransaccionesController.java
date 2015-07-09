package pe.tgestiona.logistica.inversa.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.read.biff.BiffException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import pe.tgestiona.logistica.inversa.bean.BaseGarantiaBean;
import pe.tgestiona.logistica.inversa.bean.BaseValidacionExternaBean;
import pe.tgestiona.logistica.inversa.bean.ConstantesGenerales;
import pe.tgestiona.logistica.inversa.bean.DetalleFichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.ErroresBean;
import pe.tgestiona.logistica.inversa.bean.FichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.FileUploadForm;
import pe.tgestiona.logistica.inversa.bean.MaterialSAPBean;
import pe.tgestiona.logistica.inversa.service.ActaDevolucionService;
import pe.tgestiona.logistica.inversa.service.BaseGarantiaService;
import pe.tgestiona.logistica.inversa.service.BaseValidacionExternaService;
import pe.tgestiona.logistica.inversa.service.ConfiguracionService;
import pe.tgestiona.logistica.inversa.service.FichaDevolucionService;
import pe.tgestiona.logistica.inversa.service.MaterialSAPService;
import pe.tgestiona.logistica.inversa.service.MotivoDevolucionService;
import pe.tgestiona.logistica.inversa.service.ValidacionService;


@Controller
public class TransaccionesController {
	
	@Autowired
	private FichaDevolucionService fichaDevolucionService;
	@Autowired
	private MaterialSAPService materialSAPService;
	@Autowired
	private ValidacionService validacionService;
	@Autowired
	private MotivoDevolucionService motivoDevolucionService;
	@Autowired
	private BaseGarantiaService baseGarantiaService;
	@Autowired
	private ActaDevolucionService actaDevolucionService;
	@Autowired
	private BaseValidacionExternaService baseValidacionExternaService;
	@Autowired
	private ConfiguracionService configuracionService;
	
	@RequestMapping(value = "mensaje", method = RequestMethod.GET)
	public String mensaje() {
		return "mensajePopUp";
	}

	@RequestMapping(value = "transacciones", method = RequestMethod.GET)
	public String abrirNuevo() {
		return "transacciones/transacciones";
	}
	
	@RequestMapping(value = "asignarFechaDFPL", method = RequestMethod.GET)
	public String asignarFechaDFPL() {
		return "transacciones/asignarFechaDFPL";
	}
	
	@RequestMapping(value = "asignarFechaRecojo", method = RequestMethod.GET)
	public String asignarFechaRecojo() {
		return "transacciones/asignarFechaRecojo";
	}

	@RequestMapping(value = "inicioProceso", method = RequestMethod.GET)
	public String irCargaFichero() {
		return "transacciones/procesarActa";
	}
	
	@RequestMapping(value = "procesoPendiente", method = RequestMethod.GET)
	public String listaFicheroPendientes(Model model,HttpServletRequest request) {
		return "transacciones/procesarActaPendiente";
	}
	
	@RequestMapping(value = "asignarPedidoEntrega", method = RequestMethod.GET)
	public String asignarPedidoEntrega() {
		return "transacciones/asignarPedidoEntrega";
	}
	
	
	@RequestMapping(value = "registroActa", method = RequestMethod.GET)
	public String registroActa() {
		return "transacciones/registrarActa";
	}

	@RequestMapping(value = "migrid", method = RequestMethod.GET)
	public String myGrid() {
		return "transacciones/grid";
	}

	
	@RequestMapping(value = "anularFicha", method = RequestMethod.GET)
	public String anularFicha() {
		return "transacciones/anularFicha";
	}
	
	
	@RequestMapping(value = "cargaFicheroSAP", method = RequestMethod.GET)
	public String irCargaFicheroSAP() {
		return "transacciones/cargaFicheroSAP";
	}
	
	@RequestMapping(value = "configuracion", method = RequestMethod.GET)
	public String configuracion() {
		return "transacciones/configuracionSistema";
	}
	
	
	//Este el nuevo procesar
	@RequestMapping(value="procesarValidacion",method=RequestMethod.POST)
	public String procesarValidacion(@ModelAttribute("uploadForm") FileUploadForm uploadForm,HttpServletRequest req,HttpServletResponse res,Model model) throws IOException{
		String mensajeProceso=null;
		HttpSession sesion = req.getSession();		
		String usuario=(String)sesion.getAttribute("usuario");
		String formato=req.getParameter("formato");		
		String tickets=req.getParameter("nroTicket");		
		String nroTickets[]=tickets.split(",");

		long time_start, time_end;

        List<MultipartFile> files = uploadForm.getFiles();        
        List<String> fileNames = new ArrayList<String>();        
        List<InputStream> lstInputStream=new ArrayList<InputStream>();
        
		InputStream excelInputStream=null;
        if(null != files && files.size() > 0) {
            for (MultipartFile multipartFile : files) {
            	try {
					excelInputStream=multipartFile.getInputStream();
	                String fileName = multipartFile.getOriginalFilename();
	                fileNames.add(fileName);
	                lstInputStream.add(excelInputStream);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
        
        if(usuario!=null){
        	usuario=usuario.toLowerCase();
    		materialSAPService.eliminaDatosSapSeriado(usuario);
    		baseGarantiaService.eliminaDatosBaseGarantia(usuario);
    		
    		List<MaterialSAPBean> listaSAP6_0=null;
    		List<MaterialSAPBean> listaSAP4_7=null;
    		
    		//En el caso el formato que elijamos sea NO SERIADOS (SOBRESTOCK Y RESIDUOS OPERATIVOS)
    		if(formato.equals(ConstantesGenerales.TipoFormatoDevolucion.NOSERIADOS.getTipoValor())){
    			if(fileNames.get(0).trim().length()>0){
					if(fileNames.get(0).endsWith(".xls")){
	    				listaSAP6_0=materialSAPService.leerarchivoSAPNoSeriado(lstInputStream.get(0),ConstantesGenerales.EXCEL_XLS);
					}
					
					if(fileNames.get(0).endsWith(".xlsx")){
	    				listaSAP6_0=materialSAPService.leerarchivoSAPNoSeriado(lstInputStream.get(0),ConstantesGenerales.EXCEL_XLSX);
					}
    			} 
    			
    			if(fileNames.get(1).trim().length()>0){
					if(fileNames.get(1).endsWith(".xls")){
						listaSAP4_7=materialSAPService.leerarchivoSAPNoSeriado(lstInputStream.get(1),ConstantesGenerales.EXCEL_XLS);
					}
					
					if(fileNames.get(1).endsWith(".xlsx")){
						listaSAP4_7=materialSAPService.leerarchivoSAPNoSeriado(lstInputStream.get(1),ConstantesGenerales.EXCEL_XLSX);
					}
    			}
    			
    			List<ErroresBean> lstErrores_6_0=new ArrayList<ErroresBean>();
    			List<ErroresBean> lstErrores_4_7=new ArrayList<ErroresBean>();
    			
    			List<String> lstCantidadCampos60=new ArrayList<String>();
    			List<String> lstCantidadCampos47=new ArrayList<String>();
    			
    			List<String> lstLongitudCodigoMaterial60=new ArrayList<String>();
    			List<String> lstLongitudCodigoMaterial47=new ArrayList<String>();
    			
    			if(listaSAP6_0!=null){
    				lstErrores_6_0=materialSAPService.obtenerTiposErroresSAP(listaSAP6_0, ConstantesGenerales.SAP_6_0,false);			
    			}

    			if(listaSAP4_7!=null){
    				lstErrores_4_7=materialSAPService.obtenerTiposErroresSAP(listaSAP4_7, ConstantesGenerales.SAP_4_7,false);			
    			}
    			
    			//En el caso los archivos SAP tuvieran un error como la cantidad de campos SAP y Longitud de Campo Material
    			if(lstErrores_6_0.size()>0 || lstErrores_4_7.size()>0){
    	    		String tbErrores="<h1><font face='Verdana, Arial, Helvetica, sans-serif' color='red' style='text-align:left;'><strong>Observaciones</strong></font></h1><br>";    		
    	    		tbErrores=tbErrores + "<table style='width: 100%; margin: 0 auto;' border='1' class='ficheroTabla'>"; 
    				tbErrores=tbErrores + "<tr><th style='width:5%;'></th><th>Tipo de Error</th><th>Detalle del Error</th></tr>";
    				if(lstErrores_6_0.size()>0){
    					for(int i=0;i<lstErrores_6_0.size();i++){
    			    		if(lstErrores_6_0.get(i).getCodError().equals(ConstantesGenerales.TipoErroresSAP.CANTIDADCAMPOS.getTipoValor())){    				
    			    			tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores_6_0.get(i).getNomError() + "</td><td style='text-align:left;'>";
    			    			lstCantidadCampos60=materialSAPService.validarCantidadCamposNoSeriado(listaSAP6_0);
    			    			for(int j=0;j<lstCantidadCampos60.size();j++){
    				    			tbErrores=tbErrores + "<strong>*</strong>" + lstCantidadCampos60.get(j) + "<br>";
    			    			}
    			    				
    			    				tbErrores=tbErrores + "</td></tr>";	    	    		
    			    		}
    			    		
    			    		
    			    		if(lstErrores_6_0.get(i).getCodError().equals(ConstantesGenerales.TipoErroresSAP.LONGITUDCAMPOMATERIAL.getTipoValor())){    				
    			    			tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores_6_0.get(i).getNomError() + "</td><td style='text-align:left;'>";
    			    			lstLongitudCodigoMaterial60=materialSAPService.validarLongitudCodigoMaterial(listaSAP6_0, ConstantesGenerales.SAP_6_0);
    			    			for(int j=0;j<lstLongitudCodigoMaterial60.size();j++){
    				    			tbErrores=tbErrores + "<strong>*</strong>" + lstLongitudCodigoMaterial60.get(j) + "<br>";
    			    			}
    			    				
    			    				tbErrores=tbErrores + "</td></tr>";	    	    		
    			    		}
    					}
    				}

    				if(lstErrores_4_7.size()>0){
    					for(int i=0;i<lstErrores_4_7.size();i++){
    			    		if(lstErrores_4_7.get(i).getCodError().equals(ConstantesGenerales.TipoErroresSAP.CANTIDADCAMPOS.getTipoValor())){    				
    			    			tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores_4_7.get(i).getNomError() + "</td><td style='text-align:left;'>";
    			    			lstCantidadCampos47=materialSAPService.validarCantidadCamposNoSeriado(listaSAP4_7);
    			    			for(int j=0;j<lstCantidadCampos47.size();j++){
    				    			tbErrores=tbErrores + "<strong>*</strong>" + lstCantidadCampos47.get(j) + "<br>";
    			    			}
    			    				
    			    				tbErrores=tbErrores + "</td></tr>";	    	    		
    			    		}
    			    		
    			    		
    			    		if(lstErrores_4_7.get(i).getCodError().equals(ConstantesGenerales.TipoErroresSAP.LONGITUDCAMPOMATERIAL.getTipoValor())){    				
    			    			tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores_4_7.get(i).getNomError() + "</td><td style='text-align:left;'>";
    			    			lstLongitudCodigoMaterial47=materialSAPService.validarLongitudCodigoMaterial(listaSAP4_7, ConstantesGenerales.SAP_4_7);
    			    			for(int j=0;j<lstLongitudCodigoMaterial47.size();j++){
    				    			tbErrores=tbErrores + "<strong>*</strong>" + lstLongitudCodigoMaterial47.get(j) + "<br>";
    			    			}
    			    				
    			    				tbErrores=tbErrores + "</td></tr>";	

    			    		}
    					}
    				}
    				
    				tbErrores=tbErrores+"</table><br>"; 
    				tbErrores=tbErrores + "<p align='center'><a href='javascript:history.back();'>Volver</a></p>";
    				
    				model.addAttribute("message", tbErrores);
    	    		return "mensajeErrores";
    			}else{//En el caso no encuentre observaciones grabamos en la tabla Material SAP para los No Seriados
    				time_start = System.currentTimeMillis();
    				if(listaSAP6_0!=null){
    					if(listaSAP6_0.size()>0 && listaSAP6_0.get(0).getCodMaterial()!=null){
    						materialSAPService.grabarMaterialSAPNoSeriado(listaSAP6_0,ConstantesGenerales.SAP_6_0,usuario);
    					}					
    				}
    				
    				if(listaSAP4_7!=null){
    					if(listaSAP4_7.size()>0 && listaSAP4_7.get(0).getCodMaterial()!=null){
    						materialSAPService.grabarMaterialSAPNoSeriado(listaSAP4_7,ConstantesGenerales.SAP_4_7,usuario);
    					}	
    				}	
    				
    				List<FichaDevolucionBean> lstFichaDevol=fichaDevolucionService.listFichaDevolucion(nroTickets);
    				List<DetalleFichaDevolucionBean> lstDetalleFicha=null;
    				lstDetalleFicha=validacionService.validarItems(lstFichaDevol,ConstantesGenerales.EstadoValidacionActa.SINVALIDAR.getTipoValor(),usuario);
    				fichaDevolucionService.actualizarDetalleFichaDevolucion(lstDetalleFicha,ConstantesGenerales.EstadoValidacionActa.SINVALIDAR.getTipoValor(),ConstantesGenerales.MotivoDevolucionLiquidacion.SOBRESTOCK.getTipoValor());
    				fichaDevolucionService.actualizarDetalleFichaDevolucionDevoluci(lstFichaDevol);
    				fichaDevolucionService.actualizarFichaDevolucion(lstFichaDevol);
    				List<String> lstObtenerResumen=fichaDevolucionService.obtenerResumenValidacion(nroTickets);
    				List<FichaDevolucionBean> lstFichaDevolFuente=actaDevolucionService.obtenerFichasFuenteActas(lstFichaDevol);
    				actaDevolucionService.generarActaDevolucion(lstFichaDevolFuente);
    				time_end = System.currentTimeMillis();
    				
    				String cabecera="<table style='width: 100%; margin: 0 auto;' border='1' class='ficheroTabla' align='center'><tr><th>Ticket</th><th>Motivo Devol.</th><th>Entidad</th><th>Aprob.</th><th>Rechaz.</th><th>Pend.</th><th>Total</th></tr>";
    				String pie="</table>";
    				String valoresTicket="";
    				for(int i=0;i<lstObtenerResumen.size();i++){
    					valoresTicket=valoresTicket+lstObtenerResumen.get(i);
    				}
    				
    				int milisegundos=(int)time_end-(int)time_start;
    				
    			    long hours = TimeUnit.MILLISECONDS.toHours(milisegundos);
    			    milisegundos -= TimeUnit.HOURS.toMillis(hours);
    			    long minutes = TimeUnit.MILLISECONDS.toMinutes(milisegundos);
    			    milisegundos -= TimeUnit.MINUTES.toMillis(minutes);
    			    long seconds = TimeUnit.MILLISECONDS.toSeconds(milisegundos);

    			    StringBuilder sb = new StringBuilder(64);
    			    sb.append(hours);
    			    sb.append(" Hora(s) ");
    			    sb.append(minutes);
    			    sb.append(" Minuto(s) ");
    			    sb.append(seconds);
    			    sb.append(" Segundo(s)");
    			     
    				mensajeProceso="<table width='75%' border='0' align='center' cellpadding='0' cellspacing='0' bgcolor='#ffffff' id='contenido-box'>" + 
    								"<tr><td height='30' bgcolor='#10B9D0'><div align='center'>" + 
    								"<strong><font color='#333333' face='Verdana, Arial, Helvetica, sans-serif'>TGestiona Logistica Inversa</font></strong></div></td>" + 
    								"</tr>" + 		
    								"<tr>" + 
    								"<td bgcolor='#f8f8ea'><table width='80%' border='0' align='center' cellpadding='0' cellspacing='0'>" + 
    								"<tr>" + 
    								"<td style='text-align:left;'>&nbsp;</td>" + 
    								"</tr>" + 
    								"<tr>" + 
    								"<td style='text-align:left;'>&nbsp;</td>" + 
    								"</tr>" + 
    								"<tr>" + 
    								"<td style='text-align:left;'><font size='5'>" + 
    								"<font face='Verdana, Arial, Helvetica, sans-serif' color='green' style='text-align:left;'><strong>Validacion Exitosa</strong><br>" + 
    								"</strong><br>" + 
    								"<br>" + 
    								"</font></font><font size='2' face='Verdana, Arial, Helvetica, sans-serif'>" + 
    								"</font>" + 
    								"<p style='text-align:left;'><font size='2' face='Verdana, Arial, Helvetica, sans-serif'><strong>Se han validado los siguientes Ticket(s):</strong></font></p>" +
    									cabecera + valoresTicket + pie +             		
    								"</td>" + 
	    			        	   "<tr>" +
	    			        	   "<td style='text-align:left;'>&nbsp;</td>" +
	    			        	   "</tr>" +
	    			        	   "<tr><td style='text-align:left;'>Tiempo Estimado: " + sb + "</td></tr>" +
	    			        	   "<tr>" +
	    			        	   "<td style='text-align:left;'>&nbsp;</td>" +
	    			        	   "</tr>" +
	    			        	   "</table></td>" + 
	    			        	   "</tr>" +
	    			        	   "<tr>" + 
	    			        	   "<td height='30' align='center' bgcolor='#E9F3F8'><span><font size='2' face='Verdana, Arial, Helvetica, sans-serif'>TGestiona - Logistica Inversa - Derechos Reservados 2015</font></span></td>" + 
	    			        	   "</tr>" +
	    			        	   "<tr>" + 
	    			        	   "<td height='30' align='center' bgcolor='#E9F3F8'><span><font size='2' face='Verdana, Arial, Helvetica, sans-serif'><a href='transacciones.do'>Volver</a></font></span></td>" + 
	    			        	   "</tr>"+
	    			        	   "</table>";
    				
    					model.addAttribute("message", mensajeProceso);
    					return "mensaje";
    				
    			}
    			
    		}else{//En el caso el formato que elijamos sea SERIADOS
    			if(fileNames.get(0).trim().length()>0){
    				if(fileNames.get(0).endsWith(".xls")){
        				listaSAP6_0=materialSAPService.leerarchivoSAPSeriado(lstInputStream.get(0),ConstantesGenerales.EXCEL_XLS);
    				}
    				
    				if(fileNames.get(0).endsWith(".xlsx")){
        				listaSAP6_0=materialSAPService.leerarchivoSAPSeriado(lstInputStream.get(0),ConstantesGenerales.EXCEL_XLSX);
    				}
    				
    			} 
    			
    			if(fileNames.get(1).trim().length()>0){
    				if(fileNames.get(1).endsWith(".xls")){
    					listaSAP4_7=materialSAPService.leerarchivoSAPSeriado(lstInputStream.get(1),ConstantesGenerales.EXCEL_XLS);
    				}
    				
    				if(fileNames.get(1).endsWith(".xlsx")){
    					listaSAP4_7=materialSAPService.leerarchivoSAPSeriado(lstInputStream.get(1),ConstantesGenerales.EXCEL_XLSX);
    				}
    			}
    			
    			List<ErroresBean> lstErrores_6_0=new ArrayList<ErroresBean>();
    			List<ErroresBean> lstErrores_4_7=new ArrayList<ErroresBean>();
    			
    			List<String> lstCantidadCampos60=new ArrayList<String>();
    			List<String> lstCantidadCampos47=new ArrayList<String>();
    			
    			List<String> lstLongitudCodigoMaterial60=new ArrayList<String>();
    			List<String> lstLongitudCodigoMaterial47=new ArrayList<String>();

    			if(listaSAP6_0!=null){
    				lstErrores_6_0=materialSAPService.obtenerTiposErroresSAP(listaSAP6_0, ConstantesGenerales.SAP_6_0,true);			
    			}

    			if(listaSAP4_7!=null){
    				lstErrores_4_7=materialSAPService.obtenerTiposErroresSAP(listaSAP4_7, ConstantesGenerales.SAP_4_7,true);			
    			}
    			
    			//En el caso haya observaciones en los archivos SAP
    			if(lstErrores_6_0.size()>0 || lstErrores_4_7.size()>0){
    	    		String tbErrores="<h1><font face='Verdana, Arial, Helvetica, sans-serif' color='red' style='text-align:center;'><strong>Observaciones</strong></font></h1><br>";    		
    	    		tbErrores=tbErrores + "<table style='width: 100%; margin: 0 auto;' border='1' class='ficheroTabla'>"; 
    				tbErrores=tbErrores + "<tr><th style='width:5%;'></th><th>Tipo de Error</th><th>Detalle del Error</th></tr>";
    				if(lstErrores_6_0.size()>0){
    					for(int i=0;i<lstErrores_6_0.size();i++){
    			    		if(lstErrores_6_0.get(i).getCodError().equals(ConstantesGenerales.TipoErroresSAP.CANTIDADCAMPOS.getTipoValor())){    				
    			    			tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores_6_0.get(i).getNomError() + "</td><td style='text-align:left;'>";
    			    			lstCantidadCampos60=materialSAPService.validarCantidadCamposSeriado(listaSAP6_0);
    			    			for(int j=0;j<lstCantidadCampos60.size();j++){
    				    			tbErrores=tbErrores + "<strong>*</strong>" + lstCantidadCampos60.get(j) + "<br>";
    			    			}
    			    				
    			    				tbErrores=tbErrores + "</td></tr>";	    	    		
    			    		}
    			    		
    			    		
    			    		if(lstErrores_6_0.get(i).getCodError().equals(ConstantesGenerales.TipoErroresSAP.LONGITUDCAMPOMATERIAL.getTipoValor())){    				
    			    			tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores_6_0.get(i).getNomError() + "</td><td style='text-align:left;'>";
    			    			lstLongitudCodigoMaterial60=materialSAPService.validarLongitudCodigoMaterial(listaSAP6_0, ConstantesGenerales.SAP_6_0);
    			    			for(int j=0;j<lstLongitudCodigoMaterial60.size();j++){
    				    			tbErrores=tbErrores + "<strong>*</strong>" + lstLongitudCodigoMaterial60.get(j) + "<br>";
    			    			}
    			    				
    			    				tbErrores=tbErrores + "</td></tr>";	    	    		
    			    		}
    					}
    				}

    				if(lstErrores_4_7.size()>0){
    					for(int i=0;i<lstErrores_4_7.size();i++){
    			    		if(lstErrores_4_7.get(i).getCodError().equals(ConstantesGenerales.TipoErroresSAP.CANTIDADCAMPOS.getTipoValor())){    				
    			    			tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores_4_7.get(i).getNomError() + "</td><td style='text-align:left;'>";
    			    			lstCantidadCampos47=materialSAPService.validarCantidadCamposSeriado(listaSAP4_7);
    			    			for(int j=0;j<lstCantidadCampos47.size();j++){
    				    			tbErrores=tbErrores + "<strong>*</strong>" + lstCantidadCampos47.get(j) + "<br>";
    			    			}
    			    				
    			    				tbErrores=tbErrores + "</td></tr>";	    	    		
    			    		}
    			    		
    			    		
    			    		if(lstErrores_4_7.get(i).getCodError().equals(ConstantesGenerales.TipoErroresSAP.LONGITUDCAMPOMATERIAL.getTipoValor())){    				
    			    			tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores_4_7.get(i).getNomError() + "</td><td style='text-align:left;'>";
    			    			lstLongitudCodigoMaterial47=materialSAPService.validarLongitudCodigoMaterial(listaSAP4_7, ConstantesGenerales.SAP_4_7);
    			    			for(int j=0;j<lstLongitudCodigoMaterial47.size();j++){
    				    			tbErrores=tbErrores + "<strong>*</strong>" + lstLongitudCodigoMaterial47.get(j) + "<br>";
    			    			}
    			    				
    			    				tbErrores=tbErrores + "</td></tr>";	

    			    		}
    					}
    				}
    				
    				tbErrores=tbErrores+"</table><br>"; 
    				tbErrores=tbErrores + "<p align='center'><a href='javascript:history.back();'>Volver</a></p>";
    				
    				model.addAttribute("message", tbErrores);
    	    		return "mensajeErrores";
    			}else{
    				
    				time_start = System.currentTimeMillis();
    				
    				if(listaSAP6_0!=null){
    					if(listaSAP6_0.size()>0 && listaSAP6_0.get(0).getCodMaterial()!=null){
    						materialSAPService.grabarMaterialSAPSeriado(listaSAP6_0,ConstantesGenerales.SAP_6_0,usuario);
    					}					
    				}
    				
    				if(listaSAP4_7!=null){
    					if(listaSAP4_7.size()>0 && listaSAP4_7.get(0).getCodMaterial()!=null){
    						materialSAPService.grabarMaterialSAPSeriado(listaSAP4_7,ConstantesGenerales.SAP_4_7,usuario);
    					}	
    				}	
    				
    				List<FichaDevolucionBean> lstFichaDevol=fichaDevolucionService.listFichaDevolucion(nroTickets);
    				List<DetalleFichaDevolucionBean> lstDetalleFicha=null;    				
    				lstDetalleFicha=validacionService.validarItems(lstFichaDevol,ConstantesGenerales.EstadoValidacionActa.SINVALIDAR.getTipoValor(),usuario);
    				fichaDevolucionService.actualizarDetalleFichaDevolucion(lstDetalleFicha,ConstantesGenerales.EstadoValidacionActa.SINVALIDAR.getTipoValor(),"");				
    				fichaDevolucionService.actualizarDetalleFichaDevolucionDevoluci(lstFichaDevol);
    				fichaDevolucionService.actualizarFichaDevolucion(lstFichaDevol);
    				fichaDevolucionService.actualizarDetallexStatusxAcopio(lstDetalleFicha, lstFichaDevol);    	
    				fichaDevolucionService.actualizarFichaDevolucion(lstFichaDevol);
    				List<String> lstObtenerResumen=fichaDevolucionService.obtenerResumenValidacion(nroTickets);
    				List<FichaDevolucionBean> lstFichaDevolFuente=actaDevolucionService.obtenerFichasFuenteActas(lstFichaDevol);    				
    				actaDevolucionService.generarActaDevolucion(lstFichaDevolFuente);
    					
    				time_end = System.currentTimeMillis();

    				String cabecera="<table style='width: 100%; margin: 0 auto;' border='1' class='ficheroTabla' align='center'><tr><th>Ticket</th><th>Motivo Devol.</th><th>Entidad</th><th>Aprob.</th><th>Rechaz.</th><th>Pend.</th><th>Total</th></tr>";
    				String pie="</table>";
    				String valoresTicket="";
    				for(int i=0;i<lstObtenerResumen.size();i++){
    					valoresTicket=valoresTicket+lstObtenerResumen.get(i);
    				}
    				
    				int milisegundos=(int)time_end-(int)time_start;
    				
    			     long hours = TimeUnit.MILLISECONDS.toHours(milisegundos);
    			     milisegundos -= TimeUnit.HOURS.toMillis(hours);
    			     long minutes = TimeUnit.MILLISECONDS.toMinutes(milisegundos);
    			     milisegundos -= TimeUnit.MINUTES.toMillis(minutes);
    			     long seconds = TimeUnit.MILLISECONDS.toSeconds(milisegundos);

    			     StringBuilder sb = new StringBuilder(64);
    			     sb.append(hours);
    			     sb.append(" Hora(s) ");
    			     sb.append(minutes);
    			     sb.append(" Minuto(s) ");
    			     sb.append(seconds);
    			     sb.append(" Segundo(s)");

    				mensajeProceso="<table width='75%' border='0' align='center' cellpadding='0' cellspacing='0' bgcolor='#ffffff' id='contenido-box'>" + 
    	        			   	   "<tr><td height='30' bgcolor='#10B9D0'><div align='center'>" + 
    	        			   	   "<strong><font color='#333333' face='Verdana, Arial, Helvetica, sans-serif'>TGestiona Logistica Inversa</font></strong></div></td>" + 
    	        			   	   "</tr>" + 		
    				        	   "<tr>" + 
    				        	   "<td bgcolor='#f8f8ea'><table width='80%' border='0' align='center' cellpadding='0' cellspacing='0'>" + 
    				        	   "<tr>" + 
    				        	   "<td style='text-align:left;'>&nbsp;</td>" + 
    				        	   "</tr>" + 
    				        	   "<tr>" + 
    				        	   "<td style='text-align:left;'>&nbsp;</td>" + 
    				        	   "</tr>" + 
    				        	   "<tr>" + 
    				        	   "<td style='text-align:left;'><font size='5'>" + 
    				        	   "<font face='Verdana, Arial, Helvetica, sans-serif' color='green' style='text-align:left;'><strong>Validacion Exitosa</strong><br>" + 
    				        	   "</strong><br>" + 
    				        	   "<br>" + 
    				        	   "</font></font><font size='2' face='Verdana, Arial, Helvetica, sans-serif'>" + 
    				        	   "</font>" + 
    				        	   "<p style='text-align:left;'><font size='2' face='Verdana, Arial, Helvetica, sans-serif'><strong>Se han validado los siguientes Ticket(s):</strong></font></p>" +
    				        	   	cabecera + valoresTicket + pie +             		
    				        	   "</td>" + 
    				        	   "<tr>" +
    				        	   "<td style='text-align:left;'>&nbsp;</td>" +
    				        	   "</tr>" +
    				        	   "<tr><td style='text-align:left;'>Tiempo Estimado: " + sb + "</td></tr>" +
    				        	   "<tr>" +
    				        	   "<td style='text-align:left;'>&nbsp;</td>" +
    				        	   "</tr>" +
    				        	   "</table></td>" + 
    				        	   "</tr>" +
    				        	   "<tr>" + 
    				        	   "<td height='30' align='center' bgcolor='#E9F3F8'><span><font size='2' face='Verdana, Arial, Helvetica, sans-serif'>TGestiona - Logistica Inversa - Derechos Reservados 2015</font></span></td>" + 
    				        	   "</tr>" +
    				        	   "<tr>" + 
    				         	   "<td height='30' align='center' bgcolor='#E9F3F8'><span><font size='2' face='Verdana, Arial, Helvetica, sans-serif'><a href='javascript:history.back();'>Volver</a></font></span></td>" + 
    				         	   "</tr>"+
    				         	   "</table>";
    				
    								model.addAttribute("message", mensajeProceso);
    								return "mensaje";	
    			}
    			
    		}		
        }else{
        	return "login";
        }

		
	}	
				
	@RequestMapping(value = "procesarValidacionPendiente", method = RequestMethod.POST)
	public String procesarValidacionPendiente(@ModelAttribute("uploadForm") FileUploadForm uploadForm,HttpServletRequest req,HttpServletResponse res, Model model) throws IOException, BiffException{
		String mensajeProceso=null;
		HttpSession sesion = req.getSession();
		String usuario=(String)sesion.getAttribute("usuario");
		String formato=req.getParameter("formato");
		String tickets=req.getParameter("nroTicket");		
		String nroTickets[]=tickets.split(",");
		long time_start, time_end;
		List<MultipartFile> files = uploadForm.getFiles();
		List<String> fileNames = new ArrayList<String>();
		List<InputStream> lstInputStream=new ArrayList<InputStream>();
		
		InputStream excelInputStream=null;
		
		if(null != files && files.size() > 0) {
            for (MultipartFile multipartFile : files) {
            	try {
					excelInputStream=multipartFile.getInputStream();
	                String fileName = multipartFile.getOriginalFilename();
	                fileNames.add(fileName);
	                lstInputStream.add(excelInputStream);
				} catch (IOException e) {
					e.printStackTrace();
				}

            }
        }
		
		
		if(usuario!=null){
			usuario=usuario.toLowerCase();
			materialSAPService.eliminaDatosSapSeriado(usuario);
			baseGarantiaService.eliminaDatosBaseGarantia(usuario);
			baseValidacionExternaService.eliminaDatosBaseValidacionExterna();
			
			List<MaterialSAPBean> listaSAP6_0=null;
			List<MaterialSAPBean> listaSAP4_7=null;
			List<BaseGarantiaBean> lstBaseGarantia=null;
			List<BaseValidacionExternaBean> lstBaseValidacionExterna=null;
			
			if(formato.equals(ConstantesGenerales.TipoFormatoDevolucion.NOSERIADOS.getTipoValor())){
				if(fileNames.get(0).trim().length()>0){
					if(fileNames.get(0).endsWith(".xls")){
	    				listaSAP6_0=materialSAPService.leerarchivoSAPNoSeriado(lstInputStream.get(0),ConstantesGenerales.EXCEL_XLS);
					}
					
					if(fileNames.get(0).endsWith(".xlsx")){
	    				listaSAP6_0=materialSAPService.leerarchivoSAPNoSeriado(lstInputStream.get(0),ConstantesGenerales.EXCEL_XLSX);
					}
				} 
				
				if(fileNames.get(1).trim().length()>0){
					if(fileNames.get(1).endsWith(".xls")){
						listaSAP4_7=materialSAPService.leerarchivoSAPNoSeriado(lstInputStream.get(1),ConstantesGenerales.EXCEL_XLS);
					}
					
					if(fileNames.get(1).endsWith(".xlsx")){
						listaSAP4_7=materialSAPService.leerarchivoSAPNoSeriado(lstInputStream.get(1),ConstantesGenerales.EXCEL_XLSX);
					}
				}
				
				
				
				List<ErroresBean> lstErrores_6_0=new ArrayList<ErroresBean>();
				List<ErroresBean> lstErrores_4_7=new ArrayList<ErroresBean>();
				
				List<String> lstCantidadCampos60=new ArrayList<String>();
				List<String> lstCantidadCampos47=new ArrayList<String>();
				
				List<String> lstLongitudCodigoMaterial60=new ArrayList<String>();
				List<String> lstLongitudCodigoMaterial47=new ArrayList<String>();
				
				if(listaSAP6_0!=null){
					lstErrores_6_0=materialSAPService.obtenerTiposErroresSAP(listaSAP6_0, ConstantesGenerales.SAP_6_0,false);			
				}

				if(listaSAP4_7!=null){
					lstErrores_4_7=materialSAPService.obtenerTiposErroresSAP(listaSAP4_7, ConstantesGenerales.SAP_4_7,false);			
				}
				
				if(lstErrores_6_0.size()>0 || lstErrores_4_7.size()>0){
		    		String tbErrores="<h1><font face='Verdana, Arial, Helvetica, sans-serif' color='red' style='text-align:center;'><strong>Observaciones</strong></font></h1><br>";    		
		    		tbErrores=tbErrores + "<table style='width: 100%; margin: 0 auto;' border='1' class='ficheroTabla'>"; 
					tbErrores=tbErrores + "<tr><th style='width:5%;'></th><th>Tipo de Error</th><th>Detalle del Error</th></tr>";
					if(lstErrores_6_0.size()>0){
						for(int i=0;i<lstErrores_6_0.size();i++){
				    		if(lstErrores_6_0.get(i).getCodError().equals(ConstantesGenerales.TipoErroresSAP.CANTIDADCAMPOS.getTipoValor())){    				
				    			tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores_6_0.get(i).getNomError() + "</td><td style='text-align:left;'>";
				    			lstCantidadCampos60=materialSAPService.validarCantidadCamposNoSeriado(listaSAP6_0);
				    			for(int j=0;j<lstCantidadCampos60.size();j++){
					    			tbErrores=tbErrores + "<strong>*</strong>" + lstCantidadCampos60.get(j) + "<br>";
				    			}
				    				
				    				tbErrores=tbErrores + "</td></tr>";	    	    		
				    		}
				    		
				    		
				    		if(lstErrores_6_0.get(i).getCodError().equals(ConstantesGenerales.TipoErroresSAP.LONGITUDCAMPOMATERIAL.getTipoValor())){    				
				    			tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores_6_0.get(i).getNomError() + "</td><td style='text-align:left;'>";
				    			lstLongitudCodigoMaterial60=materialSAPService.validarLongitudCodigoMaterial(listaSAP6_0, ConstantesGenerales.SAP_6_0);
				    			for(int j=0;j<lstLongitudCodigoMaterial60.size();j++){
					    			tbErrores=tbErrores + "<strong>*</strong>" + lstLongitudCodigoMaterial60.get(j) + "<br>";
				    			}
				    				
				    				tbErrores=tbErrores + "</td></tr>";	    	    		
				    		}
						}
					}

					if(lstErrores_4_7.size()>0){
						for(int i=0;i<lstErrores_4_7.size();i++){
				    		if(lstErrores_4_7.get(i).getCodError().equals(ConstantesGenerales.TipoErroresSAP.CANTIDADCAMPOS.getTipoValor())){    				
				    			tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores_4_7.get(i).getNomError() + "</td><td style='text-align:left;'>";
				    			lstCantidadCampos47=materialSAPService.validarCantidadCamposNoSeriado(listaSAP4_7);
				    			for(int j=0;j<lstCantidadCampos47.size();j++){
					    			tbErrores=tbErrores + "<strong>*</strong>" + lstCantidadCampos47.get(j) + "<br>";
				    			}
				    				
				    				tbErrores=tbErrores + "</td></tr>";	    	    		
				    		}
				    		
				    		
				    		if(lstErrores_4_7.get(i).getCodError().equals(ConstantesGenerales.TipoErroresSAP.LONGITUDCAMPOMATERIAL.getTipoValor())){    				
				    			tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores_4_7.get(i).getNomError() + "</td><td style='text-align:left;'>";
				    			lstLongitudCodigoMaterial47=materialSAPService.validarLongitudCodigoMaterial(listaSAP4_7, ConstantesGenerales.SAP_4_7);
				    			for(int j=0;j<lstLongitudCodigoMaterial47.size();j++){
					    			tbErrores=tbErrores + "<strong>*</strong>" + lstLongitudCodigoMaterial47.get(j) + "<br>";
				    			}
				    				
				    				tbErrores=tbErrores + "</td></tr>";	

				    		}
						}
					}
					
					tbErrores=tbErrores+"</table><br>"; 
					tbErrores=tbErrores + "<p align='center'><a href='javascript:history.back();'>Volver</a></p>";
					
					model.addAttribute("message", tbErrores);
		    		return "mensajeErrores";
				}else{
					time_start = System.currentTimeMillis();
					if(listaSAP6_0!=null){
						if(listaSAP6_0.size()>0 && listaSAP6_0.get(0).getCodMaterial()!=null){
							materialSAPService.grabarMaterialSAPNoSeriado(listaSAP6_0,ConstantesGenerales.SAP_6_0,usuario);
						}					
					}
					
					if(listaSAP4_7!=null){
						if(listaSAP4_7.size()>0 && listaSAP4_7.get(0).getCodMaterial()!=null){
							materialSAPService.grabarMaterialSAPNoSeriado(listaSAP4_7,ConstantesGenerales.SAP_4_7,usuario);
						}	
					}	
					
					List<FichaDevolucionBean> lstFichaDevol=fichaDevolucionService.listFichaDevolucion(nroTickets);
					List<DetalleFichaDevolucionBean> lstDetalleFicha=null;
					lstDetalleFicha=validacionService.validarItems(lstFichaDevol,ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor(),usuario);
					fichaDevolucionService.actualizarDetalleFichaDevolucion(lstDetalleFicha,ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor(),ConstantesGenerales.MotivoDevolucionLiquidacion.SOBRESTOCK.getTipoValor());
					fichaDevolucionService.actualizarDetalleFichaDevolucionDevoluci(lstFichaDevol);					
					fichaDevolucionService.actualizarFichaDevolucion(lstFichaDevol);
					List<String> lstObtenerResumen=fichaDevolucionService.obtenerResumenValidacion(nroTickets);
					List<FichaDevolucionBean> lstFichaDevolFuente=actaDevolucionService.obtenerFichasFuenteActas(lstFichaDevol);
					actaDevolucionService.generarActaDevolucion(lstFichaDevolFuente);
					time_end = System.currentTimeMillis();
					
					String cabecera="<table style='width: 100%; margin: 0 auto;' border='1' class='ficheroTabla' align='center'><tr><th>Ticket</th><th>Motivo Devol.</th><th>Entidad</th><th>Aprob.</th><th>Rechaz.</th><th>Pend.</th><th>Total</th></tr>";
					String pie="</table>";
					String valoresTicket="";
					for(int i=0;i<lstObtenerResumen.size();i++){
						valoresTicket=valoresTicket+lstObtenerResumen.get(i);
					}
					
					
					int milisegundos=(int)time_end-(int)time_start;
					
				     long hours = TimeUnit.MILLISECONDS.toHours(milisegundos);
				     milisegundos -= TimeUnit.HOURS.toMillis(hours);
				     long minutes = TimeUnit.MILLISECONDS.toMinutes(milisegundos);
				     milisegundos -= TimeUnit.MINUTES.toMillis(minutes);
				     long seconds = TimeUnit.MILLISECONDS.toSeconds(milisegundos);

				     StringBuilder sb = new StringBuilder(64);
				     sb.append(hours);
				     sb.append(" Hora(s) ");
				     sb.append(minutes);
				     sb.append(" Minuto(s) ");
				     sb.append(seconds);
				     sb.append(" Segundo(s)");

					
					mensajeProceso="<table width='65%' border='0' align='center' cellpadding='0' cellspacing='0' bgcolor='#ffffff' id='contenido-box'>" + 
		     			   "<tr><td height='30' bgcolor='#10B9D0'><div align='center'>" + 
		     			   "<strong><font color='#333333' face='Verdana, Arial, Helvetica, sans-serif'>TGestiona Logistica Inversa</font></strong></div></td>" + 
				        	   "</tr>" + 		
				        	   "<tr>" + 
				        	   "<td bgcolor='#f8f8ea'><table width='80%' border='0' align='center' cellpadding='0' cellspacing='0'>" + 
				        	   "<tr>" + 
				        	   "<td style='text-align:left;'>&nbsp;</td>" + 
				        	   "</tr>" + 
				        	   "<tr>" + 
				        	   "<td  style='text-align:left;'>&nbsp;</td>" + 
				        	   "</tr>" + 
				        	   "<tr>" + 
				        	   "<td style='text-align:left;'><font size='5'>" + 
				        	   "<font face='Verdana, Arial, Helvetica, sans-serif' color='green' style='text-align:left;'><strong>Revalidacion Exitosa</strong><br>" + 
				        	   "</strong><br>" + 
				        	   "<br>" + 
				        	   "</font></font><font size='2' face='Verdana, Arial, Helvetica, sans-serif'>" + 
				        	   "</font>" + 
				        	   "<p style='text-align:left;'><font size='2' face='Verdana, Arial, Helvetica, sans-serif'><strong>Se han revalidado los siguientes Ticket(s):</strong></font></p>" +
				        	   	cabecera + valoresTicket + pie +             		
				        	   "</td>" + 
				        	   "<tr>" +
				        	   "<td style='text-align:left;'>&nbsp;</td>" +
				        	   "</tr>" +
				        	   "<tr><td style='text-align:left;'>Tiempo Estimado: " + sb + "</td></tr>" +
				        	   "<tr>" +
				        	   "<td style='text-align:left;'>&nbsp;</td>" +
				        	   "</tr>" +
				        	   "</table></td>" + 
				        	   "</tr>" +
				        	   "<tr>" + 
				        	   "<td height='30' align='center' bgcolor='#E9F3F8'><span><font size='2' face='Verdana, Arial, Helvetica, sans-serif'>TGestiona - Logistica Inversa - Derechos Reservados 2015</font></span></td>" + 
				        	   "</tr>" +
				        	   "<tr>" + 
				        	   "<td height='30' align='center' bgcolor='#E9F3F8'><span><font size='2' face='Verdana, Arial, Helvetica, sans-serif'><a href='transacciones.do'>Volver</a></font></span></td>" + 
				        	   "</tr>"+
				        	   "</table>";
					
						model.addAttribute("message", mensajeProceso);
						return "mensaje";
					
				}
				
			}else{
				if(fileNames.get(0).trim().length()>0){
					if(fileNames.get(0).endsWith(".xls")){
						listaSAP6_0=materialSAPService.leerarchivoSAPSeriado(lstInputStream.get(0),ConstantesGenerales.EXCEL_XLS);
					}
					
					if(fileNames.get(0).endsWith(".xlsx")){
						listaSAP6_0=materialSAPService.leerarchivoSAPSeriado(lstInputStream.get(0),ConstantesGenerales.EXCEL_XLSX);
					}
				} 
				
				if(fileNames.get(1).trim().length()>0){
					if(fileNames.get(1).endsWith(".xls")){
						listaSAP4_7=materialSAPService.leerarchivoSAPSeriado(lstInputStream.get(1),ConstantesGenerales.EXCEL_XLS);
					}
					
					if(fileNames.get(1).endsWith(".xlsx")){
						listaSAP4_7=materialSAPService.leerarchivoSAPSeriado(lstInputStream.get(1),ConstantesGenerales.EXCEL_XLSX);
					}
				}
			}
			
			if(fileNames.get(2).trim().length()>0){
				lstBaseGarantia=baseGarantiaService.leerarchivoExcelValidarPendientes(lstInputStream.get(2));
			}

			if(fileNames.get(3).trim().length()>0){
				lstBaseValidacionExterna=baseValidacionExternaService.leerarchivoExcelValidacionExterna(lstInputStream.get(3));
			}
			
			List<ErroresBean> lstErrores_6_0=new ArrayList<ErroresBean>();
			List<ErroresBean> lstErrores_4_7=new ArrayList<ErroresBean>();
			
			List<String> lstCantidadCampos60=new ArrayList<String>();
			List<String> lstCantidadCampos47=new ArrayList<String>();
			
			List<String> lstLongitudCodigoMaterial60=new ArrayList<String>();
			List<String> lstLongitudCodigoMaterial47=new ArrayList<String>();

			if(listaSAP6_0!=null){
				lstErrores_6_0=materialSAPService.obtenerTiposErroresSAP(listaSAP6_0, ConstantesGenerales.SAP_6_0,true);			
			}
			
			if(listaSAP4_7!=null){
				lstErrores_4_7=materialSAPService.obtenerTiposErroresSAP(listaSAP4_7, ConstantesGenerales.SAP_4_7,true);			
			}
			
			if(lstErrores_6_0.size()>0 || lstErrores_4_7.size()>0){
	    		String tbErrores="<h1><font face='Verdana, Arial, Helvetica, sans-serif' color='red' style='text-align:center;'><strong>Observaciones</strong></font></h1><br>";    		
	    		tbErrores=tbErrores + "<table style='width: 100%; margin: 0 auto;' border='1' class='ficheroTabla'>"; 
				tbErrores=tbErrores + "<tr><th style='width:5%;'></th><th>Tipo de Error</th><th>Detalle del Error</th></tr>";
				if(lstErrores_6_0.size()>0){
					for(int i=0;i<lstErrores_6_0.size();i++){
			    		if(lstErrores_6_0.get(i).getCodError().equals(ConstantesGenerales.TipoErroresSAP.CANTIDADCAMPOS.getTipoValor())){    				
			    			tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores_6_0.get(i).getNomError() + "</td><td style='text-align:left;'>";
			    			lstCantidadCampos60=materialSAPService.validarCantidadCamposSeriado(listaSAP6_0);
			    			for(int j=0;j<lstCantidadCampos60.size();j++){
				    			tbErrores=tbErrores + "<strong>*</strong>" + lstCantidadCampos60.get(j) + "<br>";
			    			}
			    				
			    				tbErrores=tbErrores + "</td></tr>";	    	    		
			    		}
			    		
			    		
			    		if(lstErrores_6_0.get(i).getCodError().equals(ConstantesGenerales.TipoErroresSAP.LONGITUDCAMPOMATERIAL.getTipoValor())){    				
			    			tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores_6_0.get(i).getNomError() + "</td><td style='text-align:left;'>";
			    			lstLongitudCodigoMaterial60=materialSAPService.validarLongitudCodigoMaterial(listaSAP6_0, ConstantesGenerales.SAP_6_0);
			    			for(int j=0;j<lstLongitudCodigoMaterial60.size();j++){
				    			tbErrores=tbErrores + "<strong>*</strong>" + lstLongitudCodigoMaterial60.get(j) + "<br>";
			    			}
			    				
			    				tbErrores=tbErrores + "</td></tr>";	    	    		
			    		}
					}
				}

				if(lstErrores_4_7.size()>0){
					for(int i=0;i<lstErrores_4_7.size();i++){
			    		if(lstErrores_4_7.get(i).getCodError().equals(ConstantesGenerales.TipoErroresSAP.CANTIDADCAMPOS.getTipoValor())){    				
			    			tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores_4_7.get(i).getNomError() + "</td><td style='text-align:left;'>";
			    			lstCantidadCampos47=materialSAPService.validarCantidadCamposSeriado(listaSAP4_7);
			    			for(int j=0;j<lstCantidadCampos47.size();j++){
				    			tbErrores=tbErrores + "<strong>*</strong>" + lstCantidadCampos47.get(j) + "<br>";
			    			}
			    				
			    				tbErrores=tbErrores + "</td></tr>";	    	    		
			    		}
			    		
			    		
			    		if(lstErrores_4_7.get(i).getCodError().equals(ConstantesGenerales.TipoErroresSAP.LONGITUDCAMPOMATERIAL.getTipoValor())){    				
			    			tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores_4_7.get(i).getNomError() + "</td><td style='text-align:left;'>";
			    			lstLongitudCodigoMaterial47=materialSAPService.validarLongitudCodigoMaterial(listaSAP4_7, ConstantesGenerales.SAP_4_7);
			    			for(int j=0;j<lstLongitudCodigoMaterial47.size();j++){
				    			tbErrores=tbErrores + "<strong>*</strong>" + lstLongitudCodigoMaterial47.get(j) + "<br>";
			    			}
			    				
			    				tbErrores=tbErrores + "</td></tr>";	

			    		}
					}
				}
				
				tbErrores=tbErrores+"</table><br>"; 
				tbErrores=tbErrores + "<p align='center'><a href='javascript:history.back();'>Volver</a></p>";
				
				model.addAttribute("message", tbErrores);
	    		return "mensajeErrores";
			}else{
				time_start = System.currentTimeMillis();
				
				if(listaSAP6_0!=null){
					if(listaSAP6_0.size()>0 && listaSAP6_0.get(0).getCodMaterial()!=null){
						materialSAPService.grabarMaterialSAPSeriado(listaSAP6_0,ConstantesGenerales.SAP_6_0,usuario);
					}				
				}

				if(listaSAP4_7!=null){
					if(listaSAP4_7.size()>0 && listaSAP4_7.get(0).getCodMaterial()!=null){
						materialSAPService.grabarMaterialSAPSeriado(listaSAP4_7,ConstantesGenerales.SAP_4_7,usuario);
					}			
				}	
				
				if(lstBaseGarantia!=null){
					baseGarantiaService.grabarBaseGarantia(lstBaseGarantia, usuario);
				}	

				if(lstBaseValidacionExterna!=null){
					baseValidacionExternaService.grabarBaseValidacionExterna(lstBaseValidacionExterna, usuario);
				}	
				
				List<FichaDevolucionBean> lstFichaDevol=fichaDevolucionService.listFichaDevolucion(nroTickets);
				List<DetalleFichaDevolucionBean> lstDetalleFicha=validacionService.validarItems(lstFichaDevol,ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor(),usuario);
				fichaDevolucionService.actualizarDetalleFichaDevolucion(lstDetalleFicha,ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor(),"");
				fichaDevolucionService.actualizarDetalleFichaDevolucionDevoluci(lstFichaDevol);
				fichaDevolucionService.actualizarFichaDevolucion(lstFichaDevol);
				List<String> lstObtenerResumen=fichaDevolucionService.obtenerResumenValidacion(nroTickets);
				List<FichaDevolucionBean> lstFichaDevolFuente=actaDevolucionService.obtenerFichasFuenteActas(lstFichaDevol);
				actaDevolucionService.generarActaDevolucion(lstFichaDevolFuente);

				time_end = System.currentTimeMillis();
				
				String cabecera="<table style='width: 100%; margin: 0 auto;' border='1' class='ficheroTabla' align='center'><tr><th>Ticket</th><th>Motivo Devol.</th><th>Entidad</th><th>Aprob.</th><th>Rechaz.</th><th>Pend.</th><th>Total</th></tr>";
				String pie="</table>";
				String valoresTicket="";
				for(int i=0;i<lstObtenerResumen.size();i++){
					valoresTicket=valoresTicket+lstObtenerResumen.get(i);
				}
				
				int milisegundos=(int)time_end-(int)time_start;
				
			     long hours = TimeUnit.MILLISECONDS.toHours(milisegundos);
			     milisegundos -= TimeUnit.HOURS.toMillis(hours);
			     long minutes = TimeUnit.MILLISECONDS.toMinutes(milisegundos);
			     milisegundos -= TimeUnit.MINUTES.toMillis(minutes);
			     long seconds = TimeUnit.MILLISECONDS.toSeconds(milisegundos);

			     StringBuilder sb = new StringBuilder(64);
			     sb.append(hours);
			     sb.append(" Hora(s) ");
			     sb.append(minutes);
			     sb.append(" Minuto(s) ");
			     sb.append(seconds);
			     sb.append(" Segundo(s)");
				
				mensajeProceso="<table width='75%' border='0' align='center' cellpadding='0' cellspacing='0' bgcolor='#ffffff' id='contenido-box'>" + 
		        			   "<tr><td height='30' bgcolor='#10B9D0'><div align='center'>" + 
		        			   "<strong><font color='#333333' face='Verdana, Arial, Helvetica, sans-serif'>TGestiona Logistica Inversa</font></strong></div></td>" + 
				        	   "</tr>" + 		
				        	   "<tr>" + 
				        	   "<td bgcolor='#f8f8ea'><table width='80%' border='0' align='center' cellpadding='0' cellspacing='0'>" + 
				        	   "<tr>" + 
				        	   "<td style='text-align:left;'>&nbsp;</td>" + 
				        	   "</tr>" + 
				        	   "<tr>" + 
				        	   "<td style='text-align:left;'>&nbsp;</td>" + 
				        	   "</tr>" + 
				        	   "<tr>" + 
				        	   "<td style='text-align:left;'><font size='5'>" + 
				        	   "<font face='Verdana, Arial, Helvetica, sans-serif' color='green' style='text-align:left;'><strong>Revalidación Exitosa</strong><br>" + 
				        	   "</strong><br>" + 
				        	   "<br>" + 
				        	   "</font></font><font size='2' face='Verdana, Arial, Helvetica, sans-serif'>" + 
				        	   "</font>" + 
				        	   "<p style='text-align:left;'><font size='2' face='Verdana, Arial, Helvetica, sans-serif'><strong>Se han validado los siguientes Ticket(s):</strong></font></p>" +
				        	   	cabecera + valoresTicket + pie +             		
				        	   "</td>" + 
				        	   "<tr>" +
				        	   "<td style='text-align:left;'>&nbsp;</td>" +
				        	   "</tr>" +
				        	   "<tr><td style='text-align:left;'>Tiempo Estimado: " + sb + "</td></tr>" +
				        	   "<tr>" +
				        	   "<td style='text-align:left;'>&nbsp;</td>" +
				        	   "</tr>" +
				        	   "</table></td>" + 
				        	   "</tr>" +
				        	   "<tr>" + 
				        	   "<td height='30' align='center' bgcolor='#E9F3F8'><span><font size='2' face='Verdana, Arial, Helvetica, sans-serif'>TGestiona - Logistica Inversa - Derechos Reservados 2015</font></span></td>" + 
				        	   "</tr>" +
				        	   "<tr>" + 
				         	   "<td height='30' align='center' bgcolor='#E9F3F8'><span><font size='2' face='Verdana, Arial, Helvetica, sans-serif'><a href='transacciones.do'>Volver</a></font></span></td>" + 
				         	   "</tr>"+
				         	   "</table>";

							model.addAttribute("message", mensajeProceso);
							return "mensaje";
			}
		}else{
			return "login";
		}
		

		
	}	
	
	@ResponseBody @RequestMapping(value = "descargaFicheroExcel", method = RequestMethod.GET,params="accion=descarga")
	public String descargaFicheroExcel(HttpServletRequest request,HttpServletResponse response, Model model) throws Exception {
		
	try 	
	{
		String nroTicket=request.getParameter("nroTicket");
	    byte [] outArray = fichaDevolucionService.descargaExcel(nroTicket);
	    response.setContentType("application/ms-excel");
	    response.setContentLength(outArray.length);
	    response.setHeader("Expires:", "0"); // eliminates browser caching
	    response.setHeader("Content-Disposition", "attachment; filename=transacciones.xls");
	    OutputStream outStream = response.getOutputStream();
	    outStream.write(outArray);
	    outStream.flush();
	}finally {

     }
	return "";
}
	
	@ResponseBody @RequestMapping(value = "descargaSeriesPendienteXls", method = RequestMethod.GET,params="accion=descarga")
	public String descargaSeriesPendienteXls(HttpServletRequest request,HttpServletResponse response, Model model) throws Exception {
		
	try 	
	{
		String nroTicket=request.getParameter("nroTicket");
	    byte [] outArray = fichaDevolucionService.descargaSeriesPendiente(nroTicket);
	    response.setContentType("application/ms-excel");
	    response.setContentLength(outArray.length);
	    response.setHeader("Expires:", "0"); // eliminates browser caching
	    response.setHeader("Content-Disposition", "attachment; filename=validacionPendiente.xls");
	    OutputStream outStream = response.getOutputStream();
	    outStream.write(outArray);
	    outStream.flush();
	}finally {

     }
		return "";
	}
	
	@ResponseBody @RequestMapping(value = "descargaSeriesPendienteGarantia", method = RequestMethod.GET,params="accion=descarga")
	public String descargaSeriesPendienteGarantia(HttpServletRequest request,HttpServletResponse response, Model model) throws Exception {
		
	try 	
	{
		String nroTicket=request.getParameter("nroTicket");
	    byte [] outArray = fichaDevolucionService.descargaSeriesPendienteGarantia(nroTicket);
	    response.setContentType("application/ms-excel");
	    response.setContentLength(outArray.length);
	    response.setHeader("Expires:", "0"); // eliminates browser caching
	    response.setHeader("Content-Disposition", "attachment; filename=validacionPendienteGarantia.xls");
	    OutputStream outStream = response.getOutputStream();
	    outStream.write(outArray);
	    outStream.flush();
	}finally {

     }
		return "";
	}
	
	@ResponseBody @RequestMapping(value = "descargaSeriesPendienteValidacionExterna", method = RequestMethod.GET,params="accion=descarga")
	public String descargaSeriesPendienteValidacionExterna(HttpServletRequest request,HttpServletResponse response, Model model) throws Exception {
		
	try 	
	{
		String nroTicket=request.getParameter("nroTicket");
	    byte [] outArray = fichaDevolucionService.descargaSeriesPendienteValidacionExterna(nroTicket);
	    response.setContentType("application/ms-excel");
	    response.setContentLength(outArray.length);
	    response.setHeader("Expires:", "0"); // eliminates browser caching
	    response.setHeader("Content-Disposition", "attachment; filename=validacionPendienteExterna.xls");
	    OutputStream outStream = response.getOutputStream();
	    outStream.write(outArray);
	    outStream.flush();
	}finally {

     }
		return "";
	}

}
