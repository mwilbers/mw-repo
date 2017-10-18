package de.mw.mwdata.core.ofdb.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import de.mw.mwdata.core.domain.AbstractMWEntity;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "ADANSICHTENDEF" /* , schema = Constants.DB_SCHEMA */)
public class AdAnsicht extends AbstractMWEntity implements IAdAnsicht {

	/**
	 *
	 */
	private static final long	serialVersionUID	= 8686809635306976964L;

	private final static String	SEQUENCE_KEY		= "ADANSICHTENDEF:DSID";

	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "DSID")
	private Long				id;

	@Column(name = "ANSICHT", updatable = false, nullable = false, unique = true)
	private String				name;

	@Column(name = "BEZEICHNUNG")
	private String				bezeichnung;

	@Column(name = "ANSICHTID")
	private Long				ansichtId;									// ref key auf FX_AnsichtDef_K

	@Column(name = "DEFAULTANSICHT", nullable = false, columnDefinition = "NUMBER(1) default 0")
	@Type(type = "fxboolean")
	// @Column(name = "PS", columnDefinition="NUMBER(11) default 0")
	private Boolean				defaultAnsicht;

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

	public void setBezeichnung( final String bezeichnung ) {
		this.bezeichnung = bezeichnung;
	}

	public String getBezeichnung() {
		return this.bezeichnung;
	}

	public void setAnsichtId( final Long ansichtId ) {
		this.ansichtId = ansichtId;
	}

	public Long getAnsichtId() {
		return this.ansichtId;
	}

	public void setDefaultAnsicht( final Boolean defaultAnsicht ) {
		this.defaultAnsicht = defaultAnsicht;
	}

	public Boolean getDefaultAnsicht() {
		return this.defaultAnsicht;
	}

}
