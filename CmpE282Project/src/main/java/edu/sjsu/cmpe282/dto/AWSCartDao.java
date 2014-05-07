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
import com.amazonaws.services.dynamodb.model.AttributeAction;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodb.model.ComparisonOperator;
import com.amazonaws.services.dynamodb.model.Condition;
import com.amazonaws.services.dynamodb.model.Key;
import com.amazonaws.services.dynamodb.model.ScanRequest;
import com.amazonaws.services.dynamodb.model.ScanResult;
import com.amazonaws.services.dynamodb.model.UpdateItemRequest;
import com.amazonaws.services.dynamodb.model.UpdateItemResult;

import edu.sjsu.cmpe282.domain.Product;

public class AWSCartDao {
	static AmazonDynamoDBClient client;
	static String tableName = "Cart_Catalog";
	
	public AWSCartDao() {
		// TODO Auto-generated constructor stub
		
		
		try {
			AWSCredentials credentials = new PropertiesCredentials(AWS_Freemarker2.class.getResourceAsStream("templates/AwsCredentials.properties"));
			System.out.println("Properties file loaded.");
			System.out.println("Access Key : "+credentials.getAWSAccessKeyId());
			System.out.println("Secret Access Key : "+credentials.getAWSSecretKey());
			client = new AmazonDynamoDBClient(credentials);
			client.setRegion(Region.getRegion(Regions.US_WEST_1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Exception thrown in AWSDao() constructor while loading AWS credential file.");
		}
	}
	
	public boolean AddToCart(String email, String productid){
		System.out.println("Update cart for "+email+" by adding "+productid);
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
		  Condition condition = new Condition()
		  .withComparisonOperator(ComparisonOperator.EQ.toString())
		  .withAttributeValueList(new AttributeValue().withS(email));
		  scanFilter.put("email", condition);
		  ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);
		  ScanResult scanResult = client.scan(scanRequest);
		  System.out.println("Scan Result Object: "+scanResult);
		  List<Map<String,AttributeValue>> result = scanResult.getItems();
		  System.out.println("Scan Result List : "+result);
		  List<String> cartEntries = result.get(0).get("items").getSS();
		  System.out.println("Cart Entries : "+cartEntries);
		  cartEntries.add(productid);
		  
		  Map<String, AttributeValueUpdate> updateItems = new HashMap<String, AttributeValueUpdate>();
		  HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
			key.put("items", new AttributeValue().withSS(cartEntries));
		  updateItems.put("items", new AttributeValueUpdate(new AttributeValue(cartEntries), AttributeAction.PUT));
		  UpdateItemRequest updateItemRequest = new UpdateItemRequest()
		  	.withTableName(tableName)
		  	.withKey(new Key(new AttributeValue(email)))
		  	.withAttributeUpdates(updateItems);
		  
		  UpdateItemResult updateItemResult = client.updateItem(updateItemRequest);
		  System.out.println("Cart updated for : "+email);
		  System.out.println("Update Item Result : "+updateItemResult.toString()); 	  	
		  
		  return true;
	}

	public void showCart(String email){
		System.out.println("Fetching shopping cart...");
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
		  Condition condition = new Condition()
		  .withComparisonOperator(ComparisonOperator.EQ.toString())
		  .withAttributeValueList(new AttributeValue().withS(email));
		  double total_price=0;
		  scanFilter.put("email", condition);
		  ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);
		  ScanResult scanResult = client.scan(scanRequest);
		  List<Map<String,AttributeValue>> result = scanResult.getItems();
		  System.out.println("Result : "+result);
		  List<String> cartEntries = new ArrayList<String>();
		  List<Product> cartProducts = new ArrayList<Product>();
		  cartEntries = result.get(0).get("items").getSS();
		  System.out.println("Cart Entries : "+cartEntries);
		  System.out.println("Cart Entries size : "+cartEntries.size());
		  for(int i=0;i<cartEntries.size()-1;i++){
			  //System.out.println(cartEntries.get(i));
			  Condition con = new Condition()
			  	.withComparisonOperator(ComparisonOperator.EQ.toString())
			  	.withAttributeValueList(new AttributeValue().withS(cartEntries.get(i)));
			  scanFilter = new HashMap<String, Condition>();
			  scanFilter.put("product_id", con);
			  scanRequest = new ScanRequest(AWSDao.tableName).withScanFilter(scanFilter);
			  scanResult = client.scan(scanRequest);
			  Map<String,String> cartProductMap = new HashMap<String, String>();
			  result = scanResult.getItems();
			  System.out.println("ScanResult : "+scanResult);
			  String name = result.get(0).get("product_name").getS(); cartProductMap.put("product_name", name);
			  String price = result.get(0).get("price").getN(); cartProductMap.put("price", price); 
			  String url = result.get(0).get("image").getS(); cartProductMap.put("image", url);
			  String cat = result.get(0).get("category").getS(); cartProductMap.put("category", cat);
			  String productid = result.get(0).get("product_id").getS(); cartProductMap.put("product_id", productid);
			  total_price = total_price + Double.parseDouble(price);
			  cartProducts.add(new Product(name,price,url,cat,productid));
		  }
		  //System.out.println(cartProducts);
		  System.out.println("Total Price : "+total_price);
		  
		  Freemarker fm = new Freemarker();
		  fm.setCartTemplate();
		  fm.writeOutput(cartProducts,total_price);
	}
}
