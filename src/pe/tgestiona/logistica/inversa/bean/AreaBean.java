package pe.tgestiona.logistica.inversa.bean;

import java.io.Serializable;

public class AreaBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private String codArea;
	private String nomArea;
	
	
	public AreaBean() {
		super();
	}

	public AreaBean(String codArea, String nomArea) {
		super();
		this.codArea = codArea;
		this.nomArea = nomArea;
	}
	
	public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getNomArea() {
		return nomArea;
	}
	public void setNomArea(String nomArea) {
		this.nomArea = nomArea;
	}

	
	
	
}
