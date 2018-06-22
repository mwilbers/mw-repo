/**
 *
 */
package de.mw.mwdata.ofdb.domain.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.BenutzerBereich;
import de.mw.mwdata.ofdb.domain.ITabDef;

/**
 * Class for managing entries in the table FX_TabDef_K
 *
 * @author Wilbers, Markus
 * @version 1.0
 * @since July, 2010
 *
 */
@TypeDefs({ @TypeDef(name = "fxenum", typeClass = de.mw.mwdata.ofdb.domain.enums.TypeZEITTYP.class),
		@TypeDef(name = "fxdatenbank", typeClass = de.mw.mwdata.ofdb.domain.enums.TypeDATENBANK.class) })
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "FX_TabDef_K" /* , schema = Constants.DB_SCHEMA */, uniqueConstraints = {
		@UniqueConstraint(columnNames = { "TABELLE", Constants.SYS_COL_OFDB }) })
public class TabDef extends AbstractMWEntity implements ITabDef {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final static String SEQUENCE_KEY = "FX_TabDef_K:DSID";

	// Important: Id-annotation here else the EAGER-Load from TabSpeig to TagDef
	// will not work
	// @see: https://forum.hibernate.org/viewtopic.php?p=2435361
	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "DSID")
	private Long id;

	@Column(name = "TABELLE", updatable = false, nullable = false, unique = true)
	private String name;

	// @OneToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "TABELLE", referencedColumnName = "ANSICHT", nullable
	// = false, insertable = false, updatable = false)
	// private AnsichtDef ansichtDef;

	@Column(name = "ALIAS", updatable = true, nullable = false, unique = true)
	private String alias;

	@Column(name = "DATENBANK", updatable = true, nullable = false)
	@Enumerated(EnumType.STRING)
	@Type(type = "fxdatenbank")
	private DATENBANK datenbank;

	@Column(name = "BEZEICHNUNG")
	private String bezeichnung;

	@Column(name = "BEREICHSID", insertable = true, updatable = true, nullable = false)
	private Long bereichsId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BEREICHSID", referencedColumnName = "BEREICHSID", insertable = false, updatable = false, nullable = false)
	private BenutzerBereich bereich;

	// TODO: ZEITTYP-Definition hier doppelt -> siehe Class-Annotation TypeDef
	@Column(name = "ZEITTYP", updatable = true, nullable = true)
	@Enumerated(EnumType.STRING)
	@Type(type = "fxenum")
	private ZEITTYP zeittyp;

	@Column(name = "EINDEUTIGERSCHLUESSEL", updatable = true, nullable = false)
	private String eindeutigerSchluessel;

	@Column(name = "FULLCLASSNAME", nullable = true)
	private String fullClassName;

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

	@Override
	public String getSequenceKey() {
		return SEQUENCE_KEY;
	}

	public String getBezeichnung() {
		return this.bezeichnung;
	}

	public void setBezeichnung(final String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public BenutzerBereich getBereich() {
		return this.bereich;
	}

	public void setBereich(final BenutzerBereich bereich) {
		this.bereich = bereich;
		this.bereichsId = bereich.getId();
	}

	public String getEindeutigerSchluessel() {
		return this.eindeutigerSchluessel;
	}

	public void setEindeutigerSchluessel(final String eindeutigerSchluessel) {
		this.eindeutigerSchluessel = eindeutigerSchluessel;
	}

	@Override
	public void setZeittyp(final ZEITTYP zeittyp) {
		this.zeittyp = zeittyp;
	}

	@Override
	public ZEITTYP getZeittyp() {
		return this.zeittyp;
	}

	@Override
	public void setDatenbank(final DATENBANK datenbank) {
		this.datenbank = datenbank;
	}

	@Override
	public DATENBANK getDatenbank() {
		return this.datenbank;
	}

	public void setAlias(final String alias) {
		this.alias = alias;
	}

	@Override
	public String getAlias() {
		return this.alias;
	}

	@Override
	public void setFullClassName(final String fullClassName) {
		this.fullClassName = fullClassName;
	}

	@Override
	public String getFullClassName() {
		return this.fullClassName;
	}

	// ... klären,wie equals und hashcode implementiert werden sollten ... -> buch
	// effective java
	// Lösungen für Unittest:
	// 1. könnte sein, dass das TestNG-Problem mit doppelter AnsichtDef daher kommt,
	// dass die gleiche ansicht zwei mal
	// gespeichert wurde wegen verlgiehc auf id
	// 2. bei jedem hibernate save() auch flush() sagen

	public void setBereichsId(final Long bereichsId) {
		this.bereichsId = bereichsId;
	}

	public Long getBereichsId() {
		return this.bereichsId;
	}

	@Override
	public String toString() {

		return "TabDef [" + this.getName() + "]";
	}

}
