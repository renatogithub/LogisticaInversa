package pe.tgestiona.logistica.inversa.dao;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.MotivoDevolucionBean;

public interface MotivoDevolucionDao {
	public List<MotivoDevolucionBean> listaMotivos(String formato);
	public List<MotivoDevolucionBean> listaMotivos();
	public MotivoDevolucionBean obtenerMotivo(String codigo);
}
