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
<script type="text/javascript" src="resources/js/configuracionSistema.js?ver=16062015A"></script>

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
		<form id="frmConfiguracion" name="frmConfiguracion" method="post" action="registro.do">
		<div id="contenido">
			<div id="tipoFormato"></div>
			<input type="hidden" name="h_tipoFormato" id="h_tipoFormato" />
				<strong>*</strong> Seleccione Motivo de Devolucion 
				<select id="cboDevolucion" name="cboDevolucion">
					<option value="-1" selected>---------------------</option>
				</select>
				<input type="hidden" name="h_devolucion" id="h_devolucion" />
			<br><br><br>
			
			<div id="tabs" style="display: none;">
					<ul>
						<li><a href="#tabs-1">1. Bodegas</a></li>
					    <li><a href="#tabs-2">2. Lote</a></li>
					    <li><a href="#tabs-3">3. Garantia</a></li>
	 					<li><a href="#tabs-4">4. SAP</a></li>
	 				    <li><a href="#tabs-5">5. Modo de Recojo</a></li> 
	 				    <li><a href="#tabs-6">6. Pedido y Entrega</a></li> 
						<li><a href="#tabs-7">7. Fecha de Corte</a></li> 
	 				    <li><a href="#tabs-8">8. Datos Material</a></li> 
					</ul>

					<br>
					<div id="titulo" style="font-size:15px;color:blue;text-decoration: underline;margin-left: 15px;font-weight: bold;"></div>
					<div id="tabs-1">
						<p><b>1. Validacion de Bodegas</b></p>
						<input type="radio" name="rdbExistenciaConfiguracionBodega" id="rdbNoExistenciaConfiguracionBodega" value=<%=ConstantesGenerales.NO %> style="margin-left:15px;" onclick="deshabilitaBodegas();" checked="checked"/>No Considerar Validación de Bodega <br><br>
						<input type="radio" name="rdbExistenciaConfiguracionBodega" id="rdbSiExistenciaConfiguracionBodega" value=<%=ConstantesGenerales.SI %> style="margin-left:15px;" onclick="habilitaBodegas();" />Considerar Validación de Bodega <br><br>
						<span id="msg_bodega" style="color: red;font-weight: bold;"></span>
						<br><br>				
						<div id="divBodegas"></div>
					</div>	
					
					<div id="tabs-2">
						<p><b>1. Validacion de Lote</b></p>
						<input type="radio" name="rdbExistenciaConfiguracionLote" id="rdbNoExistenciaConfiguracionLote" value=<%=ConstantesGenerales.NO %> style="margin-left:15px;" onclick="deshabilitaLote();" checked="checked"/>No considerar Validación de Lote <br><br>
						<input type="radio" name="rdbExistenciaConfiguracionLote" id="rdbExistenciaConfiguracionLote" value=<%=ConstantesGenerales.SI %> style="margin-left:15px;" onclick="habilitaLote();" />Considerar Validación de Lote <br><br>						
						<span id="msg_Lote" style="color: red;font-weight: bold;"></span>
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
						<input type="radio" name="rdbExistenciaConfiguracionGarantia" id="rdbNoExistenciaConfiguracionGarantia" value=<%=ConstantesGenerales.NO %> style="margin-left:15px;" onclick="deshabilitaGarantia();" checked="checked"/>No considerar Validación de Garantia <br><br>
						<input type="radio" name="rdbExistenciaConfiguracionGarantia" id="rdbSiExistenciaConfiguracionGarantia" value=<%=ConstantesGenerales.SI %> style="margin-left:15px;" onclick="habilitaGarantia();" />Considerar Validación de Garantia<br><br>																		
						<span style="margin-left:35px;"><strong>Validación de Garantia Proveedor</strong></span><br><br>
						<span style="margin-left:45px;">Dias Proveedor:</span><input type="text" name="t_diasProveedores" id="t_diasProveedores" onkeypress="validarSiNumero(this.value);"  style="margin-left:15px;" size="10px" readonly /><br><br>
						<span id="msg_GarantiaProveedor" style="color: red;font-weight: bold;"></span><br><br>
						<span style="margin-left:35px;"><strong>Validación de Garantia Taller</strong></span><br><br>
						<span style="margin-left:45px;">Dias Taller:</span><input type="text" name="t_diasTaller" id="t_diasTaller" onkeypress="validarSiNumero(this.value);"  style="margin-left:15px;" size="10px" readonly /><br><br>
						<span id="msg_GarantiaTaller" style="color: red;font-weight: bold;"></span><br><br>
						<input type="radio" name="rdbFecha" id="rdbFechaCorreo" value=<%=ConstantesGenerales.TipoFechaGarantia.FECHACORREO.getTipoValor() %> style="margin-left:30px;" checked="checked">Fecha del Correo				
						<br><br>
						<input type="radio" name="rdbFecha" id="rdbFechaDevolucion" value=<%=ConstantesGenerales.TipoFechaGarantia.FECHADEVOLUCION.getTipoValor() %> style="margin-left:30px;"> Fecha de Devolucion
						<br><br>


						<%-- 
						<input type="radio" name="rdbExistenciaConfiguracionGarantiaProveedor" id="rdbNoExistenciaConfiguracionGarantiaProveedor" value=<%=ConstantesGenerales.NO %> style="margin-left:15px;" onclick="deshabilitaGarantiaProveedor();" checked="checked"/>No considerar Validación de Garantia Proveedor <br><br>
						<input type="radio" name="rdbExistenciaConfiguracionGarantiaProveedor" id="rdbSiExistenciaConfiguracionGarantiaProveedor" value=<%=ConstantesGenerales.SI %> style="margin-left:15px;" onclick="habilitaGarantiaProveedor();" />Considerar Validación de Garantia Proveedor<br>																		
						<span id="msg_GarantiaProveedor" style="color: red;"></span>
						<br>
						<span style="margin-left:45px;">Dias Proveedor:</span><input type="text" name="t_diasProveedores" id="t_diasProveedores" onkeypress="validarSiNumero(this.value);"  style="margin-left:15px;" size="10px" readonly /><br><br>

						<span id="msg_TipoFechaGarantiaProveedor" style="color: red;"></span><br>					
						<input type="radio" name="rdbFechaProveedor" id="rdbFechaCorreoProveedor" value=<%=ConstantesGenerales.TipoFechaGarantia.FECHACORREO.getTipoValor() %> style="margin-left:30px;" checked="checked">Fecha del Correo				
						<br><br>
						<input type="radio" name="rdbFechaProveedor" id="rdbFechaDevolucionProveedor" value=<%=ConstantesGenerales.TipoFechaGarantia.FECHADEVOLUCION.getTipoValor() %> style="margin-left:30px;"> Fecha de Devolucion
						<br><br>
						<input type="radio" name="rdbExistenciaConfiguracionGarantiaTaller" id="rdbNoExistenciaConfiguracionGarantiaTaller" value=<%=ConstantesGenerales.NO %> style="margin-left:15px;" onclick="deshabilitaGarantiaTaller();" checked="checked"/>No considerar Validación de Garantia Taller <br><br>
						<input type="radio" name="rdbExistenciaConfiguracionGarantiaTaller" id="rdbSiExistenciaConfiguracionGarantiaTaller" value=<%=ConstantesGenerales.SI %> style="margin-left:15px;" onclick="habilitaGarantiaTaller();" />Considerar Validación de Garantia Taller<br>												
						<span id="msg_GarantiaTaller" style="color: red;"></span>
						<br>
						<span style="margin-left:45px;">Dias Taller:</span><input type="text" name="t_diasTaller" id="t_diasTaller" onkeypress="validarSiNumero(this.value);" style="margin-left:15px;" size="10px" readonly /><br><br>

						<span id="msg_TipoFechaGarantiaTaller" style="color: red;"></span><br>					
						<input type="radio" name="rdbFechaTaller" id="rdbFechaCorreoTaller" value=<%=ConstantesGenerales.TipoFechaGarantia.FECHACORREO.getTipoValor() %> style="margin-left:30px;" checked="checked">Fecha del Correo				
						<br><br>
						<input type="radio" name="rdbFechaTaller" id="rdbFechaDevolucionTaller" value=<%=ConstantesGenerales.TipoFechaGarantia.FECHADEVOLUCION.getTipoValor() %> style="margin-left:30px;">Fecha de Devolucion
 --%>
					
					</div>
					<div id="tabs-4">
						<p><b>4. Validación de Existencia en SAP</b></p>
						<span id="msg_ExistenciaSAP" style="color: red;"></span><br>
						<input type="radio" name="rdbExistenciaSAP" id="rdbSiExistenciaSAP" value=<%=ConstantesGenerales.SI %> style="margin-left:30px;" checked="checked" />Considerar la Existencia en SAP	
						<br><br>				
						<input type="radio" name="rdbExistenciaSAP" id="rdbNoExistenciaSAP" value=<%=ConstantesGenerales.NO %> style="margin-left:30px;" />No Considerar la Existencia en SAP
					</div>
					
					<div id="tabs-5">
						<p><b>5. Establecer Modo de Recojo</b></p>
						<span id="msg_ModoRecojo" style="color: red;"></span><br>
						<input type="radio" name="rdbModoRecojo" id="rdbNivelCantidad" value=<%=ConstantesGenerales.ModoRecojo.NIVELCANTIDAD.getCriterio() %> style="margin-left:30px;" checked="checked" /><%=ConstantesGenerales.ModoRecojo.NIVELCANTIDAD.getDescripcion()%>	
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
						
						<input type="radio" name="rdbExistenciaPedidoEntrega" id="rdbSiPedidoEntrega" value=<%=ConstantesGenerales.SI %> style="margin-left:30px;" checked="checked" />Asignar Pedido y Entrega	
						<br><br>				
						<input type="radio" name="rdbExistenciaPedidoEntrega" id="rdbNoPedidoEntrega" value=<%=ConstantesGenerales.NO %> style="margin-left:30px;" />No Asignar Pedido y Entrega	
						<br><br>
							<div class="msg" style="width: 90%;">
								Al indicar esta opcion aquellas actas con dicho motivo de devolucion se le tendra que asignar Pedido y Entrega para dar la Generacion de Acta
								por finalizada.
							</div>
					</div>
					
					<div id="tabs-7">
						<p><b>7. Hora Cierre Distribucion y Planeamiento</b></p>

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
					
					<div id="tabs-8">
						<p><b>8. Establecer valores Materiales</b></p><br>
						Provision/Planta Externa:
						<select id="cboProvisionPlantaExterna" name="cboProvisionPlantaExterna">
							<option value="-1" selected>---------------------</option>
							<option value="NOAPLICA" selected>NO APLICA</option>
							<option value="OTF">Datos OTF</option>
						</select><br><br>
						Negocio:
						<select id="cboNegocioMaterial" name="cboNegocioMaterial">
							<option value="-1" selected>---------------------</option>
							<option value="NOAPLICA" selected>NO APLICA</option>
							<option value="OTF">Datos OTF</option>
						</select><br><br>
						Tipo de Material:
						<select id="cboTipoMaterial" name="cboTipoMaterial">
							<option value="-1" selected>---------------------</option>
							<option value="NOAPLICA" selected>NO APLICA</option>
							<option value="OTF">Datos OTF</option>
						</select><br><br>
						Rubro de Material:
						<select id="cboRubroMaterial" name="cboRubroMaterial">
							<option value="-1" selected>---------------------</option>
							<option value="NOAPLICA" selected>NO APLICA</option>
							<option value="OTF">Datos OTF</option>
						</select><br><br>
						Tecnologia de Material:
						<select id="cboTecnologiaMaterial" name="cboTecnologiaMaterial">
							<option value="-1" selected>---------------------</option>
							<option value="NOAPLICA" selected>NO APLICA</option>
							<option value="OTF">Datos OTF</option>
						</select><br><br>
						Seriado/No Seriado:
						<select id="cboSeriadoNoSeriado" name="cboSeriadoNoSeriado">
							<option value="-1" selected>---------------------</option>
							<option value="NOAPLICA" selected>NO APLICA</option>
							<option value="OTF">Datos OTF</option>
						</select><br><br>
						Sociedad:
						<select id="cboSociedadMaterial" name="cboSociedadMaterial">
							<option value="-1" selected>---------------------</option>
							<option value="NOAPLICA" selected>NO APLICA</option>
							<option value="OTF">Datos OTF</option>
						</select><br><br>
						Peso:
						<select id="cboPesoMaterial" name="cboPesoMaterial">
							<option value="-1" selected>---------------------</option>
							<option value="lIMA" selected>LIMA</option>
							<option value="PROVINCIA">PROVINCIA</option>
						</select><br><br>
						Volumen:
						<select id="cboVolumenMaterial" name="cboVolumenMaterial">
							<option value="-1" selected>---------------------</option>
							<option value="lIMA" selected>LIMA</option>
							<option value="PROVINCIA">PROVINCIA</option>
						</select><br><br>
						Precio Equipo:
						<select id="cboPrecioMaterial" name="cboPrecioMaterial">
							<option value="-1" selected>---------------------</option>
							<option value="lIMA" selected>LIMA</option>
							<option value="PROVINCIA">PROVINCIA</option>
						</select><br><br>
						
					</div>
			</div>	
			<br><br>
			<input name="btnConfigurar" id="btnConfigurar" value="Configurar" type="button" style="float: right;display: none;" disabled class="button" onclick="onConfiguraMotivos();"/>
			
		</div>
		</form><br><br>
		<jsp:include page="../pie.html" flush="true" />
	</div>

</body>
</html>