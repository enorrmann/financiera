// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ar.com.norrmann.financiera.web.converter;

import ar.com.norrmann.financiera.model.Usuario;
import ar.com.norrmann.financiera.web.converter.UsuarioConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

privileged aspect UsuarioConverter_Roo_Converter {
    
    declare parents: UsuarioConverter implements Converter;
    
    declare @type: UsuarioConverter: @FacesConverter("ar.com.norrmann.financiera.web.converter.UsuarioConverter");
    
    public Object UsuarioConverter.getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        Long id = Long.parseLong(value);
        return Usuario.findUsuario(id);
    }
    
    public String UsuarioConverter.getAsString(FacesContext context, UIComponent component, Object value) {
        return value instanceof Usuario ? ((Usuario) value).getId().toString() : "";
    }
    
}
