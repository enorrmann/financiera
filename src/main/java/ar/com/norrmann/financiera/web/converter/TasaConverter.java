package ar.com.norrmann.financiera.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import ar.com.norrmann.financiera.model.Tasa;
import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = Tasa.class)
public class TasaConverter {
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value instanceof Tasa && ((Tasa) value).getId()!=null? ((Tasa) value).getId().toString() : "";
    }

}
