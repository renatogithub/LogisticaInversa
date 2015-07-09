package pe.tgestiona.logistica.inversa.test;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pe.tgestiona.logistica.inversa.bean.ConstantesGenerales;
import pe.tgestiona.logistica.inversa.util.Util;


public class Test {


	public static void main(String[] args) throws IOException, ParseException {
		
		
/*		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = upload.parseRequest(req);
		FileItem item = items.get(0);
		
		//Obtengo datos del archivo enviado
		System.out.println("Nombre del archivo:\t"+item.getName());
		System.out.println("Tamaño del archivo:\t"+item.getSize()/1024+"Kb")*/
		
		
/*		 byte[] result = Files.readAllBytes(Paths.get("peliculas.xlsx"));     
	     InputStream is = new ByteArrayInputStream(result);
	     Workbook book = WorkbookFactory.create(is);*/
		
		//InputStream input = new BufferedInputStream(new FileInputStream("peliculas.xlsx")); 
		
		//byte[] result = Files.readAllBytes(Paths.get("peliculas.xlsx"));
		//InputStream input = new BufferedInputStream(new FileInputStream("peliculas.xlsx")); 
		//InputStream excelFile = new FileInputStream("peliculas.xlsx"); 
		//BufferedReader Archivo = new BufferedReader(new InputStreamReader(new FileInputStream("peliculas.xlsx")));	
		
		
/*		InputStream excelFile = new ByteArrayInputStream(result);
//		InputStream excelFile = new FileInputStream("peliculas.xlsx"); 
		POIFSFileSystem fs = new POIFSFileSystem( excelFile );
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = wb.getSheetAt(0);
		Iterator rows = sheet.rowIterator(); 
		
		while( rows.hasNext() ) { 
			HSSFRow row = (HSSFRow) rows.next(); 
			System.out.println("\n"); 
			Iterator cells = row.cellIterator(); 
				while( cells.hasNext() ) {
					HSSFCell cell = (HSSFCell) cells.next(); 
					if(HSSFCell.CELL_TYPE_NUMERIC==cell.getCellType()){ 
						System.out.print( cell.getNumericCellValue()+" " );
					}	
					
					if(HSSFCell.CELL_TYPE_STRING==cell.getCellType()){
	                    System.out.print( cell.getStringCellValue()+"     " );
					}        	
				}

		}*/

		
		Util util=new Util();
		
		String fechaxx="12/05/2015";
		
		System.out.println("Dia:" + fechaxx.substring(0, 2));

		System.out.println("Dia:" + fechaxx.substring(3, 5));
		
		System.out.println("Dia:" + fechaxx.substring(8, 10));
		
		String texto="aBcDDD";
		
		texto="".toLowerCase();
		
		System.out.println(texto);
		
		String decimaltexto="1975";
		
		int valorxxx=decimaltexto.indexOf(",");

		System.out.println(valorxxx);
		
		String cantidad="";
		
		System.out.println("Cambio" + decimaltexto.replace("," ,"."));
		
		
		
		if(valorxxx>0){
			cantidad=(decimaltexto.substring(0, valorxxx) + "." + decimaltexto.substring(valorxxx+1));
		}
		
		System.out.println("Decimal:" +  cantidad);
		
		//System.out.println("Decimal:" +  Double.parseDouble(cantidad));
				
		
		
		System.out.println(util.obtenerHora());
		System.out.println(util.obtenerFechaTicket());
		System.out.println(util.rellenar("1245748", 25));
		System.out.println(util.rellenar("1245748", 25)+"codigo");
		
		File f = new File("FormatoFinal.xls"); // Creamos un objeto file
		System.out.println(f.getAbsolutePath()); // Llamamos al método que devuelve la ruta absoluta
		System.out.println(f.getCanonicalPath());

		DateFormat formatDate = new SimpleDateFormat("dd/MM/yy");
		
		Date f_1=null;
		Date f_2=null;
		
		f_1=formatDate.parse("04/02/14");
		
		f_2=formatDate.parse("04/02/2014");
		
		System.out.println("F1:" + f_1);
		
		System.out.println("F2:" + f_2);
		
		System.out.println("F1 FORMATO:" + f_1);
		
		String valor="1234567";
		String letra1,letra2,letra3,letra4,letra5;
		
		int x=valor.indexOf("z");
		
		System.out.println("Indice" + x);
		
		letra1=valor.substring(0, 1);
		letra2=valor.substring(1, 2);

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.set(2006, 12, 30);
		cal2.set(2007, 5, 3);
		  
		SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
		  
		Date f1 = null;
		Date f2 = null;
		  
		f1=fecha.parse("30/10/2014");
		f2=fecha.parse("30/11/2014");
		
		long dif=f2.getTime() - f1.getTime();
		  
		long dias = dif / (1000 * 60 * 60 * 24);
		
		System.out.println("Meses Prueba:" + dias/30);
		  
		System.out.println(dias);
		System.out.println(util.obtenerFecha());

		System.out.println("Valor Bodega:" + ConstantesGenerales.TipoConfiguracion.BODEGA.getTipoValor());
		System.out.println("Valor Lote:" + ConstantesGenerales.TipoConfiguracion.LOTE.getTipoValor());
		
		System.out.println("Modo Recojo Serie: " + ConstantesGenerales.ModoRecojo.NIVELSERIE.getCriterio());
		System.out.println("Modo Recojo Serie: " + ConstantesGenerales.ModoRecojo.NIVELSERIE.getDescripcion());
		
		System.out.println("Modo Recojo Cantidad: " + ConstantesGenerales.ModoRecojo.NIVELCANTIDAD.getCriterio());
		System.out.println("Modo Recojo Cantidad: " + ConstantesGenerales.ModoRecojo.NIVELCANTIDAD.getDescripcion());
		
		System.out.println("Busqueda Modo Recojo Cantidad: " + ConstantesGenerales.ModoRecojo.NIVELCANTIDAD.getCriterioModoRecojo("NIVELCANTIDAD").getDescripcion());
		
		String palabra="Buenos dias Peru";
		String extrae="";
		
		extrae=palabra.substring(0, palabra.length()-4);
		
		System.out.println(extrae);
		
		String fechax="05/12/2015";
		String extraeMes="";
		String extraeAnio="";
		String mes="02";
		System.out.println("Mes:" + Integer.parseInt(mes));
		
		extraeMes=fechax.substring(3, 5);
		extraeAnio=fechax.substring(8, 10);
		System.out.println("Mes Extrae:"  + extraeMes);
		System.out.println("Año Extrae:"  + extraeAnio);
		
		String cadena="x123456789123456789012368";
		
		System.out.println(util.quitarDigitosIzquierda(cadena, 18));
		
		String fechaDevol="01/10/2014";
		
		System.out.println("DiaFD: " + fechaDevol.substring(0, 2));
		System.out.println("DiaFD: " + fechaDevol.substring(3, 5));
		System.out.println("DiaFD: " + fechaDevol.substring(6, 10));
		
		String fechaHora01="04/06/2015 13:26:01";
		String fechaHora02="04/10/2015 13:26:01";
		String soloFecha01=fechaHora01.substring(0,10);
		String soloFecha02=fechaHora02.substring(0,10);
		
		SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
		
		Date f1X = null;
		Date f2X = null;
		  
		f1X=formatoFecha.parse(soloFecha01);
		f2X=formatoFecha.parse(soloFecha02);
		
		long diferencia=f2X.getTime() - f1X.getTime();
		  
		long diasX = diferencia / (1000 * 60 * 60 * 24);
		
		System.out.println("Meses Prueba:" + diasX/30);
		
		System.out.println(f1X);
		System.out.println(f2X);
		
		String fexa01="01/02/2014";
		
		System.out.println(fexa01.substring(3, 5));
		System.out.println(util.obtenerAnio(fexa01));
		
		System.out.println("Dia:" + util.obtenerDia(fexa01));
		
		System.out.println("Fecha Anterior Historial 01:" + util.obtenerFechaAnteriorHistorial(3, "01/04/2015"));
		System.out.println("Fecha Anterior Historial 02:" + util.obtenerFechaAnteriorHistorial("01/04/2015"));
		
		
		//System.out.println("Fecha Anterior:" + util.obtenerFechaPosteriorAcopio("26/12/2006"));
		System.out.println("Fecha Anterior: 26/05/2006");		
		System.out.println("Fecha Anterior:" + util.obtenerFechaPosteriorAcopio(3,"26/05/2006"));		
		
		
		//SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
		
		
		
		String fechaRecepPost="23/02/2015";
		String fechaSolicitud="23/02/2015";
		
		Date fechaRecepPostDate = new Date();
		Date fechaSolicitudDate = new Date();

		  
		fechaRecepPostDate=formatoFecha.parse(fechaRecepPost);
		fechaSolicitudDate=formatoFecha.parse(fechaSolicitud);
		
		if(fechaRecepPostDate.before(fechaSolicitudDate)){
			System.out.println("Fecha Uno es Menor");
			System.out.println("Fecha Dos es Mayor");
			System.out.println("Se aprueba ya que Fecha Recepcion es menor a la Fecha de Soolicitud");
		}else{
			System.out.println("Fecha Uno es Mayor");
			System.out.println("Fecha Dos es Menor");
			System.out.println("Se rechaza ya que Fecha Recepcion es mayor a la Fecha de Soolicitud");
		}
		

		
		String codigo1="EQ123";
		String codigo2="2015";
		
		System.out.println(codigo1.substring(0,2));
		
		System.out.println(codigo2.substring(2));
		
		
		
		 // System.out.println("Valor Garantia:" + ConstantesGenerales.TipoConfiguracion.GARANTIA.getTipoValor());
		  
		  
		  //System.out.println("Enummm:" + ConstantesGenerales.ExistenciaBodegas.getCriterioExistenciaBodegas("1").getCriterio());
		  
/*		  ErroresFileEnum eq=ErroresFileEnum.AVENTURA;
		  ErroresFileEnum eq2=ErroresFileEnum.ROMANCE;
		  
		  
		  
		  
		  System.out.println("Mi codigo: " + eq.getCodigo());
		  System.out.println("Mi codigo: " + eq2.getCodigo());
		  
		  List<ErroresFileEnum>*/
		
		String cadenaPrueba=" d 4 ee 5 6 89       e j           dn";
		String cadenaSinEspacios="";
		String letra="";
		for(int c=0;c<cadenaPrueba.length();c++){
			letra=cadenaPrueba.substring(c, (c+1));
			if(!letra.equals(" ")){
				cadenaSinEspacios=cadenaSinEspacios+letra;
			}
		}
		
		System.out.println(cadenaSinEspacios);
		
		System.out.println("Mi fechax:" + util.obtenerFechaAnteriorHistorial(3,"03/07/2015"));
		
	}

}
