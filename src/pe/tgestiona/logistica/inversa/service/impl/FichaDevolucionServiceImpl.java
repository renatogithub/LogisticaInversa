package pe.tgestiona.logistica.inversa.service.impl;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.DetalleFichaDevolucionBean;
import pe.tgestiona.logistica.inversa.bean.ErroresBean;
import pe.tgestiona.logistica.inversa.bean.FichaDevolucionBean;
import pe.tgestiona.logistica.inversa.dao.DetalleFichaDevolucionDao;
import pe.tgestiona.logistica.inversa.dao.FichaDevolucionDao;
import pe.tgestiona.logistica.inversa.service.FichaDevolucionService;

@Service
public class FichaDevolucionServiceImpl implements FichaDevolucionService{

	@Autowired
	private FichaDevolucionDao fichaDevolucionDao;
	@Autowired
	private DetalleFichaDevolucionDao detalleFichaDevolucionDao;
	
	@Override
	public String grabarCabeceraFichaDevolucion(int cantReg,FichaDevolucionBean fichaDevolucionBean) {
		return fichaDevolucionDao.grabarCabeceraFichaDevolucion(cantReg, fichaDevolucionBean);
	}
	

	@Override
	public List<FichaDevolucionBean> listFichaDevolucion(String[] tickets) {
		return fichaDevolucionDao.listFichaDevolucion(tickets);
	}
	
	@Override
	public void anularFichaDevolucion(String ticket, String observacion) {
		fichaDevolucionDao.anularFichaDevolucion(ticket, observacion);
	}


	@Override
	public byte[] descargarSeries(String ticket) {
		return fichaDevolucionDao.descargarSeries(ticket);
	}

	@Override
	public FichaDevolucionBean obtenerFichaDevolucionAnular(String nroTicket) {
		return fichaDevolucionDao.obtenerFichaDevolucionAnular(nroTicket);
	}

	
	@Override
	public void grabarDetalleFichaDevolucion(String nroTicket,List<DetalleFichaDevolucionBean> lista,FichaDevolucionBean fichaDevolucionBean) {
		detalleFichaDevolucionDao.grabarDetalleFichaDevolucion(nroTicket, lista, fichaDevolucionBean);
	}

	@Override
	public List<String> validarCamposVacios(List<DetalleFichaDevolucionBean> lista) {
		return detalleFichaDevolucionDao.validarCamposVacios(lista);
	}




	@Override
	public List<String> validarSeriesCaracteres(List<DetalleFichaDevolucionBean> lista) {
		return detalleFichaDevolucionDao.validarSeriesCaracteres(lista);
	}


	@Override
	public List<String> validarContenido(List<DetalleFichaDevolucionBean> lista) {
		return detalleFichaDevolucionDao.validarContenido(lista);
	}


	@Override
	public List<String> validarCorrespondencia(List<DetalleFichaDevolucionBean> lista) {
		return detalleFichaDevolucionDao.validarCorrespondencia(lista);
	}


	@Override
	public byte[] descargaExcel(String nroTicket) {
		return detalleFichaDevolucionDao.descargaExcel(nroTicket);
	}



	@Override
	public List<DetalleFichaDevolucionBean> leerarchivoExcel(InputStream file) {
		return detalleFichaDevolucionDao.leerarchivoExcel(file);
	}


	@Override
	public byte[] descargaSeriesPendiente(String nroTicket) {
		return detalleFichaDevolucionDao.descargaSeriesPendiente(nroTicket);
	}


	@Override
	public byte[] descargaSeriesPendienteGarantia(String nroTicket) {
		return detalleFichaDevolucionDao.descargaSeriesPendienteGarantia(nroTicket);
	}

	@Override
	public List<String> obtenerResumenValidacion(String[] tickets) {
		return detalleFichaDevolucionDao.obtenerResumenValidacion(tickets);
	}

	@Override
	public void actualizarDetalleFichaDevolucionDevoluci(List<FichaDevolucionBean> lista) {
		detalleFichaDevolucionDao.actualizarDetalleFichaDevolucionDevoluci(lista);
	}

	@Override
	public List<String> validarCantidadCampos(List<DetalleFichaDevolucionBean> lista) {
		return detalleFichaDevolucionDao.validarCantidadCampos(lista);
	}

	@Override
	public byte[] descargaSeriesPendienteValidacionExterna(String nroTicket) {
		return detalleFichaDevolucionDao.descargaSeriesPendienteValidacionExterna(nroTicket);
	}


	@Override
	public void actualizarDetalleFichaDevolucion(List<DetalleFichaDevolucionBean> lista, String estadoValidacion,String motivo) {
		detalleFichaDevolucionDao.actualizarDetalleFichaDevolucion(lista, estadoValidacion, motivo);
	}

	@Override
	public void actualizarFichaDevolucion(List<FichaDevolucionBean> lista) {
		fichaDevolucionDao.actualizarFichaDevolucion(lista);
		
	}

	@Override
	public void actualizarDetallexStatusxAcopio(List<DetalleFichaDevolucionBean> listaDeta,List<FichaDevolucionBean> listaFicha) {
		detalleFichaDevolucionDao.actualizarDetallexStatusxAcopio(listaDeta, listaFicha);
	}

	@Override
	public List<ErroresBean> obtenerTiposErroresFicha(List<DetalleFichaDevolucionBean> lista, String formato) {
		return detalleFichaDevolucionDao.obtenerTiposErroresFicha(lista, formato);
	}

	@Override
	public List<String> obtenerResumenValidacionMsg(String ticket) {
		return detalleFichaDevolucionDao.obtenerResumenValidacionMsg(ticket);
	}

	@Override
	public byte[] descargarPlantillaxls() {
		return fichaDevolucionDao.descargarPlantillaxls();
	}

	@Override
	public byte[] descargarMaterial(String ticket) {
		return fichaDevolucionDao.descargarMaterial(ticket);
	}


	@Override
	public List<FichaDevolucionBean> listTicketSinProcesar(String formato,String usuario) {
		return fichaDevolucionDao.listTicketSinProcesar(formato, usuario);
	}


	@Override
	public List<FichaDevolucionBean> listTicketSinProcesarFechas(String formato, String f1, String f2, String usuario) {
		return fichaDevolucionDao.listTicketSinProcesarFechas(formato, f1, f2, usuario);
	}


	@Override
	public List<FichaDevolucionBean> listTicketPendiente(String formato,String usuario) {
		return fichaDevolucionDao.listTicketPendiente(formato, usuario);
	}


	@Override
	public List<FichaDevolucionBean> listTicketPendienteFechas(String formato,String f1, String f2, String usuario) {
		return fichaDevolucionDao.listTicketPendienteFechas(formato, f1, f2, usuario);
	}

}
