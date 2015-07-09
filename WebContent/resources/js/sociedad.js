/* Lista de Sociedad de Material */
$(document).ready(
  function() {
	$.ajax({
	  type : 'GET',
		url : 'listasociedadmaterial.do',
		dataType : 'json',
		success : function(result) {
		for (x = 0; x < result.length; x++) {
			$("#cboSocMat").append(
			"<option value='" + result[x].codSociedad + "'>"
							  + result[x].nomSociedad + "</option>");
			}
		}
		});
  });

/*OBTENER DATOS DE SOCIEDAD DE MATERIAL*/

$(document).ready(
		function() {
			$("#cboSocMat").change(
					function() {
						var codSociedad = $("#cboSocMat option:selected").val();
						$.ajax({
							type : 'GET',
							url : 'obtenerDatosSociedadMaterial.do?codSociedadMaterial=' + codSociedad,
							dataType : 'json',
							success : function(result) {
								$('#h_SocMat').val(result.codSociedad);
							}
						});

					});
		});