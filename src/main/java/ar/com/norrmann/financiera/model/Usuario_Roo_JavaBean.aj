// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ar.com.norrmann.financiera.model;

import ar.com.norrmann.financiera.model.Usuario;
import java.util.Date;

privileged aspect Usuario_Roo_JavaBean {
    
    public String Usuario.getApellido() {
        return this.apellido;
    }
    
    public void Usuario.setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public String Usuario.getNombre() {
        return this.nombre;
    }
    
    public void Usuario.setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String Usuario.getNombreUsuario() {
        return this.nombreUsuario;
    }
    
    public void Usuario.setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    public String Usuario.getPassword() {
        return this.password;
    }
    
    public void Usuario.setPassword(String password) {
        this.password = password;
    }
    
    public String Usuario.getRol() {
        return this.rol;
    }
    
    public void Usuario.setRol(String rol) {
        this.rol = rol;
    }
    
    public Date Usuario.getFechaAlta() {
        return this.fechaAlta;
    }
    
    public void Usuario.setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
    
    public Date Usuario.getFechaUltimoAcceso() {
        return this.fechaUltimoAcceso;
    }
    
    public void Usuario.setFechaUltimoAcceso(Date fechaUltimoAcceso) {
        this.fechaUltimoAcceso = fechaUltimoAcceso;
    }
    
}
