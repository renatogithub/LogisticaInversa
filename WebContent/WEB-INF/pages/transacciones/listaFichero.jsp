<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<title>Sistema Integrado - Gestion de Logistica Inversa</title>
<link rel="shortcut icon" href="resources/icon/logo.ico" />
<link rel="stylesheet" type="text/css" href="resources/css/screen.css">
<script src="resources/jquery/jquery-1.9.1.js"></script>
<script src="resources/jquery/jquery-1.4.2.min.js"></script>
<script src="resources/jquery/jquery.dataTables.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#tbActa').DataTable();
	});
</script>

<%@ page session="true" %>
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
			<div id="izquierda"></div>
			<div id="derecha">
				<table style="width: 100%; margin: 0 auto;" border="1"
					class="ficheroTabla" id="tbActa">
					<thead>
						<tr>
							<th>Descripcion</th>
							<th>Serie</th>
							<th>Cod. Material</th>
							<th>Tipo</th>
							<th>Modelo</th>
							<th>Marca</th>
							<th>Tecnologia</th>
							<th>Cantidad</th>
						</tr>
					</thead>
					<tfoot>
						<tr>
							<th>Descripcion</th>
							<th>Serie</th>
							<th>Cod. Material</th>
							<th>Tipo</th>
							<th>Modelo</th>
							<th>Marca</th>
							<th>Tecnologia</th>
							<th>Cantidad</th>
						</tr>
					</tfoot>
					<c:forEach var="fila" items="${listaDetalleFichero}">
						<tr>

							<td>${fila.descripcion}</td>
							<td>${fila.codSerie}</td>
							<td>${fila.codSAP}</td>
							<td>${fila.tipo}</td>
							<td>${fila.modelo}</td>
							<td>${fila.marca}</td>
							<td>${fila.tecnologia}</td>
							<td>${fila.cant}</td>

						</tr>
					</c:forEach>
				</table>
				<br />

				<p style="text-align: center;">
					<a href="welcome.do">Volver</a>
				</p>
			</div>

		</div>

		<jsp:include page="../pie.html" flush="true" />
	</div>

</body>
</html>