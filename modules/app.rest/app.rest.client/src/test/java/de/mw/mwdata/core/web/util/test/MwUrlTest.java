package de.mw.mwdata.core.web.util.test;

import java.net.MalformedURLException;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.mw.common.testframework.util.TestUtils;
import de.mw.mwdata.rest.url.RestUrl;

@Test
public class MwUrlTest {

	@Test
	public void testRestbasedMwUrl() throws MalformedURLException {

		RestUrl aURL = new RestUrl("http://localhost:8080/app.admin.web/admin/tabDef/42"); // /list.htm

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

		RestUrl aURL = new RestUrl("http://www.testdomain.com/app.admin.web/admin/tabDef/42"); // /list.htm

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
