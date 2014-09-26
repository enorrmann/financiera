package ar.com.norrmann.financiera.web;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.message.Message;
import org.primefaces.context.RequestContext;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import ar.com.norrmann.financiera.model.Cobrador;
import ar.com.norrmann.financiera.model.ControlCobrador;
import ar.com.norrmann.financiera.model.Pago;
import ar.com.norrmann.financiera.web.converter.CobradorConverter;
import ar.com.norrmann.financiera.web.util.I18N;

@RooSerializable
@RooJsfManagedBean(entity = ControlCobrador.class, beanName = "controlCobradorBean")
public class ControlCobradorBean {
	
	private FiltroCuotaList filtroControl = new FiltroCuotaList();
	
	public HtmlPanelGrid populateCreatePanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance(); 
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory(); 
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        

        
        HtmlOutputText cobradorCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        cobradorCreateOutput.setId("cobradorCreateOutput");
        cobradorCreateOutput.setValue("Cobrador: * ");
        htmlPanelGrid.getChildren().add(cobradorCreateOutput);
        
        AutoComplete cobradorCreateInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        cobradorCreateInput.setId("cobradorCreateInput");
        cobradorCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{controlCobradorBean.controlCobrador.cobrador}", Cobrador.class));
        cobradorCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{controlCobradorBean.completeCobrador}", List.class, new Class[] { String.class }));
        cobradorCreateInput.setDropdown(true);
        cobradorCreateInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "cobrador", String.class));
        cobradorCreateInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{cobrador.apellidos} #{cobrador.nombres}", String.class));
        cobradorCreateInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{cobrador}", Cobrador.class));
        cobradorCreateInput.setConverter(new CobradorConverter());
        cobradorCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(cobradorCreateInput);
        
        Message cobradorCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        cobradorCreateInputMessage.setId("cobradorCreateInputMessage");
        cobradorCreateInputMessage.setFor("cobradorCreateInput");
        cobradorCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(cobradorCreateInputMessage);
        
        HtmlOutputText fechaCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        fechaCreateOutput.setId("fechaCreateOutput");
        fechaCreateOutput.setValue("Fecha:   ");
        htmlPanelGrid.getChildren().add(fechaCreateOutput);
        
        Calendar fechaCreateInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        fechaCreateInput.setId("fechaCreateInput");
        fechaCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{controlCobradorBean.controlCobrador.fecha}", Date.class));
        fechaCreateInput.setNavigator(true);
        fechaCreateInput.setEffect("slideDown");
        fechaCreateInput.setPattern("dd/MM/yyyy");
        fechaCreateInput.setRequired(false);
        htmlPanelGrid.getChildren().add(fechaCreateInput);
        
        Message fechaCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        fechaCreateInputMessage.setId("fechaCreateInputMessage");
        fechaCreateInputMessage.setFor("fechaCreateInput");
        fechaCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(fechaCreateInputMessage);
        
        HtmlOutputText montoEntregadoCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        montoEntregadoCreateOutput.setId("montoEntregadoCreateOutput");
        montoEntregadoCreateOutput.setValue("Monto Entregado:   ");
        htmlPanelGrid.getChildren().add(montoEntregadoCreateOutput);
        
        InputText montoEntregadoCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        montoEntregadoCreateInput.setId("montoEntregadoCreateInput");
        montoEntregadoCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{controlCobradorBean.controlCobrador.montoEntregado}", BigDecimal.class));
        montoEntregadoCreateInput.setRequired(false);
        htmlPanelGrid.getChildren().add(montoEntregadoCreateInput);
        
        Message montoEntregadoCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        montoEntregadoCreateInputMessage.setId("montoEntregadoCreateInputMessage");
        montoEntregadoCreateInputMessage.setFor("montoEntregadoCreateInput");
        montoEntregadoCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(montoEntregadoCreateInputMessage);
        
        HtmlOutputText montoDevueltoCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        montoDevueltoCreateOutput.setId("montoDevueltoCreateOutput");
        montoDevueltoCreateOutput.setValue("Monto Devuelto:   ");
        htmlPanelGrid.getChildren().add(montoDevueltoCreateOutput);
        
        InputText montoDevueltoCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        montoDevueltoCreateInput.setId("montoDevueltoCreateInput");
        montoDevueltoCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{controlCobradorBean.controlCobrador.montoDevuelto}", BigDecimal.class));
        montoDevueltoCreateInput.setRequired(false);
        htmlPanelGrid.getChildren().add(montoDevueltoCreateInput);
        
        Message montoDevueltoCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        montoDevueltoCreateInputMessage.setId("montoDevueltoCreateInputMessage");
        montoDevueltoCreateInputMessage.setFor("montoDevueltoCreateInput");
        montoDevueltoCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(montoDevueltoCreateInputMessage);
        
        return htmlPanelGrid;
    }

	public HtmlPanelGrid populateEditPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        
        HtmlOutputText cobradorEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        cobradorEditOutput.setId("cobradorEditOutput");
        cobradorEditOutput.setValue("Cobrador: * ");
        htmlPanelGrid.getChildren().add(cobradorEditOutput);
        
        AutoComplete cobradorEditInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        cobradorEditInput.setId("cobradorEditInput");
        cobradorEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{controlCobradorBean.controlCobrador.cobrador}", Cobrador.class));
        cobradorEditInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{controlCobradorBean.completeCobrador}", List.class, new Class[] { String.class }));
        cobradorEditInput.setDropdown(true);
        cobradorEditInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "cobrador", String.class));
        cobradorEditInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{cobrador.apellidos} #{cobrador.nombres}", String.class));
        cobradorEditInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{cobrador}", Cobrador.class));
        cobradorEditInput.setConverter(new CobradorConverter());
        cobradorEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(cobradorEditInput);
        
        Message cobradorEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        cobradorEditInputMessage.setId("cobradorEditInputMessage");
        cobradorEditInputMessage.setFor("cobradorEditInput");
        cobradorEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(cobradorEditInputMessage);
        
        HtmlOutputText fechaEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        fechaEditOutput.setId("fechaEditOutput");
        fechaEditOutput.setValue("Fecha:   ");
        htmlPanelGrid.getChildren().add(fechaEditOutput);
        
        Calendar fechaEditInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        fechaEditInput.setId("fechaEditInput");
        fechaEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{controlCobradorBean.controlCobrador.fecha}", Date.class));
        fechaEditInput.setNavigator(true);
        fechaEditInput.setEffect("slideDown");
        fechaEditInput.setPattern("dd/MM/yyyy");
        fechaEditInput.setRequired(false);
        htmlPanelGrid.getChildren().add(fechaEditInput);
        
        Message fechaEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        fechaEditInputMessage.setId("fechaEditInputMessage");
        fechaEditInputMessage.setFor("fechaEditInput");
        fechaEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(fechaEditInputMessage);

        HtmlOutputText montoEntregadoEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        montoEntregadoEditOutput.setId("montoEntregadoEditOutput");
        montoEntregadoEditOutput.setValue("Monto Entregado:   ");
        htmlPanelGrid.getChildren().add(montoEntregadoEditOutput);
        
        InputText montoEntregadoEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        montoEntregadoEditInput.setId("montoEntregadoEditInput");
        montoEntregadoEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{controlCobradorBean.controlCobrador.montoEntregado}", BigDecimal.class));
        montoEntregadoEditInput.setRequired(false);
        htmlPanelGrid.getChildren().add(montoEntregadoEditInput);
        
        Message montoEntregadoEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        montoEntregadoEditInputMessage.setId("montoEntregadoEditInputMessage");
        montoEntregadoEditInputMessage.setFor("montoEntregadoEditInput");
        montoEntregadoEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(montoEntregadoEditInputMessage);
        
        HtmlOutputText montoDevueltoEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        montoDevueltoEditOutput.setId("montoDevueltoEditOutput");
        montoDevueltoEditOutput.setValue("Monto Devuelto:   ");
        htmlPanelGrid.getChildren().add(montoDevueltoEditOutput);
        
        InputText montoDevueltoEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        montoDevueltoEditInput.setId("montoDevueltoEditInput");
        montoDevueltoEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{controlCobradorBean.controlCobrador.montoDevuelto}", BigDecimal.class));
        montoDevueltoEditInput.setRequired(false);
        htmlPanelGrid.getChildren().add(montoDevueltoEditInput);
        
        Message montoDevueltoEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        montoDevueltoEditInputMessage.setId("montoDevueltoEditInputMessage");
        montoDevueltoEditInputMessage.setFor("montoDevueltoEditInput");
        montoDevueltoEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(montoDevueltoEditInputMessage);
        
        return htmlPanelGrid;
    }

	public HtmlPanelGrid populateViewPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText fechaLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        fechaLabel.setId("fechaLabel");
        fechaLabel.setValue("Fecha:   ");
        htmlPanelGrid.getChildren().add(fechaLabel);
        
        HtmlOutputText fechaValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        fechaValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{controlCobradorBean.controlCobrador.fecha}", Date.class));
        DateTimeConverter fechaValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        fechaValueConverter.setPattern("dd/MM/yyyy");
        fechaValue.setConverter(fechaValueConverter);
        htmlPanelGrid.getChildren().add(fechaValue);
        
        HtmlOutputText cobradorLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        cobradorLabel.setId("cobradorLabel");
        cobradorLabel.setValue("Cobrador:   ");
        htmlPanelGrid.getChildren().add(cobradorLabel);
        
        HtmlOutputText cobradorValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        cobradorValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{controlCobradorBean.controlCobrador.cobrador}", Cobrador.class));
        cobradorValue.setConverter(new CobradorConverter());
        htmlPanelGrid.getChildren().add(cobradorValue);
        
        HtmlOutputText montoEntregadoLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        montoEntregadoLabel.setId("montoEntregadoLabel");
        montoEntregadoLabel.setValue("Monto Entregado:   ");
        htmlPanelGrid.getChildren().add(montoEntregadoLabel);
        
        HtmlOutputText montoEntregadoValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        montoEntregadoValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{controlCobradorBean.controlCobrador.montoEntregado}", String.class));
        htmlPanelGrid.getChildren().add(montoEntregadoValue);
        
        HtmlOutputText montoDevueltoLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        montoDevueltoLabel.setId("montoDevueltoLabel");
        montoDevueltoLabel.setValue("Monto Devuelto:   ");
        htmlPanelGrid.getChildren().add(montoDevueltoLabel);
        
        HtmlOutputText montoDevueltoValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        montoDevueltoValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{controlCobradorBean.controlCobrador.montoDevuelto}", String.class));
        htmlPanelGrid.getChildren().add(montoDevueltoValue);
        
        return htmlPanelGrid;
    }
    public void setSelectedPagoList(List<Pago> selectedPagoList) {
    }

	public FiltroCuotaList getFiltroControl() {
		return filtroControl;
	}

	public void setFiltroControl(FiltroCuotaList filtroControl) {
		this.filtroControl = filtroControl;
	}
	
	public String filtrarLista(){
		setCreateDialogVisible(false);
		setAllControlCobradors(ControlCobrador.findControlCobradorsByFechaEquals(filtroControl.getFechaHasta()).getResultList());
		setDataVisible(getAllControlCobradors()!=null&&!getAllControlCobradors().isEmpty());
		return "controlCobrador";
	}

	public String persist() {
		ControlCobrador controlCobrador = getControlCobrador();
		String message = "";
        if (controlCobrador.getId() != null) {
            controlCobrador.merge();
            message = I18N.ACTUALIZADO_OK;
        } else {
            controlCobrador.persist();
            message = I18N.CREADO_OK;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialog.hide()");
        context.execute("editDialog.hide()");
        
        FacesMessage facesMessage = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        filtroControl.setFechaHasta(controlCobrador.getFecha());
        reset();
        return filtrarLista();
    }
}
