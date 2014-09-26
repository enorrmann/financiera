package ar.com.norrmann.financiera.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.message.Message;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.context.RequestContext;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import ar.com.norrmann.financiera.model.MovimientoCaja;
import ar.com.norrmann.financiera.model.Pago;
import ar.com.norrmann.financiera.model.TipoMovimientoCaja;
import ar.com.norrmann.financiera.web.converter.TipoMovimientoCajaConverter;
import ar.com.norrmann.financiera.web.util.I18N;

@RooSerializable
@RooJsfManagedBean(entity = MovimientoCaja.class, beanName = "movimientoCajaBean")
public class MovimientoCajaBean {

	private FiltroCuotaList filtroMovimientos = new FiltroCuotaList();
	private BigDecimal total=new BigDecimal(0);
	private MovimientoCaja movimientoCaja;
	private List<Pago> selectedPagoList;
	private boolean createDialogVisible = false;
	private List<MovimientoCaja> allMovimientoCajas;
	private boolean dataVisible = false;
	
	public HtmlPanelGrid populateCreatePanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText empresaCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        empresaCreateOutput.setId("empresaCreateOutput");
        empresaCreateOutput.setValue("Caja: * ");
        htmlPanelGrid.getChildren().add(empresaCreateOutput);
        
        htmlPanelGrid.getChildren().add(getSelectOneMenuEmpresas());
        
        Message empresaCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        empresaCreateInputMessage.setId("empresaCreateInputMessage");
        empresaCreateInputMessage.setFor("idEmpresaList");
        empresaCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(empresaCreateInputMessage);
        
        HtmlOutputText tipoMovimientoCajaCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        tipoMovimientoCajaCreateOutput.setId("tipoMovimientoCajaCreateOutput");
        tipoMovimientoCajaCreateOutput.setValue("Tipo de Movimiento : * ");
        htmlPanelGrid.getChildren().add(tipoMovimientoCajaCreateOutput);
        
        AutoComplete tipoMovimientoCajaCreateInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        tipoMovimientoCajaCreateInput.setId("tipoMovimientoCajaCreateInput");
        tipoMovimientoCajaCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{movimientoCajaBean.movimientoCaja.tipoMovimientoCaja}", TipoMovimientoCaja.class));
        tipoMovimientoCajaCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{movimientoCajaBean.completeTipoMovimientoCaja}", List.class, new Class[] { String.class }));
        tipoMovimientoCajaCreateInput.setDropdown(true);
        tipoMovimientoCajaCreateInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "tipoMovimientoCaja", String.class));
        tipoMovimientoCajaCreateInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{tipoMovimientoCaja.tipoMovimiento}", String.class));
        tipoMovimientoCajaCreateInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{tipoMovimientoCaja}", TipoMovimientoCaja.class));
        tipoMovimientoCajaCreateInput.setConverter(new TipoMovimientoCajaConverter());
        tipoMovimientoCajaCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(tipoMovimientoCajaCreateInput);
        
        Message tipoMovimientoCajaCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        tipoMovimientoCajaCreateInputMessage.setId("tipoMovimientoCajaCreateInputMessage");
        tipoMovimientoCajaCreateInputMessage.setFor("tipoMovimientoCajaCreateInput");
        tipoMovimientoCajaCreateInputMessage.setDisplay("icon"); 
        htmlPanelGrid.getChildren().add(tipoMovimientoCajaCreateInputMessage);
        
        HtmlOutputText fechaCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        fechaCreateOutput.setId("fechaCreateOutput");
        fechaCreateOutput.setValue("Fecha:   ");
        htmlPanelGrid.getChildren().add(fechaCreateOutput);
        
        Calendar fechaCreateInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        fechaCreateInput.setId("fechaCreateInput");
        fechaCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{movimientoCajaBean.movimientoCaja.fecha}", Date.class));
        fechaCreateInput.setNavigator(true);
        fechaCreateInput.setEffect("slideDown");
        fechaCreateInput.setPattern("dd/MM/yyyy");
        fechaCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(fechaCreateInput);
        
        Message fechaCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        fechaCreateInputMessage.setId("fechaCreateInputMessage");
        fechaCreateInputMessage.setFor("fechaCreateInput");
        fechaCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(fechaCreateInputMessage);
        
        HtmlOutputText importeCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        importeCreateOutput.setId("importeCreateOutput");
        importeCreateOutput.setValue("Importe:   ");
        htmlPanelGrid.getChildren().add(importeCreateOutput);
        
        InputText importeCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        importeCreateInput.setId("importeCreateInput");
        importeCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{movimientoCajaBean.movimientoCaja.importe}", BigDecimal.class));
        importeCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(importeCreateInput);
        
        Message importeCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        importeCreateInputMessage.setId("importeCreateInputMessage");
        importeCreateInputMessage.setFor("importeCreateInput");
        importeCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(importeCreateInputMessage);
        
        HtmlOutputText conceptoCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        conceptoCreateOutput.setId("conceptoCreateOutput");
        conceptoCreateOutput.setValue("Concepto:   ");
        htmlPanelGrid.getChildren().add(conceptoCreateOutput);
        
        InputText conceptoCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        conceptoCreateInput.setId("conceptoCreateInput");
        conceptoCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{movimientoCajaBean.movimientoCaja.concepto}", String.class));
        htmlPanelGrid.getChildren().add(conceptoCreateInput);
        
        Message conceptoCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        conceptoCreateInputMessage.setId("conceptoCreateInputMessage");
        conceptoCreateInputMessage.setFor("conceptoCreateInput");
        conceptoCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(conceptoCreateInputMessage);
        
        return htmlPanelGrid;
    }

	public HtmlPanelGrid populateEditPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText tipoMovimientoCajaEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        tipoMovimientoCajaEditOutput.setId("tipoMovimientoCajaEditOutput");
        tipoMovimientoCajaEditOutput.setValue("Tipo Movimiento Caja: * ");
        htmlPanelGrid.getChildren().add(tipoMovimientoCajaEditOutput);
        
        AutoComplete tipoMovimientoCajaEditInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        tipoMovimientoCajaEditInput.setId("tipoMovimientoCajaEditInput");
        tipoMovimientoCajaEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{movimientoCajaBean.movimientoCaja.tipoMovimientoCaja}", TipoMovimientoCaja.class));
        tipoMovimientoCajaEditInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{movimientoCajaBean.completeTipoMovimientoCaja}", List.class, new Class[] { String.class }));
        tipoMovimientoCajaEditInput.setDropdown(true);
        tipoMovimientoCajaEditInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "tipoMovimientoCaja", String.class));
        tipoMovimientoCajaEditInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{tipoMovimientoCaja.tipoMovimiento}", String.class));
        tipoMovimientoCajaEditInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{tipoMovimientoCaja}", TipoMovimientoCaja.class));
        tipoMovimientoCajaEditInput.setConverter(new TipoMovimientoCajaConverter());
        tipoMovimientoCajaEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(tipoMovimientoCajaEditInput);
        
        Message tipoMovimientoCajaEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        tipoMovimientoCajaEditInputMessage.setId("tipoMovimientoCajaEditInputMessage");
        tipoMovimientoCajaEditInputMessage.setFor("tipoMovimientoCajaEditInput");
        tipoMovimientoCajaEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(tipoMovimientoCajaEditInputMessage);
        
        HtmlOutputText fechaEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        fechaEditOutput.setId("fechaEditOutput");
        fechaEditOutput.setValue("Fecha:   ");
        htmlPanelGrid.getChildren().add(fechaEditOutput);
        
        Calendar fechaEditInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        fechaEditInput.setId("fechaEditInput");
        fechaEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{movimientoCajaBean.movimientoCaja.fecha}", Date.class));
        fechaEditInput.setNavigator(true);
        fechaEditInput.setEffect("slideDown");
        fechaEditInput.setPattern("dd/MM/yyyy");
        fechaEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(fechaEditInput);
        
        Message fechaEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        fechaEditInputMessage.setId("fechaEditInputMessage");
        fechaEditInputMessage.setFor("fechaEditInput");
        fechaEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(fechaEditInputMessage);
        
        HtmlOutputText importeEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        importeEditOutput.setId("importeEditOutput");
        importeEditOutput.setValue("Importe:   ");
        htmlPanelGrid.getChildren().add(importeEditOutput);
        
        InputText importeEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        importeEditInput.setId("importeEditInput");
        importeEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{movimientoCajaBean.movimientoCaja.importe}", BigDecimal.class));
        importeEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(importeEditInput);
        
        Message importeEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        importeEditInputMessage.setId("importeEditInputMessage");
        importeEditInputMessage.setFor("importeEditInput");
        importeEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(importeEditInputMessage);
        
        HtmlOutputText conceptoEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        conceptoEditOutput.setId("conceptoEditOutput");
        conceptoEditOutput.setValue("Concepto:   ");
        htmlPanelGrid.getChildren().add(conceptoEditOutput);
        
        InputText conceptoEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        conceptoEditInput.setId("conceptoEditInput");
        conceptoEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{movimientoCajaBean.movimientoCaja.concepto}", String.class));
        htmlPanelGrid.getChildren().add(conceptoEditInput);
        
        Message conceptoEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        conceptoEditInputMessage.setId("conceptoEditInputMessage");
        conceptoEditInputMessage.setFor("conceptoEditInput");
        conceptoEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(conceptoEditInputMessage);
        
        return htmlPanelGrid;
    }

	public HtmlPanelGrid populateViewPanel() {
        return null;
    }

	public String persist() {
		MovimientoCaja movimientoCaja = getMovimientoCaja();
        String message = "";
        if (movimientoCaja.getId() != null) {
            movimientoCaja.merge();
            message = I18N.ACTUALIZADO_OK;
        } else {
            movimientoCaja.persist();
            message = I18N.CREADO_OK; 
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialog.hide()");
        context.execute("editDialog.hide()");
        
        FacesMessage facesMessage = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        filtroMovimientos.setFechaHasta(movimientoCaja.getFecha());
        reset();
        return filtrarLista();
    }
	public FiltroCuotaList getFiltroMovimientos() {
		return filtroMovimientos;
	}

	public void setFiltroMovimientos(FiltroCuotaList filtroMovimientos) {
		this.filtroMovimientos = filtroMovimientos;
	}
 
	public String filtrarLista(){
		setCreateDialogVisible(false);
		List<MovimientoCaja> movimientoCajaList = new ArrayList<MovimientoCaja>();
		if (filtroMovimientos.getFechaHasta()==null){
			filtroMovimientos.setFechaHasta(new Date());
		}
		MovimientoCaja cobrosDelDia = getCobrosDelDia();
		if (cobrosDelDia.getImporte()!=null&&!cobrosDelDia.getImporte().abs().equals(new BigDecimal(0))){
			movimientoCajaList.add(cobrosDelDia);
		}
		movimientoCajaList.addAll(MovimientoCaja.findMovimientoCajasByFechaEqualsAndIdEmpresa(filtroMovimientos.getFechaHasta(),filtroMovimientos.getIdEmpresa()));
		setAllMovimientoCajas(movimientoCajaList);
		setDataVisible(getAllMovimientoCajas()!=null&&!getAllMovimientoCajas().isEmpty());
		calcularTotal();
		return "movimientoCaja";
	}

	private void calcularTotal() {
		this.total = new BigDecimal(0);
		List<MovimientoCaja> movimientoList = getAllMovimientoCajas();
		if (movimientoList==null)return;
		for (MovimientoCaja unMovimientoCaja:movimientoList){
			total=total.add(unMovimientoCaja.getImporteNeto());
		}
		
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	private MovimientoCaja getCobrosDelDia(){
		MovimientoCaja pagosDelDia = new MovimientoCaja();
		pagosDelDia.setTipoMovimientoCaja(TipoMovimientoCaja.findTipoMovimientoCaja(1L));
		pagosDelDia.setConcepto("Cobros del dia");
		pagosDelDia.setFecha(filtroMovimientos.getFechaHasta());
		List<Pago> pagoList = Pago.findPagoesByFechaPagoEqualsAndIdEmpresa(filtroMovimientos.getFechaHasta(),filtroMovimientos.getIdEmpresa());
		pagosDelDia.setPagoList(pagoList);
		return pagosDelDia;
		
	}
	private SelectOneMenu getSelectOneMenuEmpresas(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		ExpressionFactory expressionFactory = application.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();
		
		SelectOneMenu selectOneMenu = (SelectOneMenu) application.createComponent(SelectOneMenu.COMPONENT_TYPE);
		selectOneMenu.setId("idEmpresaList");
		selectOneMenu.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{movimientoCajaBean.movimientoCaja.idEmpresa}", Long.class));
			
		List<UIComponent> menuChildren = selectOneMenu.getChildren();
		UISelectItem selectItem1 = (UISelectItem) application.createComponent(UISelectItem.COMPONENT_TYPE);
		selectItem1.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext,"1", Long.class));
		selectItem1.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext,"Caja 1", String.class));
		
		UISelectItem selectItem2 = (UISelectItem) application.createComponent(UISelectItem.COMPONENT_TYPE);
		selectItem2.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext,"2", Long.class));
		selectItem2.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext,"Caja 2", String.class));
		
		UISelectItem selectItem3 = (UISelectItem) application.createComponent(UISelectItem.COMPONENT_TYPE);
		selectItem3.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext,"3", Long.class));
		selectItem3.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext,"Caja 3", String.class));
		
		menuChildren.add(selectItem1);
		menuChildren.add(selectItem2);
		menuChildren.add(selectItem3);
		return selectOneMenu; 
	}
	
    public MovimientoCaja getMovimientoCaja() {
        if (movimientoCaja == null) {
            movimientoCaja = new MovimientoCaja();
        }
        return movimientoCaja;
    }

	public void setMovimientoCaja(MovimientoCaja movimientoCaja) {
		this.movimientoCaja = movimientoCaja;
	}
	   public void reset() {
	        movimientoCaja = null;
	        selectedPagoList = null;
	        createDialogVisible = false;
	    }

	public List<Pago> getSelectedPagoList() {
		return selectedPagoList;
	}

	public void setSelectedPagoList(List<Pago> selectedPagoList) {
		this.selectedPagoList = selectedPagoList;
	}

    public void setCreateDialogVisible(boolean createDialogVisible) {
        this.createDialogVisible = createDialogVisible;
    }

	public boolean isCreateDialogVisible() {
		return createDialogVisible;
	}
    public List<MovimientoCaja> getAllMovimientoCajas() {
        return allMovimientoCajas;
    }
    
    public void setAllMovimientoCajas(List<MovimientoCaja> allMovimientoCajas) {
        this.allMovimientoCajas = allMovimientoCajas;
    }
    public boolean isDataVisible() {
        return dataVisible;
    }
    
    public void setDataVisible(boolean dataVisible) {
        this.dataVisible = dataVisible;
    }
    public String onEdit() {
//        if (movimientoCaja != null && movimientoCaja.getPagoList() != null) {
////            selectedPagoList = new ArrayList<Pago>(movimientoCaja.getPagoList());
//        }
        return null;
    }
}
