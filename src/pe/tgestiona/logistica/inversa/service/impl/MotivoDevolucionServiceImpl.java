package pe.tgestiona.logistica.inversa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.MotivoDevolucionBean;
import pe.tgestiona.logistica.inversa.dao.MotivoDevolucionDao;
import pe.tgestiona.logistica.inversa.service.MotivoDevolucionService;

@Service
public class MotivoDevolucionServiceImpl implements MotivoDevolucionService {

	@Autowired
	private MotivoDevolucionDao motivoDevolucionDao;
	
	
	@Override
	public List<MotivoDevolucionBean> listaMotivos(String formato) {
		return motivoDevolucionDao.listaMotivos(formato);
	}

	@Override
	public List<MotivoDevolucionBean> listaMotivos() {
		return motivoDevolucionDao.listaMotivos();
	}

	@Override
	public MotivoDevolucionBean obtenerMotivo(String codigo) {
		return motivoDevolucionDao.obtenerMotivo(codigo);
	}

}
