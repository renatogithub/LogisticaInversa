$(document).ready(
	function() {
		$.ajax({
			type : 'GET',
			url : 'listaFormato.do',
			dataType : 'json',
			success : function(result) {
				$("#tipoFormato").append("<br/>");
					for (var x = 0; x < result.length; x++) {
						$("#tipoFormato").append("<input type='radio' name='formato' value='" + result[x].codTipoFormato + "' id='formato" + x + "' onChange='verActas(this)'>" + result[x].nomTipoFormato + "&nbsp;&nbsp;");
					}					
					$("#tipoFormato").append("<span id='msg_formato' style='color: red;'></span> <br /><br />");
				}
			});
});

function buscar_ticket(){
	var ticket=$('#t_ticket').val();
	$("#dialog:ui-dialog").dialog("destroy");
	if(ticket==""){		
    	$("#dialog").text("Debe llenar el campo Ticket antes de Anularlo");
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
		$("#loadingmessage").show();		
		$("#dialog").hide();
		$("#tbConsultaActaQry").hide();
		$.ajax({
			type : 'GET',				
			url:'getTicketAnular.do?t_ticket=' + ticket,
			dataType : 'json',
			success : function(data) {
				var $tabla = $("#tbConsultaActaQry");
				$("#tbConsultaActaQry tbody tr").remove();
				if(data.nroTicket==null){
			    	$("#dialog").text("El Nro de Ticket No Existe Por Favor Revisar");
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
			    	 $("#loadingmessage").hide(); 
			    	 document.getElementById("btnAnular").disabled = true;
				}else{
					var contenido="<tr>" +
					  "<th colspan=6><b>INFORMACION DE ACTA A ANULAR</b></th>" +
					  "</tr>"+
					  "<tr>" + 						  
					  "<th colspan=4 style='text-align:left;'>Nro. Ticket: " + data.nroTicket  + "</th>" +
					  "</tr>" + 
					  "<tr>" + 						  
					  "<th>Fecha de Carga: </th><td>" + data.fechaCarga  + "</td>" +
					  "<th>Fecha de Solicitud: </th><td>" + data.fechaSolicitud  + "</td>" +
					  "</tr>" +
					  "<tr>" + 						  
					  "<th>Canal: </th><td>" + data.canal  + "</td>" +
					  "<th>Entidad: </th><td>" + data.entidad  + "</td>" +
					  "</tr>" +
					  "<tr>" + 						  
					  "<th>Formato: </th><td>" + data.tipoFormato  + "</td>" +
					  "<th>Tipo de Devolucion: </th><td>" + data.tipoDevolucion  + "</td>" +
					  "</tr>" +
					  "<tr>" + 						  
					  "<th>Enviado: </th><td>" + data.enviado  + "</td>" +
					  "<th>Destino: </th><td>" + data.destino  + "</td>" +
					  "</tr>" + 
					  "<tr>" + 						  
					  "<th>Cantidad: </th><td>" + data.cantReg  + "</td>" +
					  "<th>Estado: </th><td>" + data.estado  + "</td>" +
					  "</tr>" ;
					
					if(data.estado=="ANULADO"){
						document.getElementById("btnAnular").disabled = true;
				    	$("#dialog").text("La Ficha se encuentra se encuentra Anulada por lo cual no podra volverla a Anular");
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
						document.getElementById("btnAnular").disabled = false;
						document.getElementById("t_observaciones").disabled = false;
						
					}
					$tabla.append(contenido);

				    $("#loadingmessage").hide(); 
				    $("#tbConsultaActaQry").show();
				}
				
				
			}
		});
		
		
	}

}


function anular_acta(){
	var ticket=$('#t_ticket').val();
	var observaciones=$('#t_observaciones').val();
	$("#dialog:ui-dialog").dialog("destroy");
	if(ticket.trim()==""){
		$("#dialog").text("Debe llenar el campo Ticket antes de Anularlo");
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
		if(observaciones.trim()==""){		
	    	$("#dialog").text("Debe llenar el campo Observaciones para Anular el Ticket");
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
			$('form').submit();
		}
	}		
	
}