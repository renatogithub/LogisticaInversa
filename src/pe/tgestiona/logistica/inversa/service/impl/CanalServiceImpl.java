package pe.tgestiona.logistica.inversa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.CanalBean;
import pe.tgestiona.logistica.inversa.dao.CanalDao;
import pe.tgestiona.logistica.inversa.service.CanalService;

@Service
public class CanalServiceImpl implements CanalService{

	@Autowired
	private CanalDao canalDao;
	
	@Override
	public List<CanalBean> listaCanales() {
		return canalDao.listaCanales();
	}

}
