package pe.tgestiona.logistica.inversa.bean;

import java.io.Serializable;

public class MaterialBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private String codigo;
	private String descripcion;
	private int cantReg;
	private String umd;
	private String peso;
	private String volUnitario;
	private String precio;
	private String sociedad;
	private String seriado;
	private String provision;
	private String negocio;
	private String rubro;
	private String tipoMaterial;
	private String tecnologia;
	private String lotizable;
	
	public MaterialBean() {
		super();
	}

		
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public int getCantReg() {
		return cantReg;
	}


	public void setCantReg(int cantReg) {
		this.cantReg = cantReg;
	}


	public String getPeso() {
		return peso;
	}


	public void setPeso(String peso) {
		this.peso = peso;
	}


	public String getVolUnitario() {
		return volUnitario;
	}


	public void setVolUnitario(String volUnitario) {
		this.volUnitario = volUnitario;
	}


	public String getPrecio() {
		return precio;
	}


	public void setPrecio(String precio) {
		this.precio = precio;
	}


	public String getSociedad() {
		return sociedad;
	}


	public void setSociedad(String sociedad) {
		this.sociedad = sociedad;
	}


	public String getSeriado() {
		return seriado;
	}


	public void setSeriado(String seriado) {
		this.seriado = seriado;
	}


	public String getProvision() {
		return provision;
	}


	public void setProvision(String provision) {
		this.provision = provision;
	}


	public String getNegocio() {
		return negocio;
	}


	public void setNegocio(String negocio) {
		this.negocio = negocio;
	}


	public String getRubro() {
		return rubro;
	}


	public void setRubro(String rubro) {
		this.rubro = rubro;
	}


	public String getTipoMaterial() {
		return tipoMaterial;
	}


	public void setTipoMaterial(String tipoMaterial) {
		this.tipoMaterial = tipoMaterial;
	}


	public String getTecnologia() {
		return tecnologia;
	}


	public void setTecnologia(String tecnologia) {
		this.tecnologia = tecnologia;
	}


	public String getUmd() {
		return umd;
	}


	public void setUmd(String umd) {
		this.umd = umd;
	}


	public String getLotizable() {
		return lotizable;
	}


	public void setLotizable(String lotizable) {
		this.lotizable = lotizable;
	}
	
	

}
