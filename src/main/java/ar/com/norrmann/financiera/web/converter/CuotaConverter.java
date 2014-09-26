package ar.com.norrmann.financiera.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import ar.com.norrmann.financiera.model.Cuota;
import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = Cuota.class)
public class CuotaConverter {
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value instanceof Cuota && ((Cuota) value).getId()!=null? ((Cuota) value).getId().toString() : "";
    }

}
