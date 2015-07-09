$(document).ready(function() {
	// Smart Wizard     	
	$('#wizard').smartWizard({
		transitionEffect : 'slideleft',
		onLeaveStep : leaveAStepCallback,
		onFinish : onFinishCallback,
		enableFinishButton : true
	});

	function leaveAStepCallback(obj) {
		var step_num = obj.attr('rel');
		return validateSteps(step_num);
	}

	function onFinishCallback() {
		if (validateAllSteps()) {
			$('form').submit();
		}
	}
});


function validateAllSteps() {
	var isStepValid = true;

	if (validateStep1() == false) {
		isStepValid = false;
		$('#wizard').smartWizard('setError', {
			stepnum : 1,
			iserror : true
		});
	} else {
		$('#wizard').smartWizard('setError', {
			stepnum : 1,
			iserror : false
		});
	}

	if (validateStep2Planeamiento() == false) {
		isStepValid = false;
		$('#wizard').smartWizard('setError', {
			stepnum : 1,
			iserror : true
		});
	} else {
		$('#wizard').smartWizard('setError', {
			stepnum : 1,
			iserror : false
		});
	}

	if (!isStepValid) {
		$('#wizard').smartWizard('showMessage',
				'Por favor complete los datos para poder continuar');
	}

	return isStepValid;
}

function validateSteps(step) {
	var isStepValid = true;
	// validate step 1
	if (step == 1) {
		if (validateStep1() == false) {
			isStepValid = false;
			$('#wizard').smartWizard(
					'showMessage',
					'Por favor complete los datos del Paso ' + step
							+ ' y de click en Siguiente.');
			$('#wizard').smartWizard('setError', {
				stepnum : step,
				iserror : true
			});
		} else {
			$('#wizard').smartWizard('setError', {
				stepnum : step,
				iserror : false
			});
		}
	}

	if (step == 2) {
		if (validateStep2Planeamiento() == false) {
			isStepValid = false;
			$('#wizard').smartWizard(
					'showMessage',
					'Por favor complete los datos del Paso ' + step
							+ ' y de click en Siguiente.');
			$('#wizard').smartWizard('setError', {
				stepnum : step,
				iserror : true
			});
		} else {
			$('#wizard').smartWizard('setError', {
				stepnum : step,
				iserror : false
			});
		}
	}

	return isStepValid;
}

function validateStep1() {
	var isValid = true;
	var cont=0;
	var los_elementos = document.getElementById('formProcesar').elements;
	for (var i=0; i<los_elementos.length; i++) {
		if((los_elementos[i].type == 'checkbox')){
			if(los_elementos[i].checked == true){
				cont=cont+1;
			}
		}
	}

	if(cont==0){
		isValid=false;
	}
	return isValid;
}


function validateStep2Planeamiento() {
	var isValid = true;
	
	//Validar Existencia Fichero

	var file_ExcelPlaneamiento = $("#ficheroExcelPlaneamiento").val();
	$('#msg_fileExcel').html('').hide();	
	if (file_ExcelPlaneamiento.length != 0) {
		extension = (file_ExcelPlaneamiento.substring(file_ExcelPlaneamiento.lastIndexOf("."))).toLowerCase();	
		if(extension==".xls"){
			$('#msg_fileExcelPlaneamiento').html('').hide();
		}else{
			isValid=false;		
			$('#msg_fileExcelPlaneamiento').html('Compruebe la extensión del archivo a subir(xls)').show();
		}
	}else {
		isValid = false;
		$('#msg_fileExcel').html('Ingrese la Fuente Planeamiento').show();
	}
		
	return isValid;
}

$(function() {
    $("#fcorreo1").datepicker();
 });

$(function() {
    $("#fcorreo2").datepicker();
 });


function verActas(radio){
	$('#h_tipoFormato').val(radio.value);
	$.ajax({
		  type : 'GET',
			url : 'lstActasGeneradasHoy.do?formato=' + radio.value,
			dataType : 'json',
			success : function(result) {
				$("#content_check_ticket").empty();
				var f = new Date();
				var contenido="<span id='headerFecha' style='color:red;font-weight: bold;font-size:11px;'>*** Cargados el: " + ((f.getDate())<10?'0'+(f.getDate()):f.getDate()) + "/" + ((f.getMonth() +1)<10?'0'+(f.getMonth()+1):f.getMonth()) + "/" + f.getFullYear() + "</span><br><br>";
				contenido=contenido + "<table class='ficheroTabla' border='1' style='border: 1px solid black;border-collapse: collapse;width:100%;'>" + 
							  "<tr><th style='text-align:center;'>N°</th><th style='text-align:center;'>N° Acta</th><th style='text-align:center;'>N° Ticket</th><th style='text-align:center;'>Entidad</th><th style='text-align:center;'>Tipo Formato</th><th style='text-align:center;'>Tipo Devolucion</th><th style='text-align:center;' width=10%>Cantidad</th><th style='text-align:center;'>Asignar</th></tr>";
				for (var x = 0; x < result.length; x++) {
					contenido=contenido+"<tr><td style='text-align:right;'>"+(x+1)+"</td><td style='text-align:center;'>"+result[x].nroActa+"</td><td style='text-align:center;'>"+result[x].nroTicket+"</td><td style='text-align:center;'>"+result[x].entidad +"</td><td style='text-align:center;'>"+result[x].tipoFormato +"</td><td style='text-align:center;'>"+result[x].tipoDevolucion +"</td><td style='text-align:center;'>"+result[x].cantSeries +"</td><td style='text-align:center;'><input type='checkbox' name='actas[]' id='chk"+x+"' onclick='obtenerActa(this,this.value);' value='" + result[x].nroActa + "|" + result[x].nroTicket + "'/></td></tr>";
				}
				contenido=contenido+"</table><br/>";
				contenido=contenido+"<p style='text-align:right;'><input type='text' name='nroActa' id='nroActa' value='' style='text-align:right;'/></p>";
				$("#content_check_ticket").append(contenido);		
			}
	});
}


function verActasFechas(){
	var radio=$('#h_tipoFormato').val();
	var f1=$('#fcorreo1').val();
	var f2=$('#fcorreo2').val();
	
	$.ajax({
		  type : 'GET',
			url : 'lstActasGeneradasFechas.do?formato=' + radio + "&fcorreo1=" + f1 + "&fcorreo2=" + f2 ,
			dataType : 'json',
			success : function(result) {
				$("#content_check_ticket").empty();
				var contenido="<span style='color:red;font-weight: bold;font-size:11px;'>*** Cargados del: " + f1 + " al " + f2 + "</span><br><br>";
				contenido=contenido + "<table class='ficheroTabla' border='1' style='border: 1px solid black;border-collapse: collapse;width:100%;'>" + 
							  "<tr><th style='text-align:center;'>N°</th><th style='text-align:center;'>N° Acta</th><th style='text-align:center;'>N° Ticket</th><th style='text-align:center;'>Entidad</th><th style='text-align:center;'>Tipo Formato</th><th style='text-align:center;'>Tipo Devolucion</th><th style='text-align:center;' width=10%>Cantidad</th><th style='text-align:center;'>Asignar</th></tr>";
				for (var x = 0; x < result.length; x++) {
					contenido=contenido+"<tr><td style='text-align:right;'>"+(x+1)+"</td><td style='text-align:center;'>"+result[x].nroActa+"</td><td style='text-align:center;'>"+result[x].nroTicket+"</td><td style='text-align:center;'>"+result[x].entidad +"</td><td style='text-align:center;'>"+result[x].tipoFormato +"</td><td style='text-align:center;'>"+result[x].tipoDevolucion +"</td><td style='text-align:center;'>"+result[x].cantSeries +"</td><td style='text-align:center;'><input type='checkbox' name='actas[]' id='chk"+x+"' onclick='obtenerActa(this,this.value);' value='" + result[x].nroActa + "|" + result[x].nroTicket + "'/></td></tr>";
				}
				contenido=contenido+"</table><br/>";
				contenido=contenido+"<p style='text-align:right;'><input type='text' name='nroActa' id='nroActa' value='' style='text-align:right;'/></p>";
				$("#content_check_ticket").append(contenido);		
			}
				});
}

$(document).ready(
		function() {
			$.ajax({
				type : 'GET',
				url : 'listaFormato.do',
				dataType : 'json',
				success : function(result) {
					$("#tipoFormato").append("<br/>");
						for (var x = 0; x < result.length; x++) {
							$("#tipoFormato").append("<input type='radio' name='formato' value='" + result[x].codTipoFormato + "' id='formato" + x + "' onChange='verActas(this)'>" + result[x].nomTipoFormato + "&nbsp;&nbsp;");
						}					
						$("#tipoFormato").append("<span id='msg_formato' style='color: red;'></span> <br /><br />");
					}
				});
		});


function obtenerActa(isChecked, myValue){
	var acta='';
	$("#nroActa").val('');
	$("input:checkbox:checked").each(function(){		
		acta=acta.concat("'" + $(this).val().split('|')[0]+"',");
	});
	
	acta = acta.substring(0, acta.length-1);
	
	$("#nroActa").val(acta);
	
	var codigosActa=$("#nroActa").val();
	
	var enlaceDescargaFuentePedido='descargaFuentePlaneamiento.do?accion=descarga&nroActa=' + codigosActa;
		
	document.getElementById('miEnlaceDescargaFuentePlaneamiento').href=enlaceDescargaFuentePedido;
		
	
}