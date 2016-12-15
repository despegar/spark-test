package com.despegar.sparkjava.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.ClassRule;
import org.junit.Test;

import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpResponse;

import spark.servlet.SparkApplication;

/**
 * The test class
 * @author fwasy
 *
 */
public class TestControllerTest {

	public static class TestContollerTestSparkApplication implements SparkApplication {
		@Override
		public void init() {
			new TestController();
		}
	}
	
	@ClassRule
	public static SparkServer<TestContollerTestSparkApplication> testServer = new SparkServer<>(TestControllerTest.TestContollerTestSparkApplication.class, 4567);
	
	@Test
	public void test() throws Exception {
		SparkClient sparkClient = testServer.getClient();
		GetMethod get = sparkClient.get("/test");
		get.addHeader("Test-Header", "test");
		HttpResponse httpResponse = sparkClient.execute(get);
		assertEquals(200, httpResponse.code());
		assertEquals("This works!", new String(httpResponse.body()));
		assertNotNull(testServer.getApplication());
	}
	
	
}