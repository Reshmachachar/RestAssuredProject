package Test;
import io.restassured.RestAssured;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Files.ReusableMethods;
import Files.payLoads;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matcher.*;

public class Library {
	
		
	
	String authorName="John foe";
	
	//AddBook
	@Test(dataProvider="getBook")
	public static String addBookdata(String isbn,String aisle)
	{
		RestAssured.baseURI="http://216.10.245.166";	
	String addBookResponse=given()
	.log().all()
	
	.header("Content-Type","application/json")
	.body(payLoads.addBookPaylod(isbn,aisle))
	.when()
	    .post("/Library/Addbook.php")
	.then()
	    .log().all()
	    .assertThat().statusCode(200)
	   .extract().response().asString();
	   
	JsonPath js=new JsonPath(addBookResponse);
	String bookID=js.getString("ID");
			System.out.println(bookID);
			return bookID;
	}
	
	@DataProvider(name="getBook")
	public Object[][] bookData()
	{
		return new Object[][] { {"xyz","123"},{"lmn","456"},{"rrr","678"}};
	}
	
	
	
//	//getBookByauthorName
//	String getBookResponse=given()
//	     .log().all()
//	     .queryParam("AuthorName", authorName)
//	.when()
//	  .get("/Library/GetBook.php")
//	.then()
//	    .log().all()
//	    .assertThat().statusCode(200)
//	    .extract().response().asString();
//	JsonPath js2= ReusableMethods.rawToJson(getBookResponse);
//	   String actualAuthor=js2.getString("author");
//	//Assert.assertEquals(actualAuthor,authorName);
//		
//	//getBookById
//			String getBookResponseById=given()
//				.log().all()
//				.queryParam("ID", bookID)
//				
//			
//			.when()
//				.get("/Library/GetBook.php")
//				
//			.then()
//				.log().all()
//				.assertThat().statusCode(200)
//				.extract().response().asString();
//			
//			//parse json
//			JsonPath js1 =ReusableMethods.rawToJson(getBookResponseById);
//			String actualBookId = js.getString("ID");	
//			System.out.println(actualBookId);
//			Assert.assertEquals(bookID ,actualBookId);
	//
	}


