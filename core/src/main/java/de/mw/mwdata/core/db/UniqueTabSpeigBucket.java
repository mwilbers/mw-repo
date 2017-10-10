package de.mw.mwdata.core.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig;

public class UniqueTabSpeigBucket {

	private List<ITabSpeig>				tableProps;
	private Map<Long, List<ITabSpeig>>	uniqueMap	= new HashMap<Long, List<ITabSpeig>>();

	public UniqueTabSpeigBucket(final List<ITabSpeig> tableProps) {
		this.tableProps = tableProps;
		init();
	}

	private void init() {

		for ( ITabSpeig tabSpeig : this.tableProps ) {

			if ( null != tabSpeig.getEindeutig() ) {

				List<ITabSpeig> entries = this.uniqueMap.get( tabSpeig.getEindeutig() );
				if ( org.springframework.util.CollectionUtils.isEmpty( entries ) ) {
					entries = new ArrayList<ITabSpeig>();
					this.uniqueMap.put( tabSpeig.getEindeutig(), entries );
				}

				entries.add( tabSpeig );

			}

		}

	}

	public List<ITabSpeig> getTabSpeigsByUniqueIdentifier( final Long uniqueIdent ) {
		return this.uniqueMap.get( uniqueIdent );
	}

}
