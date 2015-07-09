package pe.tgestiona.logistica.inversa.dao;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.FichaDevolucionBean;

public interface FichaDevolucionDao {
	public String grabarCabeceraFichaDevolucion(int cantReg,FichaDevolucionBean fichaDevolucionBean);
	public List<FichaDevolucionBean> listTicketSinProcesar(String formato,String usuario);
	public List<FichaDevolucionBean> listTicketSinProcesarFechas(String formato,String f1,String f2,String usuario);
	public List<FichaDevolucionBean> listTicketPendiente(String formato,String usuario);
	public List<FichaDevolucionBean> listTicketPendienteFechas(String formato,String f1,String f2,String usuario);
	public List<FichaDevolucionBean> listFichaDevolucion(String[] tickets);
	public void anularFichaDevolucion(String ticket,String observacion);
	public void actualizarFichaDevolucion(List<FichaDevolucionBean> lista);
	public byte[] descargarSeries(String ticket);
	public byte[] descargarMaterial(String ticket);
	public byte[] descargarPlantillaxls();
	public FichaDevolucionBean obtenerFichaDevolucionAnular(String nroTicket);
}
