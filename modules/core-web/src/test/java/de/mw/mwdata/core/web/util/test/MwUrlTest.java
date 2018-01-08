package de.mw.mwdata.core.web.util.test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.mw.mwdata.core.web.util.MwUrl;
import de.mw.mwdata.core.web.util.RestbasedMwUrl;

@Test
public class MwUrlTest {

	@Test(enabled = false)
	public void testMwUrl() throws MalformedURLException {

		MwUrl aURL = new MwUrl("http://localhost:8080/app.admin.web/admin/tabDef"); // /list.htm

		System.out.println("protocol = " + aURL.getProtocol());
		Assert.assertEquals(aURL.getProtocol(), "http");

		System.out.println("host = " + aURL.getHost());
		Assert.assertEquals(aURL.getHost(), "localhost");

		System.out.println("port = " + aURL.getPort());
		Assert.assertEquals(aURL.getPort(), 8080);

		System.out.println("contextPath = " + aURL.getContextPath());
		Assert.assertEquals(aURL.getContextPath(), "app.admin.web");

		System.out.println("servletPath = " + aURL.getServletPath());
		Assert.assertEquals(aURL.getServletPath(), "admin");

		System.out.println("servletSubPath = " + aURL.getServletSubPath());
		Assert.assertEquals(aURL.getServletSubPath(), "tabDef");

	}

	@Test
	public void testRestbasedMwUrl() throws MalformedURLException {

		List<String> excludedPaths = new ArrayList<>();
		RestbasedMwUrl aURL = new RestbasedMwUrl("http://localhost:8080/app.admin.web/admin/tabDef/42", excludedPaths); // /list.htm

		System.out.println("protocol = " + aURL.getProtocol());
		Assert.assertEquals(aURL.getProtocol(), "http");

		System.out.println("host = " + aURL.getHost());
		Assert.assertEquals(aURL.getHost(), "localhost");

		System.out.println("port = " + aURL.getPort());
		Assert.assertEquals(aURL.getPort(), 8080);

		System.out.println("contextPath = " + aURL.getContextPath());
		Assert.assertEquals(aURL.getContextPath(), "app.admin.web");

		System.out.println("servletPath = " + aURL.getServletPath());
		Assert.assertEquals(aURL.getServletPath(), "admin");

		System.out.println("entityName = " + aURL.getEntityName());
		Assert.assertEquals(aURL.getEntityName(), "tabDef");

		System.out.println("entityId = " + aURL.getEntityId());
		Assert.assertEquals(aURL.getEntityId(), 42);

	}

}
