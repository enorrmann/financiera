// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ar.com.norrmann.financiera.web.converter;

import ar.com.norrmann.financiera.model.InteresPunitorio;
import ar.com.norrmann.financiera.web.converter.InteresPunitorioConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

privileged aspect InteresPunitorioConverter_Roo_Converter {
    
    declare parents: InteresPunitorioConverter implements Converter;
    
    declare @type: InteresPunitorioConverter: @FacesConverter("ar.com.norrmann.financiera.web.converter.InteresPunitorioConverter");
    
    public Object InteresPunitorioConverter.getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        Long id = Long.parseLong(value);
        return InteresPunitorio.findInteresPunitorio(id);
    }
    
    public String InteresPunitorioConverter.getAsString(FacesContext context, UIComponent component, Object value) {
        return value instanceof InteresPunitorio ? ((InteresPunitorio) value).getId().toString() : "";
    }
    
}
