package pe.tgestiona.logistica.inversa.bean;

import java.io.Serializable;

public class EntidadBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private String codEntidad;
	private String nomEntidad;
	private String codCanal;
	private String codUbigeo;
	private String centro6_0;
	private String almacen6_0;
	private String direccion;
	
	
	public EntidadBean() {
		super();
	}
	public String getCodEntidad() {
		return codEntidad;
	}
	public void setCodEntidad(String codEntidad) {
		this.codEntidad = codEntidad;
	}
	public String getNomEntidad() {
		return nomEntidad;
	}
	public void setNomEntidad(String nomEntidad) {
		this.nomEntidad = nomEntidad;
	}
	public String getCodCanal() {
		return codCanal;
	}
	public void setCodCanal(String codCanal) {
		this.codCanal = codCanal;
	}
	public String getCodUbigeo() {
		return codUbigeo;
	}
	public void setCodUbigeo(String codUbigeo) {
		this.codUbigeo = codUbigeo;
	}
	public String getCentro6_0() {
		return centro6_0;
	}
	public void setCentro6_0(String centro6_0) {
		this.centro6_0 = centro6_0;
	}
	public String getAlmacen6_0() {
		return almacen6_0;
	}
	public void setAlmacen6_0(String almacen6_0) {
		this.almacen6_0 = almacen6_0;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	
	
	
	
}
