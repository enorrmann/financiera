// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ar.com.norrmann.financiera.model;

import ar.com.norrmann.financiera.model.Cliente;
import ar.com.norrmann.financiera.model.Credito;
import ar.com.norrmann.financiera.model.Cuota;
import ar.com.norrmann.financiera.model.InteresPunitorio;
import ar.com.norrmann.financiera.model.ResumenCredito;
import ar.com.norrmann.financiera.model.TipoCredito;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

privileged aspect Credito_Roo_JavaBean {
    
    public Cliente Credito.getCliente() {
        return this.cliente;
    }
    
    public void Credito.setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public Cliente Credito.getGarante() {
        return this.garante;
    }
    
    public void Credito.setGarante(Cliente garante) {
        this.garante = garante;
    }
    
    public Date Credito.getFechaSolicitud() {
        return this.fechaSolicitud;
    }
    
    public void Credito.setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }
    
    public Date Credito.getFechaPrimerVencimiento() {
        return this.fechaPrimerVencimiento;
    }
    
    public void Credito.setFechaPrimerVencimiento(Date fechaPrimerVencimiento) {
        this.fechaPrimerVencimiento = fechaPrimerVencimiento;
    }
    
    public BigDecimal Credito.getMontoSolicitado() {
        return this.montoSolicitado;
    }
    
    public void Credito.setMontoSolicitado(BigDecimal montoSolicitado) {
        this.montoSolicitado = montoSolicitado;
    }
    
    public int Credito.getCantidadCuotas() {
        return this.cantidadCuotas;
    }
    
    public void Credito.setCantidadCuotas(int cantidadCuotas) {
        this.cantidadCuotas = cantidadCuotas;
    }
    
    public int Credito.getPeriodoDeVencimiento() {
        return this.periodoDeVencimiento;
    }
    
    public void Credito.setPeriodoDeVencimiento(int periodoDeVencimiento) {
        this.periodoDeVencimiento = periodoDeVencimiento;
    }
    
    public TipoCredito Credito.getTipoCredito() {
        return this.tipoCredito;
    }
    
    public void Credito.setTipoCredito(TipoCredito tipoCredito) {
        this.tipoCredito = tipoCredito;
    }
    
    public InteresPunitorio Credito.getInteresPunitorio() {
        return this.interesPunitorio;
    }
    
    public void Credito.setInteresPunitorio(InteresPunitorio interesPunitorio) {
        this.interesPunitorio = interesPunitorio;
    }
    
    public void Credito.setResumenCredito(ResumenCredito resumenCredito) {
        this.resumenCredito = resumenCredito;
    }
    
    public void Credito.setCuotas(List<Cuota> cuotas) {
        this.cuotas = cuotas;
    }
    
}
