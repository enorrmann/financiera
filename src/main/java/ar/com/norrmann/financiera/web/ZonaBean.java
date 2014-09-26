package ar.com.norrmann.financiera.web;

import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;

import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.message.Message;
import org.primefaces.context.RequestContext;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import ar.com.norrmann.financiera.model.Zona;
import ar.com.norrmann.financiera.web.converter.ZonaConverter;
import ar.com.norrmann.financiera.web.util.I18N;

@RooSerializable
@RooJsfManagedBean(entity = Zona.class, beanName = "zonaBean")
public class ZonaBean {
    private Zona zona;
    public Zona getZona() {
        if (zona == null) {
            zona = new Zona();
        }
        return zona;
    }
    
    public void setZona(Zona zona) {
        this.zona = zona;
    }
    public String persist() {
        String message = "";
        if (zona.getId() != null) {
            zona.merge();
            message = I18N.ACTUALIZADO_OK;
        } else {
            zona.persist();
            message = I18N.CREADO_OK;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialog.hide()");
        context.execute("editDialog.hide()");
        
        FacesMessage facesMessage = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllZonas();
    }
    
    public String delete() {
        zona.remove();
        FacesMessage facesMessage = new FacesMessage(I18N.BORRADO_OK);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllZonas();
    }
    public HtmlPanelGrid populateCreatePanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText zonaCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        zonaCreateOutput.setId("zonaCreateOutput");
        zonaCreateOutput.setValue("Zona: * ");
        htmlPanelGrid.getChildren().add(zonaCreateOutput);
        
        InputText zonaCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        zonaCreateInput.setId("zonaCreateInput");
        zonaCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{zonaBean.zona.zona}", String.class));
        zonaCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(zonaCreateInput);
        
        Message zonaCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        zonaCreateInputMessage.setId("zonaCreateInputMessage");
        zonaCreateInputMessage.setFor("zonaCreateInput");
        zonaCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(zonaCreateInputMessage);
        
        HtmlOutputText descripcionCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        descripcionCreateOutput.setId("descripcionCreateOutput");
        descripcionCreateOutput.setValue("Descripcion:   ");
        htmlPanelGrid.getChildren().add(descripcionCreateOutput);
        
        InputText descripcionCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        descripcionCreateInput.setId("descripcionCreateInput");
        descripcionCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{zonaBean.zona.descripcion}", String.class));
        htmlPanelGrid.getChildren().add(descripcionCreateInput);
        
        Message descripcionCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        descripcionCreateInputMessage.setId("descripcionCreateInputMessage");
        descripcionCreateInputMessage.setFor("descripcionCreateInput");
        descripcionCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(descripcionCreateInputMessage);
        
        return htmlPanelGrid;
    }
    
    public ZonaConverter getZonaConverter(){
    	return new ZonaConverter();
    }
    public List<Zona> getZonaAll(){
    	return Zona.findAllZonas();
    }
}
