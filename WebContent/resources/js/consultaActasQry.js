 
function onPress_ENTER()
{
	var keyPressed = event.keyCode || event.which;

	//if ENTER is pressed
    if(keyPressed==13){
    	busca_Acta('1');
        keyPressed=null;
    }else{
        return false;
    }
}

/*-----------------------------------------------------------------------------------*/
/*  Utilizando AJAX para traer los elementos de la consulta y escribirlos en la Tabla
/*-----------------------------------------------------------------------------------*/

function busca_Acta(pagina) {
	var ticket=$('#t_ticket').val();
	var acta=$('#t_acta').val();
	var estado=$("#cboEstado option:selected" ).val();
	var material=$('#t_material').val();
	var serie=$('#t_serie').val();
	var descrip=$('#t_descrip').val();
	var f1=$('#fcorreo1').val();
	var f2=$('#fcorreo2').val();
	var estadoActa=$("#cboEstadoActa option:selected" ).val();
	$("#dialog:ui-dialog").dialog("destroy");
	if(ticket=="" && acta=="" && estado=="" && material=="" && serie=="" && descrip=="" && f1=="" && f2=="" && estadoActa==""){		
    	$("#dialog").text("Debe escoger al menos un criterio de busqueda");
    	$("#dialog").dialog({
    		autoOpen: true,
    		modal:true,
    		width:400,
    		buttons: {
    			"Cerrar": function () {
    				$(this).dialog("close");
    			}
    		}
    		});
	}else{	
		$("#dialog").hide();
		$("#loadingmessage").show();
		$("#tbConsultaActaQry").hide();
		$.ajax({
			type : 'GET',				
			url:'lstActaGeneradas.do?vActa=' + acta + '&vNroTicket=' + ticket + '&vMaterial=' + material + '&vSerie=' + serie + '&vDescrip=' + descrip + '&vEstado=' + estado + '&vF1=' + f1 + '&vF2=' + f2 + '&vEstadoActa=' + estadoActa + '&vPag=' + pagina,
			dataType : 'json',
			success : function(data) {
				var $tabla = $("#tbConsultaActaQry");
				var $paginador=$("#paginador");
				var cantidad=0;
				var pagActual=pagina;
				var pagIntervalo=5;
				var totalReg=0;
				var nroActa;
				$("#tbConsultaActaQry tbody tr").remove();
				$("#paginador a").remove();
				$("#paginador span").remove();
				
				for (var idx in data){	
					cantidad=cantidad+1;
					total= data[idx];										
					totalReg=total[7];
			    }
				
			    var pagUlt=totalReg/20;
			    pagUlt=Math.round(pagUlt);
			    
				var contenido="<tr>" +
							  "<th colspan=6><b>LISTADO DE CONTENIDO DEL ACTA</b></th>" +
							  "</tr>"+
							  "<tr>" + 						  
							  "<th colspan=6>Pagina <b>" + pagActual + " de " + pagUlt + ". </b> Total de Registros: <b>" + totalReg + "</b></th>" +
							  "</tr>"+
							  "<tr>" +
								  "<th style='text-align:center;'>Acta</th>"+
								  "<th style='width:5%;text-align:center;'>Ticket</th>"+
								  "<th style='text-align:center;'>Material</th>"+
								  "<th style='text-align:center;'>Serie</th>"+
								  "<th style='width:50%;text-align:center;'>Descripcion</th>"+
								  "<th style='text-align:center;'>Estado</th>"+
							  "</tr>";
				$tabla.append(contenido);			
			    for (var idx in data)
			    {		    	
			    	actas= data[idx];
			    	
			        $tabla.append(
			            "<tr>"+
			            "<td style='text-align:center;'>" + actas[0] + "</td>" +
			            "<td style='width:5principal%;text-align:center;'>" + actas[1] + "</td>" +
			            "<td style='text-align:center;'>" + actas[2] + "</td>" +
			            "<td style='text-align:center;'>" + actas[3] + "</td>" +
			            "<td style='width:50%;text-align:left;'>" + actas[4] + "</td>" +
			            "<td style='text-align:center;'>" + actas[5] + "</td>" +
			            "</tr>"); 
			    } 
			    var enlace='lstActaGeneradasExcel.do?accion=descarga&vActa=' + acta + '&vNroTicket=' + ticket + '&vMaterial=' + material + '&vSerie=' + serie + '&vDescrip=' + descrip + '&vEstado=' + estado + '&vPag=' + pagina;
			    document.getElementById('miEnlace').href=enlace;	
			    
			    $("#loadingmessage").hide(); 
			    $("#tbConsultaActaQry").show();
			    
			    if(parseInt(pagActual)>(parseInt(pagIntervalo)+1)) {
			    	$paginador.append("<a id='pagina' onclick=busca_Acta('1'); class='paginador'><< Primero</a>");			    	
			    }

			    for (var i = (parseInt(pagActual)-parseInt(pagIntervalo)) ; i <= (parseInt(pagActual)-1) ; i ++) {
			    	if(i>=1){
				    	$paginador.append("<a id='pagina' onclick=busca_Acta('" + i + "'); class='paginador'>" + i + "</a>");			    		
			    	}
			    }
			    
			    $paginador.append("<span class='paginadoractivo'>"+pagActual+ "</span>");
			    
			    for (var i = (parseInt(pagActual)+1) ; i <= (parseInt(pagActual)+parseInt(pagIntervalo)) ; i ++) {
			    	if(i<=pagUlt){
				    	$paginador.append("<a id='pagina' onclick=busca_Acta('" + i + "'); class='paginador'>" + i + "</a>");			    		
			    	}
			    }
			    
			    if(parseInt(pagActual)<(parseInt(pagUlt)-parseInt(pagIntervalo))) {
			    	$paginador.append("<a id='pagina' onclick=busca_Acta('" + pagUlt + "'); class='paginador'>Ultimo>></a>");
			    }
			    
			    $paginador.append("<br>");
			    
			}
		});
	}
}

$(function() {
    $("#fcorreo1").datepicker();
 });

$(function() {
    $("#fcorreo2").datepicker();
 });
