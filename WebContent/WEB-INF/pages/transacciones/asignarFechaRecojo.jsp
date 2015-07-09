<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="true"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
<script src="resources/jquery/jquery-1.9.1.js"></script>
<script src="resources/jquery/jquery-ui.js"></script>
<link rel="stylesheet" href="resources/css/jquery-ui.css">
<title>Sistema Integrado - Gestion de Logistica Inversa</title>
<link rel="shortcut icon" href="resources/icon/logo.ico" />
<link rel="stylesheet" type="text/css" href="resources/css/screen.css">
<script type="text/javascript" src="resources/js/asignarFechaRecojo.js?ver=04.07.2015A"></script>
<link href="resources/css/smart_wizard.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="resources/jquery/jquery.smartWizard-2.0.min.js"></script>
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


<script type="text/javascript">
	jQuery(function($){
		$("#fRecojo").mask("99/99/9999",{placeholder:"dd/mm/yyyy"});	
	});
</script>


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
						<form:form action="asignarFechaRecojo.do" method="POST" enctype="multipart/form-data" class="formProcesar" id="formProcesar" name="formProcesar" modelattribute="uploadForm">
								<input type='hidden' name="issubmit" value="1">
								<!-- Tabs -->
								<div id="wizard" class="swMain" style="position:relative;z-index:2;">
									<ul>
										<li><a href="#step-1"> <span class="stepNumber">1</span>
											<span class="stepDesc"> Datos Acta<br /> <small>Seleccionar Acta</small>
											</span>
										</a></li>

									</ul>
									<div id="step-1">
										<div>
											<h2 class="StepTitle" style="background-color: green;color: white;">Asignar Fecha Recojo</h2>									
										</div>
										<h2 class="StepTitle">Paso 1: Datos del Acta</h2>
										<br /> 
										<!-- Variables Session -->
										<input type="hidden" name="issubmit" value=<%= session.getAttribute("issubmit") %> id="issubmit" />										
										<input type="hidden" name="h_usuario" value=<%= session.getAttribute("usuario") %> />


										<fieldset style="padding: 10px;">
											<legend>
												<b>*</b> Selecciona Tipo de Formato: 
											</legend>
												<div id="tipoFormato" style="margin-left:30px;"></div>
												<input type="hidden" name="h_tipoFormato" id="h_tipoFormato" />
												<input type="hidden" name="h_nomtipoFormato" id="h_nomtipoFormato" />
												<input type='hidden' name='nroTicket' id='nroTicket' value='' style='text-align:right;'/>
												<input type='hidden' name='nroActas' id='nroActas' value='' style='text-align:right;'/>
										</fieldset><br/><br/>

										<fieldset style="padding: 10px;">
											<legend>
												<b>*</b> Selecciona Fecha Recojo: 
											</legend>
												<strong>*</strong> Fecha de Recojo (dd/mm/yyyy): 
												<input type="text" name="t_fRecojo" id="fRecojo" size="8px" />												
												<span id="msg_fcorreo" style="color: red;"></span>

										</fieldset><br/><br/>

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