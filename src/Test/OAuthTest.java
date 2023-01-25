package Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import Files.ReusableMethods;
import static org.hamcrest.Matchers.*;

public class OAuthTest
{
 public static void main(String[] args) {
	System.out.println("n develop branch ");
	
		 
		 
	String url="https://rahulshettyacademy.com/getCourse.php?state=verify&code=4/0AWtgzh5ghf1tc6KuKnEwJWyG5FSROKiqm5mO6Qpn_hJHdpnA0MqshYsqeJUjniIfBx4u9A&scope=email%20https://www.googleapis.com/auth/userinfo.email%20openid&authuser=0&hd=cogniwize.com&prompt=none";
	String partialUrl=url.split("code=")[1];
	String code=partialUrl.split("&scope")[0];
		//getToken
		 String getAccessToken= given()
		 	   .log().all()
		 	   .urlEncodingEnabled(false)
		 	   .queryParam("code",code)
		 	   .queryParam("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		 	   .queryParam("client_secret","erZOWM9g3UtwNRj340YYaK_W")
		 	   .queryParam("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
		 	   .queryParam("grant_type","authorization_code")
		 	   
		 	 .when()
		 	      .post("https://www.googleapis.com/oauth2/v4/token")
		 	  .then()
		 	        .log().all()
		 	        .extract().response().asString();
		 	        System.out.println(getAccessToken);
		 	        JsonPath jpath=new JsonPath(getAccessToken);
		 	        String accessToken=jpath.getString("access_token");
		 	        System.out.println(accessToken);
		//getCourse
		String getcourseDetails=given()
		.log().all()
		  .queryParam("access_token",accessToken)
		 .when()
		 	.get("https://rahulshettyacademy.com/getCourse.php")
		 	.asString();
		 	System.out.println(getcourseDetails);
		 	
		 
	}

}
