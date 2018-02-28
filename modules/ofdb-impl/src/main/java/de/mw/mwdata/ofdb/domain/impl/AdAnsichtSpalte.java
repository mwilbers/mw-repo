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

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.ofdb.domain.IAdAnsicht;
import de.mw.mwdata.ofdb.domain.IAdAnsichtSpalte;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "ADANSICHTSPALTEN" /* , schema = Constants.DB_SCHEMA */)
public class AdAnsichtSpalte extends AbstractMWEntity implements IAdAnsichtSpalte {

	/**
	 *
	 */
	private static final long	serialVersionUID	= -5212940635987767345L;

	private final static String	SEQUENCE_KEY		= "ADANSICHTSPALTEN:DSID";

	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "DSID")
	private Long				id;

	@Column(name = "SPALTEASKEY", updatable = false, nullable = false, unique = true)
	private String				name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ANSICHTID", referencedColumnName = "DSID", nullable = false)
	private AdAnsicht			adAnsicht;

	@Column(name = "ANSICHTSPALTENDEFID")
	private Long				ansichtSpaltenId;								// ref key auf FX_AnsichtSpalten_K

	// @Column(name = "INDEXGRID", updatable = true, nullable = false, unique = false)
	// private Integer indexGrid;

	@Override
	public String getSequenceKey() {
		return this.SEQUENCE_KEY;
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

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.IAdAnsichtSpalte#setAdAnsicht(de.mw.mwdata.core.ofdb.domain.AdAnsicht)
	 */
	public void setAdAnsicht( final IAdAnsicht adAnsicht ) {
		this.adAnsicht = (AdAnsicht) adAnsicht;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.IAdAnsichtSpalte#getAdAnsicht()
	 */
	public IAdAnsicht getAdAnsicht() {
		return this.adAnsicht;
	}

	public void setAnsichtSpaltenId( final Long ansichtSpaltenId ) {
		this.ansichtSpaltenId = ansichtSpaltenId;
	}

	public Long getAnsichtSpaltenId() {
		return this.ansichtSpaltenId;
	}

	// public void setIndexGrid( final Integer indexGrid ) {
	// this.indexGrid = indexGrid;
	// }
	//
	// public Integer getIndexGrid() {
	// return this.indexGrid;
	// }

}
