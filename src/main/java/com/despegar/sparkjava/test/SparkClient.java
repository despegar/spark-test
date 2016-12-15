package com.despegar.sparkjava.test;

import com.despegar.http.client.DeleteMethod;
import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpClient;
import com.despegar.http.client.HttpClientException;
import com.despegar.http.client.HttpMethod;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.OptionsMethod;
import com.despegar.http.client.PatchMethod;
import com.despegar.http.client.PostMethod;
import com.despegar.http.client.PutMethod;

/**
 * Client for make HTTP requests
 * 
 * @author fwasy
 */
public class SparkClient {
	
	private String protocolHostPort;
	
	private HttpClient httpClient;

	SparkClient(int port) {
		this.protocolHostPort = "http://localhost:"+port;
		this.httpClient = new HttpClient(1);
	}
	
	public GetMethod get(String path) {
		return new GetMethod(this.protocolHostPort + path);
	}
	
	public PostMethod post(String path, String body) {
		return new PostMethod(this.protocolHostPort + path, body);
	}
	
	public PutMethod put(String path, String body) {
		return new PutMethod(this.protocolHostPort + path, body);
	}
	
	public PatchMethod patch(String path, String body) {
		return new PatchMethod(this.protocolHostPort + path, body);
	}
	
	public DeleteMethod delete(String path) {
		return new DeleteMethod(this.protocolHostPort + path);
	}
	
	public OptionsMethod options(String path) {
		return new OptionsMethod(this.protocolHostPort + path);
	}
	
	public HttpResponse execute(HttpMethod httpMethod) throws HttpClientException {
		return this.httpClient.execute(httpMethod);
	}
	

	


}
