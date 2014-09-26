package ar.com.norrmann.financiera.web;

import java.util.Collection;
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


import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.message.Message;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.context.RequestContext;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import ar.com.norrmann.financiera.model.Usuario;
import ar.com.norrmann.financiera.web.util.I18N;

@RooSerializable
@RooJsfManagedBean(entity = Usuario.class, beanName = "usuarioBean")
public class UsuarioBean {

	public String getUsuarioLogueado() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String name = auth.getName(); // get logged in username
		return name;
	}

	public HtmlPanelGrid populateCreatePanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText apellidoCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        apellidoCreateOutput.setId("apellidoCreateOutput");
        apellidoCreateOutput.setValue("Apellido: * ");
        htmlPanelGrid.getChildren().add(apellidoCreateOutput);
        
        InputText apellidoCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        apellidoCreateInput.setId("apellidoCreateInput");
        apellidoCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{usuarioBean.usuario.apellido}", String.class));
        htmlPanelGrid.getChildren().add(apellidoCreateInput);
        
        Message apellidoCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        apellidoCreateInputMessage.setId("apellidoCreateInputMessage");
        apellidoCreateInputMessage.setFor("apellidoCreateInput");
        apellidoCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(apellidoCreateInputMessage);
        
        HtmlOutputText nombreCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        nombreCreateOutput.setId("nombreCreateOutput");
        nombreCreateOutput.setValue("Nombre: * ");
        htmlPanelGrid.getChildren().add(nombreCreateOutput);
        
        InputText nombreCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        nombreCreateInput.setId("nombreCreateInput");
        nombreCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{usuarioBean.usuario.nombre}", String.class));
        htmlPanelGrid.getChildren().add(nombreCreateInput);
        
        Message nombreCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        nombreCreateInputMessage.setId("nombreCreateInputMessage");
        nombreCreateInputMessage.setFor("nombreCreateInput");
        nombreCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(nombreCreateInputMessage);
        
        HtmlOutputText nombreUsuarioCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        nombreUsuarioCreateOutput.setId("nombreUsuarioCreateOutput");
        nombreUsuarioCreateOutput.setValue("Nombre Usuario: * ");
        htmlPanelGrid.getChildren().add(nombreUsuarioCreateOutput);
        
        InputText nombreUsuarioCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        nombreUsuarioCreateInput.setId("nombreUsuarioCreateInput");
        nombreUsuarioCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{usuarioBean.usuario.nombreUsuario}", String.class));
        htmlPanelGrid.getChildren().add(nombreUsuarioCreateInput);
        
        Message nombreUsuarioCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        nombreUsuarioCreateInputMessage.setId("nombreUsuarioCreateInputMessage");
        nombreUsuarioCreateInputMessage.setFor("nombreUsuarioCreateInput");
        nombreUsuarioCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(nombreUsuarioCreateInputMessage);
        
        HtmlOutputText passwordCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        passwordCreateOutput.setId("passwordCreateOutput");
        passwordCreateOutput.setValue("Password: * ");
        htmlPanelGrid.getChildren().add(passwordCreateOutput);
        
        InputText passwordCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        passwordCreateInput.setId("passwordCreateInput");
        passwordCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{usuarioBean.usuario.password}", String.class));
        htmlPanelGrid.getChildren().add(passwordCreateInput);
        
        Message passwordCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        passwordCreateInputMessage.setId("passwordCreateInputMessage");
        passwordCreateInputMessage.setFor("passwordCreateInput");
        passwordCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(passwordCreateInputMessage);
        
        HtmlOutputText rolCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        rolCreateOutput.setId("rolCreateOutput");
        rolCreateOutput.setValue("Rol: * ");
        htmlPanelGrid.getChildren().add(rolCreateOutput);
        
//        InputText rolCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
//        rolCreateInput.setId("rolCreateInput");
//        rolCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{usuarioBean.usuario.rol}", String.class));
//        htmlPanelGrid.getChildren().add(rolCreateInput);
        
         htmlPanelGrid.getChildren().add(getSelectOneMenuRoles());
        
        Message rolCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        rolCreateInputMessage.setId("rolCreateInputMessage");
        rolCreateInputMessage.setFor("equipmentTypeList");
        rolCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(rolCreateInputMessage);
        
        return htmlPanelGrid;
    }
	
	private SelectOneMenu getSelectOneMenuRoles(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		ExpressionFactory expressionFactory = application.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();
		
		SelectOneMenu selectOneMenu = (SelectOneMenu) application.createComponent(SelectOneMenu.COMPONENT_TYPE);
		selectOneMenu.setId("equipmentTypeList");
		selectOneMenu.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{usuarioBean.usuario.rol}", String.class));
			
		List<UIComponent> menuChildren = selectOneMenu.getChildren();
		UISelectItem rolAdminItem = (UISelectItem) application.createComponent(UISelectItem.COMPONENT_TYPE);
		rolAdminItem.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext,"Administrador", String.class));
		rolAdminItem.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext,"Administrador", String.class));
		
		UISelectItem rolUserItem = (UISelectItem) application.createComponent(UISelectItem.COMPONENT_TYPE);
		rolUserItem.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext,"Administrativo", String.class));
		rolUserItem.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext,"Administrativo", String.class));
		
		UISelectItem rolCobradorItem = (UISelectItem) application.createComponent(UISelectItem.COMPONENT_TYPE);
		rolCobradorItem.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext,"Cobrador", String.class));
		rolCobradorItem.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext,"Cobrador", String.class));
		
		menuChildren.add(rolCobradorItem);
		menuChildren.add(rolUserItem);
		menuChildren.add(rolAdminItem);
		return selectOneMenu;
	}

	public HtmlPanelGrid populateEditPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText apellidoEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        apellidoEditOutput.setId("apellidoEditOutput");
        apellidoEditOutput.setValue("Apellido: * ");
        htmlPanelGrid.getChildren().add(apellidoEditOutput);
        
        InputText apellidoEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        apellidoEditInput.setId("apellidoEditInput");
        apellidoEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{usuarioBean.usuario.apellido}", String.class));
        htmlPanelGrid.getChildren().add(apellidoEditInput);
        
        Message apellidoEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        apellidoEditInputMessage.setId("apellidoEditInputMessage");
        apellidoEditInputMessage.setFor("apellidoEditInput");
        apellidoEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(apellidoEditInputMessage);
        
        HtmlOutputText nombreEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        nombreEditOutput.setId("nombreEditOutput");
        nombreEditOutput.setValue("Nombre: * ");
        htmlPanelGrid.getChildren().add(nombreEditOutput);
        
        InputText nombreEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        nombreEditInput.setId("nombreEditInput");
        nombreEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{usuarioBean.usuario.nombre}", String.class));
        htmlPanelGrid.getChildren().add(nombreEditInput);
        
        Message nombreEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        nombreEditInputMessage.setId("nombreEditInputMessage");
        nombreEditInputMessage.setFor("nombreEditInput");
        nombreEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(nombreEditInputMessage);
        
        HtmlOutputText nombreUsuarioEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        nombreUsuarioEditOutput.setId("nombreUsuarioEditOutput");
        nombreUsuarioEditOutput.setValue("Nombre Usuario: * ");
        htmlPanelGrid.getChildren().add(nombreUsuarioEditOutput);
        
        InputText nombreUsuarioEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        nombreUsuarioEditInput.setId("nombreUsuarioEditInput");
        nombreUsuarioEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{usuarioBean.usuario.nombreUsuario}", String.class));
        htmlPanelGrid.getChildren().add(nombreUsuarioEditInput);
        
        Message nombreUsuarioEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        nombreUsuarioEditInputMessage.setId("nombreUsuarioEditInputMessage");
        nombreUsuarioEditInputMessage.setFor("nombreUsuarioEditInput");
        nombreUsuarioEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(nombreUsuarioEditInputMessage);
        
        HtmlOutputText passwordEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        passwordEditOutput.setId("passwordEditOutput");
        passwordEditOutput.setValue("Password: * ");
        htmlPanelGrid.getChildren().add(passwordEditOutput);
        
        InputText passwordEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        passwordEditInput.setId("passwordEditInput");
        passwordEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{usuarioBean.usuario.password}", String.class));
        htmlPanelGrid.getChildren().add(passwordEditInput);
        
        Message passwordEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        passwordEditInputMessage.setId("passwordEditInputMessage");
        passwordEditInputMessage.setFor("passwordEditInput");
        passwordEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(passwordEditInputMessage);
        
        HtmlOutputText rolEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        rolEditOutput.setId("rolEditOutput");
        rolEditOutput.setValue("Rol: * ");
        htmlPanelGrid.getChildren().add(rolEditOutput);
        
//        InputText rolEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
//        rolEditInput.setId("rolEditInput");
//        rolEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{usuarioBean.usuario.rol}", String.class));
//        htmlPanelGrid.getChildren().add(rolEditInput);
        htmlPanelGrid.getChildren().add(getSelectOneMenuRoles());
        
        Message rolEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        rolEditInputMessage.setId("rolEditInputMessage");
        rolEditInputMessage.setFor("equipmentTypeList");
        rolEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(rolEditInputMessage);
        return htmlPanelGrid;
    }
	public HtmlPanelGrid populateViewPanel() {
		return null;
	}

    public boolean isAdministrador(){
    	return hasRole("Administrador");
    }

    public boolean isCobrador(){
    	return hasRole("Cobrador");
    }

    public boolean isAdministrativo(){
    	return hasRole("Administrativo");
    }
    
	public String displayList() {
    	if (!hasRole("Administrador")){
            FacesMessage facesMessage = new FacesMessage("No tiene privilegios para realizar esa acción");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return null;
    	}
        setCreateDialogVisible(false);
        findAllUsuarios();
        return "usuario";
    }
    
    public String displayCreateDialog() {
    	if (!hasRole("Administrador")){
            FacesMessage facesMessage = new FacesMessage("No tiene privilegios para realizar esa acción");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return null;
    	}
        setUsuario(new Usuario());
        setCreateDialogVisible(true);
        return "usuario";
    }


	 protected final boolean hasRole(String role) {
		    boolean hasRole = false;
		    UserDetails userDetails = getUserDetails();
		    if (userDetails != null) {
		      Collection authorities = userDetails.getAuthorities();
		      if (isRolePresent(authorities, role)) {
		        hasRole = true;
		      }
		    } 
		    return hasRole;
		  }
		  /**
		   * Get info about currently logged in user
		   * @return UserDetails if found in the context, null otherwise
		   */
		  protected UserDetails getUserDetails() {
		    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    UserDetails userDetails = null;
		    if (principal instanceof UserDetails) {
		      userDetails = (UserDetails) principal;
		    }
		    return userDetails;
		  }
		  /**
		   * Check if a role is present in the authorities of current user
		   * @param authorities all authorities assigned to current user
		   * @param role required authority
		   * @return true if role is present in list of authorities assigned to current user, false otherwise
		   */
		  private boolean isRolePresent(Collection<GrantedAuthority> authorities, String role) {
		    boolean isRolePresent = false;
		    for (GrantedAuthority grantedAuthority : authorities) {
		      isRolePresent = grantedAuthority.getAuthority().equals(role);
		      if (isRolePresent) break;
		    }
		    return isRolePresent;
		  }

	public String persist() {
        String message = "";
        Usuario usuario = getUsuario();
        if (usuario.getId() != null) {
            usuario.merge();
            message = I18N.ACTUALIZADO_OK;
        } else {
            usuario.persist();
            message = I18N.CREADO_OK;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialog.hide()");
        context.execute("editDialog.hide()");
        
        FacesMessage facesMessage = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllUsuarios();
    }

	public String delete() {
		Usuario usuario = getUsuario();
        usuario.remove();
        FacesMessage facesMessage = new FacesMessage(I18N.BORRADO_OK);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllUsuarios();
    }
}
