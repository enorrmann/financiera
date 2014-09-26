package ar.com.norrmann.financiera.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import ar.com.norrmann.financiera.model.Cobrador;
import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = Cobrador.class)
public class CobradorConverter {
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value instanceof Cobrador && ((Cobrador) value).getId()!=null? ((Cobrador) value).getId().toString() : "";
    }

}
