
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



/*OBTENER DATOS DE Gestor*/

$(document).ready(
		function() {
			$("#cbo_GestorActa").change(
					function() {
						var correo = $("#cbo_GestorActa option:selected").val();
						$('#t_correo').val('');
						$('#t_tlf').val('');
						$('#t_rpm').val('');
						$.ajax({
							type : 'GET',
							url : 'obtenerDatosGestor.do?correo=' + correo,
							dataType : 'json',
							success : function(result) {								
								$('#h_idCorreoGestor').val(result.correo);
								$('#t_correoGestor').val(result.correo);
								$('#t_tlfGestor').val(result.tlfAnexo);
								$('#t_rpmGestor').val(result.rpm);
							}
						});

					});
		});

/*GRABAR DATOS DE Gestor*/

function onGrabarGestor(){
	if (validarGestor()) {
		$('form').submit();
	}
}

function validarGestor(){
	var isGestorValid = true;
	
	if (validateStep1() == false) {
		isGestorValid = false;
	} 	
	
	return isGestorValid;
}


function validateStep1() {
	var isValid = true;
	
	var nombre = $("#t_nombre").val();
	var correo = $("#t_correo").val();

	if (nombre.length == 0) {
		isValid = false;
		$('#msg_nombre').html('Ingrese el nombre de un Gestor').show();
	} else {
		$('#msg_nombre').html('').hide();
	}
	
	if (correo.length == 0) {
		isValid = false;
		$('#msg_correo').html('Ingrese el nombre de un Correo').show();
	} else {
		var regex = /[\w-\.]{2,}@([\w-]{2,}\.)*([\w-]{2,}\.)[\w-]{2,4}/;
		if (regex.test(correo.trim())) {
	        $('#msg_correo').html('').hide();
	    }else{
	    	isValid = false;
	        $('#msg_correo').html('Formato de Correo Incorrecto').show();
	    }
		
		
	}
	
	return isValid;
	
}

function loadGestor(){
	var paramstr = window.location.search.substr(1);	
	paramstr=decodeURI(paramstr);
	var params=paramstr.split("=");
	var entidad=params[1];
	$('#t_entidad').val(entidad);
}


$(function() {
	$("#dialog:ui-dialog").dialog("destroy");
		
    $("#btnRegistrar").click(function() {
    	$("#dialog:ui-dialog").dialog("destroy");
    	$("#dialog").text("¿Seguro que desea Grabar los Datos del Gestor?");
        $("#dialog").dialog({
        	width:400,
            height:180,
            modal: true,
            buttons: {
              Si: function() {
            	  onGrabarGestor();
            	  $("#dialog:ui-dialog" ).dialog( "destroy" );
            	  $("#dialog" ).dialog({
                      autoOpen: false,
                      width:300,
                      height:200,
                      modal: true,
                      buttons: {
                          Ok: function() {
                        	  $("#dialog").text("Gestor Registrado");
                              $("#dialog").dialog( "open" );
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
      });
  });