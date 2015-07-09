
/*AGREGAR DATOS AL COMBO ENTIDAD DE ACUERDO AL CANAL*/

$(document).ready(
		function() {
			$("#cbo_canal").change(
					function() {
						var codCanal = $("#cbo_canal option:selected").val();
						$.ajax({
							type : 'GET',
							url : 'listaEntidades.do?codCanal=' + codCanal,
							dataType : 'json',
							success : function(result) {
								$('#cbo_entidad option[value!=-1]').remove();
								$('#h_entidad').val('');
								$('#direccion').val('');
								for (x = 0; x < result.length; x++) {
									$("#cbo_entidad").append(
											"<option value='"
													+ result[x].codEntidad + "'>"
													+ result[x].nomEntidad
													+ "</option>");
								}

							}
						});

					});
		});


/*OBTENER DATOS DE ENTIDAD*/

$(document).ready(
		function() {
			$("#cbo_entidad").change(
					function() {
						var codEntidad = $("#cbo_entidad option:selected").val();
						$.ajax({
							type : 'GET',
							url : 'datosEntidad.do?codEntidad=' + codEntidad,
							dataType : 'json',
							success : function(data1) {
								$('#h_entidad').val(data1.codEntidad);
								$('#h_nomentidad').val(data1.nomEntidad);
								$('#direccion').val(data1.direccion);
								$('#btnAddGestor').attr("onclick","addGestorActa('" + data1.codEntidad + "')");								

								//Datos del Gestor
								
								$.ajax({
									type : 'GET',
									url : 'listagestoresentidad.do?codEntidad=' + codEntidad,
									dataType : 'json',
									success : function(data3) {
										$('#cbo_GestorActa option[value!=-1]').remove();
										$('#t_correoGestor').val('');
										$('#t_tlfGestor').val('');
										$('#t_rpmGestor').val('');
										for (var x = 0; x < data3.length; x++) {
											$("#cbo_GestorActa").append(
													"<option value='"
															+ data3[x].correo + "'>"
															+ data3[x].nombre
															+ "</option>");
										}
									}
								});
							}
						});
						
						
					
						

					});
		});


/* Lista de Motivo de Devolucion */

function listaMotivos(radio){
	$("#cboDevolucion").html('');
	$("#h_tipoFormato").val(radio.value);
	$("#h_nomtipoFormato").val(radio.nextSibling.data.trim());	
	$.ajax({
		type : 'GET',
		url : 'listaDevolucion.do?formato=' + radio.value,
		dataType : 'json',
		success : function(result) {
			$("#cboDevolucion").append("<option value=''>---------------------</option>");
			for (x = 0; x < result.length; x++) {
				$("#cboDevolucion").append(
					"<option value='" + result[x].codDevol + "'>" + result[x].nomDevol + "</option>");
				}
			}
	});
}

function obtenerDatosMotivo(){
	var codDevolucion = $("#cboDevolucion option:selected").val();
	var nomDevolucion = $("#cboDevolucion option:selected").text();
	$.ajax({
		type : 'GET',
		url : 'obtenerDatosMotivo.do?cboDevolucion=' + codDevolucion,
		dataType : 'json',
		success : function(result) {
			$('#h_devolucion').val(codDevolucion);
			$('#h_tdevolucion').val(nomDevolucion);
			$('#h_abrvdevolucion').val(result.abrvMotivo);
		}
	});
}

/*Obtener Hora*/

$(document).ready(
		function() {
			$("#cboHora").change(
			function() {
				var hora = $("#cboHora option:selected").val();
				var minuto = $("#cboMinuto option:selected").val();
				$('#hCorreo').val(hora+":"+minuto);				
			});
});

$(document).ready(
		function() {
			$("#cboMinuto").change(
			function() {
				var hora = $("#cboHora option:selected").val();
				var minuto = $("#cboMinuto option:selected").val();
				$('#hCorreo').val(hora+":"+minuto);
			});
});



/*OBTENER DATOS DE CANAL*/

$(document).ready(
		function() {
			$("#cbo_canal").change(
			function() {
				var codCanal = $("#cbo_canal option:selected").val();
				$('#h_canal').val(codCanal);
			});
});




	

