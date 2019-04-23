package de.mw.mwdata.ofdb.domain.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.Benutzer;
import de.mw.mwdata.ofdb.domain.IUserView;

@Entity
@Table(name = "BENUTZERANSICHT", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "ANSICHTDEFID", "BEZEICHNUNG" }) })
@JsonPropertyOrder({ "id" })
public class UserView extends AbstractMWEntity implements IUserView {

	private static final long serialVersionUID = 2826350923517764494L;

	private final static String SEQUENCE_KEY = "BENUTZERANSICHT:DSID";

	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "DSID")
	private Long id;

	@Column(name = "BEZEICHNUNG", updatable = true, nullable = false, unique = false)
	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ANSICHTDEFID", referencedColumnName = "DSID", nullable = false, insertable = false, updatable = false)
	private AnsichtDef viewDef;

	@Column(name = "ANSICHTDEFID", insertable = true, updatable = true, nullable = false, unique = false)
	private Long viewDefId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BENUTZERID", referencedColumnName = "DSID", nullable = false, insertable = false, updatable = false)
	private Benutzer user;

	@Column(name = "BENUTZERID", insertable = true, updatable = true, nullable = false, unique = false)
	private Long userId;

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
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
	public String getSequenceKey() {
		return SEQUENCE_KEY;
	}

	public AnsichtDef getViewDef() {
		return viewDef;
	}

	public void setViewDef(AnsichtDef viewDef) {
		this.viewDef = viewDef;
		this.viewDefId = viewDef.getId();
	}

	public Long getViewDefId() {
		return viewDefId;
	}

	public Benutzer getUser() {
		return user;
	}

	public void setUser(Benutzer user) {
		this.user = user;
		this.userId = user.getId();
	}

	public Long getUserId() {
		return userId;
	}

}
