package ar.com.norrmann.financiera.web;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import ar.com.norrmann.financiera.model.Cliente;
import ar.com.norrmann.financiera.model.Zona;

public class FiltroCuotaList implements Serializable{
	private Date fechaDesde = new Date();
	private Date fechaHasta = new Date();
	private Zona zona;
	private Long idEmpresa=1L;
	private String cliente;
	private Cliente entidadCliente;

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public Date getFechaDesde() {
		if (fechaDesde==null)return null;
		return DateUtils.truncate(fechaDesde,Calendar.DAY_OF_MONTH);
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		if (fechaHasta==null)return null;
		return DateUtils.truncate(fechaHasta,Calendar.DAY_OF_MONTH);
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public Zona getZona() {
		return zona;
	}

	public void setZona(Zona zona) {
		this.zona = zona;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Cliente getEntidadCliente() {
		return entidadCliente;
	}

	public void setEntidadCliente(Cliente entidadCliente) {
		this.entidadCliente = entidadCliente;
	}

}
