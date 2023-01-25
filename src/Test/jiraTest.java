package Test;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;

import java.io.File;

import org.testng.Assert;

import Files.ReusableMethods;

public class jiraTest {
	public static void main(String[] args) {
		
		//login 
		RestAssured.baseURI="http://localhost:8080";
		
		SessionFilter sf=new SessionFilter();
	String cookieLoginResponce= given()
		.log().all()
		.header("Content-Type","application/json")
	    .body("{\r\n"
	    		+ "    \"username\": \"reshma.chachar\",\r\n"
	    		+ "    \"password\": \"Reshma@123\"\r\n"
	    		+ "}")
	    .filter(sf)
	   .when()
	     .post("/rest/auth/1/session")
	   .then()
	    	.log().all()
	    	.extract().response().asString();
		
	//create Issue 
	String idResponse=given()
		.log().all()
		.header("Content-Type","application/json")
		.body("{\r\n"
				+ "    \"fields\": {\r\n"
				+ "        \"project\": {\r\n"
				+ "            \"key\": \"RESTAA\"\r\n"
				+ "        },\r\n"
				+ "        \"summary\": \"This is my first issue in project RestAssured\",\r\n"
				+ "        \"description\": \"Creating my first bug\",\r\n"
				+ "         \"issuetype\": {\"name\": \"Task\"}\r\n"
				+ "        }\r\n"
				+ "    }\r\n"
				+ "}")
		.filter(sf)
		
	.when()
		.post("/rest/api/2/issue/")
	.then()
		.log().all()
		.assertThat().statusCode(201)
		.extract().response().asString();
		JsonPath js2=ReusableMethods.rawToJson(idResponse);
		String ID=js2.getString("id");
		
	
	//AddComment
	String expComment="hiiiii how are you,this is comment ";
String addCommentResponse=	given()
	   .log().all()
	   .header("Content-Type","application/json")
	   .pathParam("key",""+ID+"")
	   .body("{\r\n"
	   		+ "    \"body\": \""+expComment+"\"\r\n"
	   		+ "}")
	   .filter(sf)
	  .when()
	  		.post("/rest/api/2/issue/{key}/comment")
	  .then()
	  	.log().all()
	  	.assertThat().statusCode(201)
	  	.extract().response().asString();
	  	JsonPath js=ReusableMethods.rawToJson(addCommentResponse);
		String addCommentId=js.get("id");//getting id of addComment 	     	
	   
	   //add Attachment 
		
		given()
		.log().all()
		.filter(sf)
		.pathParam("key",""+ID+"")
		.header("X-Atlassian-Token"," no-check")
		.header("Content-Type","multipart/form-data")
		.multiPart("file",new File("F:\\RestAssuredTraining\\DemoProject\\src\\Test\\myFile.txt"))
		.when()
		.post("/rest/api/2/issue/{key}/attachments")
		
		.then()
		.log().all()
		.assertThat().statusCode(200);
		
		
	//getIssue
		String getIssueResponse=	
				given()
					.log().all()
					.filter(sf)
					.pathParam("key",""+ID+"")
					.queryParam("fields", "comment")
				.when()
					.get("/rest/api/2/issue/{key}")
				.then()
					.log().all()
					.extract().response().asString();
		JsonPath js1=ReusableMethods.rawToJson(getIssueResponse);
		String issueResp=js1.getString("comment") ;
		
//Validation		
		// find count of response
		  int courseCount = js1.getInt("fields.comment.comments.size()");
	        System.out.println("total course count is"+courseCount);
	       
	        for(int i=0;i<courseCount;i++)
	        {
	        	if(js1.getString("fields.comment.comments["+i+"].id").equalsIgnoreCase(addCommentId))
	        			//equals(""+addCommentId+""))
	        			{

	        		String actualComm=js1.getString("fields.comment.comments["+i+"].body");
	        		   Assert.assertEquals(expComment,actualComm);
	        			System.out.println(actualComm);
	        		}
	        	else
	        	{
	        		System.out.println("assertion failed ");
	        	}
	        	
	        }

}
	}
