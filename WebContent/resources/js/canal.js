/* Lista de Canales */
$(document).ready(
  function() {
	$.ajax({
	  type : 'GET',
		url : 'listacanales.do',
		dataType : 'json',
		success : function(result) {
		for (x = 0; x < result.length; x++) {
			$("#cbo_canal").append(
			"<option value='" + result[x].codCanal + "'>"
							  + result[x].nomCanal + "</option>");
			}
		}
		});
  });