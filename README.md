# Spark Web Framework Test Library

This library allows testing [Spark Web Framework](http://sparkjava.com/) based applications through HTTP. This way, the complete flow of the application can be tested, from the parsing of a HTTP request through business logic, executing asserts against HTTP responses headers, status, body, etc.

## Getting Started

### Dependency

#### Maven

Add to your **pom.xml** dependencies

```xml
<dependency>
    <groupId>com.despegar</groupId>
    <artifactId>spark-test</artifactId>
    <version>1.1.1</version>
</dependency>
```

#### Gradle

Add to your **build.gradle** dependencies:

```json
dependencies {
    testCompile: "com.despegar:spark-test:1.1.1"
}
```

### Usage

```java
/**
 * The class that defines a Spark Web Framework route
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
```

```java
/**
 * The test class
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
```
