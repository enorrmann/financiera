package ar.com.norrmann.financiera.web;

import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;

import ar.com.norrmann.financiera.model.Cobrador;
import ar.com.norrmann.financiera.model.Zona;
import ar.com.norrmann.financiera.web.converter.ZonaConverter;
import ar.com.norrmann.financiera.web.util.I18N;

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.message.Message;
import org.primefaces.context.RequestContext;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = Cobrador.class, beanName = "cobradorBean")
public class CobradorBean {
    private Cobrador cobrador;

	private String name = "Cobradores";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    public String persist() {
        String message = "";
        if (cobrador.getId() != null) {
            cobrador.merge();
            message = I18N.ACTUALIZADO_OK;
        } else {
            cobrador.persist();
            message = I18N.CREADO_OK;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialog.hide()");
        context.execute("editDialog.hide()");
        
        FacesMessage facesMessage = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllCobradors();
    }
    
    public String delete() {
        cobrador.remove();
        FacesMessage facesMessage = new FacesMessage(I18N.BORRADO_OK);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllCobradors();
    }

    public Cobrador getCobrador() {
        if (cobrador == null) {
            cobrador = new Cobrador();
        }
        return cobrador;
    }
    
    public void setCobrador(Cobrador cobrador) {
        this.cobrador = cobrador;
    }

    public HtmlPanelGrid populateCreatePanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText apellidosCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        apellidosCreateOutput.setId("apellidosCreateOutput");
        apellidosCreateOutput.setValue("Apellidos: * ");
        
        htmlPanelGrid.getChildren().add(apellidosCreateOutput);
        
        InputText apellidosCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        apellidosCreateInput.setId("apellidosCreateInput");
        apellidosCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{cobradorBean.cobrador.apellidos}", String.class));
        apellidosCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(apellidosCreateInput);
        
        Message apellidosCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        apellidosCreateInputMessage.setId("apellidosCreateInputMessage");
        apellidosCreateInputMessage.setFor("apellidosCreateInput");
        apellidosCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(apellidosCreateInputMessage);
        
        HtmlOutputText nombresCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        nombresCreateOutput.setId("nombresCreateOutput");
        nombresCreateOutput.setValue("Nombres: * ");
        htmlPanelGrid.getChildren().add(nombresCreateOutput);
        
        InputText nombresCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        nombresCreateInput.setId("nombresCreateInput");
        nombresCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{cobradorBean.cobrador.nombres}", String.class));
        nombresCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(nombresCreateInput);
        
        Message nombresCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        nombresCreateInputMessage.setId("nombresCreateInputMessage");
        nombresCreateInputMessage.setFor("nombresCreateInput");
        nombresCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(nombresCreateInputMessage);
        
        HtmlOutputText zonaCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        zonaCreateOutput.setId("zonaCreateOutput");
        zonaCreateOutput.setValue("Zona:   ");
        htmlPanelGrid.getChildren().add(zonaCreateOutput);
        
        AutoComplete zonaCreateInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        zonaCreateInput.setId("zonaCreateInput");
        zonaCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{cobradorBean.cobrador.zona}", Zona.class));
        zonaCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{cobradorBean.completeZona}", List.class, new Class[] { String.class }));
        zonaCreateInput.setDropdown(true);
        zonaCreateInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "zona", String.class));
        zonaCreateInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{zona.zona} #{zona.descripcion}", String.class));
        zonaCreateInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{zona}", Zona.class));
        zonaCreateInput.setConverter(new ZonaConverter());
        zonaCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(zonaCreateInput);
        
        Message zonaCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        zonaCreateInputMessage.setId("zonaCreateInputMessage");
        zonaCreateInputMessage.setFor("zonaCreateInput");
        zonaCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(zonaCreateInputMessage);
        
        return htmlPanelGrid;
    }

}
