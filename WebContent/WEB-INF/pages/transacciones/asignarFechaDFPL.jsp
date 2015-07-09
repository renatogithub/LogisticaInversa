<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="true"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
<script src="resources/jquery/jquery-1.9.1.js"></script>
<script src="resources/jquery/jquery-ui.js"></script>
<link rel="stylesheet" href="resources/css/jquery-ui.css">
<title>Sistema Integrado - Gestion de Logistica Inversa</title>
<link rel="shortcut icon" href="resources/icon/logo.ico" />
<link rel="stylesheet" type="text/css" href="resources/css/screen.css">
<script type="text/javascript" src="resources/js/canal.js"></script>
<script type="text/javascript" src="resources/js/entidad.js"></script>
<script type="text/javascript" src="resources/js/validarActa.js?ver=22.04.15D"></script>
<link href="resources/css/smart_wizard.css" rel="stylesheet" type="text/css">
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
						<form:form action="procesarValidacion.do" method="POST" enctype="multipart/form-data" class="formProcesar" id="formProcesar" name="formProcesar" modelattribute="uploadForm">
							<input type='hidden' name="issubmit" value="1">
								<!-- Tabs -->
							<div id="wizard" class="swMain" style="position:relative;z-index:2;">
								<ul>
									<li><a href="#step-1"> <span class="stepNumber">1</span>
										<span class="stepDesc"> Procesar Ticket<br /> <small>Asignar Tickets a Procesar</small>
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
										<br /> <input type="hidden" name="h_usuario"
											value=<%= session.getAttribute("usuario") %> />


										<fieldset style="padding: 10px;">
											<legend>
												<b>*</b> Selecciona Tipo de Formato: 
											</legend>
												<div id="tipoFormato" style="margin-left:30px;"></div>
												<input type="hidden" name="h_tipoFormato" id="h_tipoFormato" />
												<input type="hidden" name="h_nomtipoFormato" id="h_nomtipoFormato" />

										</fieldset><br/>
										
										<fieldset style="padding: 10px;">
											<legend>
												<b>*</b> Busqueda del Acta: 
											</legend>
												<b>*</b> Fecha del Correo (dd/mm/yyyy): <input type='text' id='fregistroActa' readonly="readonly" name='t_fregistro' size=8 >&nbsp;												
												<b>*</b> Fecha del Correo (dd/mm/yyyy): <input type='text' id='fcorreoActa' readonly="readonly" name='t_fCorreo' size=8 >
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
										<h2 class="StepTitle">Paso 2: Cargar Archivo SAP</h2>
										<br/><br/>
										<table>
										
											<tr>
												<td>
													<input type="checkbox" id="chkSinSerie" name="chkSinSerie" value="SINSERIE" style="margin-bottom: 20px;" />Sin Serie
												</td>
											</tr>
											<tr>
												<td>
													<h4>Fuente SAP 6.0</h4>
												</td>
											</tr>
											<tr>
												<td>Selecciona Archivo SAP 6.0:</td>
												<td>
													<div class="upload">

														<input type="file" name="files[0]" id="ficheroExcelSAP6_0" style="margin-bottom: 20px;" />
														<span id="msg_fileExcel60" style="color: red;"></span> <br />
													</div>
												</td>
											</tr>
											<tr><td><h4>Fuente SAP 4.7</h4></td></tr>
											<tr>
												<td>Selecciona Archivo SAP 4.7:</td>
												<td>
													<div class="upload">
														<input type="file" name="files[1]" id="ficheroExcelSAP4_7" style="margin-bottom: 20px;" />
														<span id="msg_fileExcel47" style="color: red;"></span>
													</div>
												</td>
											</tr>

										</table>
										<span id="msg_fileExcel" style="color: red;"></span>
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