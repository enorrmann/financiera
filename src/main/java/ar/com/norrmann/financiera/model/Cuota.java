package ar.com.norrmann.financiera.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooSerializable
@RooJpaActiveRecord(finders = { "findCuotasByFechaVencimientoBetween", "findCuotasByCredito" })
public class Cuota implements Comparable<ar.com.norrmann.financiera.model.Cuota> {

    @Min(1)
    private int numero;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    @NotNull
    private Date fechaVencimiento;

    @NotNull
    @DecimalMin("0.01")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal importe;

    @NotNull
    @ManyToOne
    private Credito credito;

    private Long pagada = 0L;

    @Override
    public int compareTo(ar.com.norrmann.financiera.model.Cuota otraCuota) {
        return numero - otraCuota.getNumero();
    }

    public static List<ar.com.norrmann.financiera.model.Cuota> findAllCuotas() {
        return entityManager().createQuery("SELECT o FROM Cuota o order by o.numero", Cuota.class).getResultList();
    }

    public Pago getUltimoPago() {
        Query query = entityManager().createQuery("SELECT p FROM Pago p where p.cuota = :cuota order by p.id desc", Pago.class);
        query.setParameter("cuota", this);
    	List<Pago> pagoList = query.getResultList();
        if (pagoList!=null&&!pagoList.isEmpty()) return pagoList.get(0);
        return null;
    }
    
    public Date getUltimaFechaPago(){
    	Pago ultimoPago = getUltimoPago();
    	if (ultimoPago!=null){
    		return ultimoPago.getFechaPago();
    	} else {
    		return null;
    	}
    }

    public List<ar.com.norrmann.financiera.model.Pago> getPagos() {
        return Pago.findPagoesByCuota(this).getResultList();
    }

    public BigDecimal getTotalPagado() {
        BigDecimal totalPagado = new BigDecimal(0);
        List<Pago> pagos = getPagos();
        for (Pago unPago : pagos) {
            totalPagado = totalPagado.add(unPago.getImporte());
        }
        return totalPagado;
    }

    public BigDecimal getSaldo() {
        if (this.pagada == 1) return new BigDecimal(0);
        BigDecimal totalPagado = getTotalPagado();
        BigDecimal intereses = getIntereses();

        // si el total pagado es mayor que el importe de la cuota, entonces el saldo es cero
        if (totalPagado.compareTo(importe.add(intereses==null?new BigDecimal(0):intereses))==1){
        	return new BigDecimal(0);
        }
        List<Pago> pagos = getPagos();
        if (intereses == null) intereses = new BigDecimal(0);
        if (pagos == null || pagos.isEmpty()) {
            return importe.add(intereses);
        } else {
        	BigDecimal saldo = importe.subtract(totalPagado).add(intereses);
        		return saldo;
        	
        }
    }

    @Transactional
    public void saldar() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Cuota merged = this.entityManager.find(Cuota.class, getId());
        merged.setPagada(1L);
        this.entityManager.flush();
    }

    @Transactional
    public void impagar() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Cuota merged = this.entityManager.find(Cuota.class, getId());
        merged.setPagada(0L);
        this.entityManager.flush();
    }

    public static TypedQuery<ar.com.norrmann.financiera.model.Cuota> findCuotasByFechaVencimientoBetween(Date minFechaVencimiento, Date maxFechaVencimiento, Zona zona) {
        if (minFechaVencimiento == null) throw new IllegalArgumentException("The minFechaVencimiento argument is required");
        if (maxFechaVencimiento == null) throw new IllegalArgumentException("The maxFechaVencimiento argument is required");
        if (zona == null) throw new IllegalArgumentException("The zona argument is required");
        EntityManager em = Cuota.entityManager();
        TypedQuery<Cuota> q = em.createQuery("SELECT o FROM Cuota AS o WHERE o.fechaVencimiento BETWEEN :minFechaVencimiento AND :maxFechaVencimiento AND o.credito.cliente.zona = :zona and o.pagada = 0 order by o.credito.cliente.apellidos,o.numero", Cuota.class);
        q.setParameter("minFechaVencimiento", minFechaVencimiento);
        q.setParameter("maxFechaVencimiento", maxFechaVencimiento);
        q.setParameter("zona", zona);
        return q;
    }

    public static List<ar.com.norrmann.financiera.model.Cuota> findCuotasByFechaVencimientoAl(Date maxFechaVencimiento, Zona zona, Cliente cliente, Long idEmpresa) {
        if (maxFechaVencimiento == null) throw new IllegalArgumentException("The maxFechaVencimiento argument is required");
        if (zona == null) throw new IllegalArgumentException("The zona argument is required");
        EntityManager em = Cuota.entityManager();
        String queryString = "SELECT o FROM Cuota AS o WHERE o.fechaVencimiento <= :maxFechaVencimiento AND o.credito.cliente.zona = :zona and o.pagada = 0 "; 
        if (cliente != null){
        	queryString += " and o.credito.cliente = :cliente ";
        }
        if (idEmpresa != null&&!idEmpresa.equals(-1L)){
        	queryString += " and o.credito.idEmpresa = :idEmpresa ";
        }

        queryString += "order by o.credito.cliente.ordenCobranza,o.credito.cliente.apellidos,o.numero";
        TypedQuery<Cuota> q = em.createQuery(queryString, Cuota.class);
        if (cliente != null){
        	q.setParameter("cliente", cliente);
        }
        if (idEmpresa != null&&!idEmpresa.equals(-1L)){
        	q.setParameter("idEmpresa", idEmpresa);
        }
        q.setParameter("maxFechaVencimiento", maxFechaVencimiento);
        q.setParameter("zona", zona);
        return q.getResultList();
    }

    public static List<ar.com.norrmann.financiera.model.Cuota> findPagosByFechaVencimientoBetween(Date minFechaVencimiento, Date maxFechaVencimiento, Zona zona, Cliente cliente, Long idEmpresa) {
        if (minFechaVencimiento == null) throw new IllegalArgumentException("The minFechaVencimiento argument is required");
        if (maxFechaVencimiento == null) throw new IllegalArgumentException("The maxFechaVencimiento argument is required");
        if (zona == null) throw new IllegalArgumentException("The zona argument is required");
        EntityManager em = Cuota.entityManager();
        String queryString = "SELECT o FROM Cuota AS o WHERE o.fechaVencimiento BETWEEN :minFechaVencimiento AND :maxFechaVencimiento AND o.credito.cliente.zona = :zona and o.pagada = 1 ";
        if (cliente != null){
        	queryString += " and o.credito.cliente = :cliente ";
        	
        }
        if (idEmpresa != null&&!idEmpresa.equals(-1L)){
        	queryString += " and o.credito.idEmpresa = :idEmpresa ";
        }
        queryString += "order by o.credito.cliente.apellidos,o.numero";
        TypedQuery<Cuota> q = em.createQuery(queryString, Cuota.class);
        if (cliente != null){
        	q.setParameter("cliente", cliente);
        }
        if (idEmpresa != null&&!idEmpresa.equals(-1L)){
        	q.setParameter("idEmpresa", idEmpresa);
        }
        q.setParameter("minFechaVencimiento", minFechaVencimiento);
        q.setParameter("maxFechaVencimiento", maxFechaVencimiento);
        q.setParameter("zona", zona);
        return q.getResultList();
    }

    public boolean isVencida() {
        //return !isCuotaPagada() && fechaVencimiento.before(Calendar.getInstance().getTime());
    	//return !isCuotaPagada() && fechaVencimiento.before(Cliente.getFechaCalculoMora());
    	return !isCuotaPagada() && fechaVencimiento.before(getCredito().getCliente().getFechaCalculoMora());
    }

    public boolean isCuotaPagada() {
        return pagada != null && pagada.equals(1L);
    }

    private int getDiasVencida() {
        //int days = Days.daysBetween(new DateTime(fechaVencimiento), new DateTime(Calendar.getInstance().getTime())).getDays();
    	//int days = Days.daysBetween(new DateTime(fechaVencimiento), new DateTime(Cliente.getFechaCalculoMora())).getDays();
    	int days = Days.daysBetween(new DateTime(fechaVencimiento), new DateTime(getCredito().getCliente().getFechaCalculoMora())).getDays();
        return days;
    }

    public BigDecimal getIntereses() {
        if (getCredito().isDiario()) return null; 
        if (!isVencida()) return null;
        //if (getCredito().getCliente().getFechaCalculoMora().after(Calendar.getInstance().getTime()))return null;
        
        //BigDecimal importeBase = this.importe.subtract(getTotalPagado());
        // si calculo sobre el saldo da mucho menos intereses 
        BigDecimal importeBase = importe;

        // por los intereses puede ser negativo
        if (importeBase.signum()==-1){
        	return new BigDecimal(0);
        }
        final BigDecimal CIEN = new BigDecimal(100);
        InteresPunitorio interesPunitorio = getCredito().getInteresPunitorio();
        if (interesPunitorio==null)return null;
        BigDecimal interesDiario = interesPunitorio.getInteres().divide(CIEN).multiply(importeBase);
        BigDecimal intereses = interesDiario.multiply(new BigDecimal(getDiasVencida()));
        return intereses.setScale(CalculadorMontos.DIGITOS_REDONDEO, BigDecimal.ROUND_HALF_DOWN);
    }

	public static TypedQuery<Cuota> findCuotasByCredito(Credito credito) {
        if (credito == null) throw new IllegalArgumentException("The credito argument is required");
        EntityManager em = Cuota.entityManager();
        TypedQuery<Cuota> q = em.createQuery("SELECT o FROM Cuota AS o WHERE o.credito = :credito order by o.numero", Cuota.class);
        q.setParameter("credito", credito);
        return q;
    }

	public static TypedQuery<Cuota> findCuotasImpagasByCredito(Credito credito) {
        if (credito == null) throw new IllegalArgumentException("The credito argument is required");
        EntityManager em = Cuota.entityManager();
        TypedQuery<Cuota> q = em.createQuery("SELECT o FROM Cuota AS o WHERE o.credito = :credito and o.pagada = 0 order by o.numero", Cuota.class);
        q.setParameter("credito", credito);
        return q;
    }

	public static Zona findZonaByCuota(Long idCuota) {
        if (idCuota == null) throw new IllegalArgumentException("The cuota argument is required");
        EntityManager em = Cuota.entityManager();
        TypedQuery<Zona> q = em.createQuery("SELECT c.credito.cliente.zona FROM Cuota AS c WHERE c.id = :idCuota", Zona.class);
        q.setParameter("idCuota", idCuota);
        List<Zona> zonaList = q.getResultList();
        if (zonaList==null||zonaList.isEmpty()){
        	return null;
        } else {
        	return zonaList.get(0);
        }
    }

	public Credito getCredito() {
        return this.credito;
    }

	public Date getFechaVencimiento() {
        return this.fechaVencimiento;
    }

	public BigDecimal getImporte() {
        return this.importe;
    }

	public int getNumero() {
        return this.numero;
    }

	public Long getPagada() {
        return this.pagada;
    }

	public void setCredito(Credito credito) {
        this.credito = credito;
    }

	public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

	public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

	public void setNumero(int numero) {
        this.numero = numero;
    }

	public void setPagada(Long pagada) {
        this.pagada = pagada;
    }
}
