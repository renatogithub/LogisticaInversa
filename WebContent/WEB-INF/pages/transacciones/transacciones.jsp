<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page session="true"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="resources/jquery/jquery-1.9.1.js"></script>
<script src="resources/jquery/jquery-ui.js"></script>
<link rel="stylesheet" href="resources/css/jquery-ui.css">
<link rel="shortcut icon" href="resources/icon/logo.ico" /> 
<link rel="stylesheet" type="text/css" href="resources/css/screen.css">

<title>Sistema Integrado - Gestion de Logistica Inversa</title>

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
	<jsp:include page="../titulo.jsp" flush="true"/>
	<div id="contenido">
		<div id="izquierda">

		</div>	
		<div id="derecha">

			<p style="text-align: center;">
				<a href="welcome.do" >Volver</a>
			</p>
		</div>
			
	</div>
	<jsp:include page="../pie.html" flush="true"/>	
	</div>
</body>
</html>
