package ar.com.norrmann.financiera.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.model.DefaultMenuModel;
import org.springframework.roo.addon.jsf.application.RooJsfApplicationBean;

@RooJsfApplicationBean
public class ApplicationBean {

    private DefaultMenuModel menuModel;

	public DefaultMenuModel getMenuModel() {
		return menuModel;
	}

	public void setMenuModel(DefaultMenuModel menuModel) {
		this.menuModel = menuModel;
	}

	public String getColumnName(String column) {
        if (column == null || column.length() == 0) {
            return column;
        }
        final Pattern p = Pattern.compile("[A-Z][^A-Z]*");
        final Matcher m = p.matcher(Character.toUpperCase(column.charAt(0)) + column.substring(1));
        final StringBuilder builder = new StringBuilder();
        while (m.find()) {
            builder.append(m.group()).append(" ");
        }
        return builder.toString().trim();
    }

    @PostConstruct
    public void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        menuModel = new DefaultMenuModel();
        Submenu submenu;
        MenuItem item;
        
        // CLIENTES
        submenu = new Submenu();
        //submenu.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{usuarioBean.administrador or usuarioBean.cobrador}", Boolean.class));
        submenu.setId("clienteSubmenu");
        submenu.setLabel("Clientes");
        item = new MenuItem();
        item.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{usuarioBean.administrador or usuarioBean.cobrador}", Boolean.class));
        item.setId("createClienteMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_create}", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{clienteBean.displayCreateDialog}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-document");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        
        // listar en mora
        item = new MenuItem();
        item.setId("listClienteEnMoraMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "Listar en mora", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{clienteBean.displayMoraList}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-folder-open");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);

        // listar todos
        item = new MenuItem();
        item.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{usuarioBean.administrador or usuarioBean.cobrador}", Boolean.class));
        item.setId("listClienteMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_list}", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{clienteBean.displayList}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-folder-open");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        
        // Ordenar cobranza
        item = new MenuItem();
        item.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{usuarioBean.administrador or usuarioBean.cobrador}", Boolean.class));
        item.setId("listOrdenClienteMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "Orden de cobro", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{clienteBean.displayOrdenList}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-folder-open");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        
        // dias de cobro
        item = new MenuItem();
        item.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{usuarioBean.administrador or usuarioBean.cobrador}", Boolean.class));
        item.setId("listDiasCobroClienteEnMoraMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "Dias de cobro", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{clienteBean.displayDiasCobroList}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-folder-open");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);

        
        menuModel.addSubmenu(submenu);
        
        // COBRADORES
        submenu = new Submenu();
        submenu.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{usuarioBean.administrador or usuarioBean.cobrador}", Boolean.class));
        submenu.setId("cobradorSubmenu");
        submenu.setLabel("Cobradores");
        item = new MenuItem();
        item.setId("createCobradorMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_create}", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{cobradorBean.displayCreateDialog}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-document");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        item = new MenuItem();
        item.setId("listCobradorMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_list}", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{cobradorBean.displayList}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-folder-open");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        menuModel.addSubmenu(submenu);
        
        // nuevo control
//        submenu = new Submenu();
//        submenu.setId("controlCobradorSubmenu");
//        submenu.setLabel("ControlCobrador");
        item = new MenuItem();
        item.setId("createControlCobradorMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "Nuevo Control", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{controlCobradorBean.displayCreateDialog}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-document");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        
        // listar controles
        item = new MenuItem();
        item.setId("listControlCobradorMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "Listar Controles", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{controlCobradorBean.filtrarLista}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-folder-open");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        menuModel.addSubmenu(submenu);
        
        // CREDITOS
        submenu = new Submenu();
        submenu.setId("creditoSubmenu");
        submenu.setLabel("Creditos");
        
        item = new MenuItem();
        item.setId("createCreditoMensualMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_create} Mensual", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{creditoBean.displayCreateDialogMensual}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-document");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data createPanel");
        submenu.getChildren().add(item);
        
        item = new MenuItem();
        item.setId("createCreditoDiarioMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_create} Diario", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{creditoBean.displayCreateDialogDiario}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-document");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data createPanel");
        submenu.getChildren().add(item);
        
        
        item = new MenuItem();
        item.setId("listCreditoMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_list}", String.class));
        //item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{creditoBean.displayList}", String.class, new Class[0]));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "credito", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-folder-open");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        menuModel.addSubmenu(submenu);
        
        // CUOTAS
        submenu = new Submenu();
        //submenu.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{usuarioBean.administrador or usuarioBean.cobrador}", Boolean.class));
        submenu.setId("cuotaSubmenu");
        submenu.setLabel("Cuotas");
        
        item = new MenuItem();
        item.setId("listCuotaMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "Listar vencimientos", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{cuotaBean.listarVencimientos}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-folder-open");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setId("listCobrosMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "Listar cuotas pagas", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{cuotaBean.listarPagos}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-folder-open");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setId("listPagosMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "Listar pagos", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{pagoBean.filtrarLista}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-folder-open");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        
        menuModel.addSubmenu(submenu);
        
        // control caja
        submenu = new Submenu();
        //submenu.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{usuarioBean.administrador or usuarioBean.cobrador}", Boolean.class));
        submenu.setId("movimientoCajaSubmenu");
        submenu.setLabel("Movimientos de Caja");
        item = new MenuItem();
        item.setId("createMovimientoCajaMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_create}", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{movimientoCajaBean.displayCreateDialog}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-document");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        item = new MenuItem();
        item.setId("listMovimientoCajaMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_list}", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{movimientoCajaBean.filtrarLista}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-folder-open");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        menuModel.addSubmenu(submenu);

        
        // ZONAS
        submenu = new Submenu();
        submenu.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{usuarioBean.administrador or usuarioBean.cobrador}", Boolean.class));
        submenu.setId("zonaSubmenu");
        submenu.setLabel("Zonas");
        item = new MenuItem();
        item.setId("createZonaMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_create}", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{zonaBean.displayCreateDialog}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-document");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        item = new MenuItem();
        item.setId("listZonaMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_list}", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{zonaBean.displayList}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-folder-open");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        menuModel.addSubmenu(submenu);

        // TASAS
        submenu = new Submenu();
        submenu.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{usuarioBean.administrador or usuarioBean.cobrador}", Boolean.class));
        submenu.setId("tasaSubmenu");
        submenu.setLabel("Tasa");
        item = new MenuItem();
        item.setId("createTasaMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_create}", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{tasaBean.displayCreateDialog}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-document");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        item = new MenuItem();
        item.setId("listTasaMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_list}", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{tasaBean.displayList}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-folder-open");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        menuModel.addSubmenu(submenu);
 
        // INTERESES
        submenu = new Submenu();
        submenu.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{usuarioBean.administrador or usuarioBean.cobrador}", Boolean.class));
        submenu.setId("interesPunitorioSubmenu");
        submenu.setLabel("Interes Punitorio");
        item = new MenuItem();
        item.setId("createInteresPunitorioMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_create}", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{interesPunitorioBean.displayCreateDialog}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-document");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        item = new MenuItem();
        item.setId("listInteresPunitorioMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_list}", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{interesPunitorioBean.displayList}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-folder-open");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        menuModel.addSubmenu(submenu);
        
        // usuarios
        submenu = new Submenu();
        submenu.setId("usuarioSubmenu");
        submenu.setLabel("Usuario");
        submenu.setValueExpression("rendered", expressionFactory.createValueExpression(elContext, "#{usuarioBean.administrador}", Boolean.class));
        item = new MenuItem();
        item.setId("createUsuarioMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_create}", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{usuarioBean.displayCreateDialog}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-document");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data :growlForm:growl");
        submenu.getChildren().add(item);
        item = new MenuItem();
        item.setId("listUsuarioMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_list}", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{usuarioBean.displayList}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-folder-open");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data :growlForm:growl");
        submenu.getChildren().add(item);
        menuModel.addSubmenu(submenu);
        
    }
}