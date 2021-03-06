package de.mw.mwdata.core;

/**
 * State enum that identifies the current state of the application. The {@link DefaultApplicationFactory} keeps this
 * state for sending events of the progress.
 *
 * @author mwilbers
 *
 */
public enum ApplicationState {

	CONFIGURE, // ofdb config data has to be inserted, e.g. when setting new ofdb data or before unit tests
	INITIALIZE, // ofdb config data has to be loaded to application cache
	RUNNING, // ofdb cache loading is finished and application is running
	OFF;

}
