package pe.tgestiona.logistica.inversa.service;

import java.io.InputStream;
import java.util.List;

import pe.tgestiona.logistica.inversa.bean.ActaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.DetalleFichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.FichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.PedidoEntregaBean;

public interface ActaDevolucionService {
	public List<FichaDevolucionBean> obtenerFichasFuenteActas(List<FichaDevolucionBean> listaFicha);
	public String generaCodigoActaDevolucion(String prefijo);
	public String obtenerRubroFichaDevolucion(String nroTicket);
	public void generarActaDevolucion(List<FichaDevolucionBean> lista);
	public void generarDetalleActaDevolucion(List<DetalleFichaDevolucionBean> lista,String nroActa);
	public List<DetalleFichaDevolucionBean> lstDetalleAprobado(String nroTicket);
	public List<ActaDevolucionBean> lstActaDevolucion(String formato);
	public List<ActaDevolucionBean> lstActaDevolucionFechas(String formato,String f1,String f2);
	public List<ActaDevolucionBean> lstActaDevolucionPedidoPendiente(String formato);	
	public List<ActaDevolucionBean> lstActaDevolucionRecojoPendiente(String formato);
	public List<PedidoEntregaBean> leerArchivoPedidoEntrega(InputStream file);
	public void asignarPedidoEntregaSeriado(List<PedidoEntregaBean> lstPedidoEntrega);
	public void asignarPedidoEntregaNoSeriado(List<PedidoEntregaBean> lstPedidoEntrega);
	public void asignarFechaRecojo(String nroTickets,String fecha);
	public List<String[]> lstActasGeneradas(String acta,String nroTicket,String material,String serie,String descrip,String estado,String f1,String f2,String estadoActas,String pag);
	public byte[] descargaFuentePedido(String nroActa,String tipoFormato);
	public byte[] lstActasGeneradasExcel(String acta,String nroTicket,String material,String serie,String descrip,String estado,String pag);
}
