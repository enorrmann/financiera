package ar.com.norrmann.financiera.web;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import ar.com.norrmann.financiera.model.Tasa;
import ar.com.norrmann.financiera.web.util.I18N;

import org.primefaces.context.RequestContext;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = Tasa.class, beanName = "tasaBean")
public class TasaBean {
    private Tasa tasa;
    public Tasa getTasa() {
        if (tasa == null) {
            tasa = new Tasa();
        }
        return tasa;
    }
    
    public void setTasa(Tasa tasa) {
        this.tasa = tasa;
    }
    public String persist() {
        String message = "";
        if (tasa.getId() != null) {
            tasa.merge();
            message = I18N.ACTUALIZADO_OK;
        } else {
            tasa.persist();
            message = I18N.CREADO_OK;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialog.hide()");
        context.execute("editDialog.hide()");
        
        FacesMessage facesMessage = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllTasas();
    }
    
    public String delete() {
        tasa.remove();
        FacesMessage facesMessage = new FacesMessage(I18N.BORRADO_OK);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllTasas();
    }

}
