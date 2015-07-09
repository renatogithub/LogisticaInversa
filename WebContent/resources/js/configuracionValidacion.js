//Se encargara de mostrar al lado izquierdo las motivos de devolucion

$(document).ready(
  function() {
	$.ajax({
	  type : 'GET',
		url : 'listaDevolucionMotivos.do',
		dataType : 'json',
		success : function(result) {
			for (var x = 0; x < result.length; x++) {
				$("#opciones").append("<b><a onclick='opcionesConfigurar(this);' id='" +result[x].codDevol + "' style='cursor:pointer;'><img src='resources/icon/flecha.png' width='8%'>" + result[x].tipoFormato + " - " + result[x].nomDevol + "</a></b><br>");
			}
			$("#opciones").append("<div class='linea' style='width: 60%;'></div>");
		}
	});
  });

//Parte de la Configuracion para los Tabs

$(function() {
    $( "#tabs" ).tabs();
 });



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

//Verifica cada una de las configuraciones.

function validarConfiguracion(){
	var isConfValid = true;

	if (validaBodega() == false) {
		isConfValid = false;
	}
	
	if (validaLote() == false) {
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
	
/*	if (validaExistenciaHora() == false) {
		isConfValid = false;
	}
*/	return isConfValid;
}


//Creamos Constantes para los Tipo de Configuracion

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
	

//Obtener Hora
	
	$(document).ready(
			function() {
				$("#cboHora").change(
				function() {
					var hora = $("#cboHora option:selected").val();
					var minuto = $("#cboMinuto option:selected").val();
					$('#hDFPL').val(hora+":"+minuto);				
				});
	});

	$(document).ready(
			function() {
				$("#cboMinuto").change(
				function() {
					var hora = $("#cboHora option:selected").val();
					var minuto = $("#cboMinuto option:selected").val();
					$('#hDFPL').val(hora+":"+minuto);
	});
});	
	

function validaBodega() {
	var isValid = true;
	var cantidad=0;
	if($("#" + TipoConfiguracion.BODEGA).is(':checked')) {
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
	if($("#" + TipoConfiguracion.LOTE).is(':checked')) {
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

function validaGarantia(){
	var isValidProveedor=true;	
	var isValidTaller=true;	
	var existemsgFechaProveedor=true;
	var existemsgFechaTaller=true;
	var isValid=true;
	var existeTipoFecha=false;
	var ele = document.getElementsByName("rdbFecha");
	var i = ele.length;	
	for (var j = 0; j < i; j++) {
	    if (ele[j].checked) { //index has to be j.
	    	existeTipoFecha=true;
	    }
	}	
	
	if($("#" + TipoConfiguracion.GARANTIAPROVEEDOR).is(':checked')) {
		var dias=$('#t_diasProveedores').val();
		if((dias.length==0 && dias=="") && (existeTipoFecha==false)){
			isValidProveedor=false;
			$('#msg_GarantiaProveedor').html('Debe ingresar una cantidad de dias').show();
			existemsgFechaProveedor=true;
		}else if((dias.length==0 && dias=="") && (existeTipoFecha==true)){
			isValidProveedor=false;
			$('#msg_GarantiaProveedor').html('Debe ingresar una cantidad de dias').show();
			existemsgFechaProveedor=false;
		}else if((dias.length>0 && dias!="") && (existeTipoFecha==true)){
			isValidProveedor=true;
			$('#msg_GarantiaProveedor').hide();
			existemsgFechaProveedor=false;
		}else if((dias.length>0 && dias!="") && (existeTipoFecha==false)){
			isValidProveedor=false;
			$('#msg_GarantiaProveedor').hide();
			existemsgFechaProveedor=true;
		}
	}else{
		var dias=$('#t_diasProveedores').val();
		if((dias.length==0 && dias=="") && (existeTipoFecha==false)){
			isValidProveedor=true;
			$('#msg_GarantiaProveedor').hide();
			existemsgFechaProveedor=false;
		}else if((dias.length==0 && dias=="") && (existeTipoFecha==true)){
			isValidProveedor=false;
			$('#msg_GarantiaProveedor').html('Seleccione la Configuracion de Garantia Proveedor').show();
			existemsgFechaProveedor=false;
		}else if((dias.length>0 && dias!="") && (existeTipoFecha==true)){
			isValidProveedor=false;
			$('#msg_GarantiaProveedor').html('Seleccione la Configuracion de Garantia Proveedor').show();
			existemsgFechaProveedor=false;
		}else if((dias.length>0 && dias!="") && (existeTipoFecha==false)){
			isValidProveedor=false;
			$('#msg_GarantiaProveedor').html('Seleccione la Configuracion de Garantia Proveedor').show();
			existemsgFechaProveedor=true;
		}
		
	}
	
	
	if($("#" + TipoConfiguracion.GARANTIATALLER).is(':checked')) {
		var dias=$('#t_diasTaller').val();
		if((dias.length==0 && dias=="") && (existeTipoFecha==false)){
			isValidTaller=false;
			$('#msg_GarantiaTaller').html('Debe ingresar una cantidad de dias').show();
			existemsgFechaTaller=true;
		}else if((dias.length==0 && dias=="") && (existeTipoFecha==true)){
			isValidTaller=false;
			$('#msg_GarantiaTaller').html('Debe ingresar una cantidad de dias').show();
			existemsgFechaTaller=false;
		}else if((dias.length>0 && dias!="") && (existeTipoFecha==true)){
			isValidTaller=true;
			$('#msg_GarantiaTaller').hide();
			existemsgFechaTaller=false;
		}else if((dias.length>0 && dias!="") && (existeTipoFecha==false)){
			isValidTaller=false;
			$('#msg_GarantiaTaller').hide();
			existemsgFechaTaller=true;
		}
	}else{
		var dias=$('#t_diasTaller').val();
		if((dias.length==0 && dias=="") && (existeTipoFecha==false)){
			isValidTaller=true;
			$('#msg_GarantiaTaller').hide();
			existemsgFechaTaller=false;
		}else if((dias.length==0 && dias=="") && (existeTipoFecha==true)){
			isValidTaller=false;
			$('#msg_GarantiaTaller').html('Seleccione la Configuracion de Garantia Taller').show();
			existemsgFechaTaller=false;
		}else if((dias.length>0 && dias!="") && (existeTipoFecha==true)){
			isValidTaller=false;
			$('#msg_GarantiaTaller').html('Seleccione la Configuracion de Garantia Taller').show();
			existemsgFechaTaller=false;
		}else if((dias.length>0 && dias!="") && (existeTipoFecha==false)){
			isValidTaller=false;
			$('#msg_GarantiaTaller').html('Seleccione la Configuracion de Garantia Taller').show();
			existemsgFechaTaller=true;
		}
		
	}

	
	if(isValidProveedor==false || isValidTaller==false){
		isValid=false;		
		if(existemsgFechaProveedor==false || existemsgFechaTaller==false){
			$('#msg_TipoFechaGarantia').html('Seleccione un Tipo de Fecha Garantia').show();		
		}else{
			$('#msg_TipoFechaGarantia').hide();		
		}
	}else{
		isValid=true;
	}
	
	return isValid;
}



function validaExistenciaSAP(){
	var isValid = false;
	var ele = document.getElementsByName("rdbExistenciaSAP");
	var i = ele.length;	
	for (var j = 0; j < i; j++) {
	    if (ele[j].checked) { //index has to be j.
	    	isValid=true;
	    }
	}

	if(isValid==false){
		$('#msg_ExistenciaSAP').html('Seleccione un Tipo de Existencia SAP').show();
	}
	
	return isValid;
			
}


function validaExistenciaHora(){
	var isValid = false;
	
	var hcorreo=$("#hDFPL").val();
		
	if(hcorreo.length!=5){			
		isValid = false;
		$('#msg_hDFPL').html('Ingrese una Hora Correcta').show();
	} else {
		isValid = true;
		$('#msg_hDFPL').html('').hide();
	}	
	return isValid;
}



function validaModoRecojo(){
	var isValid = false;
	var ele = document.getElementsByName("rdbModoRecojo");
	var i = ele.length;	
	for (var j = 0; j < i; j++) {
	    if (ele[j].checked) { //index has to be j.
	    	isValid=true;
	    }
	}
	
	if(isValid==false){
		$('#msg_ModoRecojo').html('Seleccione un Modo de Recojo').show();
	}
	
	return isValid;			
}

function validaTipoFechaGarantia(){
	var isValid=false;
	var existeTipoFecha=false;
	var ele = document.getElementsByName("rdbFecha");
	var i = ele.length;	
	for (var j = 0; j < i; j++) {
	    if (ele[j].checked) { //index has to be j.
	    	existeTipoFecha=true;
	    }
	}
	
	
	if($("#" + TipoConfiguracion.GARANTIAPROVEEDOR).is(':checked')) {
		if(existeTipoFecha==false){
			$('#msg_TipoFechaGarantia').html('Seleccione un Tipo de Fecha Garantia').show();
			isValid=false;
		}else{
			$('#msg_TipoFechaGarantia').hide();
			isValid=true;
		}
	}else{
		if(existeTipoFecha==false){
			$('#msg_TipoFechaGarantia').hide();
			isValid=true;
		}else{
			$('#msg_TipoFechaGarantia').hide();
			isValid=true;
		}
	}
	
	
	if($("#" + TipoConfiguracion.GARANTIATALLER).is(':checked')) {
		if(existeTipoFecha==false){
			$('#msg_TipoFechaGarantia').html('Seleccione un Tipo de Fecha Garantia').show();
		}else{
	    	$('#msg_TipoFechaGarantia').hide();
		}
	}else{
    	isValid=true;
    	$('#msg_TipoFechaGarantia').hide();
	}
	
	return isValid;
			
}




function validarSiNumero(numero){
	if ((event.keyCode < 48) || (event.keyCode > 57)) 
		  event.returnValue = false;
  }

function getTipoBodega(){
	 $('select#lst_Lote').find('option').each(function() {
		 $("#divLotes").append("<input type='hidden' name='t_lote[]' id='t_lote' value='" + $(this).val() + "'/>");
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


function habilitaGarantiaProveedores(myId){
	if($(myId).is(':checked')) {
		$("#t_diasProveedores").prop("readonly", false);
	}else{
		$("#t_diasProveedores").prop("readonly", true);		
	}
}

function habilitaGarantiaTaller(myId){
	if($(myId).is(':checked')) {
		$("#t_diasTaller").prop("readonly", false);
	}else{
		$("#t_diasTaller").prop("readonly", true);		
	}
}

function opcionesConfigurar(myId){
	$("#" + TipoConfiguracion.BODEGA).prop("checked", "");
	$("#" + TipoConfiguracion.LOTE).prop("checked", "");
	$("#" + TipoConfiguracion.GARANTIAPROVEEDOR).prop("checked", "");
	$("#" + TipoConfiguracion.GARANTIATALLER).prop("checked", "");
	$("#" + TipoConfiguracion.EXISTENCIASAP).prop("checked", "");
	$("#" + TipoConfiguracion.MODORECOJO).prop("checked", "");
	$("#" + TipoConfiguracion.CIERREPROCESO).prop("checked", "");
	$("#chkPedEntr").prop("checked", "");
	$("#rdbFechaCorreo").prop("checked", "");
	$("#rdbFechaDevolucion").prop("checked", "");
	$("#rdbModoRecojo").prop("checked", "");
	$("#divBodegas").html('');
	$("#t_diasProveedores").val("");
	$("#t_diasTaller").val("");
	$("#t_criterio").val(myId.id);
	$("#titulo").text(myId.text);
	document.getElementById("btnConfigurar").disabled = false;
	var n=0;
	
	$.ajax({
		type : 'GET',
		url : 'listaTipoBodegas.do',
		dataType : 'json',
		success : function(dataBodega) {
			$.ajax({
				type : 'GET',
			    url : 'listaConfiguracion.do?criterio=' + myId.id,
				dataType : 'json',
				success : function(dataConfiguracion) {
					$("#divBodegas").html('');
					for (var x = 0; x < dataBodega.length; x++) {
						n=0;
						for (var y = 0; y < dataConfiguracion.length; y++) {
							if(dataBodega[x].codTipoBodegas==dataConfiguracion[y].valor){
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

/*	$.ajax({
	    type : 'GET',
		url : 'listaTipoBodegas.do',
		dataType : 'json',
		success : function(resultBodega) {
			$.ajax({
			    type : 'GET',
			    url : 'listaConfiguracion.do?criterio=' + myId.id,
				dataType : 'json',
				success : function(resultConfiguracion) {
					$("#divBodegas").html('');
					for (var x = 0; x < resultBodega.length; x++) {
						n=0;
						for(var t=0;t<resultConfiguracion.length;t++){
							if(resultBodega[x].codTipoBodegas==resultConfiguracion[t].valor){
								n=1;
								$("#divBodegas").append("<input type='checkbox' name='chkTipoBodega[]' id='TpBodega' value='" + resultBodega[x].codTipoBodegas + "' style='margin-left:30px;' checked>" + resultBodega[x].nomTipoBodegas + "<br><br>");
							}
						}
						
							if(n==0){
								$("#divBodegas").append("<input type='checkbox' name='chkTipoBodega[]' id='TpBodega' value='" + resultBodega[x].codTipoBodegas + "' style='margin-left:30px;'>" + resultBodega[x].nomTipoBodegas + "<br><br>");
							}
					}

				}
			
			});
			
		}

	});*/

	$("#lst_Lote").empty();
	
	
	$.ajax({
	    type : 'GET',
	    url : 'listaConfiguracion.do?criterio=' + myId.id,
		dataType : 'json',
		success : function(resultConfiguracion) {
			for(t=0;t<resultConfiguracion.length;t++){
				if(resultConfiguracion[t].codConfiguracion==TipoConfiguracion.BODEGA){
					$("#" + TipoConfiguracion.BODEGA).prop("checked", "checked");
				}
				
				if(resultConfiguracion[t].codConfiguracion==TipoConfiguracion.LOTE){
					$("#" + TipoConfiguracion.LOTE).prop("checked", "checked");
					$("#lst_Lote").append(
							"<option value='"
									+ resultConfiguracion[t].valor + "'>"
									+ resultConfiguracion[t].valor
									+ "</option>");
					
					$("#divLotes").append("<input type='hidden' name='t_lote[]' id='t_lote' value='" + resultConfiguracion[t].valor + "'/>");
				}
				
				if((resultConfiguracion[t].codConfiguracion==TipoConfiguracion.GARANTIAPROVEEDOR) || (resultConfiguracion[t].codConfiguracion==TipoConfiguracion.GARANTIATALLER)){
					if(resultConfiguracion[t].parametro=="FECHASOLICITUD"){
						$("#rdbFechaCorreo").prop("checked", "checked");	
					}
					if(resultConfiguracion[t].parametro=="FECHADEVOLUCION"){
						$("#rdbFechaDevolucion").prop("checked", "checked");	
					}
				}
				
				if(resultConfiguracion[t].codConfiguracion==TipoConfiguracion.GARANTIAPROVEEDOR){
					$("#" + TipoConfiguracion.GARANTIAPROVEEDOR).prop("checked", "checked");
					$("#t_diasProveedores").val(resultConfiguracion[t].valor);
				}
				
				if(resultConfiguracion[t].codConfiguracion==TipoConfiguracion.GARANTIATALLER){
					$("#" + TipoConfiguracion.GARANTIATALLER).prop("checked", "checked");
					$("#t_diasTaller").val(resultConfiguracion[t].valor);
				}
				
				if(resultConfiguracion[t].codConfiguracion==TipoConfiguracion.EXISTENCIASAP){
					if(resultConfiguracion[t].valor=="SI"){
						$("#rdbExistenciaSAP").prop("checked", "checked");						
					}
					if(resultConfiguracion[t].valor=="NO"){
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
					}

				}
				
				if(resultConfiguracion[t].codConfiguracion==TipoConfiguracion.CIERREPROCESO){
					
					var valor=resultConfiguracion[t].valor;
					var hora="";
					var minuto="";
					var h="";
					var m="";

					if(valor==null || valor==""){
						valor="";					
					}else{
						h=valor.substring(0, 2);
						m=valor.substring(3, 6); 						
					}
					$('#hDFPL').val(valor);
					for (var x = 0; x <= 24; x++) {
						if(x<10){
							hora="0" + x;
						}else{
							hora=x;
						}
						if(hora==h){
							$("#cboHora").append("<option value='" + hora + "' selected>" + hora + "</option>");							
						}else{
							$("#cboHora").append("<option value='" + hora + "'>" + hora + "</option>");
						}

					}
					
					for (var x = 0; x <= 60; x++) {
						if(x<10){
							minuto="0" + x;
						}else{
							minuto=x;
						}
						
						if(minuto==m){
							$("#cboMinuto").append("<option value='" + minuto + "' selected>" + minuto + "</option>");							
						}else{
							$("#cboMinuto").append("<option value='" + minuto + "'>" + minuto + "</option>");
						}

					}


				}
				
								
				if(resultConfiguracion[t].codConfiguracion==TipoConfiguracion.GENERACIONPEDIDOENTREGA){
					if(resultConfiguracion[t].valor=="SI"){
						$("#chkPedEntr").prop("checked", "checked");						
					}

				}
				
			}			
			
		}

	});
	
}


$(document).ready(
	function() {
		var rdbExistenciaConfBodega=document.getElementsByName("rdbExistenciaConfiguracionBodega");
		for(var x=0;x<rdbExistenciaConfBodega.length;x++){
			rdbExistenciaConfBodega[x].disabled=true;				
		}

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

function elegirMotivo(radio){
	$("#cboDevolucion").html('');
	$("#h_tipoFormato").val(radio.value);
	$('#h_devolucion').val('');				
	$.ajax({
		type : 'GET',
		url : 'listaDevolucion.do?formato=' + radio.value,
		dataType : 'json',
		success : function(result) {
			$("#cboDevolucion").append("<option value=''>---------------------</option>");
			for (var x = 0; x < result.length; x++) {
				$("#cboDevolucion").append("<option value='" + result[x].codDevol + "' onClick=elegirConfiguracion(this);>" + result[x].nomDevol + "</option>");
				}
			}
	});
}

function elegirConfiguracion(combo){
	$("#divBodegas").html('');
	deshabilitaBodegas();
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
			for(var t=0;t<resultConfiguracion.length;t++){
				if(resultConfiguracion[t].codConfiguracion==TipoConfiguracion.BODEGA){
					$("#" + TipoConfiguracion.BODEGA).prop("checked", "checked");
				}
				if(resultConfiguracion[t].codConfiguracion==TipoConfiguracion.LOTE){
					$("#" + TipoConfiguracion.LOTE).prop("checked", "checked");
					$("#lst_Lote").append(
							"<option value='"
									+ resultConfiguracion[t].valor + "'>"
									+ resultConfiguracion[t].valor
									+ "</option>");
					
					$("#divLotes").append("<input type='hidden' name='t_lote[]' id='t_lote' value='" + resultConfiguracion[t].valor + "'/>");
				}
			}	
	
		}

	});
	
}


$(document).ready(
		function() {
			$("#cboDevolucion").change(
			function() {
				var codDevolucion = $("#cboDevolucion option:selected").val();
				$('#h_devolucion').val(codDevolucion);
				elegirConfiguracion(codDevolucion);
				var rdbExistenciaConfBodega=document.getElementsByName("rdbExistenciaConfiguracionBodega");
				for(var x=0;x<rdbExistenciaConfBodega.length;x++){
					rdbExistenciaConfBodega[x].disabled=false;				
				}
				
			});
});

function deshabilitaBodegas(){
	var chkBodegas=document.getElementsByName("chkTipoBodega[]");
	for(var i=0;i<chkBodegas.length;i++){
		chkBodegas[i].disabled=true;
	}
}


