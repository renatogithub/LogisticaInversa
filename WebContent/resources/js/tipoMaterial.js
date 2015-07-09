/* Lista de Tipo de Material */
$(document).ready(
  function() {
	$.ajax({
	  type : 'GET',
		url : 'listatipomaterial.do',
		dataType : 'json',
		success : function(result) {
		for (x = 0; x < result.length; x++) {
			$("#cboTipoMat").append(
			"<option value='" + result[x].codTipoMaterial + "'>"
							  + result[x].nomTipoMaterial + "</option>");
			}
		}
		});
  });

/*OBTENER DATOS DE TIPO DE MATERIAL*/

$(document).ready(
		function() {
			$("#cboTipoMat").change(
					function() {
						var codTipoMaterial = $("#cboTipoMat option:selected").val();
						$.ajax({
							type : 'GET',
							url : 'obtenerDatosTipoMaterial.do?codTipoMaterial=' + codTipoMaterial,
							dataType : 'json',
							success : function(result) {
								$('#h_TipoMat').val(result.codTipoMaterial);
							}
						});

					});
		});