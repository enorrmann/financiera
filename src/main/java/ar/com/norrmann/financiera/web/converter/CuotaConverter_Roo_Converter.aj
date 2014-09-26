// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ar.com.norrmann.financiera.web.converter;

import ar.com.norrmann.financiera.model.Cuota;
import ar.com.norrmann.financiera.web.converter.CuotaConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

privileged aspect CuotaConverter_Roo_Converter {
    
    declare parents: CuotaConverter implements Converter;
    
    declare @type: CuotaConverter: @FacesConverter("ar.com.norrmann.financiera.web.converter.CuotaConverter");
    
    public Object CuotaConverter.getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        Long id = Long.parseLong(value);
        return Cuota.findCuota(id);
    }
    
}