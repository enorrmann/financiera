package ar.com.norrmann.financiera.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import ar.com.norrmann.financiera.model.Zona;
import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = Zona.class)
public class ZonaConverter {
    public String ZonagetAsString(FacesContext context, UIComponent component, Object value) {
        return value instanceof Zona && ((Zona) value).getId()!=null? ((Zona) value).getId().toString() : "";
    }

}
