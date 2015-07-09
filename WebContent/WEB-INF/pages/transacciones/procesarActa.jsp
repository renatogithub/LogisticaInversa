<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="true"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
<script type="text/javascript" src="resources/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="resources/jquery/jquery-ui.js"></script>
<link rel="stylesheet" href="resources/css/jquery-ui.css">
<title>Sistema Integrado - Gestion de Logistica Inversa</title>
<link rel="shortcut icon" href="resources/icon/logo.ico" />
<link rel="stylesheet" type="text/css" href="resources/css/screen.css">
<link rel="stylesheet" type="text/css" href="resources/css/smart_wizard.css">
<script type="text/javascript" src="resources/js/canal.js"></script>
<script type="text/javascript" src="resources/js/entidad.js"></script>
<script type="text/javascript" src="resources/js/validarActa.js?ver=07.07.2015b"></script>
<script type="text/javascript" src="resources/jquery/jquery.smartWizard-2.0.min.js"></script>

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

%>

<style type="text/css" >

.button:hover{
    cursor: pointer;
    background-color: rgb(172, 213, 252);
}

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

</head>
<body>

	<div id="content">
		<jsp:include page="../titulo.jsp" flush="true" />
		<div id="contenido">
			<div id="dialog" title="Sistema Integrado - Gestion de Logistica Inversa"></div>
			<div id="derecha">

				<table align="center" border="0" cellpadding="0" cellspacing="0"
					width="100%">
					<tr>
						<td>
						<form:form action="procesarValidacion.do" method="POST" enctype="multipart/form-data" class="formProcesar" id="formProcesar" name="formProcesar" modelattribute="uploadForm">
							<input type='hidden' name="issubmit" value="1">
								<!-- Tabs -->
							<div id="wizard" class="swMain" style="position:relative;z-index:2;">
								<ul>
									<li><a href="#step-1"> <span class="stepNumber">1</span>
										<span class="stepDesc"> Procesar Ticket<br /> <small>Seleccionar Tickets a Procesar</small>
										</span>
										</a></li>
									<li><a href="#step-2"> <span class="stepNumber">2</span>
										<span class="stepDesc"> Fuente SAP<br /> <small>Registrar
											Datos del SAP</small>
										</span>
									</a></li>

								</ul>
									<div id="step-1">
										<div>
											<h2 class="StepTitle" style="background-color: green;color: white;">Validacion de Ticket(s)</h2>									
										</div>
										<h2 class="StepTitle">Paso 1: Datos del Acta</h2>
										<br /> <input type="hidden" name="h_usuario" id="h_usuario"
											value=<%= session.getAttribute("usuario") %> />

										<fieldset style="padding: 10px;">
											<legend>
												<strong>*</strong> Selecciona Tipo de Formato: 
											</legend>
												<div id="tipoFormato" style="margin-left:30px;"></div>
												<input type="hidden" name="h_tipoFormato" id="h_tipoFormato" />
												<input type="hidden" name="h_nomtipoFormato" id="h_nomtipoFormato" />

										</fieldset><br/>
										
										<fieldset style="padding: 10px;">
											<legend>
												<strong>*</strong> Busqueda del Acta: 
											</legend>
												<br>Fecha de Registro del Acta
												De (dd/mm/yyyy): <input type='text' id='fcorreo1' readonly="readonly" name='fcorreo1' size=8 >&nbsp;												
												Hasta (dd/mm/yyyy): <input type='text' id='fcorreo2' readonly="readonly" name='fcorreo2' size=8 >&nbsp;&nbsp;&nbsp;
												<input name="btnConsultar" id="btnConsultar" value="Consultar" type="button" class="button" onclick='verSinProcesarFechas();'/>
										</fieldset><br/>
										

										<fieldset style="padding: 10px;height: 300px;">
											<legend>
												<strong>Datos del Acta:</strong>
											</legend>
												<div id="content_check_ticket"></div>
			 									 <%--
													Se van a escribir los check dinamicamente con los Numero de Ticket
												 --%>
										</fieldset>
										<br />
									</div>
									
									<div id="step-2">
										<div>
											<h2 class="StepTitle" style="background-color: green;color: white;">Validacion de Ticket(s)</h2>									
										</div>
										<h2 class="StepTitle">Paso 2: Cargar Archivo SAP</h2>
										<br/><br/>
										<div id="validarSAP">
										<table>
										
											<tr>
												<td style='text-align:left;'>
													<input type="checkbox" id="chkSinSerie" name="chkSinSerie" value="SINSERIE" style="margin-bottom: 20px;" />Validar sin Datos SAP
												</td>
											</tr>
											<tr>
												<td style='text-align:left;'>
													<h4>Fuente SAP 6.0</h4>
												</td>
											</tr>
											<tr>
												<td style='text-align:left;'>Selecciona Archivo SAP 6.0:</td>
												<td style='text-align:left;'>
													<div class="upload">

														<input type="file" name="files[0]" id="ficheroExcelSAP6_0" style="margin-bottom: 20px;" />
														<span id="msg_fileExcel60" style="color: red;"></span> <br />
													</div>
												</td>
											</tr>
											<tr>
												<td style='text-align:left;'>
													<h4>Fuente SAP 4.7</h4>
												</td>
											</tr>
											<tr>
												<td style='text-align:left;'>Selecciona Archivo SAP 4.7:</td>
												<td style='text-align:left;'>
													<div class="upload">
														<input type="file" name="files[1]" id="ficheroExcelSAP4_7" style="margin-bottom: 20px;" />
														<span id="msg_fileExcel47" style="color: red;"></span>
													</div>
												</td>
											</tr>

										</table>
										</div>
										<span id="msg_fileExcel" style="color: red;"></span>
									<div id="loadingmessage" style="text-align: center;display: none;"><img src="resources/imagenes/procesandoActa.gif" style="width: 50%;"/></div>
									</div>

								</div>
								
								<!-- End SmartWizard Content -->

							</form:form>

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