package Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import Files.ReusableMethods;

import static org.hamcrest.Matchers.*;
public class simpleBookAPI {
	
public static void main(String[] args) {
	
	RestAssured.baseURI="https://simple-books-api.glitch.me";
	//----------------------------------------------status of API-------------------------------
	
	given()
		.log().all()
	.when()
	     .get("/status")
	.then()
		.log().all()
		.assertThat().statusCode(200)
		.body("status",equalTo("OK"));
	
	//---------------------------------------------List of books -----------------------------
	
	given()
		.log().all()
	.when()
		.get("/books")
	.then()
		.log().all()
		.assertThat().statusCode(200);
		
	//---------------------------------------------Get a single book----------------------------
	String id="3";
	given()
		.log().all()
		//.pathParam("bookId", id)
		//.queryParam("bookId",id)//NaN
	.when()
		.get("/books/3")
	.then()
		.log().all()
		.assertThat().statusCode(200);
	
// //-----------------------------------------API Authentication-------------------------------
	String AccessToken=given()
		.log().all()
		.header("Content-Type","application/json")
		.body("{\r\n"
				+ "   \"clientName\": \"Reshma\",\r\n"
				+ "   \"clientEmail\": \"reshmachachar45@gmail.com\"\r\n"
				+ "}")
	.when()
		.post("/api-clients/")
	.then()
		.log().all()
		.assertThat().statusCode(201)
		.extract().response().asString();
	 JsonPath js = ReusableMethods.rawToJson(AccessToken);
	   String token=js.getString("accessToken");
       System.out.println("token is"+token);
       
     //----------------------------------------Submit an order-----------------------------------
       
       String orderID=given()
		.log().all()
		.header("Content-Type", "application/json")
		.header("Authorization", ""+token+"")
		.body("{\r\n"
				+ "  \"bookId\": 4,\r\n"
				+ "  \"customerName\": \"Reshna\"\r\n"
				+ "}")
		
		.when()
			.post("/orders")
			
		.then()
			.log().all()
			.assertThat().statusCode(201)
	        .extract().response().asString();
       JsonPath js1 = ReusableMethods.rawToJson(orderID);
	   String orderid=js1.getString("orderId");
       System.out.println("order id is"+orderid);
       		
      //--------------------------------Get all orders------------------------------
		given()
			.log().all()
			.header("Content-Type", "application/json")
			.header("Authorization", ""+token+"")
		.when()
			.get("/orders")
		.then()
			.log().all()
			.assertThat().statusCode(200);
			
      	//-----------------------------Get an order-------------------------------

		given()
		.log().all()
			.header("Authorization", ""+token+"")
		.when()
			 .get("/orders/"+orderid+"")
		.then()
			 .log().all()
			 .assertThat().statusCode(200);
//-------------------------------Update an order-------------------------	
	given()
	.log().all()
	.header("Authorization", ""+token+"")
	.body("{\r\n" + 
			"  \"customerName\": \"ChacharReshma\"\r\n" + 
			"}")
	.when()
		.patch("/orders/"+orderid+"")
	
	.then()
		.log().all()
		.assertThat().statusCode(204);
	
	   
	

		
}
}
