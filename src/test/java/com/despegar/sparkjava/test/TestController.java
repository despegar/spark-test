package com.despegar.sparkjava.test;

import static spark.Spark.get;

import spark.Request;
import spark.Response;

/**
 * The class that defines a Spark Web Framework route
 * @author fwasy
 *
 */
public class TestController {

	public TestController() {
		get("/test", (request, response) ->  this.testMethod(request, response));
	}
	
	public String testMethod(Request request, Response response) {
		return "This works!";
	}
		
}