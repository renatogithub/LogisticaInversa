package pe.tgestiona.logistica.inversa.bean;

public class PedidoEntregaBean {
	private String nroActa;
	private String nroTicket;
	private String codMaterial;
	private String desMaterial;
	private int cantSeries;
	private String centro;
	private String almacen;
	private String lote;
	private String pedido;
	private String posicion;
	private String entrega;
	private String fecCreacionVale;
	
	public PedidoEntregaBean() {
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
	public String getCodMaterial() {
		return codMaterial;
	}
	public void setCodMaterial(String codMaterial) {
		this.codMaterial = codMaterial;
	}
	public String getDesMaterial() {
		return desMaterial;
	}
	public void setDesMaterial(String desMaterial) {
		this.desMaterial = desMaterial;
	}
	public int getCantSeries() {
		return cantSeries;
	}
	public void setCantSeries(int cantSeries) {
		this.cantSeries = cantSeries;
	}
	public String getCentro() {
		return centro;
	}
	public void setCentro(String centro) {
		this.centro = centro;
	}
	public String getAlmacen() {
		return almacen;
	}
	public void setAlmacen(String almacen) {
		this.almacen = almacen;
	}
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getPedido() {
		return pedido;
	}

	public void setPedido(String pedido) {
		this.pedido = pedido;
	}

	public String getPosicion() {
		return posicion;
	}

	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}

	public String getEntrega() {
		return entrega;
	}

	public void setEntrega(String entrega) {
		this.entrega = entrega;
	}

	public String getFecCreacionVale() {
		return fecCreacionVale;
	}

	public void setFecCreacionVale(String fecCreacionVale) {
		this.fecCreacionVale = fecCreacionVale;
	}
	
	
	
	
}
