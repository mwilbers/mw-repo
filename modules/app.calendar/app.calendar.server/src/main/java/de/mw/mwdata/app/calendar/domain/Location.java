package de.mw.mwdata.app.calendar.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import de.mw.mwdata.core.domain.AbstractMWEntity;

@TypeDefs({ @TypeDef(name = "fxboolean", typeClass = de.mw.mwdata.core.db.FxBooleanType.class) })
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "CAL_ORT" /* , schema = Constants.DB_SCHEMA */, uniqueConstraints = @UniqueConstraint(columnNames = {
		"ORTNAME", "PLZ" }))
public class Location extends AbstractMWEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1985466676891080035L;

	private final static String SEQUENCE_KEY = "CAL_ORT:ORTID";

	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "ORTID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "ORTNAME", unique = false, updatable = true, nullable = false)
	private String name;

	@Column(name = "PLZ", unique = false, updatable = true, nullable = false)
	private String zipCode;

	@Override
	public String getSequenceKey() {
		return SEQUENCE_KEY;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

}
