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

import pe.tgestiona.logistica.inversa.bean.DistribucionBean;
import pe.tgestiona.logistica.inversa.bean.FileUploadForm;
import pe.tgestiona.logistica.inversa.service.DistribucionService;


@Controller
public class DistribucionController {
	
	@Autowired
	private DistribucionService distribucionService;
	
	@RequestMapping(value = "cargaDistribucion", method = RequestMethod.GET)
	public String cargaDistribucion() {
		return "distribucion/cargaDistribucion";
	}

	@ResponseBody @RequestMapping(value = "descargaFuenteDistribucion", method = RequestMethod.GET,params="accion=descarga")
	public String descargaSeriesPendienteXls(HttpServletRequest request,HttpServletResponse response, Model model) throws Exception {
		
	try 	
	{
		String acta=request.getParameter("nroActa");
	    byte [] outArray = distribucionService.descargarFuenteDistribucion(acta);
	    response.setContentType("application/ms-excel");
	    response.setContentLength(outArray.length);
	    response.setHeader("Expires:", "0"); // eliminates browser caching
	    response.setHeader("Content-Disposition", "attachment; filename=fuenteDistribucion.xls");
	    OutputStream outStream = response.getOutputStream();
	    outStream.write(outArray);
	    outStream.flush();
	}finally {

     }
		return "";
	}	
	
	@RequestMapping(value = "cargaFuenteDistribucion", method = RequestMethod.POST)
	public String cargaFuenteDistribucion(@ModelAttribute("uploadForm") FileUploadForm uploadForm,HttpServletRequest req,HttpServletResponse res, Model model) throws IOException, BiffException{
		HttpSession sesion = req.getSession();
		String mensaje=null;
        String mySesion =(String)sesion.getAttribute("issubmit");        
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
		
        List<DistribucionBean> lstCargaDistribucion=null;
	
		if(fileNames.get(0).trim().length()>0){
			lstCargaDistribucion=distribucionService.leerarchivoExcel(lstInputStream.get(0));
		} 
		
		
        if (mySesion!=null){
        	distribucionService.grabarCargaDistribucion(lstCargaDistribucion);
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
            		"<font face='Verdana, Arial, Helvetica, sans-serif' color='green' style='text-align:center;'><strong>Registro Exitoso</strong><br>" + 
            		"</strong><br>" + 
            		"</font></font>" + 
            		"<p><font size='2' face='Verdana, Arial, Helvetica, sans-serif'>Se ha cargado la informacion de Distribucion Fisica para el:<br> <strong>Ticket:</strong>" + lstCargaDistribucion.get(0).getNroTicket() + "<br> <strong>Acta:</strong>" + lstCargaDistribucion.get(0).getNroActa() + " </font></p>" + 
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
        	return "distribucion/cargaDistribucion";
        } 
		

	}
	
	
	
}