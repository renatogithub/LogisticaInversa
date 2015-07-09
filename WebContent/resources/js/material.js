function onPress_ENTER()
{
	var keyPressed = event.keyCode || event.which;

	//if ENTER is pressed
    if(keyPressed==13){
    	busca_Material('1');
        keyPressed=null;
    }else{
        return false;
    }
}



/*-----------------------------------------------------------------------------------*/
/*  Utilizando AJAX para traer los elementos de la consulta y escribirlos en la Tabla
/*-----------------------------------------------------------------------------------*/

function busca_Material(pagina) {
	var material=$('#t_material').val();
	var descripcion=$('#t_descripcion').val();
	var lotizable=$("#cboLotizable option:selected" ).val();
	var tipo=$('#t_tipo').val();
	var rubro=$('#t_rubro').val();
	var tecnologia=$('#t_tecnologia').val();

	$("#dialog:ui-dialog").dialog("destroy");
	if(material=="" && descripcion=="" && lotizable=="" && tipo=="" && rubro=="" && tecnologia==""){		
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
		$("#tbConsultaMaterialQry").hide();
		$.ajax({
			type : 'GET',		
			url:'consultaMaterial.do?vMaterial=' + material + '&vDescripcion=' + descripcion + '&vPeso=&vVolumen=&vSeriado=&vTipo=' + tipo + '&vRubro=' + rubro + '&vTecnologia=' + tecnologia + '&vNegocio=&vProvincia=&vSociedad=&vPrecioEquipo=&vLotizable=' + lotizable + '&vPag=' + pagina + '&vRegMostrar=20&vPagIntervalo=5',
			dataType : 'json',
			success : function(data) {
				var $tituloTabla=$("#tituloTabla");				
				var $tabla = $("#tbConsultaMaterialQry");
				var $paginador=$("#paginador");
				var cantidad=0;
				var pagActual=pagina;
				var pagIntervalo=5;
				var totalReg=0;
				var precio;
				var original;
				$("#tbConsultaMaterialQry tbody tr").remove();
				$("#paginador a").remove();
				$("#paginador span").remove();
			//	$("#tituloTabla").remove();
				for (var idx in data){	
					cantidad=cantidad+1;
					total= data[idx];										
					totalReg=total[15];
			    }
				
			    var pagUlt=totalReg/20;
			    pagUlt=Math.round(pagUlt);
			    $("#tituloTabla").html('');
			    var contenidoTitulo="<p style='text-align:center;'><h1>Listado de Materiales</h1></p><br> Pagina " + pagActual + " de " + pagUlt + ". <b>Total de Registros: </b>" + totalReg;
			    $tituloTabla.append(contenidoTitulo);
			    var contenido=    "<tr>" +
								  "<th style='width:10%;text-align:center;'>Material</th>"+
								  "<th style='width:45%;text-align:center;'>Descripción</th>"+
								  "<th style='width:6%;text-align:center;'>Lotizable</th>"+
								  "<th style='width:10%;text-align:center;'>Precio Equipo</th>"+
								  "<th style='width:10%;text-align:center;'>Tipo</th>"+
								  "<th style='width:10%;text-align:center;'>Rubro</th>"+
								  "<th style='width:10%;text-align:center;'>Tecnologia</th>"+
								  "<th style='width:6%;'></th>"+
								  "<th style='width:6%;'></th>"+
							  "</tr>";
			    
			   // contenido="<span><strong>Listado de Materiales</strong></span><br>"  + contenido ;
				$tabla.append(contenido);			
			    for (var idx in data)
			    {		    	
			    	material= data[idx];
			    	if(material[6]=="-"){
			    		precio="-";
			    	}else{
			    		original=parseFloat(material[6]);
			    		precio=original.toFixed(2);
			    	}
			        $tabla.append(
			            "<tr><tbody>"+
			            "<td style='width:10%;text-align:center;'>" + material[0] + "</td>" +
			            "<td style='width:45%;text-align:left;'>" + material[1] + "</td>" +
			            "<td style='width:6%;text-align:center;'>" + material[7] + "</td>" +
			            "<td style='width:10%;text-align:center;'>" + precio + "</td>" +
			            "<td style='width:10%;text-align:left;'>" + material[11] + "</td>" +
			            "<td style='width:10%;text-align:left;'>" + material[12] + "</td>" +
			            "<td style='width:10%;text-align:left;'>" + material[13] + "</td>" +
			         //   "<td style='width:6%;text-align:center;'><a onclick='obtenerMaterial.do?accion=get&t_codigo=" + material[0].trim() +  "'><img src='resources/imagenes/editar-icono.png' style='width:100%;' title='Actualizar Material'/></a></td>" +
			            "<td style='width:6%;text-align:center;'><a href='obtenerMaterial.do?accion=get&t_codigo=" + material[0].trim() +  "'><img src='resources/imagenes/editar-icono.png' style='width:100%;' title='Actualizar Material'/></a></td>" +
			            "<td style='width:6%;text-align:center;'><a href='#'><img src='resources/imagenes/eliminar-icono.png' style='width: 100%;' title='Eliminar Material'/></a></td>" +
			            "</tbody></tr>"); 
			    } 
			    
			    $("#loadingmessage").hide(); 
			    $("#tbConsultaMaterialQry").show();
			    
			    if(parseInt(pagActual)>(parseInt(pagIntervalo)+1)) {
			    	$paginador.append("<a id='pagina' onclick=busca_Material('1'); class='paginador'><< Primero</a>");			    	
			    }

			    for (var i = (parseInt(pagActual)-parseInt(pagIntervalo)) ; i <= (parseInt(pagActual)-1) ; i ++) {
			    	if(i>=1){
				    	$paginador.append("<a id='pagina' onclick=busca_Material('" + i + "'); class='paginador'>" + i + "</a>");			    		
			    	}
			    }
			    
			    $paginador.append("<span class='paginadoractivo'>"+pagActual+ "</span>");
			    
			    for (var i = (parseInt(pagActual)+1) ; i <= (parseInt(pagActual)+parseInt(pagIntervalo)) ; i ++) {
			    	if(i<=pagUlt){
				    	$paginador.append("<a id='pagina' onclick=busca_Material('" + i + "'); class='paginador'>" + i + "</a>");			    		
			    	}
			    }
			    
			    if(parseInt(pagActual)<(parseInt(pagUlt)-parseInt(pagIntervalo))) {
			    	$paginador.append("<a id='pagina' onclick=busca_Material('" + pagUlt + "'); class='paginador'>Ultimo>></a>");
			    }
			    
			    $paginador.append("<br>");
			    
			}
		});
	}
}


function addMaterial() { 	
	window.location="addMaterial.do";
}

function loadMaterial(codigo){
	alert(codigo);
}


/*function loadMaterial(){
	var paramstr = window.location.search.substr(1);
	paramstr=decodeURI(paramstr);
	var params=paramstr.split("=");
	var codMaterial=params[1];
	$.ajax({
		type : 'GET',
		url : 'obtenerDatosMaterial.do?codMaterial=' + codMaterial,
		dataType : 'json',
		success : function(result) {
			$('#t_codigo').val(result.codigo);
			$('#t_descripcion').val(result.descripcion);

		}
	});
	

}*/

function updateMaterial(codigo) { 	
	open('addMaterial.do?codMaterial='+codigo,'','top=100,left=300,width=600,height=550') ; 
}

function onGrabarMaterial(){
	if (validarMaterial()) {
		$("#dialog:ui-dialog").dialog("destroy");
	    $("#dialog").text("¿Seguro que desea Grabar el Material?");
	    $("#dialog").dialog({
	    	width:300,
	        height:150,
	        modal: true,
	        buttons: {
	        	Si: function() {
					$('form').submit();
		            $("#dialog:ui-dialog" ).dialog( "destroy" );
		            $("#dialog" ).dialog({
		            autoOpen: false,
			  		width:350,
					height:150,
		            modal: true,
		            buttons: {
		            Ok: function() {
		            	$(this).dialog( "close" );
		            }
	            }
	            });
		            $("#dialog").text("Material Registrado");
	                $("#dialog").dialog( "open" );
	             },
	              No: function() {
	                $( this ).dialog( "close" );
	              }
	            }
	          });  

		}else{
			$("#dialog").text("Debe ingresar correctamente los datos a Registrar");
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
		}
}


function validarMaterial(){
	var isValid = true;
	
	var codigo=$('#t_codigo').val();
	
	if (codigo.length == 0) {
		isValid = false;
		$('#msg_codigo').html('Ingrese un codigo de Material').show();
	} else {
		$('#msg_codigo').html('').hide();
	}
	
	var descripcion=$('#t_descripcion').val();
	
	if (descripcion.length == 0) {
		isValid = false;
		$('#msg_descripcion').html('Ingrese una Descripcion').show();
	} else {
		$('#msg_descripcion').html('').hide();
	}
	
	var umd=$('#t_umd').val();
	
	if (umd.length == 0) {
		isValid = false;
		$('#msg_umd').html('Ingrese un UMD').show();
	} else {
		$('#msg_umd').html('').hide();
	}
	
	var peso=$('#t_peso').val();	
	
	if (peso.length == 0) {
		isValid = false;
		$('#msg_peso').html('Ingrese un Peso').show();
	} else {
		$('#msg_peso').html('').hide();
	}
	
	var volumen=$('#t_volUnitario').val();	
	
	if (volumen.length == 0) {
		isValid = false;
		$('#msg_volUnitario').html('Ingrese un Volumen').show();
	} else {
		$('#msg_volUnitario').html('').hide();
	}
	
	var precio=$('#t_precio').val();	
	
	if (precio.length == 0) {
		isValid = false;
		$('#msg_precio').html('Ingrese un Precio').show();
	} else {
		$('#msg_precio').html('').hide();
	}
	
	var provision=$("#cboProv option:selected" ).val();
	
	if (provision == -1) {
		isValid = false;
		$('#msg_prov').html('Seleccione una Provisión').show();
	} else {
		$('#msg_prov').html('').hide();
	}
	
	var negocio=$("#cboNegocio option:selected" ).val();
	
	if (negocio == -1) {
		isValid = false;
		$('#msg_Negocio').html('Seleccione una Negocio').show();
	} else {
		$('#msg_Negocio').html('').hide();
	}	
	
	var tipoMaterial=$("#cboTipoMat option:selected" ).val();
	
	if (tipoMaterial == -1) {
		isValid = false;
		$('#msg_TipoMat').html('Seleccione un Tipo de Material').show();
	} else {
		$('#msg_TipoMat').html('').hide();
	}
	
	var rubroMaterial=$("#cboRubMat option:selected" ).val();
	
	if (rubroMaterial == -1) {
		isValid = false;
		$('#msg_RubMat').html('Seleccione un Rubro de Material').show();
	} else {
		$('#msg_RubMat').html('').hide();
	}
	
	var tecnologiaMaterial=$("#cboTecMat option:selected" ).val();
	
	if (tecnologiaMaterial == -1) {
		isValid = false;
		$('#msg_TecMat').html('Seleccione una Tecnologia de Material').show();
	} else {
		$('#msg_TecMat').html('').hide();
	}
	
	var sociedadMaterial=$("#cboSocMat option:selected" ).val();
	
	if (sociedadMaterial == -1) {
		isValid = false;
		$('#msg_SocMat').html('Seleccione una Sociedad de Material').show();
	} else {
		$('#msg_SocMat').html('').hide();
	}

	return isValid;
}


