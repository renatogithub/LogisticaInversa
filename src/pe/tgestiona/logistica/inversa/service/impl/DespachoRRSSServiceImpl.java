package pe.tgestiona.logistica.inversa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.tgestiona.logistica.inversa.bean.DespachoRRSSBean;
import pe.tgestiona.logistica.inversa.dao.DespachoRRSSDao;
import pe.tgestiona.logistica.inversa.service.DespachoRRSSService;

@Service
public class DespachoRRSSServiceImpl implements DespachoRRSSService{

	@Autowired
	private DespachoRRSSDao despachoRRSSDao;



	@Override
	public void guardarDespachoRRSS(DespachoRRSSBean despachoRRSSBean) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<DespachoRRSSBean> lstDespachoRRSS(String opcion,String material, String serie, int pag) {
		return despachoRRSSDao.lstDespachoRRSS(opcion, material, serie, pag);
	}


}
