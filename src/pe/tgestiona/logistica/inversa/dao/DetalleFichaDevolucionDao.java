package pe.tgestiona.logistica.inversa.dao;

import java.io.InputStream;
import java.util.List;

import pe.tgestiona.logistica.inversa.bean.DetalleFichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.ErroresBean;
import pe.tgestiona.logistica.inversa.bean.FichaDevolucionBean;

public interface DetalleFichaDevolucionDao {
	//Proceso Registro
	public void grabarDetalleFichaDevolucion(String nroTicket,List<DetalleFichaDevolucionBean> lista,FichaDevolucionBean fichaDevolucionBean);
	public List<String> validarCantidadCampos(List<DetalleFichaDevolucionBean> lista);
	public List<String> validarCamposVacios(List<DetalleFichaDevolucionBean> lista);
	public List<String> validarSeriesCaracteres(List<DetalleFichaDevolucionBean> lista);
	public List<String> validarContenido(List<DetalleFichaDevolucionBean> lista);
	public List<String> validarCorrespondencia(List<DetalleFichaDevolucionBean> lista);
	
	public List<String> validarCantidadSeries(List<DetalleFichaDevolucionBean> lista);
	public List<String> validarLongitudSeries(List<DetalleFichaDevolucionBean> lista);
	public List<DetalleFichaDevolucionBean> leerarchivoExcel(InputStream file);
	public List<ErroresBean> obtenerTiposErroresFicha(List<DetalleFichaDevolucionBean> lista,String formato);
	//Proceso Validacion
	public void actualizarDetalleFichaDevolucion(List<DetalleFichaDevolucionBean> lista,String estadoValidacion,String motivo);
	public void actualizarDetalleFichaDevolucionDevoluci(List<FichaDevolucionBean> lista);
	public void actualizarDetallexStatusxAcopio(List<DetalleFichaDevolucionBean> listaDeta,List<FichaDevolucionBean> listaFicha);
	public byte[] descargaExcel(String nroTicket);
	public byte[] descargaSeriesPendiente(String nroTicket);
	public byte[] descargaSeriesPendienteGarantia(String nroTicket);
	public byte[] descargaSeriesPendienteValidacionExterna(String nroTicket);
	public List<String> obtenerResumenValidacion(String[] tickets);
	public List<String> obtenerResumenValidacionMsg(String ticket);
	

}
