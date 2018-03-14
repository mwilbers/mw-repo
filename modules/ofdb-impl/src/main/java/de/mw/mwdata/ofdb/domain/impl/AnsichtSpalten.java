/**
 *
 */
package de.mw.mwdata.ofdb.domain.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabSpeig;

/**
 * Class for managing entries in the table FX_TabDef_K
 *
 * @author Wilbers, Markus
 * @version 1.0
 * @since July, 2010
 *
 */
@TypeDefs({ @TypeDef(name = "fxboolean", typeClass = de.mw.mwdata.core.db.FxBooleanType.class) })
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "FX_ANSICHTSPALTEN_K" /* , schema = Constants.DB_SCHEMA */)
public class AnsichtSpalten extends AbstractMWEntity implements IAnsichtSpalte {

	private static final long serialVersionUID = -8040480474359231162L;

	private final static String SEQUENCE_KEY = "FX_AnsichtSpalten_K:DSID";

	// FIXME: sequenceGenerator to replace by global constant
	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "DSID")
	private Long id;

	@Column(name = "SPALTEAKEY", updatable = false, insertable = false, nullable = false, unique = false)
	private String name;

	@Column(name = "SPALTEAKEY", updatable = true, insertable = true, nullable = false, unique = false)
	private String spalteAKey;

	@Column(name = "INDEXGRID", updatable = true, nullable = false, unique = false)
	private Integer indexGrid;

	@Column(name = "TABAKEY", updatable = true, nullable = false, unique = false)
	private String tabAKey;

	// @Column(name = "ANSICHT", insertable = false, updatable = false, nullable =
	// false, unique = false)
	// private String ansicht;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ANSICHTDEFID", referencedColumnName = "DSID", nullable = false, insertable = false, updatable = false)
	private AnsichtDef ansichtDef;

	@Column(name = "ANSICHTDEFID", insertable = true, updatable = true, nullable = false, unique = false)
	private Long ansichtDefId;

	@Column(name = "TABSPEIGID", insertable = false, updatable = false, nullable = false, unique = false)
	private Long tabSpeigId;
	//
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "TABSPEIGID", referencedColumnName = "DSID", updatable = true, insertable = true, nullable = true)
	private TabSpeig tabSpeig;

	@Column(name = "ANSICHTTABID", insertable = false, updatable = false, nullable = false, unique = false)
	private Long viewTabId;
	//
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "ANSICHTTABID", referencedColumnName = "DSID", updatable = true, insertable = true, nullable = true)
	private AnsichtTab viewTab;

	@Column(name = "INGRIDANZEIGEN", columnDefinition = "NUMBER(11) default 0")
	@Type(type = "fxboolean")
	private Boolean inGridAnzeigen;

	@Column(name = "INGRIDLADEN", columnDefinition = "NUMBER(11) default 0")
	@Type(type = "fxboolean")
	private Boolean inGridLaden;

	@Column(name = "FILTER", columnDefinition = "NUMBER(11) default 0")
	@Type(type = "fxboolean")
	private Boolean filter;

	@Column(name = "ANZAHLNACHKOMMASTELLEN", updatable = true, nullable = true)
	private Integer anzahlNachkommastellen;

	@Column(name = "BEARBHINZUFZUGELASSEN", columnDefinition = "NUMBER(11) default 0")
	@Type(type = "fxboolean")
	private Boolean bearbHinzufZugelassen;

	@Column(name = "BEARBZUGELASSEN", columnDefinition = "NUMBER(11) default 0")
	@Type(type = "fxboolean")
	private Boolean bearbZugelassen;

	@Column(name = "EINGABENOTWENDIG", columnDefinition = "NUMBER(11) default 0")
	@Type(type = "fxboolean")
	private Boolean eingabeNotwendig;

	@Column(name = "ANSICHTSUCHEN", updatable = true, nullable = true, unique = false)
	private String ansichtSuchen;

	@Column(name = "SUCHWERTAUSTABAKEY", updatable = true, nullable = true, unique = false)
	private String suchwertAusTabAKey;

	@Column(name = "SUCHWERTAUSSPALTEAKEY", updatable = true, nullable = true, unique = false)
	private String suchwertAusSpalteAKey;

	@Column(name = "WHERETABAKEY", updatable = true, nullable = true, unique = false)
	private String whereTabAKey;

	@Column(name = "WHERESPALTEAKEY", updatable = true, nullable = true, unique = false)
	private String whereSpalteAKey;

	@Column(name = "VERDECKENDURCHTABAKEY", updatable = true, nullable = true, unique = false)
	private String verdeckenDurchTabAKey;

	@Column(name = "VERDECKENDURCHSPALTEAKEY", updatable = true, nullable = true, unique = false)
	private String verdeckenDurchSpalteAKey;

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.domain.AbstractMWEntity#getSequenceKey()
	 */
	@Override
	public String getSequenceKey() {
		return this.SEQUENCE_KEY;
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

	public Integer getIndexGrid() {
		return this.indexGrid;
	}

	public void setIndexGrid(final Integer indexGrid) {
		this.indexGrid = indexGrid;
	}

	public String getTabAKey() {
		return this.tabAKey;
	}

	public void setTabAKey(final String tabAKey) {
		this.tabAKey = tabAKey;
	}

	public String getSpalteAKey() {
		return this.spalteAKey;
	}

	public void setSpalteAKey(final String spalteAKey) {
		this.spalteAKey = spalteAKey;
		this.name = spalteAKey;
	}

	public Boolean getInGridAnzeigen() {
		return this.inGridAnzeigen;
	}

	public void setInGridAnzeigen(final Boolean inGridAnzeigen) {
		this.inGridAnzeigen = inGridAnzeigen;
	}

	public Boolean getInGridLaden() {
		return this.inGridLaden;
	}

	public void setInGridLaden(final Boolean inGridLaden) {
		this.inGridLaden = inGridLaden;
	}

	public Boolean getFilter() {
		return this.filter;
	}

	public void setFilter(final Boolean filter) {
		this.filter = filter;
	}

	public Integer getAnzahlNachkommastellen() {
		return this.anzahlNachkommastellen;
	}

	public void setAnzahlNachkommastellen(final Integer anzahlNachkommastellen) {
		this.anzahlNachkommastellen = anzahlNachkommastellen;
	}

	public Boolean getBearbHinzufZugelassen() {
		return this.bearbHinzufZugelassen;
	}

	public void setBearbHinzufZugelassen(final Boolean bearbHinzufZugelassen) {
		this.bearbHinzufZugelassen = bearbHinzufZugelassen;
	}

	public Boolean getBearbZugelassen() {
		return this.bearbZugelassen;
	}

	public void setBearbZugelassen(final Boolean bearbZugelassen) {
		this.bearbZugelassen = bearbZugelassen;
	}

	public Boolean getEingabeNotwendig() {
		return this.eingabeNotwendig;
	}

	public void setEingabeNotwendig(final Boolean eingabeNotwendig) {
		this.eingabeNotwendig = eingabeNotwendig;
	}

	public String getSuchwertAusTabAKey() {
		return this.suchwertAusTabAKey;
	}

	public void setSuchwertAusTabAKey(final String suchwertAusTabAKey) {
		this.suchwertAusTabAKey = suchwertAusTabAKey;
	}

	public String getSuchwertAusSpalteAKey() {
		return this.suchwertAusSpalteAKey;
	}

	public void setSuchwertAusSpalteAKey(final String suchwertAusSpalteAKey) {
		this.suchwertAusSpalteAKey = suchwertAusSpalteAKey;
	}

	public String getWhereTabAKey() {
		return this.whereTabAKey;
	}

	public void setWhereTabAKey(final String whereTabAKey) {
		this.whereTabAKey = whereTabAKey;
	}

	public String getWhereSpalteAKey() {
		return this.whereSpalteAKey;
	}

	public void setWhereSpalteAKey(final String whereSpalteAKey) {
		this.whereSpalteAKey = whereSpalteAKey;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.mw.mwdata.core.ofdb.domain.IAnsichtSpalte#setAnsichtDef(de.mw.mwdata.core.
	 * ofdb.domain.AnsichtDef)
	 */
	public void setAnsichtDef(final IAnsichtDef ansichtDef) {
		this.ansichtDef = (AnsichtDef) ansichtDef;
		this.ansichtDefId = ansichtDef.getId();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.IAnsichtSpalte#getAnsichtDef()
	 */
	public IAnsichtDef getAnsichtDef() {
		return this.ansichtDef;
	}

	// @Override
	public void setTabSpEig(final ITabSpeig tabSpeig) {
		this.tabSpeig = (TabSpeig) tabSpeig;
		this.tabSpeigId = tabSpeig.getId();
	}

	public ITabSpeig getTabSpEig() {
		return this.tabSpeig;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.mw.mwdata.core.ofdb.domain.IAnsichtSpalte#setViewTab(de.mw.mwdata.core.
	 * ofdb.domain.AnsichtTab)
	 */
	public void setViewTab(final IAnsichtTab viewTab) {
		this.viewTab = (AnsichtTab) viewTab;
		this.viewTabId = viewTab.getId();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.IAnsichtSpalte#getViewTab()
	 */
	public IAnsichtTab getViewTab() {
		return this.viewTab;
	}

	@Override
	public String toString() {
		return "AnsichtSpalte [" + this.getTabAKey() + " : " + this.getSpalteAKey() + "]";
	}

	public void setAnsichtSuchen(final String ansichtSuchen) {
		this.ansichtSuchen = ansichtSuchen;
	}

	public String getAnsichtSuchen() {
		return this.ansichtSuchen;
	}

	public void setVerdeckenDurchTabAKey(final String verdeckenDurchTabAKey) {
		this.verdeckenDurchTabAKey = verdeckenDurchTabAKey;
	}

	public String getVerdeckenDurchTabAKey() {
		return this.verdeckenDurchTabAKey;
	}

	public void setVerdeckenDurchSpalteAKey(final String verdeckenDurchSpalteAKey) {
		this.verdeckenDurchSpalteAKey = verdeckenDurchSpalteAKey;
	}

	public String getVerdeckenDurchSpalteAKey() {
		return this.verdeckenDurchSpalteAKey;
	}

	// public boolean hasSameParentTable() {
	// return (this.suchwertAusTabAKey.equals( this.tabAKey ));
	// }

}
