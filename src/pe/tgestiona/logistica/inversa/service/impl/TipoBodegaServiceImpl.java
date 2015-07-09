package pe.tgestiona.logistica.inversa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.TipoBodegaBean;
import pe.tgestiona.logistica.inversa.dao.TipoBodegaDao;
import pe.tgestiona.logistica.inversa.service.TipoBodegaService;

@Service
public class TipoBodegaServiceImpl implements TipoBodegaService{

	@Autowired
	private TipoBodegaDao tipoBodegaDao;
	
	@Override
	public List<TipoBodegaBean> lstTipoBodegas() {
		return tipoBodegaDao.lstTipoBodegas();
	}

}
