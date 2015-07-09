package pe.tgestiona.logistica.inversa.bean;

public enum ErroresFileEnum{
	CI("Campos Incompletos","01"),
	SCM("Series con Codigo de Material","02"),
	SE("Series Erroneas","03"),
	EC("Campos con contenido Erroneo","04");
	
	private String nombreError;
	private String codError;
	
	
	
	private ErroresFileEnum(String nombreError, String codError) {
		this.nombreError = nombreError;
		this.codError = codError;
	}
	
	
	public String getNombreError() {
		return nombreError;
	}
	public void setNombreError(String nombreError) {
		this.nombreError = nombreError;
	}
	public String getCodError() {
		return codError;
	}
	public void setCodError(String codError) {
		this.codError = codError;
	}
	
	

	
	
}