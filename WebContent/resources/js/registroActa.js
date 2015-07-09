
/*-----------------------------------------------------------------------------------*/
/*  Funcion para ejecutar Focus en Inputs
/*-----------------------------------------------------------------------------------*/


 function KeyAscii(e) {
	 return (document.all) ? e.keyCode : e.which;
 }
 
 function TabKey(e, nextobject) {
  nextobject = document.getElementById(nextobject);
  if (nextobject) {
   if (KeyAscii(e) == 13) nextobject.focus();
  }
 }


/*-----------------------------------------------------------------------------------*/
/*  Configuracion de Asistente de Formulario Wizard
/*-----------------------------------------------------------------------------------*/

	$(document).ready(function() {
		// Smart Wizard     	
		$('#wizard').smartWizard({
			transitionEffect : 'slideleft',
			onShowStep: null,
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
		    	$("#dialog").text("¿Seguro que desea Grabar el Acta?");
		        $("#dialog").dialog({
		        	width:350,
		            height:150,
		            modal: true,
		            buttons: {
		              Si: function() {
						$('form').submit();
						 $("#loadingmessage").css("display", "block");
						 $("#registroFuente").css("display", "none");
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
				$("#registroFuente").css("display", "block");
			}
		}
	});	
		
	
/*-----------------------------------------------------------------------------------*/
/*  DatePicker para la Fecha del Correo
/*-----------------------------------------------------------------------------------*/

/*$(function() {
    $( "#fcorreo" ).datepicker();
  });
*/
/*-----------------------------------------------------------------------------------*/
/*  Definimos funcion para la que valide el ingreso de cada control
/*-----------------------------------------------------------------------------------*/

function habilitarMesAnio(){
	$("#cboMesDevolucion").prop("disabled", false);
	$("#cboAnioDevolucion").prop("disabled", false);
}

function deshabilitarMesAnio(){
	$("#cboMesDevolucion").prop("disabled", true);
	$("#cboAnioDevolucion").prop("disabled", true);
}


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
	
	if (validateStep3() == false) {
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
	
	// validate step 2
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
	
	// validate step 3
	if (step == 3) {
		if (validateStep3() == false) {
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
	
	//Validar Tipo de Formato
	var tipoFormato = $("#h_tipoFormato").val();

	if (tipoFormato.length == 0) {
		isValid = false;
		$('#msg_formato').html('Seleccione un Tipo de Formato')
				.show();
	} else {
		$('#msg_formato').html('').hide();
	}
	
	//Validar Motivo de Devolucion
	var devol = $("#h_devolucion").val();	
	if (devol.length == 0) {
		isValid = false;
		$('#msg_devolucion').html('Seleccione un Motivo de Devolucion').show();
	} else {
		$('#msg_devolucion').html('').hide();
	}

	
	//Validar Mes de Devolucion
	
	if ($("#rdbMesDevolucion").is(":checked")){
		var mesDevolucion = $("#cboMesDevolucion option:selected").val();
		var anioDevolucion = $("#cboAnioDevolucion option:selected").val();	 
		if(mesDevolucion.length == 0) {
			isValid = false;
			$('#msg_MesDevolucion').html('Seleccione un Mes de Devolucion').show();
		}else{
			$('#msg_MesDevolucion').html('').hide();
		}
		
		if(anioDevolucion.length == 0) {
			isValid = false;
			$('#msg_AnioDevolucion').html('Seleccione un Año de Devolucion').show();
		}else{
			$('#msg_AnioDevolucion').html('').hide();
		}
		
		if(mesDevolucion.length == 0 && anioDevolucion.length == 0){
			isValid = false;
			$('#msg_MesDevolucion').html('').hide();
			$('#msg_AnioDevolucion').html('').hide();
			$('#msg_MesAnioDevolucion').html('Seleccione un Mes y un Año de Devolucion').show();
		}else{
			$('#msg_MesAnioDevolucion').html('').hide();
		}		
	}
	
	if ($("#rdbMesDevolucionNoAplica").is(":checked")){
		$('#msg_MesDevolucion').html('').hide();
		$('#msg_AnioDevolucion').html('').hide();
		$('#msg_MesAnioDevolucion').html('').hide();
	}
	
		
	
	//Validar la Fecha de Correo
	
	 var fcorreo=$("#fCorreo").val();
	
	if(fcorreo.length==0){
		isValid = false;
		$('#msg_fcorreo').html('Ingrese la Fecha de Envio Correo').show();
	} else {
		if(fcorreo.length==10){
			var fhoy = new Date();
			
			array_fecha = fcorreo.split("/");

			var dia=array_fecha[0];
			var mes=(array_fecha[1]-1);
			var ano=(array_fecha[2]);
			var fechaCorreo = new Date(ano,mes,dia);
			
			
			if(fechaCorreo <= fhoy){
		    	$('#msg_fcorreo').html('').hide();
		    	if(dia>31){
		    		isValid=false;
		    		$('#msg_fcorreo').html('Los dias estan comprendidos entre [01-31]').show();
		    	}
		    	
		    	if(mes>13){
		    		isValid=false;
		    		$('#msg_fcorreo').html('El mes estan comprendido entre [01-12]').show();
		    	}
		    	
		    	if(ano<2000){
		    		isValid=false;
		    		$('#msg_fcorreo').html('Solo se aceptaran actas con Años superiores al 2000').show();
		    	}
		    }else{
		    	isValid=false;
		    	$('#msg_fcorreo').html('La Fecha del Correo es mayor a la Fecha Actual').show();
		    }

		}else{
			isValid=false;
	    	$('#msg_fcorreo').html('Ingrese la fecha con un formato: dd/mm/yyyy').show();
		}
		
	}

	//Validar la Hora de Correo
	
	var hcorreo=$("#hCorreo").val();
	
	if(hcorreo.length==0){
		isValid = false;
		$('#msg_hcorreo').html('Ingrese la Hora de Envio Correo').show();
	}else{
		if(hcorreo.length==5){			
			$('#msg_hcorreo').html('').hide();
			array_hora = hcorreo.split(":");
			var hora=array_hora[0];
			var min=array_hora[1];
			if(hora>24){
	    		isValid=false;
	    		$('#msg_hcorreo').html('Los horas estan comprendidos entre [00-24]').show();
	    	}
			
			if(min>60){
	    		isValid=false;
	    		$('#msg_hcorreo').html('Los horas estan comprendidos entre [00-60]').show();
	    	}
		} else {
			isValid=false;
	    	$('#msg_hcorreo').html('Ingrese la Hora con un formato: hh:mm').show();
		}
	}			
	
	

	//Validar Modo Recojo
	
	var enviado=$("#cboEnviado").val();
	
	if(enviado=="-1"){
		isValid=false;
		$('#msg_enviado').html('Seleccione un Valor para Envio').show();
	}else{
		$('#msg_enviado').html('').hide();
	}	
	
	var destino=$("#cboDestinoFisico").val();
	
	if(destino=="-1"){
		isValid=false;
		$('#msg_destino').html('Seleccione un Valor para Destino').show();
	}else{
		$('#msg_destino').html('').hide();
	}	

	return isValid;
}


function validateStep2() {
	var isValid = true;
	//Validar Canal
	var canal = $("#h_canal").val();
	if (canal.length == 0) {
		isValid = false;
		$('#msg_canal').html('Seleccione un Canal').show();
	} else {
		$('#msg_canal').html('').hide();
	}

	//Validar Entidad
	var entidad = $("#h_entidad").val();
	if (entidad.length == 0) {
		isValid = false;
		$('#msg_entidad').html('Seleccione una Entidad').show();
	} else {
		$('#msg_entidad').html('').hide();
	}
	
	
	//Validar Correo Gestor
	var idCorreoGestor = $("#h_idCorreoGestor").val();
	if (idCorreoGestor.length == 0) {
		isValid = false;
		$('#msg_GestorActa').html('Seleccione un Gestor del Acta').show();
	} else {
		$('#msg_GestorActa').html('').hide();
	}
	
	
	
	return isValid;
}	

function validateStep3() {
	var isValid = true;

	//Validar Existencia Fichero

	var file = $("#ficheroExcel").val();
	if (file.length != 0) {
		extension = (file.substring(file.lastIndexOf("."))).toLowerCase();
		if(extension==".xls"){
			$('#msg_fileExcel').html('').hide();
		}else{
			isValid=false;		
			$('#msg_fileExcel').html('Compruebe la extensión del archivo a subir(xls)').show();
		}
		
	} else {
		isValid = false;
		$('#msg_fileExcel').html('Ingrese el Archivo en Formato Excel (xls)').show();
	}
	
	return isValid;
}


/*-----------------------------------------------------------------------------------*/
/*  Listar la Descripcion de los Formatos de Devolucion
/*-----------------------------------------------------------------------------------*/


$(document).ready(
	function() {
		$.ajax({
			type : 'GET',
			url : 'listaFormato.do',
			dataType : 'json',
			success : function(result) {
				$("#tipoFormato").append("<br/>");
					for (var x = 0; x < result.length; x++) {
						$("#tipoFormato").append("<input type='radio' name='formato' value='" + result[x].codTipoFormato + "' id='formato' onChange='listaMotivos(this)'>" + result[x].nomTipoFormato + "&nbsp;&nbsp;");
					}					
					$("#tipoFormato").append("<span id='msg_formato' style='color: red;'></span> <br /><br />");
				}
			});
	});


/*-----------------------------------------------------------------------------------*/
/*  Llamar a la ventana Pop Up de Gestor del Acta
/*-----------------------------------------------------------------------------------*/

function addGestorActa(codigo) { 	
	open('addGestor.do?codEntidad='+codigo,'','top=100,left=300,width=600,height=300') ; 
}


/*-----------------------------------------------------------------------------------*/
/*  Indicar Prefijo SER
/*-----------------------------------------------------------------------------------*/

function verSER() { 	
	$("#cboRubro").html('');
	$("#cboRubro").append("<option value='-1' selected>---------------------</option>");
	$('#h_Rubro').val('');
}


/*-----------------------------------------------------------------------------------*/
/*  Llenar la lista de Abreviaturas de Rubros
/*-----------------------------------------------------------------------------------*/

function verRubros() { 	
	$.ajax({
		type : 'GET',
		url : 'listarubromaterial.do',
		dataType : 'json',
		success : function(result) {
		for (var x = 0; x < result.length; x++) {
			$("#cboRubro").append(
			"<option value='" + result[x].abrRubro + "'>"
							  + result[x].nomRubro + "</option>");
			}
		}
	});
}

/*-----------------------------------------------------------------------------------*/
/*  Obtener Datos de Rubro
/*-----------------------------------------------------------------------------------*/

$(document).ready(
	function() {
		$("#cboRubro").change(
		function() {
			var abrRubro = $("#cboRubro option:selected").val();
			$('#h_Rubro').val(abrRubro);				
		});
});

