
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sistema Integrado - Gestion de Logistica Inversa</title>
<link rel="stylesheet" type="text/css" href="resources/css/message.css">
<link rel="shortcut icon" href="resources/icon/logo.ico" /> 
<%@ page session="true" %>
<%
    String usuario = "", clave="";
    HttpSession sesionOk = request.getSession();
    if (sesionOk.getAttribute("usuario") == null) {%>
    <jsp:forward page="login.jsp">
        <jsp:param name="error" value="Es obligatorioidentificarse"/>
    </jsp:forward><% }
    else {
        usuario = (String)sesionOk.getAttribute("usuario");
        clave = (String)sesionOk.getAttribute("clave");

    }

%>

</head>
<body>
	<div id="contenido-box" style="width: 50%;">
		<img alt="" src="resources/imagenes/tgestiona.png">
		<p style="text-align: center;" class="error">
			<b>Usted ah intentado ingresar a la intranet sin éxito.<br>Talvez aun no cuente con usuario o su clave sea incorrecta<br>Verifique</b>
		</p>
		<p style="text-align: center;">
			<img src="resources/imagenes/errorIngreso.png"/>
		</p>	
		<p style="text-align: left;">
			<a href="inicio.do">Volver</a>
		</p>
		
	</div>	
</body>
</html>