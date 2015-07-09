package pe.tgestiona.logistica.inversa.service;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.MotivoDevolucionBean;

public interface MotivoDevolucionService {
	public List<MotivoDevolucionBean> listaMotivos(String formato);
	public List<MotivoDevolucionBean> listaMotivos();
	public MotivoDevolucionBean obtenerMotivo(String codigo);
}
