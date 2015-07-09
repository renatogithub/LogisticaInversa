/* Lista de Tecnologia de Material */
$(document).ready(
  function() {
	$.ajax({
	  type : 'GET',
		url : 'listatecnologiamaterial.do',
		dataType : 'json',
		success : function(result) {
		for (x = 0; x < result.length; x++) {
			$("#cboTecMat").append(
			"<option value='" + result[x].codTecnologia + "'>"
							  + result[x].nomTecnologia + "</option>");
			}
		}
		});
  });

/*OBTENER DATOS DE TECNOLOGIA DE MATERIAL*/

$(document).ready(
		function() {
			$("#cboTecMat").change(
					function() {
						var codTecnologiaMaterial = $("#cboTecMat option:selected").val();
						$.ajax({
							type : 'GET',
							url : 'obtenerDatosTecnologiaMaterial.do?codTecnologiaMaterial=' + codTecnologiaMaterial,
							dataType : 'json',
							success : function(result) {
								$('#h_TecMat').val(result.codTecnologia);
							}
						});

					});
		});