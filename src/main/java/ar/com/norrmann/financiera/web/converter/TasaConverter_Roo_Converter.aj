// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ar.com.norrmann.financiera.web.converter;

import ar.com.norrmann.financiera.model.Tasa;
import ar.com.norrmann.financiera.web.converter.TasaConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

privileged aspect TasaConverter_Roo_Converter {
    
    declare parents: TasaConverter implements Converter;
    
    declare @type: TasaConverter: @FacesConverter("ar.com.norrmann.financiera.web.converter.TasaConverter");
    
    public Object TasaConverter.getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        Long id = Long.parseLong(value);
        return Tasa.findTasa(id);
    }
    
}