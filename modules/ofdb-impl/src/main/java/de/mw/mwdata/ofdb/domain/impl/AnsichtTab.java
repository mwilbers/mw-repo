package de.mw.mwdata.ofdb.domain.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabDef;

/**
 * Class for managing entries in the table FX_AnsichtTab_K
 *
 * @author Wilbers, Markus
 * @since Oct, 2012
 *
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "FX_ANSICHTTAB_K" /* , schema = Constants.DB_SCHEMA */)
@JsonPropertyOrder({ "id", "name", "ansichtDef", "ansichtDefId", "tabDef", "tabDefId" })
public class AnsichtTab extends AbstractMWEntity implements IAnsichtTab {

	/**
	 *
	 */
	private static final long serialVersionUID = 7366027316373448392L;

	private static final String SEQUENCE_KEY = "FX_AnsichtTab_K:DSID";

	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "DSID")
	private Long id;

	@Column(name = "ANSICHT", updatable = false, nullable = false, unique = false)
	private String name;

	@Column(name = "TABAKEY", updatable = true, nullable = false, unique = false)
	private String tabAKey;

	@Column(name = "TABELLE", updatable = true, nullable = true, unique = false)
	private String tabelle;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ANSICHTDEFID", referencedColumnName = "DSID", nullable = false, updatable = false, insertable = false)
	private AnsichtDef ansichtDef;

	@Column(name = "ANSICHTDEFID", insertable = true, updatable = true, nullable = false, unique = false)
	private Long ansichtDefId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TABDEFID", referencedColumnName = "DSID", nullable = false, updatable = false, insertable = false)
	private TabDef tabDef;

	@Column(name = "TABDEFID", insertable = true, updatable = true, nullable = false, unique = false)
	private Long tabDefId;

	@Column(name = "REIHENFOLGE", updatable = true, nullable = false, unique = false)
	private Integer reihenfolge;

	@Column(name = "JOINTYP", updatable = true, nullable = false, unique = false)
	private String joinTyp;

	@Column(name = "JOIN1SPALTEAKEY", updatable = true, nullable = false, unique = false)
	private String join1SpalteAKey;

	@Column(name = "JOIN2TABAKEY", updatable = true, nullable = false, unique = false)
	private String join2TabAKey;

	@Column(name = "JOIN2SPALTEAKEY", updatable = true, nullable = false, unique = false)
	private String join2SpalteAKey;

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

	public void setTabAKey(final String tabAKey) {
		this.tabAKey = tabAKey;
	}

	public String getTabAKey() {
		return this.tabAKey;
	}

	public void setTabelle(final String tabelle) {
		this.tabelle = tabelle;
	}

	public String getTabelle() {
		return this.tabelle;
	}

	public void setAnsichtDef(final AnsichtDef ansichtDef) {
		this.ansichtDef = ansichtDef;
		this.ansichtDefId = ansichtDef.getId();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.IAnsichtTab#getAnsichtDef()
	 */
	public AnsichtDef getAnsichtDef() {
		return this.ansichtDef;
	}

	public void setReihenfolge(final Integer reihenfolge) {
		this.reihenfolge = reihenfolge;
	}

	public Integer getReihenfolge() {
		return this.reihenfolge;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.mw.mwdata.core.ofdb.domain.IAnsichtTab#setTabDef(de.mw.mwdata.core.ofdb.
	 * domain.TabDef)
	 */
	public void setTabDef(final ITabDef tabDef) {
		this.tabDef = (TabDef) tabDef;
		this.tabDefId = tabDef.getId();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.mw.mwdata.core.ofdb.domain.IAnsichtTab#getTabDef()
	 */
	public ITabDef getTabDef() {
		return this.tabDef;
	}

	public void setJoinTyp(final String joinTyp) {
		this.joinTyp = joinTyp;
	}

	public String getJoinTyp() {
		return this.joinTyp;
	}

	public void setJoin1SpalteAKey(final String join1SpalteAKey) {
		this.join1SpalteAKey = join1SpalteAKey;
	}

	public String getJoin1SpalteAKey() {
		return this.join1SpalteAKey;
	}

	public void setJoin2TabAKey(final String join2TabAKey) {
		this.join2TabAKey = join2TabAKey;
	}

	public String getJoin2TabAKey() {
		return this.join2TabAKey;
	}

	public void setJoin2SpalteAKey(final String join2SpalteAKey) {
		this.join2SpalteAKey = join2SpalteAKey;
	}

	public String getJoin2SpalteAKey() {
		return this.join2SpalteAKey;
	}

	@Override
	public boolean equals(final Object obj) {

		if (null == obj) {
			return false;
		}

		AnsichtTab otherAnsichtTab = (AnsichtTab) obj;
		if (!otherAnsichtTab.getAnsichtDef().equals(this.getAnsichtDef())) {
			return false;
		}
		if (!otherAnsichtTab.getTabAKey().equalsIgnoreCase(this.getTabAKey())) {
			return false;
		}

		return true;

		// FIXME: implement hashCode()

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.getAnsichtDef() == null) ? 0 : this.getAnsichtDef().hashCode());
		result = (prime * result) + ((this.getName() == null) ? 0 : this.getName().hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(this.getClass().getSimpleName() + " [ AnsichtDef = '");
		b.append(this.getAnsichtDef().getName());
		b.append(", TabAKey = '");
		b.append(this.getTabAKey());
		b.append("', TabDef = '");
		b.append(this.getTabDef().getName());
		b.append("' ] ");
		return b.toString();
	}

	// @Override
	public String findTableName() {
		if (StringUtils.isBlank(this.getTabelle())) {
			return this.getTabAKey();
		} else {
			return this.getTabelle();
		}
	}

}
