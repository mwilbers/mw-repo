/**
 * 
 */
package de.mw.mwdata.core.web.validate;

import java.beans.PropertyEditorSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.service.ICrudService;
import de.mw.mwdata.core.utils.Utils;
import de.mw.mwdata.core.web.Config;


/**
 * General PropertyEditor for mwdata-specific Entity-Mapping in view
 * 
 * @author Wilbers, Markus
 * @version 1.0
 * @since Mar, 2011
 * 
 */
public class EntityPropertyEditor extends PropertyEditorSupport {

	private final Class<IEntity>			clazz;
	
	private ICrudService<IEntity> 			crudService;

	private List<IEntity>					items;
	private Map<String, IEntity>			itemMap	= new HashMap<String, IEntity>();

	public EntityPropertyEditor(final Class<IEntity> clazz, final ICrudService<IEntity> crudService) {

		this.clazz = clazz;
		this.crudService = crudService;
		Map<String,String> map = new HashMap<String, String>();
		this.items = (List<IEntity>) this.crudService.findAll(this.clazz, map);

		this.itemMap = Utils.listToMap( this.items );

		assert this.itemMap.size() == this.items.size();
	}

	@Override
	public String getAsText() {

		if ( getValue() == null ) {
			return "";
		}

		IEntity entity = this.clazz.cast( getValue() );
		return entity.getName();

	}

	@Override
	public void setAsText( final String text ) throws IllegalArgumentException {

		// TIP: look here
		// http://forum.springsource.org/showthread.php?33381-Step-by-Step-guide-Using-form-tag-listbox-with-Property-Editors
		// Use Case 2 ...

		if ( !StringUtils.isBlank( text ) && !Config.SELECTLIST_DEFAULTVALUE.equals( text ) ) {

			IEntity entity = this.itemMap.get( text );
			setValue( entity );

		}

	}
}
