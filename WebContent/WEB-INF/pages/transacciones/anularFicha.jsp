<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page import="pe.tgestiona.logistica.inversa.bean.ConstantesGenerales"%>
<%@page session="true"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
<script src="resources/jquery/jquery-1.9.1.js"></script>
<script src="resources/jquery/jquery-ui.js"></script>
<link rel="stylesheet" href="resources/css/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="resources/css/screen.css">
<link rel="shortcut icon" href="resources/icon/logo.ico" />
<title>Sistema Integrado - Gestion de Logistica Inversa</title>
<script type="text/javascript" src="resources/js/anularFicha.js?ver=26.05.2015E"></script>

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

.nostyle {
	margin: 0;
	padding: 0;
	border: 1px solid;
	outline: 0;
	font-size: 100%;
	vertical-align: baseline;
	background: transparent;
	margin: 5px 5px 5px 5px;
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

<body>
	<div id="content">
	<jsp:include page="../titulo.jsp" flush="true" />
		<div id="contenido">
		<div id="dialog" title="Sistema Integrado - Gestion de Logistica Inversa"></div>
		<div id="derecha">
			<form action="anularTicket.do" method="POST">
				<!-- Variables Session -->
				<input type="hidden" name="issubmit" value=<%= session.getAttribute("issubmit") %> id="issubmit" />
				<input type="hidden" name="h_usuario" id="h_usuario" value=<%= session.getAttribute("usuario") %> />							
				<div id="contiene_busqueda">
					<h3>Anular Acta</h3><br><br><br>
					<fieldset style="width:50%;padding-left: 20px;" class="nostyle">
					<legend>Busqueda</legend>
					<br>
					<table style="width: 50%" cellspacing="0" width="100" border="0">
						<tr>
							<td style="text-align:left;">
								<strong>Ticket:</strong><br>
								<input type="text" name="t_ticket" id="t_ticket" size="15" maxlength="10" class="nostyle" />
							</td>
							<td style="text-align:left;">
								<input id="btnConsultar" type="button" onclick="buscar_ticket();" value="Consultar" class="button" style="margin-left: 20px; width: 100px; height: 30px;" />
							</td>
						</tr>
					</table>
					<br>
					</fieldset>					
				<br>
				<div id="loadingmessage" style="text-align: center;display: none;"><img src="resources/imagenes/load.gif" style="width: 10%;"/></div>
				<table style="width: 100%; margin: 0 auto;" border="1" class="ficheroTabla" id="tbConsultaActaQry">
					<tbody>
		
					</tbody>
				</table>
				<br>
				<textarea rows="10" style="width: 100%;text-align: left;font-size: 14px;" id="t_observaciones" name="t_observaciones" class="nostyle" disabled="disabled">
				</textarea>
				<br><br>
				<input id="btnAnular" type="button" onclick="anular_acta();" value="Anular" class="button" style="width: 120px; height: 30px;" disabled="disabled"/>
				<br><br>
				</div>
				</form>	
				<p style="text-align: left;">
					<a href="welcome.do">Volver</a>
				</p>
			</div>

		</div>
		<jsp:include page="../pie.html" flush="true" />
	</div>
</body>
</html>