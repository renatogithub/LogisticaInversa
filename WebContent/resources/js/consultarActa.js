function carga(){
	var paramstr = window.location.search.substr(1);
	paramstr=decodeURI(paramstr);
	var paramarr = paramstr.split ("&");
	var params=paramarr[1].split("=");
	var nroTicket = params[1];
	var nroTickets=nroTicket.replace(/'/g,"");
	$("#t_ticket").val(nroTickets);
	
	if(nroTickets==""){
		$('#msg_ticket').html('Seleccione al menos un Nro de Ticket').show();
		$("#btnRegistrar").prop("disabled", "disabled");	
	}else{
		$('#msg_ticket').html('Seleccione al menos un Nro de Ticket').hide();
		$("#btnRegistrar").prop("disabled", "");	
	}
}

function loadPedido(){
	var paramstr = window.location.search.substr(1);
	paramstr=decodeURI(paramstr);
	var paramarr = paramstr.split ("&");
	var params=paramarr[1].split("=");
	var nroActa=params[1];
	$("#nroActa").val(nroActa);
	
	if(nroActa==""){
		$('#msg_acta').html('Seleccione al menos un Nro de Acta').show();
		$("#btnRegistrar").prop("disabled", "disabled");	
	}else{
		$('#msg_acta').html('Seleccione al menos un Nro de Acta').hide();
		$("#btnRegistrar").prop("disabled", "");	
	}
}


function abrir(value) { 	
	open('ventana.do?accion=envio&nroTicket=' + value,'','top=100,left=300,width=500,height=500') ; 

}

function abrirPedido(value) { 	
	open('uploadPedido.do?accion=envio&nroActas=' + value,'','top=100,left=300,width=500,height=500') ; 

}

function onFinishCallback() {
	if (validaInputs()) {
		$('form').submit();
	}
}

function onFinishCallbackPedido() {
	if (validaInputsPedido()) {
		$('form').submit();
	}
}


function validaInputsPedido(){
	var isValid=true;
	
	var ficheroPedido = $("#ficheroPedido").val();
	
	if(ficheroPedido.length!=0){
		extension = (ficheroPedido.substring(ficheroPedido.lastIndexOf("."))).toLowerCase();	
		if(extension==".xls"){
			$('#msg_ficheroPedido').html('').hide();
		}else{
			isValid=false;		
			$('#msg_ficheroPedido').html('Compruebe la extensión del archivo a subir(xls)').show();
		}
	}else {
		isValid = false;
		$('#msg_ficheroPedido').html('Ingrese la Fuente Pedido').show();
	}
	
	return isValid;
}

function validaInputs(){
	var isValid = true;
	
	//Validar Existencia Fichero SAP

	var file_sap60 = $("#ficheroExcelSAP6_0").val();
	var file_sap47 = $("#ficheroExcelSAP4_7").val();
	
	//if (file_sap60.length != 0 || file_sap47.length != 0) {
		if(file_sap60.length != 0){
			extension60 = (file_sap60.substring(file_sap60.lastIndexOf("."))).toLowerCase();	
			if(extension60==".xls"){
				$('#msg_fileExcel60').html('').hide();
			}else{
				isValid=false;		
				$('#msg_fileExcel60').html('Compruebe la extensión del archivo a subir(xls)').show();
			}
		}


		if(file_sap47.length != 0){
			extension47 = (file_sap47.substring(file_sap47.lastIndexOf("."))).toLowerCase();	
			if(extension47==".xls"){
				$('#msg_fileExcel47').html('').hide();
			}else{
				isValid=false;		
				$('#msg_fileExcel47').html('Compruebe la extensión del archivo a subir(xls)').show();
			}
		}
	return isValid;
}


function obtenerTicket(isChecked, myValue){
	var ticket='';
	$("#nroTicket").val('');
	$("input:checkbox:checked").each(function(){
		ticket=ticket.concat("'" + $(this).val()+"',");
		//ticket=ticket.concat($(this).val()+",");
	});
	
	var tickets = ticket.substring(0, ticket.length-1);
	
	$("#nroTicket").val(tickets);
	
	var codigos=$("#nroTicket").val();
	var enlaceDescargaPlantilla='descargaFicheroExcel.do?accion=descarga&nroTicket=' + codigos;
	var enlaceDescargaPendientes='descargaSeriesPendienteXls.do?accion=descarga&nroTicket=' + codigos;
	var enlaceDescargaPendientesGarantia='descargaSeriesPendienteGarantia.do?accion=descarga&nroTicket=' + codigos;
	var enlaceDescargaPendientesExterno='descargaSeriesPendienteValidacionExterna.do?accion=descarga&nroTicket=' + codigos;
		
		if (isChecked.checked) {
			document.getElementById('miEnlaceDescargaPlantilla').href=enlaceDescargaPlantilla;
			document.getElementById('miEnlaceDescargaPendientes').href=enlaceDescargaPendientes;
			document.getElementById('miEnlaceDescargaPendientesGarantia').href=enlaceDescargaPendientesGarantia;
			document.getElementById('miEnlaceDescargaPendientesExterna').href=enlaceDescargaPendientesExterno;
		}else{
			document.getElementById('miEnlaceDescargaPlantilla').href=enlaceDescargaPlantilla;
			document.getElementById('miEnlaceDescargaPendientes').href=enlaceDescargaPendientes;
			document.getElementById('miEnlaceDescargaPendientesGarantia').href=enlaceDescargaPendientesGarantia;
			document.getElementById('miEnlaceDescargaPendientesExterna').href=enlaceDescargaPendientesExterno;
			
		}
		
	
}


function obtenerActa(isChecked, myValue){
	var acta='';
	var ticket='';
	$("#nroActas").val('');
	$("#nroTicket").val('');
	$("input:checkbox:checked").each(function(){
		
		acta=acta.concat("'" + $(this).val().split('|')[0]+"',");
		ticket=ticket.concat("'" + $(this).val().split('|')[1]+"',");
	});
	
	acta = acta.substring(0, acta.length-1);
	ticket = ticket.substring(0, ticket.length-1);
	
	$("#nroActas").val(acta);
	$("#nroTicket").val(ticket);
	
	var codigosActa=$("#nroActas").val();
	var codigosTicket=$("#nroTicket").val();
	
	var enlaceDescargaPlantilla='descargaFicheroExcel.do?accion=descarga&nroTicket=' + codigosTicket;
	var enlaceDescargaFuentePedido='descargaFuentePedido.do?accion=descarga&nroActas=' + codigosActa;
		
	if (isChecked.checked) {
		document.getElementById('miEnlaceDescargaPlantilla').href=enlaceDescargaPlantilla;
		document.getElementById('miEnlaceFuentePedido').href=enlaceDescargaFuentePedido;
	}else{
		document.getElementById('miEnlaceDescargaPlantilla').href=enlaceDescargaPlantilla;
		document.getElementById('miEnlaceFuentePedido').href=enlaceDescargaFuentePedido;
	}
	
}