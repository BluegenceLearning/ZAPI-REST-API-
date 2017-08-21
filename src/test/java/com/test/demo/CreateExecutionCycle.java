package com.test.demo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;

public class CreateExecutionCycle {

	public static void main(String[] args) throws URISyntaxException, JSONException, IllegalStateException, IOException, Exception {
		
		// Replace zephyr baseurl <ZAPI_Cloud_URL> shared with the user for ZAPI Cloud
		String zephyrBaseUrl = "https://prod-api.zephyr4jiracloud.com/connect";
		
		// zephyr accessKey , we can get from Addons >> zapi section
		String accessKey = "YzcyZjQzNDgtNTU2YS0zOGJmLTk4ZTAtYTRhOTM4MGZhYjQ1IGFkbWluIFVTRVJfREVGQVVMVF9OQU1F";
		
		// zephyr secretKey , we can get from Addons >> zapi section
		String secretKey = "ux7gK59ouAz4zY7mF8rP1kdfBFNEESCTn53xksJxUCY";
		
		String userName = "admin";
		
		ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, accessKey, secretKey, userName)
				.build();

		/** Declare the Variables here **/
		int projectId = 10001;
		double versionId = -1;
		String cycleName = "Hanover October Release Execution";
		String cycleDescription = "Created by Cognizant QA Team";
		String build = "Regression New";
		String environment = "UAT";
		
		String createCycleUri = zephyrBaseUrl + "/public/rest/api/1.0/cycle?";
		
		/** Cycle Object created - DO NOT EDIT **/
		JSONObject createCycleObj = new JSONObject();
		createCycleObj.put("name", cycleName);        			
		createCycleObj.put("description", cycleDescription);			
		createCycleObj.put("startDate", System.currentTimeMillis());
		createCycleObj.put("projectId", projectId);
		createCycleObj.put("versionId", versionId);
		createCycleObj.put("build", build);
		createCycleObj.put("environment", environment);

		StringEntity cycleJSON = null;
		try {
			cycleJSON = new StringEntity(createCycleObj.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		CreateExecutionCycle cc = new CreateExecutionCycle();
		
		String cycleID = cc.createCycle(createCycleUri, client, accessKey, cycleJSON);
		System.out.println("Cycle Created with Cycle Id :" + cycleID);
		
		
		/**
		 * Add tests to Cycle IssueId's
		 * 
		 */
		
		String addTestsUri = zephyrBaseUrl + "/public/rest/api/1.0/executions/add/cycle/" + cycleID;
		
		String[] issueIds = {"PL-12"}; //Issue Id's to be added to Test Cycle, add more issueKeys separated by comma. Issues must be created in JIRA before adding from here

		JSONObject addTestsObj = new JSONObject();
		addTestsObj.put("issues", issueIds);
		addTestsObj.put("method", "1");
		addTestsObj.put("projectId", projectId);
		addTestsObj.put("versionId", versionId);

		StringEntity addTestsJSON = null;
		try {
			addTestsJSON = new StringEntity(addTestsObj.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String ID = cc.addTestsToCycle(addTestsUri, client, accessKey, addTestsJSON);
		System.out.println("Tests added successfully" + ID);

	}
	
	public String createCycle(String uriStr, ZFJCloudRestClient client, String accessKey, StringEntity cycleJSON)
			throws URISyntaxException, JSONException {

		URI uri = new URI(uriStr);
		int expirationInSec = 360;
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);
		System.out.println(uri.toString());
		System.out.println(jwt);

		HttpResponse response = null;
		
		//HttpClient restClient = new DefaultHttpClient();
		
		HttpClient restClient = HttpClientBuilder.create().build();

		HttpPost createCycleReq = new HttpPost(uri);
		createCycleReq.addHeader("Content-Type", "application/json");
		createCycleReq.addHeader("Authorization", jwt);
		createCycleReq.addHeader("zapiAccessKey", accessKey);
		createCycleReq.setEntity(cycleJSON);

		try {
			response = restClient.execute(createCycleReq);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println(statusCode);
		String cycleId = "-1";
		if (statusCode >= 200 && statusCode < 300) {
			HttpEntity entity = response.getEntity();
			String string = null;
			try {
				string = EntityUtils.toString(entity);
				JSONObject cycleObj = new JSONObject(string);
				cycleId = cycleObj.getString("id");
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			try {
				throw new ClientProtocolException("Unexpected response status: " + statusCode);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
		}
		return cycleId;
	}
	
	
	public String addTestsToCycle(String uriStr, ZFJCloudRestClient client, String accessKey, StringEntity addTestsJSON)
			throws URISyntaxException, JSONException, IllegalStateException, IOException {

		URI uri = new URI(uriStr);
		int expirationInSec = 360;
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);
		System.out.println(uri.toString());
		System.out.println(jwt);

		HttpResponse response = null;
		
		//HttpClient restClient = new DefaultHttpClient();
		
		HttpClient restClient = HttpClientBuilder.create().build();

		HttpPost addTestsReq = new HttpPost(uri);
		addTestsReq.addHeader("Content-Type", "application/json");
		addTestsReq.addHeader("Authorization", jwt);
		addTestsReq.addHeader("zapiAccessKey", accessKey);
		addTestsReq.setEntity(addTestsJSON);

		try {
			response = restClient.execute(addTestsReq);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println(statusCode);
		System.out.println(response.toString());
		String string = null;
		if (statusCode >= 200 && statusCode < 300) {
			HttpEntity entity = response.getEntity();			
			try {
				string = EntityUtils.toString(entity);
				//System.out.println(string);
				JSONObject cycleObj = new JSONObject(entity);
				System.out.println(cycleObj.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			try {
				throw new ClientProtocolException("Unexpected response status: " + statusCode);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
		}
		return string;
	}


}
