package pe.tgestiona.logistica.inversa.bean;

public class ConfiguracionBean {
	private String codConfiguracion;
	private String nomConfiguracion;
	private String codCriterio;
	private String valor;
	private String parametro;
	private String fecha;
	private String existencia;

	public ConfiguracionBean() {
		super();
	}
	
	public String getCodConfiguracion() {
		return codConfiguracion;
	}
	public void setCodConfiguracion(String codConfiguracion) {
		this.codConfiguracion = codConfiguracion;
	}
	public String getNomConfiguracion() {
		return nomConfiguracion;
	}
	public void setNomConfiguracion(String nomConfiguracion) {
		this.nomConfiguracion = nomConfiguracion;
	}
	public String getCodCriterio() {
		return codCriterio;
	}
	public void setCodCriterio(String codCriterio) {
		this.codCriterio = codCriterio;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getParametro() {
		return parametro;
	}
	public void setParametro(String parametro) {
		this.parametro = parametro;
	}
	public String getExistencia() {
		return existencia;
	}
	public void setExistencia(String existencia) {
		this.existencia = existencia;
	}
	
	
		
}
