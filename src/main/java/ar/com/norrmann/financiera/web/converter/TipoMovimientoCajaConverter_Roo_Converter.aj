// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ar.com.norrmann.financiera.web.converter;

import ar.com.norrmann.financiera.model.TipoMovimientoCaja;
import ar.com.norrmann.financiera.web.converter.TipoMovimientoCajaConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

privileged aspect TipoMovimientoCajaConverter_Roo_Converter {
    
    declare parents: TipoMovimientoCajaConverter implements Converter;
    
    declare @type: TipoMovimientoCajaConverter: @FacesConverter("ar.com.norrmann.financiera.web.converter.TipoMovimientoCajaConverter");
    
    public Object TipoMovimientoCajaConverter.getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        Long id = Long.parseLong(value);
        return TipoMovimientoCaja.findTipoMovimientoCaja(id);
    }
    
    public String TipoMovimientoCajaConverter.getAsString(FacesContext context, UIComponent component, Object value) {
        return value instanceof TipoMovimientoCaja ? ((TipoMovimientoCaja) value).getId().toString() : "";
    }
    
}
