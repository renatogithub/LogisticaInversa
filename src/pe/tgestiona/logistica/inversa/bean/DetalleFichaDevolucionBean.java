package pe.tgestiona.logistica.inversa.bean;

public class DetalleFichaDevolucionBean {	
	//Datos de Ingreso
	private String nroTicket;
	private int nro;
	private String descripcion;
	private String serie;
	private String codSAP;
	private String tipo;
	private String rubro;
	private String tecnologia;
	private String pesoRRSS;
	private String volumenRRSS;
	private double cantMateriales;

	//Datos del SAP 6.0
	private String serieSAP_6_0;
	private String codMaterialSAP_6_0;
	private String desMaterialSAP_6_0;
	private String centroSAP_6_0;
	private String almacen_6_0;
	private String lote_6_0;
	private String tipoStocks_6_0;
	private String statusSistema_6_0;
	private String statusUsuario_6_0;
	private String modificadoPor_6_0;
	private String creadoPor_6_0;
	private String fechaIngresoSAP_6_0;
	private String fechaUltimoMovSAP_6_0;
	//Datos del SAP 4.7
	private String statusSAP_6_0;
	private String serieSAP_4_7;
	private String codMaterialSAP_4_7;
	private String desMaterialSAP_4_7;
	private String centroSAP_4_7;
	private String almacen_4_7;
	private String lote_4_7;
	private String tipoStocks_4_7;
	private String statusSistema_4_7;
	private String statusUsuario_4_7;
	private String modificadoPor_4_7;
	private String creadoPor_4_7;
	private String fechaIngresoSAP_4_7;
	private String fechaUltimoMovSAP_4_7;
	private String statusSAP_4_7;
	//Datos de Material
	private String negocio;
	private String peso;
	private String precio;
	private String volUnitario;
	private String sociedad;
	private String seriado;
	private String provision;

	//Datos de Validacion
	private String validacionGarantia;
	private String diasGarantia;
	private String estado;
	private String existeRS;
	private String estadoLiquidacion;
	private String fechaRemozadoTaller;
	private String fechaDespachoAlmacen;
	private String fechaValidacion;
	private String fechaSolicitud;
	private String motivoRechazoDevolucion;
	private String observacionAprobado;
	private String liq_noliq;
	private String enviado;
	private String destinoFisico;
	private String fecGestionDF_PL;
	private String modoRecojo;
	private String mesLiquidacion_Averias;
	private String tramosMinimos;
	private int cantColumnasFicha;
	private String indBod;
	private String indLote;
	private String indGar;
	private String obsBodAprob;
	private String obsLoteAprob;
	private String obsGarAprob;	
	private String obsBodRechaz;
	private String obsLoteRechaz;
	private String obsGarRechaz;	
	private String nroPedido;
	private String posPedido;
	private String nroEntrega;
	private String fCreacionPedido;
	private String remozado_Reparado;
	private String diagnostico;
	private String codigoParte;
	private String nroRequerimiento;
	private String nroCircuitoDigital;
	private String direccionMac;
	private String observacion;
	
	public DetalleFichaDevolucionBean() {
		super();
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

	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getCodSAP() {
		return codSAP;
	}
	public void setCodSAP(String codSAP) {
		this.codSAP = codSAP;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getTecnologia() {
		return tecnologia;
	}
	public void setTecnologia(String tecnologia) {
		this.tecnologia = tecnologia;
	}

	public String getRubro() {
		return rubro;
	}
	public void setRubro(String rubro) {
		this.rubro = rubro;
	}

	
	public double getCantMateriales() {
		return cantMateriales;
	}

	public void setCantMateriales(double cantMateriales) {
		this.cantMateriales = cantMateriales;
	}

	public String getSerieSAP_6_0() {
		return serieSAP_6_0;
	}
	public void setSerieSAP_6_0(String serieSAP_6_0) {
		this.serieSAP_6_0 = serieSAP_6_0;
	}
	public String getCodMaterialSAP_6_0() {
		return codMaterialSAP_6_0;
	}
	public void setCodMaterialSAP_6_0(String codMaterialSAP_6_0) {
		this.codMaterialSAP_6_0 = codMaterialSAP_6_0;
	}
	public String getCentroSAP_6_0() {
		return centroSAP_6_0;
	}
	public void setCentroSAP_6_0(String centroSAP_6_0) {
		this.centroSAP_6_0 = centroSAP_6_0;
	}
	public String getAlmacen_6_0() {
		return almacen_6_0;
	}
	public void setAlmacen_6_0(String almacen_6_0) {
		this.almacen_6_0 = almacen_6_0;
	}
	public String getLote_6_0() {
		return lote_6_0;
	}
	public void setLote_6_0(String lote_6_0) {
		this.lote_6_0 = lote_6_0;
	}
	public String getFechaIngresoSAP_6_0() {
		return fechaIngresoSAP_6_0;
	}
	public void setFechaIngresoSAP_6_0(String fechaIngresoSAP_6_0) {
		this.fechaIngresoSAP_6_0 = fechaIngresoSAP_6_0;
	}
	public String getFechaUltimoMovSAP_6_0() {
		return fechaUltimoMovSAP_6_0;
	}
	public void setFechaUltimoMovSAP_6_0(String fechaUltimoMovSAP_6_0) {
		this.fechaUltimoMovSAP_6_0 = fechaUltimoMovSAP_6_0;
	}
	
	public String getStatusSAP_6_0() {
		return statusSAP_6_0;
	}
	public void setStatusSAP_6_0(String statusSAP_6_0) {
		this.statusSAP_6_0 = statusSAP_6_0;
	}
	public String getSerieSAP_4_7() {
		return serieSAP_4_7;
	}
	public void setSerieSAP_4_7(String serieSAP_4_7) {
		this.serieSAP_4_7 = serieSAP_4_7;
	}
	public String getCodMaterialSAP_4_7() {
		return codMaterialSAP_4_7;
	}
	public void setCodMaterialSAP_4_7(String codMaterialSAP_4_7) {
		this.codMaterialSAP_4_7 = codMaterialSAP_4_7;
	}
	public String getCentroSAP_4_7() {
		return centroSAP_4_7;
	}
	public void setCentroSAP_4_7(String centroSAP_4_7) {
		this.centroSAP_4_7 = centroSAP_4_7;
	}
	public String getAlmacen_4_7() {
		return almacen_4_7;
	}
	public void setAlmacen_4_7(String almacen_4_7) {
		this.almacen_4_7 = almacen_4_7;
	}
	public String getLote_4_7() {
		return lote_4_7;
	}
	public void setLote_4_7(String lote_4_7) {
		this.lote_4_7 = lote_4_7;
	}
	public String getFechaIngresoSAP_4_7() {
		return fechaIngresoSAP_4_7;
	}
	public void setFechaIngresoSAP_4_7(String fechaIngresoSAP_4_7) {
		this.fechaIngresoSAP_4_7 = fechaIngresoSAP_4_7;
	}
	public String getFechaUltimoMovSAP_4_7() {
		return fechaUltimoMovSAP_4_7;
	}
	public void setFechaUltimoMovSAP_4_7(String fechaUltimoMovSAP_4_7) {
		this.fechaUltimoMovSAP_4_7 = fechaUltimoMovSAP_4_7;
	}
	
	public String getStatusSAP_4_7() {
		return statusSAP_4_7;
	}
	public void setStatusSAP_4_7(String statusSAP_4_7) {
		this.statusSAP_4_7 = statusSAP_4_7;
	}
	public String getValidacionGarantia() {
		return validacionGarantia;
	}
	public void setValidacionGarantia(String validacionGarantia) {
		this.validacionGarantia = validacionGarantia;
	}
	public String getDiasGarantia() {
		return diasGarantia;
	}
	public void setDiasGarantia(String diasGarantia) {
		this.diasGarantia = diasGarantia;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getExisteRS() {
		return existeRS;
	}
	public void setExisteRS(String existeRS) {
		this.existeRS = existeRS;
	}
	public String getEstadoLiquidacion() {
		return estadoLiquidacion;
	}
	public void setEstadoLiquidacion(String estadoLiquidacion) {
		this.estadoLiquidacion = estadoLiquidacion;
	}
	public String getFechaRemozadoTaller() {
		return fechaRemozadoTaller;
	}
	public void setFechaRemozadoTaller(String fechaRemozadoTaller) {
		this.fechaRemozadoTaller = fechaRemozadoTaller;
	}
	public String getFechaValidacion() {
		return fechaValidacion;
	}
	public void setFechaValidacion(String fechaValidacion) {
		this.fechaValidacion = fechaValidacion;
	}
	public String getMotivoRechazoDevolucion() {
		return motivoRechazoDevolucion;
	}
	public void setMotivoRechazoDevolucion(String motivoRechazoDevolucion) {
		this.motivoRechazoDevolucion = motivoRechazoDevolucion;
	}
	public String getObservacionAprobado() {
		return observacionAprobado;
	}
	public void setObservacionAprobado(String observacionAprobado) {
		this.observacionAprobado = observacionAprobado;
	}


	public String getDesMaterialSAP_6_0() {
		return desMaterialSAP_6_0;
	}


	public void setDesMaterialSAP_6_0(String desMaterialSAP_6_0) {
		this.desMaterialSAP_6_0 = desMaterialSAP_6_0;
	}


	public String getDesMaterialSAP_4_7() {
		return desMaterialSAP_4_7;
	}


	public void setDesMaterialSAP_4_7(String desMaterialSAP_4_7) {
		this.desMaterialSAP_4_7 = desMaterialSAP_4_7;
	}


	public String getTipoStocks_6_0() {
		return tipoStocks_6_0;
	}


	public void setTipoStocks_6_0(String tipoStocks_6_0) {
		this.tipoStocks_6_0 = tipoStocks_6_0;
	}


	public String getStatusSistema_6_0() {
		return statusSistema_6_0;
	}


	public void setStatusSistema_6_0(String statusSistema_6_0) {
		this.statusSistema_6_0 = statusSistema_6_0;
	}


	public String getStatusUsuario_6_0() {
		return statusUsuario_6_0;
	}


	public void setStatusUsuario_6_0(String statusUsuario_6_0) {
		this.statusUsuario_6_0 = statusUsuario_6_0;
	}


	public String getModificadoPor_6_0() {
		return modificadoPor_6_0;
	}


	public void setModificadoPor_6_0(String modificadoPor_6_0) {
		this.modificadoPor_6_0 = modificadoPor_6_0;
	}


	public String getCreadoPor_6_0() {
		return creadoPor_6_0;
	}


	public void setCreadoPor_6_0(String creadoPor_6_0) {
		this.creadoPor_6_0 = creadoPor_6_0;
	}


	public String getTipoStocks_4_7() {
		return tipoStocks_4_7;
	}


	public void setTipoStocks_4_7(String tipoStocks_4_7) {
		this.tipoStocks_4_7 = tipoStocks_4_7;
	}


	public String getStatusSistema_4_7() {
		return statusSistema_4_7;
	}


	public void setStatusSistema_4_7(String statusSistema_4_7) {
		this.statusSistema_4_7 = statusSistema_4_7;
	}


	public String getStatusUsuario_4_7() {
		return statusUsuario_4_7;
	}


	public void setStatusUsuario_4_7(String statusUsuario_4_7) {
		this.statusUsuario_4_7 = statusUsuario_4_7;
	}


	public String getModificadoPor_4_7() {
		return modificadoPor_4_7;
	}


	public void setModificadoPor_4_7(String modificadoPor_4_7) {
		this.modificadoPor_4_7 = modificadoPor_4_7;
	}


	public String getCreadoPor_4_7() {
		return creadoPor_4_7;
	}


	public void setCreadoPor_4_7(String creadoPor_4_7) {
		this.creadoPor_4_7 = creadoPor_4_7;
	}


	public String getNegocio() {
		return negocio;
	}


	public void setNegocio(String negocio) {
		this.negocio = negocio;
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


	public String getLiq_noliq() {
		return liq_noliq;
	}


	public void setLiq_noliq(String liq_noliq) {
		this.liq_noliq = liq_noliq;
	}


	public String getEnviado() {
		return enviado;
	}


	public void setEnviado(String enviado) {
		this.enviado = enviado;
	}


	public String getDestinoFisico() {
		return destinoFisico;
	}


	public void setDestinoFisico(String destinoFisico) {
		this.destinoFisico = destinoFisico;
	}


	public String getFecGestionDF_PL() {
		return fecGestionDF_PL;
	}


	public void setFecGestionDF_PL(String fecGestionDF_PL) {
		this.fecGestionDF_PL = fecGestionDF_PL;
	}


	public String getPrecio() {
		return precio;
	}


	public void setPrecio(String precio) {
		this.precio = precio;
	}


	public String getModoRecojo() {
		return modoRecojo;
	}


	public void setModoRecojo(String modoRecojo) {
		this.modoRecojo = modoRecojo;
	}


	public String getPesoRRSS() {
		return pesoRRSS;
	}


	public void setPesoRRSS(String pesoRRSS) {
		this.pesoRRSS = pesoRRSS;
	}


	public String getVolumenRRSS() {
		return volumenRRSS;
	}


	public void setVolumenRRSS(String volumenRRSS) {
		this.volumenRRSS = volumenRRSS;
	}


	public String getMesLiquidacion_Averias() {
		return mesLiquidacion_Averias;
	}


	public void setMesLiquidacion_Averias(String mesLiquidacion_Averias) {
		this.mesLiquidacion_Averias = mesLiquidacion_Averias;
	}


	public String getTramosMinimos() {
		return tramosMinimos;
	}


	public void setTramosMinimos(String tramosMinimos) {
		this.tramosMinimos = tramosMinimos;
	}


	public String getFechaDespachoAlmacen() {
		return fechaDespachoAlmacen;
	}

	public void setFechaDespachoAlmacen(String fechaDespachoAlmacen) {
		this.fechaDespachoAlmacen = fechaDespachoAlmacen;
	}

	public int getCantColumnasFicha() {
		return cantColumnasFicha;
	}

	public void setCantColumnasFicha(int cantColumnasFicha) {
		this.cantColumnasFicha = cantColumnasFicha;
	}

	public String getIndBod() {
		return indBod;
	}

	public void setIndBod(String indBod) {
		this.indBod = indBod;
	}

	public String getIndLote() {
		return indLote;
	}

	public void setIndLote(String indLote) {
		this.indLote = indLote;
	}

	public String getIndGar() {
		return indGar;
	}

	public void setIndGar(String indGar) {
		this.indGar = indGar;
	}

	public String getObsBodAprob() {
		return obsBodAprob;
	}

	public void setObsBodAprob(String obsBodAprob) {
		this.obsBodAprob = obsBodAprob;
	}

	public String getObsLoteAprob() {
		return obsLoteAprob;
	}

	public void setObsLoteAprob(String obsLoteAprob) {
		this.obsLoteAprob = obsLoteAprob;
	}

	public String getObsGarAprob() {
		return obsGarAprob;
	}

	public void setObsGarAprob(String obsGarAprob) {
		this.obsGarAprob = obsGarAprob;
	}

	public String getObsBodRechaz() {
		return obsBodRechaz;
	}

	public void setObsBodRechaz(String obsBodRechaz) {
		this.obsBodRechaz = obsBodRechaz;
	}

	public String getObsLoteRechaz() {
		return obsLoteRechaz;
	}

	public void setObsLoteRechaz(String obsLoteRechaz) {
		this.obsLoteRechaz = obsLoteRechaz;
	}

	public String getObsGarRechaz() {
		return obsGarRechaz;
	}

	public void setObsGarRechaz(String obsGarRechaz) {
		this.obsGarRechaz = obsGarRechaz;
	}

	public String getNroPedido() {
		return nroPedido;
	}

	public void setNroPedido(String nroPedido) {
		this.nroPedido = nroPedido;
	}

	public String getPosPedido() {
		return posPedido;
	}

	public void setPosPedido(String posPedido) {
		this.posPedido = posPedido;
	}

	public String getNroEntrega() {
		return nroEntrega;
	}

	public void setNroEntrega(String nroEntrega) {
		this.nroEntrega = nroEntrega;
	}

	public String getfCreacionPedido() {
		return fCreacionPedido;
	}

	public void setfCreacionPedido(String fCreacionPedido) {
		this.fCreacionPedido = fCreacionPedido;
	}

	public String getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public String getRemozado_Reparado() {
		return remozado_Reparado;
	}

	public void setRemozado_Reparado(String remozado_Reparado) {
		this.remozado_Reparado = remozado_Reparado;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public String getCodigoParte() {
		return codigoParte;
	}

	public void setCodigoParte(String codigoParte) {
		this.codigoParte = codigoParte;
	}

	public String getNroRequerimiento() {
		return nroRequerimiento;
	}

	public void setNroRequerimiento(String nroRequerimiento) {
		this.nroRequerimiento = nroRequerimiento;
	}

	public String getNroCircuitoDigital() {
		return nroCircuitoDigital;
	}

	public void setNroCircuitoDigital(String nroCircuitoDigital) {
		this.nroCircuitoDigital = nroCircuitoDigital;
	}

	public String getDireccionMac() {
		return direccionMac;
	}

	public void setDireccionMac(String direccionMac) {
		this.direccionMac = direccionMac;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	
	
}
