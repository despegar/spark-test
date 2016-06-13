package com.despegar.sparkjava.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.ClassRule;
import org.junit.Test;

import com.despegar.sparkjava.test.SparkClient.UrlResponse;

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
		UrlResponse response = testServer.getClient().doMethod("GET", "/test", null);
		assertEquals(200, response.status);
		assertEquals("This works!", response.body);
		assertNotNull(testServer.getApplication());
	}
	
	
}