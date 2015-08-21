package pe.tgestiona.logistica.inversa.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Util {
	
	public String obtenerMesAnioLiquidacion(String mesDevol){
		String formato="";
		String sanio="";
		formato=mesDevol+ " " + sanio.substring(2);		
		return formato;
	}
	
	public String obtenerMesLiquidacionAverias(String fechaDevolucion){
		String formato="";
		String smes="";
		String sanio="";
		String smesLetras="";
		int mes=0;
		
		if(fechaDevolucion.length()==8){
			sanio=fechaDevolucion.substring(6, 8);			
		}else{
			sanio=fechaDevolucion.substring(8, 10);			
		}
		smes=fechaDevolucion.substring(3, 5);

		mes=Integer.parseInt(smes);
		
		switch(mes){
		case 1: 
			smesLetras="ENERO";
			break;
		case 2: 
			smesLetras="FEBRERO";
			break;
		case 3: 
			smesLetras="MARZO";
			break;
		case 4: 
			smesLetras="ABRIL";
			break;
		case 5: 
			smesLetras="MAYO";
			break;
		case 6: 
			smesLetras="JUNIO";
			break;
		case 7: 
			smesLetras="JULIO";
			break;
		case 8: 
			smesLetras="AGOSTO";
			break;
		case 9: 
			smesLetras="SETIEMBRE";
			break;
		case 10: 
			smesLetras="OCTUBRE";
			break;
		case 11: 
			smesLetras="NOVIEMBRE";
			break;
		case 12: 
			smesLetras="DICIEMBRE";
			break;
	}
		formato=smesLetras + " " + sanio;
		
		return formato;
	}

	public String obtenerFechaActa(){
		String formato="";
		Calendar fecha = new GregorianCalendar();
		int anio = fecha.get(Calendar.YEAR);
		int mes=fecha.get(Calendar.MONTH)+1;
		String sanio="";
		String smes="";
		smes=(mes>9)?String.valueOf(mes):"0"+String.valueOf(mes);
		sanio=String.valueOf(anio);
		formato=smes+sanio.substring(2);		
		return formato;
	}
	
	
	public String obtenerFechaTicket(){
		String formato="";
		Calendar fecha = new GregorianCalendar();
		int anio = fecha.get(Calendar.YEAR);
		int mes=fecha.get(Calendar.MONTH)+1;
		int dia=fecha.get(Calendar.DAY_OF_MONTH);
		String sanio="";
		String smes="";
		String sdia="";
		sdia=(dia>9)?String.valueOf(dia):"0"+String.valueOf(dia);
		smes=(mes>9)?String.valueOf(mes):"0"+String.valueOf(mes);
		sanio=String.valueOf(anio);
		formato=sdia+smes+sanio.substring(2);
		return formato;
	}
	
	public String obtenerFecha(){
		Calendar fecha = new GregorianCalendar();
		int año = fecha.get(Calendar.YEAR);
		int mes=fecha.get(Calendar.MONTH)+1;
		int dia=fecha.get(Calendar.DAY_OF_MONTH);
		String sfecha="";
		String saño="";
		String smes="";
		String sdia="";
	
		sdia=(dia>9)?String.valueOf(dia):"0"+String.valueOf(dia);
		smes=(mes>9)?String.valueOf(mes):"0"+String.valueOf(mes);
		saño=String.valueOf(año);
		
		sfecha=sdia+"/"+smes+"/"+saño;	
		
		return sfecha;
	}
	
	public String obtenerMesLetras(){
		Calendar fecha = new GregorianCalendar();
		int mes=fecha.get(Calendar.MONTH)+1;
		String smes="";
		
		switch(mes){
		case 1: 
			smes="ENERO";
			break;
		case 2: 
			smes="FEBRERO";
			break;
		case 3: 
			smes="MARZO";
			break;
		case 4: 
			smes="ABRIL";
			break;
		case 5: 
			smes="MAYO";
			break;
		case 6: 
			smes="JUNIO";
			break;
		case 7: 
			smes="JULIO";
			break;
		case 8: 
			smes="AGOSTO";
			break;
		case 9: 
			smes="SETIEMBRE";
			break;
		case 10: 
			smes="OCTUBRE";
			break;
		case 11: 
			smes="NOVIEMBRE";
			break;
		case 12: 
			smes="DICIEMBRE";
			break;
	}
		return smes;
	}
	
	public String obtenerHora(){
		Calendar fecha = new GregorianCalendar();
		int hora=fecha.get(Calendar.HOUR_OF_DAY);
		int min=fecha.get(Calendar.MINUTE);
		int seg=fecha.get(Calendar.SECOND);
		String tiempo="";
		String shora="";
		String smin="";
		String sseg="";
		
		shora=(hora>9)?String.valueOf(hora):"0"+String.valueOf(hora);
		smin=(min>9)?String.valueOf(min):"0"+String.valueOf(min);
		sseg=(seg>9)?String.valueOf(seg):"0"+String.valueOf(seg);
		
		tiempo=shora + ":" + smin + ":" + sseg;
		
		return tiempo;
	}
	
	public String obtenerFechaLetras(){
		
		Calendar fecha = new GregorianCalendar();
		int año = fecha.get(Calendar.YEAR);
		int mes=fecha.get(Calendar.MONTH)+1;
		int dia=fecha.get(Calendar.DAY_OF_MONTH);
		int diasem=fecha.get(Calendar.DAY_OF_WEEK);	
		String sfecha="";
		String saño="";
		String smes="";
		String sdia="";
		String sdiasem="";
		sdia=(dia>9)?String.valueOf(dia):"0"+String.valueOf(dia);
		saño=String.valueOf(año);
		switch(diasem){
			case 1: 
				sdiasem="Domingo";
				break;
			case 2: 
				sdiasem="Lunes";
				break;
			case 3: 
				sdiasem="Martes";
				break;
			case 4: 
				sdiasem="Miercoles";
				break;
			case 5: 
				sdiasem="Jueves";
				break;
			case 6: 
				sdiasem="Viernes";
				break;
			case 7: 
				sdiasem="Sabado";
				break;
		}
		
		
		switch(mes){
			case 1: 
				smes="Enero";
				break;
			case 2: 
				smes="Febrero";
				break;
			case 3: 
				smes="Marzo";
				break;
			case 4: 
				smes="Abril";
				break;
			case 5: 
				smes="Mayo";
				break;
			case 6: 
				smes="Junio";
				break;
			case 7: 
				smes="Julio";
				break;
			case 8: 
				smes="Agosto";
				break;
			case 9: 
				smes="Setiembre";
				break;
			case 10: 
				smes="Octubre";
				break;
			case 11: 
				smes="Noviembre";
				break;
			case 12: 
				smes="Diciembre";
				break;
		}
		
		sfecha=sdiasem + " " + sdia + " de " +smes + " del " + saño;	
		
		return sfecha;
	}
	
	public boolean caracterInvalido(String serie){
		boolean existe=false;
		String letra="";
		int contador=0;
		String caracteresValidos[]={"1","2","3","4","5","6","7","8","9","0","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		for(int l=0;l<serie.length();l++){
			letra=serie.substring(l, (l+1));
			contador=0;
			for(int c=0;c<caracteresValidos.length;c++){
				if(letra.equals(caracteresValidos[c])){
					contador=contador+1;
					break;
				}
			}
			
			if(contador==0){
				existe=true;
			}
		}
		
		
		return existe;
	}
	
	public String rellenar(String serie, int cant){
		int largo=cant-serie.length();
		String ceros="";
		String valor="";
		for(int i=0;i<largo;i++){
			ceros=ceros+"0";
		}
		valor=ceros.concat(serie);
		return valor;

	}
	
	public String quitarDigitosIzquierda(String serie, int cant){
		int retirar=serie.length()-cant;
		String valor="";
		valor=serie.substring(retirar);		
		return valor;

	}
	
	public String obtenerFechaAnteriorHistorial(int cantMeses,String fecha){
		String fechaInicio="";
		int anio,mes,mesInicio;
		anio=obtenerAnio(fecha);
		mes=obtenerMes(fecha);
		mesInicio=mes-cantMeses;
		String smes="",sanio="",sdia="";
				
		if(mesInicio<=0){
			mesInicio=(12+mesInicio);
			anio=anio-1;
		}

		sdia="01";
		
		smes=(mesInicio>9)?String.valueOf(mesInicio):"0"+String.valueOf(mesInicio);
		sanio=String.valueOf(anio);
		
		fechaInicio=sdia + "/"  + smes + "/" + sanio;
		
		return fechaInicio;
	}
	
	public String obtenerFechaAnteriorHistorial(String fecha){
		String fechaFin="";
		int anio,mes,mesInicio;
		anio=obtenerAnio(fecha);
		mes=obtenerMes(fecha);
		mesInicio=mes-1;
		String smes="",sanio="",sdia="";
				
		if(mesInicio<=0){
			mesInicio=(12+mesInicio);
			anio=anio-1;
		}
		
		switch(mesInicio){
		case 1:sdia="31";
			   break;
		case 2:if(anio%4==0){
					sdia="29";	   										
				}else{
					sdia="28";	   						
				}
				break;
		case 3:sdia="31";
			   break;
		case 4:sdia="30";
				break;
		case 5:sdia="31";
				break;
		case 6:sdia="30";
				break;		
		case 7:sdia="31";
				break;
		case 8:sdia="31";
				break;
		case 9:sdia="30";
				break;
		case 10:sdia="31";
				break;
		case 11:sdia="30";
				break;		
		case 12:sdia="31";
		   		break;
	}
		
		smes=(mesInicio>9)?String.valueOf(mesInicio):"0"+String.valueOf(mesInicio);
		sanio=String.valueOf(anio);
		
		fechaFin=sdia + "/" + smes + "/" + sanio;
		
		return fechaFin;
	}
	
	
	public int obtenerAnio(String fecha){
		int anio=0;
		anio=Integer.parseInt(fecha.trim().substring(6, 10));
		return anio;
	}
	
	public int obtenerMes(String fecha){
		int mes=0;
		mes=Integer.parseInt(fecha.trim().substring(3, 5));
		return mes;
	}
	
	public int obtenerDia(String fecha){
		int dia=0;
		dia=Integer.parseInt(fecha.trim().substring(0, 2));
		return dia;
	}
	
	public String obtenerFechaPosteriorAcopio(int cantMeses,String fecha){
		String fechaInicio="";
		int anio,mes,mesInicio,dia;
		anio=obtenerAnio(fecha);
		mes=obtenerMes(fecha);
		dia=obtenerDia(fecha);
		mesInicio=mes+cantMeses;
		String sdia="",smes="",sanio="";
				
		if(mesInicio>12){
			mesInicio=(mesInicio-12);
			anio=anio+1;
		}
		
		sdia=(dia>9)?String.valueOf(dia):"0"+String.valueOf(dia);
		smes=(mesInicio>9)?String.valueOf(mesInicio):"0"+String.valueOf(mesInicio);
		sanio=String.valueOf(anio);
		
		fechaInicio=sdia + "/" + smes + "/" + sanio;
		
		return fechaInicio;
	}
	
	
	public String obtenerMesDigitos(String mes){
		String mesDigito="";
			switch(mes){
			case "ENERO": 
				mesDigito="01";
				break;
			case "FEBRERO": 
				mesDigito="02";
				break;
			case "MARZO": 
				mesDigito="03";
				break;
			case "ABRIL": 
				mesDigito="04";
				break;
			case "MAYO": 
				mesDigito="05";
				break;
			case "JUNIO": 
				mesDigito="06";
				break;
			case "JULIO": 
				mesDigito="07";
				break;
			case "AGOSTO": 
				mesDigito="08";
				break;
			case "SETIEMBRE": 
				mesDigito="09";
				break;
			case "OCTUBRE": 
				mesDigito="10";
				break;
			case "NOVIEMBRE": 
				mesDigito="11";
				break;
			case "DICIEMBRE": 
				mesDigito="12";
				break;
		}
		return mesDigito;	
	}
	
	public String eliminarEspaciosBlanco(String serie){
		String serieSinEspacios="";
		String letra="";
		for(int c=0;c<serie.trim().length();c++){
			letra=serie.substring(c, (c+1));
			if(!letra.equals(" ")){
				serieSinEspacios=serieSinEspacios+letra;
			}
		}
		
		return serieSinEspacios;
	}
	
	public String descripcionSAPApostrofe(String descripcionSAP,String sTextoBuscado,String sTextoReemplazado){
		String nuevaDescripcionSAP="";
		String[] arraysTextoBuscado = descripcionSAP.trim().split(sTextoBuscado);
	    
	    for (int i = 0; i < arraysTextoBuscado.length; i++) {
	    	if(i<=arraysTextoBuscado.length-2){
		    	nuevaDescripcionSAP=nuevaDescripcionSAP + arraysTextoBuscado[i] + sTextoReemplazado;	    		
	    	}else{
	    		nuevaDescripcionSAP=nuevaDescripcionSAP + arraysTextoBuscado[i];
	    	}

	    }
	    
		return nuevaDescripcionSAP;
	}
	
}
