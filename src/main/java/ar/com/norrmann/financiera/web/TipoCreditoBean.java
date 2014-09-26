package ar.com.norrmann.financiera.web;

import ar.com.norrmann.financiera.model.TipoCredito;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = TipoCredito.class, beanName = "tipoCreditoBean")
public class TipoCreditoBean {
}
