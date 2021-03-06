// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ar.com.norrmann.financiera.web;

import ar.com.norrmann.financiera.model.Credito;
import ar.com.norrmann.financiera.model.Cuota;
import ar.com.norrmann.financiera.web.CuotaBean;
import ar.com.norrmann.financiera.web.converter.CreditoConverter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.LongRangeValidator;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.message.Message;
import org.primefaces.component.spinner.Spinner;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;

privileged aspect CuotaBean_Roo_ManagedBean {
    
    declare @type: CuotaBean: @ManagedBean(name = "cuotaBean");
    
    declare @type: CuotaBean: @SessionScoped;
    
    private String CuotaBean.name = "Cuotas";
    
    private Cuota CuotaBean.cuota;
    
    private List<Cuota> CuotaBean.allCuotas;
    
    private boolean CuotaBean.dataVisible = false;
    
    private List<String> CuotaBean.columns;
    
    private HtmlPanelGrid CuotaBean.createPanelGrid;
    
    private HtmlPanelGrid CuotaBean.editPanelGrid;
    
    private HtmlPanelGrid CuotaBean.viewPanelGrid;
    
    private boolean CuotaBean.createDialogVisible = false;
    
    @PostConstruct
    public void CuotaBean.init() {
        columns = new ArrayList<String>();
        columns.add("fechaVencimiento");
        columns.add("importe");
        columns.add("numero");
        columns.add("pagada");
    }
    
    public String CuotaBean.getName() {
        return name;
    }
    
    public List<String> CuotaBean.getColumns() {
        return columns;
    }
    
    public List<Cuota> CuotaBean.getAllCuotas() {
        return allCuotas;
    }
    
    public void CuotaBean.setAllCuotas(List<Cuota> allCuotas) {
        this.allCuotas = allCuotas;
    }
    
    public String CuotaBean.findAllCuotas() {
        allCuotas = Cuota.findAllCuotas();
        dataVisible = !allCuotas.isEmpty();
        return null;
    }
    
    public boolean CuotaBean.isDataVisible() {
        return dataVisible;
    }
    
    public void CuotaBean.setDataVisible(boolean dataVisible) {
        this.dataVisible = dataVisible;
    }
    
    public HtmlPanelGrid CuotaBean.getCreatePanelGrid() {
        if (createPanelGrid == null) {
            createPanelGrid = populateCreatePanel();
        }
        return createPanelGrid;
    }
    
    public void CuotaBean.setCreatePanelGrid(HtmlPanelGrid createPanelGrid) {
        this.createPanelGrid = createPanelGrid;
    }
    
    public HtmlPanelGrid CuotaBean.getEditPanelGrid() {
        if (editPanelGrid == null) {
            editPanelGrid = populateEditPanel();
        }
        return editPanelGrid;
    }
    
    public void CuotaBean.setEditPanelGrid(HtmlPanelGrid editPanelGrid) {
        this.editPanelGrid = editPanelGrid;
    }
    
    public HtmlPanelGrid CuotaBean.getViewPanelGrid() {
        return populateViewPanel();
    }
    
    public void CuotaBean.setViewPanelGrid(HtmlPanelGrid viewPanelGrid) {
        this.viewPanelGrid = viewPanelGrid;
    }
    
    public HtmlPanelGrid CuotaBean.populateEditPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText creditoEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        creditoEditOutput.setId("creditoEditOutput");
        creditoEditOutput.setValue("Credito: * ");
        htmlPanelGrid.getChildren().add(creditoEditOutput);
        
        AutoComplete creditoEditInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        creditoEditInput.setId("creditoEditInput");
        creditoEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{cuotaBean.cuota.credito}", Credito.class));
        creditoEditInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{cuotaBean.completeCredito}", List.class, new Class[] { String.class }));
        creditoEditInput.setDropdown(true);
        creditoEditInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "credito", String.class));
        creditoEditInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{credito.tasa} #{credito.montoDeCuota} #{credito.idEmpresa} #{credito.fechaSolicitud}", String.class));
        creditoEditInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{credito}", Credito.class));
        creditoEditInput.setConverter(new CreditoConverter());
        creditoEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(creditoEditInput);
        
        Message creditoEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        creditoEditInputMessage.setId("creditoEditInputMessage");
        creditoEditInputMessage.setFor("creditoEditInput");
        creditoEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(creditoEditInputMessage);
        
        HtmlOutputText fechaVencimientoEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        fechaVencimientoEditOutput.setId("fechaVencimientoEditOutput");
        fechaVencimientoEditOutput.setValue("Fecha Vencimiento: * ");
        htmlPanelGrid.getChildren().add(fechaVencimientoEditOutput);
        
        Calendar fechaVencimientoEditInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        fechaVencimientoEditInput.setId("fechaVencimientoEditInput");
        fechaVencimientoEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{cuotaBean.cuota.fechaVencimiento}", Date.class));
        fechaVencimientoEditInput.setNavigator(true);
        fechaVencimientoEditInput.setEffect("slideDown");
        fechaVencimientoEditInput.setPattern("dd/MM/yyyy");
        fechaVencimientoEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(fechaVencimientoEditInput);
        
        Message fechaVencimientoEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        fechaVencimientoEditInputMessage.setId("fechaVencimientoEditInputMessage");
        fechaVencimientoEditInputMessage.setFor("fechaVencimientoEditInput");
        fechaVencimientoEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(fechaVencimientoEditInputMessage);
        
        HtmlOutputText importeEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        importeEditOutput.setId("importeEditOutput");
        importeEditOutput.setValue("Importe: * ");
        htmlPanelGrid.getChildren().add(importeEditOutput);
        
        InputText importeEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        importeEditInput.setId("importeEditInput");
        importeEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{cuotaBean.cuota.importe}", BigDecimal.class));
        importeEditInput.setRequired(true);
        DoubleRangeValidator importeEditInputValidator = new DoubleRangeValidator();
        importeEditInputValidator.setMinimum(0.01);
        importeEditInput.addValidator(importeEditInputValidator);
        htmlPanelGrid.getChildren().add(importeEditInput);
        
        Message importeEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        importeEditInputMessage.setId("importeEditInputMessage");
        importeEditInputMessage.setFor("importeEditInput");
        importeEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(importeEditInputMessage);
        
        HtmlOutputText numeroEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        numeroEditOutput.setId("numeroEditOutput");
        numeroEditOutput.setValue("Numero: * ");
        htmlPanelGrid.getChildren().add(numeroEditOutput);
        
        Spinner numeroEditInput = (Spinner) application.createComponent(Spinner.COMPONENT_TYPE);
        numeroEditInput.setId("numeroEditInput");
        numeroEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{cuotaBean.cuota.numero}", Integer.class));
        numeroEditInput.setRequired(true);
        numeroEditInput.setMin(1.0);
        LongRangeValidator numeroEditInputValidator = new LongRangeValidator();
        numeroEditInputValidator.setMinimum(1);
        numeroEditInput.addValidator(numeroEditInputValidator);
        
        htmlPanelGrid.getChildren().add(numeroEditInput);
        
        Message numeroEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        numeroEditInputMessage.setId("numeroEditInputMessage");
        numeroEditInputMessage.setFor("numeroEditInput");
        numeroEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(numeroEditInputMessage);
        
        HtmlOutputText pagadaEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        pagadaEditOutput.setId("pagadaEditOutput");
        pagadaEditOutput.setValue("Pagada:   ");
        htmlPanelGrid.getChildren().add(pagadaEditOutput);
        
        Spinner pagadaEditInput = (Spinner) application.createComponent(Spinner.COMPONENT_TYPE);
        pagadaEditInput.setId("pagadaEditInput");
        pagadaEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{cuotaBean.cuota.pagada}", Long.class));
        pagadaEditInput.setRequired(false);
        
        htmlPanelGrid.getChildren().add(pagadaEditInput);
        
        Message pagadaEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        pagadaEditInputMessage.setId("pagadaEditInputMessage");
        pagadaEditInputMessage.setFor("pagadaEditInput");
        pagadaEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(pagadaEditInputMessage);
        
        return htmlPanelGrid;
    }
    
    public HtmlPanelGrid CuotaBean.populateViewPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText creditoLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        creditoLabel.setId("creditoLabel");
        creditoLabel.setValue("Credito:   ");
        htmlPanelGrid.getChildren().add(creditoLabel);
        
        HtmlOutputText creditoValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        creditoValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{cuotaBean.cuota.credito}", Credito.class));
        creditoValue.setConverter(new CreditoConverter());
        htmlPanelGrid.getChildren().add(creditoValue);
        
        HtmlOutputText fechaVencimientoLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        fechaVencimientoLabel.setId("fechaVencimientoLabel");
        fechaVencimientoLabel.setValue("Fecha Vencimiento:   ");
        htmlPanelGrid.getChildren().add(fechaVencimientoLabel);
        
        HtmlOutputText fechaVencimientoValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        fechaVencimientoValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{cuotaBean.cuota.fechaVencimiento}", Date.class));
        DateTimeConverter fechaVencimientoValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        fechaVencimientoValueConverter.setPattern("dd/MM/yyyy");
        fechaVencimientoValue.setConverter(fechaVencimientoValueConverter);
        htmlPanelGrid.getChildren().add(fechaVencimientoValue);
        
        HtmlOutputText importeLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        importeLabel.setId("importeLabel");
        importeLabel.setValue("Importe:   ");
        htmlPanelGrid.getChildren().add(importeLabel);
        
        HtmlOutputText importeValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        importeValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{cuotaBean.cuota.importe}", String.class));
        htmlPanelGrid.getChildren().add(importeValue);
        
        HtmlOutputText numeroLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        numeroLabel.setId("numeroLabel");
        numeroLabel.setValue("Numero:   ");
        htmlPanelGrid.getChildren().add(numeroLabel);
        
        HtmlOutputText numeroValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        numeroValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{cuotaBean.cuota.numero}", String.class));
        htmlPanelGrid.getChildren().add(numeroValue);
        
        HtmlOutputText pagadaLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        pagadaLabel.setId("pagadaLabel");
        pagadaLabel.setValue("Pagada:   ");
        htmlPanelGrid.getChildren().add(pagadaLabel);
        
        HtmlOutputText pagadaValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        pagadaValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{cuotaBean.cuota.pagada}", String.class));
        htmlPanelGrid.getChildren().add(pagadaValue);
        
        return htmlPanelGrid;
    }
    
    public Cuota CuotaBean.getCuota() {
        if (cuota == null) {
            cuota = new Cuota();
        }
        return cuota;
    }
    
    public void CuotaBean.setCuota(Cuota cuota) {
        this.cuota = cuota;
    }
    
    public List<Credito> CuotaBean.completeCredito(String query) {
        List<Credito> suggestions = new ArrayList<Credito>();
        for (Credito credito : Credito.findAllCreditoes()) {
            String creditoStr = String.valueOf(credito.getTasa() +  " "  + credito.getMontoDeCuota() +  " "  + credito.getIdEmpresa() +  " "  + credito.getFechaSolicitud());
            if (creditoStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(credito);
            }
        }
        return suggestions;
    }
    
    public String CuotaBean.onEdit() {
        return null;
    }
    
    public boolean CuotaBean.isCreateDialogVisible() {
        return createDialogVisible;
    }
    
    public void CuotaBean.setCreateDialogVisible(boolean createDialogVisible) {
        this.createDialogVisible = createDialogVisible;
    }
    
    public String CuotaBean.displayList() {
        createDialogVisible = false;
        findAllCuotas();
        return "cuota";
    }
    
    public String CuotaBean.displayCreateDialog() {
        cuota = new Cuota();
        createDialogVisible = true;
        return "cuota";
    }
    
    public String CuotaBean.persist() {
        String message = "";
        if (cuota.getId() != null) {
            cuota.merge();
            message = "Successfully updated";
        } else {
            cuota.persist();
            message = "Successfully created";
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialog.hide()");
        context.execute("editDialog.hide()");
        
        FacesMessage facesMessage = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllCuotas();
    }
    
    public String CuotaBean.delete() {
        cuota.remove();
        FacesMessage facesMessage = new FacesMessage("Successfully deleted");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllCuotas();
    }
    
    public void CuotaBean.reset() {
        cuota = null;
        createDialogVisible = false;
    }
    
    public void CuotaBean.handleDialogClose(CloseEvent event) {
        reset();
    }
    
}
