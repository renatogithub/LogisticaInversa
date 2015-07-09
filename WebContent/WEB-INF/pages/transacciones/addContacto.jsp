<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
<script src="resources/jquery/jquery-1.9.1.js"></script>
<script src="resources/jquery/jquery-ui.js"></script>
<link rel="stylesheet" href="resources/css/jquery-ui.css">
<title>Sistema Integrado - Gestion de Logistica Inversa</title>
<link rel="shortcut icon" href="resources/icon/logo.ico" />
<script type="text/javascript" src="resources/js/contacto.js?ver=02072015"></script>

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
<body onload="loadContacto();">
	<form:form action="registrarContacto.do" method="POST" id="frmContacto" name="frmContacto">
	<input type="hidden" name="issubmit" value=<%= sesionOk.getAttribute("issubmit") %> id="issubmit" />
	<div id="dialog" title="Sistema de Gestion de logistica Inversa"></div>
	<h2>Registro de Contactos del Acta</h2>
	<fieldset style="padding: 10px;">
	<input type="hidden" name="t_entidad" id="t_entidad">
		<table border=0>
			<tr>
				<td><strong> Nombre de Contacto:</strong></td>
				<td>
					<input type="text" name="t_nombre" id="t_nombre" size="50" onKeyPress="TabKey(event, 't_correo')"><br>
					<span id="msg_nombre" style="color: red;"></span>
				</td>
			</tr>
			<tr>
				<td><strong> Correo:</strong></td>
				<td>
					<input type="text" name="t_correo" id="t_correo" placeholder="username@example.com" size="30" onKeyPress="TabKey(event, 't_tlfAnexo')"><br>
					<span id="msg_correo" style="color: red;"></span>
				</td>
			</tr>
			<tr>
				<td><strong> Telefono - Anexo:</strong></td>
				<td>
					<input type="text" name="t_tlfAnexo" id="t_tlfAnexo" onKeyPress="TabKey(event, 't_rpm')">
					<span id="msg_tlfAnexo" style="color: red;"></span>
				</td>
			</tr>

			<tr>
				<td><strong> RPM:</strong></td>
				<td>
					<input type="text" name="t_rpm" id="t_rpm" onKeyPress="TabKey(event, 'btnRegistrar')">
					<span id="msg_rpm" style="color: red;"></span>
				</td>
			</tr>
									
		</table>	
	</fieldset>
	<br>
	<input name="btnRegistrar" id="btnRegistrar" value="Registrar" type="button" class="button"/>
										
	</form:form>
</body>
</html>