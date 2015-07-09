/* Lista de Negocio */
$(document).ready(
  function() {
	$.ajax({
	  type : 'GET',
		url : 'listanegocio.do',
		dataType : 'json',
		success : function(result) {
		for (x = 0; x < result.length; x++) {
			$("#cboNegocio").append(
			"<option value='" + result[x].codNegocio + "'>"
							  + result[x].nomNegocio + "</option>");
			}
		}
		});
  });

/*OBTENER DATOS DE NEGOCIO*/

$(document).ready(
		function() {
			$("#cboNegocio").change(
					function() {
						var codNegocio = $("#cboNegocio option:selected").val();
						$.ajax({
							type : 'GET',
							url : 'obtenerDatosNegocio.do?codNegocio=' + codNegocio,
							dataType : 'json',
							success : function(result) {
								$('#h_Negocio').val(result.codNegocio);
							}
						});

					});
		});
  