package pe.tgestiona.logistica.inversa.bean;

public class PlaneamientoBean {
	private String nroActa;
	private String nroTicket;
	private int nro;
	private String nroSerie;
	private String fProgDevolLurin;
	private String fRProg;
	private String observacion;
	private int cantColumnasPlaneamiento;
	
	public PlaneamientoBean() {
		super();
	}
	
	public String getNroActa() {
		return nroActa;
	}
	
	public void setNroActa(String nroActa) {
		this.nroActa = nroActa;
	}
	
	public String getNroTicket() {
		return nroTicket;
	}
	
	public void setNroTicket(String nroTicket) {
		this.nroTicket = nroTicket;
	}
	
	public int getNro() {
		return nro;
	}
	
	public void setNro(int nro) {
		this.nro = nro;
	}
	
	public String getNroSerie() {
		return nroSerie;
	}
	
	public void setNroSerie(String nroSerie) {
		this.nroSerie = nroSerie;
	}
	
	public String getfProgDevolLurin() {
		return fProgDevolLurin;
	}
	
	public void setfProgDevolLurin(String fProgDevolLurin) {
		this.fProgDevolLurin = fProgDevolLurin;
	}
	
	public String getfRProg() {
		return fRProg;
	}
	
	public void setfRProg(String fRProg) {
		this.fRProg = fRProg;
	}
	
	public String getObservacion() {
		return observacion;
	}
	
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public int getCantColumnasPlaneamiento() {
		return cantColumnasPlaneamiento;
	}

	public void setCantColumnasPlaneamiento(int cantColumnasPlaneamiento) {
		this.cantColumnasPlaneamiento = cantColumnasPlaneamiento;
	}
	
	
}
