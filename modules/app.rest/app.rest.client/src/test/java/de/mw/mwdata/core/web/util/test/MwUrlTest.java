package de.mw.mwdata.core.web.util.test;

import java.net.MalformedURLException;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.mw.common.testframework.util.TestUtils;
import de.mw.mwdata.rest.client.util.MwUrl;
import de.mw.mwdata.rest.service.RestbasedMwUrl;

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

		RestbasedMwUrl aURL = new RestbasedMwUrl("http://localhost:8080/app.admin.web/admin/tabDef/42"); // /list.htm

		TestUtils.out("protocol = " + aURL.getProtocol());
		Assert.assertEquals(aURL.getProtocol(), "http");

		TestUtils.out("host = " + aURL.getHost());
		Assert.assertEquals(aURL.getHost(), "localhost");

		TestUtils.out("port = " + aURL.getPort());
		Assert.assertEquals(aURL.getPort(), 8080);

		TestUtils.out("contextPath = " + aURL.getContextPath());
		Assert.assertEquals(aURL.getContextPath(), "app.admin.web");

		TestUtils.out("servletPath = " + aURL.getServletPath());
		Assert.assertEquals(aURL.getServletPath(), "admin");

		TestUtils.out("entityName = " + aURL.getEntityName());
		Assert.assertEquals(aURL.getEntityName(), "tabDef");

		TestUtils.out("entityId = " + aURL.getEntityId());
		Assert.assertEquals(aURL.getEntityId(), 42);

	}

	@Test
	public void testDomainAndRestbasedMwUrl() throws MalformedURLException {

		RestbasedMwUrl aURL = new RestbasedMwUrl("http://www.testdomain.com/app.admin.web/admin/tabDef/42"); // /list.htm

		TestUtils.out("protocol = " + aURL.getProtocol());
		Assert.assertEquals(aURL.getProtocol(), "http");

		TestUtils.out("host = " + aURL.getHost());
		Assert.assertEquals(aURL.getHost(), "www.testdomain.com");

		TestUtils.out("port = " + aURL.getPort());
		Assert.assertEquals(aURL.getPort(), -1);

		TestUtils.out("contextPath = " + aURL.getContextPath());
		Assert.assertEquals(aURL.getContextPath(), "app.admin.web");

		TestUtils.out("servletPath = " + aURL.getServletPath());
		Assert.assertEquals(aURL.getServletPath(), "admin");

		TestUtils.out("entityName = " + aURL.getEntityName());
		Assert.assertEquals(aURL.getEntityName(), "tabDef");

		TestUtils.out("entityId = " + aURL.getEntityId());
		Assert.assertEquals(aURL.getEntityId(), 42);

	}

}
