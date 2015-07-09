package pe.tgestiona.logistica.inversa.service.impl;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.ActaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.DetalleFichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.FichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.PedidoEntregaBean;
import pe.tgestiona.logistica.inversa.dao.ActaDevolucionDao;
import pe.tgestiona.logistica.inversa.service.ActaDevolucionService;

@Service
public class ActaDevolucionServiceImpl implements ActaDevolucionService{

	@Autowired
	private ActaDevolucionDao actaDevolucionDao;
	
	@Override
	public void generarActaDevolucion(List<FichaDevolucionBean> lista){		
		actaDevolucionDao.generarActaDevolucion(lista);
	}

	@Override
	public String generaCodigoActaDevolucion(String prefijo) {
		return actaDevolucionDao.generaCodigoActaDevolucion(prefijo);
	}

	@Override
	public List<PedidoEntregaBean> leerArchivoPedidoEntrega(InputStream file) {
		return actaDevolucionDao.leerArchivoPedidoEntrega(file);
	}

	@Override
	public void asignarPedidoEntregaSeriado(List<PedidoEntregaBean> lstPedidoEntrega) {
		actaDevolucionDao.asignarPedidoEntregaSeriado(lstPedidoEntrega);		
	}

	@Override
	public void generarDetalleActaDevolucion(List<DetalleFichaDevolucionBean> lista, String nroActa) {
		actaDevolucionDao.generarDetalleActaDevolucion(lista, nroActa);
	}

	@Override
	public List<DetalleFichaDevolucionBean> lstDetalleAprobado(String nroTicket) {
		return actaDevolucionDao.lstDetalleAprobado(nroTicket);
	}

	@Override
	public List<FichaDevolucionBean> obtenerFichasFuenteActas(List<FichaDevolucionBean> listaFicha) {
		return actaDevolucionDao.obtenerFichasFuenteActas(listaFicha);
	}

	
	@Override
	public List<ActaDevolucionBean> lstActaDevolucion(String formato) {
		return actaDevolucionDao.lstActaDevolucion(formato);
	}

	@Override
	public List<ActaDevolucionBean> lstActaDevolucionPedidoPendiente(String formato) {
		return actaDevolucionDao.lstActaDevolucionPedidoPendiente(formato);
	}

	@Override
	public List<ActaDevolucionBean> lstActaDevolucionFechas(String formato,String f1, String f2) {
		return actaDevolucionDao.lstActaDevolucionFechas(formato, f1, f2);
	}

	@Override
	public byte[] lstActasGeneradasExcel(String acta, String nroTicket,String material, String serie, String descrip, String estado,String pag) {
		return actaDevolucionDao.lstActasGeneradasExcel(acta, nroTicket, material, serie, descrip, estado, pag);
	}

	@Override
	public String obtenerRubroFichaDevolucion(String nroTicket) {
		return actaDevolucionDao.obtenerRubroFichaDevolucion(nroTicket);
	}

	@Override
	public List<String[]> lstActasGeneradas(String acta, String nroTicket,
			String material, String serie, String descrip, String estado,
			String f1, String f2, String estadoActas, String pag) {
		return actaDevolucionDao.lstActasGeneradas(acta, nroTicket, material, serie, descrip, estado, f1, f2, estadoActas, pag);
	}

	@Override
	public byte[] descargaFuentePedido(String nroActa, String tipoFormato) {
		return actaDevolucionDao.descargaFuentePedido(nroActa, tipoFormato);
	}

	@Override
	public void asignarFechaRecojo(String nroTickets, String fecha) {
		actaDevolucionDao.asignarFechaRecojo(nroTickets, fecha);		
	}

	@Override
	public List<ActaDevolucionBean> lstActaDevolucionRecojoPendiente(String formato) {
		return actaDevolucionDao.lstActaDevolucionRecojoPendiente(formato);
	}

	@Override
	public void asignarPedidoEntregaNoSeriado(List<PedidoEntregaBean> lstPedidoEntrega) {
		actaDevolucionDao.asignarPedidoEntregaNoSeriado(lstPedidoEntrega);
		
	}

}
