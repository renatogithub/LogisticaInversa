package pe.tgestiona.logistica.inversa.service;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.DespachoRRSSBean;

public interface DespachoRRSSService {
//	public List<DespachoRRSSBean> lstDespachoRRSS(String opcion,String material,String serie,int pag);
	//public int lstDespachoRRSS(String opcion,String material,String serie);
	public List<DespachoRRSSBean> lstDespachoRRSS(String opcion,String material,String serie,int pag);
	public void guardarDespachoRRSS(DespachoRRSSBean despachoRRSSBean);
}
