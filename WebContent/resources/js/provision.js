/* Lista de Provisiones */
$(document).ready(
  function() {
	$.ajax({
	  type : 'GET',
		url : 'listaprovision.do',
		dataType : 'json',
		success : function(result) {
		for (x = 0; x < result.length; x++) {
			$("#cboProv").append(
			"<option value='" + result[x].codProvisionPlantaExterna + "'>"
							  + result[x].nomProvisionPlantaExterna + "</option>");
			}
		}
		});
  });



/*OBTENER DATOS DE PROVISION*/

$(document).ready(
		function() {
			$("#cboProv").change(
					function() {
						var codProvision = $("#cboProv option:selected").val();
						$.ajax({
							type : 'GET',
							url : 'obtenerDatosProvision.do?codProvision=' + codProvision,
							dataType : 'json',
							success : function(result) {
								$('#h_Prov').val(result.codProvisionPlantaExterna);
							}
						});

					});
		});
