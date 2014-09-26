package ar.com.norrmann.financiera.model;

import java.math.BigDecimal;

public class CalculadorMontos {
	
	final static BigDecimal CIEN = new BigDecimal(100);
	final static BigDecimal UNO = new BigDecimal(1);
	final static int DIGITOS_REDONDEO = 0;
	
	// ejemplo de calculo
	
	// indice = (tasa * cantidad cuotas)/100+1
	// importe cuota = solicitado * (indice) / cantidad cuotas
	// 
	// solicitado = 6000
	// cantidad cuotas = 12
	// interes = 5
	// 6000 * 1.6 / 12
	
	public static String calcularMontoTotal(String montoCuota, String cantidadCuotas, String tasa) {

		// solicitado = importe cuota * cantidad cuotas / indice
		
		BigDecimal cuota = getBigDecimal(montoCuota);
		BigDecimal cuotas = getBigDecimal(cantidadCuotas);
		BigDecimal tasaInteres = getBigDecimal(tasa);

		BigDecimal indice =  tasaInteres.multiply(cuotas).divide(CIEN).add(UNO);
		
		BigDecimal montoTotal = cuota.multiply(cuotas).divide(indice,DIGITOS_REDONDEO,BigDecimal.ROUND_HALF_UP);
		return montoTotal.toString();
	}
	
	public static String calcularTasa(String montoTotal, String montoCuota, String cantidadCuotas) {
		BigDecimal monto = getBigDecimal(montoTotal);
		BigDecimal cuota = getBigDecimal(montoCuota);
		BigDecimal cuotas = getBigDecimal(cantidadCuotas);
		
		//indice = importe cuota * cantidad cuotas / solicitado
		BigDecimal indice =  cuota.multiply(cuotas).divide(monto,10,BigDecimal.ROUND_HALF_UP);
		
		//BigDecimal tasa = cuotas.multiply(cuota).divide(monto,10,BigDecimal.ROUND_HALF_UP);
		BigDecimal tasa = indice.subtract(UNO).multiply(CIEN).divide(cuotas,2,BigDecimal.ROUND_HALF_UP);
		//tasa=tasa.subtract(UNO).multiply(CIEN).setScale(2,BigDecimal.ROUND_HALF_UP);

		return tasa.toString();
	}
	
	public static String calcularCantidadCuotas(String montoTotal, String montoCuota, String tasa) {
		return "10";
	}
	
	public static String calcularMontoCuota(String montoTotal, String cantidadCuotas, String tasa) {
		
		// indice = (tasa * cantidad cuotas)/100+1
		// importe cuota = solicitado * (indice) / cantidad cuotas

		BigDecimal montoSolicitado = getBigDecimal(montoTotal);
		BigDecimal cuotas = getBigDecimal(cantidadCuotas);

		BigDecimal indice =  getBigDecimal(tasa).multiply(new BigDecimal(cantidadCuotas)).divide(CIEN).add(UNO);;
		BigDecimal importeCuota = montoSolicitado.multiply(indice).divide(cuotas, CalculadorMontos.DIGITOS_REDONDEO,BigDecimal.ROUND_HALF_UP);
		return importeCuota.toString();
	}
	
	private static BigDecimal getBigDecimal(String value){
		BigDecimal bigDecimal = null;
		try{
			bigDecimal = new BigDecimal(value);
			bigDecimal = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP);
		} catch (Exception e){
			e.printStackTrace();
		}
		return bigDecimal;
		
	}
}
