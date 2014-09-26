package ar.com.norrmann.financiera.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findMovimientoCajasByFechaEquals" })
public class MovimientoCaja {

    @ManyToOne
    @NotNull
    private TipoMovimientoCaja tipoMovimientoCaja;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "M-")
    private Date fecha = new Date();

    @Digits(integer = 9, fraction = 2)
    private BigDecimal importe;

    private String concepto;
    
    @Transient
    private List<Pago> pagoList; 
    
	@Column(name="id_empresa")
	private Long idEmpresa;

    
    public List<Pago> getPagoList() {
		return pagoList;
	}

	public void setPagoList(List<Pago> pagoList) {
		this.pagoList = pagoList;
		importe=new BigDecimal(0);
		if (pagoList==null)return;
		for (Pago unPago:pagoList){
			importe=importe.add(unPago.getImporte());
		}
	}

	public BigDecimal getImporteNeto(){
    	return importe.multiply(new BigDecimal(tipoMovimientoCaja.getVariacion()));
    }

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	
    public static List<MovimientoCaja> findMovimientoCajasByFechaEqualsAndIdEmpresa(Date fecha,Long idEmpresa) {
        if (fecha == null) throw new IllegalArgumentException("The fecha argument is required");
        
        EntityManager em = MovimientoCaja.entityManager();
        TypedQuery<MovimientoCaja> q = em.createQuery("SELECT o FROM MovimientoCaja AS o WHERE o.fecha = :fecha and o.idEmpresa = :idEmpresa", MovimientoCaja.class);
        q.setParameter("fecha", fecha);
        q.setParameter("idEmpresa", idEmpresa);
        return q.getResultList();
    }
}
