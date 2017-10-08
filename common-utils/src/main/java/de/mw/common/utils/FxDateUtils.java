/**
 * 
 */
package de.mw.common.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author wilbers
 * 
 */
public class FxDateUtils {

//	// conversion joda.DateTime to formatted date-output
//	Date date = new Date();
//    date.setTime( subspace.getLastUpdate().getMillis() );
//    DateFormat df = DateFormat.getDateInstance( DateFormat.MEDIUM, UserContext.getClientLocale() );
//    String formattedDate = df.format( date );
	
	public static Date date( final int year, final int month, final int day ) throws IllegalArgumentException {

		if ( month < 1 || month > 12 ) {
			throw new IllegalArgumentException( "Wrong month number." );
		}

		Calendar calendar = Calendar.getInstance();
		calendar.set( year, month - 1, day, 0, 0, 0 ); // month 0-based
		return calendar.getTime();
	}

	public static int getYear( final Date date ) {
		Calendar c = Calendar.getInstance();
		c.setTime( date );
		return c.get( Calendar.YEAR );
	}

	public static int getMonth( final Date date ) {
		Calendar c = Calendar.getInstance();
		c.setTime( date );
		return c.get( Calendar.MONTH ) + 1;
	}

	public static int getDayOfMonth( final Date date ) {
		Calendar c = Calendar.getInstance();
		c.setTime( date );
		return c.get( Calendar.DAY_OF_MONTH );
	}

	public static Date getZeroTimeDate( final Date date ) {
		Date res = date;
		Calendar calendar = Calendar.getInstance();

		calendar.setTime( date );
		calendar.set( Calendar.HOUR_OF_DAY, 0 );
		calendar.set( Calendar.MINUTE, 0 );
		calendar.set( Calendar.SECOND, 0 );
		calendar.set( Calendar.MILLISECOND, 0 );

		res = calendar.getTime();

		return res;
	}

	public static int compare( final Date date1, final Date date2, final boolean compareWithTime ) {
		if ( compareWithTime ) {
			return date1.compareTo( date2 );
		} else {
			return getZeroTimeDate( date1 ).compareTo( getZeroTimeDate( date2 ) );

			// TODO: maven-artefact joda-time bereits integriert, besser hier zu verwenden ?
		}
	}

}
