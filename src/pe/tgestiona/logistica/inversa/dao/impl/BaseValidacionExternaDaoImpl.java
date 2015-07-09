package pe.tgestiona.logistica.inversa.dao.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import pe.tgestiona.logistica.inversa.bean.BaseValidacionExternaBean;
import pe.tgestiona.logistica.inversa.bean.ConstantesGenerales;
import pe.tgestiona.logistica.inversa.dao.BaseValidacionExternaDao;

@Repository
public class BaseValidacionExternaDaoImpl implements BaseValidacionExternaDao {

	@Autowired
	private DriverManagerDataSource dataSource;
	
	@Override
	public List<BaseValidacionExternaBean> leerarchivoExcelValidacionExterna(InputStream file) {
		List<BaseValidacionExternaBean> listValidacionExterna = null;
		
		try {
			Workbook workbook = Workbook.getWorkbook(file); //Pasamos el excel que vamos a leer
			Sheet sheet = workbook.getSheet(0); //Seleccionamos la hoja que vamos a leer
			String dato;
			listValidacionExterna = new ArrayList<BaseValidacionExternaBean>();
			
			for (int fila = 1; fila < sheet.getRows(); fila++) { //recorremos las filas
				BaseValidacionExternaBean baseValidacionExternaBean=new BaseValidacionExternaBean();
				for (int columna = 0; columna < sheet.getColumns(); columna++) { //recorremos las columnas
					
					dato = sheet.getCell(columna, fila).getContents(); //setear la celda leida a nombre
					if(dato==null){
						dato="";
					}
					switch(columna){
						case 0:	baseValidacionExternaBean.setNroTicket(dato);
				    			break;
					    case 1:	baseValidacionExternaBean.setMaterial(dato);
					    		break;
					    case 3: baseValidacionExternaBean.setSerie(dato);
					    		break;
						case 4: if(dato.trim().equals("")){
									baseValidacionExternaBean.setEstado(ConstantesGenerales.EstadoValidacionActa.PENDIENTE.getTipoValor());
								}else{
									baseValidacionExternaBean.setEstado(dato);
								}
								break;
						case 5: if(dato.trim().equals("")){
									baseValidacionExternaBean.setMotivo(ConstantesGenerales.GUION);
								}else{
									baseValidacionExternaBean.setMotivo(dato);
								}
								break;
								
					}
					
				}
				listValidacionExterna.add(baseValidacionExternaBean);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		}
			
		return listValidacionExterna;
	}

	@Override
	public void grabarBaseValidacionExterna(List<BaseValidacionExternaBean> lista, String usuario) {
		String sql = "";
		int ctos = 0;

		Connection cn = null;
		try {
			cn = dataSource.getConnection();
			Statement st = cn.createStatement();
				for (int i = 0; i < lista.size(); i++) {
					if(lista.get(i).getEstado().equals(ConstantesGenerales.EstadoValidacionActa.APROBADO.getTipoValor()) ||
					   lista.get(i).getEstado().equals(ConstantesGenerales.EstadoValidacionActa.RECHAZADO.getTipoValor())){
						
					
					sql = "INSERT INTO BASEVALIDACIONEXTERNA(NROTICKET,CODMATERIAL,SERIE,ESTADO,MOTIVO,USUARIO) VALUES ('"
						   +  lista.get(i).getNroTicket() + "','" + lista.get(i).getMaterial() + "','" + lista.get(i).getSerie() + "','" 
						   +  lista.get(i).getEstado() + "','" + lista.get(i).getMotivo() + "','" + usuario + "')";
					
					ctos = st.executeUpdate(sql);
					if (ctos == 0) {
						System.out.println("0 filas afectadas");
					}
				}
			}		
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
	}

	@Override
	public void eliminaDatosBaseValidacionExterna() {
		String sql="TRUNCATE TABLE BASEVALIDACIONEXTERNA";
		Connection cn = null;
		PreparedStatement pstmt = null;
		try {
			cn=dataSource.getConnection();
			cn.setAutoCommit(false);
			pstmt = cn.prepareStatement(sql);
			pstmt.executeUpdate();
			cn.commit();
			
		} catch (SQLException e) {
			e.getMessage();
		} finally{
			try{
				cn.close();
			}catch(Exception e){
				e.getMessage();
			}
		}		
	}

}
