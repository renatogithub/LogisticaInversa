package pe.tgestiona.logistica.inversa.bean;

public class ConstantesGenerales {
	public static final String SAP_6_0 = "6.0"; 
	public static final String SAP_4_7 = "4.7"; 
	public static final int LONGITUD_MATERIAL_SAP_6_0A=10;
	public static final int LONGITUD_MATERIAL_SAP_6_0B=11;
	public static final int LONGITUD_MATERIAL_SAP_4_7A=7;
	public static final int LONGITUD_MATERIAL_SAP_4_7B=8;
	public static final String SIN_GARANTIA_SAP = "0"; 
	public static final String GARANTIA_SAP = "1"; 
	public static final String VACIO = "-----"; 	
	public static final String GUION = "-"; 
	public static final String SI="SI";
	public static final String NO="NO";
	public static final String SINSERIE="SINSERIE";
	public static final int CANTIDAD_COLUMNAS_SAP_SERIADO=12;
	public static final int CANTIDAD_COLUMNAS_SAP_NOSERIADO=6;
	public static final int CANTIDAD_COLUMNAS_FICHA=14;
	public static final int CANTIDAD_COLUMNAS_DISTRIBUCION=22;
	public static final int CANTIDAD_COLUMNAS_PLANEAMIENTO=7;
	public static final int CANTIDAD_SERIES_TEMPRESAS=100;
	public static final int LONGITUD_SERIES_MAX=25;
	public static final int LONGITUD_MATERIAL_MAX=25;
	public static final String DOA="MTDV06";
	public static final String EXCEL_XLSX="xlsx";
	public static final String EXCEL_XLS="xls";
	public static final String TIPO_EQUIPO="EQUIPO";
	public static final String TIPO_SMARTCARD="SMARTCARD";
	public static final String TIPO_DISCODURO="DISCO DURO";
	public static final String RUBRO_TELEFONO="TELEFONO";
	public static final String RUBRO_DECO="DECO";
	public static final String TECNOLOGIA_ESTANDAR="ESTANDAR";
	public static final String TECNOLOGIA_INTERMEDIO="INTERMEDIO";
	public static final String TECNOLOGIA_SMARTCARD="SMARTCARD";
	public static final String TECNOLOGIA_NOAPLICA="NO APLICA";
	public static final String CODIGO_EQ="EQ";
	public static final String CODIGO_RI="RI";
	public static final String NOAPLICA="NOAPLICA";
	
	public static enum IndicadorModoRecojo{
		SI("1"), //Indica si se recojio. 
		NO("0"); //Indica si NO se recojio.
		
		private String tipoValor;
		
		IndicadorModoRecojo(String value){
			tipoValor=value;
		}

		public String getTipoValor() {
			return tipoValor;
		}

		public void setTipoValor(String tipoValor) {
			this.tipoValor = tipoValor;
		}
	
	}
	
	
	public static enum IndicadorCriterios{
		NONE("N"), //No es Sujeto 
		APROBADO("A"), //Aprobado
		PENDIENTE("P"), //Pendiente
		RECHAZADO("R"); //Rechazado
		
		private String tipoValor;
		
		IndicadorCriterios(String value){
			tipoValor=value;
		}

		public String getTipoValor() {
			return tipoValor;
		}

		public void setTipoValor(String tipoValor) {
			this.tipoValor = tipoValor;
		}
	
	}
	
	public static enum ZonalUbicacion{
		PROVINCIA("PROVINCIA"), 
		LIMA("LIMA"); 
		
		private String tipoValor;
		
		ZonalUbicacion(String value){
			tipoValor=value;
		}

		public String getTipoValor() {
			return tipoValor;
		}

		public void setTipoValor(String tipoValor) {
			this.tipoValor = tipoValor;
		}
	
	}
	
	
	
	public static enum RubroPermitidos47{
		DECO("RBM0005"), 
		CABLEMODEM("RBM0002"); 
		
		private String tipoValor;
		
		RubroPermitidos47(String value){
			tipoValor=value;
		}

		public String getTipoValor() {
			return tipoValor;
		}

		public void setTipoValor(String tipoValor) {
			this.tipoValor = tipoValor;
		}
	
	}
	public static enum RecepcionAcopio{
		REPEPCIONADO("RECEPCIONADO"), 
		NOREPEPCIONADO("NO RECEPCIONADO"); 
		
		private String tipoValor;
		
		RecepcionAcopio(String value){
			tipoValor=value;
		}

		public String getTipoValor() {
			return tipoValor;
		}

		public void setTipoValor(String tipoValor) {
			this.tipoValor = tipoValor;
		}
	
	}
		
	public static enum EstadoBusqueda{
		EXISTE("EXISTE"), //Simplemente Existe para cualquier busqueda(Tramos Minimos)
		EXISTEA("EXISTEA"), //Existe pero ya fue Aprobado(Tramos Minimos)
		EXISTEP("EXISTEP"), //Existe pero es Pendiente(Tramos Minimos)
		NOEXISTE("NOEXISTE"),
		NOCUMPLE("NOCUMPLE"),
		NOSAP("NOSAP");
		
		private String tipoValor;
		
		EstadoBusqueda(String value){
			tipoValor=value;
		}

		public String getTipoValor() {
			return tipoValor;
		}

		public void setTipoValor(String tipoValor) {
			this.tipoValor = tipoValor;
		}
	
	}
	
	
	public static enum RubroMaterial{
		CABLE("CABLE"),
		RETACERIA("RETACERIA");
		
		private String tipoValor;
		
		RubroMaterial(String value){
			tipoValor=value;
		}

		public String getTipoValor() {
			return tipoValor;
		}

		public void setTipoValor(String tipoValor) {
			this.tipoValor = tipoValor;
		}
	
	}
	
	
	public static enum MotivoDevolucionLiquidacion{
		AVERIAS("MTDV02"),
		BAJAS("MTDV03"),
		DOAS("MTDV06"),
		SINSERIE("MTDV21"),
		RESIDUOSOPERATIVOS("MTDV19"),
		SOBRESTOCK("MTDV20"),
		ANULACIONVENTA("MTDV01");
		
		private String tipoValor;
		
		MotivoDevolucionLiquidacion(String value){
			tipoValor=value;
		}

		public String getTipoValor() {
			return tipoValor;
		}

		public void setTipoValor(String tipoValor) {
			this.tipoValor = tipoValor;
		}
	
	}
	
	public static enum TipoFormatoDevolucion{
		OTROS("FRT0001"),
		SERIADOS("FRT0002"),
		NOSERIADOS("FRT0003");
		
		private String tipoValor;
		
		TipoFormatoDevolucion(String value){
			tipoValor=value;
		}

		public String getTipoValor() {
			return tipoValor;
		}

		public void setTipoValor(String tipoValor) {
			this.tipoValor = tipoValor;
		}
	
	
	}
	
	
	public static enum TipoBodega{
		PRIMARIO("TPBD0001"),
		SECUNDARIO("TPBD0002"),
		LIQUIDACION("TPBD0003"),
		LIQUIDADO("TPBD0004"),
		TRANSITO("TPBD0005");
		
		private String tipoValor;
		
		TipoBodega(String value){
			tipoValor=value;
		}

		public String getTipoValor() {
			return tipoValor;
		}

		public void setTipoValor(String tipoValor) {
			this.tipoValor = tipoValor;
		}
	
	
	}
	
	public static enum TipoFechaGarantia{
		FECHACORREO("FECHASOLICITUD"),
		FECHADEVOLUCION("FECHADEVOLUCION");
		
		private String tipoValor;
		
		TipoFechaGarantia(String value){
			tipoValor=value;
		}

		public String getTipoValor() {
			return tipoValor;
		}

		public void setTipoValor(String tipoValor) {
			this.tipoValor = tipoValor;
		}
		
		
	}
	

	public static enum EstadoPedidoActa{
		GENEROPEDIDO("SE HA GENERADO NRO PEDIDO"),
		NOGENEROPEDIDO("NO SE HA GENERADO NRO PEDIDO"),
		EXISTEPEDIDO("1"), //Indica si existe una configuracion de Pedido
		NOEXISTEPEDIDO("0"), //Indica si no existe una configuracion de Pedido
		CONPEDIDO("1"), //Indica si hemos creado un pedido (esto aplica solo para las configuraciones de Pedido existentes)
		SINPEDIDO("0"); //Indica si aun no hemos creado un pedido (esto aplica solo para las configuraciones de Pedido existentes)
		
		private String tipoValor;
		
		EstadoPedidoActa(String value) {
			tipoValor = value; 
		}

		public String getTipoValor() {
			return tipoValor;
		}

		public void setTipoValor(String tipoValor) {
			this.tipoValor = tipoValor;
		}
		
	}
	
	
	
	public static enum BaseGarantia{
		RESPONSABILIDAD_TELEFONICA("RESPONSABILIDAD DE TELEFONICA"),									
		RESPONSABILIDAD_PUNTO("RESPONSABILIDAD DEL PUNTO");
		
		private String tipoValor;
		
		BaseGarantia(String value) {
			tipoValor = value; 
		}

		public String getTipoValor() {
			return tipoValor;
		}

		public void setTipoValor(String tipoValor) {
			this.tipoValor = tipoValor;
		}
		
	}
	
	public static enum TipoErroresFicha{
		CAMPOSVACIOS("ERR01"),
		SERIESMATERIAL("ERR02"),
		CARACTERESSERIE("ERR03"),
		CONTENIDO("ERR04"),
		CORRESPONDENCIA("ERR05"),
		CANTIDADCAMPOS("ERR06"),
		CANTIDADSERIES("ERR07"),
		LONGITUDSERIES("ERR08");
		
		private String tipoValor;
		
		TipoErroresFicha(String value) {
			tipoValor = value; 
		}

		public String getTipoValor() {
			return tipoValor;
		}

		public void setTipoValor(String tipoValor) {
			this.tipoValor = tipoValor;
		}
		
	}
	
	public static enum TipoErroresSAP{
		CANTIDADCAMPOS("ERR01"),
		LONGITUDCAMPOMATERIAL("ERR02");
		
		private String tipoValor;
		
		TipoErroresSAP(String value) {
			tipoValor = value; 
		}

		public String getTipoValor() {
			return tipoValor;
		}

		public void setTipoValor(String tipoValor) {
			this.tipoValor = tipoValor;
		}
		
	}
	
	public static enum PerfilUsuario{
		ADMINISTRADOR("ADM"),
		TRANSACCIONES("OPERTR"),
		DISTRIBUCIONFISICA("OPERDF"),
		PLANEAMIENTO("OPERPL"),
		ACOPIO("OPERAC"),
		CONSULTA("OPERCS");
		
		private String tipoValor;
		
		PerfilUsuario(String value) {
			tipoValor = value; 
		}

		public String getTipoValor() {
			return tipoValor;
		}

		public void setTipoValor(String tipoValor) {
			this.tipoValor = tipoValor;
		}
		
		
		
	}
	
	public static enum Enviado {
		DISTRIBUCION("DISTRIBUCION"),
		PLANEAMIENTO("PLANEAMIENTO"),
		RROO("RR.OO");
		
		private String tipoValor;
		
		Enviado(String value) {
			tipoValor = value; 
		}
		public String getTipoValor() {
			if (tipoValor == null) {
				tipoValor = "";
			}
			return tipoValor;
		}
		
	}
	
	
	public static enum Destino {
		VENEZUELA("DISTRIBUCION"),
		CENTROACOPIO("C.A"),
		OPRROO("OP.RR.OO");
		
		private String tipoValor;
		
		Destino(String value) {
			tipoValor = value; 
		}
		public String getTipoValor() {
			if (tipoValor == null) {
				tipoValor = "";
			}
			return tipoValor;
		}
		
	}
	
	public static enum TipoConfiguracion {
		BODEGA("CONF01"),
		LOTE("CONF02"),
		GARANTIAPROVEEDORES("CONF03"),
		GARANTIATALLER("CONF04"),
		EXISTENCIASAP("CONF05"),
		MODORECOJO("CONF06"),
		CIERREPROCESO("CONF07"),
		PEDIDOENTREGA("CONF08");
		
		private String tipoValor;
		
		TipoConfiguracion(String value) {
			tipoValor = value; 
		}
		public String getTipoValor() {
			if (tipoValor == null) {
				tipoValor = "";
			}
			return tipoValor;
		}
		
	}
	
	public static enum EstadoTaller{
		REPARADO("REPARADO"),
		REMOZADO("REMOZADO");
		
		private String tipoValor;
		
		EstadoTaller(String value) {
			tipoValor = value; 
		}
		public String getTipoValor() {
			if (tipoValor == null) {
				tipoValor = "";
			}
			return tipoValor;
		}
		
	}
	
	public static enum ModoRecojo {		
		NIVELCANTIDAD("NIVELCANTIDAD",	"VALIDAR A NIVEL DE CANTIDAD"),
		NIVELSERIE("NIVELSERIE", "VALIDAR A NIVEL DE SERIE"),
		NONE("", "");		
		private String criterio;
		private String descripcion;
		
		ModoRecojo(String value, String desc) {
			criterio = value;
			descripcion = desc;
		}
		public String getCriterio() {
			if (criterio == null) {
				criterio = "";
			}
			return criterio;
		}		
		public String getDescripcion() {
			if (descripcion == null) {
				descripcion = "";
			}
			return descripcion;
		}
		public static ModoRecojo getCriterioModoRecojo(String value) {
			for (ModoRecojo  estado : ModoRecojo.values()) {
				if (estado.getCriterio().equals(value)) {
					return estado;
				}
			}
			return ModoRecojo.NONE;			
		}		
	}
	

	
	public static enum EstadoValidacionActa{
		APROBADO("APROBADO"),
		RECHAZADO("RECHAZADO"),
		PENDIENTE("PENDIENTE"),
		PROCESADO("PROCESADO"),
		SINPROCESAR("SIN PROCESAR"),
		ANULADO("ANULADO"),
		SINVALIDAR("");		
		
		private String tipoValor;
		
		EstadoValidacionActa(String value) {
			tipoValor = value; 
		}
		public String getTipoValor() {
			if (tipoValor == null) {
				tipoValor = "";
			}
			return tipoValor;
		}
		
	}
	
	
	public static enum EstadoValidacionGarantia{
		SIPROVEEDOR("SI PROVEEDOR"),
		SITALLER("SI TALLER"),
		NOAPLICA("NO APLICA"),
		NO("NO");
		
		private String tipoValor;
		
		EstadoValidacionGarantia(String value) {
			tipoValor = value; 
		}
		public String getTipoValor() {
			if (tipoValor == null) {
				tipoValor = "";
			}
			return tipoValor;
		}
		
	}
	
	public static enum ExistenciaSAP{
		EXISTESAP("Serie existe en SAP"),
		NOEXISTESAP("Serie No existe en SAP");
		
		private String tipoValor;
		
		ExistenciaSAP(String value) {
			tipoValor = value; 
		}
		public String getTipoValor() {
			if (tipoValor == null) {
				tipoValor = "";
			}
			return tipoValor;
		}
		
	}
	
	public static enum ExistenciaValorConfiguracion{
		EXISTE("1"),
		NOEXISTE("2"),
		NONE("0");
		
		private String tipoValor;
		
		ExistenciaValorConfiguracion(String value) {
			tipoValor = value; 
		}
		public String getTipoValor() {
			if (tipoValor == null) {
				tipoValor = "";
			}
			return tipoValor;
		}
		
	}
	
	
	
}
