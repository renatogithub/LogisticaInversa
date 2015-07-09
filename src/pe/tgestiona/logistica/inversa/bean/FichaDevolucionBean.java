package pe.tgestiona.logistica.inversa.bean;

public class FichaDevolucionBean {
	private String nroTicket;
	private String fechaCarga;
	private String usuario;
	private String codTipoDevolucion;
	private String tipoDevolucion;
	private String tipoDevolucionAbvr;
	private String tipoFormato;
	private String canal;
	private String entidad;
	private int cantidadItem;
	private String nombre;
	private String mesGestion;
	private String estado;	
	private String fechaSolicitud;
	private String horaSolicitud;
	private String enviado;
	private String destino;
	private String gestorActa;
	private boolean retaceria;
	private String zonal;
	private String esPedido;
	private String prefijoActa;
	private String fechaDevolucion;
	private String mesDevolucion;

	
	public FichaDevolucionBean() {
		super();
	}
	
	public String getCanal() {
		return canal;
	}

	public void setCanal(String canal) {
		this.canal = canal;
	}

	
	public String getNroTicket() {
		return nroTicket;
	}
	public void setNroTicket(String nroTicket) {
		this.nroTicket = nroTicket;
	}
	public String getFechaCarga() {
		return fechaCarga;
	}
	public void setFechaCarga(String fechaCarga) {
		this.fechaCarga = fechaCarga;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getTipoDevolucion() {
		return tipoDevolucion;
	}
	public void setTipoDevolucion(String tipoDevolucion) {
		this.tipoDevolucion = tipoDevolucion;
	}
	public String getEntidad() {
		return entidad;
	}
	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getMesGestion() {
		return mesGestion;
	}
	public void setMesGestion(String mesGestion) {
		this.mesGestion = mesGestion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	public String getHoraSolicitud() {
		return horaSolicitud;
	}
	public void setHoraSolicitud(String horaSolicitud) {
		this.horaSolicitud = horaSolicitud;
	}

	public String getTipoFormato() {
		return tipoFormato;
	}

	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}

	public String getEnviado() {
		return enviado;
	}

	public void setEnviado(String enviado) {
		this.enviado = enviado;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getTipoDevolucionAbvr() {
		return tipoDevolucionAbvr;
	}

	public void setTipoDevolucionAbvr(String tipoDevolucionAbvr) {
		this.tipoDevolucionAbvr = tipoDevolucionAbvr;
	}

	public String getCodTipoDevolucion() {
		return codTipoDevolucion;
	}

	public void setCodTipoDevolucion(String codTipoDevolucion) {
		this.codTipoDevolucion = codTipoDevolucion;
	}


	public boolean isRetaceria() {
		return retaceria;
	}

	public void setRetaceria(boolean retaceria) {
		this.retaceria = retaceria;
	}


	public String getZonal() {
		return zonal;
	}

	public void setZonal(String zonal) {
		this.zonal = zonal;
	}

	public String getEsPedido() {
		return esPedido;
	}

	public void setEsPedido(String esPedido) {
		this.esPedido = esPedido;
	}

	public String getPrefijoActa() {
		return prefijoActa;
	}

	public void setPrefijoActa(String prefijoActa) {
		this.prefijoActa = prefijoActa;
	}

	public int getCantidadItem() {
		return cantidadItem;
	}

	public void setCantidadItem(int cantidadItem) {
		this.cantidadItem = cantidadItem;
	}

	public String getFechaDevolucion() {
		return fechaDevolucion;
	}

	public void setFechaDevolucion(String fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}


	public String getMesDevolucion() {
		return mesDevolucion;
	}

	public void setMesDevolucion(String mesDevolucion) {
		this.mesDevolucion = mesDevolucion;
	}

	public String getGestorActa() {
		return gestorActa;
	}

	public void setGestorActa(String gestorActa) {
		this.gestorActa = gestorActa;
	}


	
	
	
}
