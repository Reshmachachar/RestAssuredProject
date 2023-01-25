package Test;
import io.restassured.RestAssured;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;

import Files.ReusableMethods;
import Files.payLoads;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matcher.*;
public class Demo {

	public static void main(String[] args) {
		
	//AddPlace
		
	RestAssured.baseURI="https://rahulshettyacademy.com";
	
 String addPlaceResponse=given()
		 
	  .log().all()
	  .queryParam("key"," qaclick123")
	  .header("Content-Type","application/json")
	  .body(payLoads.addPlacePayload())
	  	.when()
	  	    .post("/maps/api/place/add/json")
         .then()
            .log().all()
            .assertThat().statusCode(200)
            .body("scope",equalTo("APP"))
            //.header("Server","Apache/2.4.4(Ubuntu)")
	       
	       .extract().response().asString();
	     
	     
	     JsonPath js=new JsonPath(addPlaceResponse);
	     String placeID=js.getString("place_id");
	     System.out.println(placeID);
	 
	String newAddress="70 winter walk, USA";
	//UpdatePlace
	     given()
	     .queryParam("key", "qaclick123")
	     .queryParam("place_id",placeID)
	     .header("Content-Type","application/json")
	     .body("{\r\n"
	     		+ "\"place_id\":\""+placeID+"\",\r\n"
	     		+ "\"address\":\""+newAddress+"\",\r\n"
	     		+ "\"key\":\"qaclick123\"\r\n"
	     		+ "}")
	     .when()
	        .put("/maps/api/place/update/json")
	     .then()
	     .log().all()
	     .assertThat().statusCode(200)
	     .body("msg", equalTo("Address successfully updated"));	     
	
	//GetPlace
	 String getPlaceResponse= given()
	   .log().all()
	    .queryParam("key", "qaclick123")
	    .queryParam("place_id",placeID)
	    
	   .when()
	     .get("/maps/api/place/get/json")
	     
	    .then()
	    .log().all()
	    .assertThat().statusCode(200)
	    .extract().response().asString();
	 
	 //   .body("address",equalTo("70 winter walk, USA"));
	 JsonPath js1=ReusableMethods.rawToJson(getPlaceResponse);
	 String actualAdd=js1.getString("address");
	 System.out.println(actualAdd);
	   Assert.assertEquals(actualAdd,newAddress);
	    
	    
	     
	    
	}



}
