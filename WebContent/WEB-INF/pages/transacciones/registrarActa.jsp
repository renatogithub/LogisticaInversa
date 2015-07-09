<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page import="pe.tgestiona.logistica.inversa.bean.ConstantesGenerales"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Calendar"%>
<%@page session="true"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
<script src="resources/jquery/jquery-1.9.1.js"></script>
<script src="resources/jquery/jquery-ui.js"></script>
<link rel="stylesheet" href="resources/css/jquery-ui.css" type="text/css">
<link rel="stylesheet" href="resources/css/smart_wizard.css" type="text/css"> 
<link rel="stylesheet" href="resources/css/screen.css" type="text/css" >
<link rel="shortcut icon" href="resources/icon/logo.ico" />
<title>Sistema Integrado - Gestion de Logistica Inversa</title>
<script type="text/javascript" src="resources/js/canal.js"></script>
<script type="text/javascript" src="resources/js/entidad.js?ver=02072015"></script>
<script type="text/javascript" src="resources/js/gestorActa.js?ver=02072015A"></script>
<script type="text/javascript" src="resources/jquery/jquery.smartWizard-2.0.min.js?ver=2015.04.16"></script>
<script type="text/javascript" src="resources/js/registroActa.js?ver=05.07.2015"></script>
<script type="text/javascript" src="resources/jquery/jquery.form.js"></script>
<script type="text/javascript" src="resources/js/jquery.maskedinput.js"></script>
<script type="text/javascript" src="resources/js/jquery.mask.min.js"></script>


<%
    String usuario = "", clave="";
    HttpSession sesionOk = request.getSession();
    if (sesionOk.getAttribute("usuario") == null) {%>
    <jsp:forward page="../login.jsp">
        <jsp:param name="error" value="Es obligatorioidentificarse"/>
    </jsp:forward><% }
    else {
        usuario = (String)sesionOk.getAttribute("usuario");
        clave = (String)sesionOk.getAttribute("clave");
    }    
    sesionOk.setAttribute("issubmit", "1");
%>

</head>

<style type="text/css" >
.button {
	display: inline-block;
	line-height: 1;
	padding: 7px 10px;
	text-decoration: none;
	font-weight: bold;
	color: #fff;
	background-color: #39c;
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	-khtml-border-radius: 5px;
	border-radius: 5px;
}

input.button, button.button {
	border: 0px none;
}

input[type="button"]:disabled {
    background: #dddddd;
}

</style>


<script type="text/javascript">
jQuery(function($){
	$("#fCorreo").mask("99/99/9999",{placeholder:"dd/mm/yyyy"});
	$("#hCorreo").mask("00:00",{placeholder:"hh:mm"});

});
</script>

<body>
	<div id="content">
		<jsp:include page="../titulo.jsp" flush="true" />
		<div id="contenido">
			<div id="dialog" title="Sistema Integrado - Gestión de Logistica Inversa"></div>
			<div id="derecha">
				<table align="center" border="0" cellpadding="0" cellspacing="0"
					width="100%">
					<tr>
						<td>
						 <form:form action="registrarActa.do" method="POST" enctype="multipart/form-data" 
						 class="formCarga" id="formCarga" name="formCarga" modelattribute="uploadForm"> 
								 <!-- Variables Session -->
								 <input type="hidden" name="issubmit" value=<%= session.getAttribute("issubmit") %> id="issubmit" />
								 <input type="hidden" name="h_usuario" id="h_usuario" value=<%= session.getAttribute("usuario") %> />				
								<!-- Tabs -->
								<div id="wizard" class="swMain" style="position:relative;z-index:2;">
								 	<ul>
										<li>
											<a href="#step-1"> 
												<span class="stepNumber">1</span>
												<span class="stepDesc"> Datos del Acta<br/> 
													<small>Seleccione los datos del Acta</small>
												</span>
											</a>
										</li>
										<li>
											<a href="#step-2"> 
												<span class="stepNumber">2</span>
												<span class="stepDesc"> Datos del Punto<br/> 
													<small>Seleccione los datos del Punto</small>
												</span>
											</a>
										</li>
										<li>
											<a href="#step-3"> 
												<span class="stepNumber">3</span>
												<span class="stepDesc"> Carga del Archivo<br/> 
													<small>Seleccione el formato del Acta</small>
												</span>
											</a>
										</li>
									</ul>
								 	<div id="step-1">
								 		<div>
									 		<h2 class="StepTitle" style="background-color: green;color: white;">Registro de Acta</h2>								 	
								 		</div>

										<h2 class="StepTitle">Paso 1: Datos del Acta</h2>
										<br><br>
										<fieldset style="padding: 10px;">
											<legend>
												<strong>Datos del Acta:</strong>
											</legend>
											<strong>*</strong> Seleccione Tipo de Formato: 
											<div id="tipoFormato" style="margin-left:30px;"></div>
											<input type="hidden" name="h_tipoFormato" id="h_tipoFormato" />
											<input type="hidden" name="h_nomtipoFormato" id="h_nomtipoFormato" />
											<strong>*</strong> Seleccione Motivo de Devolucion <select
												id="cboDevolucion" name="cboDevolucion" onchange="obtenerDatosMotivo();">
												<option value="-1" selected>---------------------</option>
											</select> <input type="hidden" name="h_devolucion" id="h_devolucion" />											
											 <input type="hidden" name="h_tdevolucion" id="h_tdevolucion" />											 
 											 <input type="hidden" name="h_abrvdevolucion" id="h_abrvdevolucion" />											 
											<span id="msg_devolucion" style="color: red;"></span> 
											<br />
											<br /> 
											<input type="checkbox" id="chkRubroDevoluci" name="chkRubroDevoluci" value="RUBRODEVOLUCI" style="margin-left:30px;font-size: 3pt;" />Considerar Rubro 'RETACERIA' para 'CABLE' (Solo para los SobreStock)
											<br /><br />
											<strong>*</strong> Fecha del Correo (dd/mm/yyyy): 
											<input type="text" name="t_fCorreo" id="fCorreo" size="8px" onKeyPress="TabKey(event, 'hCorreo')">												

											<span id="msg_fcorreo" style="color: red;"></span>
											<br><br>
											<strong>*</strong> Hora del Correo (hh:mm): 
											<input type="text" name="t_hCorreo" id="hCorreo" size="8px" onKeyPress="TabKey(event, 'rdbMesDevolucion')"/>
											<span id="msg_hcorreo" style="color: red;"></span>
											<br><br>
											<strong>*</strong> Mes Devolucion: <br><br>
											<input type="radio" name="rdbMesDevolucion" id="rdbMesDevolucion" value="MESDEVOLUCION" style="margin-left:30px;font-size: 3pt;" onclick="habilitarMesAnio();" checked="checked" onKeyPress="TabKey(event, 'cboMesDevolucion')">Seleccione Mes:&nbsp;&nbsp;
											<select id="cboMesDevolucion" name="cboMesDevolucion" onKeyPress="TabKey(event, 'cboAnioDevolucion')">
												<option value="" selected>---------------------</option>
												<option value="ENERO">ENERO</option>
												<option value="FEBRERO">FEBRERO</option>
												<option value="MARZO">MARZO</option>
												<option value="ABRIL">ABRIL</option>
												<option value="MAYO">MAYO</option>
												<option value="JUNIO">JUNIO</option>
												<option value="JULIO">JULIO</option>
												<option value="AGOSTO">AGOSTO</option>
												<option value="SETIEMBRE">SETIEMBRE</option>
												<option value="OCTUBRE">OCTUBRE</option>
												<option value="NOVIEMBRE">NOVIEMBRE</option>
												<option value="DICIEMBRE">DICIEMBRE</option>
											</select>
											&nbsp;&nbsp;
											<select id="cboAnioDevolucion" name="cboAnioDevolucion" style="width: 8%;" onKeyPress="TabKey(event, 'cboEnviado')">
												<option value="" selected>---------</option>
												<%
													Calendar fecha = new GregorianCalendar();
													int anioActual = fecha.get(Calendar.YEAR);
													int anioAnterior = fecha.get(Calendar.YEAR) - 1;
													String sAnioActual=String.valueOf(anioActual);
													String sAnioAnterior=String.valueOf(anioAnterior);
													out.println("<option value=" + sAnioActual + ">" + sAnioActual + "</option>"); 
													out.println("<option value=" + sAnioAnterior + ">" + sAnioAnterior + "</option>"); 
												
												%>
																																																																																																												
											</select>
											<span id="msg_MesDevolucion" style="color: red;"></span>
											<span id="msg_AnioDevolucion" style="color: red;"></span>
											<span id="msg_MesAnioDevolucion" style="color: red;"></span>
											<br><br>
											<input type="radio" name="rdbMesDevolucion" id="rdbMesDevolucionNoAplica" value="NOAPLICA" style="margin-left:30px;font-size: 3pt;" onclick="deshabilitarMesAnio();">No Aplica
											
										</fieldset>
										<br />
										<fieldset style="padding: 10px;">
											<legend>
												<strong>Enviado y Destino:</strong>
											</legend>
											<strong>*</strong> Enviado 
											<select id="cboEnviado" name="cboEnviado" onKeyPress="TabKey(event, 'cboDestinoFisico')">
												<option value="-1" selected>---------------------</option>
												<option value="DISTRIBUCION" selected>Distribución Fisica</option>
												<option value="PLANEAMIENTO">Planeamiento</option>
												<option value="RR.OO">Residuos Operativos</option>
											</select>
											<span id="msg_enviado" style="color: red;"></span>
											<br><br>
											<strong>*</strong> Destino Fisico 
											<select id="cboDestinoFisico" name="cboDestinoFisico">
												<option value="-1" selected>---------------------</option>
												<option value="VENEZUELA" selected>Venezuela</option>
												<option value="C.A">Centro de Acopio</option>
												<option value="OP.RR.OO">OP.RR.OO</option>
											</select>
											<span id="msg_destino" style="color: red;"></span>
										</fieldset>
										<br/>
										<fieldset style="padding: 10px;">
											<legend>
												<strong>Prefijo. Codigo de Acta:</strong>
											</legend>
											<input type="radio" name="rdbPrefijo" id="rdbSER" value="SER" checked="checked" onclick="verSER();">SER<br><br>
											<input type="radio" name="rdbPrefijo" id="rdbRubro" value="RUBRO" onclick="verRubros();">Rubro &nbsp;&nbsp;
											<select id="cboRubro" name="cboRubro">
												<option value="-1" selected>---------------------</option>
											</select> 
											<input type="hidden" name="h_Rubro" id="h_Rubro" />	
										</fieldset>
									</div>
								  
								 	<div id="step-2">
								 		<div>
									 		<h2 class="StepTitle" style="background-color: green;color: white;">Registro de Acta</h2>								 	
								 		</div>
								
										<h2 class="StepTitle">Paso 2: Datos del Punto</h2>
										<br><br>
										<fieldset style="padding: 10px;">
											<legend>
												<strong>Datos de la Entidad:</strong>
											</legend>

											<strong>*</strong> Seleccione el Canal <select id="cbo_canal"
												name="cbo_canal">
												<option value="-1" selected>---------------------</option>
											</select> <input type="hidden" name="h_canal" id="h_canal" /> <span
												id="msg_canal" style="color: red;"></span> <br /> <br />
											<strong>*</strong>Seleccione la Entidad: <select id="cbo_entidad"
												name="cbo_entidad">
												<option value="-1" selected>---------------------</option>
											</select><input type="hidden" name="h_entidad" id="h_entidad" />
											<input type="hidden" name="h_nomentidad" id="h_nomentidad" />											
											<span id="msg_entidad" style="color: red;"></span> <br /> <br />
											<strong>*</strong> Direccion: <input type="text"
												name="direccion" id="direccion" disabled="disabled"
												size="70px"> <br />

										</fieldset>

										<br />

										<fieldset style="padding: 10px;">
											<legend>
												<strong>Gestor del Acta:</strong>
											</legend>

											<strong>*</strong> Seleccione el Gestor 
												<select id="cbo_GestorActa" name="cbo_GestorActa">
													<option value="-1" selected>---------------------</option>
												</select><input type="hidden" name="h_idCorreoGestor" id="h_idCorreoGestor" /> 
												<span id="msg_GestorActa" style="color: red;"></span> <br /><br />
											<strong>*</strong> Correo: 
												<input type="text" name="t_correoGestor" id="t_correoGestor" disabled="disabled" size="35px" value=""> <br /> <br />
											<strong>*</strong> Tlf-Anexo: 
												<input type="text" name="t_tlfGestor" id="t_tlfGestor" disabled="disabled" size="25px" value=""> <br /> <br /> 
											<strong>*</strong> RPM: 
												<input type="text" name="t_rpmGestor" id="t_rpmGestor" disabled="disabled" size="25px" value=""> <br /><br />
												
												<input id="btnAddGestor" type="button"  value="Adicionar" class="button" style="margin-left: 20px; width: 100px; height: 30px;" />								
										</fieldset>
										<br />
									</div>
								 
								 	<div id="step-3">
								 		<div>
									 		<h2 class="StepTitle" style="background-color: green;color: white;">Registro de Acta</h2>								 	
								 		</div>
								 	
										<h2 class="StepTitle">Paso 3: Fuente Acta</h2>
										<br><br>
										<div id="registroFuente">
											<fieldset style="padding: 10px;">
												<legend>
													<strong>Carga de Archivo:</strong>
												</legend>
	
												<strong>*</strong> Seleccione archivo excel
												<div class="upload">
													<input type="file" name="files[0]" id="ficheroExcel">
													<span id="msg_fileExcel" style="color: red;"></span> 
												</div>	
														
												<br><br>
												<div style='font-weight: bold;'>
													<a id='miPlantilla' href='descargaPlantillaXls.do?accion=descarga'>Descargar Plantilla de Ingreso</a>		
												</div>																						
											</fieldset>
										</div>
										<div id="loadingmessage" style="text-align: center;display: none;"><img src="resources/imagenes/unmomento.gif" style="width: 50%;"/></div>
									</div>
								</div>

								<!-- End SmartWizard Content -->
							</form:form>
						<div id="loader-icon" style="display:none;"><img src="resources/imagenes/load.gif" style="width: 100%;" /></div>
						</td>
					</tr>
				</table>
				<br>
				<p style="text-align: left;">
					<a href="welcome.do">Volver</a>
				</p>
			</div>

		</div>
		<jsp:include page="../pie.html" flush="true" />
	</div>
</body>
</html>