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

import org.jboss.logging.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import pe.tgestiona.logistica.inversa.bean.ActaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.ConstantesGenerales;
import pe.tgestiona.logistica.inversa.bean.FileUploadForm;
import pe.tgestiona.logistica.inversa.bean.PedidoEntregaBean;
import pe.tgestiona.logistica.inversa.service.ActaDevolucionService;

@Controller
public class ActaDevolucionController {
	
	@Autowired
	private ActaDevolucionService actaDevolucionService;	
	
	@RequestMapping(value = "consultarActaGeneradas", method = RequestMethod.GET)
	public String consultarActaGeneradas() {
		return "transacciones/consultarActaQuery";
	}
	
	@ResponseBody @RequestMapping(value = "lstActasGeneradasHoy")
	public List<ActaDevolucionBean> lstActasGeneradas(HttpServletRequest request) {
		List<ActaDevolucionBean> list = null;
		String formato=request.getParameter("formato");
		list=actaDevolucionService.lstActaDevolucion(formato);
		return list;
	}
	
	@ResponseBody @RequestMapping(value = "lstActasGeneradasFechas")
	public List<ActaDevolucionBean> lstActasGeneradasFechas(HttpServletRequest request) {
		List<ActaDevolucionBean> list = null;
		String formato=request.getParameter("formato");
		String fCorreo1=request.getParameter("fcorreo1");
		String fCorreo2=request.getParameter("fcorreo2");
		list=actaDevolucionService.lstActaDevolucionFechas(formato,fCorreo1,fCorreo2);
		return list;
	}
	
	@ResponseBody @RequestMapping(value = "lstActaGeneradas")
	public List<String[]> lstActaGeneradas(@Param String vActa,@Param String vNroTicket,@Param String vMaterial,@Param String vSerie,@Param String vDescrip,@Param String vEstado,@Param String vF1,@Param String vF2,@Param String vEstadoActa,@Param String vPag,Model model,HttpServletRequest req){
		List<String[]> lstActasGeneradas=null;
		lstActasGeneradas=actaDevolucionService.lstActasGeneradas(vActa, vNroTicket, vMaterial, vSerie, vDescrip, vEstado, vF1, vF2, vEstadoActa, vPag);
		model.addAttribute("lstActaGeneradas",lstActasGeneradas);
		return lstActasGeneradas;
	}

	@ResponseBody @RequestMapping(value = "lstActaGeneradasExcel", method = RequestMethod.GET,params="accion=descarga")
	public String lstActaGeneradasExcel(@Param String vActa,@Param String vNroTicket,@Param String vMaterial,@Param String vSerie,@Param String vDescrip,@Param String vEstado,@Param String vPag,Model model,HttpServletRequest req,HttpServletResponse response) throws IOException{
		try 	
			{
				byte [] outArray=actaDevolucionService.lstActasGeneradasExcel(vActa, vNroTicket, vMaterial, vSerie, vDescrip, vEstado,vPag);
			    response.setContentType("application/ms-excel");
			    response.setContentLength(outArray.length);
			    response.setHeader("Expires:", "0"); // eliminates browser caching
			    response.setHeader("Content-Disposition", "attachment; filename=descargaTrx.xls");
			    OutputStream outStream = response.getOutputStream();
			    outStream.write(outArray);
			    outStream.flush();
			}finally {

		     }
				return "";
		    
	}

	
	@ResponseBody @RequestMapping(value = "listaPedidoEntregaPendiente")
	public List<ActaDevolucionBean> listaPedidoEntregaPendiente(HttpServletRequest request) {
		List<ActaDevolucionBean> list = null;
		String formato=request.getParameter("formato");
		list=actaDevolucionService.lstActaDevolucionPedidoPendiente(formato);
		return list;
	}

	@ResponseBody @RequestMapping(value = "listaRecojoPendiente")
	public List<ActaDevolucionBean> listaRecojoPendiente(HttpServletRequest request) {
		List<ActaDevolucionBean> list = null;
		String formato=request.getParameter("formato");
		list=actaDevolucionService.lstActaDevolucionRecojoPendiente(formato);
		return list;
	}

	
	@ResponseBody @RequestMapping(value = "descargaFuentePedido", method = RequestMethod.GET,params="accion=descarga")
	public String descargaFuentePedido(HttpServletRequest request,HttpServletResponse response, Model model) throws Exception {
		
	try 	
	{
		String nroActas=request.getParameter("nroActas");
		String formato=request.getParameter("formato");
	    byte [] outArray = actaDevolucionService.descargaFuentePedido(nroActas,formato);
	    response.setContentType("application/ms-excel");
	    response.setContentLength(outArray.length);
	    response.setHeader("Expires:", "0"); // eliminates browser caching
	    response.setHeader("Content-Disposition", "attachment; filename=fuentePedido.xls");
	    OutputStream outStream = response.getOutputStream();
	    outStream.write(outArray);
	    outStream.flush();
	}finally {

     }
		return "";
	}
	
	
	@RequestMapping(value = "cargaPedidoEntrega", method = RequestMethod.POST)
	public String cargaPedidoEntregaSeriado(@ModelAttribute("uploadForm") FileUploadForm uploadForm,HttpServletRequest req,HttpServletResponse res, Model model) throws IOException, BiffException{
		HttpSession sesion = req.getSession();
		String mensaje=null;
        String mySesion =(String)sesion.getAttribute("issubmit");   
        String formato=req.getParameter("h_tipoFormato");
		List<MultipartFile> files = uploadForm.getFiles();
		List<InputStream> lstInputStream=new ArrayList<InputStream>();
		List<String> fileNames = new ArrayList<String>();
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
		
        List<PedidoEntregaBean> lstPedidoEntrega=null;
	
		if(fileNames.get(0).trim().length()>0){
			lstPedidoEntrega=actaDevolucionService.leerArchivoPedidoEntrega(lstInputStream.get(0));
		} 
		
		
        if (mySesion!=null){
        	
        	if(formato.equals(ConstantesGenerales.TipoFormatoDevolucion.SERIADOS.getTipoValor())){
        		actaDevolucionService.asignarPedidoEntregaSeriado(lstPedidoEntrega);
        	}

        	if(formato.equals(ConstantesGenerales.TipoFormatoDevolucion.NOSERIADOS.getTipoValor())){
        		actaDevolucionService.asignarPedidoEntregaNoSeriado(lstPedidoEntrega);
        	}

        	
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
            		"<td align='left'>&nbsp;</td>" + 
            		"</tr>" + 
            		"<tr>" + 
            		"<td style='text-align:left;'><font size='4'>" + 
            		"<font face='Verdana, Arial, Helvetica, sans-serif' color='green' style='text-align:left;'><strong>Registro Exitoso</strong><br>" + 
            		"</strong><br>" + 
            		"</font></font>" + 
            		"<p style='text-align:left;'><font size='2' face='Verdana, Arial, Helvetica, sans-serif'>Se ha generado el Pedido y las Entregas para el Ticket:<strong>" + lstPedidoEntrega.get(0).getNroTicket() + "</strong></font></p>" + 
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
	
	@RequestMapping(value = "asignarFechaRecojo", method = RequestMethod.POST)
	public String asignarFechaRecojo(HttpServletRequest request,Model model) {
		HttpSession sesion = request.getSession();
		String mensaje=null;
        String mySesion =(String)sesion.getAttribute("issubmit");        
		String nroTickets=request.getParameter("nroTicket");
		String nroActas=request.getParameter("nroActas");
		String fechaRecojo=request.getParameter("t_fRecojo");
        if (mySesion!=null){
    		actaDevolucionService.asignarFechaRecojo(nroTickets, fechaRecojo);
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
            		"<p style='text-align:left;'><font size='2' face='Verdana, Arial, Helvetica, sans-serif'>Se registro Fecha de Recojo a las Actas:<strong>" + nroActas + "</strong></font></p>" + 
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
		        	"<td height='30' align='center' bgcolor='#E9F3F8'><span><font size='2' face='Verdana, Arial, Helvetica, sans-serif'><a href='asignarFechaRecojo.do'>Volver</a></font></span></td>" + 
		        	"</tr>"+
		        	"</table>";
            model.addAttribute("message", mensaje);
            return "mensaje";

        }else{
        	return "transacciones/asignarFechaRecojo";
        }
	}
	
	
}
