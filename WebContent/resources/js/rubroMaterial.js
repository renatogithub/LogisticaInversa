/* Lista de Rubro de Material */
$(document).ready(
  function() {
	$.ajax({
	  type : 'GET',
		url : 'listarubromaterial.do',
		dataType : 'json',
		success : function(result) {
		for (x = 0; x < result.length; x++) {
			$("#cboRubMat").append(
			"<option value='" + result[x].codRubro + "'>"
							  + result[x].nomRubro + "</option>");
			}
		}
		});
  });

/*OBTENER DATOS DE RUBRO DE MATERIAL*/

$(document).ready(
		function() {
			$("#cboRubMat").change(
					function() {
						var codRubroMaterial = $("#cboRubMat option:selected").val();
						$.ajax({
							type : 'GET',
							url : 'obtenerDatosRubroMaterial.do?codRubroMaterial=' + codRubroMaterial,
							dataType : 'json',
							success : function(result) {
								$('#h_RubMat').val(result.codRubro);
							}
						});

					});
		});