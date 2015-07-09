package pe.tgestiona.logistica.inversa.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import pe.tgestiona.logistica.inversa.dao.PedidoEntregaDao;

@Repository
public class PedidoEntregaDaoImpl implements PedidoEntregaDao{

	@Autowired
	private DriverManagerDataSource dataSource;
	


}
