<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="resources/jquery/jquery-1.9.1.js"></script>
<script src="resources/jquery/jquery-ui.js"></script>
<link rel="stylesheet" href="resources/css/jquery-ui.css">

<title>Sistema Integrado - Gestion de Logistica Inversa</title>
<link rel="stylesheet" href="resources/css/screen.css" type="text/css" >
<link rel="shortcut icon" href="resources/icon/logo.ico" />
<script type="text/javascript" src="resources/js/provision.js"></script>
<script type="text/javascript" src="resources/js/negocio.js"></script>
<script type="text/javascript" src="resources/js/sociedad.js"></script>
<script type="text/javascript" src="resources/js/tipoMaterial.js"></script>
<script type="text/javascript" src="resources/js/rubroMaterial.js"></script>
<script type="text/javascript" src="resources/js/tecnologiaMaterial.js"></script>
<script type="text/javascript" src="resources/js/material.js?v=19.06.2015C"></script>

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
body {
	font: .8em Arial,Tahoma,Verdana;
	color: #777;
}

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
						 <form:form action="registrarMaterial.do" method="POST" id="frmMaterial" name="frmMaterial">
							<input type="hidden" name="issubmit" value=<%= sesionOk.getAttribute("issubmit") %> id="issubmit" />
							<input type="hidden" name="h_usuario" id="h_usuario" value=<%= session.getAttribute("usuario") %> />		
							<div id="dialog" title="Sistema de Gestion de logistica Inversa"></div>
							<h2>Registrar de Materiales</h2>
								<br><br>
								<table align="center" border="0" cellpadding="0" cellspacing="0"
											width="110%">
									<tr>
										<td style="text-align: left;"><strong> Codigo Material:</strong></td>
										<td style="text-align: left;">
											<input type="text" name="t_codigo" id="t_codigo" >
											<span id="msg_codigo" style="color: red;font-weight: bold;"></span>
										</td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
									<tr>
										<td style="text-align: left;"><strong> Descripcion Material:</strong></td>
										<td style="text-align: left;">
											<input type="text" name="t_descripcion" id="t_descripcion" size="100"><br>
											<span id="msg_descripcion" style="color: red;font-weight: bold;"></span>
										</td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
									
									<tr>
										<td style="text-align: left;"><strong> UMD:</strong></td>
										<td style="text-align: left;">
											<input type="text" name="t_umd" id="t_umd" size="3">
											<span id="msg_umd" style="color: red;font-weight: bold;"></span>
										</td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
									
						
									<tr>
										<td style="text-align: left;"><strong> Peso:</strong></td>
										<td style="text-align: left;">
											<input type="text" name="t_peso" id="t_peso" size="10">
											<span id="msg_peso" style="color: red;font-weight: bold;"></span>
										</td>
									</tr>
									
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
									
						
									<tr>
										<td style="text-align: left;"><strong> Volumen Unitario:</strong></td>
										<td style="text-align: left;">
											<input type="text" name="t_volUnitario" id="t_volUnitario" size="10">
											<span id="msg_volUnitario" style="color: red;font-weight: bold;"></span>
										</td>
									</tr>
									
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
									
									
									<tr>
										<td style="text-align: left;"><strong> Precio Equipo:</strong></td>
										<td style="text-align: left;">
											<input type="text" name="t_precio" id="t_precio" size="10">
											<span id="msg_precio" style="color: red;font-weight: bold;"></span>
										</td>
									</tr>
						
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
						
						
									<tr>
										<td style="text-align: left;"><strong> Seriado / No Seriado:</strong></td>
										<td style="text-align: left;">
											<input type="radio" name="rdbSerNoSer" id="rdbSer" value="SI" checked="checked">Seriado
											<input type="radio" name="rdbSerNoSer" id="rdbNoSer" value="NO">No Seriado
											<span id="msg_SerNoSer" style="color: red;font-weight: bold;"></span>
										</td>
									</tr>
						
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
						
									
									<tr>
										<td style="text-align: left;"><strong> Lotizable:</strong></td>
										<td style="text-align: left;">
											<input type="radio" name="rdbLotizable" id="rdbLotizableSI" value="SI" checked="checked">Lotizable
											<input type="radio" name="rdbLotizable" id="rdbLotizableNO" value="NO">No Lotizable
											<span id="msg_Lotizable" style="color: red;font-weight: bold;"></span>
										</td>
									</tr>
									
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
									
									
									<tr>
										<td style="text-align: left;"><strong> Provision / Planta Externa:</strong></td>
										<td style="text-align: left;">
											<select name="cboProv" id="cboProv">
												<option value="-1" selected>---------------------</option>
											</select>
											<input type="hidden" name="h_Prov" id="h_Prov" />
											<span id="msg_prov" style="color: red;font-weight: bold;"></span>
										</td>
									</tr>
									
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
									
									
									<tr>
										<td style="text-align: left;"><strong> Negocio:</strong></td>
										<td style="text-align: left;">
											<select name="cboNegocio" id="cboNegocio">
												<option value="-1" selected>---------------------</option>
											</select>
											<input type="hidden" name="h_Negocio" id="h_Negocio" />
											<span id="msg_Negocio" style="color: red;font-weight: bold;"></span>
										</td>
									</tr>
									
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
									
									
									<tr>
										<td style="text-align: left;"><strong> Tipo de Material:</strong></td>
										<td style="text-align: left;">
											<select name="cboTipoMat" id="cboTipoMat">
												<option value="-1" selected>---------------------</option>
											</select>
											<input type="hidden" name="h_TipoMat" id="h_TipoMat" />
											<span id="msg_TipoMat" style="color: red;font-weight: bold;"></span>
										</td>
									</tr>
									
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
									
									<tr>
										<td style="text-align: left;"><strong> Rubro de Material:</strong></td>
										<td style="text-align: left;">
											<select name="cboRubMat" id="cboRubMat">
												<option value="-1" selected>---------------------</option>
											</select>
											<input type="hidden" name="h_RubMat" id="h_RubMat" />
											<span id="msg_RubMat" style="color: red;font-weight: bold;"></span>
										</td>
									</tr>
									
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
									
									<tr>
										<td style="text-align: left;"><strong> Tecnología:</strong></td>
										<td style="text-align: left;">
											<select name="cboTecMat" id="cboTecMat">
												<option value="-1" selected>---------------------</option>
											</select>
											<input type="hidden" name="h_TecMat" id="h_TecMat" />
											<span id="msg_TecMat" style="color: red;font-weight: bold;"></span>
										</td>
									</tr>
									
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
									
									<tr>
										<td style="text-align: left;"><strong> Sociedad:</strong></td>
										<td style="text-align: left;">
											<select name="cboSocMat" id="cboSocMat">
												<option value="-1" selected>---------------------</option>
											</select>
											<input type="hidden" name="h_SocMat" id="h_SocMat" />
											<span id="msg_SocMat" style="color: red;font-weight: bold;"></span>
										</td>
									</tr>			
									
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>						
								</table>
						
							<br>
							<p style="text-align: left;">
								<input name="btnRegistrar" id="btnRegistrar" value="Registrar" type="button" class="button" style="text-align: left;" onclick="onGrabarMaterial();"/>
							</p>															
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