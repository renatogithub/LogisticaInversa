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
	    	$("#dialog").text("¿Seguro que desea Grabar la Fuente Pedido?");
	        $("#dialog").dialog({
	        	width:350,
	            height:150,
	            modal: true,
	            buttons: {
	              Si: function() {
					$('form').submit();
					 $("#loadingmessage").css("display", "block");
					 $("#registroFuente").css("display", "none");
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
	              },
	              No: function() {
	                $( this ).dialog( "close" );
	              }
	            }
	          });  		
		}else{
			$("#loadingmessage").css("display", "none");
			$("#registroFuente").css("display", "block");
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

	if (validateStep2Pedidos() == false) {
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
		if (validateStep2Pedidos() == false) {
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
	var isValid = false;
	//var rdbActa = document.formProcesar.rdbActa;
	var acta=$("#nroActas").val();
	
	if(acta.length>0){
		isValid=true;
	}

	return isValid;
}


function validateStep2Pedidos() {
	var isValid = true;
	
	//Validar Existencia Fichero

	var file_PedidoEntrega = $("#ficheroPedidoEntrega").val();
	$('#msg_fileExcel').html('').hide();	
	if (file_PedidoEntrega.length != 0) {
		extension = (file_PedidoEntrega.substring(file_PedidoEntrega.lastIndexOf("."))).toLowerCase();	
		if(extension==".xls"){
			$('#msg_PedidoEntrega').html('').hide();
		}else{
			isValid=false;		
			$('#msg_PedidoEntrega').html('Compruebe la extensión del archivo a subir(xls)').show();
		}
	}else {
		isValid = false;
		$('#msg_fileExcel').html('Ingrese la Fuente Pedido y Entrega').show();
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
							$("#tipoFormato").append("<input type='radio' name='formato' value='" + result[x].codTipoFormato + "' id='formato" + x + "' onChange='verActasSinPedido(this)'>" + result[x].nomTipoFormato + "&nbsp;&nbsp;");
						}					
						$("#tipoFormato").append("<span id='msg_formato' style='color: red;'></span> <br /><br />");
					}
				});
		});


function verActasSinPedido(radio){
	$('#h_tipoFormato').val(radio.value);
	$("#nroActas").val('');
	$("#nroTicket").val('');
	$.ajax({
		  type : 'GET',
			url : 'listaPedidoEntregaPendiente.do?formato=' + radio.value,
			dataType : 'json',
			success : function(result) {
				$("#content_check_ticket").empty();
				//var contenido="<span id='headerFecha' style='color:red;font-weight: bold;font-size:11px;'>*** Cargados el: " + ((f.getDate())<10?'0'+(f.getDate()):f.getDate()) + "/" + ((f.getMonth() +1)<10?'0'+(f.getMonth()+1):f.getMonth()) + "/" + f.getFullYear() + "</span><br>";
				var contenido="<div id='tbTabla' style='height:200px;'><table class='styleTabla'><thead>" + 
				  			  "<tr><th style='text-align:center;width:5%;'>N°</th><th style='text-align:center;width:20%;'>N° Acta</th><th style='text-align:center;width:15%;'>N° Ticket</th><th style='text-align:center;'>Entidad</th><th style='text-align:center;'>Tipo Formato</th><th style='text-align:center;'>Tipo Devolucion</th><th style='text-align:center;' width=10%>Cantidad</th><th style='text-align:center;'>Asignar</th></tr>";
				
				for (var x = 0; x < result.length; x++) {
					contenido=contenido+"<tr><td style='text-align:right;'>"+(x+1)+"</td><td style='text-align:center;'>"+result[x].nroActa+"</td><td style='text-align:center;'>"+result[x].nroTicket+"</td><td style='text-align:center;'>"+result[x].entidad +"</td><td style='text-align:center;'>"+result[x].tipoFormato +"</td><td style='text-align:center;'>"+result[x].tipoDevolucion +"</td><td style='text-align:center;'>"+result[x].cantItems +"</td><td style='text-align:center;'><input type='radio' name='rdbActa' id='rdb"+x+"' onclick='obtenerActa(this,this.value);' value='" + result[x].nroActa + "|" + result[x].nroTicket + "'/></td></tr>";
				}
				contenido=contenido+"</tbody></table></div><br>";
				$("#content_check_ticket").append(contenido);		
			}
	});
}

function obtenerActa(isChecked, myValue){
	
	var acta='';
	var ticket='';
	var formato='';
	acta=myValue.split('|')[0];
	ticket=myValue.split('|')[1];
	$("#nroActas").val(acta);
	$("#nroTicket").val(ticket);
	formato=$("#h_tipoFormato").val();	
	
	var codigosActa=$("#nroActas").val();
	var codigosTicket=$("#nroTicket").val();
	
	var enlaceDescargaPlantilla='descargaFicheroExcel.do?accion=descarga&nroTicket=' + codigosTicket;
	var enlaceDescargaFuentePedido='descargaFuentePedido.do?accion=descarga&nroActas=' + codigosActa + '&formato=' + formato;
	
	if (isChecked.checked) {
		
		document.getElementById('miEnlaceDescargaPlantilla').href=enlaceDescargaPlantilla;
		document.getElementById('miEnlaceFuentePedido').href=enlaceDescargaFuentePedido;
	}else{
		document.getElementById('miEnlaceDescargaPlantilla').href=enlaceDescargaPlantilla;
		document.getElementById('miEnlaceFuentePedido').href=enlaceDescargaFuentePedido;
	}
		
	
}