package ar.com.norrmann.financiera.web;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.el.ELContext; 
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.message.Message;
import org.primefaces.context.RequestContext;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import ar.com.norrmann.financiera.model.Cliente;
import ar.com.norrmann.financiera.model.Zona;
import ar.com.norrmann.financiera.report.DateUtils;
import ar.com.norrmann.financiera.web.converter.ZonaConverter;
import ar.com.norrmann.financiera.web.util.I18N;
import ar.com.norrmann.financiera.web.util.MailValidator;

@RooSerializable
@RooJsfManagedBean(entity = Cliente.class, beanName = "clienteBean")
public class ClienteBean {

	private Cliente cliente;
	private BigDecimal totalMora;
	private FiltroCuotaList filtroCliente = new FiltroCuotaList();
	private TipoListado tipoListado;
	
	private static Properties templates = new Properties();
	
	static {
		String rutaQuerys = "ar/com/norrmann/financiera/templates/intimaciones.xml";
		InputStream defConsultas = ClienteBean.class.getClassLoader().getResourceAsStream(rutaQuerys);
		try {
			templates.loadFromXML(defConsultas);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Cliente getCliente() {
		if (cliente == null) {
			cliente = new Cliente();
		}
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public String actualizarObservaciones(){
		if (cliente!=null && cliente.getId()!=null){
			cliente.merge();
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("viewDialog.hide()");
			reset();
		}
		if (tipoListado.equals(TipoListado.MOROSOS)){
			return displayMoraList();
		} else {
			return displayList();
		}
	}

	public String persist() {
		String message = "";
		if (cliente.getId() != null) {
			cliente.merge();
			message = I18N.ACTUALIZADO_OK;
		} else {
			cliente.persist();
			message = I18N.CREADO_OK;
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("createDialog.hide()");
		context.execute("editDialog.hide()");

		FacesMessage facesMessage = new FacesMessage(message);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
		return findAllClientes();
	}

	public String delete() {
		cliente.remove();
		FacesMessage facesMessage = new FacesMessage(I18N.BORRADO_OK);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
		return findAllClientes();
	}

	public String displayMoraList() {
		tipoListado = TipoListado.MOROSOS;
		Zona zona = filtroCliente.getZona();
		// seteo fecha de calculo seleccionada en el buscador y filtro en base a
		// eso
		//Cliente.setFechaCalculoMora(filtroCliente.getFechaHasta());
		setCreateDialogVisible(false);
		setAllClientes(Cliente.findClientesEnMora(zona,filtroCliente.getFechaHasta()));
		setDataVisible(getAllClientes() != null && !getAllClientes().isEmpty());
		setFechaCalculoMora(filtroCliente.getFechaHasta());
		calcularTotalMora();
		return "cliente";
	}

	private void calcularTotalMora() {
		this.totalMora = new BigDecimal(0);
		List<Cliente> clientesEnMora = getAllClientes();
		if (clientesEnMora == null)
			return;
		for (Cliente uncliente : clientesEnMora) {
			totalMora = totalMora.add(uncliente.getDebe());
		}

	}

	public BigDecimal getTotalMora() {
		return totalMora;
	}

	public void setTotalMora(BigDecimal totalMora) {
		this.totalMora = totalMora;
	}

	public String displayList() {
		tipoListado = TipoListado.TODOS;
		//Cliente.setFechaCalculoMora(new Date());
		this.totalMora = null;
		setCreateDialogVisible(false);
		findAllClientes();
		setFechaCalculoMora(new Date());
		return "cliente";
	}
	
	public String displayDiasCobroList(){
		tipoListado = TipoListado.DIAS_COBRO;
		this.totalMora = null;
		setCreateDialogVisible(false);
		findAllClientes();
		setFechaCalculoMora(new Date());
		return "dias_cobro";	
	}

	private void setFechaCalculoMora(Date fecha){
		List<Cliente> clientes = getAllClientes();
		if (clientes==null||clientes.isEmpty())return;
		for (Cliente unCliente : clientes){
			unCliente.setFechaCalculoMora(fecha);
		}
	}
	public String displayOrdenList() {
		//Cliente.setFechaCalculoMora(new Date());
		tipoListado = TipoListado.ORDEN_COBRANZA;
		this.totalMora = null;
		setCreateDialogVisible(false);
		setAllClientes(Cliente.findAllClientesOrdenCobranza(filtroCliente
				.getZona()));
		setDataVisible(getAllClientes() != null && !getAllClientes().isEmpty());
		setFechaCalculoMora(new Date());
		return "orden_cliente";
	}

	public String confirmarOrden() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest myRequest = (HttpServletRequest) context
				.getExternalContext().getRequest();
		String idList = myRequest.getParameter("idList");
		if (idList == null || idList.trim().isEmpty()) {
			return "orden_cliente";
		}

		String[] idArray = idList.split(",");
		updateOrdenCobranza(idArray);

		return displayOrdenList();
	}

	public HtmlPanelGrid populateCreatePanel() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		ExpressionFactory expressionFactory = application
				.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();

		HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application
				.createComponent(HtmlPanelGrid.COMPONENT_TYPE);

		HtmlOutputText apellidosCreateOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		apellidosCreateOutput.setId("apellidosCreateOutput");
		apellidosCreateOutput.setValue("Apellidos: * ");
		htmlPanelGrid.getChildren().add(apellidosCreateOutput);

		InputText apellidosCreateInput = (InputText) application
				.createComponent(InputText.COMPONENT_TYPE);
		apellidosCreateInput.setId("apellidosCreateInput");
		apellidosCreateInput.setRequired(true);
		apellidosCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{clienteBean.cliente.apellidos}", String.class));
		htmlPanelGrid.getChildren().add(apellidosCreateInput);

		Message apellidosCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		apellidosCreateInputMessage.setId("apellidosCreateInputMessage");
		apellidosCreateInputMessage.setFor("apellidosCreateInput");
		apellidosCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(apellidosCreateInputMessage);

		HtmlOutputText nombresCreateOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		nombresCreateOutput.setId("nombresCreateOutput");
		nombresCreateOutput.setValue("Nombres: * ");
		htmlPanelGrid.getChildren().add(nombresCreateOutput);

		InputText nombresCreateInput = (InputText) application
				.createComponent(InputText.COMPONENT_TYPE);
		nombresCreateInput.setId("nombresCreateInput");
		nombresCreateInput.setRequired(true);
		nombresCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{clienteBean.cliente.nombres}", String.class));
		htmlPanelGrid.getChildren().add(nombresCreateInput);

		Message nombresCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		nombresCreateInputMessage.setId("nombresCreateInputMessage");
		nombresCreateInputMessage.setFor("nombresCreateInput");
		nombresCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(nombresCreateInputMessage);

		HtmlOutputText domicilioCreateOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		domicilioCreateOutput.setId("domicilioCreateOutput");
		domicilioCreateOutput.setValue("Domicilio:   ");
		htmlPanelGrid.getChildren().add(domicilioCreateOutput);

		InputText domicilioCreateInput = (InputText) application
				.createComponent(InputText.COMPONENT_TYPE);
		domicilioCreateInput.setId("domicilioCreateInput");
		domicilioCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{clienteBean.cliente.domicilio}", String.class));
		htmlPanelGrid.getChildren().add(domicilioCreateInput);

		Message domicilioCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		domicilioCreateInputMessage.setId("domicilioCreateInputMessage");
		domicilioCreateInputMessage.setFor("domicilioCreateInput");
		domicilioCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(domicilioCreateInputMessage);

		HtmlOutputText domicilioComercialCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		domicilioComercialCreateOutput.setId("domicilioComercialCreateOutput");
		domicilioComercialCreateOutput.setValue("Domicilio Comercial:   ");
		htmlPanelGrid.getChildren().add(domicilioComercialCreateOutput);

		InputText domicilioComercialCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
		domicilioComercialCreateInput.setId("domicilioComercialCreateInput");
		domicilioComercialCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext,
		"#{clienteBean.cliente.domicilioComercial}", String.class));
		htmlPanelGrid.getChildren().add(domicilioComercialCreateInput);

		Message domicilioComercialCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
		domicilioComercialCreateInputMessage.setId("domicilioComercialCreateInputMessage");
		domicilioComercialCreateInputMessage.setFor("domicilioComercialCreateInput");
		domicilioComercialCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(domicilioComercialCreateInputMessage);
		
		HtmlOutputText telefonoCreateOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		telefonoCreateOutput.setId("telefonoCreateOutput");
		telefonoCreateOutput.setValue("Telefono:   ");
		htmlPanelGrid.getChildren().add(telefonoCreateOutput);

		InputText telefonoCreateInput = (InputText) application
				.createComponent(InputText.COMPONENT_TYPE);
		telefonoCreateInput.setId("telefonoCreateInput");
		telefonoCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{clienteBean.cliente.telefono}", String.class));
		htmlPanelGrid.getChildren().add(telefonoCreateInput);

		Message telefonoCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		telefonoCreateInputMessage.setId("telefonoCreateInputMessage");
		telefonoCreateInputMessage.setFor("telefonoCreateInput");
		telefonoCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(telefonoCreateInputMessage);

		HtmlOutputText telefonoReferenciaCreateOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		telefonoReferenciaCreateOutput.setId("telefonoReferenciaCreateOutput");
		telefonoReferenciaCreateOutput.setValue("Telefono de Referencia:   ");
		htmlPanelGrid.getChildren().add(telefonoReferenciaCreateOutput);

		InputText telefonoReferenciaCreateInput = (InputText) application
				.createComponent(InputText.COMPONENT_TYPE);
		telefonoReferenciaCreateInput.setId("telefonoReferenciaCreateInput");
		telefonoReferenciaCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{clienteBean.cliente.telefonoDeReferencia}", String.class));
		htmlPanelGrid.getChildren().add(telefonoReferenciaCreateInput);

		Message telefonoReferenciaCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		telefonoReferenciaCreateInputMessage.setId("telefonoReferenciaCreateInputMessage");
		telefonoReferenciaCreateInputMessage.setFor("telefonoReferenciaCreateInput");
		telefonoReferenciaCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(telefonoReferenciaCreateInputMessage);

		HtmlOutputText emailCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		emailCreateOutput.setId("emailCreateOutput");
		emailCreateOutput.setValue("e-mail: ");
		htmlPanelGrid.getChildren().add(emailCreateOutput);

		InputText emailCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
		emailCreateInput.setId("emailCreateInput");
		emailCreateInput.setRequired(true);
		emailCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext,"#{clienteBean.cliente.eMail}", String.class));
		htmlPanelGrid.getChildren().add(emailCreateInput);

		Message emailCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
		emailCreateInputMessage.setId("emailCreateInputMessage");
		emailCreateInputMessage.setFor("emailCreateInput");
		emailCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(emailCreateInputMessage);
		
		HtmlOutputText dniCreateOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		dniCreateOutput.setId("dniCreateOutput");
		dniCreateOutput.setValue("Dni:   ");
		htmlPanelGrid.getChildren().add(dniCreateOutput);

		InputText dniCreateInput = (InputText) application
				.createComponent(InputText.COMPONENT_TYPE);
		dniCreateInput.setId("dniCreateInput");
		dniCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{clienteBean.cliente.dni}",
						String.class));
		htmlPanelGrid.getChildren().add(dniCreateInput);

		Message dniCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		dniCreateInputMessage.setId("dniCreateInputMessage");
		dniCreateInputMessage.setFor("dniCreateInput");
		dniCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(dniCreateInputMessage);


		HtmlOutputText domicilioLaboralCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		domicilioLaboralCreateOutput.setId("domicilioLaboralCreateOutput");
		domicilioLaboralCreateOutput.setValue("Domicilio Laboral:   ");
		htmlPanelGrid.getChildren().add(domicilioLaboralCreateOutput);

		InputText domicilioLaboralCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
		domicilioLaboralCreateInput.setId("domicilioLaboralCreateInput");
		domicilioLaboralCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext,
		"#{clienteBean.cliente.domicilioLaboral}", String.class));
		htmlPanelGrid.getChildren().add(domicilioLaboralCreateInput);

		Message domicilioLaboralCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
		domicilioLaboralCreateInputMessage.setId("domicilioLaboralCreateInputMessage");
		domicilioLaboralCreateInputMessage.setFor("domicilioLaboralCreateInput");
		domicilioLaboralCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(domicilioLaboralCreateInputMessage);

		
		HtmlOutputText telefonoLaboralCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		telefonoLaboralCreateOutput.setId("telefonoLaboralCreateOutput");
		telefonoLaboralCreateOutput.setValue("Telefono Laboral:   ");
		htmlPanelGrid.getChildren().add(telefonoLaboralCreateOutput);

		InputText telefonoLaboralCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
		telefonoLaboralCreateInput.setId("telefonoLaboralCreateInput");
		telefonoLaboralCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext,
		"#{clienteBean.cliente.telefonoLaboral}", String.class));
		htmlPanelGrid.getChildren().add(telefonoLaboralCreateInput);

		Message telefonoLaboralCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
		telefonoLaboralCreateInputMessage.setId("telefonoLaboralCreateInputMessage");
		telefonoLaboralCreateInputMessage.setFor("telefonoLaboralCreateInput");
		telefonoLaboralCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(telefonoLaboralCreateInputMessage);

		HtmlOutputText sueldoCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		sueldoCreateOutput.setId("sueldoCreateOutput");
		sueldoCreateOutput.setValue("Sueldo:   ");
		htmlPanelGrid.getChildren().add(sueldoCreateOutput);

		InputText sueldoCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
		sueldoCreateInput.setId("sueldoCreateInput");
		sueldoCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext,
		"#{clienteBean.cliente.sueldo}", String.class));
		htmlPanelGrid.getChildren().add(sueldoCreateInput);

		Message sueldoCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
		sueldoCreateInputMessage.setId("sueldoCreateInputMessage");
		sueldoCreateInputMessage.setFor("sueldoCreateInput");
		sueldoCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(sueldoCreateInputMessage);

		HtmlOutputText zonaCreateOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		zonaCreateOutput.setId("zonaCreateOutput");
		zonaCreateOutput.setValue("Zona:   ");
		htmlPanelGrid.getChildren().add(zonaCreateOutput);

		AutoComplete zonaCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		zonaCreateInput.setId("zonaCreateInput");
		zonaCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{clienteBean.cliente.zona}", Zona.class));
		zonaCreateInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{clienteBean.completeZona}", List.class,
						new Class[] { String.class }));
		zonaCreateInput.setDropdown(true);
		zonaCreateInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "zona", String.class));
		zonaCreateInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext,
						"#{zona.zona} #{zona.descripcion}", String.class));
		zonaCreateInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{zona}", Zona.class));
		zonaCreateInput.setConverter(new ZonaConverter());
		zonaCreateInput.setRequired(true);
		htmlPanelGrid.getChildren().add(zonaCreateInput);

		Message zonaCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		zonaCreateInputMessage.setId("zonaCreateInputMessage");
		zonaCreateInputMessage.setFor("zonaCreateInput");
		zonaCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(zonaCreateInputMessage);

		return htmlPanelGrid;
	}

	public HtmlPanelGrid populateEditPanel() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		ExpressionFactory expressionFactory = application
				.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();

		HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application
				.createComponent(HtmlPanelGrid.COMPONENT_TYPE);

		HtmlOutputText apellidosEditOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		apellidosEditOutput.setId("apellidosEditOutput");
		apellidosEditOutput.setValue("Apellidos: * ");
		htmlPanelGrid.getChildren().add(apellidosEditOutput);

		InputText apellidosEditInput = (InputText) application
				.createComponent(InputText.COMPONENT_TYPE);
		apellidosEditInput.setId("apellidosEditInput");
		apellidosEditInput.setRequired(true);
		apellidosEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{clienteBean.cliente.apellidos}", String.class));
		htmlPanelGrid.getChildren().add(apellidosEditInput);

		Message apellidosEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		apellidosEditInputMessage.setId("apellidosEditInputMessage");
		apellidosEditInputMessage.setFor("apellidosEditInput");
		apellidosEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(apellidosEditInputMessage);

		HtmlOutputText nombresEditOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		nombresEditOutput.setId("nombresEditOutput");
		nombresEditOutput.setValue("Nombres: * ");
		htmlPanelGrid.getChildren().add(nombresEditOutput);

		InputText nombresEditInput = (InputText) application
				.createComponent(InputText.COMPONENT_TYPE);
		nombresEditInput.setId("nombresEditInput");
		nombresEditInput.setRequired(true);
		nombresEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{clienteBean.cliente.nombres}", String.class));
		htmlPanelGrid.getChildren().add(nombresEditInput);

		Message nombresEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		nombresEditInputMessage.setId("nombresEditInputMessage");
		nombresEditInputMessage.setFor("nombresEditInput");
		nombresEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(nombresEditInputMessage);

		HtmlOutputText domicilioEditOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		domicilioEditOutput.setId("domicilioEditOutput");
		domicilioEditOutput.setValue("Domicilio:   ");
		htmlPanelGrid.getChildren().add(domicilioEditOutput);

		InputText domicilioEditInput = (InputText) application
				.createComponent(InputText.COMPONENT_TYPE);
		domicilioEditInput.setId("domicilioEditInput");
		domicilioEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{clienteBean.cliente.domicilio}", String.class));
		htmlPanelGrid.getChildren().add(domicilioEditInput);

		Message domicilioEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		domicilioEditInputMessage.setId("domicilioEditInputMessage");
		domicilioEditInputMessage.setFor("domicilioEditInput");
		domicilioEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(domicilioEditInputMessage);

		HtmlOutputText domicilioComercialEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		domicilioComercialEditOutput.setId("domicilioComercialEditOutput");
		domicilioComercialEditOutput.setValue("Domicilio Comercial:   ");
		htmlPanelGrid.getChildren().add(domicilioComercialEditOutput);

		InputText domicilioComercialEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
		domicilioComercialEditInput.setId("domicilioComercialEditInput");
		domicilioComercialEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext,
		"#{clienteBean.cliente.domicilioComercial}", String.class));
		htmlPanelGrid.getChildren().add(domicilioComercialEditInput);

		Message domicilioComercialEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
		domicilioComercialEditInputMessage.setId("domicilioComercialEditInputMessage");
		domicilioComercialEditInputMessage.setFor("domicilioComercialEditInput");
		domicilioComercialEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(domicilioComercialEditInputMessage);

		HtmlOutputText telefonoEditOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		telefonoEditOutput.setId("telefonoEditOutput");
		telefonoEditOutput.setValue("Telefono:   ");
		htmlPanelGrid.getChildren().add(telefonoEditOutput);

		InputText telefonoEditInput = (InputText) application
				.createComponent(InputText.COMPONENT_TYPE);
		telefonoEditInput.setId("telefonoEditInput");
		telefonoEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{clienteBean.cliente.telefono}", String.class));
		htmlPanelGrid.getChildren().add(telefonoEditInput);

		Message telefonoEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		telefonoEditInputMessage.setId("telefonoEditInputMessage");
		telefonoEditInputMessage.setFor("telefonoEditInput");
		telefonoEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(telefonoEditInputMessage);

		HtmlOutputText telefonoReferenciaCreateOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		telefonoReferenciaCreateOutput.setId("telefonoReferenciaCreateOutput");
		telefonoReferenciaCreateOutput.setValue("Telefono de Referencia:   ");
		htmlPanelGrid.getChildren().add(telefonoReferenciaCreateOutput);

		InputText telefonoReferenciaCreateInput = (InputText) application
				.createComponent(InputText.COMPONENT_TYPE);
		telefonoReferenciaCreateInput.setId("telefonoReferenciaCreateInput");
		telefonoReferenciaCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{clienteBean.cliente.telefonoDeReferencia}", String.class));
		htmlPanelGrid.getChildren().add(telefonoReferenciaCreateInput);

		Message telefonoReferenciaCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		telefonoReferenciaCreateInputMessage.setId("telefonoReferenciaCreateInputMessage");
		telefonoReferenciaCreateInputMessage.setFor("telefonoReferenciaCreateInput");
		telefonoReferenciaCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(telefonoReferenciaCreateInputMessage);

		HtmlOutputText emailEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		emailEditOutput.setId("emailEditOutput");
		emailEditOutput.setValue("e-mail: ");
		htmlPanelGrid.getChildren().add(emailEditOutput);

		InputText emailEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
		emailEditInput.setId("emailEditInput");
		emailEditInput.setRequired(true);
		emailEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext,"#{clienteBean.cliente.eMail}", String.class));
		htmlPanelGrid.getChildren().add(emailEditInput);

		Message emailEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
		emailEditInputMessage.setId("emailEditInputMessage");
		emailEditInputMessage.setFor("emailEditInput");
		emailEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(emailEditInputMessage);		
		
		HtmlOutputText dniEditOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		dniEditOutput.setId("dniEditOutput");
		dniEditOutput.setValue("Dni:   ");
		htmlPanelGrid.getChildren().add(dniEditOutput);

		InputText dniEditInput = (InputText) application
				.createComponent(InputText.COMPONENT_TYPE);
		dniEditInput.setId("dniEditInput");
		dniEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{clienteBean.cliente.dni}",
						String.class));
		htmlPanelGrid.getChildren().add(dniEditInput);

		Message dniEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		dniEditInputMessage.setId("dniEditInputMessage");
		dniEditInputMessage.setFor("dniEditInput");
		dniEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(dniEditInputMessage);


		HtmlOutputText domicilioLaboralEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		domicilioLaboralEditOutput.setId("domicilioLaboralEditOutput");
		domicilioLaboralEditOutput.setValue("Domicilio Laboral:   ");
		htmlPanelGrid.getChildren().add(domicilioLaboralEditOutput);

		InputText domicilioLaboralEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
		domicilioLaboralEditInput.setId("domicilioLaboralEditInput");
		domicilioLaboralEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext,
		"#{clienteBean.cliente.domicilioLaboral}", String.class));
		htmlPanelGrid.getChildren().add(domicilioLaboralEditInput);

		Message domicilioLaboralEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
		domicilioLaboralEditInputMessage.setId("domicilioLaboralEditInputMessage");
		domicilioLaboralEditInputMessage.setFor("domicilioLaboralEditInput");
		domicilioLaboralEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(domicilioLaboralEditInputMessage);

		
		HtmlOutputText telefonoLaboralEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		telefonoLaboralEditOutput.setId("telefonoLaboralEditOutput");
		telefonoLaboralEditOutput.setValue("Telefono Laboral:   ");
		htmlPanelGrid.getChildren().add(telefonoLaboralEditOutput);

		InputText telefonoLaboralEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
		telefonoLaboralEditInput.setId("telefonoLaboralEditInput");
		telefonoLaboralEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext,
		"#{clienteBean.cliente.telefonoLaboral}", String.class));
		htmlPanelGrid.getChildren().add(telefonoLaboralEditInput);

		Message telefonoLaboralEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
		telefonoLaboralEditInputMessage.setId("telefonoLaboralEditInputMessage");
		telefonoLaboralEditInputMessage.setFor("telefonoLaboralEditInput");
		telefonoLaboralEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(telefonoLaboralEditInputMessage);

		HtmlOutputText sueldoEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		sueldoEditOutput.setId("sueldoEditOutput");
		sueldoEditOutput.setValue("Sueldo:   ");
		htmlPanelGrid.getChildren().add(sueldoEditOutput);

		InputText sueldoEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
		sueldoEditInput.setId("sueldoEditInput");
		sueldoEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext,
		"#{clienteBean.cliente.sueldo}", String.class));
		htmlPanelGrid.getChildren().add(sueldoEditInput);

		Message sueldoEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
		sueldoEditInputMessage.setId("sueldoEditInputMessage");
		sueldoEditInputMessage.setFor("sueldoEditInput");
		sueldoEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(sueldoEditInputMessage);
		
		
		HtmlOutputText zonaEditOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		zonaEditOutput.setId("zonaEditOutput");
		zonaEditOutput.setValue("Zona:   ");
		htmlPanelGrid.getChildren().add(zonaEditOutput);

		AutoComplete zonaEditInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		zonaEditInput.setId("zonaEditInput");
		zonaEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{clienteBean.cliente.zona}", Zona.class));
		zonaEditInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{clienteBean.completeZona}", List.class,
						new Class[] { String.class }));
		zonaEditInput.setDropdown(true);
		zonaEditInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "zona", String.class));
		zonaEditInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext,
						"#{zona.zona} #{zona.descripcion}", String.class));
		zonaEditInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{zona}", Zona.class));
		zonaEditInput.setConverter(new ZonaConverter());
		zonaEditInput.setRequired(true);
		htmlPanelGrid.getChildren().add(zonaEditInput);

		Message zonaEditInputMessage = (Message) application 
				.createComponent(Message.COMPONENT_TYPE);
		zonaEditInputMessage.setId("zonaEditInputMessage");
		zonaEditInputMessage.setFor("zonaEditInput");
		zonaEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(zonaEditInputMessage);

		return htmlPanelGrid;
	}

	public HtmlPanelGrid populateViewPanel() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		ExpressionFactory expressionFactory = application
				.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();

		HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application
				.createComponent(HtmlPanelGrid.COMPONENT_TYPE);

		HtmlOutputText apellidosLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		apellidosLabel.setId("apellidosLabel");
		apellidosLabel.setValue("Apellidos:   ");
		htmlPanelGrid.getChildren().add(apellidosLabel);

		HtmlOutputText apellidosValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		apellidosValue.setId("apellidosValue");
		apellidosValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{clienteBean.cliente.apellidos}", String.class));
		htmlPanelGrid.getChildren().add(apellidosValue);

		HtmlOutputText nombresLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		nombresLabel.setId("nombresLabel");
		nombresLabel.setValue("Nombres:   ");
		htmlPanelGrid.getChildren().add(nombresLabel);

		HtmlOutputText nombresValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		nombresValue.setId("nombresValue");
		nombresValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{clienteBean.cliente.nombres}", String.class));
		htmlPanelGrid.getChildren().add(nombresValue);

		HtmlOutputText domicilioLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		domicilioLabel.setId("domicilioLabel");
		domicilioLabel.setValue("Domicilio:   ");
		htmlPanelGrid.getChildren().add(domicilioLabel);

		HtmlOutputText domicilioValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		domicilioValue.setId("domicilioValue");
		domicilioValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{clienteBean.cliente.domicilio}", String.class));
		htmlPanelGrid.getChildren().add(domicilioValue);

		HtmlOutputText domicilioComercialLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		domicilioComercialLabel.setId("domicilioComercialLabel");
		domicilioComercialLabel.setValue("Domicilio Comercial:   ");
		htmlPanelGrid.getChildren().add(domicilioComercialLabel);

		HtmlOutputText domicilioComercialValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		domicilioComercialValue.setId("domicilioComercialValue");
		domicilioComercialValue.setValueExpression("value", expressionFactory.createValueExpression(elContext,"#{clienteBean.cliente.domicilioComercial}", String.class));
		htmlPanelGrid.getChildren().add(domicilioComercialValue);

		HtmlOutputText telefonoLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		telefonoLabel.setId("telefonoLabel");
		telefonoLabel.setValue("Telefono:   ");
		htmlPanelGrid.getChildren().add(telefonoLabel);

		HtmlOutputText telefonoValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		telefonoValue.setId("telefonoValue");
		telefonoValue.setValueExpression("value", expressionFactory.createValueExpression(elContext,"#{clienteBean.cliente.telefono}", String.class));
		htmlPanelGrid.getChildren().add(telefonoValue);

		HtmlOutputText telefonoDeReferenciaLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		telefonoDeReferenciaLabel.setId("telefonoDeReferenciaLabel");
		telefonoDeReferenciaLabel.setValue("Telefono de Referencia:   ");
		htmlPanelGrid.getChildren().add(telefonoDeReferenciaLabel);

		HtmlOutputText telefonoDeReferenciaValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		telefonoDeReferenciaValue.setId("telefonoDeReferenciaValue");
		telefonoDeReferenciaValue.setValueExpression("value", expressionFactory.createValueExpression(elContext,"#{clienteBean.cliente.telefonoDeReferencia}", String.class));
		htmlPanelGrid.getChildren().add(telefonoDeReferenciaValue);

		
		HtmlOutputText eMailLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		eMailLabel.setId("eMailLabel");
		eMailLabel.setValue("e-mail:   ");
		htmlPanelGrid.getChildren().add(eMailLabel);

		HtmlOutputText eMailValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		eMailValue.setId("eMailValue");
		eMailValue.setValueExpression("value", expressionFactory.createValueExpression(elContext,"#{clienteBean.cliente.eMail}", String.class));
		htmlPanelGrid.getChildren().add(eMailValue);

		HtmlOutputText dniLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		dniLabel.setId("dniLabel");
		dniLabel.setValue("Dni:   ");
		htmlPanelGrid.getChildren().add(dniLabel);

		HtmlOutputText dniValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		dniValue.setId("dniValue");
		dniValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{clienteBean.cliente.dni}",
						String.class));
		htmlPanelGrid.getChildren().add(dniValue);

		HtmlOutputText domicilioLaboralLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		domicilioLaboralLabel.setId("domicilioLaboralLabel");
		domicilioLaboralLabel.setValue("Domicilio Laboral:   ");
		htmlPanelGrid.getChildren().add(domicilioLaboralLabel);

		HtmlOutputText domicilioLaboralValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		domicilioLaboralValue.setId("domicilioLaboralValue");
		domicilioLaboralValue.setValueExpression("value", expressionFactory.createValueExpression(elContext,"#{clienteBean.cliente.domicilioLaboral}", String.class));
		htmlPanelGrid.getChildren().add(domicilioLaboralValue);

		HtmlOutputText telefonoLaboralLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		telefonoLaboralLabel.setId("telefonoLaboralLabel");
		telefonoLaboralLabel.setValue("Telefono Laboral:   ");
		htmlPanelGrid.getChildren().add(telefonoLaboralLabel);

		HtmlOutputText telefonoLaboralValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		telefonoLaboralValue.setId("telefonoLaboralValue");
		telefonoLaboralValue.setValueExpression("value", expressionFactory.createValueExpression(elContext,"#{clienteBean.cliente.telefonoLaboral}", String.class));
		htmlPanelGrid.getChildren().add(telefonoLaboralValue);
		
		HtmlOutputText sueldoLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		sueldoLabel.setId("sueldoLabel");
		sueldoLabel.setValue("Sueldo:   ");
		htmlPanelGrid.getChildren().add(sueldoLabel);

		HtmlOutputText sueldoValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		sueldoValue.setId("sueldoValue");
		sueldoValue.setValueExpression("value", expressionFactory.createValueExpression(elContext,"#{clienteBean.cliente.sueldo}", String.class));
		htmlPanelGrid.getChildren().add(sueldoValue);
		
		HtmlOutputText zonaLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		zonaLabel.setId("zonaLabel");
		zonaLabel.setValue("Zona:   ");
		htmlPanelGrid.getChildren().add(zonaLabel);

		HtmlOutputText zonaValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		zonaValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{clienteBean.cliente.zona.zona}", String.class));
		htmlPanelGrid.getChildren().add(zonaValue);

		return htmlPanelGrid;
	}

	public FiltroCuotaList getFiltroCliente() {
		return filtroCliente;
	}

	public void setFiltroCliente(FiltroCuotaList filtroCliente) {
		this.filtroCliente = filtroCliente;
	}

	enum TipoListado {
		TODOS, MOROSOS, ORDEN_COBRANZA, DIAS_COBRO
	}

	public TipoListado getTipoListado() {
		return tipoListado;
	}

	public void setTipoListado(TipoListado tipoListado) {
		this.tipoListado = tipoListado;
	}

	public void updateOrdenCobranza(String[] idOrderedArray) {
		if (idOrderedArray == null || idOrderedArray.length == 0)
			return;
		new Cliente().updateOrdenCobranza(idOrderedArray);
	}

	public String filtrarLista() {
		if (tipoListado.equals(TipoListado.MOROSOS)) {
			return displayMoraList();
		} else if (tipoListado.equals(TipoListado.ORDEN_COBRANZA)) {
			return displayOrdenList();
		} else {
			return null;
		}
	}
	
	public String sendIntimacion(){
		sendMail(getCliente(), "intimacion");
		return null;
	}
	
	public String sendInvitacionAlPago(){
		sendMail(getCliente(), "invitacion");
		return null;
	}

	public String printInvitacionAlPago(){
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/PrintServlet?reportName=invitacionAlPago");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	public String printIntimacion(){
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/PrintServlet?reportName=intimacion");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Cliente> completeCliente(String query) {
		List<Cliente> clientes = Cliente.findClienteByApellidoLike(query);
		return clientes;
		}
	
	private void sendMail(Cliente cliente,String modelo){
		String mail = cliente.geteMail();
		if (!MailValidator.isValidEmailAddress(mail)){
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"El cliente no tiene una direccion de mail valida",null);
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			return;
		}
		try {
				Context initCtx = new InitialContext();
				Context envCtx = (Context) initCtx.lookup("java:comp/env");
				Session session = (Session) envCtx.lookup("mail/Session");
				
				javax.mail.Message message = new MimeMessage(session);
				InternetAddress to[] = new InternetAddress[1];
				to[0] = new InternetAddress(mail);
				message.setFrom(new InternetAddress("notificaciones.cash@gmail.com"));
				message.setRecipients(javax.mail.Message.RecipientType.BCC, to);
				message.setSubject("Notificaciones CASH");
				message.setContent(getModelo(cliente,modelo),"text/html");
				Transport.send(message);

				} catch (AddressException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		FacesMessage facesMessage = new FacesMessage("Se ha enviado el mail");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}
	
	private String getModelo(Cliente cliente,String modelo){
		String modelo1 = templates.get(modelo).toString();
		modelo1 = modelo1.replaceFirst("_FECHA_", DateUtils.getFormalLetterDate(new Date()));
		modelo1 = modelo1.replaceFirst("_NOMBRE_COMPLETO_", cliente.getNombreLargo());
		String domicilio = cliente.getDomicilioComercial();
		if (domicilio==null){
			domicilio = cliente.getDomicilio();
		}
		if (domicilio==null){
			domicilio = "";
		}
		modelo1 = modelo1.replaceFirst("_DOMICILIO_", domicilio);
		return modelo1;
	}
}
