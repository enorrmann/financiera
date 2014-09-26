package ar.com.norrmann.financiera.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findPagoesByCuota", "findPagoesByFechaPagoBetween", "findPagoesByFechaPagoEquals" })
public class Pago {

    @NotNull
    @ManyToOne
    private Cuota cuota;

    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE)
    private Cobrador cobrador;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date fechaPago = new Date();

    @NotNull
    @DecimalMin("0.01")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal importe;

    public static List<ar.com.norrmann.financiera.model.Pago> findPagoesByFechaPagoBetweenAndZona(Date minFechaPago, Date maxFechaPago, Zona zona) {
        List<Pago> listaVacia = new ArrayList<Pago>();
        if (minFechaPago == null) return listaVacia;
        if (maxFechaPago == null) return listaVacia;
        if (zona == null) return listaVacia;
        EntityManager em = Pago.entityManager();
        TypedQuery<Pago> q = em.createQuery("SELECT o FROM Pago AS o WHERE o.fechaPago BETWEEN :minFechaPago AND :maxFechaPago AND o.cuota.credito.cliente.zona = :zona", Pago.class);
        q.setParameter("minFechaPago", minFechaPago);
        q.setParameter("maxFechaPago", maxFechaPago);
        q.setParameter("zona", zona);
        return q.getResultList();
    }
    public static List<Pago> findPagoesByFechaPagoEqualsAndIdEmpresa(Date fechaPago,Long idEmpresa) {
        if (fechaPago == null) throw new IllegalArgumentException("The fechaPago argument is required");
        EntityManager em = Pago.entityManager();
        TypedQuery<Pago> q = em.createQuery("SELECT o FROM Pago AS o WHERE o.cuota.credito.idEmpresa = :idEmpresa and o.fechaPago = :fechaPago", Pago.class);
        q.setParameter("fechaPago", fechaPago);
        q.setParameter("idEmpresa", idEmpresa);
        return q.getResultList();
    }
}
