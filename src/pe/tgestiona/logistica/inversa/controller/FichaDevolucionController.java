package pe.tgestiona.logistica.inversa.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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

import pe.tgestiona.logistica.inversa.bean.ConstantesGenerales;
import pe.tgestiona.logistica.inversa.bean.DetalleFichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.ErroresBean;
import pe.tgestiona.logistica.inversa.bean.FichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.FileUploadForm;
import pe.tgestiona.logistica.inversa.service.FichaDevolucionService;
import pe.tgestiona.logistica.inversa.service.ValidacionService;
import pe.tgestiona.logistica.inversa.util.Util;

@Controller
public class FichaDevolucionController {

	@Autowired
	private FichaDevolucionService fichaDevolucionService;
	@Autowired
	private ValidacionService validacionService;
	
	@ResponseBody @RequestMapping(value = "descargaSeriesXls", method = RequestMethod.GET,params="accion=descarga")
	public String descargaFicheroXls(HttpServletRequest request,HttpServletResponse response, Model model) throws Exception {
		
	try 	
	{
		String tickets=request.getParameter("nroTicket");
	    byte [] outArray = fichaDevolucionService.descargarSeries(tickets);
	    response.setContentType("application/ms-excel");
	    response.setContentLength(outArray.length);
	    response.setHeader("Expires:", "0"); // eliminates browser caching
	    response.setHeader("Content-Disposition", "attachment; filename=series.xls");
	    OutputStream outStream = response.getOutputStream();
	    outStream.write(outArray);
	    outStream.flush();
	}finally {

     }
		return "";
	}
	
	@ResponseBody @RequestMapping(value = "descargaMaterialXls", method = RequestMethod.GET,params="accion=descarga")
	public String descargaMaterialXls(HttpServletRequest request,HttpServletResponse response, Model model) throws Exception {
		
	try 	
	{
		String tickets=request.getParameter("nroTicket");
	    byte [] outArray = fichaDevolucionService.descargarMaterial(tickets);
	    response.setContentType("application/ms-excel");
	    response.setContentLength(outArray.length);
	    response.setHeader("Expires:", "0"); // eliminates browser caching
	    response.setHeader("Content-Disposition", "attachment; filename=materiales.xls");
	    OutputStream outStream = response.getOutputStream();
	    outStream.write(outArray);
	    outStream.flush();
	}finally {

     }
		return "";
	}

	
	@ResponseBody @RequestMapping(value = "descargaPlantillaXls", method = RequestMethod.GET,params="accion=descarga")
	public String descargaPlantillaXls(HttpServletRequest request,HttpServletResponse response, Model model) throws Exception {
		
	try 	
	{
	    byte [] outArray = fichaDevolucionService.descargarPlantillaxls();
	    response.setContentType("application/ms-excel");
	    response.setContentLength(outArray.length);
	    response.setHeader("Expires:", "0"); // eliminates browser caching
	    response.setHeader("Content-Disposition", "attachment; filename=plantillaCarga.xls");
	    OutputStream outStream = response.getOutputStream();
	    outStream.write(outArray);
	    outStream.flush();
	}finally {

     }
		return "";
	}

		
	@ResponseBody @RequestMapping(value = "listaTicketSinProcesar")
	public List<FichaDevolucionBean> listaTicketSinProcesar(HttpServletRequest request) {
		List<FichaDevolucionBean> list = null;
		String formato=request.getParameter("formato");
		String usuario = request.getParameter("h_usuario");
		if(usuario!=null){
			usuario=usuario.toLowerCase();
		}	
		list=fichaDevolucionService.listTicketSinProcesar(formato,usuario);
		
		return list;
	}
	
	
	@ResponseBody @RequestMapping(value = "listaTicketSinProcesarFechas")
	public List<FichaDevolucionBean> listaTicketSinProcesarFechas(HttpServletRequest request) {
		List<FichaDevolucionBean> list = null;
		String formato=request.getParameter("formato");
		String fecha1=request.getParameter("fcorreo1");
		String fecha2=request.getParameter("fcorreo2");
		String usuario = request.getParameter("h_usuario");
		if(usuario!=null){
			usuario=usuario.toLowerCase();
		}	
		list=fichaDevolucionService.listTicketSinProcesarFechas(formato,fecha1,fecha2,usuario);
		return list;
	}
	
	
	
	@ResponseBody @RequestMapping(value = "listaTicketPendiente")
	public List<FichaDevolucionBean> listaTicketPendiente(HttpServletRequest request) {
		List<FichaDevolucionBean> list = null;
		String formato=request.getParameter("formato");
		String usuario = request.getParameter("h_usuario");
		if(usuario!=null){
			usuario=usuario.toLowerCase();
		}	
		list=fichaDevolucionService.listTicketPendiente(formato,usuario);
		return list;
	}
	

	@ResponseBody @RequestMapping(value = "listaTicketPendienteFechas")
	public List<FichaDevolucionBean> listaTicketPendienteFechas(HttpServletRequest request) {
		List<FichaDevolucionBean> list = null;
		String formato=request.getParameter("formato");
		String fecha1=request.getParameter("fcorreo1");
		String fecha2=request.getParameter("fcorreo2");
		String usuario = request.getParameter("h_usuario");
		if(usuario!=null){
			usuario=usuario.toLowerCase();
		}	
		list=fichaDevolucionService.listTicketPendienteFechas(formato,fecha1,fecha2,usuario);
		return list;
	}
	
	@ResponseBody @RequestMapping(value = "getResumenValidacion")
	public List<String> getResumenValidacion(HttpServletRequest request) {
		List<String> listResumenValidacion=new ArrayList<String>();		
		String ticket=request.getParameter("tickets[]");		
		System.out.println(ticket);
		listResumenValidacion=fichaDevolucionService.obtenerResumenValidacionMsg(ticket);		
		return listResumenValidacion;
	}
	

	@ResponseBody @RequestMapping(value = "getTicketAnular")
	public FichaDevolucionBean getTicketAnular(HttpServletRequest request) {
		FichaDevolucionBean fichaDevolucionBean = null;
		String nroTicket=request.getParameter("t_ticket");
		fichaDevolucionBean=fichaDevolucionService.obtenerFichaDevolucionAnular(nroTicket);
		return fichaDevolucionBean;
	}
	
	
	@RequestMapping(value = "anularTicket", method = RequestMethod.POST)
	public String anularTicket(HttpServletRequest request,Model model) {
		HttpSession sesion = request.getSession();
		String mensaje=null;
        String mySesion =(String)sesion.getAttribute("issubmit");        
		String nroTicket=request.getParameter("t_ticket");
		String observaciones=request.getParameter("t_observaciones");	
        if (mySesion!=null){
    		fichaDevolucionService.anularFichaDevolucion(nroTicket, observaciones);   
            sesion.removeAttribute("issubmit");
            mensaje="<table border='0' align='center' cellpadding='0' cellspacing='0' bgcolor='#ffffff' id='contenido-box'>" + 
            		"<tr><td height='30' bgcolor='#10B9D0'><div align='center'>" + 
            		"<strong><font color='#333333' face='Verdana, Arial, Helvetica, sans-serif'>TGestiona Logistica Inversa</font></strong></div></td>" + 
            		"</tr>" + 		
            		"<tr>" + 
            		"<td bgcolor='#f8f8ea'><table width='80%' border='0' align='center' cellpadding='0' cellspacing='0'>" + 
            		"<tr>" + 
            		"<td>&nbsp;</td>" + 
            		"</tr>" + 
            		"<tr>" + 
            		"<td align='right'>&nbsp;</td>" + 
            		"</tr>" + 
            		"<tr>" + 
            		"<td><font size='4'>" + 
            		"<font face='Verdana, Arial, Helvetica, sans-serif' color='green' style='text-align:center;'><strong>Anulación Exitosa</strong><br>" + 
            		"</strong><br>" + 
            		"</font></font>" + 
            		"<p style='text-align:left;'><font size='2' face='Verdana, Arial, Helvetica, sans-serif'>Se ha anulado el Nro de Ticket:<strong>" + nroTicket + "</strong></font></p>" + 
            		"</td>" + 
            		"</tr>" + 
            		"<tr>" +
            		"<td>&nbsp;</td>" +
            		"</tr>" +
            		"</table></td>" + 
            		"</tr>" +
            		"<tr>" + 
            		"<td height='30' align='center' bgcolor='#E9F3F8'><span><font size='2' face='Verdana, Arial, Helvetica, sans-serif'>TGestiona - Logistica Inversa - Derechos Reservados 2015</font></span></td>" + 
		        	"</tr>" +
		        	"<tr>" + 
		        	"<td height='30' align='center' bgcolor='#E9F3F8'><span><font size='2' face='Verdana, Arial, Helvetica, sans-serif'><a href='anularFicha.do'>Volver</a></font></span></td>" + 
		        	"</tr>"+
		        	"</table>";
            model.addAttribute("message", mensaje);
            return "mensaje";

        }else{
        	return "transacciones/anularFicha";
        }
	}
	

	@RequestMapping(value = "registrarActa", method = RequestMethod.POST)
	public String registraFichero(@ModelAttribute("uploadForm") FileUploadForm uploadForm,HttpServletRequest req,HttpServletResponse res, Model model) throws IOException, BiffException{
		HttpSession sesion = req.getSession();
		Util util=new Util();
		String nroTicket=null;
		String mensaje=null;
		String prefijo=null;
		String fechaDevolucion=null;
        String mySesion =(String)sesion.getAttribute("issubmit");        
		String devolucion = req.getParameter("h_devolucion");
		String nomdevolucion = req.getParameter("h_tdevolucion");		
		String codTipoFormato=req.getParameter("h_tipoFormato");
		String nomTipoFormato=req.getParameter("h_nomtipoFormato");
		String rubroDevoluci=req.getParameter("chkRubroDevoluci");
		String tipoPrefijo=req.getParameter("rdbPrefijo");
		String rubro=req.getParameter("h_Rubro");
		String abrvDevolucion=req.getParameter("h_abrvdevolucion");	
		String entidad = req.getParameter("h_entidad");
		String nomentidad = req.getParameter("h_nomentidad");
		String fCorreo = req.getParameter("t_fCorreo");
		String hCorreo = req.getParameter("t_hCorreo");
		String idCorreoGestor = req.getParameter("h_idCorreoGestor");
		String cboEnviado = req.getParameter("cboEnviado");		
		String cboDestinoFisico = req.getParameter("cboDestinoFisico");
		String usuario = req.getParameter("h_usuario");
		String mesDevolucion=req.getParameter("rdbMesDevolucion");
		String cboMesDevolucion=req.getParameter("cboMesDevolucion");
		String cboAnioDevolucion=req.getParameter("cboAnioDevolucion");
		
		boolean esretaceria=false;
		
		if(rubroDevoluci==null){
			esretaceria=false;
		}else{
			esretaceria=true;
		}
		
		if(mesDevolucion.equals(ConstantesGenerales.NOAPLICA)){
			mesDevolucion=ConstantesGenerales.NOAPLICA;
			fechaDevolucion=ConstantesGenerales.NOAPLICA;
		}else{
			mesDevolucion=cboMesDevolucion + " " + cboAnioDevolucion.substring(2);
			fechaDevolucion="30/" + util.obtenerMesDigitos(cboMesDevolucion) + "/" + cboAnioDevolucion;
		}
		
		if(tipoPrefijo.equals("SER")){
			prefijo="SER";
		}

		if(tipoPrefijo.equals("RUBRO")){
			prefijo=rubro;
		}
		
		prefijo=prefijo + "-" + abrvDevolucion;
		
		
		if(usuario!=null){
			usuario=usuario.toLowerCase();
		}		
		
		String mesanio=fCorreo.substring(3, 5) + fCorreo.substring(8,10);
		
		prefijo=prefijo + "-" + mesanio;
		
		FichaDevolucionBean fichaDevolucionBean=new FichaDevolucionBean();
		fichaDevolucionBean.setTipoDevolucion(devolucion);
		fichaDevolucionBean.setUsuario(usuario);
		fichaDevolucionBean.setEntidad(entidad);
		fichaDevolucionBean.setFechaSolicitud(fCorreo);
		fichaDevolucionBean.setHoraSolicitud(hCorreo);
		fichaDevolucionBean.setEnviado(cboEnviado);
		fichaDevolucionBean.setDestino(cboDestinoFisico);
		fichaDevolucionBean.setGestorActa(idCorreoGestor);
		fichaDevolucionBean.setRetaceria(esretaceria);
		fichaDevolucionBean.setPrefijoActa(prefijo);
		fichaDevolucionBean.setMesDevolucion(mesDevolucion);
		fichaDevolucionBean.setFechaDevolucion(fechaDevolucion);
				
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
		
        fichaDevolucionBean.setNombre(fileNames.get(0).trim());
        
        //Leemos el archivo Detalle y obtenemos la lista de errores
        List<DetalleFichaDevolucionBean> lstDetaFichaDevolucion=fichaDevolucionService.leerarchivoExcel(lstInputStream.get(0));
        List<ErroresBean> lstErrores=new ArrayList<ErroresBean>();
        lstErrores=fichaDevolucionService.obtenerTiposErroresFicha(lstDetaFichaDevolucion,codTipoFormato);
        
        //Si la lista de errores esta llena(Existe Errores) mostramos un mensaje de error y nos redirecciona a la pagina "mensaje"
    	if(lstErrores.size()>0){
    		String tbErrores="<h1><font face='Verdana, Arial, Helvetica, sans-serif' color='red' style='text-align:center;'><strong>Observaciones</strong></font></h1><br>";    		
    		tbErrores=tbErrores + "<table style='width: 100%; margin: 0 auto;' border='1' class='ficheroTabla'>"; 
			tbErrores=tbErrores + "<tr><th style='width:5%;'></th><th>Tipo de Error</th><th>Detalle del Error</th></tr>";
				
	    	for(int i=0;i<lstErrores.size();i++){
	    		
	    		if(lstDetaFichaDevolucion!=null){
		    		if(!devolucion.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.SINSERIE.getTipoValor())){
			    		if(lstErrores.get(i).getCodError().equals(ConstantesGenerales.TipoErroresFicha.CAMPOSVACIOS.getTipoValor())){    				
			    			tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores.get(i).getNomError() + "</td><td style='text-align:left;'>";
			    			List<String> listCamposIncompletos=fichaDevolucionService.validarCamposVacios(lstDetaFichaDevolucion);
			    			for(int j=0;j<listCamposIncompletos.size();j++){
				    			tbErrores=tbErrores + "<strong>* Campo: </strong>" + listCamposIncompletos.get(j) + "<br>";
			    			}
			    				
			    				tbErrores=tbErrores + "</td></tr>";	    	    		
			    		}
		    		}
		    		

		    		if(!devolucion.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.SINSERIE.getTipoValor())){
			    		if(lstErrores.get(i).getCodError().equals(ConstantesGenerales.TipoErroresFicha.CARACTERESSERIE.getTipoValor())){
			    	    	tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores.get(i).getNomError() + "</td><td style='text-align:left;'>";
			    	    	List<String> listCaracteresSerie=fichaDevolucionService.validarSeriesCaracteres(lstDetaFichaDevolucion);
			    	    		
			    			for(int j=0;j<listCaracteresSerie.size();j++){
				    			tbErrores=tbErrores + "<strong>*</strong>" + listCaracteresSerie.get(j) + "<br>";
			    			}	    	    		
			    				tbErrores=tbErrores + "</td></tr>";
			    		}
		    		}
		    		
		    		if(!devolucion.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.SINSERIE.getTipoValor())){
			    		if(lstErrores.get(i).getCodError().equals(ConstantesGenerales.TipoErroresFicha.CONTENIDO.getTipoValor())){
			    	    	tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores.get(i).getNomError() + "</td><td style='text-align:left;'>";
			    	    	List<String> listContenido=fichaDevolucionService.validarContenido(lstDetaFichaDevolucion);
			    	    	
			    			for(int j=0;j<listContenido.size();j++){
				    			tbErrores=tbErrores + "<strong>*</strong>" + listContenido.get(j) + "<br>";
			    			}	    	    		
			    				tbErrores=tbErrores + "</td></tr>";
			    		}
		    		}
		    		
		    		if(!devolucion.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.SINSERIE.getTipoValor())){
			    		if(lstErrores.get(i).getCodError().equals(ConstantesGenerales.TipoErroresFicha.CORRESPONDENCIA.getTipoValor())){
			    	    	tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores.get(i).getNomError() + "</td><td style='text-align:left;'>";
			    	    	List<String> listCorrespondencia=fichaDevolucionService.validarCorrespondencia(lstDetaFichaDevolucion);
			    	    		
			    			for(int j=0;j<listCorrespondencia.size();j++){
				    			tbErrores=tbErrores + "<strong>*</strong>" + listCorrespondencia.get(j) + "<br>";
			    			}	    	    		
			    				tbErrores=tbErrores + "</td></tr>";	    				
			    		}
		    		}	
		    		
		    		if(!devolucion.equals(ConstantesGenerales.MotivoDevolucionLiquidacion.SINSERIE.getTipoValor())){
			    		if(lstErrores.get(i).getCodError().equals(ConstantesGenerales.TipoErroresFicha.CANTIDADCAMPOS.getTipoValor())){
			    	    	tbErrores=tbErrores + "<tr><td style='width:5%;'>" + (i+1) + "</td><td style='width:35%;text-align:left;'>" + lstErrores.get(i).getNomError() + "</td><td style='text-align:left;'>";
			    	    	List<String> listCantidadCampos=fichaDevolucionService.validarCantidadCampos(lstDetaFichaDevolucion);
			    	    		
			    			for(int j=0;j<listCantidadCampos.size();j++){
				    			tbErrores=tbErrores + "<strong>*</strong>" + listCantidadCampos.get(j) + "<br>";
			    			}
			    	    		
			    				tbErrores=tbErrores + "</td></tr>";
			    		}
		    		}		    		
	    		}

    		}
	    		tbErrores=tbErrores+"</table><br>"; 
    			tbErrores=tbErrores + "<p align='center'><a href='inicio.do'>Volver</a></p>";
	    		model.addAttribute("message", tbErrores);
	    		return "mensajeErrores";
			}else{ //Si no contiene errores y la session NO es Nula, registramos la Ficha Devolucion y mostramos un mensaje Resumen
		        if (mySesion!=null){
	            	nroTicket=fichaDevolucionService.grabarCabeceraFichaDevolucion(lstDetaFichaDevolucion.size(),fichaDevolucionBean);
	            	fichaDevolucionService.grabarDetalleFichaDevolucion(nroTicket, lstDetaFichaDevolucion,fichaDevolucionBean);
		            sesion.removeAttribute("issubmit");//Removemos la variable de session evitando el F5 
		            
		            mensaje="<table border='0' align='center' cellpadding='0' cellspacing='0' bgcolor='#ffffff' id='contenido-box'>" + 
		            		"<tr><td height='30' bgcolor='#10B9D0'><div align='center'>" + 
		            		"<strong><font color='#333333' face='Verdana, Arial, Helvetica, sans-serif'>TGestiona Logistica Inversa</font></strong></div></td>" + 
		            		"</tr>" + 		
		            		"<tr>" + 
		            		"<td bgcolor='#f8f8ea'><table width='80%' border='0' align='center' cellpadding='0' cellspacing='0'>" + 
		            		"<tr>" + 
		            		"<td>&nbsp;</td>" + 
		            		"</tr>" + 
		            		"<tr>" + 
		            		"<td align='left'>&nbsp;</td>" + 
		            		"</tr>" + 
		            		"<tr>" + 
		            		"<td style='text-align:left;'><font size='4'>" + 
		            		"<font face='Verdana, Arial, Helvetica, sans-serif' color='green'><strong>Registro Exitoso</strong><br>" + 
		            		"</strong><br>" + 
		            		"</font></font>" + 
		            		"<p style='text-align:left;'><font size='2' face='Verdana, Arial, Helvetica, sans-serif'>Se ha generado el Nro de Ticket:<strong>" + nroTicket + "</strong></font></p>" + 
		            		"<p style='text-align:left;'><font size='2' face='Verdana, Arial, Helvetica, sans-serif'>Con un total de <strong>" + lstDetaFichaDevolucion.size() + " item(s)</strong></font></p>" + 
		            		"<p style='text-align:left;'><font size='2' face='Verdana, Arial, Helvetica, sans-serif'>Para la Entidad: <strong>" + nomentidad + "</strong></font></p>" + 
		            		"<p style='text-align:left;'><font size='2' face='Verdana, Arial, Helvetica, sans-serif'>Motivo de Devolucion: <strong> " + nomTipoFormato + " - " + nomdevolucion + "</strong></font></p>" + 		            		
		            		"</td>" + 
		            		"</tr>" + 
		            		"<tr>" +
		            		"<td>&nbsp;</td>" +
		            		"</tr>" +
		            		"</table></td>" + 
		            		"</tr>" +
		            		"<tr>" + 
		            		"<td height='30' align='center' bgcolor='#E9F3F8'><span><font size='2' face='Verdana, Arial, Helvetica, sans-serif'>TGestiona - Logistica Inversa - Derechos Reservados 2015</font></span></td>" + 
				        	"</tr>" +
				        	"<tr>" + 
				        	"<td height='30' align='center' bgcolor='#E9F3F8'><span><font size='2' face='Verdana, Arial, Helvetica, sans-serif'><a href='registroActa.do'>Volver</a></font></span></td>" + 
				        	"</tr>"+
				        	"</table>";
		            model.addAttribute("message", mensaje);
		            return "mensaje";
		        }else{
		        	return "transacciones/registrarActa";
		        } 

			} 		
    		
	}
	
}
