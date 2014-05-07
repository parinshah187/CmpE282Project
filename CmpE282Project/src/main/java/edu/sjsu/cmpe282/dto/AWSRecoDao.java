package edu.sjsu.cmpe282.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodb.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.ComparisonOperator;
import com.amazonaws.services.dynamodb.model.Condition;
import com.amazonaws.services.dynamodb.model.ScanRequest;
import com.amazonaws.services.dynamodb.model.ScanResult;

import edu.sjsu.cmpe282.domain.Product;

public class AWSRecoDao {
	static AmazonDynamoDBClient client;
	static String tableName = "recommendation_catalog";

	// Constructor with AWS DynamoDB connection
	// Setup AWS Client to read DynamoDB data
	public AWSRecoDao() {
		// TODO Auto-generated constructor stub
		try {
			AWSCredentials credentials = new PropertiesCredentials(
					AWS_Freemarker2.class
							.getResourceAsStream("templates/AwsCredentials.properties"));
			System.out.println("Properties file loaded.");
			System.out.println("Access Key : "
					+ credentials.getAWSAccessKeyId());
			System.out.println("Secret Access Key : "
					+ credentials.getAWSSecretKey());
			client = new AmazonDynamoDBClient(credentials);
			client.setRegion(Region.getRegion(Regions.US_WEST_1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out
					.println("Exception thrown in AWSDao() constructor while loading AWS credential file.");
		}
	}

	public String QueryRecommendations(String userID){
		
		System.out.println("Creating Recommendation page...");
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
		Condition condition = new Condition().withComparisonOperator(
				ComparisonOperator.EQ.toString()).withAttributeValueList(
				new AttributeValue().withS(userID));
		scanFilter.put("userID", condition);
		ScanRequest scanRequest = new ScanRequest(tableName)
		.withScanFilter(scanFilter);
		ScanResult scanResult = client.scan(scanRequest);
		List<Map<String,AttributeValue>> result = scanResult.getItems();
		System.out.println("Result : "+result);
		List<String> recoEntries = new ArrayList<String>();
		recoEntries = result.get(0).get("recommendation_skus").getSS();
		System.out.println("Recommendation Entries : "+recoEntries);
		System.out.println("Recommendation Entries size : "+recoEntries.size());
		Freemarker fm = new Freemarker();
		fm.setRecoTemplate();
		return fm.writeHTMLOutput(recoEntries);
	}
}
