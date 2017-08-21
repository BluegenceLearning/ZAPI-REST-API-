package com.test.jwt;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;


public class MyJWTGeneratorTest {
	
		
public static String getJWT(String ZephyrbaseURL, String accessKey, String secretKey, String userName) throws URISyntaxException, IllegalStateException, IOException{
		
		ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(ZephyrbaseURL, accessKey, secretKey, userName).build();
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		
				
		String createCycle="https://prod-api.zephyr4jiracloud.com/connect/public/rest/api/1.0/cycle?";
		
		
		URI uri = new URI(createCycle);
		int expirationInSec = 36000;
		String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);
		
		return jwt;
		
	} 
	
	
	/**

	public static void main(String[] args) throws URISyntaxException, IllegalStateException, IOException {
		
		
		
		// Replace Zephyr BaseUrl with the <ZAPI_CLOUD_URL> shared with ZAPI Cloud Installation
				String zephyrBaseUrl = "https://prod-api.zephyr4jiracloud.com/connect";
				// zephyr accessKey , we can get from Addons >> zapi section
				String accessKey = "YzcyZjQzNDgtNTU2YS0zOGJmLTk4ZTAtYTRhOTM4MGZhYjQ1IGFkbWluIFVTRVJfREVGQVVMVF9OQU1F";
				// zephyr secretKey , we can get from Addons >> zapi section
				String secretKey = "ux7gK59ouAz4zY7mF8rP1kdfBFNEESCTn53xksJxUCY";
				// Jira UserName
				String userName = "admin";
				
				/**
				ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, accessKey, secretKey, userName).build();
				JwtGenerator jwtGenerator = client.getJwtGenerator();
				
				// API to which the JWT token has to be generated
				//String createCycleUri = zephyrBaseUrl + "/public/rest/api/1.0/cycle?expand=&clonedCycleId=";
				
				String createCycle="https://prod-api.zephyr4jiracloud.com/connect/public/rest/api/1.0/cycle?";
				
				
				URI uri = new URI(createCycle);
				int expirationInSec = 36000;
				String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);
				
				
		
		       
				
				// Print the URL and JWT token to be used for making the REST call
				System.out.println("Below are the details ..... \n");
				
				//System.out.println("FINAL API : " + uri.toString());
				System.out.println("JWT Token : " + MyJWTGeneratorTest.getJWT(zephyrBaseUrl, accessKey, secretKey, userName));
				
				System.out.println(System.getProperty("user.dir"));

	}

**/
		
	
}
