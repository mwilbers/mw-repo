package de.mw.mwdata.core.mocks;

import java.util.Date;

import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.domain.BenutzerBereich;
import de.mw.mwdata.core.domain.Sequence;

public class CoreMockFactory {

	public static Sequence createSequenceMock(final String sequenceName, final long inkrement,
			final long letzteBelegteNr) {

		Sequence sequence = new Sequence();
		sequence.setName(sequenceName); // FX_TabDef_K:DSID
		sequence.setInkrement(inkrement);
		sequence.setLetzteBelegteNr(letzteBelegteNr);
		sequence.setSystem(Boolean.TRUE);
		sequence.setAngelegtAm(new Date());
		sequence.setAngelegtVon(Constants.SYS_USER_DEFAULT);

		return sequence;

	}

	public static BenutzerBereich createBenutzerBereichMock(final String bereichName) {
		BenutzerBereich bereichMock = new BenutzerBereich();
		bereichMock.setName(bereichName);
		bereichMock.setSystem(Boolean.TRUE);

		return bereichMock;
	}

}
