<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="true"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
<link rel="stylesheet" href="resources/css/jquery-ui.css">
<link rel="shortcut icon" href="resources/icon/logo.ico" />
<link rel="stylesheet" type="text/css" href="resources/css/screen.css">
<link href="resources/css/smart_wizard.css" rel="stylesheet" type="text/css">
<script src="resources/jquery/jquery-1.9.1.js"></script>
<script src="resources/jquery/jquery-ui.js"></script>
<script type="text/javascript" src="resources/jquery/jquery.smartWizard-2.0.min.js"></script>
<title>Sistema Integrado - Gestion de Logistica Inversa</title>
<script type="text/javascript" src="resources/js/canal.js"></script>
<script type="text/javascript" src="resources/js/entidad.js"></script>
<script type="text/javascript" src="resources/js/distribucion.js?ver=16.06.15A"></script>

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

			<div id="derecha">

				<table align="center" border="0" cellpadding="0" cellspacing="0"
					width="100%">
					<tr>
						<td>
						<form:form action="cargaFuenteDistribucion.do" method="POST" enctype="multipart/form-data" class="formProcesar" id="formProcesar" name="formProcesar" modelattribute="uploadForm">
								 <input type="hidden" name="issubmit" value=<%= session.getAttribute("issubmit") %> id="issubmit" />
 								 <input type="hidden" name="h_usuario" id="h_usuario" value=<%= session.getAttribute("usuario") %> />		
								<!-- Tabs -->
							<div id="wizard" class="swMain" style="position:relative;z-index:2;">
								<ul>
									<li><a href="#step-1"> <span class="stepNumber">1</span>
											<span class="stepDesc"> Datos Acta<br /> <small>Seleccionar Acta</small>
										</span>
										</a></li>
									<li><a href="#step-2"> <span class="stepNumber">2</span>
										<span class="stepDesc"> Fuente Distribución<br /> <small>Registrar Datos de Distribución</small>
										</span>
									</a></li>

								</ul>
									<div id="step-1">
										<div>
											<h2 class="StepTitle" style="background-color: green;color: white;">Carga de informacion de Distribucion</h2>									
										</div>
										<h2 class="StepTitle">Paso 1: Datos del Acta</h2>
										<br /> <input type="hidden" name="h_usuario"
											value=<%= session.getAttribute("usuario") %> />


										<fieldset style="padding: 10px;">
											<legend>
												<b>*</b> Selecciona Tipo de Formato: 
											</legend>
												<div id="tipoFormato" style="margin-left:30px;"></div>
												<input type="hidden" name="h_tipoFormato" id="h_tipoFormato" />
												<input type="hidden" name="h_nomtipoFormato" id="h_nomtipoFormato" />
												<div id="mnuOpciones" style="margin-bottom: 40px;">

												<table border="0" style="text-align: top;float: left;">
													<tr>
														<td vAlign="top" align="center" style="width: 120px;">
															<a id='miEnlaceDescargaFuenteDistribucion' href='' style="text-align: center;"><small>Descargar <br>Fuente Distribucion</small></a>
														</td>	
													</tr>
												</table>
												</div>



										</fieldset>
										<br/>
										
										<fieldset style="padding: 10px;">
											<legend>
												<b>*</b> Busqueda del Acta: 
											</legend>
												<b>*</b> Fecha del Correo (dd/mm/yyyy): <input type='text' id='fcorreo1' readonly="readonly" name='fcorreo1' size=8 >&nbsp;												
												<b>*</b> Fecha del Correo (dd/mm/yyyy): <input type='text' id='fcorreo2' readonly="readonly" name='fcorreo2' size=8 >&nbsp;&nbsp;&nbsp;
												<input name="btnConsultar" id="btnConsultar" value="Consultar" type="button" class="button" onclick='verActasFechas();'/>
										</fieldset><br/>
										

										<fieldset style="padding: 10px;height: 200px;">
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
										<h2 class="StepTitle">Paso 2: Cargar Archivo Distribución</h2>
										<br/><br/>
										<table>
										
											<tr>
												<td>
													<h4>Fuente Distribucion</h4>
												</td>
											</tr>
											<tr>
												<td>Selecciona Archivo Fuente Distribución:</td>
												<td>
													<div class="upload">
														<input type="file" name="files[0]" id="ficheroExcelDistribucion" style="margin-bottom: 20px;" />
														<span id="msg_fileExcelDistribucion" style="color: red;"></span> <br />
													</div>
												</td>
											</tr>

										</table>
										<span id="msg_fileExcel" style="color: red;"></span>
									</div>

								</div>
							<div id="loadingmessage" style="text-align: center;display: none;"><img src="resources/imagenes/load.gif" style="width: 10%;"/></div>
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