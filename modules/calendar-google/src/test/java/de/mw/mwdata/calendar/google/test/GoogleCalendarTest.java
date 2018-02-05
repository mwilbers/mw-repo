package de.mw.mwdata.calendar.google.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

public class GoogleCalendarTest {

	/** Application name. */
	private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";

	/** Directory to store user credentials for this application. */
	// private static final java.io.File DATA_STORE_DIR = new
	// java.io.File(System.getProperty("user.home"),
	// ".credentials/calendar-java-quickstart");
	private static final java.io.File DATA_STORE_DIR = new java.io.File("./src/main/resources",
			".credentials/calendar-java-quickstart");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/calendar-java-quickstart
	 */
	private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR_READONLY);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	public static Credential authorize() throws IOException {
		// Load client secrets.

		// 1. for user based google account
		// Build flow and trigger user authorization request.
		// InputStream in =
		// GoogleCalendarTest.class.getResourceAsStream("/client_secret.json"); //
		// target/classes
		// GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
		// new InputStreamReader(in));
		// GoogleAuthorizationCodeFlow flow = new
		// GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
		// clientSecrets,
		// SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		// Credential credential = new AuthorizationCodeInstalledApp(flow, new
		// LocalServerReceiver()).authorize("user");

		// 2. for service-account ...
		// reads json file from src/test/resources
		InputStream in = GoogleCalendarTest.class.getClassLoader().getResourceAsStream("MyProject-b7cf98003143.json");
		GoogleCredential credential = GoogleCredential.fromStream(in)
				.createScoped(Collections.singleton(CalendarScopes.CALENDAR));

		System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}

	/**
	 * Build and return an authorized Calendar client service.
	 * 
	 * @return an authorized Calendar client service
	 * @throws IOException
	 */
	public static com.google.api.services.calendar.Calendar getCalendarService() throws IOException {
		Credential credential = authorize();

		return new com.google.api.services.calendar.Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
	}

	@Test(enabled = true)
	public void testGoogleCalendar() throws IOException {
		// Build a new authorized API client service.
		// Note: Do not confuse this class with the
		// com.google.api.services.calendar.model.Calendar class.
		com.google.api.services.calendar.Calendar service = getCalendarService();

		// for error "401 Unauthorized" delete StoredCredentials file first in java
		// folders

		DateTime now = new DateTime(System.currentTimeMillis());

		// for service-acccounts use concrete calendar-id instead of "primary"
		// holunderteetest@gmail.com
		// calendar-id schverein luenne:
		// bqb7kije2prlv7jqrsgask6bpc@group.calendar.google.com
		Events events = service.events().list("bqb7kije2prlv7jqrsgask6bpc@group.calendar.google.com").execute();

		// List the next 10 events from the primary calendar.
		// Events events =
		// service.events().list("primary").setMaxResults(10).setTimeMin(now).setOrderBy("startTime")
		// .setSingleEvents(true).execute();

		List<Event> items = events.getItems();
		if (items.size() == 0) {
			System.out.println("No upcoming events found.");
		} else {
			System.out.println("Upcoming events");
			for (Event event : items) {
				DateTime start = event.getStart().getDateTime();
				if (start == null) {
					start = event.getStart().getDate();
				}
				System.out.printf("%s (%s)\n", event.getSummary(), start);
			}
		}

		// List<com.google.api.services.calendar.model.Calendar> cals = new
		// ArrayList<>();
		// for (int i = 0; i < 100; i++) {
		// com.google.api.services.calendar.model.Calendar newCal =
		// createCalendar(service, i);
		// cals.add(newCal);
		//
		// }
		//
		// for (int i = 99; i >= 0; i--) {
		// com.google.api.services.calendar.model.Calendar cal = cals.get(i);
		// service.calendars().delete(cal.getId()).execute();
		//
		// }
		//
		// System.out.println("Calendars deleted !");
		// service.calendars().delete(newCal.getId()).execute();

	}

	private com.google.api.services.calendar.model.Calendar createCalendar(Calendar service, int index)
			throws IOException {

		// Create a new calendar
		com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
		calendar.setSummary("calendarSummary" + index);
		calendar.setTimeZone("America/Los_Angeles");

		// Insert the new calendar
		com.google.api.services.calendar.model.Calendar createdCalendar = service.calendars().insert(calendar)
				.execute();

		System.out.println(createdCalendar.getId());
		return createdCalendar;

	}

	// *********************** some more samples ************************

	// /**
	// * Be sure to specify the name of your application. If the application name is
	// {@code null} or
	// * blank, the application will log a warning. Suggested format is
	// "MyCompany-ProductName/1.0".
	// */
	// private static final String APPLICATION_NAME = "";
	//
	// /** Directory to store user credentials. */
	// private static final java.io.File DATA_STORE_DIR =
	// new java.io.File(System.getProperty("user.home"), ".store/calendar_sample");
	//
	// /**
	// * Global instance of the {@link DataStoreFactory}. The best practice is to
	// make it a single
	// * globally shared instance across your application.
	// */
	// private static FileDataStoreFactory dataStoreFactory;
	//
	// /** Global instance of the HTTP transport. */
	// private static HttpTransport httpTransport;
	//
	// /** Global instance of the JSON factory. */
	// private static final JsonFactory JSON_FACTORY =
	// JacksonFactory.getDefaultInstance();
	//
	// private static com.google.api.services.calendar.Calendar client;
	//
	// static final java.util.List<Calendar> addedCalendarsUsingBatch =
	// Lists.newArrayList();
	//
	// /** Authorizes the installed application to access user's protected data. */
	// private static Credential authorize() throws Exception {
	// // load client secrets
	// GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
	// new
	// InputStreamReader(CalendarSample.class.getResourceAsStream("/client_secrets.json")));
	// if (clientSecrets.getDetails().getClientId().startsWith("Enter")
	// || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
	// System.out.println(
	// "Enter Client ID and Secret from
	// https://code.google.com/apis/console/?api=calendar "
	// + "into calendar-cmdline-sample/src/main/resources/client_secrets.json");
	// System.exit(1);
	// }
	// // set up authorization code flow
	// GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	// httpTransport, JSON_FACTORY, clientSecrets,
	// Collections.singleton(CalendarScopes.CALENDAR)).setDataStoreFactory(dataStoreFactory)
	// .build();
	// // authorize
	// return new AuthorizationCodeInstalledApp(flow, new
	// LocalServerReceiver()).authorize("user");
	// }
	//
	// public static void main(String[] args) {
	// try {
	// // initialize the transport
	// httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	//
	// // initialize the data store factory
	// dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
	//
	// // authorization
	// Credential credential = authorize();
	//
	// // set up global Calendar instance
	// client = new com.google.api.services.calendar.Calendar.Builder(
	// httpTransport, JSON_FACTORY,
	// credential).setApplicationName(APPLICATION_NAME).build();
	//
	// // run commands
	// showCalendars();
	// addCalendarsUsingBatch();
	// Calendar calendar = addCalendar();
	// updateCalendar(calendar);
	// addEvent(calendar);
	// showEvents(calendar);
	// deleteCalendarsUsingBatch();
	// deleteCalendar(calendar);
	//
	// } catch (IOException e) {
	// System.err.println(e.getMessage());
	// } catch (Throwable t) {
	// t.printStackTrace();
	// }
	// System.exit(1);
	// }
	//
	// private static void showCalendars() throws IOException {
	// View.header("Show Calendars");
	// CalendarList feed = client.calendarList().list().execute();
	// View.display(feed);
	// }
	//
	// private static void addCalendarsUsingBatch() throws IOException {
	// View.header("Add Calendars using Batch");
	// BatchRequest batch = client.batch();
	//
	// // Create the callback.
	// JsonBatchCallback<Calendar> callback = new JsonBatchCallback<Calendar>() {
	//
	// @Override
	// public void onSuccess(Calendar calendar, HttpHeaders responseHeaders) {
	// View.display(calendar);
	// addedCalendarsUsingBatch.add(calendar);
	// }
	//
	// @Override
	// public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) {
	// System.out.println("Error Message: " + e.getMessage());
	// }
	// };
	//
	// // Create 2 Calendar Entries to insert.
	// Calendar entry1 = new Calendar().setSummary("Calendar for Testing 1");
	// client.calendars().insert(entry1).queue(batch, callback);
	//
	// Calendar entry2 = new Calendar().setSummary("Calendar for Testing 2");
	// client.calendars().insert(entry2).queue(batch, callback);
	//
	// batch.execute();
	// }
	//
	// private static Calendar addCalendar() throws IOException {
	// View.header("Add Calendar");
	// Calendar entry = new Calendar();
	// entry.setSummary("Calendar for Testing 3");
	// Calendar result = client.calendars().insert(entry).execute();
	// View.display(result);
	// return result;
	// }
	//
	// private static Calendar updateCalendar(Calendar calendar) throws IOException
	// {
	// View.header("Update Calendar");
	// Calendar entry = new Calendar();
	// entry.setSummary("Updated Calendar for Testing");
	// Calendar result = client.calendars().patch(calendar.getId(),
	// entry).execute();
	// View.display(result);
	// return result;
	// }
	//
	//
	// private static void addEvent(Calendar calendar) throws IOException {
	// View.header("Add Event");
	// Event event = newEvent();
	// Event result = client.events().insert(calendar.getId(), event).execute();
	// View.display(result);
	// }
	//
	// private static Event newEvent() {
	// Event event = new Event();
	// event.setSummary("New Event");
	// Date startDate = new Date();
	// Date endDate = new Date(startDate.getTime() + 3600000);
	// DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
	// event.setStart(new EventDateTime().setDateTime(start));
	// DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
	// event.setEnd(new EventDateTime().setDateTime(end));
	// return event;
	// }
	//
	// private static void showEvents(Calendar calendar) throws IOException {
	// View.header("Show Events");
	// Events feed = client.events().list(calendar.getId()).execute();
	// View.display(feed);
	// }
	//
	// private static void deleteCalendarsUsingBatch() throws IOException {
	// View.header("Delete Calendars Using Batch");
	// BatchRequest batch = client.batch();
	// for (Calendar calendar : addedCalendarsUsingBatch) {
	// client.calendars().delete(calendar.getId()).queue(batch, new
	// JsonBatchCallback<Void>() {
	//
	// @Override
	// public void onSuccess(Void content, HttpHeaders responseHeaders) {
	// System.out.println("Delete is successful!");
	// }
	//
	// @Override
	// public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) {
	// System.out.println("Error Message: " + e.getMessage());
	// }
	// });
	// }
	//
	// batch.execute();
	// }
	//
	// private static void deleteCalendar(Calendar calendar) throws IOException {
	// View.header("Delete Calendar");
	// client.calendars().delete(calendar.getId()).execute();
	// }

}
