package pe.tgestiona.logistica.inversa.bean;

public class DespachoRRSSBean {
	private String anio;
	private String serie;
	private String material;
	private String descripcion;
	private String destino;
	private String tipo;
	private String gEmision;
	private String fEnvio;
	private String observacion;
	private int cantReg;
	
	public int getCantReg() {
		return cantReg;
	}
	public void setCantReg(int cantReg) {
		this.cantReg = cantReg;
	}
	public DespachoRRSSBean() {
		super();
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getgEmision() {
		return gEmision;
	}
	public void setgEmision(String gEmision) {
		this.gEmision = gEmision;
	}
	public String getfEnvio() {
		return fEnvio;
	}
	public void setfEnvio(String fEnvio) {
		this.fEnvio = fEnvio;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	
	
	
}
