package pe.tgestiona.logistica.inversa.controller;



import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import pe.tgestiona.logistica.inversa.bean.UsuarioBean;
import pe.tgestiona.logistica.inversa.service.UsuarioService;

@Controller
@SessionAttributes({"usuario","fecha","fechaLetras"})
@Scope("session")
public class MainController {
	
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private UsuarioBean usuarioBean;
	
	
	@RequestMapping(value="inicio",method = RequestMethod.GET)
	public String inicio() {	  
		return "login";
	}
 
	@RequestMapping(value="welcome")
	public String welcome(){
		return "welcome";
	}
	
	
	@RequestMapping(value="principal", method=RequestMethod.POST)
	public String acceso(HttpServletRequest req, HttpServletResponse res){
		String username = req.getParameter("user");
		String password = req.getParameter("pass");
		UsuarioBean usuarioBean=new UsuarioBean();		
		usuarioBean=usuarioService.iniciarSesion(username, password, req.getRemoteAddr());
		HttpSession sesion = req.getSession();

		if (usuarioBean == null) {
			return "error";
		}else{
			sesion.setAttribute("usuario", username);
			sesion.setAttribute("perfil", usuarioBean.getPerfil());
			sesion.setAttribute("nomperfil", usuarioBean.getArea());
			return "welcome";
		}		
	}
	
	
	 @RequestMapping(value="logout", method=RequestMethod.GET)
     public String logout(HttpServletRequest req, HttpServletResponse res) throws IOException {
		 HttpSession session=req.getSession();  
		 String usuario=(String)session.getAttribute("usuario");
		 if(session !=null){
			usuarioService.cerrarSesion(usuario);
			session.removeAttribute("usuario");
			session.invalidate();                               
		 }
		 return "login";
		 		 
     }
	 
	@RequestMapping(value="consultarRRSS",method = RequestMethod.GET)
	public String consultarRRSS() {	  
		return "consultarRRSS";
	}
	
}
