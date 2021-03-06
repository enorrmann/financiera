// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ar.com.norrmann.financiera.model;

import ar.com.norrmann.financiera.model.TipoCredito;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect TipoCredito_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager TipoCredito.entityManager;
    
    public static final EntityManager TipoCredito.entityManager() {
        EntityManager em = new TipoCredito().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long TipoCredito.countTipoCreditoes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TipoCredito o", Long.class).getSingleResult();
    }
    
    public static List<TipoCredito> TipoCredito.findAllTipoCreditoes() {
        return entityManager().createQuery("SELECT o FROM TipoCredito o", TipoCredito.class).getResultList();
    }
    
    public static TipoCredito TipoCredito.findTipoCredito(Long id) {
        if (id == null) return null;
        return entityManager().find(TipoCredito.class, id);
    }
    
    public static List<TipoCredito> TipoCredito.findTipoCreditoEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TipoCredito o", TipoCredito.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void TipoCredito.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void TipoCredito.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            TipoCredito attached = TipoCredito.findTipoCredito(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void TipoCredito.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void TipoCredito.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public TipoCredito TipoCredito.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TipoCredito merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
