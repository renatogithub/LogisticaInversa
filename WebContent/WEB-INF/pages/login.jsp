<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page session="true"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="resources/jquery/jquery-1.9.1.js"></script>
<script src="resources/jquery/jquery-ui.js"></script>
<link rel="stylesheet" href="resources/css/jquery-ui.css">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="shortcut icon" href="resources/icon/logo.ico" /> 
<title>Sistema Integrado - Gestion de Logistica Inversa</title>

<!-- <link rel="stylesheet" type="text/css" href="resources/css/message.css"> -->
<script type="text/javascript" src="resources/js/login.js"></script>

<style type="text/css" >

/* 	.button:hover{
	    cursor: pointer;
	    background-color: rgb(172, 213, 252);
	}
	
	.button {
		display: inline-block;
		line-height: 1;
		padding: 7px 10px;
		text-decoration: none;
		font-weight: bold;
		color: #fff;
		background-color: #39c;
		-moz-border-radius: 5px;
		-webkit-border-radius: 5px;
		-khtml-border-radius: 5px;
		border-radius: 5px;
	}
	
	input.button, button.button {
		border: 0px none;
	}
	
	input[type="button"]:disabled {
	    background: #dddddd;
	} */
	
/* 	#login{
    box-shadow:
          0 0 2px rgba(0, 0, 0, 0.2),
          0 1px 1px rgba(0, 0, 0, .2),
          0 3px 0 #fff,
          0 4px 0 rgba(0, 0, 0, .2),
          0 6px 0 #fff,
          0 7px 0 rgba(0, 0, 0, .2);
}

#login{
    position: absolute;
    z-index: 0;
}

#login:before{
    content: '';
    position: absolute;
    z-index: -1;
    border: 1px dashed #ccc;
    top: 5px;
    bottom: 5px;
    left: 5px;
    right: 5px;
    -moz-box-shadow: 0 0 0 1px #fff;
    -webkit-box-shadow: 0 0 0 1px #fff;
    box-shadow: 0 0 0 1px #fff;
}

h1{
    text-shadow: 0 1px 0 rgba(255, 255, 255, .7), 0px 2px 0 rgba(0, 0, 0, .5);
    text-transform: uppercase;
    text-align: center;
    color: #666;
    margin: 0 0 30px 0;
    letter-spacing: 4px;
    font: normal 26px/1 Verdana, Helvetica;
    position: relative;
}

h1:after, h1:before{
    background-color: #777;
    content: "";
    height: 1px;
    position: absolute;
    top: 15px;
    width: 120px;
}

h1:after{
    background-image: -webkit-gradient(linear, left top, right top, from(#777), to(#fff));
    background-image: -webkit-linear-gradient(left, #777, #fff);
    background-image: -moz-linear-gradient(left, #777, #fff);
    background-image: -ms-linear-gradient(left, #777, #fff);
    background-image: -o-linear-gradient(left, #777, #fff);
    background-image: linear-gradient(left, #777, #fff);
    right: 0;
}

h1:before{
    background-image: -webkit-gradient(linear, right top, left top, from(#777), to(#fff));
    background-image: -webkit-linear-gradient(right, #777, #fff);
    background-image: -moz-linear-gradient(right, #777, #fff);
    background-image: -ms-linear-gradient(right, #777, #fff);
    background-image: -o-linear-gradient(right, #777, #fff);
    background-image: linear-gradient(right, #777, #fff);
    left: 0;
} */

form {
	width: 350px;
	height: 350px;
	margin: 100px auto;
	background: white;
	border-radius: 3px;
	box-shadow: 0 0 10px rgba(0,0,0, .4); 
	text-align: center;
	padding-top: 1px;
}

form .text-field {																			/* Input fields; Username, Password etc. */
	border: 1px solid #a6a6a6;
	width: 230px;
	height: 40px;
	border-radius: 3px;
	margin-top: 10px;
	padding-left: 10px;
	color: #6c6c6c;
	background: #fcfcfc;
	outline: none;
}

form .text-field:focus {
	box-shadow: inset 0 0 2px rgba(0,0,0, .3);
	color: #a6a6a6;
	background: white;
}

form .button {																				/* Submit button */
	border-radius: 3px;
	border: 1px solid #336895;
	box-shadow: inset 0 1px 0 #8dc2f0;
	width: 242px;
	height: 40px;
	margin-top: 20px;
	
	background: linear-gradient(bottom, #4889C2 0%, #5BA7E9 100%);
	background: -o-linear-gradient(bottom, #4889C2 0%, #5BA7E9 100%);
	background: -moz-linear-gradient(bottom, #4889C2 0%, #5BA7E9 100%);
	background: -webkit-linear-gradient(bottom, #4889C2 0%, #5BA7E9 100%);
	background: -ms-linear-gradient(bottom, #4889C2 0%, #5BA7E9 100%);
	
	cursor: pointer;
	color: white;
	font-weight: bold;
	text-shadow: 0 -1px 0 #336895;
	
	font-size: 13px;
}

form .button:hover {
	background: linear-gradient(bottom, #5c96c9 0%, #6bafea 100%);
	background: -o-linear-gradient(bottom, #5c96c9 0%, #6bafea 100%);
	background: -moz-linear-gradient(bottom, #5c96c9 0%, #6bafea 100%);
	background: -webkit-linear-gradient(bottom, #5c96c9 0%, #6bafea 100%);
	background: -ms-linear-gradient(bottom, #5c96c9 0%, #6bafea 100%);
}

form .button:active {
	background: linear-gradient(bottom, #5BA7E9 0%, #4889C2 100%);
	background: -o-linear-gradient(bottom, #5BA7E9 0%, #4889C2 100%);
	background: -moz-linear-gradient(bottom, #5BA7E9 0%, #4889C2 100%);
	background: -webkit-linear-gradient(bottom, #5BA7E9 0%, #4889C2 100%);
	background: -ms-linear-gradient(bottom, #5BA7E9 0%, #4889C2 100%);
	
	box-shadow: inset 0 0 2px rgba(0,0,0, .3), 0 1px 0 white;
}

</style>

</head>
<!-- <body onload='document.loginForm.user.focus();'> -->
 <body>
 
<!-- 	<div id="contenido-box" style="width:300px;"> -->
<!-- 	<p style="text-align: center;">
		<b>Acceso al Sistema</b>
	</p> -->
		
		
 
		 <form name='loginForm' action="<c:url value='principal.do' />" method="post">
			<img alt="" src="resources/imagenes/tgestiona.png">
			<h2>Acceso al Sistema</h2>
		
			<input type="text" class="text-field" placeholder="Username" name="user" id="t_user"/>
			<br><span id="msg_user" style="color: red;font-weight: bold;font-size: 12px;"></span>
		    <input type="password" class="text-field" placeholder="Password" name="pass" id="t_pass"/>
		    <br><span id="msg_pass" style="color: red;font-weight: bold;font-size: 12px;"></span>
		    <input type="button" value="Ingresar" class="button"  onclick="accederLogin();"/>
		    
		</form>
 
<%-- 		<form name='loginForm'
		    action="<c:url value='principal.do' />" method="post">
 
		    <table>
			<tr>
				<td>Usuario:</td>
				<td><input type="text" name="user" id="t_user" value="">
					<br><span id="msg_user" style="color: red;font-weight: bold;font-size: 12px;"></span>
				</td>				
			</tr>
			<tr>
				<td>Contraseña:</td>
				<td><input type="password" name="pass" id="t_pass" value="" onkeypress="return enterKey(event);" />
					<br><span id="msg_pass" style="color: red;font-weight: bold;font-size: 12px;"></span>
				</td>
			</tr>
			<tr>
			    <td colspan='2'>
                    <input name="btnIngresar" type="button" value="Acceder" onclick="accederLogin();" class="button" style="float: right;"/>
                </td>
			</tr>
		   </table>
		</form> --%>
<!-- 	</div> -->
 
</body>
</html>