/*-----------------------------------------------------------------------------------*/
/*  Definimos la funcion para acceder al formulario Login
/*-----------------------------------------------------------------------------------*/

function accederLogin() {
	if (validarInputs()) {
		$('form').submit();
	}
}

function enterKey(e) {
    if (e.keyCode == 13) {
    	accederLogin();
    }
}

function validarInputs() {
	var isValid = true;
	
	//Validar Usuario
	var usuario = $("#t_user").val();

	if (usuario.length == 0) {
		isValid = false;
		$('#msg_user').html('Debe Ingresar un usuario').show();
	} else {
		$('#msg_user').html('').hide();
	}
	
	//Validar Password
	var password = $("#t_pass").val();
	
	if (password.length == 0) {
		isValid = false;
		$('#msg_pass').html('Debe Ingresar una contraseña').show();
	} else {
		$('#msg_pass').html('').hide();
	}

	return isValid;
}