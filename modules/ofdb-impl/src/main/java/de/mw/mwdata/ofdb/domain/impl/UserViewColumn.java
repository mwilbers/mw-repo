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

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.ofdb.domain.IUserViewColumn;

@Entity
@Table(name = "BENUTZERANSICHTSPALTEN")
@JsonPropertyOrder({ "id" })
public class UserViewColumn extends AbstractMWEntity implements IUserViewColumn {

	private static final long serialVersionUID = 247324149257557343L;

	private final static String SEQUENCE_KEY = "BENUTZERANSICHTSPALTEN:DSID";

	@Id
	@GeneratedValue(generator = "SEQ_GEN")
	@GenericGenerator(name = "SEQ_GEN", strategy = "de.mw.mwdata.core.db.FxSequenceGenerator")
	@Column(name = "DSID")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BENUTZERANSICHTID", referencedColumnName = "DSID", nullable = false, insertable = false, updatable = false)
	private UserView userView;

	@Column(name = "BENUTZERANSICHTID", insertable = true, updatable = true, nullable = false, unique = false)
	private Long userViewId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ANSICHTSPALTENID", referencedColumnName = "DSID", nullable = false, insertable = false, updatable = false)
	private AnsichtSpalten viewColumn;

	@Column(name = "ANSICHTSPALTENID", insertable = true, updatable = true, nullable = false, unique = false)
	private Long viewColumnId;

	@Column(name = "REIHENFOLGE", updatable = true, nullable = false, unique = false)
	private Long order;

	@Column(name = "BREITE", updatable = true, nullable = false, unique = false)
	private Long width;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

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

	public UserView getUserView() {
		return userView;
	}

	public void setUserView(UserView userView) {
		this.userView = userView;
		this.userViewId = userView.getId();
	}

	public Long getUserViewId() {
		return userViewId;
	}

	public AnsichtSpalten getViewColumn() {
		return viewColumn;
	}

	public void setViewColumn(AnsichtSpalten viewColumn) {
		this.viewColumn = viewColumn;
		this.viewColumnId = viewColumn.getId();
	}

	public Long getViewColumnId() {
		return viewColumnId;
	}

	public Long getOrder() {
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

	public Long getWidth() {
		return width;
	}

	public void setWidth(Long width) {
		this.width = width;
	}

}
