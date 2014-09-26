package ar.com.norrmann.financiera.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import ar.com.norrmann.financiera.model.Cliente;
import ar.com.norrmann.financiera.model.Cobrador;
import ar.com.norrmann.financiera.model.Cuota;
import ar.com.norrmann.financiera.model.Pago;
import ar.com.norrmann.financiera.model.Zona;

@RooSerializable
@RooJsfManagedBean(entity = Cuota.class, beanName = "cuotaBean")
public class CuotaBean {
	
	private TipoListado tipoListado=TipoListado.VENCIMIENTOS;
	
	private FiltroCuotaList filtroCuotaList = new FiltroCuotaList();
	private Map<Cliente, BigDecimal> saldoMap = new HashMap<Cliente, BigDecimal>();
	public HtmlPanelGrid populateCreatePanel() {
		return null;
	}
	
	private Date fechaPagoTotalCuota;
	private Pago ultimoPago;
	
	public String listarPagos(){
		tipoListado=TipoListado.PAGOS;
		setAllCuotas(null);
		setDataVisible(false);
		return "cuota";
	}
	public String listarVencimientos(){
		tipoListado=TipoListado.VENCIMIENTOS;
		setAllCuotas(null);
		setDataVisible(false);
		return "cuota";
	}
	
	private void setFechaCalculoMora(Date fecha){
		List<Cuota> cuotas = getAllCuotas();
		if (cuotas==null||cuotas.isEmpty())return;
		for (Cuota unaCuota : cuotas){
			unaCuota.getCredito().getCliente().setFechaCalculoMora(fecha);
		}
	}
	
	public String filtrarLista(){
		//Cliente.setFechaCalculoMora(filtroCuotaList.getFechaHasta());
		if (filtroCuotaList.getZona()!=null){
			if (tipoListado.equals(TipoListado.VENCIMIENTOS)){
				setAllCuotas(Cuota.findCuotasByFechaVencimientoAl(filtroCuotaList.getFechaHasta(),filtroCuotaList.getZona(),filtroCuotaList.getEntidadCliente(),filtroCuotaList.getIdEmpresa()));
			} else {
				setAllCuotas(Cuota.findPagosByFechaVencimientoBetween(filtroCuotaList.getFechaDesde(), filtroCuotaList.getFechaHasta(),filtroCuotaList.getZona(),filtroCuotaList.getEntidadCliente(),filtroCuotaList.getIdEmpresa()));
				
			}
			setDataVisible(!getAllCuotas().isEmpty());
			setFechaCalculoMora(filtroCuotaList.getFechaHasta());
			calculateSaldoMap();
		}
		return "cuota";
	}
	public FiltroCuotaList getFiltroCuotaList() {
		return filtroCuotaList;
	}
	public void setFiltroCuotaList(FiltroCuotaList filtroCuotaList) {
		this.filtroCuotaList = filtroCuotaList;
	}
	
	private void calculateSaldoMap(){
		List<Cuota> cuotas = getAllCuotas();
		saldoMap.clear();
		for (Cuota unaCuota:cuotas){
			Cliente cliente = unaCuota.getCredito().getCliente();
			BigDecimal saldo = saldoMap.get(cliente);
			if (saldo==null){
				saldo = unaCuota.getSaldo();
			} else {
				saldo=saldo.add(unaCuota.getSaldo());
			}
			saldoMap.put(cliente, saldo);
			
		}
	}
	public String initPagarTotalCuota(){
		this.fechaPagoTotalCuota=filtroCuotaList.getFechaHasta();
		return null;
	}
	public String pagarTotalCuota(){
		Zona zona = Cuota.findZonaByCuota(getCuota().getId());
		Cobrador cobrador = Cobrador.findCobradorByZona(zona);
		if (cobrador==null){
			FacesMessage facesMessage = new FacesMessage("No hay cobradores para la zona "+zona.getZona());
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			return null;
		}
		Pago pago = new Pago();
		pago.setCobrador(cobrador);
		pago.setCuota(getCuota());
		//pago.setFechaPago(filtroCuotaList.getFechaHasta());
		pago.setFechaPago(this.fechaPagoTotalCuota);
		pago.setImporte(getCuota().getSaldo());
		pago.persist();
		this.ultimoPago = pago;
		BigDecimal saldo = getCuota().getSaldo();
		if (saldo.compareTo(BigDecimal.ZERO) == 0) {
			getCuota().saldar();
		}

		//return filtrarLista();
		return null;
		
	}
	
	public String printUltimoPagoCuota(){
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/PrintServlet?reportName=reciboUltimaCuota");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String printUltimoPagoGeneral(){
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/PrintServlet?reportName=reciboUltimoPago");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<Cliente, BigDecimal> getSaldoMap() {
		return saldoMap;
	}

	public void setSaldoMap(Map<Cliente, BigDecimal> saldoMap) {
		this.saldoMap = saldoMap;
	}
	
	enum TipoListado {
	    VENCIMIENTOS,PAGOS 
	}

	public TipoListado getTipoListado() {
		return tipoListado;
	}

	public void setTipoListado(TipoListado tipoListado) {
		this.tipoListado = tipoListado;
	}
	public Date getFechaPagoTotalCuota() {
		return fechaPagoTotalCuota;
	}
	public void setFechaPagoTotalCuota(Date fechaPagoTotalCuota) {
		this.fechaPagoTotalCuota = fechaPagoTotalCuota;
	}
	public Pago getUltimoPago() {
		return ultimoPago;
	}
	public void setUltimoPago(Pago ultimoPago) {
		this.ultimoPago = ultimoPago;
	}
}
