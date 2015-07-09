
/*-----------------------------------------------------------------------------------*/
/*  Lista aquellos Tickets que estan pendientes por procesar
/*-----------------------------------------------------------------------------------*/
var elementos=[];
var codigos;
function sumaRegTicket(isChecked, myValue){
	
	var elem=myValue.split('|');
	var valor='';
	$("#nroTicket").val('');
	$("input:checkbox:checked").each(function(){
		valor=valor.concat("'"+$(this).val().split('|')[1]+"',");		
	});

    var tickets = valor.substring(0, valor.length-1);
    
	$("#nroTicket").val(tickets);
	var codigos=$("#nroTicket").val();
	var enlace='descargaSeriesXls.do?accion=descarga&nroTicket=' + codigos;

	cantReg=parseInt(elem[0]);
	tot = parseInt(document.formProcesar.t_total.value);
	if (isChecked.checked) {
		document.formProcesar.t_total.value = tot + cantReg;
		document.getElementById('miEnlace').href=enlace;		
	}else{
		document.formProcesar.t_total.value = tot - cantReg;
		document.getElementById('miEnlace').href=enlace;
	}
	
}

$(function() {
    $("#fcorreo1").datepicker();
 });

$(function() {
    $("#fcorreo2").datepicker();
 });


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
			 $("#dialog:ui-dialog").dialog("destroy");
			 $("#dialog").text("¿Seguro que desea Revalidar el Acta?");
			 $("#dialog").dialog({
				 width:350,
			     height:150,
			     modal: true,
			     buttons: {
			     Si: function() {
			    	 $('form').submit();
			    	 $("#loadingmessage").css("display", "block");
			    	 $("#validarFuenteValidacion").css("display", "none");
			         $("#dialog:ui-dialog" ).dialog( "destroy" );
			         $("#dialog" ).dialog({
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
			           	},
			          No: function() {
			             $( this ).dialog( "close" );
			          	}
			          	}
			          }); 
			 
			 
			 
			 
		}else{
			 $("#loadingmessage").css("display", "none");
			 $("#validarFuenteValidacion").css("display", "block");
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

	if (validateStep2() == false) {
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
		if (validateStep2() == false) {
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


function validateStep2() {
	var isValid = true;
	
	//Validar Existencia Fichero

	var file_sap60 = $("#ficheroExcelSAP6_0").val();
	var file_sap47 = $("#ficheroExcelSAP4_7").val();
	var file_garantia =$("#ficheroExcelGarantia").val();   
	var file_ValidacionExterna =$("#ficheroValidacionExterna").val();   
	
	if($("#formato2").is(':checked')){
		isValid=true;
	}else{
		if (file_sap60.length != 0 || file_sap47.length != 0 || file_garantia.length!=0 || file_ValidacionExterna.length!=0) {
			
			if(file_sap60.length != 0){
				extension60 = (file_sap60.substring(file_sap60.lastIndexOf("."))).toLowerCase();	
				if(extension60==".xls" || extension60==".xlsx"){
					$('#msg_fileExcel60').html('').hide();
				}else{
					isValid=false;		
					$('#msg_fileExcel60').html('Solo esta permitido subir archivos Excel').show();
				}
			}

			if(file_sap47.length != 0){
				extension47 = (file_sap47.substring(file_sap47.lastIndexOf("."))).toLowerCase();	
				if(extension47==".xls" || extension47==".xlsx"){
					$('#msg_fileExcel47').html('').hide();
				}else{
					isValid=false;		
					$('#msg_fileExcel47').html('Solo esta permitido subir archivos Excel').show();
				}
			}
			
			if(file_garantia.length != 0){
				extensionGarantia = (file_garantia.substring(file_garantia.lastIndexOf("."))).toLowerCase();	
				if(extensionGarantia==".xls"){
					$('#msg_fileExcelGarantia').html('').hide();
				}else{
					isValid=false;		
					$('#msg_fileExcelGarantia').html('Compruebe la extensión del archivo a subir(xls)').show();
				}
			}	
			
			if(file_ValidacionExterna.length != 0){
				extensionValExterna = (file_ValidacionExterna.substring(file_ValidacionExterna.lastIndexOf("."))).toLowerCase();	
				if(extensionValExterna==".xls"){
					$('#msg_fileValidacionExterna').html('').hide();
				}else{
					isValid=false;		
					$('#msg_fileValidacionExterna').html('Compruebe la extensión del archivo a subir(xls)').show();
				}
			}	
		} else {
			isValid = false;
			$('#msg_fileExcel').html('Ingrese al menos un archivo para la Validacion').show();
		}
	}
	
	return isValid;
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
							$("#tipoFormato").append("<input type='radio' name='formato' value='" + result[x].codTipoFormato + "' id='formato" + x + "' onChange='verPendientes(this)'>" + result[x].nomTipoFormato + "&nbsp;&nbsp;");
						}					
						$("#tipoFormato").append("<span id='msg_formato' style='color: red;'></span> <br /><br />");
					}
				});
		});


function verResumen(ticket){
	$.ajax({
		  type : 'GET',
			url : 'getResumenValidacion.do?ticket=' + ticket,
			dataType : 'json',
			success : function(result) {
				alert("ss");		
			}
		});

}


function verPendientes(radio){
	$('#h_tipoFormato').val(radio.value);
	var usuario=$('#h_usuario').val();
	$.ajax({
		  type : 'GET',
			url : 'listaTicketPendiente.do?formato=' + radio.value + "&h_usuario=" + usuario,
			dataType : 'json',
			success : function(result) {
				$("#content_check_ticket").empty();
				var f = new Date();
				var contenido="<span id='headerFecha' style='color:red;font-weight: bold;font-size:11px;'>*** Cargados el: " + ((f.getDate())<10?'0'+(f.getDate()):f.getDate()) + "/" + ((f.getMonth() +1)<10?'0'+(f.getMonth()+1):f.getMonth()) + "/" + f.getFullYear() + "</span><br>";
				contenido=contenido + "<div id='tbTabla'><table class='styleTabla'><thead>" + 
									"<tr><th style='text-align:center;width:5%;'>N°</th><th style='text-align:center;width:15%;'>N°Ticket</th><th style='text-align:center;'>Entidad</th><th style='text-align:center;width:15%;'>Tipo Formato</th><th style='text-align:center;'>Tipo Devolucion</th><th style='text-align:center;'>Fecha Carga</th><th style='text-align:center;width:10%;'>Cant. Item</th><th style='text-align:center;'>Validar</th></tr></thead><tbody>";
				for (var x = 0; x < result.length; x++) {
					contenido=contenido+"<tr><td style='text-align:right;'>"+(x+1)+"</td><td style='text-align:center;'>"+result[x].nroTicket+"</td><td style='text-align:center;'>"+result[x].entidad +"</td><td style='text-align:center;'>"+result[x].tipoFormato +"</td><td style='text-align:center;'>"+result[x].tipoDevolucion +"</td><td style='text-align:center;'>"+result[x].fechaCarga + "</td><td style='text-align:center;'>"+result[x].cantidadItem +"</td><td style='text-align:center;'><input type='checkbox' name='tickets[]' id='chk"+x+"' onclick='obtenerTicket(this,this.value);' value='" + result[x].nroTicket + "'/></td></tr>";
				}
				contenido=contenido+"</tbody></table></div><br>";
				$("#content_check_ticket").append(contenido);		
			}
				});
}


function verPendientesFechas(){
	var radio=$('#h_tipoFormato').val();
	var f1=$('#fcorreo1').val();
	var f2=$('#fcorreo2').val();
	var usuario=$('#h_usuario').val();
	$.ajax({
		  type : 'GET',
			url : 'listaTicketPendienteFechas.do?formato=' + radio + "&fcorreo1=" + f1 + "&fcorreo2=" + f2  + "&h_usuario=" + usuario,
			dataType : 'json',
			success : function(result) {
				$("#content_check_ticket").empty();
				var contenido="<span style='color:red;font-weight: bold;font-size:11px;'>*** Cargados del: " + f1 + " al " + f2 + "</span><br>";
				contenido=contenido + "<div id='tbTabla'><table class='styleTabla'><thead>" + 
								"<tr><th style='text-align:center;width:5%;'>N°</th><th style='text-align:center;width:15%;'>N°Ticket</th><th style='text-align:center;'>Entidad</th><th style='text-align:center;width:15%;'>Tipo Formato</th><th style='text-align:center;'>Tipo Devolucion</th><th style='text-align:center;'>Fecha Carga</th><th style='text-align:center;width:10%;'>Cant. Item</th><th style='text-align:center;'>Validar</th></tr></thead><tbody>";
				for (var x = 0; x < result.length; x++) {
					contenido=contenido+"<tr><td style='text-align:right;'>"+(x+1)+"</td><td style='text-align:center;'>"+result[x].nroTicket+"</td><td style='text-align:center;'>"+result[x].entidad +"</td><td style='text-align:center;'>"+result[x].tipoFormato +"</td><td style='text-align:center;'>"+result[x].tipoDevolucion +"</td><td style='text-align:center;'>"+result[x].fechaCarga + "</td><td style='text-align:center;'>"+result[x].cantidadItem +"</td><td style='text-align:center;'><input type='checkbox' name='tickets[]' id='chk"+x+"' onclick='obtenerTicket(this,this.value);' value='" + result[x].nroTicket + "'/></td></tr>";
				}
				contenido=contenido+"</tbody></table></div><br>";
				$("#content_check_ticket").append(contenido);		
			}
				});
}

