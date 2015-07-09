<%@page import="pe.tgestiona.logistica.inversa.bean.ConstantesGenerales"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page session="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="resources/jquery/jquery-1.9.1.js"></script>
<script src="resources/jquery/jquery-ui.js"></script>
<link rel="stylesheet" href="resources/css/jquery-ui.css">
<title>Sistema Integrado - Gestion de Logistica Inversa</title>
<link rel="shortcut icon" href="resources/icon/logo.ico" />
<link rel="stylesheet" type="text/css" href="resources/css/screen.css">
<script type="text/javascript" src="resources/js/configuracionValidacion.js?ver=12052015B"></script>
<%@page session="true" %>
<style type="text/css" >

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

.msg {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #31708f;
	background-color: #d9edf7;
	border-color: #bce8f1;
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
<body>

	<div id="content">
		<jsp:include page="../titulo.jsp" flush="true" />
		<div id="dialog" title="Sistema de Gestion de logistica Inversa"></div>
		<div id="contenido">
			<div id="izquierda">
				
				<!-- Aqui deberia pintar los motivos de Devolucion a configurar y otras opciones -->
				<div class="linea" style="width: 60%;"></div>
				<div id="opciones"></div>
				
			</div>

			<div id="derecha" style="width: 75%;">
			<h4>Tipo de Configuraciones</h4>
			<form id="frmConfiguracion" name="frmConfiguracion" method="post" action="registro.do">
			<div id="tabs">
				<ul>
					<li><a href="#tabs-1">1. Bodegas</a></li>
				    <li><a href="#tabs-2">2. Lote</a></li>
				    <li><a href="#tabs-3">3. Garantia</a></li>
 					<li><a href="#tabs-4">4. SAP</a></li>
 				    <li><a href="#tabs-5">5. Modo de Recojo</a></li> 
 				    <li><a href="#tabs-6">6. Otros</a></li> 
				</ul>
				
					<input type="hidden" name="t_criterio" id="t_criterio"/>
					<br>
						<div id="titulo" style="font-size:15px;color:blue;text-decoration: underline;margin-left: 15px;font-weight: bold;"></div>
					<div id="tabs-1">
						<p><b>1. Validacion de Bodegas</b></p>
						<input type="checkbox" name="chkTipoConfiguracion[]" style="margin-left:15px;" id="CONF01" value="CONF01"/>Validar Correspondencia de Bodegas
						<span id="msg_bodega" style="color: red;"></span>
						<br><br>					
						<div id="divBodegas"></div>
					</div>		
					
					<div id="tabs-2">
						<p><b>1. Validacion de Lote</b></p>
						<input type="checkbox" name="chkTipoConfiguracion[]" style="margin-left:15px;" id="CONF02" value="CONF02"/>Validar Correspondencia de Lote
						<span id="msg_Lote" style="color: red;"></span>
						<br><br>
						<div id="divLotes"></div>
	 					<table border="0">
							<tr>
								<td rowspan="2"> 
									<select id="lst_Lote" multiple name="lst_Lote[]" size="5" style="margin-left:40px;width: 120px;" ></select> 
	 							</td>
								<td> 
									<input type="button" name="btnAgregaLote" id="btnAgregaLote" value="Agregar Lote" style="margin-left:15px;" onclick="addLote();"  class="button"/>
	 							</td>
							</tr>
							
							<tr>
								<td> 
									<input type="button" name="btnQuitarLote" id="btnQuitarLote" value="Quitar Lote" style="margin-left:15px;" onclick="removerLote();" class="button"/>
	 							</td>
							</tr>
						</table> 
					</div>	
					
					<div id="tabs-3">
						<p><b>3. Validacion de Garantia</b></p>
						<input type="checkbox" name="chkTipoConfiguracion[]" id="CONF03" value="CONF03" onclick="habilitaGarantiaProveedores(this);"  style="margin-left:30px;"/>Validar Garantia Proveedores
						<span id="msg_GarantiaProveedor" style="color: red;"></span>
						<br><br>
						<span style="margin-left:45px;">Dias Proveedor:</span><input type="text" name="t_diasProveedores" id="t_diasProveedores" onkeypress="validarSiNumero(this.value);"  style="margin-left:15px;" size="10px" readonly /><br><br>
						<input type="checkbox" name="chkTipoConfiguracion[]" id="CONF04" value="CONF04" onclick="habilitaGarantiaTaller(this);" style="margin-left:30px;"/>Validar Garantia Taller
						<span id="msg_GarantiaTaller" style="color: red;"></span>
						<br><br>
						<span style="margin-left:45px;">Dias Taller:</span><input type="text" name="t_diasTaller" id="t_diasTaller" onkeypress="validarSiNumero(this.value);" style="margin-left:15px;" size="10px" readonly /><br><br>
						<span id="msg_TipoFechaGarantia" style="color: red;"></span><br>					
						<input type="radio" name="rdbFecha" id="rdbFechaCorreo" value=<%=ConstantesGenerales.TipoFechaGarantia.FECHACORREO.getTipoValor() %> style="margin-left:30px;">Fecha del Correo				
						<br><br>
						<input type="radio" name="rdbFecha" id="rdbFechaDevolucion" value=<%=ConstantesGenerales.TipoFechaGarantia.FECHADEVOLUCION.getTipoValor() %> style="margin-left:30px;">Fecha de Devolucion
					
					</div>
					
					<div id="tabs-4">
						<p><b>4. Validación de Existencia en SAP</b></p>
						<span id="msg_ExistenciaSAP" style="color: red;"></span><br><br>
						<input type="radio" name="rdbExistenciaSAP" id="rdbExistenciaSAP" value=<%=ConstantesGenerales.SI %> style="margin-left:30px;">Considerar la Existencia en SAP	
						<br><br>				
						<input type="radio" name="rdbExistenciaSAP" id="rdbNoExistenciaSAP" value=<%=ConstantesGenerales.NO %> style="margin-left:30px;">No Considerar la Existencia en SAP
					</div>
					
					<div id="tabs-5">
						<p><b>5. Establecer Modo Recojo</b></p>
						<span id="msg_ModoRecojo" style="color: red;"></span>
						<br><br>
						<input type="radio" name="rdbModoRecojo" id="rdbNivelCantidad" value=<%=ConstantesGenerales.ModoRecojo.NIVELCANTIDAD.getCriterio() %> style="margin-left:30px;"><%=ConstantesGenerales.ModoRecojo.NIVELCANTIDAD.getDescripcion()%>	
						<br><br>				
 						<input type="radio" name="rdbModoRecojo" id="rdbNivelSerie" value=<%=ConstantesGenerales.ModoRecojo.NIVELSERIE.getCriterio()%>  style="margin-left:30px;"><%=ConstantesGenerales.ModoRecojo.NIVELSERIE.getDescripcion()%>
 						<br><br>
						<input type="checkbox" name="chkSeriado" id="chkSeriado" value="SI" style="margin-left:30px;"/>Considerar Seriado / No Seriado
						<br><br>
						<div class="msg" style="width: 90%;">
							Al indicar esta opcion los materiales que tengan el estado Seriado seran <strong>VALIDADOS A NIVEL DE SERIE</strong> y si tienen 
							el estado No Seriado seran <strong>VALIDADOS A NIVEL DE CANTIDAD</strong>
						</div>
					</div>
					
					<div id="tabs-6">
						<p><b>6. Establecer Pedido y Entrega</b></p>
						<input type="checkbox" name="chkPedEntr" id="chkPedEntr" value="SI" style="margin-left:30px;"/>Asignar Pedido y Entrega
						<br><br>
							<div class="msg" style="width: 90%;">
								Al indicar esta opcion aquellas actas con dicho motivo de devolucion se le tendra que asignar Pedido y Entrega para dar la Generacion de Acta
								por finalizada.
							</div>
							
						<p><b>7. Hora Cierre Distribucion y Planeamiento</b></p>
						<input type="text" name="hDFPL" id="hDFPL" size="8px" readonly="readonly"/>											

						<p style="margin-left:30px;">* Hora (hh:mm): 
							<select id="cboHora" name="cboHora" style="width: 50px;">
							<option value="0" selected>--</option>
							</select>
							:
							<select id="cboMinuto" name="cboMinuto" style="width: 50px;">
							<option value="0" selected>--</option>
							</select>
							<span id="msg_hDFPL" style="color: red;"></span>
						</p>	
							
					</div>
					
				</div>
				<br><br>
				<input name="btnConfigurar" id="btnConfigurar" value="Configurar" type="button" style="float: right;" disabled class="button" onclick="onConfiguraMotivos();"/>
				</form>
				
			</div>
		</div>

		<jsp:include page="../pie.html" flush="true" />
	</div>
</body>
</html>