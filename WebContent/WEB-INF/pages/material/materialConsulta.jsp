<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
<script type="text/javascript" src="resources/js/material.js?ver=17.06.2015B"></script>
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
	background-color: #E7F7FG;
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
    <jsp:forward page="login.jsp">
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
				<form action="" method="GET">
				<h3>Consulta de Material</h3>
				<fieldset style="width:100%;padding-left: 20px;">
				<div id="dialog" title="Sistema Integrado - Gestion de Logistica Inversa"></div>
				<legend>Busqueda</legend>
					<br>
					<table style="width: 100%" cellspacing="0" width="100" border="0">
						<tr>
							<td style="text-align:left;">
								<strong>Material:</strong><br>
								<input type="text" name="t_material" id="t_material" size="20" maxlength="25" class="nostyle" onkeypress="onPress_ENTER();"/>
							</td>
							<td style="text-align:left;" colspan="2">
								<strong>Descripción:</strong><br>	 
								<input type="text" name="t_descripcion" id="t_descripcion" size="100" maxlength="100" class="nostyle" onkeypress="onPress_ENTER();"/>
							</td>
						</tr>
						<tr>
							<td style="text-align:left;">
								<strong>Tipo:</strong><br>
								<input id="t_tipo" size="20" type="text" name="t_tipo" maxlength="20" class="nostyle" onkeypress="onPress_ENTER();"/>
							</td>
							<td style="text-align:left;">
								<strong>Rubro:</strong><br>
								<input id="t_rubro" size="20" type="text" name="t_rubro" maxlength="25" class="nostyle" onkeypress="onPress_ENTER();"/>
							</td>
							<td style="text-align:left;">
								<strong>Tecnologia:</strong><br>
								<input id="t_tecnologia" size="30" type="text" name="t_tecnologia" class="nostyle" onkeypress="onPress_ENTER();"/>
							</td>
						</tr>
						
						<tr>	
							<td style="text-align:left;">
								<strong>Lotizable:</strong><br>		
								<select id="cboLotizable" class="nostyle">
								  <option value="">---Seleccione un Estado de Lote--</option>
								  <option value="SI">SI</option>
								  <option value="NO">NO</option>
								</select>
							</td>
							
							<td style="text-align:left;">
								<input id="btnConsultar" type="button" onclick="busca_Material('1');" value="Consultar" class="button" style="margin-left: 20px; width: 120px; height: 30px;" />								
							</td>
							
							<td style="text-align:left;">
								<input id="btnAdicionar" type="button" onclick="addMaterial();" value="Adicionar" class="button" style="margin-left: 20px; width: 120px; height: 30px;" />								
							</td>

						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>

					</table>
					</fieldset>
				</form>
			</div>
			<br><br>
				<div id="loadingmessage" style="text-align: center;display: none;"><img src="resources/imagenes/load.gif" style="width: 10%;"/></div>
				<div id="tituloTabla"></div>
				
				<table class='styleTabla' id="tbConsultaMaterialQry">
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