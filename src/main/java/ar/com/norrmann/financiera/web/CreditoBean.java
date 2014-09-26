package ar.com.norrmann.financiera.web;

import java.io.IOException;
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
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.convert.BigDecimalConverter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.model.SelectItem;
import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.LongRangeValidator;

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.button.Button;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.message.Message;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.spinner.Spinner;
import org.primefaces.context.RequestContext;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import ar.com.norrmann.financiera.model.Cliente;
import ar.com.norrmann.financiera.model.Credito;
import ar.com.norrmann.financiera.model.Cuota;
import ar.com.norrmann.financiera.model.InteresPunitorio;
import ar.com.norrmann.financiera.model.Tasa;
import ar.com.norrmann.financiera.model.TipoCredito;
import ar.com.norrmann.financiera.web.converter.ClienteConverter;
import ar.com.norrmann.financiera.web.converter.InteresPunitorioConverter;
import ar.com.norrmann.financiera.web.converter.TasaConverter;
import ar.com.norrmann.financiera.web.util.I18N;

@RooSerializable
@RooJsfManagedBean(entity = Credito.class, beanName = "creditoBean")
public class CreditoBean {

    private Credito credito;
    private BigDecimal totalCreditos; 
    private BigDecimal totalIntereses;
    private BigDecimal totalTotal;
    private BigDecimal totalImpagas;
    private List<Cuota> selectedCuotas;
    private FiltroCuotaList filtroCreditoList = new FiltroCuotaList();
    
	public List<Cuota> getCuotas(){
		ArrayList<Cuota> cuotas = new ArrayList<Cuota>();
		if (getCredito()==null)return cuotas;
		cuotas.addAll(getCredito().getCuotas());
		return cuotas;
	}
    

	
	public String updateCuotas() {
		String message = "";
		if (!credito.seleccionoDias()){
	        FacesMessage facesMessage = new FacesMessage("Debe seleccionar el dia de vencimiento");
	        facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			return null;
			
		} 
		credito.recalcularCuotasDiario();
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("editDialog.hide()");
        message = I18N.ACTUALIZADO_OK;
        FacesMessage facesMessage = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		return filtrarLista();
	}
	public String persist() {
		String message = "";
		Cliente cliente = credito.getCliente();
		if (!credito.seleccionoDias()){
	        FacesMessage facesMessage = new FacesMessage("Debe seleccionar el dia de vencimiento");
	        facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			return null;
			
		}
		if (cliente.puedoPresetar()){
	        FacesMessage facesMessage = new FacesMessage("El cliente no presentó libre deuda");
	        facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			return null;
		}
		if (cliente.esMalPagador()){
	        FacesMessage facesMessage = new FacesMessage("El cliente está registrado como Mal Pagador");
	        facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			return null;
		}
        if (credito.getId() != null) {
            credito.merge();
            message = I18N.ACTUALIZADO_OK;
        } else {
        	credito.generarCuotas();
            credito.persist();
            message = I18N.CREADO_OK;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialog.hide()");
        context.execute("editDialog.hide()");
        
        FacesMessage facesMessage = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        filtroCreditoList.setIdEmpresa(credito.getIdEmpresa());
        filtroCreditoList.setEntidadCliente(credito.getCliente());
        reset();
        return filtrarLista();
    }    
    public String delete() {
        credito.removePagos();
        credito.removeCuotas();
        credito.remove();
        FacesMessage facesMessage = new FacesMessage(I18N.BORRADO_OK);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllCreditoes();
    }
    public Credito getCredito() {
        if (credito == null) {
            credito = new Credito();
        } 
        return credito;
    }
    
    public void setCredito(Credito credito) {
        this.credito = credito;
    }
    

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

        HtmlOutputText clienteCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        clienteCreateOutput.setId("clienteCreateOutput");
        clienteCreateOutput.setValue("Cliente: * ");
        htmlPanelGrid.getChildren().add(clienteCreateOutput);
        
        AutoComplete clienteCreateInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        clienteCreateInput.setId("clienteCreateInput");
        clienteCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.cliente}", Cliente.class));
        clienteCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{creditoBean.completeCliente}", List.class, new Class[] { String.class }));
        clienteCreateInput.setDropdown(false);
        clienteCreateInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "cliente", String.class));
        clienteCreateInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{cliente.apellidos} #{cliente.nombres} #{cliente.dni}", String.class));
        clienteCreateInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{cliente}", Cliente.class));
        clienteCreateInput.setConverter(new ClienteConverter());
        clienteCreateInput.setRequired(true);
        clienteCreateInput.setMaxResults(10);
        htmlPanelGrid.getChildren().add(clienteCreateInput);
        
        Message clienteCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        clienteCreateInputMessage.setId("clienteCreateInputMessage");
        clienteCreateInputMessage.setFor("clienteCreateInput");
        clienteCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(clienteCreateInputMessage);
        
        HtmlOutputText garanteCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        garanteCreateOutput.setId("garanteCreateOutput");
        garanteCreateOutput.setValue("Garante:  ");
        htmlPanelGrid.getChildren().add(garanteCreateOutput);
        
        AutoComplete garanteCreateInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        garanteCreateInput.setId("garanteCreateInput");
        garanteCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.garante}", Cliente.class));
        garanteCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{creditoBean.completeCliente}", List.class, new Class[] { String.class }));
        garanteCreateInput.setDropdown(false);
        garanteCreateInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "cliente", String.class));
        garanteCreateInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{cliente.apellidos} #{cliente.nombres} #{cliente.dni}", String.class));
        garanteCreateInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{cliente}", Cliente.class));
        garanteCreateInput.setConverter(new ClienteConverter());
        garanteCreateInput.setRequired(false);
        garanteCreateInput.setMaxResults(10);
        htmlPanelGrid.getChildren().add(garanteCreateInput);
        
        Message garanteCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        garanteCreateInputMessage.setId("garanteCreateInputMessage");
        garanteCreateInputMessage.setFor("garanteCreateInput");
        garanteCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(garanteCreateInputMessage);

        HtmlOutputText fechaSolicitudCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        fechaSolicitudCreateOutput.setId("fechaSolicitudCreateOutput");
        fechaSolicitudCreateOutput.setValue("Fecha Solicitud: * ");
        htmlPanelGrid.getChildren().add(fechaSolicitudCreateOutput);
        
        Calendar fechaSolicitudCreateInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        fechaSolicitudCreateInput.setId("fechaSolicitudCreateInput");
        fechaSolicitudCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.fechaSolicitud}", Date.class));
        fechaSolicitudCreateInput.setNavigator(true);
        fechaSolicitudCreateInput.setEffect("slideDown");
        fechaSolicitudCreateInput.setPattern("dd/MM/yyyy");
        fechaSolicitudCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(fechaSolicitudCreateInput);
        
        Message fechaSolicitudCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        fechaSolicitudCreateInputMessage.setId("fechaSolicitudCreateInputMessage");
        fechaSolicitudCreateInputMessage.setFor("fechaSolicitudCreateInput");
        fechaSolicitudCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(fechaSolicitudCreateInputMessage);
        
// fecha primer vencimiento
        HtmlOutputText fechaPv = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        fechaPv.setId("fechaPv");
        fechaPv.setValue("Primer vencimiento: * ");
        htmlPanelGrid.getChildren().add(fechaPv);
        
        Calendar fechaPvCreateInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        fechaPvCreateInput.setId("fechaPvCreateInput");
        fechaPvCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.fechaPrimerVencimiento}", Date.class));
        fechaPvCreateInput.setNavigator(true);
        fechaPvCreateInput.setEffect("slideDown");
        fechaPvCreateInput.setPattern("dd/MM/yyyy");
        fechaPvCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(fechaPvCreateInput);
        
        Message fechaPvCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        fechaPvCreateInputMessage.setId("fechaPvCreateInputMessage");
        fechaPvCreateInputMessage.setFor("fechaPvCreateInput");
        fechaPvCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(fechaPvCreateInputMessage);

        // fin fecha primer vencimiento
        
        HtmlOutputText montoSolicitadoCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        montoSolicitadoCreateOutput.setId("montoSolicitadoCreateOutput");
        montoSolicitadoCreateOutput.setValue("Monto Solicitado: * ");
        htmlPanelGrid.getChildren().add(montoSolicitadoCreateOutput);
        
        InputText montoSolicitadoCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        montoSolicitadoCreateInput.setId("montoSolicitadoCreateInput");
        montoSolicitadoCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.montoSolicitado}", BigDecimal.class));
        montoSolicitadoCreateInput.setRequired(true);
        DoubleRangeValidator montoSolicitadoCreateInputValidator = new DoubleRangeValidator();
        montoSolicitadoCreateInputValidator.setMinimum(1.0);
        montoSolicitadoCreateInput.addValidator(montoSolicitadoCreateInputValidator);
        
        //htmlPanelGrid.getChildren().add(montoSolicitadoCreateInput);
        
        
        Button cbMonto = (Button)application.createComponent(Button.COMPONENT_TYPE);
        cbMonto.setValueExpression("styleClass", expressionFactory.createValueExpression(elContext, "cbMonto", String.class));
        cbMonto.setValueExpression("icon", expressionFactory.createValueExpression(elContext, "ui-icon-calculator", String.class));
        cbMonto.setValueExpression("title", expressionFactory.createValueExpression(elContext, "Calcular", String.class));
        cbMonto.setValueExpression("onclick", expressionFactory.createValueExpression(elContext, "calcularMontoTotal();return true;", String.class));
        
        //htmlPanelGrid.getChildren().add(cbMonto);
        
        HtmlPanelGroup panelGroupMonto = (HtmlPanelGroup) FacesContext.getCurrentInstance().getApplication().createComponent(HtmlPanelGroup.COMPONENT_TYPE);
        panelGroupMonto.getChildren().add(montoSolicitadoCreateInput);
        panelGroupMonto.getChildren().add(cbMonto);
        
        htmlPanelGrid.getChildren().add(panelGroupMonto);

        Message montoSolicitadoCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        montoSolicitadoCreateInputMessage.setId("montoSolicitadoCreateInputMessage");
        montoSolicitadoCreateInputMessage.setFor("montoSolicitadoCreateInput");
        montoSolicitadoCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(montoSolicitadoCreateInputMessage);
        
        HtmlOutputText montoDeCuotaCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        montoDeCuotaCreateOutput.setId("montoDeCuotaCreateOutput");
        montoDeCuotaCreateOutput.setValue("Monto de Cuota: * ");
        htmlPanelGrid.getChildren().add(montoDeCuotaCreateOutput);
        
        InputText montoDeCuotaCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        montoDeCuotaCreateInput.setId("montoDeCuotaCreateInput");
        montoDeCuotaCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.montoDeCuota}", BigDecimal.class));
        montoDeCuotaCreateInput.setRequired(true);
        DoubleRangeValidator montoDeCuotaCreateInputValidator = new DoubleRangeValidator();
        montoDeCuotaCreateInputValidator.setMinimum(1.0);
        montoDeCuotaCreateInput.addValidator(montoDeCuotaCreateInputValidator);
        
      //  htmlPanelGrid.getChildren().add(montoDeCuotaCreateInput);
        
        Button cbMontoCuota = (Button)application.createComponent(Button.COMPONENT_TYPE);
        cbMontoCuota.setValueExpression("styleClass", expressionFactory.createValueExpression(elContext, "cbMontoCuota", String.class));
        cbMontoCuota.setValueExpression("icon", expressionFactory.createValueExpression(elContext, "ui-icon-calculator", String.class));
        cbMontoCuota.setValueExpression("title", expressionFactory.createValueExpression(elContext, "Calcular", String.class));
        cbMontoCuota.setValueExpression("onclick", expressionFactory.createValueExpression(elContext, "calcularMontoCuota();return true;", String.class));

       // htmlPanelGrid.getChildren().add(cbMontoCuota);

        HtmlPanelGroup panelGroupMontoCuota = (HtmlPanelGroup) FacesContext.getCurrentInstance().getApplication().createComponent(HtmlPanelGroup.COMPONENT_TYPE);
        panelGroupMontoCuota.getChildren().add(montoDeCuotaCreateInput);
        panelGroupMontoCuota.getChildren().add(cbMontoCuota);

        htmlPanelGrid.getChildren().add(panelGroupMontoCuota);


        Message montoDeCuotaCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        montoDeCuotaCreateInputMessage.setId("montoDeCuotaCreateInputMessage");
        montoDeCuotaCreateInputMessage.setFor("montoDeCuotaCreateInput");
        montoDeCuotaCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(montoDeCuotaCreateInputMessage);

        
        HtmlOutputText cantidadCuotasCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        cantidadCuotasCreateOutput.setId("cantidadCuotasCreateOutput");
        cantidadCuotasCreateOutput.setValue("Cantidad Cuotas: * ");
        htmlPanelGrid.getChildren().add(cantidadCuotasCreateOutput);
        
        Spinner cantidadCuotasCreateInput = (Spinner) application.createComponent(Spinner.COMPONENT_TYPE);
        cantidadCuotasCreateInput.setId("cantidadCuotasCreateInput");
        cantidadCuotasCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.cantidadCuotas}", Integer.class));
        cantidadCuotasCreateInput.setRequired(true);
        cantidadCuotasCreateInput.setMin(1.0);
        LongRangeValidator cantidadCuotasCreateInputValidator = new LongRangeValidator();
        cantidadCuotasCreateInputValidator.setMinimum(1);
        cantidadCuotasCreateInput.addValidator(cantidadCuotasCreateInputValidator);
        
      //  htmlPanelGrid.getChildren().add(cantidadCuotasCreateInput);
        
        Button cbCantidadCuotas = (Button)application.createComponent(Button.COMPONENT_TYPE);
        cbCantidadCuotas.setValueExpression("styleClass", expressionFactory.createValueExpression(elContext, "cbCantidadCuotas", String.class));
        cbCantidadCuotas.setValueExpression("icon", expressionFactory.createValueExpression(elContext, "ui-icon-calculator", String.class));
        cbCantidadCuotas.setValueExpression("title", expressionFactory.createValueExpression(elContext, "Calcular", String.class));
        cbCantidadCuotas.setValueExpression("onclick", expressionFactory.createValueExpression(elContext, "calcularCantidadCuotas();return true;", String.class));
        
       // htmlPanelGrid.getChildren().add(cbCantidadCuotas);

        HtmlPanelGroup panelGroupCantidadCuotas = (HtmlPanelGroup) FacesContext.getCurrentInstance().getApplication().createComponent(HtmlPanelGroup.COMPONENT_TYPE);
        panelGroupCantidadCuotas.getChildren().add(cantidadCuotasCreateInput);
        //panelGroupCantidadCuotas.getChildren().add(cbCantidadCuotas);

        htmlPanelGrid.getChildren().add(panelGroupCantidadCuotas);
        
        Message cantidadCuotasCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        cantidadCuotasCreateInputMessage.setId("cantidadCuotasCreateInputMessage");
        cantidadCuotasCreateInputMessage.setFor("cantidadCuotasCreateInput");
        cantidadCuotasCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(cantidadCuotasCreateInputMessage);
        
        HtmlOutputText tasaCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        tasaCreateOutput.setId("tasaCreateOutput");
        tasaCreateOutput.setValue("Tasa: * ");
        htmlPanelGrid.getChildren().add(tasaCreateOutput);
        
        AutoComplete tasaCreateInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        tasaCreateInput.setId("tasaCreateInput");
        tasaCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.tasa}", BigDecimal.class));
        tasaCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{creditoBean.completeTasa}", List.class, new Class[] { String.class }));
        tasaCreateInput.setDropdown(true);
        tasaCreateInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "tasa", String.class));
        tasaCreateInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{tasa}", String.class));
        tasaCreateInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{tasa}", String.class));
        tasaCreateInput.setConverter(new BigDecimalConverter());
        tasaCreateInput.setRequired(true); 
       // htmlPanelGrid.getChildren().add(tasaCreateInput);
        
        Button cbTasa = (Button)application.createComponent(Button.COMPONENT_TYPE);
        cbTasa.setValueExpression("styleClass", expressionFactory.createValueExpression(elContext, "cbTasa", String.class));
        cbTasa.setValueExpression("icon", expressionFactory.createValueExpression(elContext, "ui-icon-calculator", String.class));
        cbTasa.setValueExpression("title", expressionFactory.createValueExpression(elContext, "Calcular", String.class));
        cbTasa.setValueExpression("onclick", expressionFactory.createValueExpression(elContext, "calcularTasa();return true;", String.class));

        HtmlPanelGroup panelGroupTasa = (HtmlPanelGroup) FacesContext.getCurrentInstance().getApplication().createComponent(HtmlPanelGroup.COMPONENT_TYPE);
        panelGroupTasa.getChildren().add(tasaCreateInput);
        panelGroupTasa.getChildren().add(cbTasa);
        
        htmlPanelGrid.getChildren().add(panelGroupTasa);

        Message tasaCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        tasaCreateInputMessage.setId("tasaCreateInputMessage");
        tasaCreateInputMessage.setFor("tasaCreateInput");
        tasaCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(tasaCreateInputMessage);

//        HtmlOutputText tasaCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
//        tasaCreateOutput.setId("tasaCreateOutput");
//        tasaCreateOutput.setValue("Tasa: * ");
//        htmlPanelGrid.getChildren().add(tasaCreateOutput);
//        
//        InputText tasaCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
//        tasaCreateInput.setId("tasaCreateInput");
//        tasaCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.tasa}", BigDecimal.class));
//        tasaCreateInput.setRequired(true);
//        DoubleRangeValidator tasaCreateInputValidator = new DoubleRangeValidator();
//        tasaCreateInputValidator.setMinimum(0.01);
//        tasaCreateInput.addValidator(tasaCreateInputValidator);
//        htmlPanelGrid.getChildren().add(tasaCreateInput);
//        
//        Message tasaCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
//        tasaCreateInputMessage.setId("tasaCreateInputMessage");
//        tasaCreateInputMessage.setFor("tasaCreateInput");
//        tasaCreateInputMessage.setDisplay("icon");
//        htmlPanelGrid.getChildren().add(tasaCreateInputMessage);
        
        
        // interes punitoio
        
        
	        HtmlOutputText interesCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
	        interesCreateOutput.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.mensual}", String.class));
	        interesCreateOutput.setId("interesCreateOutput");
	        interesCreateOutput.setValue("Interes punitorio: ");
	        htmlPanelGrid.getChildren().add(interesCreateOutput);
	        
	        AutoComplete interesCreateInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
	        interesCreateInput.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.mensual}", String.class));
	        interesCreateInput.setId("interesCreateInput");
	        interesCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.interesPunitorio}", InteresPunitorio.class));
	        interesCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{creditoBean.completeInteres}", List.class, new Class[] { String.class }));
	        interesCreateInput.setDropdown(true);
	        interesCreateInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "interes", String.class));
	        interesCreateInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{interes.interes} #{interes.descripcion}", String.class));
	        interesCreateInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{interes}", InteresPunitorio.class));
	        interesCreateInput.setConverter(new InteresPunitorioConverter());
	        interesCreateInput.setRequired(false);
	        htmlPanelGrid.getChildren().add(interesCreateInput);
	        
	        Message interesCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
	        interesCreateInputMessage.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.mensual}", String.class));
	        interesCreateInputMessage.setId("interesCreateInputMessage");
	        interesCreateInputMessage.setFor("interesCreateInput");
	        interesCreateInputMessage.setDisplay("icon");
	        htmlPanelGrid.getChildren().add(interesCreateInputMessage);
	        
        // fin interes punitorio
        
        
        
	        HtmlOutputText periodoDeVencimientoCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
	        periodoDeVencimientoCreateOutput.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.diario}", String.class));
	        periodoDeVencimientoCreateOutput.setId("periodoDeVencimientoCreateOutput");
	        periodoDeVencimientoCreateOutput.setValue("Periodo De Vencimiento: * ");
	        htmlPanelGrid.getChildren().add(periodoDeVencimientoCreateOutput);
	        
//	        Spinner periodoDeVencimientoCreateInput = (Spinner) application.createComponent(Spinner.COMPONENT_TYPE);
//	        periodoDeVencimientoCreateInput.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.diario}", String.class));
//	        periodoDeVencimientoCreateInput.setId("periodoDeVencimientoCreateInput");
//	        periodoDeVencimientoCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.periodoDeVencimiento}", Integer.class));
//	        periodoDeVencimientoCreateInput.setRequired(false);
//	        periodoDeVencimientoCreateInput.setMin(1.0);
//	        LongRangeValidator periodoDeVencimientoCreateInputValidator = new LongRangeValidator();
//	        periodoDeVencimientoCreateInputValidator.setMinimum(1);
//	        periodoDeVencimientoCreateInput.addValidator(periodoDeVencimientoCreateInputValidator);
	        
	        HtmlPanelGroup pg = getDiasPanelGroup(application,
					expressionFactory, elContext);
	        
	        
	        
	        htmlPanelGrid.getChildren().add(pg);
	        
//	        Message periodoDeVencimientoCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
//	        periodoDeVencimientoCreateInputMessage.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.diario}", String.class));
//	        periodoDeVencimientoCreateInputMessage.setId("periodoDeVencimientoCreateInputMessage");
//	        periodoDeVencimientoCreateInputMessage.setFor("periodoDeVencimientoCreateInput");
//	        periodoDeVencimientoCreateInputMessage.setDisplay("icon");
//	        htmlPanelGrid.getChildren().add(periodoDeVencimientoCreateInputMessage);

        
        return htmlPanelGrid;
    }


	private HtmlPanelGroup getDiasPanelGroup(Application application,
			ExpressionFactory expressionFactory, ELContext elContext) {
		return getDiasPanelGroup(application, expressionFactory, elContext,false);
	}
	private HtmlPanelGroup getDiasPanelGroup(Application application,
			ExpressionFactory expressionFactory, ELContext elContext, boolean disabled) {
		HtmlPanelGroup pg = new HtmlPanelGroup();
		pg.getChildren().add(getCheckDia(expressionFactory, application, elContext, "L", java.util.Calendar.MONDAY,disabled));
		pg.getChildren().add(getCheckDia(expressionFactory, application, elContext, "M", java.util.Calendar.TUESDAY,disabled));
		pg.getChildren().add(getCheckDia(expressionFactory, application, elContext, "M", java.util.Calendar.WEDNESDAY,disabled));
		pg.getChildren().add(getCheckDia(expressionFactory, application, elContext, "J", java.util.Calendar.THURSDAY,disabled));
		pg.getChildren().add(getCheckDia(expressionFactory, application, elContext, "V", java.util.Calendar.FRIDAY,disabled));
		pg.getChildren().add(getCheckDia(expressionFactory, application, elContext, "S", java.util.Calendar.SATURDAY,disabled));
		return pg;
	}
	
	private SelectBooleanCheckbox getCheckDia(ExpressionFactory expressionFactory, Application application, ELContext elContext,String abrev, int  dia,boolean disabled){
		SelectBooleanCheckbox check = (SelectBooleanCheckbox) application.createComponent(SelectBooleanCheckbox.COMPONENT_TYPE);
		check.setId("idCheck"+abrev+dia);
		check.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, abrev, String.class));
		check.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.diario}", String.class));
		if (disabled){
			check.setValueExpression("disabled", expressionFactory.createValueExpression(elContext, "true", Boolean.class));
		}
		check.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.venceDia["+dia+"]}", Boolean.class));
		return check;
	}
	
	public String displayCreateDialogMensual(){
        credito = new Credito();
        credito.setTipoCredito(TipoCredito.findTipoCredito(TipoCredito.ID_MENSUAL));
        setCreateDialogVisible(true);
        return "credito";
	}
	public String displayCreateDialogDiario(){
        credito = new Credito();
        credito.setTipoCredito(TipoCredito.findTipoCredito(TipoCredito.ID_DIARIO));
        setCreateDialogVisible(true);
        return "credito";
	}
    public List<InteresPunitorio> completeInteres(String query) {
        List<InteresPunitorio> suggestions = new ArrayList<InteresPunitorio>();
        for (InteresPunitorio interes : InteresPunitorio.findAllInteresPunitorios()) {
            String tasaStr = String.valueOf(interes.getInteres() +  " "  + interes.getDescripcion());
            if (tasaStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(interes);
            }
        }
        return suggestions; 
    }



	public HtmlPanelGrid populateViewPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);

        HtmlOutputText cajaLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        cajaLabel.setId("cajaLabel");
        cajaLabel.setValue("Caja:   ");
        htmlPanelGrid.getChildren().add(cajaLabel);
        
        HtmlOutputText cajaValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        cajaValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "Caja #{creditoBean.credito.idEmpresa}", String.class));
        htmlPanelGrid.getChildren().add(cajaValue);
        
        HtmlOutputText clienteLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        clienteLabel.setId("clienteLabel");
        clienteLabel.setValue("Cliente:   ");
        htmlPanelGrid.getChildren().add(clienteLabel);
        
        HtmlOutputText clienteValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        clienteValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.cliente.nombreLargo}", String.class));
        htmlPanelGrid.getChildren().add(clienteValue);
        
        HtmlOutputText garanteLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        garanteLabel.setId("garanteLabel");
        garanteLabel.setValue("Garante:   ");
        htmlPanelGrid.getChildren().add(garanteLabel);
        
        HtmlOutputText garanteValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        garanteValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.garante.nombreLargo}", String.class));
        htmlPanelGrid.getChildren().add(garanteValue);

        HtmlOutputText tipoLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        tipoLabel.setId("tipoLabel");
        tipoLabel.setValue("Tipo:   ");
        htmlPanelGrid.getChildren().add(tipoLabel);
        
        HtmlOutputText tipoValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        tipoValue.setId("tipoValue");
        tipoValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.tipoCredito.tipo}", String.class));
        htmlPanelGrid.getChildren().add(tipoValue);

        HtmlOutputText fechaSolicitudLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        fechaSolicitudLabel.setId("fechaSolicitudLabel");
        fechaSolicitudLabel.setValue("Fecha Solicitud:   ");
        htmlPanelGrid.getChildren().add(fechaSolicitudLabel);
        
        HtmlOutputText fechaSolicitudValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        fechaSolicitudValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.fechaSolicitud}", Date.class));
        DateTimeConverter fechaSolicitudValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        fechaSolicitudValueConverter.setPattern("dd/MM/yyyy");
        fechaSolicitudValue.setConverter(fechaSolicitudValueConverter);
        htmlPanelGrid.getChildren().add(fechaSolicitudValue);
        
        HtmlOutputText montoSolicitadoLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        montoSolicitadoLabel.setId("montoSolicitadoLabel");
        montoSolicitadoLabel.setValue("Monto Solicitado:   ");
        htmlPanelGrid.getChildren().add(montoSolicitadoLabel);
        
        HtmlOutputText montoSolicitadoValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        montoSolicitadoValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.montoSolicitado}", String.class));
        htmlPanelGrid.getChildren().add(montoSolicitadoValue);
        
        HtmlOutputText cantidadCuotasLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        cantidadCuotasLabel.setId("cantidadCuotasLabel");
        cantidadCuotasLabel.setValue("Cantidad Cuotas:   ");
        htmlPanelGrid.getChildren().add(cantidadCuotasLabel);
        
        HtmlOutputText cantidadCuotasValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        cantidadCuotasValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.cantidadCuotas}", String.class));
        htmlPanelGrid.getChildren().add(cantidadCuotasValue);
        
        HtmlOutputText tasaLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        tasaLabel.setId("tasaLabel");
        tasaLabel.setValue("Tasa:   ");
        htmlPanelGrid.getChildren().add(tasaLabel);
        
        HtmlOutputText tasaValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        tasaValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.tasa}", BigDecimal.class));
        htmlPanelGrid.getChildren().add(tasaValue);

        HtmlOutputText interesLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        interesLabel.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.mensual}", String.class));
        interesLabel.setId("interesLabel");
        interesLabel.setValue("Interes punitorio:   ");
        htmlPanelGrid.getChildren().add(interesLabel);
        
        HtmlOutputText interesValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        interesValue.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.mensual}", String.class));
        interesValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.interesPunitorio.interes}", BigDecimal.class));
        htmlPanelGrid.getChildren().add(interesValue);

        HtmlOutputText periodoDeVencimientoLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        periodoDeVencimientoLabel.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.diario}", String.class));
        periodoDeVencimientoLabel.setId("periodoDeVencimientoLabel");
        periodoDeVencimientoLabel.setValue("Vence:   ");
        htmlPanelGrid.getChildren().add(periodoDeVencimientoLabel);
//        
//        HtmlOutputText periodoDeVencimientoValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
//        periodoDeVencimientoValue.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.diario}", String.class));
//        periodoDeVencimientoValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.periodoDeVencimiento}", String.class));
//        htmlPanelGrid.getChildren().add(periodoDeVencimientoValue);
        
        HtmlPanelGroup pg = getDiasPanelGroup(application,
				expressionFactory, elContext,true);
        
        htmlPanelGrid.getChildren().add(pg);
        return htmlPanelGrid;
    }



	public BigDecimal getTotalCreditos() {
		return totalCreditos;
	}



	public void setTotalCreditos(BigDecimal totalCreditos) {
		this.totalCreditos = totalCreditos;
	}

	public String displayList() {
		//Cliente.setFechaCalculoMora(new Date());
        setCreateDialogVisible(false);
        findAllCreditoes();
        setFechaCalculoMora(new Date());
        calcularTotalCreditos();
        return "credito";
    }

	public String filtrarLista() {
		//Cliente.setFechaCalculoMora(new Date());
        setCreateDialogVisible(false);
        setAllCreditoes(Credito.findCreditoesByClienteAndIdEmpresa(filtroCreditoList.getEntidadCliente(), filtroCreditoList.getIdEmpresa()));
        setDataVisible(getAllCreditoes()!=null&&!getAllCreditoes().isEmpty());
        setFechaCalculoMora(new Date());
        calcularTotalCreditos();
        return "credito";
    }
 
	private void setFechaCalculoMora(Date fecha){
		List<Credito> creditos = getAllCreditoes();
		if (creditos==null||creditos.isEmpty())return;
		for (Credito unCredito : creditos){
			unCredito.getCliente().setFechaCalculoMora(fecha);
		}
	}



	private void calcularTotalCreditos() {
		totalCreditos=new BigDecimal(0);
		totalIntereses=new BigDecimal(0);
		totalTotal=new BigDecimal(0);
		totalImpagas=new BigDecimal(0);
		List<Credito> creditoList = getAllCreditoes();
		for (Credito unCredito:creditoList){
			totalCreditos=totalCreditos.add(unCredito.getMontoSolicitado());
			totalIntereses=totalIntereses.add(unCredito.getInteres());
			totalTotal=totalTotal.add(unCredito.getMontoTotal());
			totalImpagas=totalImpagas.add(unCredito.getResumenCredito().getTotalCuotasImpagas());
		}
		
	}



	public List<Cuota> getSelectedCuotas() {
		return selectedCuotas;
	}



	public void setSelectedCuotas(List<Cuota> selectedCuotas) {
		this.selectedCuotas = selectedCuotas;
	}
	
	public String printReport(){
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/PrintServlet?reportName=credito");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<String> completeTasa(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (Tasa tasa : Tasa.findAllTasas()) {
            String tasaStr = String.valueOf(tasa.getTasa() +  " "  + tasa.getDescripcion());
            if (tasaStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(tasa.getTasa().toString());
            }
        }
        return suggestions;
    }
	
	public String calcularMontoTotal(){
		System.out.println("calcularMontoTotal");
		return null;
	}
	
	private SelectOneMenu getSelectOneMenuEmpresas(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		ExpressionFactory expressionFactory = application.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();
		
		SelectOneMenu selectOneMenu = (SelectOneMenu) application.createComponent(SelectOneMenu.COMPONENT_TYPE);
		selectOneMenu.setId("idEmpresaList");
		selectOneMenu.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{creditoBean.credito.idEmpresa}", Long.class));
			
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
		
		UISelectItem selectItem4 = (UISelectItem) application.createComponent(UISelectItem.COMPONENT_TYPE);
		selectItem4.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext,"4", Long.class));
		selectItem4.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext,"Caja 4", String.class));
		UISelectItem selectItem5 = (UISelectItem) application.createComponent(UISelectItem.COMPONENT_TYPE);
		selectItem5.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext,"5", Long.class));
		selectItem5.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext,"Caja 5", String.class));
		UISelectItem selectItem6 = (UISelectItem) application.createComponent(UISelectItem.COMPONENT_TYPE);
		selectItem6.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext,"6", Long.class));
		selectItem6.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext,"Caja 6", String.class));
		UISelectItem selectItem7 = (UISelectItem) application.createComponent(UISelectItem.COMPONENT_TYPE);
		selectItem7.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext,"7", Long.class));
		selectItem7.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext,"Caja 7", String.class));
		UISelectItem selectItem8 = (UISelectItem) application.createComponent(UISelectItem.COMPONENT_TYPE);
		selectItem8.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext,"8", Long.class));
		selectItem8.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext,"Caja 8", String.class));
		UISelectItem selectItem9 = (UISelectItem) application.createComponent(UISelectItem.COMPONENT_TYPE);
		selectItem9.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext,"9", Long.class));
		selectItem9.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext,"Caja 9", String.class));
		menuChildren.add(selectItem1);
		menuChildren.add(selectItem2);
		menuChildren.add(selectItem3);
		menuChildren.add(selectItem4);
		menuChildren.add(selectItem5);
		menuChildren.add(selectItem6);
		menuChildren.add(selectItem7);
		menuChildren.add(selectItem8);
		menuChildren.add(selectItem9);
		return selectOneMenu;
	}
	
	public ClienteConverter getClienteConverter(){
		return new ClienteConverter();
	}
	
	public List<SelectItem> getFiltroCajaItems(){
		List<SelectItem> list = new ArrayList<SelectItem>();
		list.add(new SelectItem(-1L, "Seleccione"));
		list.add(new SelectItem(1L, "Caja 1"));
		list.add(new SelectItem(2L, "Caja 2"));
		list.add(new SelectItem(3L, "Caja 3"));

		list.add(new SelectItem(4L, "Caja 4"));
		list.add(new SelectItem(5L, "Caja 5"));
		list.add(new SelectItem(6L, "Caja 6"));
		list.add(new SelectItem(7L, "Caja 7"));
		list.add(new SelectItem(8L, "Caja 8"));
		list.add(new SelectItem(9L, "Caja 9"));

		return list;
	}



	public BigDecimal getTotalIntereses() {
		return totalIntereses;
	}



	public void setTotalIntereses(BigDecimal totalIntereses) {
		this.totalIntereses = totalIntereses;
	}



	public BigDecimal getTotalTotal() {
		return totalTotal;
	}



	public void setTotalTotal(BigDecimal totalTotal) {
		this.totalTotal = totalTotal;
	}



	public FiltroCuotaList getFiltroCreditoList() {
		return filtroCreditoList;
	}



	public void setFiltroCreditoList(FiltroCuotaList filtroCreditoList) {
		this.filtroCreditoList = filtroCreditoList;
	}



	public BigDecimal getTotalImpagas() {
		return totalImpagas;
	}



	public void setTotalImpagas(BigDecimal totalImpagas) {
		this.totalImpagas = totalImpagas;
	}

	public HtmlPanelGrid populateEditPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        
        HtmlPanelGroup pg = getDiasPanelGroup(application,
				expressionFactory, elContext);
        
        htmlPanelGrid.getChildren().add(pg);

        return htmlPanelGrid;
    }
    public String onEdit() {
    	credito.setVenceDiaFromCuotas();
        return null;
    }
    public String onView() {
    	credito.setVenceDiaFromCuotas();
        return null;
    }


}
