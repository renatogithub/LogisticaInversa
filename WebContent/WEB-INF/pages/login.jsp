<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page session="true"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="resources/jquery/jquery-1.9.1.js"></script>
<script src="resources/jquery/jquery-ui.js"></script>
<link rel="stylesheet" href="resources/css/jquery-ui.css" type="text/css">
<link rel="stylesheet" href="resources/css/login.css" type="text/css" >
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="shortcut icon" href="resources/icon/logo.ico" /> 
<title>Sistema Integrado - Gestion de Logistica Inversa</title>
<script type="text/javascript" src="resources/js/login.js"></script>
</head>
 <body>
	<form name='loginForm' action="<c:url value='principal.do' />" method="post">
		<img alt="" src="resources/imagenes/tgestiona.png">
		<h2>Acceso al Sistema</h2>
		<input type="text" class="text-field" placeholder="Username" name="user" id="t_user"/>
			<br><span id="msg_user" style="color: red;font-weight: bold;font-size: 12px;"></span>
		<input type="password" class="text-field" placeholder="Password" name="pass" id="t_pass"/>
			<br><span id="msg_pass" style="color: red;font-weight: bold;font-size: 12px;"></span>
		<input type="button" value="Ingresar" class="button"  onclick="accederLogin();"/>
	</form>
 
</body>
</html>