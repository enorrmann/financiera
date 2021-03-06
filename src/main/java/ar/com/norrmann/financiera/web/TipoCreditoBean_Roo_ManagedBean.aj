// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ar.com.norrmann.financiera.web;

import ar.com.norrmann.financiera.model.TipoCredito;
import ar.com.norrmann.financiera.web.TipoCreditoBean;
import java.util.ArrayList;
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
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.message.Message;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;

privileged aspect TipoCreditoBean_Roo_ManagedBean {
    
    declare @type: TipoCreditoBean: @ManagedBean(name = "tipoCreditoBean");
    
    declare @type: TipoCreditoBean: @SessionScoped;
    
    private String TipoCreditoBean.name = "TipoCreditoes";
    
    private TipoCredito TipoCreditoBean.tipoCredito;
    
    private List<TipoCredito> TipoCreditoBean.allTipoCreditoes;
    
    private boolean TipoCreditoBean.dataVisible = false;
    
    private List<String> TipoCreditoBean.columns;
    
    private HtmlPanelGrid TipoCreditoBean.createPanelGrid;
    
    private HtmlPanelGrid TipoCreditoBean.editPanelGrid;
    
    private HtmlPanelGrid TipoCreditoBean.viewPanelGrid;
    
    private boolean TipoCreditoBean.createDialogVisible = false;
    
    @PostConstruct
    public void TipoCreditoBean.init() {
        columns = new ArrayList<String>();
        columns.add("tipo");
        columns.add("descripcion");
    }
    
    public String TipoCreditoBean.getName() {
        return name;
    }
    
    public List<String> TipoCreditoBean.getColumns() {
        return columns;
    }
    
    public List<TipoCredito> TipoCreditoBean.getAllTipoCreditoes() {
        return allTipoCreditoes;
    }
    
    public void TipoCreditoBean.setAllTipoCreditoes(List<TipoCredito> allTipoCreditoes) {
        this.allTipoCreditoes = allTipoCreditoes;
    }
    
    public String TipoCreditoBean.findAllTipoCreditoes() {
        allTipoCreditoes = TipoCredito.findAllTipoCreditoes();
        dataVisible = !allTipoCreditoes.isEmpty();
        return null;
    }
    
    public boolean TipoCreditoBean.isDataVisible() {
        return dataVisible;
    }
    
    public void TipoCreditoBean.setDataVisible(boolean dataVisible) {
        this.dataVisible = dataVisible;
    }
    
    public HtmlPanelGrid TipoCreditoBean.getCreatePanelGrid() {
        if (createPanelGrid == null) {
            createPanelGrid = populateCreatePanel();
        }
        return createPanelGrid;
    }
    
    public void TipoCreditoBean.setCreatePanelGrid(HtmlPanelGrid createPanelGrid) {
        this.createPanelGrid = createPanelGrid;
    }
    
    public HtmlPanelGrid TipoCreditoBean.getEditPanelGrid() {
        if (editPanelGrid == null) {
            editPanelGrid = populateEditPanel();
        }
        return editPanelGrid;
    }
    
    public void TipoCreditoBean.setEditPanelGrid(HtmlPanelGrid editPanelGrid) {
        this.editPanelGrid = editPanelGrid;
    }
    
    public HtmlPanelGrid TipoCreditoBean.getViewPanelGrid() {
        return populateViewPanel();
    }
    
    public void TipoCreditoBean.setViewPanelGrid(HtmlPanelGrid viewPanelGrid) {
        this.viewPanelGrid = viewPanelGrid;
    }
    
    public HtmlPanelGrid TipoCreditoBean.populateCreatePanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText tipoCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        tipoCreateOutput.setId("tipoCreateOutput");
        tipoCreateOutput.setValue("Tipo:   ");
        htmlPanelGrid.getChildren().add(tipoCreateOutput);
        
        InputText tipoCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        tipoCreateInput.setId("tipoCreateInput");
        tipoCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{tipoCreditoBean.tipoCredito.tipo}", String.class));
        htmlPanelGrid.getChildren().add(tipoCreateInput);
        
        Message tipoCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        tipoCreateInputMessage.setId("tipoCreateInputMessage");
        tipoCreateInputMessage.setFor("tipoCreateInput");
        tipoCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(tipoCreateInputMessage);
        
        HtmlOutputText descripcionCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        descripcionCreateOutput.setId("descripcionCreateOutput");
        descripcionCreateOutput.setValue("Descripcion:   ");
        htmlPanelGrid.getChildren().add(descripcionCreateOutput);
        
        InputText descripcionCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        descripcionCreateInput.setId("descripcionCreateInput");
        descripcionCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{tipoCreditoBean.tipoCredito.descripcion}", String.class));
        htmlPanelGrid.getChildren().add(descripcionCreateInput);
        
        Message descripcionCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        descripcionCreateInputMessage.setId("descripcionCreateInputMessage");
        descripcionCreateInputMessage.setFor("descripcionCreateInput");
        descripcionCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(descripcionCreateInputMessage);
        
        return htmlPanelGrid;
    }
    
    public HtmlPanelGrid TipoCreditoBean.populateEditPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText tipoEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        tipoEditOutput.setId("tipoEditOutput");
        tipoEditOutput.setValue("Tipo:   ");
        htmlPanelGrid.getChildren().add(tipoEditOutput);
        
        InputText tipoEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        tipoEditInput.setId("tipoEditInput");
        tipoEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{tipoCreditoBean.tipoCredito.tipo}", String.class));
        htmlPanelGrid.getChildren().add(tipoEditInput);
        
        Message tipoEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        tipoEditInputMessage.setId("tipoEditInputMessage");
        tipoEditInputMessage.setFor("tipoEditInput");
        tipoEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(tipoEditInputMessage);
        
        HtmlOutputText descripcionEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        descripcionEditOutput.setId("descripcionEditOutput");
        descripcionEditOutput.setValue("Descripcion:   ");
        htmlPanelGrid.getChildren().add(descripcionEditOutput);
        
        InputText descripcionEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        descripcionEditInput.setId("descripcionEditInput");
        descripcionEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{tipoCreditoBean.tipoCredito.descripcion}", String.class));
        htmlPanelGrid.getChildren().add(descripcionEditInput);
        
        Message descripcionEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        descripcionEditInputMessage.setId("descripcionEditInputMessage");
        descripcionEditInputMessage.setFor("descripcionEditInput");
        descripcionEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(descripcionEditInputMessage);
        
        return htmlPanelGrid;
    }
    
    public HtmlPanelGrid TipoCreditoBean.populateViewPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText tipoLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        tipoLabel.setId("tipoLabel");
        tipoLabel.setValue("Tipo:   ");
        htmlPanelGrid.getChildren().add(tipoLabel);
        
        HtmlOutputText tipoValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        tipoValue.setId("tipoValue");
        tipoValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{tipoCreditoBean.tipoCredito.tipo}", String.class));
        htmlPanelGrid.getChildren().add(tipoValue);
        
        HtmlOutputText descripcionLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        descripcionLabel.setId("descripcionLabel");
        descripcionLabel.setValue("Descripcion:   ");
        htmlPanelGrid.getChildren().add(descripcionLabel);
        
        HtmlOutputText descripcionValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        descripcionValue.setId("descripcionValue");
        descripcionValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{tipoCreditoBean.tipoCredito.descripcion}", String.class));
        htmlPanelGrid.getChildren().add(descripcionValue);
        
        return htmlPanelGrid;
    }
    
    public TipoCredito TipoCreditoBean.getTipoCredito() {
        if (tipoCredito == null) {
            tipoCredito = new TipoCredito();
        }
        return tipoCredito;
    }
    
    public void TipoCreditoBean.setTipoCredito(TipoCredito tipoCredito) {
        this.tipoCredito = tipoCredito;
    }
    
    public String TipoCreditoBean.onEdit() {
        return null;
    }
    
    public boolean TipoCreditoBean.isCreateDialogVisible() {
        return createDialogVisible;
    }
    
    public void TipoCreditoBean.setCreateDialogVisible(boolean createDialogVisible) {
        this.createDialogVisible = createDialogVisible;
    }
    
    public String TipoCreditoBean.displayList() {
        createDialogVisible = false;
        findAllTipoCreditoes();
        return "tipoCredito";
    }
    
    public String TipoCreditoBean.displayCreateDialog() {
        tipoCredito = new TipoCredito();
        createDialogVisible = true;
        return "tipoCredito";
    }
    
    public String TipoCreditoBean.persist() {
        String message = "";
        if (tipoCredito.getId() != null) {
            tipoCredito.merge();
            message = "Successfully updated";
        } else {
            tipoCredito.persist();
            message = "Successfully created";
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialog.hide()");
        context.execute("editDialog.hide()");
        
        FacesMessage facesMessage = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllTipoCreditoes();
    }
    
    public String TipoCreditoBean.delete() {
        tipoCredito.remove();
        FacesMessage facesMessage = new FacesMessage("Successfully deleted");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllTipoCreditoes();
    }
    
    public void TipoCreditoBean.reset() {
        tipoCredito = null;
        createDialogVisible = false;
    }
    
    public void TipoCreditoBean.handleDialogClose(CloseEvent event) {
        reset();
    }
    
}
