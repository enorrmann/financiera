package ar.com.norrmann.financiera.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooSerializable
@RooJpaActiveRecord(finders = { "findCobradorsByZona" })
public class Cobrador {

    @NotNull
    private String apellidos;

    @NotNull
    private String nombres;

    @ManyToOne
    private Zona zona;

    public String getNombreCompleto() {
        return getApellidos() + ", " + getNombres();
    }

    public static List<ar.com.norrmann.financiera.model.Cobrador> findAllCobradors() {
        return entityManager().createQuery("SELECT o FROM Cobrador o order by o.apellidos,o.nombres", Cobrador.class).getResultList();
    }
    public static Cobrador findCobradorByZona(Zona zona) {
        if (zona == null) throw new IllegalArgumentException("The zona argument is required");
        EntityManager em = Cobrador.entityManager();
        TypedQuery<Cobrador> q = em.createQuery("SELECT o FROM Cobrador AS o WHERE o.zona = :zona", Cobrador.class);
        q.setParameter("zona", zona);
        q.setMaxResults(1);
        List<Cobrador> cobradorList = q.getResultList();
        if (cobradorList!=null&&!cobradorList.isEmpty()){
        	return cobradorList.get(0);
        } else {
        	return null;
        }
    }
}
