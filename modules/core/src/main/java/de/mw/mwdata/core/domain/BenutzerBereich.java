/**
 *
 */
package de.mw.mwdata.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import de.mw.mwdata.core.Constants;

/**
 * @author Wilbers, Markus
 * @version 1.0
 * @since Mar, 2011
 *
 */
@TypeDefs({ @TypeDef(name = "fxboolean", typeClass = de.mw.mwdata.core.db.FxBooleanType.class) })
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "BenutzerBereicheDef" /* , schema = Constants.DB_SCHEMA */, uniqueConstraints = @UniqueConstraint(columnNames = {
		"name" }))
@FilterDef(name = "BereichNotImported")
@Filter(name = "BereichNotImported", condition = "importId is null")
public class BenutzerBereich extends AbstractMWEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = -7210768017173573098L;

	private final static String SEQUENCE_KEY = "BenutzerBereicheDef:BereichsID";

	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "BEREICHSID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "NAME", unique = true, updatable = true, nullable = false)
	private String name;

	@Column(name = "BEARBEITEN", columnDefinition = "NUMBER(1) default 0")
	@Type(type = "fxboolean")
	private Boolean bearbeiten;

	@Column(name = "HINZUFUEGEN", columnDefinition = "NUMBER(1) default 0")
	@Type(type = "fxboolean")
	private Boolean hinzufuegen;

	@Column(name = "LESEN", columnDefinition = "NUMBER(1) default 0")
	@Type(type = "fxboolean")
	private Boolean lesen;

	@Column(name = "LOESCHEN", columnDefinition = "NUMBER(1) default 0")
	@Type(type = "fxboolean")
	private Boolean loeschen;

	@Column(name = "BESCHREIBUNG", updatable = true, nullable = true)
	private String beschreibung;

	@Column(name = Constants.SYS_COL_IMPORT_ID, updatable = true, nullable = true)
	private Long importId;

	@Override
	public String getSequenceKey() {
		return SEQUENCE_KEY;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	public void setBearbeiten(final Boolean bearbeiten) {
		this.bearbeiten = bearbeiten;
	}

	public Boolean getBearbeiten() {
		return this.bearbeiten;
	}

	public void setHinzufuegen(final Boolean hinzufuegen) {
		this.hinzufuegen = hinzufuegen;
	}

	public Boolean getHinzufuegen() {
		return this.hinzufuegen;
	}

	public void setLesen(final Boolean lesen) {
		this.lesen = lesen;
	}

	public Boolean getLesen() {
		return this.lesen;
	}

	public void setLoeschen(final Boolean loeschen) {
		this.loeschen = loeschen;
	}

	public Boolean getLoeschen() {
		return this.loeschen;
	}

	public void setBeschreibung(final String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public String getBeschreibung() {
		return this.beschreibung;
	}

	@Override
	public boolean equals(final Object obj) {

		if (null == obj) {
			return false;
		}

		BenutzerBereich oBereich = (BenutzerBereich) obj;

		if (null == this.getId() && null == oBereich.getId()) {
			throw new IllegalArgumentException("Bereich.Id is null");
		}

		if (null == this.getId() || null == oBereich.getId()) {
			return false;
		}

		return (this.getId() == oBereich.getId());

	}

	public void setImportId(final Long importId) {
		this.importId = importId;
	}

	public Long getImportId() {
		return this.importId;
	}

}
