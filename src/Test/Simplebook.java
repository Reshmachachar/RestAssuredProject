package Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;
import Files.ReusableMethods;
import Files.SimplebookPayloads;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class Simplebook
{
	public static String bearerToken;
public static void main(String[] args)
{

	RestAssured.baseURI="https://simple-books-api.glitch.me";
	
	//status of API
	System.out.println("----------------------status of API----------------------");
	statusOfAPI();
	
	//List of all Book 
	System.out.println("----------------------List of Book----------------------");
	listOfBook();
	
//	//Get a single book
//	System.out.println("----------------------get single Book----------------------");
//	
//	getSingleBook();
	
//Token generation
	System.out.println("----------------------token generation----------------------");
	JsonPath js = ReusableMethods.rawToJson(registerApi("Reshma","reshma13chachar@gmail.com"));
	 bearerToken=js.getString("accessToken");    
     System.out.println("Token :"+bearerToken);

 	System.out.println("----------------------postorder----------------------");
     //Postorder
     JsonPath js1 = ReusableMethods.rawToJson(postOrder(4,"reshma13",bearerToken));
	   String orderid=js1.getString("orderId");
     System.out.println("order id is"+orderid);

 	System.out.println("----------------------getAll order ----------------------");
     //getAllorders
     getAllOrder(bearerToken);

 	System.out.println("----------------------get single order----------------------");
     //getSingleOrder
     getsingleOrder(orderid,bearerToken);
     

 	System.out.println("----------------------update order---------------------");
     //UpdateOrderPayload
     updateOrder(orderid, "RESHMA", bearerToken);
     getsingleOrder(orderid,bearerToken);
     
     //deleteOrder
     deleteOrder(orderid,bearerToken );
     System.out.println("order deleted successfully");
	
}
public static void statusOfAPI()
{
	given()
		.log().all()
	.when()
		.get("/status")
	.then()
		.log().all()
		.assertThat().statusCode(200)
		.body("status",equalTo("OK"));
}
public static void listOfBook()
{
	given()
		.log().all()
	.when()
		.get("/books")
    .then()
		.log().all()
		.assertThat().statusCode(200);
}

public static void getSingleBook()
{
	given()
		.log().all()
	.when()
		.get("books/{bookId}")
	.then()
		.log().all()
		.assertThat().statusCode(200);

}

public static String registerApi(String name,String email)
{	return
	given()
			.log().all()
			.header("Content-Type","application/json")
			.body(SimplebookPayloads.registerPayload(name,email))
		.when()
			.post("/api-clients/")
		.then()
			.log().all()
			.assertThat().statusCode(201)
			.extract().body().asString();
}
public static String postOrder(int bookId,String customerName,String bearerToken)
{
    return
    given()
    	.auth().oauth2(bearerToken)
    	.headers("Content-Type","application/json")
    	.body(SimplebookPayloads.postOrderPayload(bookId,customerName))
    .when()
    	.post("/orders/")
    .then()
    	.log().all()
    	.assertThat().statusCode(201)
    	.extract().body().asString();
}
public static void getAllOrder(String bearerToken)
{
	given()
    	.auth().oauth2(bearerToken)        
    .when()
    	.get("/orders")
    .then()
    	.log().all()
    	.assertThat().statusCode(200);     
}
public static void getsingleOrder(String orderId,String bearerToken)
{
	given()
    	.auth().oauth2(bearerToken)
    .when()
    	.get("/orders/{orderId}",orderId)
    .then()
    	.log().all()
    	.assertThat().statusCode(200);  
}
public static void updateOrder(String orderId,String customerName,String bearer_Token)
{

    given()
    	.auth().oauth2(bearer_Token)
    	.headers("Content-Type","application/json")
    	.body(SimplebookPayloads.updateOrderPayload(customerName))
    .when()
    	.patch("/orders/{orderId}",orderId)
    .then()
    	.log().all()
    	.assertThat().statusCode(204);
    
}
public static void deleteOrder(String orderId,String bearer_Token)
{

    given()
    	.auth().oauth2(bearer_Token)
    .when()
    	.delete("/orders/{orderId}",orderId)
    .then()
    	.log().all()
    	.assertThat().statusCode(204);
    
}
}