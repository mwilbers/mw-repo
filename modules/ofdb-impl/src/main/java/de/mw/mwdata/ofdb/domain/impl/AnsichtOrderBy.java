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

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.ofdb.domain.IAnsichtOrderBy;
import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;

/**
 * Class for managing entries in the table FX_AnsichtDef_K
 *
 * @author Wilbers, Markus
 * @version 1.0
 * @since Oct, 2012
 *
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "FX_ANSICHTORDERBY_K" /* , schema = Constants.DB_SCHEMA */)
public class AnsichtOrderBy extends AbstractMWEntity implements IAnsichtOrderBy {

	/**
	 *
	 */
	private static final long	serialVersionUID	= 4202224101431839827L;
	private final static String	SEQUENCE_KEY		= "FX_AnsichtOrderBy_K:DSID";

	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "DSID")
	private Long				id;

	@Column(name = "ANSICHT", updatable = true, nullable = false, unique = true)
	private String				name;

	@Column(name = "ANSICHTTABID", insertable = false, updatable = false, nullable = false, unique = false)
	private Long				ansichtTabId;
	//
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	// @PrimaryKeyJoinColumn
	// @NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "ANSICHTTABID", referencedColumnName = "DSID", updatable = true, insertable = true, nullable = true)
	private AnsichtTab			ansichtTab;

	@Column(name = "REIHENFOLGE", updatable = true, nullable = false, unique = true)
	private Integer				reihenfolge;

	@Column(name = "TABAKEY", updatable = true, nullable = false, unique = false)
	private String				tabAKey;

	@Column(name = "ANSICHTSPALTENID", insertable = false, updatable = false, nullable = false, unique = false)
	private Long				ansichtSpaltenId;
	//
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	// @PrimaryKeyJoinColumn
	// @NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "ANSICHTSPALTENID", referencedColumnName = "DSID", updatable = true, insertable = true, nullable = true)
	private AnsichtSpalten		ansichtSpalten;

	@Column(name = "SPALTEAKEY", updatable = true, nullable = false, unique = false)
	private String				spalteAKey;

	@Column(name = "AUFSTEIGEND", columnDefinition = "NUMBER(1) default 0")
	@Type(type = "fxboolean")
	private Boolean				aufsteigend;

	@Override
	public String getSequenceKey() {
		return SEQUENCE_KEY;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId( final Long id ) {
		this.id = id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName( final String name ) {
		this.name = name;
	}

	// public void setAnsichtDefId( Long ansichtDefId ) {
	// this.ansichtDefId = ansichtDefId;
	// }
	//
	// public Long getAnsichtDefId() {
	// return ansichtDefId;
	// }

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.IAnsichtOrderBy#setAnsichtTab(de.mw.mwdata.core.ofdb.domain.AnsichtTab)
	 */
	public void setAnsichtTab( final IAnsichtTab ansichtTab ) {
		this.ansichtTab = (AnsichtTab) ansichtTab;
		this.ansichtTabId = ansichtTab.getId();
		this.name = ansichtTab.getName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.IAnsichtOrderBy#getAnsichtTab()
	 */
	public IAnsichtTab getAnsichtTab() {
		return this.ansichtTab;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.mw.mwdata.core.ofdb.domain.IAnsichtOrderBy#setAnsichtSpalten(de.mw.mwdata.core.ofdb.domain.AnsichtSpalten)
	 */
	public void setAnsichtSpalten( final IAnsichtSpalte ansichtSpalten ) {
		this.ansichtSpalten = (AnsichtSpalten) ansichtSpalten;
		this.ansichtSpaltenId = ansichtSpalten.getId();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.IAnsichtOrderBy#getAnsichtSpalten()
	 */
	public IAnsichtSpalte getAnsichtSpalten() {
		return this.ansichtSpalten;
	}

	public void setReihenfolge( final Integer reihenfolge ) {
		this.reihenfolge = reihenfolge;
	}

	public Integer getReihenfolge() {
		return this.reihenfolge;
	}

	public void setTabAKey( final String tabAKey ) {
		this.tabAKey = tabAKey;
	}

	public String getTabAKey() {
		return this.tabAKey;
	}

	public void setSpalteAKey( final String spalteAKey ) {
		this.spalteAKey = spalteAKey;
	}

	public String getSpalteAKey() {
		return this.spalteAKey;
	}

	public void setAufsteigend( final Boolean aufsteigend ) {
		this.aufsteigend = aufsteigend;
	}

	public Boolean getAufsteigend() {
		return this.aufsteigend;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append( "AnsichtOrderBy [TabAKey: " );
		b.append( this.getTabAKey() );
		b.append( ", SpalteAKey: " );
		b.append( this.getSpalteAKey() );
		b.append( "]" );

		return b.toString();
	}

}
