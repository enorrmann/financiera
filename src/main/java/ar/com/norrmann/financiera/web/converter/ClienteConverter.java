package ar.com.norrmann.financiera.web.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

import ar.com.norrmann.financiera.model.Cliente;

@RooJsfConverter(entity = Cliente.class)
public class ClienteConverter {
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return value instanceof Cliente && ((Cliente) value).getId() != null ? ((Cliente) value)
				.getId().toString() : "";
	}

	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (value == null || value.length() == 0) {
			return null;
		}

		Long id = null;
		try {
			id = Long.parseLong(value);
		} catch (NumberFormatException e) {
			FacesMessage msg = new FacesMessage("Por favor, seleccione un cliente de la lista");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(msg);
		}

		return Cliente.findCliente(id);
	}

}
