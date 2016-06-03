package com.despegar.sparkjava.test;


import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import spark.Service;
import spark.Spark;
import spark.servlet.SparkApplication;

/**
 * The server for running the test's {@link SparkApplication}
 * @author Fernando Wasylyszyn
 */
public class SparkServer extends ExternalResource {

    private Class<? extends SparkApplication> sparkApplicationClass;
    
    private int port;
    
    private SparkClient sparkClient;
    
    /**
     * Constructor. It will use default Spark port ({@link Service#SPARK_DEFAULT_PORT}
     * @param sparkApplicationClass {@link SparkApplication} to use
     */
    public SparkServer(Class<? extends SparkApplication> sparkApplicationClass) {
    	this.sparkApplicationClass = sparkApplicationClass;
    	this.port = Service.SPARK_DEFAULT_PORT;
    }
    
    /**
     * Constructor
     * @param sparkApplicationClass {@link SparkApplication} to use
     * @param port port where to run server
     */
    public SparkServer(Class<? extends SparkApplication> sparkApplicationClass, int port) {
    	this.sparkApplicationClass = sparkApplicationClass;
    	this.port = port;
    }

    public SparkClient getClient() {
        return this.sparkClient;
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
    	this.sparkApplicationClass.newInstance().init();
    	this.sparkClient = new SparkClient(this.port);
    }

    /* (non-Javadoc)
     * @see org.junit.rules.ExternalResource#after()
     */
    @Override
    protected void after() {
    	Spark.stop();
    }
    
}