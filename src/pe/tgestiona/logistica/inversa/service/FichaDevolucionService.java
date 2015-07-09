package pe.tgestiona.logistica.inversa.service;

import java.io.InputStream;
import java.util.List;

import pe.tgestiona.logistica.inversa.bean.DetalleFichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.ErroresBean;
import pe.tgestiona.logistica.inversa.bean.FichaDevolucionBean;

public interface FichaDevolucionService {
	
	//Ficha Devolucion
	public String grabarCabeceraFichaDevolucion(int cantReg,FichaDevolucionBean fichaDevolucionBean);
	public List<FichaDevolucionBean> listTicketSinProcesar(String formato,String usuario);
	public List<FichaDevolucionBean> listTicketSinProcesarFechas(String formato,String f1,String f2,String usuario);
	public List<FichaDevolucionBean> listTicketPendiente(String formato,String usuario);
	public List<FichaDevolucionBean> listTicketPendienteFechas(String formato,String f1,String f2,String usuario);

/*	public List<FichaDevolucionBean> listTicketSinProcesar(String formato);
	public List<FichaDevolucionBean> listTicketSinProcesarFechas(String formato,String f1,String f2);
	public List<FichaDevolucionBean> listTicketPendiente(String formato);
	public List<FichaDevolucionBean> listTicketPendienteFechas(String formato,String f1,String f2);
*/	public List<FichaDevolucionBean> listFichaDevolucion(String[] tickets);
	public void anularFichaDevolucion(String ticket,String observacion);
	public void actualizarFichaDevolucion(List<FichaDevolucionBean> lista);
	public byte[] descargarSeries(String ticket);
	public byte[] descargarMaterial(String ticket);
	public byte[] descargarPlantillaxls();
	public FichaDevolucionBean obtenerFichaDevolucionAnular(String nroTicket);

	
	//Detalle Ficha Devolucion
	public void grabarDetalleFichaDevolucion(String nroTicket,List<DetalleFichaDevolucionBean> lista,FichaDevolucionBean fichaDevolucionBean);
	public void actualizarDetalleFichaDevolucion(List<DetalleFichaDevolucionBean> lista,String estadoValidacion,String motivo);
	public void actualizarDetalleFichaDevolucionDevoluci(List<FichaDevolucionBean> lista);	
	public void actualizarDetallexStatusxAcopio(List<DetalleFichaDevolucionBean> listaDeta,List<FichaDevolucionBean> listaFicha);
	public byte[] descargaExcel(String nroTicket);
	public byte[] descargaSeriesPendiente(String nroTicket);
	public byte[] descargaSeriesPendienteGarantia(String nroTicket);
	public byte[] descargaSeriesPendienteValidacionExterna(String nroTicket);
	public List<DetalleFichaDevolucionBean> leerarchivoExcel(InputStream file);
	public List<ErroresBean> obtenerTiposErroresFicha(List<DetalleFichaDevolucionBean> lista,String formato);	
	public List<String> obtenerResumenValidacion(String[] tickets);	
	public List<String> obtenerResumenValidacionMsg(String ticket);
	public List<String> validarCamposVacios(List<DetalleFichaDevolucionBean> lista);
	public List<String> validarSeriesCaracteres(List<DetalleFichaDevolucionBean> lista);
	public List<String> validarContenido(List<DetalleFichaDevolucionBean> lista);
	public List<String> validarCorrespondencia(List<DetalleFichaDevolucionBean> lista);
	public List<String> validarCantidadCampos(List<DetalleFichaDevolucionBean> lista);
}
