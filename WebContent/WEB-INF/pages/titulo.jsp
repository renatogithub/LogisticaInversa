<%@ page import="pe.tgestiona.logistica.inversa.bean.ConstantesGenerales"%>
<%@ page import="pe.tgestiona.logistica.inversa.util.Util"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% HttpSession sesion = request.getSession(true);%>
<% Util util=new Util();%>

<div id="top">
	<b>Perfil: </b><%= sesion.getAttribute("nomperfil") %> <b> | </b>
	<b>Usuario: </b><%= sesion.getAttribute("usuario") %> <b> | Fecha:</b> <%=util.obtenerFechaLetras() %>
	<b> | </b><a style='color:white;' href="logout.do">Cerrar Sesion</a>
	<%if(sesion.getAttribute("perfil").equals(ConstantesGenerales.PerfilUsuario.ADMINISTRADOR.getTipoValor())){ %>
		<b> | </b><a style='color:white;' href="configuracion.do">Configurar</a>
<%} %>

</div>
<div id="logo"></div>
<br />
<br />


<div id="menu">
<ul>
<%if(sesion.getAttribute("perfil").equals(ConstantesGenerales.PerfilUsuario.CONSULTA.getTipoValor())){ %>
  <li class="nivel1"><a href="transacciones.do" class="nivel1"><img src="resources/icon/home.jpg" width="15%">&nbsp;&nbsp;Inicio</a></li>
<%} %>

<%if(!sesion.getAttribute("perfil").equals(ConstantesGenerales.PerfilUsuario.CONSULTA.getTipoValor())){ %>
  <li class="nivel1"><a href="#" class="nivel1"><img src="resources/icon/transacciones.png" width="15%">&nbsp;&nbsp;Transacciones</a>
	<ul>
		<li><a href="registroActa.do">Registrar Ficha Devolucion</a></li>
		<li><a href="inicioProceso.do">Validar Ficha(s) Devolucion(es)</a></li>
		<li><a href="procesoPendiente.do">Validar Ficha(s) Pendiente(s)</a></li>
		<li><a href="asignarPedidoEntrega.do">Asignar Pedido y Entrega</a></li>
		<li><a href="asignarFechaRecojo.do">Asignar Fecha Recojo</a></li>
		<li><a href="consultarActaGeneradas.do">Consultar Actas Generadas</a></li>
		<li><a href="consultarMaterial.do">Consultar Material</a></li>
		<li><a href="anularFicha.do">Anular Ficha</a></li>	
	<!-- 	<li><a href="migrid.do">Ver Grid</a></li>	 -->
	</ul>
  </li>
  <li class="nivel1"><a href="#" class="nivel1"><img src="resources/icon/transporte.png" width="15%">&nbsp;&nbsp;Dist. Fisica</a>
  	<ul>
		<li><a href="cargaDistribucion.do">Cargar Fuente Distribucion</a></li>
	</ul>
  </li>
  <li class="nivel1"><a href="#" class="nivel1"><img src="resources/icon/calendario.jpg" width="15%">&nbsp;&nbsp;Planeamiento</a>
    <ul>
		<li><a href="cargaPlaneamiento.do">Cargar Fuente Planeamiento</a></li>
	</ul>
  </li>
  <li class="nivel1"><a href="#" class="nivel1"><img src="resources/icon/acopio.png" width="15%">&nbsp;&nbsp;Acopio</a>
	  <ul>
  		<li><a href="consultarRRSS.do">Consultar RRSS</a></li>
  	  </ul>	  
  </li>  
  
<%} %>
 <li class="nivel1"><a href="#" class="nivel1"><img src="resources/icon/baseunica.png" width="15%">&nbsp;&nbsp;Base Unica</a></li>



<%if(sesion.getAttribute("perfil").equals(ConstantesGenerales.PerfilUsuario.ADMINISTRADOR.getTipoValor())){ %>
  <li class="nivel1"><a href="#" class="nivel1"><img src="resources/icon/mantenimiento.png" width="15%">&nbsp;&nbsp;Mantenimiento</a>
	<ul>
		<li><a href="#"  style="width: 100%;">Usuarios</a></li>
		<li><a href="addMaterial.do"  style="width: 100%;">Material</a></li>
		<li><a href="#"  style="width: 100%;">Tramos Minimos</a></li>
		<li><a href="#"  style="width: 100%;">Canal</a></li>
		<li><a href="#"  style="width: 100%;">Entidad</a></li>
	</ul>
  </li>
<%} %>

<!--   <li class="nivel1"><a href="#" class="nivel1"><img src="resources/icon/mantenimiento.png" width="15%">&nbsp;&nbsp;Dist. Fisica</a>
  	<ul>
		<li><a href="cargaDistribucion.do">Usuarios</a></li>
	</ul>
  </li> -->

</ul>
</div>
 
<div class="line"></div>