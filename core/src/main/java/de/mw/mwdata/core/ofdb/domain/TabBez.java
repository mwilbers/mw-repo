/**
 *
 */
package de.mw.mwdata.core.ofdb.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import de.mw.mwdata.core.domain.AbstractMWEntity;

/**
 * @author Wilbers, Markus
 * @version 1.0
 * @since Apr, 2011
 *
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "FX_TabBez_K" /* , schema = Constants.DB_SCHEMA */)
public class TabBez extends AbstractMWEntity implements ITabBez {

	/**
	 *
	 */
	private static final long	serialVersionUID	= 8882904581424345871L;

	private final static String	SEQUENCE_KEY		= "FX_TabBez_K:DSID";

	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "DSID")
	private Long				id;

	@Transient
	private String				name;

	@Column(name = "TABELLEEIG", updatable = false, nullable = false, unique = false)
	private String				tabelleEig;

	@Column(name = "TABELLEDEF", updatable = false, nullable = false, unique = false)
	private String				tabelleDef;

	@Column(name = "DEFSCHLUESSEL", updatable = false, nullable = false, unique = false)
	private String				defSchluessel;

	@Column(name = "EIGSCHLUESSEL", updatable = false, nullable = false, unique = false)
	private String				eigSchluessel;

	@Column(name = "OPERATOR", updatable = false, nullable = false, unique = false)
	private String				operator;

	@Column(name = "INDB", updatable = false, nullable = false, unique = false)
	private Long				inDb;

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

	public void setTabelleDef( final String tabelleDef ) {
		this.tabelleDef = tabelleDef;
	}

	public String getTabelleDef() {
		return this.tabelleDef;
	}

	public String getDefSchluessel() {
		return this.defSchluessel;
	}

	public void setDefSchluessel( final String defSchluessel ) {
		this.defSchluessel = defSchluessel;
	}

	public String getEigSchluessel() {
		return this.eigSchluessel;
	}

	public void setEigSchluessel( final String eigSchluessel ) {
		this.eigSchluessel = eigSchluessel;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator( final String operator ) {
		this.operator = operator;
	}

	public Long getInDb() {
		return this.inDb;
	}

	public void setInDb( final Long inDb ) {
		this.inDb = inDb;
	}

	public void setTabelleEig( final String tabelleEig ) {
		this.tabelleEig = tabelleEig;
	}

	public String getTabelleEig() {
		return this.tabelleEig;
	}

}
