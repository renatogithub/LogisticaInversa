/*
 * CREAMOS CONSTANTES PARA LOS TIPOS DE CONFIGURACION
 * */

var TipoConfiguracion = function(){
	return {
	    'BODEGA':"CONF01",
	    'LOTE':"CONF02",
	    'GARANTIAPROVEEDOR':"CONF03",
	    'GARANTIATALLER':"CONF04",
	    'EXISTENCIASAP':"CONF05",
	    'MODORECOJO':"CONF06",
		'CIERREPROCESO':"CONF07",
		'GENERACIONPEDIDOENTREGA':"CONF08"
	    };
	}();
	
	
//Creamos Constantes para los Tipo de Configuracion

var TipoFormato = function(){
	return {
		'OTROS':"FRT0001",
		'SERIADO':"FRT0002",
		'NOSERIADO':"FRT0003"
		};
	}();	
	
//Parte de la Configuracion para los Tabs

$(function() {
    $( "#tabs" ).tabs();
 });

//Escribe los Tipo de Formato de Devolución

$(document).ready(
	function() {
		$.ajax({
			type : 'GET',
			url : 'listaFormato.do',
			dataType : 'json',
			success : function(result) {
				$("#tipoFormato").append("<br/>");
				for (var x = 0; x < result.length; x++) {
					$("#tipoFormato").append("<input type='radio' name='formato' value='" + result[x].codTipoFormato + "' id='formato' onChange='elegirMotivo(this)'>" + result[x].nomTipoFormato + "&nbsp;&nbsp;");
				}					
					$("#tipoFormato").append("<span id='msg_formato' style='color: red;'></span> <br /><br />");
				}
			});
	});

//Llenamos los motivos de Devolucion en el Select

function elegirMotivo(radio){
	$("#cboDevolucion").html('');
	$("#h_tipoFormato").val(radio.value);
	$('#h_devolucion').val('');				
	$.ajax({
		type : 'GET',
		url : 'listaDevolucion.do?formato=' + radio.value,
		dataType : 'json',
		success : function(result) {
			document.getElementById('tabs').style.display = 'none';
			document.getElementById('btnConfigurar').style.display = 'none';			
			$("#cboDevolucion").append("<option value=''>---------------------</option>");
			for (var x = 0; x < result.length; x++) {
				$("#cboDevolucion").append("<option value='" + result[x].codDevol + "' onClick=elegirConfiguracion(this);>" + result[x].nomDevol + "</option>");
				}
			}
	});
}

//Elegimos el motivo Devolucion

$(document).ready(
		function() {
			$("#cboDevolucion").change(
			function() {
				var codDevolucion = $("#cboDevolucion option:selected").val();
				$('#h_devolucion').val(codDevolucion);
				document.getElementById('tabs').style.display = 'block';
				document.getElementById('btnConfigurar').style.display = 'block';
				document.getElementById('btnConfigurar').disabled=false;
				elegirConfiguracion(codDevolucion);				
			});
});

function elegirConfiguracion(combo){
	$("#divBodegas").html('');	
	var existe=0;
	$.ajax({
		type : 'GET',
		url : 'listaTipoBodegas.do',
		dataType : 'json',
		success : function(dataBodega) {
			$.ajax({
				type : 'GET',
			    url : 'listaConfiguracion.do?criterio=' + combo,
				dataType : 'json',
				success : function(dataConfiguracion) {
					$("#divBodegas").html('');
					for (var x = 0; x < dataBodega.length; x++) {
						n=0;
						for (var y = 0; y < dataConfiguracion.length; y++) {
							if(dataBodega[x].codTipoBodegas==dataConfiguracion[y].valor){
								existe=1;
								n=1;
								$("#divBodegas").append("<input type='checkbox' name='chkTipoBodega[]' id='TpBodega' value='" + dataBodega[x].codTipoBodegas + "' style='margin-left:30px;' checked>" + dataBodega[x].nomTipoBodegas + "<br><br>");
							}
						}
						
						if(n==0){
							$("#divBodegas").append("<input type='checkbox' name='chkTipoBodega[]' id='TpBodega' value='" + dataBodega[x].codTipoBodegas + "' style='margin-left:30px;'>" + dataBodega[x].nomTipoBodegas + "<br><br>");
						}
					
					}
					
				}
				});			
		}
		});
	
	
	$("#lst_Lote").empty();
	
	$.ajax({
	    type : 'GET',
	    url : 'listaConfiguracion.do?criterio=' + combo,
		dataType : 'json',
		success : function(resultConfiguracion) {
			var existeLote=0;
			$("#" + TipoConfiguracion.GARANTIAPROVEEDOR).prop("checked", "");
			$("#t_diasProveedores").val('');
			$("#" + TipoConfiguracion.GARANTIATALLER).prop("checked", "");
			$("#t_diasTaller").val('');
			for(var t=0;t<resultConfiguracion.length;t++){
				
				if(resultConfiguracion[t].codConfiguracion==TipoConfiguracion.LOTE){
					if(resultConfiguracion[t].valor!="-"){
						var existeLote=1;
						$("#lst_Lote").append(
								"<option value='"
										+ resultConfiguracion[t].valor + "'>"
										+ resultConfiguracion[t].valor
										+ "</option>");
						
					}
					
					if(existeLote==0){
						$("#rdbNoExistenciaConfiguracionLote").prop("checked","checked");
						var btnAgregarLote=document.getElementById("btnAgregaLote");
						var btnQuitarLote=document.getElementById("btnQuitarLote");	
						btnAgregarLote.disabled=true;
						btnQuitarLote.disabled=true;
					}else{
						$("#rdbExistenciaConfiguracionLote").prop("checked","checked");
						var btnAgregarLote=document.getElementById("btnAgregaLote");
						var btnQuitarLote=document.getElementById("btnQuitarLote");	
						btnAgregarLote.disabled=false;
						btnQuitarLote.disabled=false;
					}
					
				}
				
				
				if(resultConfiguracion[t].codConfiguracion==TipoConfiguracion.BODEGA){
					if(resultConfiguracion[t].existencia=="SI"){
						$("#rdbSiExistenciaConfiguracionBodega").prop("checked", "checked");						
					}
					if(resultConfiguracion[t].existencia=="NO"){
						$("#rdbNoExistenciaConfiguracionBodega").prop("checked", "checked");						
					}										
				}
				
				if((resultConfiguracion[t].codConfiguracion==TipoConfiguracion.GARANTIAPROVEEDOR) || (resultConfiguracion[t].codConfiguracion==TipoConfiguracion.GARANTIATALLER)){
					if(resultConfiguracion[t].codConfiguracion==TipoConfiguracion.GARANTIAPROVEEDOR){
						if(resultConfiguracion[t].existencia=="NO"){
							$("#rdbNoExistenciaConfiguracionGarantia").prop("checked", "checked");	
						}
						
						if(resultConfiguracion[t].existencia=="SI"){
							$("#rdbSiExistenciaConfiguracionGarantia").prop("checked", "checked");	
						}
						
						
						$("#t_diasProveedores").val(resultConfiguracion[t].valor);
						
						if(resultConfiguracion[t].parametro=="FECHASOLICITUD"){
							$("#rdbFechaCorreo").prop("checked", "checked");												
						}
						if(resultConfiguracion[t].parametro=="FECHADEVOLUCION"){
							$("#rdbFechaDevolucion").prop("checked", "checked");						
						}
					}
					
					if(resultConfiguracion[t].codConfiguracion==TipoConfiguracion.GARANTIATALLER){
						if(resultConfiguracion[t].existencia=="NO"){
							$("#rdbNoExistenciaConfiguracionGarantia").prop("checked", "checked");		
						}
						if(resultConfiguracion[t].existencia=="SI"){
							$("#rdbSiExistenciaConfiguracionGarantia").prop("checked", "checked");	
						}
						
						$("#t_diasTaller").val(resultConfiguracion[t].valor);
						
						if(resultConfiguracion[t].parametro=="FECHASOLICITUD"){
							$("#rdbFechaCorreo").prop("checked", "checked");												
						}
						if(resultConfiguracion[t].parametro=="FECHADEVOLUCION"){
							$("#rdbFechaDevolucion").prop("checked", "checked");						
						}
					}
					
				}

				if(resultConfiguracion[t].codConfiguracion==TipoConfiguracion.EXISTENCIASAP){
					if(resultConfiguracion[t].existencia=="SI"){
						$("#rdbSiExistenciaSAP").prop("checked", "checked");						
					}
					if(resultConfiguracion[t].existencia=="NO"){
						$("#rdbNoExistenciaSAP").prop("checked", "checked");						
					}	
				}
				
				
				if(resultConfiguracion[t].codConfiguracion==TipoConfiguracion.MODORECOJO){
					if(resultConfiguracion[t].valor=="NIVELCANTIDAD"){
						$("#rdbNivelCantidad").prop("checked", "checked");						
					}
					if(resultConfiguracion[t].valor=="NIVELSERIE"){
						$("#rdbNivelSerie").prop("checked", "checked");						
					}
					
					if(resultConfiguracion[t].parametro=="SI"){
						$("#chkSeriado").prop("checked", "checked");						
					}else{
						$("#chkSeriado").prop("checked", "");						
					}

				}
				
				if(resultConfiguracion[t].codConfiguracion==TipoConfiguracion.GENERACIONPEDIDOENTREGA){
					if(resultConfiguracion[t].existencia=="SI"){
						$("#rdbSiPedidoEntrega").prop("checked", "checked");						
					}
					if(resultConfiguracion[t].existencia=="NO"){
						$("#rdbNoPedidoEntrega").prop("checked", "checked");						
					}

				}
				
				
				
			}	
	
		}

	});
	
}


function removerLote() {
    var x = document.getElementById("lst_Lote");
    x.remove(x.selectedIndex);
    obtieneValoresLote();
}

function obtieneValoresLote(){	
	$("#divLotes").html('');
	 $('select#lst_Lote').find('option').each(function() {
		 $("#divLotes").append("<input type='hidden' name='t_lote[]' id='t_lote' value='" + $(this).val() + "'/>");
 	});
}

function addLote() {
	var lote = prompt("Ingrese un Lote", "");
	var lstLote = document.getElementById("lst_Lote");
	var option = document.createElement("option");
	if(lote.trim()!="" && lote!=null){
		option.text = lote.toUpperCase();
		lstLote.add(option);
		obtieneValoresLote();
	}

}

function deshabilitaBodegas(){
	var chkBodegas=document.getElementsByName("chkTipoBodega[]");
	for(var i=0;i<chkBodegas.length;i++){
		chkBodegas[i].disabled=true;
	}
}

function habilitaBodegas(){
	var chkBodegas=document.getElementsByName("chkTipoBodega[]");
	for(var i=0;i<chkBodegas.length;i++){
		chkBodegas[i].disabled=false;
	}
}

function deshabilitaLote(){
	var lstLote=document.getElementById("lst_Lote");
	var btnAgregaLote=document.getElementById("btnAgregaLote");
	var btnQuitarLote=document.getElementById("btnQuitarLote");
	lstLote.disabled=true;
	btnAgregaLote.disabled=true;
	btnQuitarLote.disabled=true;
}

function habilitaLote(){
	var lstLote=document.getElementById("lst_Lote");
	var btnAgregaLote=document.getElementById("btnAgregaLote");
	var btnQuitarLote=document.getElementById("btnQuitarLote");
	lstLote.disabled=false;
	btnAgregaLote.disabled=false;
	btnQuitarLote.disabled=false;
}

function deshabilitaGarantia(){
	var diasProveedor=document.getElementById("t_diasProveedores");
	var diasTaller=document.getElementById("t_diasTaller");
	var rdbFechaCorreo=document.getElementById("rdbFechaCorreo");
	var rdbFechaDevolucion=document.getElementById("rdbFechaDevolucion");
	diasProveedor.disabled=true;
	diasTaller.disabled=true;
	rdbFechaCorreo.disabled=true;
	rdbFechaDevolucion.disabled=true;
	$("#t_diasProveedores").prop("readonly", true);
	$("#t_diasTaller").prop("readonly", true);
}


function habilitaGarantia(){
	var diasProveedor=document.getElementById("t_diasProveedores");
	var diasTaller=document.getElementById("t_diasTaller");
	var rdbFechaCorreo=document.getElementById("rdbFechaCorreo");
	var rdbFechaDevolucion=document.getElementById("rdbFechaDevolucion");
	diasProveedor.disabled=false;
	diasTaller.disabled=false;
	rdbFechaCorreo.disabled=false;
	rdbFechaDevolucion.disabled=false;
	$("#t_diasProveedores").prop("readonly", false);
	$("#t_diasTaller").prop("readonly", false);
}

function onConfiguraMotivos() {
	$("#dialog:ui-dialog").dialog("destroy");
	$("#dialog").text("¿Seguro que desea Grabar la Configuracion?");
    $("#dialog").dialog({
    	width:350,
        height:150,
        modal: true,
        buttons: {
          Si: function() {
        	  mensaje=onGrabarConfiguracion();
        	  $( "#dialog:ui-dialog" ).dialog( "destroy" );
        	  $( "#dialog" ).dialog({
                  autoOpen: false,
  		          width:350,
		          height:150,
                  modal: true,
                  buttons: {
                      Ok: function() {
                          $( this ).dialog( "close" );
                      }
                  }
              });
        	  $("#dialog").text(mensaje);
              $("#dialog").dialog("open");
          },
          No: function() {
            $( this ).dialog( "close" );
          }
        }
      });  
}

function onGrabarConfiguracion(){
	var mensaje="";
	if (validarConfiguracion()) {
		mensaje="Configuracion Registrada";
		$('form').submit();
	}else{
		mensaje="La configuracion no se ha registrado debido a observaciones encontradas";
	}
	return mensaje;
}

function validarConfiguracion(){
	var isConfValid = true;

	if (validaBodega() == false) {
		isConfValid = false;
	}
	
	if (validaLote() == false) {
		isConfValid = false;
	}	
	
	if(validaGarantiaProveedor()==false){
		isConfValid=false;
	}
	
	if(validaGarantiaTaller()==false){
		isConfValid=false;
	}
	
/*	if (validaLote() == false) {
		isConfValid = false;
	}	

	if(validaGarantia()==false){
		isConfValid=false;
	}
	
	if (validaExistenciaSAP() == false) {
		isConfValid = false;
	}
		
	if (validaModoRecojo() == false) {
		isConfValid = false;
	}
	
	if (validaExistenciaHora() == false) {
		isConfValid = false;
	}
*/	return isConfValid;
}

function validarSiNumero(numero){
	if ((event.keyCode < 48) || (event.keyCode > 57)) 
		  event.returnValue = false;
 }


function validaBodega() {
	var isValid = true;
	var cantidad=0;
	if($("#rdbSiExistenciaConfiguracionBodega").is(':checked')) {
		chkTipoBodega=document.frmConfiguracion.elements['chkTipoBodega[]'];
		for (var x=0; x < chkTipoBodega.length; x++) {
			if(chkTipoBodega[x].checked==true){
				cantidad=cantidad+1;				
			}		
		}
		
		if(cantidad==0){
			isValid=false;
			$('#msg_bodega').html('Debe seleccionar un Tipo de Bodega').show();
		}else{
			$('#msg_bodega').hide();
			isValid=true;
		}		
		
	}else{
		chkTipoBodega=document.frmConfiguracion.elements['chkTipoBodega[]'];
		for (var x=0; x < chkTipoBodega.length; x++) {
			if(chkTipoBodega[x].checked==true){
				cantidad=cantidad+1;				
			}		
		}
		
		if(cantidad==0){
			isValid=true;
		}else{
			isValid=false;
			$('#msg_bodega').html('Seleccione la Configuracion Bodega').show();
		}
			
	}
	return isValid;
}

function validaLote() {
	var isValid = true;
	var cantidad=0;
	if($("#rdbExistenciaConfiguracionLote").is(':checked')) {
		isValid=true;
		
		$('select#lst_Lote').find('option').each(function() {
			cantidad=cantidad+1;
	 	});
		 
		if(cantidad==0){
			isValid=false;
			$('#msg_Lote').html('Debe adicionar un Lote').show();
		}else{
			isValid=true;
		}		
		
	}else{
		
		$('select#lst_Lote').find('option').each(function() {
			cantidad=cantidad+1;
	 	});
		 
		if(cantidad==0){
			isValid=true;			
		}else{
			isValid=false;
			$('#msg_Lote').html('Seleccione la Configuracion de Lote').show();
		}

	}
	return isValid;
}


function validaGarantiaProveedor() {
	var isValidGarantiaProveedor = true;
	if($("#rdbSiExistenciaConfiguracionGarantia").is(':checked')) {
		var dias=$('#t_diasProveedores').val();
		if((dias.length==0 && dias=="") || (dias=="-")){
			isValidGarantiaProveedor=false;
			$('#msg_GarantiaProveedor').html('Debe ingresar una cantidad de dias Proveedor').show();	
		}
	}
	return isValidGarantiaProveedor;
}

function validaGarantiaTaller() {
	var isValidGarantiaTaller = true;
	if($("#rdbSiExistenciaConfiguracionGarantia").is(':checked')) {
		var dias=$('#t_diasTaller').val();
		if((dias.length==0 && dias=="") || (dias=="-")){
			isValidGarantiaTaller=false;
			$('#msg_GarantiaTaller').html('Debe ingresar una cantidad de dias Taller').show();	
		}
	}
	return isValidGarantiaTaller;
}


