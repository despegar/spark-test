package com.despegar.sparkjava.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import spark.servlet.SparkApplication;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * Annotation that must be used in any class that tests a Spark Web Framework route
 * @author fwasy
 */
public @interface SparkTest {

	/**
	 * @return the port where the HTTP Server must run
	 */
	int port() default 8080;
	
	/**
	 * @return the {@link Class} that implements {@link SparkApplication} for the test
	 */
	Class<? extends SparkApplication> sparkApplicationClass();
		
}