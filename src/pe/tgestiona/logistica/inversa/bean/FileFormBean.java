package pe.tgestiona.logistica.inversa.bean;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FileFormBean {

	CommonsMultipartFile ficheroExcel;

	public CommonsMultipartFile getFicheroExcel() {
		return ficheroExcel;
	}

	public void setFicheroExcel(CommonsMultipartFile ficheroExcel) {
		this.ficheroExcel = ficheroExcel;
	}

	
	
	
/*	public CommonsMultipartFile getFichero() {
		return fichero;
	}

	public void setFichero(CommonsMultipartFile fichero) {
		this.fichero = fichero;
	}
	*/
	
	
	
}
