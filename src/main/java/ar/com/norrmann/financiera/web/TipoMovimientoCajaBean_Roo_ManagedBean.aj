// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ar.com.norrmann.financiera.web;

import ar.com.norrmann.financiera.model.TipoMovimientoCaja;
import ar.com.norrmann.financiera.web.TipoMovimientoCajaBean;
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
import org.primefaces.component.spinner.Spinner;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;

privileged aspect TipoMovimientoCajaBean_Roo_ManagedBean {
    
    declare @type: TipoMovimientoCajaBean: @ManagedBean(name = "tipoMovimientoCajaBean");
    
    declare @type: TipoMovimientoCajaBean: @SessionScoped;
    
    private String TipoMovimientoCajaBean.name = "TipoMovimientoCajas";
    
    private TipoMovimientoCaja TipoMovimientoCajaBean.tipoMovimientoCaja;
    
    private List<TipoMovimientoCaja> TipoMovimientoCajaBean.allTipoMovimientoCajas;
    
    private boolean TipoMovimientoCajaBean.dataVisible = false;
    
    private List<String> TipoMovimientoCajaBean.columns;
    
    private HtmlPanelGrid TipoMovimientoCajaBean.createPanelGrid;
    
    private HtmlPanelGrid TipoMovimientoCajaBean.editPanelGrid;
    
    private HtmlPanelGrid TipoMovimientoCajaBean.viewPanelGrid;
    
    private boolean TipoMovimientoCajaBean.createDialogVisible = false;
    
    @PostConstruct
    public void TipoMovimientoCajaBean.init() {
        columns = new ArrayList<String>();
        columns.add("tipoMovimiento");
        columns.add("variacion");
    }
    
    public String TipoMovimientoCajaBean.getName() {
        return name;
    }
    
    public List<String> TipoMovimientoCajaBean.getColumns() {
        return columns;
    }
    
    public List<TipoMovimientoCaja> TipoMovimientoCajaBean.getAllTipoMovimientoCajas() {
        return allTipoMovimientoCajas;
    }
    
    public void TipoMovimientoCajaBean.setAllTipoMovimientoCajas(List<TipoMovimientoCaja> allTipoMovimientoCajas) {
        this.allTipoMovimientoCajas = allTipoMovimientoCajas;
    }
    
    public String TipoMovimientoCajaBean.findAllTipoMovimientoCajas() {
        allTipoMovimientoCajas = TipoMovimientoCaja.findAllTipoMovimientoCajas();
        dataVisible = !allTipoMovimientoCajas.isEmpty();
        return null;
    }
    
    public boolean TipoMovimientoCajaBean.isDataVisible() {
        return dataVisible;
    }
    
    public void TipoMovimientoCajaBean.setDataVisible(boolean dataVisible) {
        this.dataVisible = dataVisible;
    }
    
    public HtmlPanelGrid TipoMovimientoCajaBean.getCreatePanelGrid() {
        if (createPanelGrid == null) {
            createPanelGrid = populateCreatePanel();
        }
        return createPanelGrid;
    }
    
    public void TipoMovimientoCajaBean.setCreatePanelGrid(HtmlPanelGrid createPanelGrid) {
        this.createPanelGrid = createPanelGrid;
    }
    
    public HtmlPanelGrid TipoMovimientoCajaBean.getEditPanelGrid() {
        if (editPanelGrid == null) {
            editPanelGrid = populateEditPanel();
        }
        return editPanelGrid;
    }
    
    public void TipoMovimientoCajaBean.setEditPanelGrid(HtmlPanelGrid editPanelGrid) {
        this.editPanelGrid = editPanelGrid;
    }
    
    public HtmlPanelGrid TipoMovimientoCajaBean.getViewPanelGrid() {
        return populateViewPanel();
    }
    
    public void TipoMovimientoCajaBean.setViewPanelGrid(HtmlPanelGrid viewPanelGrid) {
        this.viewPanelGrid = viewPanelGrid;
    }
    
    public HtmlPanelGrid TipoMovimientoCajaBean.populateCreatePanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText tipoMovimientoCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        tipoMovimientoCreateOutput.setId("tipoMovimientoCreateOutput");
        tipoMovimientoCreateOutput.setValue("Tipo Movimiento:   ");
        htmlPanelGrid.getChildren().add(tipoMovimientoCreateOutput);
        
        InputText tipoMovimientoCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        tipoMovimientoCreateInput.setId("tipoMovimientoCreateInput");
        tipoMovimientoCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{tipoMovimientoCajaBean.tipoMovimientoCaja.tipoMovimiento}", String.class));
        htmlPanelGrid.getChildren().add(tipoMovimientoCreateInput);
        
        Message tipoMovimientoCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        tipoMovimientoCreateInputMessage.setId("tipoMovimientoCreateInputMessage");
        tipoMovimientoCreateInputMessage.setFor("tipoMovimientoCreateInput");
        tipoMovimientoCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(tipoMovimientoCreateInputMessage);
        
        HtmlOutputText variacionCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        variacionCreateOutput.setId("variacionCreateOutput");
        variacionCreateOutput.setValue("Variacion:   ");
        htmlPanelGrid.getChildren().add(variacionCreateOutput);
        
        Spinner variacionCreateInput = (Spinner) application.createComponent(Spinner.COMPONENT_TYPE);
        variacionCreateInput.setId("variacionCreateInput");
        variacionCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{tipoMovimientoCajaBean.tipoMovimientoCaja.variacion}", Long.class));
        variacionCreateInput.setRequired(false);
        
        htmlPanelGrid.getChildren().add(variacionCreateInput);
        
        Message variacionCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        variacionCreateInputMessage.setId("variacionCreateInputMessage");
        variacionCreateInputMessage.setFor("variacionCreateInput");
        variacionCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(variacionCreateInputMessage);
        
        return htmlPanelGrid;
    }
    
    public HtmlPanelGrid TipoMovimientoCajaBean.populateEditPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText tipoMovimientoEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        tipoMovimientoEditOutput.setId("tipoMovimientoEditOutput");
        tipoMovimientoEditOutput.setValue("Tipo Movimiento:   ");
        htmlPanelGrid.getChildren().add(tipoMovimientoEditOutput);
        
        InputText tipoMovimientoEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        tipoMovimientoEditInput.setId("tipoMovimientoEditInput");
        tipoMovimientoEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{tipoMovimientoCajaBean.tipoMovimientoCaja.tipoMovimiento}", String.class));
        htmlPanelGrid.getChildren().add(tipoMovimientoEditInput);
        
        Message tipoMovimientoEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        tipoMovimientoEditInputMessage.setId("tipoMovimientoEditInputMessage");
        tipoMovimientoEditInputMessage.setFor("tipoMovimientoEditInput");
        tipoMovimientoEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(tipoMovimientoEditInputMessage);
        
        HtmlOutputText variacionEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        variacionEditOutput.setId("variacionEditOutput");
        variacionEditOutput.setValue("Variacion:   ");
        htmlPanelGrid.getChildren().add(variacionEditOutput);
        
        Spinner variacionEditInput = (Spinner) application.createComponent(Spinner.COMPONENT_TYPE);
        variacionEditInput.setId("variacionEditInput");
        variacionEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{tipoMovimientoCajaBean.tipoMovimientoCaja.variacion}", Long.class));
        variacionEditInput.setRequired(false);
        
        htmlPanelGrid.getChildren().add(variacionEditInput);
        
        Message variacionEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        variacionEditInputMessage.setId("variacionEditInputMessage");
        variacionEditInputMessage.setFor("variacionEditInput");
        variacionEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(variacionEditInputMessage);
        
        return htmlPanelGrid;
    }
    
    public HtmlPanelGrid TipoMovimientoCajaBean.populateViewPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText tipoMovimientoLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        tipoMovimientoLabel.setId("tipoMovimientoLabel");
        tipoMovimientoLabel.setValue("Tipo Movimiento:   ");
        htmlPanelGrid.getChildren().add(tipoMovimientoLabel);
        
        HtmlOutputText tipoMovimientoValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        tipoMovimientoValue.setId("tipoMovimientoValue");
        tipoMovimientoValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{tipoMovimientoCajaBean.tipoMovimientoCaja.tipoMovimiento}", String.class));
        htmlPanelGrid.getChildren().add(tipoMovimientoValue);
        
        HtmlOutputText variacionLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        variacionLabel.setId("variacionLabel");
        variacionLabel.setValue("Variacion:   ");
        htmlPanelGrid.getChildren().add(variacionLabel);
        
        HtmlOutputText variacionValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        variacionValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{tipoMovimientoCajaBean.tipoMovimientoCaja.variacion}", String.class));
        htmlPanelGrid.getChildren().add(variacionValue);
        
        return htmlPanelGrid;
    }
    
    public TipoMovimientoCaja TipoMovimientoCajaBean.getTipoMovimientoCaja() {
        if (tipoMovimientoCaja == null) {
            tipoMovimientoCaja = new TipoMovimientoCaja();
        }
        return tipoMovimientoCaja;
    }
    
    public void TipoMovimientoCajaBean.setTipoMovimientoCaja(TipoMovimientoCaja tipoMovimientoCaja) {
        this.tipoMovimientoCaja = tipoMovimientoCaja;
    }
    
    public String TipoMovimientoCajaBean.onEdit() {
        return null;
    }
    
    public boolean TipoMovimientoCajaBean.isCreateDialogVisible() {
        return createDialogVisible;
    }
    
    public void TipoMovimientoCajaBean.setCreateDialogVisible(boolean createDialogVisible) {
        this.createDialogVisible = createDialogVisible;
    }
    
    public String TipoMovimientoCajaBean.displayList() {
        createDialogVisible = false;
        findAllTipoMovimientoCajas();
        return "tipoMovimientoCaja";
    }
    
    public String TipoMovimientoCajaBean.displayCreateDialog() {
        tipoMovimientoCaja = new TipoMovimientoCaja();
        createDialogVisible = true;
        return "tipoMovimientoCaja";
    }
    
    public String TipoMovimientoCajaBean.persist() {
        String message = "";
        if (tipoMovimientoCaja.getId() != null) {
            tipoMovimientoCaja.merge();
            message = "Successfully updated";
        } else {
            tipoMovimientoCaja.persist();
            message = "Successfully created";
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialog.hide()");
        context.execute("editDialog.hide()");
        
        FacesMessage facesMessage = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllTipoMovimientoCajas();
    }
    
    public String TipoMovimientoCajaBean.delete() {
        tipoMovimientoCaja.remove();
        FacesMessage facesMessage = new FacesMessage("Successfully deleted");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllTipoMovimientoCajas();
    }
    
    public void TipoMovimientoCajaBean.reset() {
        tipoMovimientoCaja = null;
        createDialogVisible = false;
    }
    
    public void TipoMovimientoCajaBean.handleDialogClose(CloseEvent event) {
        reset();
    }
    
}
