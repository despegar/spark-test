package com.despegar.sparkjava.test;


import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.despegar.http.client.DeleteMethod;
import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HeadMethod;
import com.despegar.http.client.HttpClient;
import com.despegar.http.client.HttpClientException;
import com.despegar.http.client.HttpMethod;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.OptionsMethod;
import com.despegar.http.client.PatchMethod;
import com.despegar.http.client.PostMethod;
import com.despegar.http.client.PutMethod;

import spark.Service;
import spark.Spark;
import spark.servlet.SparkApplication;

/**
 * The server for running the test's {@link SparkApplication}
 * @author Fernando Wasylyszyn
 */
public class SparkServer<T extends SparkApplication> extends ExternalResource {

    private Class<T> sparkApplicationClass;
    
    private T sparkApplication;
    
    private int port;
    
    private String protocolHostPort;
    
    private HttpClient httpClient;
    
    /**
     * Constructor. It will use default Spark port ({@link Service#SPARK_DEFAULT_PORT}
     * @param sparkApplicationClass {@link SparkApplication} to use
     */
    public SparkServer(Class<T> sparkApplicationClass) {
    	this(sparkApplicationClass, Service.SPARK_DEFAULT_PORT);
    }
    
    /**
     * Constructor
     * @param sparkApplicationClass {@link SparkApplication} to use
     * @param port port where to run server
     */
    public SparkServer(Class<T> sparkApplicationClass, int port) {
    	this.sparkApplicationClass = sparkApplicationClass;
    	this.port = port;
    	this.protocolHostPort = "http://localhost:" + port;
    	this.httpClient = new HttpClient(1);
    }

    public T getApplication() {
    	return this.sparkApplication;
    }

    @Override
	public Statement apply(Statement base, Description description) {
    	return super.apply(base, description);
	}

	/* (non-Javadoc)
     * @see org.junit.rules.ExternalResource#before()
     */
    @Override
    protected void before() throws Throwable {
    	Spark.port(this.port);
    	this.sparkApplication = this.sparkApplicationClass.newInstance();
    	this.sparkApplication.init();
    	Spark.awaitInitialization();
    }
    
    public GetMethod get(String path, boolean followRedirect) {
		return new GetMethod(this.protocolHostPort + path, followRedirect);
	}
	
	public PostMethod post(String path, String body, boolean followRedirect) {
		return new PostMethod(this.protocolHostPort + path, body, followRedirect);
	}
	
	public PutMethod put(String path, String body, boolean followRedirect) {
		return new PutMethod(this.protocolHostPort + path, body, followRedirect);
	}
	
	public PatchMethod patch(String path, String body, boolean followRedirect) {
		return new PatchMethod(this.protocolHostPort + path, body, followRedirect);
	}
	
	public DeleteMethod delete(String path, boolean followRedirect) {
		return new DeleteMethod(this.protocolHostPort + path, followRedirect);
	}
	
	public OptionsMethod options(String path, boolean followRedirect) {
		return new OptionsMethod(this.protocolHostPort + path, followRedirect);
	}
	
	public HeadMethod head(String path, boolean followRedirect) {
		return new HeadMethod(this.protocolHostPort + path, followRedirect);
	}
	
	public HttpResponse execute(HttpMethod httpMethod) throws HttpClientException {
		return this.httpClient.execute(httpMethod);
	}

    /* (non-Javadoc)
     * @see org.junit.rules.ExternalResource#after()
     */
    @Override
    protected void after() {
    	this.sparkApplication.destroy();
    	Spark.stop();
    }
    
}