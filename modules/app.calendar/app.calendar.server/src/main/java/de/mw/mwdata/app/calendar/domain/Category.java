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
@Table(name = "CAL_KATEGORIE" /* , schema = Constants.DB_SCHEMA */, uniqueConstraints = @UniqueConstraint(columnNames = {
		"KATEGORIENAME" }))
public class Category extends AbstractMWEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5469932557256523433L;
	private final static String SEQUENCE_KEY = "CAL_KATEGORIE:KATEGORIEID";

	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "KATEGORIEID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "KATEGORIENAME", unique = true, updatable = true, nullable = false)
	private String name;

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

}
