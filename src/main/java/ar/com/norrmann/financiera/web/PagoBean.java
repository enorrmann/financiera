package ar.com.norrmann.financiera.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.validator.DoubleRangeValidator;

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.message.Message;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import ar.com.norrmann.financiera.model.Cobrador;
import ar.com.norrmann.financiera.model.Cuota;
import ar.com.norrmann.financiera.model.Pago;
import ar.com.norrmann.financiera.model.Zona;
import ar.com.norrmann.financiera.web.converter.CobradorConverter;
import ar.com.norrmann.financiera.web.util.I18N;

@RooSerializable
@RooJsfManagedBean(entity = Pago.class, beanName = "pagoBean")
public class PagoBean {
	private Cuota cuota;

	@ManagedProperty("#{cuotaBean}")
	private CuotaBean cuotaBean;

	private BigDecimal totalPagos=new BigDecimal(0);
	private FiltroCuotaList filtroPagoList = new FiltroCuotaList();

	public String doNuevoPago() {
		setPago(new Pago());
		getPago().setCuota(cuota);
		setCreateDialogVisible(true);
		return null;
	}

	public Cuota getCuota() {
		return cuota;
	}

	public void setCuota(Cuota cuota) {
		this.cuota = cuota;
	}

	public HtmlPanelGrid populateCreatePanel() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		ExpressionFactory expressionFactory = application
				.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();

		HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application
				.createComponent(HtmlPanelGrid.COMPONENT_TYPE);

		// HtmlOutputText cuotaCreateOutput = (HtmlOutputText)
		// application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		// cuotaCreateOutput.setId("cuotaCreateOutput");
		// cuotaCreateOutput.setValue("Cuota: * ");
		// htmlPanelGrid.getChildren().add(cuotaCreateOutput);
		//
		// AutoComplete cuotaCreateInput = (AutoComplete)
		// application.createComponent(AutoComplete.COMPONENT_TYPE);
		// cuotaCreateInput.setId("cuotaCreateInput");
		// cuotaCreateInput.setValueExpression("value",
		// expressionFactory.createValueExpression(elContext,
		// "#{pagoBean.pago.cuota}", Cuota.class));
		// cuotaCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext,
		// "#{pagoBean.completeCuota}", List.class, new Class[] { String.class
		// }));
		// cuotaCreateInput.setDropdown(true);
		// cuotaCreateInput.setValueExpression("var",
		// expressionFactory.createValueExpression(elContext, "cuota",
		// String.class));
		// cuotaCreateInput.setValueExpression("itemLabel",
		// expressionFactory.createValueExpression(elContext,
		// "#{cuota.numero} #{cuota.fechaVencimiento} #{cuota.importe}",
		// String.class));
		// cuotaCreateInput.setValueExpression("itemValue",
		// expressionFactory.createValueExpression(elContext, "#{cuota}",
		// Cuota.class));
		// cuotaCreateInput.setConverter(new CuotaConverter());
		// cuotaCreateInput.setRequired(true);
		// htmlPanelGrid.getChildren().add(cuotaCreateInput);
		//
		// Message cuotaCreateInputMessage = (Message)
		// application.createComponent(Message.COMPONENT_TYPE);
		// cuotaCreateInputMessage.setId("cuotaCreateInputMessage");
		// cuotaCreateInputMessage.setFor("cuotaCreateInput");
		// cuotaCreateInputMessage.setDisplay("icon");
		// htmlPanelGrid.getChildren().add(cuotaCreateInputMessage);

//		HtmlOutputText cobradorCreateOutput = (HtmlOutputText) application
//				.createComponent(HtmlOutputText.COMPONENT_TYPE);
//		cobradorCreateOutput.setId("cobradorCreateOutput");
//		cobradorCreateOutput.setValue("Cobrador: * ");
//		htmlPanelGrid.getChildren().add(cobradorCreateOutput);
//
//		AutoComplete cobradorCreateInput = (AutoComplete) application
//				.createComponent(AutoComplete.COMPONENT_TYPE);
//		cobradorCreateInput.setId("cobradorCreateInput");
//		cobradorCreateInput.setValueExpression("value", expressionFactory
//				.createValueExpression(elContext, "#{pagoBean.pago.cobrador}",
//						Cobrador.class));
//		cobradorCreateInput.setCompleteMethod(expressionFactory
//				.createMethodExpression(elContext,
//						"#{pagoBean.completeCobrador}", List.class,
//						new Class[] { String.class }));
//		cobradorCreateInput.setDropdown(true);
//		cobradorCreateInput.setValueExpression("var", expressionFactory
//				.createValueExpression(elContext, "cobrador", String.class));
//		cobradorCreateInput.setValueExpression("itemLabel", expressionFactory
//				.createValueExpression(elContext,
//						"#{cobrador.apellidos} #{cobrador.nombres}",
//						String.class));
//		cobradorCreateInput.setValueExpression("itemValue",
//				expressionFactory.createValueExpression(elContext,
//						"#{cobrador}", Cobrador.class));
//		cobradorCreateInput.setConverter(new CobradorConverter());
//		cobradorCreateInput.setRequired(true);
//		htmlPanelGrid.getChildren().add(cobradorCreateInput);
//
//		Message cobradorCreateInputMessage = (Message) application
//				.createComponent(Message.COMPONENT_TYPE);
//		cobradorCreateInputMessage.setId("cobradorCreateInputMessage");
//		cobradorCreateInputMessage.setFor("cobradorCreateInput");
//		cobradorCreateInputMessage.setDisplay("icon");
//		htmlPanelGrid.getChildren().add(cobradorCreateInputMessage);

		

		HtmlOutputText importeCreateOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		importeCreateOutput.setId("importeCreateOutput");
		importeCreateOutput.setValue("Importe: * ");
		htmlPanelGrid.getChildren().add(importeCreateOutput);

		InputText importeCreateInput = (InputText) application
				.createComponent(InputText.COMPONENT_TYPE);
		importeCreateInput.setId("importeCreateInput");
		importeCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{pagoBean.pago.importe}",
						BigDecimal.class));
		importeCreateInput.setRequired(true);
		DoubleRangeValidator importeCreateInputValidator = new DoubleRangeValidator();
		importeCreateInputValidator.setMinimum(0.01);
		importeCreateInput.addValidator(importeCreateInputValidator);
		htmlPanelGrid.getChildren().add(importeCreateInput);

		Message importeCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		importeCreateInputMessage.setId("importeCreateInputMessage");
		importeCreateInputMessage.setFor("importeCreateInput");
		importeCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(importeCreateInputMessage);

		HtmlOutputText fechaPagoCreateOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		fechaPagoCreateOutput.setId("fechaPagoCreateOutput");
		fechaPagoCreateOutput.setValue("Fecha de Pago: *  ");
		htmlPanelGrid.getChildren().add(fechaPagoCreateOutput);

		Calendar fechaPagoCreateInput = (Calendar) application
				.createComponent(Calendar.COMPONENT_TYPE);
		fechaPagoCreateInput.setId("fechaPagoCreateInput");
		fechaPagoCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{pagoBean.pago.fechaPago}",
						Date.class));
		fechaPagoCreateInput.setNavigator(true);
		fechaPagoCreateInput.setEffect("slideDown");
		fechaPagoCreateInput.setPattern("dd/MM/yyyy");
		fechaPagoCreateInput.setRequired(true);
		htmlPanelGrid.getChildren().add(fechaPagoCreateInput);

		Message fechaPagoCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		fechaPagoCreateInputMessage.setId("fechaPagoCreateInputMessage");
		fechaPagoCreateInputMessage.setFor("fechaPagoCreateInput");
		fechaPagoCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(fechaPagoCreateInputMessage);
		
		return htmlPanelGrid;
	}

	public String persist() {
		Zona zona = Cuota.findZonaByCuota(getPago().getCuota().getId());
		Cobrador cobrador = Cobrador.findCobradorByZona(zona);
		if (cobrador==null){
			FacesMessage facesMessage = new FacesMessage("No hay cobradores para la zona "+zona.getZona());
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			return null;
		}

		Pago pago = getPago();
		pago.setCobrador(cobrador);
		String message = "";
		BigDecimal importePagado = pago.getImporte();
		BigDecimal saldoCuota = pago.getCuota().getSaldo();
		if (importePagado.compareTo(saldoCuota) == 1) {
			FacesMessage facesMessage = new FacesMessage(
					"El pago no puede superar el saldo de la cuota ( $ "
							+ pago.getCuota().getSaldo() + ")");
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			return null;
		}
		if (pago.getId() != null) {
			pago.merge();
			message = I18N.ACTUALIZADO_OK;
		} else {
			pago.persist();
			Cuota cuota = pago.getCuota();
			BigDecimal saldo = cuota.getSaldo();
			if (saldo.compareTo(BigDecimal.ZERO) == 0) {
				cuota.saldar();
			}
			message = I18N.CREADO_OK;
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("createDialog.hide()");
		context.execute("editDialog.hide()");

		FacesMessage facesMessage = new FacesMessage(message);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
		cuotaBean.filtrarLista();
		filtrarLista();
		return null;

	}

	public CuotaBean getCuotaBean() {
		return cuotaBean;
	}

	public void setCuotaBean(CuotaBean cuotaBean) {
		this.cuotaBean = cuotaBean;
	}

	public HtmlPanelGrid populateEditPanel() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		ExpressionFactory expressionFactory = application
				.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();

		HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application
				.createComponent(HtmlPanelGrid.COMPONENT_TYPE);

		HtmlOutputText cobradorEditOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		cobradorEditOutput.setId("cobradorEditOutput");
		cobradorEditOutput.setValue("Cobrador: * ");
		htmlPanelGrid.getChildren().add(cobradorEditOutput);

		AutoComplete cobradorEditInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		cobradorEditInput.setId("cobradorEditInput");
		cobradorEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{pagoBean.pago.cobrador}",
						Cobrador.class));
		cobradorEditInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{pagoBean.completeCobrador}", List.class,
						new Class[] { String.class }));
		cobradorEditInput.setDropdown(true);
		cobradorEditInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "cobrador", String.class));
		cobradorEditInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext,
						"#{cobrador.apellidos} #{cobrador.nombres}",
						String.class));
		cobradorEditInput.setValueExpression("itemValue",
				expressionFactory.createValueExpression(elContext,
						"#{cobrador}", Cobrador.class));
		cobradorEditInput.setConverter(new CobradorConverter());
		cobradorEditInput.setRequired(true);
		htmlPanelGrid.getChildren().add(cobradorEditInput);

		Message cobradorEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		cobradorEditInputMessage.setId("cobradorEditInputMessage");
		cobradorEditInputMessage.setFor("cobradorEditInput");
		cobradorEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(cobradorEditInputMessage);

		HtmlOutputText fechaPagoEditOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		fechaPagoEditOutput.setId("fechaPagoEditOutput");
		fechaPagoEditOutput.setValue("Fecha Pago:   ");
		htmlPanelGrid.getChildren().add(fechaPagoEditOutput);

		Calendar fechaPagoEditInput = (Calendar) application
				.createComponent(Calendar.COMPONENT_TYPE);
		fechaPagoEditInput.setId("fechaPagoEditInput");
		fechaPagoEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{pagoBean.pago.fechaPago}",
						Date.class));
		fechaPagoEditInput.setNavigator(true);
		fechaPagoEditInput.setEffect("slideDown");
		fechaPagoEditInput.setPattern("dd/MM/yyyy");
		fechaPagoEditInput.setRequired(false);
		htmlPanelGrid.getChildren().add(fechaPagoEditInput);

		Message fechaPagoEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		fechaPagoEditInputMessage.setId("fechaPagoEditInputMessage");
		fechaPagoEditInputMessage.setFor("fechaPagoEditInput");
		fechaPagoEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(fechaPagoEditInputMessage);

		HtmlOutputText importeEditOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		importeEditOutput.setId("importeEditOutput");
		importeEditOutput.setValue("Importe: * ");
		htmlPanelGrid.getChildren().add(importeEditOutput);

		InputText importeEditInput = (InputText) application
				.createComponent(InputText.COMPONENT_TYPE);
		importeEditInput.setId("importeEditInput");
		importeEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{pagoBean.pago.importe}",
						BigDecimal.class));
		importeEditInput.setRequired(true);
		DoubleRangeValidator importeEditInputValidator = new DoubleRangeValidator();
		importeEditInputValidator.setMinimum(0.01);
		importeEditInput.addValidator(importeEditInputValidator);
		htmlPanelGrid.getChildren().add(importeEditInput);

		Message importeEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		importeEditInputMessage.setId("importeEditInputMessage");
		importeEditInputMessage.setFor("importeEditInput");
		importeEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(importeEditInputMessage);

		return htmlPanelGrid;
	}

	public HtmlPanelGrid populateViewPanel() {
		return null;
	}

	public void handleDialogClose(CloseEvent event) {
		reset();
		filtrarLista();
	}

	public FiltroCuotaList getFiltroPagoList() {
		return filtroPagoList;
	}

	public void setFiltroPagoList(FiltroCuotaList filtroPagoList) {
		this.filtroPagoList = filtroPagoList;
	}

	public String filtrarLista() {
		setCreateDialogVisible(false);
		setAllPagoes(Pago.findPagoesByFechaPagoBetweenAndZona(
				filtroPagoList.getFechaDesde(), filtroPagoList.getFechaHasta(),
				filtroPagoList.getZona()));
		setDataVisible(getAllPagoes() != null && !getAllPagoes().isEmpty());
		calcularTotales();
		return "pago";
	}

	private void calcularTotales() {
		List<Pago> pagoList = getAllPagoes();
		totalPagos=new BigDecimal(0);
		if (pagoList == null || pagoList.isEmpty()) return;
		for (Pago unPago : pagoList) {
			totalPagos=totalPagos.add(unPago.getImporte());
		}

	}

	public String delete() {
		getPago().getCuota().impagar();
		getPago().remove();
		FacesMessage facesMessage = new FacesMessage(I18N.BORRADO_OK);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
		return filtrarLista();
	}

	public BigDecimal getTotalPagos() {
		return totalPagos;
	}

	public void setTotalPagos(BigDecimal totalPagos) {
		this.totalPagos = totalPagos;
	}
	public String printReport(){
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/PrintServlet?reportName=recibo");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
