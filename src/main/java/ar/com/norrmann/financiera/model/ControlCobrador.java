package ar.com.norrmann.financiera.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooSerializable
@RooJpaActiveRecord(finders = { "findControlCobradorsByFechaEquals" })
public class ControlCobrador {

    @NotNull
    @ManyToOne
    private Cobrador cobrador;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "M-")
    private Date fecha = new Date();

    @Digits(integer = 9, fraction = 2)
    private BigDecimal montoEntregado;

    @Digits(integer = 9, fraction = 2)
    private BigDecimal montoDevuelto;

    @Transient
    private BigDecimal totalCobrado;

    @Transient
    private List<Pago> pagoList;

    public List<ar.com.norrmann.financiera.model.Pago> getPagoList() {
        if (pagoList != null) return pagoList;
        if (cobrador == null || fecha == null) return null;
        Query query = entityManager().createQuery("select p from Pago p where p.cobrador.id = :idCobrador and p.fechaPago = :fechaPago", Pago.class);
        query.setParameter("idCobrador", cobrador.getId());
        query.setParameter("fechaPago", fecha);
        pagoList = query.getResultList();
        return pagoList;
    }

    public BigDecimal getTotalCobrado() {
        if (totalCobrado != null) return totalCobrado;
        if (cobrador == null || fecha == null) return null;
        Query query = entityManager().createQuery("select sum(p.importe) from Pago p where p.cobrador.id = :idCobrador and p.fechaPago = :fechaPago", BigDecimal.class);
        query.setParameter("idCobrador", cobrador.getId());
        query.setParameter("fechaPago", fecha);
        List<BigDecimal> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            totalCobrado = resultList.get(0);
        }
        return totalCobrado;
    }

    @Transient
    public BigDecimal getDiferencia() {
        BigDecimal diferencia = new BigDecimal(0);
        if (montoEntregado != null) {
            diferencia = diferencia.add(montoEntregado);
        }
        if (getTotalCobrado() != null) {
            diferencia = diferencia.add(getTotalCobrado());
        }
        if (montoDevuelto != null) {
            diferencia = diferencia.subtract(montoDevuelto);
        }
        return diferencia;
    }
}
