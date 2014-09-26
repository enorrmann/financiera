package ar.com.norrmann.financiera.model;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@RooJavaBean

@RooJpaActiveRecord
public class TipoMovimientoCaja {
	
	private String tipoMovimiento;
	private Long variacion=1L;
	public String toString(){
		return "";
	}
	public boolean isPositivo(){
		return variacion>0;
	}
	public boolean isNegativo(){
		return variacion<0;
	}
}
