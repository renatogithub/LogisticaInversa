
/*-----------------------------------------------------------------------------------*/
/*  Lista aquellos Tickets que estan pendientes por procesar
/*-----------------------------------------------------------------------------------*/
var elementos=[];
var codigos;


/*$(document).ajaxStart(function(){
    $("#loadingmessage").css("display", "block");
});

$(document).ajaxComplete(function(){
    $("#loadingmessage").css("display", "none");
}); */

function marcaTodos(isChecked){
	if (isChecked.checked) {
		for (var i=0;i<document.formProcesar.elements.length;i++) {			
			if(document.formProcesar.elements[i].type == "checkbox"){
				var id=document.formProcesar.elements[i].id;
				var res=id.substring(0,9);
				if(res=="chkTicket"){
					document.formProcesar.elements[i].checked=1; 	
					sumaRegTicket(document.formProcesar.elements[i],document.formProcesar.elements[i].value);
				}
			} 
		        
		}
	}else{		
		for (var i=0;i<document.formProcesar.elements.length;i++) {   
			if(document.formProcesar.elements[i].type == "checkbox"){
				var id=document.formProcesar.elements[i].id;
				var res=id.substring(0,9);
				if(res=="chkTicket"){
					document.formProcesar.elements[i].checked=0; 
					sumaRegTicket(document.formProcesar.elements[i],document.formProcesar.elements[i].value);
				}
			} 
		}
		document.formProcesar.t_total.value=0;
	}
}


function sumaRegTicket(isChecked, myValue){
	var elem=myValue.split('|');
	var valor='';
	var tickets='';
	var codigos='';
	var enlaceSeries='';
	var enlaceMaterial='';
	if (isChecked.checked) {
		$("#nroTicket").val('');
		$("input:checkbox:checked").each(function(){
			var id=isChecked.id;
			var res=id.substring(0,9);
			if(res=="chkTicket"){
				if($(this).val().split('|')[1]===undefined){
					valor='';
				}else{
					valor=valor.concat("'"+$(this).val().split('|')[1]+"',");					
				}
			}
						
		});
		
		if(valor.length>0){
		    tickets = valor.substring(0, valor.length-1);
			$("#nroTicket").val(tickets);
			codigos=$("#nroTicket").val();
			enlaceSeries='descargaSeriesXls.do?accion=descarga&nroTicket=' + codigos;
			enlaceMaterial='descargaMaterialXls.do?accion=descarga&nroTicket=' + codigos;
		}
			cantidadItem=parseInt(elem[0]);
			tot = parseInt(document.formProcesar.t_total.value);
			document.formProcesar.t_total.value = tot + cantidadItem;
			document.getElementById('miEnlaceSeries').href=enlaceSeries;	
			document.getElementById('miEnlaceMaterial').href=enlaceMaterial;		
	}else{
		$("#nroTicket").val('');
		$("input:checkbox:checked").each(function(){
			var id=isChecked.id;
			var res=id.substring(0,9);
			if(res=="chkTicket"){
				if($(this).val().split('|')[1]===undefined){
					valor='';
				}else{
					valor=valor.concat("'"+$(this).val().split('|')[1]+"',");					
				}
			}
						
		});
		
		if(valor.length>0){
		    tickets = valor.substring(0, valor.length-1);
			$("#nroTicket").val(tickets);
			codigos=$("#nroTicket").val();
			enlaceSeries='descargaSeriesXls.do?accion=descarga&nroTicket=' + codigos;
			enlaceMaterial='descargaMaterialXls.do?accion=descarga&nroTicket=' + codigos;
		}
			cantidadItem=parseInt(elem[0]);
			tot = parseInt(document.formProcesar.t_total.value);
			document.formProcesar.t_total.value = tot - cantidadItem;
			document.getElementById('miEnlaceSeries').href=enlaceSeries;	
			document.getElementById('miEnlaceMaterial').href=enlaceMaterial;
	}
	
}


$(function() {
    $("#fcorreo1").datepicker();
 });

$(function() {
    $("#fcorreo2").datepicker();
 });


$(document).ready(function() {
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
		    $("#dialog").text("¿Seguro que desea Validar el Acta?");
		    $("#dialog").dialog({
		    	width:350,
		        height:150,
		        modal: true,
		        buttons: {
		        Si: function() {
		        	$('form').submit();
					$("#loadingmessage").css("display", "block");
					$("#validarSAP").css("display", "none");
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
			 $("#validarSAP").css("display", "block");
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

	if (validateStep2SinValidar() == false) {
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
		if (validateStep2SinValidar() == false) {
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


function validateStep2SinValidar() {
	var isValid = true;
	
	//Validar Existencia Fichero

	var file_sap60 = $("#ficheroExcelSAP6_0").val();
	var file_sap47 = $("#ficheroExcelSAP4_7").val();
	$('#msg_fileExcel').html('').hide();	
	if($("#formato0").is(':checked') ||$("#formato1").is(':checked')){ //Si el Formato es Tempresa o Seriado
		if($("#chkSinSerie").is(':checked')) {
			isValid=true;
			$("#chkSinSerie").val("SINSERIE");
		}else{
			if (file_sap60.length != 0 || file_sap47.length != 0) {
				
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
			} else {
				isValid = false;
				$('#msg_fileExcel').html('Ingrese al menos un archivo SAP').show();
			}
		}		
	}else{
		if($("#formato2").is(':checked')){ //Si el Formato es No Seriado
			isValid=true;
		}else{
			if (file_sap60.length != 0 || file_sap47.length != 0) {
				
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
			} else {
				isValid = false;
				$('#msg_fileExcel').html('Ingrese al menos un archivo SAP').show();
			}
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
							$("#tipoFormato").append("<input type='radio' name='formato' value='" + result[x].codTipoFormato + "' id='formato" + x + "' onChange='verSinProcesar(this)'>" + result[x].nomTipoFormato + "&nbsp;&nbsp;");
						}					
						$("#tipoFormato").append("<span id='msg_formato' style='color: red;'></span> <br /><br />");
						

					}
				});
		});

function verSinProcesar(radio){
	$('#h_tipoFormato').val(radio.value);
	var usuario=$('#h_usuario').val();
	$.ajax({
		  type : 'GET',
			url : 'listaTicketSinProcesar.do?formato=' + radio.value + "&h_usuario=" + usuario,
			dataType : 'json',
			success : function(result) {
				$("#content_check_ticket").empty();
				var f = new Date();
				var contenido="<span id='headerFecha' style='color:red;font-weight: bold;font-size:11px;' >*** Cargados el: " + ((f.getDate())<10?'0'+(f.getDate()):f.getDate()) + "/" + ((f.getMonth() +1)<10?'0'+(f.getMonth()+1):f.getMonth()) + "/" + f.getFullYear() + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a id='miEnlaceSeries' href=''>Descargar Series</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a id='miEnlaceMaterial' href=''>Descargar Materiales</a></span>&nbsp;&nbsp;&nbsp;<input type='checkbox' id='chkMarcaTodos' onclick='marcaTodos(this);' />Marcar Todos<br>";
				contenido=contenido + "<div id='tbTabla' style='height:200px;'><table class='styleTabla'><thead>" + 
							  "<tr><th style='text-align:center;width:5%;'>N°</th><th style='text-align:center;width:15%;'>N°Ticket</th><th style='text-align:center;'>Entidad</th><th style='text-align:center;width:15%;'>Tipo Formato</th><th style='text-align:center;'>Tipo Devolucion</th><th style='text-align:center;'>Fecha Carga</th><th style='text-align:center;width:10%;'>Cant. Item</th><th style='text-align:center;'>Validar</th></tr></thead><tbody>";
				for (var x = 0; x < result.length; x++) {
					contenido=contenido+"<tr><td style='text-align:right;'>"+(x+1)+"</td><td style='text-align:center;'>"+result[x].nroTicket+"</td><td style='text-align:center;'>"+result[x].entidad +"</td><td style='text-align:center;'>"+result[x].tipoFormato +"</td><td style='text-align:center;'>"+result[x].tipoDevolucion +"</td><td style='text-align:center;'>"+result[x].fechaCarga + "</td><td style='text-align:center;'>"+result[x].cantidadItem +"</td><td style='text-align:center;'><input type='checkbox' name='tickets[]' id='chkTicket"+x+"' onclick='sumaRegTicket(this,this.value);' value='" + result[x].cantidadItem + "|" +result[x].nroTicket + "|" + result[x].codTipoDevolucion + "'/></td></tr>";
				}
				contenido=contenido+"</tbody></table></div><br>";
				contenido=contenido+"<div style='text-align:right;'><strong>Total de Items a Validar: &nbsp;&nbsp;</strong><input type='text' name='t_total' id='t_total' size='5' value='0' disabled style='text-align:right;'/><br></div>";				
				contenido=contenido+"<p style='text-align:right;'><input type='hidden' name='nroTicket' id='nroTicket' value='' style='text-align:right;'/></p>";							
				$("#content_check_ticket").append(contenido);		
			}
		});
}


function verSinProcesarFechas(){
	var radio=$('#h_tipoFormato').val();
	var f1=$('#fcorreo1').val();
	var f2=$('#fcorreo2').val();
	var usuario=$('#h_usuario').val();	
	$.ajax({
		  type : 'GET',
			url : 'listaTicketSinProcesarFechas.do?formato=' + radio + "&fcorreo1=" + f1 + "&fcorreo2=" + f2 + "&h_usuario=" + usuario,
			dataType : 'json',
			success : function(result) {
				$("#content_check_ticket").empty();
				var contenido="<span id='headerFecha' style='color:red;font-weight: bold;font-size:11px;'>*** Cargados del: " + f1 + " al " + f2 + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a id='miEnlaceSeries' href=''>Descargar Series</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a id='miEnlaceMaterial' href=''>Descargar Materiales</a></span>&nbsp;&nbsp;&nbsp;<input type='checkbox' id='chkMarcaTodos' onclick='marcaTodos(this);' />Marcar Todos<br>";
				contenido=contenido + "<div id='tbTabla'><table class='styleTabla'><thead>" + 
								"<tr><th style='text-align:center;width:5%;'>N°</th><th style='text-align:center;width:15%;'>N°Ticket</th><th style='text-align:center;'>Entidad</th><th style='text-align:center;width:15%;'>Tipo Formato</th><th style='text-align:center;'>Tipo Devolucion</th><th style='text-align:center;'>Fecha Carga</th><th style='text-align:center;width:10%;'>Cant. Item</th><th style='text-align:center;'>Validar</th></tr></thead><tbody>";
				for (var x = 0; x < result.length; x++) {
					contenido=contenido+"<tr><td style='text-align:right;'>"+(x+1)+"</td><td style='text-align:center;'>"+result[x].nroTicket+"</td><td style='text-align:center;'>"+result[x].entidad +"</td><td style='text-align:center;'>"+result[x].tipoFormato +"</td><td style='text-align:center;'>"+result[x].tipoDevolucion +"</td><td style='text-align:center;'>"+result[x].fechaCarga + "</td><td style='text-align:center;'>"+result[x].cantidadItem +"</td><td style='text-align:center;'><input type='checkbox' name='tickets[]' id='chkTicket"+x+"' onclick='sumaRegTicket(this,this.value);' value='" + result[x].cantidadItem + "|" +result[x].nroTicket + "|" + result[x].codTipoDevolucion + "'/></td></tr>";
				}
				contenido=contenido+"</tbody></table></div><br>";
				contenido=contenido+"<div style='text-align:right;'><strong>Total de Items a Validar: &nbsp;&nbsp;</strong><input type='text' name='t_total' id='t_total' size='5' value='0' disabled style='text-align:right;'/><br></div>";				
				contenido=contenido+"<p style='text-align:right;'><input type='hidden' name='nroTicket' id='nroTicket' value='' style='text-align:right;'/></p>";
				$("#content_check_ticket").append(contenido);		
			}
				});
}
