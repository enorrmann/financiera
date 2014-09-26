package ar.com.norrmann.financiera.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import ar.com.norrmann.financiera.model.Credito;
import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = Credito.class)
public class CreditoConverter {
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value instanceof Credito && ((Credito) value).getId()!=null ? ((Credito) value).getId().toString() : "";
    }

}
