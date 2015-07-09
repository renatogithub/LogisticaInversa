package pe.tgestiona.logistica.inversa.bean;

public class ActaDevolucionBean extends FichaDevolucionBean {
	private String nroActa;
	private String nroTicket;
	private int cantItems;
	private String fecha;
	private String estadoPedido;
	private String modoRecojo;
	
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

	public int getCantItems() {
		return cantItems;
	}
	public void setCantItems(int cantItems) {
		this.cantItems = cantItems;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getEstadoPedido() {
		return estadoPedido;
	}
	public void setEstadoPedido(String estadoPedido) {
		this.estadoPedido = estadoPedido;
	}
	public String getModoRecojo() {
		return modoRecojo;
	}
	public void setModoRecojo(String modoRecojo) {
		this.modoRecojo = modoRecojo;
	}
	
	
	
	
}
