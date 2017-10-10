package de.mw.mwdata.app.admin.angweb3.control;

import java.util.Date;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.ofdb.domain.ITabDef;

public class JsonTabDef  implements ITabDef {


	private String name;
	private Long id;
	private Boolean system;
	private String ofdb;
	private String angelegtVon;
	private Date angelegtAm;
	
	public JsonTabDef() {
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return this.id;
	}
	@Override
	public void setId(Long id) {
		this.id = id;
		
	}
	@Override
	public String getSequenceKey() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isInDB() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setZeittyp(ZEITTYP zeittyp) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public ZEITTYP getZeittyp() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setDatenbank(DATENBANK datenbank) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public DATENBANK getDatenbank() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setFullClassName(String fullClassName) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getFullClassName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return null;
	} 
	
	// from abstractmwtentity
	@Override
	public String toString() {
		return getName();
	}

	// implementations ...

	


	public void setOfdb( final String ofdb ) {
		this.ofdb = ofdb;
	}

	public String getOfdb() {
		return this.ofdb;
	}

	public void setSystem( final Boolean system ) {
		this.system = system;
	}

	public Boolean isSystem() {
		return this.system;
	}
//
//	public Boolean getSystem() {
//		return isSystem();
//	}

	@Override
	public boolean equals( final Object obj ) {

		if ( null == obj ) {
			return false;
		}

		AbstractMWEntity otherEntity = (AbstractMWEntity) obj;

		if ( null == this.getName() && null != otherEntity.getName() ) {
			return false;
		}
		if ( this.getName().equalsIgnoreCase( otherEntity.getName() ) ) {
			return true;
		}

		return false;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.getName() == null) ? 0 : this.getName().hashCode());
		return result;
	}
	
}