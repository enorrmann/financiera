package ar.com.norrmann.financiera.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooSerializable
public class Cliente {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8656285873924356129L;

	@Transient
	private Date fechaCalculoMora = new Date();

	@NotNull
	private String apellidos;

	@NotNull
	private String nombres;

	private String domicilio;
	private String domicilioComercial;
	private String telefono;
	private String telefonoDeReferencia;
	private String eMail;
	private String dni;

	private String telefonoLaboral;
	private String domicilioLaboral;
	private String sueldo;

	@ManyToOne
	private Zona zona;

	@Transient
	private BigDecimal debe;

	private Long ordenCobranza = 0L;

	private Long presentoDoc1 = 0L;
	private Long presentoDoc2 = 0L;
	private Long presentoDoc3 = 0L;
	private Long presentoDoc4 = 0L;
	private Long presentoDoc5 = 0L;
	private Long presentoDoc6 = 0L;
	private Long presentoDoc7 = 0L;
	private Long presentoDoc8 = 0L;
	private Long presentoDoc9 = 0L;

	private String observaciones;

	public String getNombreLargo() {
		return getApellidos() + ", " + getNombres();
	}

	public BigDecimal getDebe() {
		if (this.debe != null)
			return this.debe;

		BigDecimal debe = new BigDecimal(0);
		Query query = entityManager()
				.createQuery(
						"SELECT c FROM Cuota c where c.pagada=0 and c.fechaVencimiento <= :hoy and c.credito.cliente =:cliente",
						Cuota.class);
		query.setParameter("hoy", getFechaCalculoMora());
		query.setParameter("cliente", this);
		List<Cuota> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			this.debe = debe;
			return debe;
		} else {
			for (Cuota unaCuota : resultList) {
				debe = debe.add(unaCuota.getSaldo());
			}
			this.debe = debe;
			return debe;
		}

	}

	public List<Credito> getCreditos() {
		return Credito.findCreditoesByCliente(this).getResultList();
	}

	public static List<Cliente> findAllClientes() {
		return entityManager().createQuery( 
				"SELECT o FROM Cliente o order by o.apellidos,o.nombres",
				Cliente.class).getResultList();
	}

	public static List<Cliente> findClienteByApellidoLike(String apellido) {
		Query query = 
		entityManager().createQuery(
				"SELECT o FROM Cliente o where o.apellidos like :apellido order by o.apellidos,o.nombres ",
				Cliente.class);
		query.setParameter("apellido", "%"+apellido+"%");
		return query.getResultList();
	}

	public static List<Cliente> findClientesEnMora(Zona zona,
			Date fechaCalculoMora) {
		if (zona == null)
			return null;
		Query query = entityManager()
				.createQuery(
						"SELECT distinct c.credito.cliente FROM Cuota c where c.pagada=0 and c.fechaVencimiento <= :hoy and c.credito.cliente.zona = :zona",
						Cliente.class);
		query.setParameter("hoy", fechaCalculoMora);
		query.setParameter("zona", zona);
		return query.getResultList();
	}

	private void updateOrdenCobranza(Long idCliente, Long newPosicion) {
		Query query = entityManager()
				.createQuery(
						"update Cliente c set c.ordenCobranza = :newPosicion where c.id = :idCliente");
		query.setParameter("newPosicion", newPosicion);
		query.setParameter("idCliente", idCliente);
		query.executeUpdate();
	}

	@Transactional
	public void updateOrdenCobranza(String[] idOrderedArray) {
		for (int i = 0; i < idOrderedArray.length; i++) {
			Long id = Long.valueOf(idOrderedArray[i]);
			updateOrdenCobranza(id, Long.valueOf(i+1));
		}

	}

	// En orden de cobro, solo deben aparecer los creditos diarios.
	public static List<Cliente> findAllClientesOrdenCobranza(Zona zona) {
		if (zona == null)
			return null;
		Query query = entityManager()
				.createQuery(
						"SELECT distinct c FROM Cliente c, Credito cr WHERE cr.cliente = c and cr.tipoCredito.id = :idTipoCredito and c.zona = :zona order by c.ordenCobranza",
						Cliente.class);
		query.setParameter("zona", zona);
		query.setParameter("idTipoCredito", TipoCredito.ID_DIARIO);
		return query.getResultList();
	}

	private Boolean getBoolean(Long longValue) {
		return longValue != null && longValue.equals(1L);
	}

	private Long getLong(Boolean boolValue) {
		return (boolValue != null && boolValue.equals(Boolean.TRUE)) ? 1L : 0L;
	}
	
	public boolean puedoPresetar(){
		return (presentoDoc3==null||presentoDoc3.equals(0L));
	}
	public boolean esMalPagador(){
		return (presentoDoc7!=null&&presentoDoc7.equals(1L));
	}
	
	public Credito getCreditoMasReciente(){
		Query query = entityManager()
				.createQuery(
						"SELECT cr FROM Credito cr WHERE cr.cliente = :cliente order by cr.id desc",
						Credito.class);
		query.setParameter("cliente", this);
		query.setMaxResults(1);
		List<Credito> resultList = query.getResultList();
		
		return resultList==null||resultList.isEmpty()?null:resultList.get(0);
	}
	
	public String getDiasCobroUltimoCredito(){
		Credito creditoMasReciente = getCreditoMasReciente();
		if (creditoMasReciente==null)return null;
		return creditoMasReciente.getDiasVencimientoString();
	}
}

