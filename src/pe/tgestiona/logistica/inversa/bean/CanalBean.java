package pe.tgestiona.logistica.inversa.bean;

import java.io.Serializable;

public class CanalBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String codCanal;
	private String nomCanal;
	
	public CanalBean() {
		super();
	}
	
	public CanalBean(String codCanal, String nomCanal) {
		super();
		this.codCanal = codCanal;
		this.nomCanal = nomCanal;
	}
	
	public String getCodCanal() {
		return codCanal;
	}
	public void setCodCanal(String codCanal) {
		this.codCanal = codCanal;
	}
	public String getNomCanal() {
		return nomCanal;
	}
	public void setNomCanal(String nomCanal) {
		this.nomCanal = nomCanal;
	}
	
	
	
}
