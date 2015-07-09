
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



/*OBTENER DATOS DE CONTACTO*/

$(document).ready(
		function() {
			$("#cbo_contacto").change(
					function() {
						var correo = $("#cbo_contacto option:selected").val();
						$('#t_correoContacto').val('');
						$('#t_tlfContacto').val('');
						$('#t_rpmContacto').val('');
						$.ajax({
							type : 'GET',
							url : 'obtenerDatosContacto.do?correo=' + correo,
							dataType : 'json',
							success : function(result) {
								
								$('#h_idCorreoContacto').val(result.correo);
								$('#t_correoContacto').val(result.correo);
								$('#t_tlfContacto').val(result.tlfAnexo);
								$('#t_rpmContacto').val(result.rpm);
							}
						});

					});
		});

/*GRABAR DATOS DE CONTACTO*/

function onGrabarContacto(){
	if (validarContacto()) {
		$('form').submit();
	}
}

function validarContacto(){
	var isContactoValid = true;
	
	if (validateStep1() == false) {
		isContactoValid = false;
	} 	
	
	return isContactoValid;
}


function validateStep1() {
	var isValid = true;
	
	var nombre = $("#t_nombre").val();
	var correo = $("#t_correo").val();

	if (nombre.length == 0) {
		isValid = false;
		$('#msg_nombre').html('Ingrese el nombre de un Contacto').show();
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

function loadContacto(){
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
    	$("#dialog").text("¿Seguro que desea Grabar los Datos del Contacto?");
        $("#dialog").dialog({
        	width:400,
            height:180,
            modal: true,
            buttons: {
              Si: function() {
            	  onGrabarContacto();
            	  $("#dialog:ui-dialog" ).dialog( "destroy" );
            	  $("#dialog" ).dialog({
                      autoOpen: false,
                      width:300,
                      height:200,
                      modal: true,
                      buttons: {
                          Ok: function() {
                        	  $("#dialog").text("Contacto Registrado");
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