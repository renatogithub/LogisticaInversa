<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="resources/jquery/jquery-1.9.1.js"></script>
<script src="resources/jquery/jquery-ui.js"></script>
<link rel="stylesheet" href="resources/css/jquery-ui.css">
<title>Sistema Integrado - Gestion de Logistica Inversa</title>
<link rel="shortcut icon" href="resources/icon/logo.ico" />
<link rel="stylesheet" type="text/css" href="resources/css/screen.css">
<script type="text/javascript" src="resources/js/consultaActasQry.js?ver=15.06.2015H"></script>
<%@ page session="true" %>

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

.paginador{
	padding:5px 10px 5px 10px;
	font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
	font-size:14px;
	background-color: #E7F7FF;
	color:#000000;
	border:0.5px solid #333333;
	cursor:pointer;
}

.paginador:hover{
	padding:5px 10px 5px 10px;
	font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
	font-size:14px;
	background-color:#73ADD6;
	color: white ;
	border:0.5px solid #333333;
	cursor:pointer;
}

.paginadoractivo{
	padding:5px 10px 5px 10px;
	font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
	font-size:14px;
	background-color:#F5DAA0;
	color:#000000;
	border:0.5px solid #000000;
	cursor:pointer;
}

a img{  
  border:none;  
  outline:none;  
}  
.content{  
  margin-top:100px;  
  padding:0px;  
  bottom:0px;  
}             
 
.demo{  
   width: 580px;  
   padding: 10px;  
   margin: 10px auto;  
   border: 1px solid #fff;  
   background-color: #00AEEF;  
   font-family: Arial, Helvetica, sans-serif;  
   font-size: 30px;  
 }  
        
 .demo h1 {  
  	color:#CCC;  
 }  
                
 .pagedemo{  
    border: 1px solid #CCC;  
    width: 90%;  
    margin: 2px;  
    padding: 50px 10px;  
    text-align: center;  
    background-color: white;                
   	font-size: 50px;  
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

	<div id="content">
		<jsp:include page="../titulo.jsp" flush="true" />
		<div id="contenido">
			<div id="derecha">

			<div id="contiene_busqueda">
				<form action="">
				<h3>Consulta de Acta</h3>
				<fieldset style="width:100%;padding-left: 20px;" class="nostyle">
				<div id="dialog" title="Sistema Integrado - Gestion de Logistica Inversa"></div>
				<legend>Busqueda</legend>
					<br>
					<table style="width: 100%" cellspacing="0" width="100" border="0">
						<tr>
							<td style="text-align:left;">
								<strong>Ticket:</strong><br>
								<input type="text" name="t_ticket" id="t_ticket" size="20" maxlength="20" class="nostyle" onkeypress="onPress_ENTER();"/>
							</td>
							<td style="text-align:left;">
								<strong>Acta:</strong><br>
								<input type="text" name="t_acta" id="t_acta" size="20" maxlength="25" class="nostyle" onkeypress="onPress_ENTER();"/>
							</td>
							<td style="text-align:left;">
								<strong>Estado:</strong><br>
								<select id="cboEstado" class="nostyle">
									  <option value="">---Seleccione un Estado---</option>
									  <option value="APROBADO">APROBADO</option>
									  <option value="PENDIENTE">PENDIENTE</option>
									  <option value="RECHAZADO">RECHAZADO</option>
								</select>
							</td>
							<td style="text-align:left;">
								<strong>Serie:</strong><br>
								<input type="text" name="t_serie" id="t_serie" size="25" maxlength="25" class="nostyle" onkeypress="onPress_ENTER();"/>
							</td>
						
						</tr>
						
						<tr>
							<td style="text-align:left;">
								<strong>Material:</strong><br>
								<input type="text" name="t_material" id="t_material" size="25" maxlength="20" class="nostyle" onkeypress="onPress_ENTER();"/>
							</td>
							
							<td colspan="3"  style="text-align:left;">
								<strong>Descripción:</strong><br>
								<input type="text" name="t_descrip" id="t_descrip" size="110" maxlength="110" class="nostyle" onkeypress="onPress_ENTER();"/>
							</td>
							
						</tr>
						
						<tr>
							<td style="text-align:left;">
								<strong>Fecha del Correo (dd/mm/yyyy)</strong> <br>
								De: &nbsp;&nbsp;<input type='text' id='fcorreo1' readonly="readonly" name='fcorreo1' class="nostyle"/>																				
							</td>
							
							<td style="text-align:left;">
								<strong>Fecha del Correo (dd/mm/yyyy)</strong> <br>
								Hasta: &nbsp;&nbsp;<input type='text' id='fcorreo2' readonly="readonly" name='fcorreo2' class="nostyle"/>																				
							</td>
							
							<td style="text-align:left;">
								<input id="btnConsultar" type="button" onclick="busca_Acta('1');" value="Consultar" class="button" style="margin-left: 20px; width: 120px; height: 30px;" />
							</td>

							<td style="text-align:left;">
								<a id="miEnlace" href=''><img src="resources/imagenes/excel.png" width="10%" border="2" ></a>
							</td>
						</tr>
						<tr>
							<td style="text-align:left;">
								<strong>Estado Acta:</strong><br>
								<select id="cboEstadoActa" class="nostyle">
									  <option value="">---Seleccione un Estado---</option>
									  <option value="ANULADO">ANULADO</option>
								</select>
							</td>
						</tr>
					</table>
					<br>
					</fieldset>
				</form>
			</div>
			<br><br>
				<div id="loadingmessage" style="text-align: center;display: none;"><img src="resources/imagenes/load.gif" style="width: 10%;"/></div>
				<table class='styleTabla' id="tbConsultaActaQry">
					<tbody>
	
					</tbody>
				</table>
			<br/>
			<div style="width:100%;float: right;" id="paginador"></div>

			<p style="text-align: center;">
				<a href="welcome.do">Volver</a>
			</p>
		</div>

	</div>

 		<jsp:include page="../pie.html" flush="true" /> 
	</div>



</body>
</html>