package ar.com.norrmann.financiera.model;

import java.math.BigDecimal;

public class ResumenCredito {
	public int getCantVencidas() {
		return cantVencidas;
	}

	public void setCantVencidas(int cantVencidas) {
		this.cantVencidas = cantVencidas;
	}

	public int getCantImpagas() {
		return cantImpagas;
	}

	public void setCantImpagas(int cantImpagas) {
		this.cantImpagas = cantImpagas;
	}

	public int getCantPagadas() {
		return cantPagadas;
	}

	public void setCantPagadas(int cantPagadas) {
		this.cantPagadas = cantPagadas;
	}

	int cantImpagas = 0;
	int cantPagadas = 0;
	int cantVencidas = 0;
	BigDecimal totalCuotasImpagas;

	public ResumenCredito(int cantVencidas, int cantImpagas, int cantPagadas, BigDecimal totalCuotasImpagas) {
		this.cantVencidas = cantVencidas;
		this.cantImpagas = cantImpagas;
		this.cantPagadas = cantPagadas;
		this.totalCuotasImpagas = totalCuotasImpagas;
	}

	public BigDecimal getTotalCuotasImpagas() {
		return totalCuotasImpagas;
	}

	public void setTotalCuotasImpagas(BigDecimal totalCuotasImpagas) {
		this.totalCuotasImpagas = totalCuotasImpagas;
	}



}
