package pe.tgestiona.logistica.inversa.dao;

import java.util.List;

import pe.tgestiona.logistica.inversa.bean.DespachoRRSSBean;

public interface DespachoRRSSDao {
	public List<DespachoRRSSBean> lstDespachoRRSS(String opcion,String material,String serie,int pag);
	public int cantRegistros(String opcion,String material,String serie);	
	public void guardarDespachoRRSS(DespachoRRSSBean despachoRRSSBean);
}
