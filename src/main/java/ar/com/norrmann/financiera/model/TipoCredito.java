package ar.com.norrmann.financiera.model;

import javax.persistence.Transient;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooSerializable
public class TipoCredito {

    @Transient
	public static final Long ID_MENSUAL = 1L;
    @Transient
	public static final Long ID_DIARIO = 2L;

	private String tipo;

    private String descripcion;
}
