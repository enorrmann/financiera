// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ar.com.norrmann.financiera.web.converter;

import ar.com.norrmann.financiera.model.Credito;
import ar.com.norrmann.financiera.web.converter.CreditoConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

privileged aspect CreditoConverter_Roo_Converter {
    
    declare parents: CreditoConverter implements Converter;
    
    declare @type: CreditoConverter: @FacesConverter("ar.com.norrmann.financiera.web.converter.CreditoConverter");
    
    public Object CreditoConverter.getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        Long id = Long.parseLong(value);
        return Credito.findCredito(id);
    }
    
}
