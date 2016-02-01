package com.despegar.sparkjava.test;

import static org.junit.Assert.assertEquals;

import org.junit.ClassRule;
import org.junit.Test;

import com.despegar.sparkjava.test.TestSparkClient.UrlResponse;

import spark.servlet.SparkApplication;

/**
 * The test class
 * @author fwasy
 *
 */
@SparkTest(port = 10000, sparkApplicationClass = TestControllerTest.TestContollerTestSparkApplication.class)
public class TestControllerTest {

	public static class TestContollerTestSparkApplication implements SparkApplication {
		@Override
		public void init() {
			new TestController();
		}
	}
	
	@ClassRule
	public static TestSparkServer testServer = new TestSparkServer();
	
	@Test
	public void test() throws Exception {
		UrlResponse response = testServer.getClient().doMethod("GET", "/test", null);
		assertEquals(200, response.status);
		assertEquals("This works!", response.body);
	}
	
	
}