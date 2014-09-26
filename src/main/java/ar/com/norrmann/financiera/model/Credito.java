package ar.com.norrmann.financiera.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findCreditoesByCliente" })
@RooSerializable
public class Credito {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private transient boolean venceLunes;
	private transient Map<Long,Boolean> venceDia= new HashMap<Long,Boolean>();
	private final transient String[] DIAS = {"","Lu","Ma","Mi","Ju","Vi","Sab"};

	@NotNull
	@ManyToOne
	private Cliente cliente;

	@ManyToOne
	private Cliente garante;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date fechaSolicitud = new Date();

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date fechaPrimerVencimiento = new Date();

	@NotNull
	@Digits(integer = 9, fraction = 2)
	private BigDecimal montoSolicitado;

	@NotNull
	@Digits(integer = 9, fraction = 2)
	private BigDecimal montoDeCuota;

	@NotNull
	private int cantidadCuotas;

	@NotNull 
	@Digits(integer = 4, fraction = 2)
	private BigDecimal tasa;

	private int periodoDeVencimiento;
	
	@Column(name="id_empresa")
	private Long idEmpresa;

//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "credito", fetch = FetchType.EAGER)
//	private Set<Cuota> cuotas = new HashSet<Cuota>();

	@NotNull
	@ManyToOne
	private TipoCredito tipoCredito;
	
	@ManyToOne
	private InteresPunitorio interesPunitorio;


	@Transient
	private ResumenCredito resumenCredito;

	public BigDecimal getMontoTotal(){
		return montoDeCuota.multiply(new BigDecimal(cantidadCuotas)).setScale(CalculadorMontos.DIGITOS_REDONDEO,BigDecimal.ROUND_HALF_UP);
	}
	
	public BigDecimal getInteres(){
		return getMontoTotal().subtract(montoSolicitado);
	}
	
	public ResumenCredito getResumenCredito() {
		if (resumenCredito==null) calcularResumenCuotas();
		return resumenCredito;
	}

	public String generarCuotas() {
		final BigDecimal UNO = new BigDecimal(1);
		final BigDecimal CIEN = new BigDecimal(100);
		this.cuotas = new ArrayList<Cuota>();
		
		
		//BigDecimal tasaPorcentual = tasa.divide(CIEN).add(UNO);
		BigDecimal tasaPorCuotas =  tasa.multiply(new BigDecimal(cantidadCuotas)).divide(CIEN).add(UNO);
		
//		BigDecimal importeCuota = montoSolicitado.multiply(tasaPorcentual)
//				.divide(new BigDecimal(cantidadCuotas), CalculadorMontos.DIGITOS_REDONDEO,
//						BigDecimal.ROUND_HALF_UP);

		BigDecimal importeCuota = montoSolicitado.multiply(tasaPorCuotas).divide(new BigDecimal(cantidadCuotas), CalculadorMontos.DIGITOS_REDONDEO,BigDecimal.ROUND_HALF_UP);
		
	if (isDiario()){
		this.cuotas.addAll(getPlanCuotasDiario(importeCuota));
	} else {
		this.cuotas.addAll(getPlanCuotasMensual(importeCuota));
	}
		return null;
	}
	
	private Set<Cuota> getPlanCuotasDiario(BigDecimal importeCuota){
		Set<Cuota> cuotaSet = new TreeSet<Cuota>();
		
		Calendar c = Calendar.getInstance();
		c.setTime(fechaPrimerVencimiento);

		for (int i = 1; i <= cantidadCuotas; i++) {
			Cuota cuota = new Cuota();
			cuota.setNumero(i);
			cuota.setImporte(importeCuota);
			cuota.setCredito(this);
			 
			while(!venceDia(c)){
				c.add(Calendar.DATE, 1);
			}
			
			cuota.setFechaVencimiento(c.getTime());
			cuotaSet.add(cuota);

			c.add(Calendar.DATE, 1);

		}
		return cuotaSet;
		
	}
	
	private boolean venceDia(Calendar c){
		Long dia = new Long(c.get(Calendar.DAY_OF_WEEK));
		Object vence = venceDia.get(dia);
		if (vence==null)return false;
		return Boolean.valueOf(vence+"").booleanValue();
	}

	private boolean venceDia(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return venceDia(calendar);
	}
	
	private Set<Cuota> getPlanCuotasMensual(BigDecimal importeCuota){
		Set<Cuota> cuotaSet = new TreeSet<Cuota>();
		for (int i = 1; i <= cantidadCuotas; i++) {
			Cuota cuota = new Cuota();
			cuota.setNumero(i);
			cuota.setImporte(importeCuota);
			cuota.setCredito(this);
			Calendar c = Calendar.getInstance();
			c.setTime(fechaPrimerVencimiento);
			c.add(Calendar.MONTH, i-1);

			// si es domingo postponer el vencimiento un dia mas
			if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
				c.add(Calendar.DATE, 1);
			}

			cuota.setFechaVencimiento(c.getTime());
			cuotaSet.add(cuota);
		}
		return cuotaSet;
	}

	public int getCantidadCuotasVencidas() {
		Date hoy = Calendar.getInstance().getTime();
		Query query = entityManager()
				.createQuery(
						"SELECT count(c) FROM Cuota c where c.pagada=0 and c.fechaVencimiento <= :hoy and c.credito=:credito",
						Long.class);
		query.setParameter("hoy", hoy);
		query.setParameter("credito", this);
		List<Long> resultList = query.getResultList();
		return resultList.get(0).intValue();
	}

	private void calcularResumenCuotas() {
		List<Cuota> cuotaList = getCuotas(); 
		int cantVencidas = 0;
		int cantImpagas = 0;
		int cantPagadas = 0;
		for (Cuota unaCuota : cuotaList) {
			if (unaCuota.isCuotaPagada()) {
				cantPagadas++;
			} else {
				cantImpagas++;
				if (unaCuota.isVencida()) {
					cantVencidas++;
				}

			}
		}
		BigDecimal totalCuotasImpagas = montoDeCuota.multiply(new BigDecimal(cantImpagas));
		resumenCredito= new ResumenCredito(cantVencidas, cantImpagas, cantPagadas,totalCuotasImpagas);
	}
	
	public boolean isMensual(){
		return tipoCredito!=null&&tipoCredito.getId().equals(TipoCredito.ID_MENSUAL);
	}
	public boolean isDiario(){
		return tipoCredito!=null&&tipoCredito.getId().equals(TipoCredito.ID_DIARIO);
	}

	
	@Transactional
	public void removePagos() {
		Query query = entityManager().createQuery("select p from Pago p where p.cuota.credito=:credito)",Pago.class);
		query.setParameter("credito", this);
		List<Pago> pagoList =query.getResultList();
		for (Pago unPago:pagoList){
		unPago.remove();
		}
		
	}
	@Transactional
	public void removeCuotas() {
		Query query = entityManager().createQuery("select c from Cuota c where c.credito=:credito)",Cuota.class);
		query.setParameter("credito", this);
		List<Cuota> cuotaList =query.getResultList();
		for (Cuota unaCuota:cuotaList){
			unaCuota.remove();
		}
		
	}
	@Transient
	private List<Cuota> cuotas; 
	public List<Cuota> getCuotas(){
		if (cuotas==null){
			cuotas =Cuota.findCuotasByCredito(this).getResultList(); 
		}
		return cuotas;
	}



	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
        for (Cuota unaCuota:cuotas){
        	unaCuota.setCredito(this);
        	unaCuota.persist();
        }
    }

	public BigDecimal getTasa() {
		return tasa;
	}

	public void setTasa(BigDecimal tasa) {
		this.tasa = tasa;
	}

	public BigDecimal getMontoDeCuota() {
		return montoDeCuota;
	}

	public void setMontoDeCuota(BigDecimal montoDeCuota) {
		this.montoDeCuota = montoDeCuota;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

    public static List<Credito> findCreditoesByClienteAndIdEmpresa(Cliente cliente, Long idEmpresa) {
        if (idEmpresa == null) throw new IllegalArgumentException("The idEmpresa argument is required");
        EntityManager em = Credito.entityManager();
        String queryString = "SELECT o FROM Credito AS o WHERE o.idEmpresa = :idEmpresa ";
        if (cliente != null){
        	queryString += " and o.cliente= :cliente ";
        }
        TypedQuery<Credito> q = em.createQuery(queryString, Credito.class);
        q.setParameter("idEmpresa", idEmpresa);
        if (cliente != null){
        	q.setParameter("cliente", cliente);
        }
        return q.getResultList();
    }

	public boolean isVenceLunes() {
		return venceLunes;
	}

	public void setVenceLunes(boolean venceLunes) {
		this.venceLunes = venceLunes;
	}

	public Map<Long, Boolean> getVenceDia() {
		return venceDia;
	}

	public void setVenceDia(Map<Long, Boolean> venceDia) {
		this.venceDia = venceDia;
	}
	
	public boolean seleccionoDias(){
		if (!isDiario()) return true;
		return venceDia.values().contains("true");
		
	}
	
	public void setVenceDiaFromCuotas(){
		List<Cuota> cuotaList = Cuota.findCuotasByCredito(this).getResultList();
		Calendar calendar = Calendar.getInstance();
		for (Cuota unaCuota:cuotaList){
			calendar.setTime(unaCuota.getFechaVencimiento());
			venceDia.put((long) calendar.get(Calendar.DAY_OF_WEEK), true);
			
		}
	}
	
	public void recalcularCuotasDiario(){
		List<Cuota> cuotaImpagaList = Cuota.findCuotasImpagasByCredito(this).getResultList();
		Collections.sort(cuotaImpagaList);
		Cuota primerCuotaImpaga = cuotaImpagaList.get(0);
		 

		Calendar c = Calendar.getInstance();
		c.setTime(primerCuotaImpaga.getFechaVencimiento());
		c.add(Calendar.DATE, -1);
		
		Date primerFechaImpaga = c.getTime();

		for (Cuota unaCuota:cuotaImpagaList){
			primerFechaImpaga = getProximoVencimiento(primerFechaImpaga);
			unaCuota.setFechaVencimiento(primerFechaImpaga);
			unaCuota.merge();
		}
		
		
	}
	
	private Date getProximoVencimiento(Date fecha){
		Calendar c = Calendar.getInstance();
		c.setTime(fecha);
		c.add(Calendar.DATE, 1);
		while(!venceDia(c)){
			c.add(Calendar.DATE, 1);
		}
		return c.getTime();
		
	}
	
	public String getDiasVencimientoString(){
		if (isMensual()) return "Mensual";
		setVenceDiaFromCuotas();
		StringBuilder sb = new StringBuilder();
		for (Long unDia : venceDia.keySet()){
			if (venceDia.get(unDia)){
				sb.append(DIAS[unDia.intValue()-1]);
				sb.append(" ");
			}
		}
		return sb.toString();
	}


}
