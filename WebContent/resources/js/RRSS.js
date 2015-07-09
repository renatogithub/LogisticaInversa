function busca_RRSS(pagina) {
	var opcion=$("input[name='r_criterio']:checked").val();
	var material=$('#t_material').val();
	var serie=$('#t_serie').val();	
	$("#loadingmessage").show();
	$("#tbConsultaRRSS").hide();
	$.ajax({
		type : 'GET',		
		url:'lstRRSS.do?opcion=' + opcion + '&vMaterial=' + material + '&vSerie=' + serie + '&vPag=' + pagina,
		dataType : 'json',
		success : function(data) {
			var $tabla = $("#tbConsultaRRSS");
			var $paginador=$("#paginador");
			var cantidad=0;
			var pagActual=pagina;
			var pagAnt=pagActual-1;
			var pagSgt=pagActual+1;
			var pagIntervalo=5;
			var totalReg=0;			
			
			$("#tbConsultaRRSS tbody tr").remove();
			$("#paginador a").remove();
			$("#paginador span").remove();
			for (var idx in data){	
				actas= data[idx];
				cantidad=cantidad+1;									
				totalReg=actas.cantReg;
		    }
		    var pagUlt=totalReg/20;
		    pagUlt=Math.round(pagUlt);
			
			var contenido="<tr>" +
						  "<th colspan=9><b>LISTADO DE RESIDUOS SOLIDOS</b></th>" +
						  "</tr>"+
						  "<tr>" + 						  
						  "<th colspan=9>Pagina <b>" + pagActual + " de " + pagUlt + ". </b> Total de Registros: <b>" + totalReg + "</b></th>" +
						  "</tr>"+
						  "<tr>" +
							  "<th>Material</th>"+
							  "<th>Serie</th>"+
							  "<th>Año</th>"+
							  "<th>Descripcion</th>"+
							  "<th>Destino</th>"+
							  "<th>Tipo</th>"+
							  "<th>GEmision</th>"+
							  "<th>FEnvio</th>"+
							  "<th>Observaciones</th>"+
						  "</tr>";

			$tabla.append(contenido);
			
		    for (var idx in data)
		    {		    	
		    	rrss= data[idx];
		        $tabla.append(
		            "<tr>"+
		            "<td>" + rrss.material + "</td>" +
		            "<td>" + rrss.serie + "</td>" +
		            "<td>" + rrss.anio + "</td>" +
		            "<td>" + rrss.descripcion + "</td>" +
		            "<td>" + rrss.destino + "</td>" +
		            "<td>" + rrss.tipo + "</td>" +
		            "<td>" + rrss.gEmision + "</td>" +
		            "<td>" + rrss.fEnvio + "</td>" +
		            "<td>" + rrss.observacion + "</td>" +
		            "</tr>"); 
		    } 
		    $("#loadingmessage").hide(); 
		    $("#tbConsultaRRSS").show();
		    
		    if(parseInt(pagActual)>(parseInt(pagIntervalo)+1)) {
		    	$paginador.append("<a id='pagina' onclick=busca_RRSS('1'); class='paginador'><< Primero</a>");			    	
		    }

		    for (var i = (parseInt(pagActual)-parseInt(pagIntervalo)) ; i <= (parseInt(pagActual)-1) ; i ++) {
		    	if(i>=1){
			    	$paginador.append("<a id='pagina' onclick=busca_RRSS('" + i + "'); class='paginador'>" + i + "</a>");			    		
		    	}
		    }
		    
		    $paginador.append("<span class='paginadoractivo'>"+pagActual+ "</span>");
		    
		    for (var i = (parseInt(pagActual)+1) ; i <= (parseInt(pagActual)+parseInt(pagIntervalo)) ; i ++) {
		    	if(i<=pagUlt){
			    	$paginador.append("<a id='pagina' onclick=busca_RRSS('" + i + "'); class='paginador'>" + i + "</a>");			    		
		    	}
		    }
		    
		    if(parseInt(pagActual)<(parseInt(pagUlt)-parseInt(pagIntervalo))) {
		    	$paginador.append("<a id='pagina' onclick=busca_RRSS('" + pagUlt + "'); class='paginador'>Ultimo>></a>");
		    }
		    $paginador.append("<br>");
		}
	});
	
}