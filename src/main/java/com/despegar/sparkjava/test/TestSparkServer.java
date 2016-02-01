package com.despegar.sparkjava.test;


import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import spark.servlet.SparkApplication;
import spark.servlet.SparkFilter;

/**
 * The server for running the test's {@link SparkApplication}
 * @author Fernando Wasylyszyn
 */
public class TestSparkServer extends ExternalResource {

    private int port;
    
    private Class<? extends SparkApplication> sparkApplicationClass;
    
    private TestSparkClient testApiClient;

    private Server httpServer;

    public TestSparkClient getClient() {
        return this.testApiClient;
    }

    @Override
	public Statement apply(Statement base, Description description) {
    	SparkTest sparkTest = description.getAnnotation(SparkTest.class);
    	this.sparkApplicationClass = sparkTest.sparkApplicationClass();
		this.port = sparkTest.port();
		return super.apply(base, description);
	}

	/* (non-Javadoc)
     * @see org.junit.rules.ExternalResource#before()
     */
    @Override
    protected void before() throws Throwable {
        this.testApiClient = new TestSparkClient(this.port);
        this.httpServer = new Server();
        ServerConnector connector = new ServerConnector(this.httpServer);
        connector.setPort(this.port);
        this.httpServer.setConnectors(new Connector[] {connector});
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setServer(this.httpServer);
        webAppContext.setContextPath("/");
        FilterHolder filterHolder = new FilterHolder(new SparkFilter());
        filterHolder.setInitParameter(SparkFilter.APPLICATION_CLASS_PARAM, this.sparkApplicationClass.getName());
        webAppContext.addFilter(filterHolder, "/*", EnumSet.of(DispatcherType.REQUEST));
        webAppContext.setResourceBase(System.getProperty("java.io.tmpdir"));
        this.httpServer.setHandler(webAppContext);
        this.httpServer.start();
    }

    /* (non-Javadoc)
     * @see org.junit.rules.ExternalResource#after()
     */
    @Override
    protected void after() {
        this.testApiClient = null;
        try {
            this.httpServer.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}