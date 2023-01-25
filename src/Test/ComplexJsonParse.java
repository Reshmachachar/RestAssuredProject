package Test;

import org.testng.Assert;

import Files.ReusableMethods;
import Files.payLoads;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {
	
	public static void main(String[] args) {
		
		JsonPath js = ReusableMethods.rawToJson(payLoads.getCoursesPayload());

		//1. Print No of courses returned by API
        int courseCount = js.getInt("courses.size()");
        System.out.println("total course count is"+courseCount);
        
        //2.Print Purchase Amount
       int purchesAmount = js.getInt("dashboard.purchaseAmount");
        System.out.println("purchesAmount is"+purchesAmount);

       // 3. Print Title of the first course
         String firstCourseTitle = js.getString("courses[0].title"); 
         System.out.println("first course title is "+firstCourseTitle);

        //4. Print All course titles and their respective Prices
         for(int i=0;i<courseCount;i++)
         {
             String courseTitles = js.getString("courses["+i+"].title");
             System.out.println(courseTitles);
             System.out.println(js.getInt("courses["+i+"].price"));
         }
         
        //5. Print no of copies sold by RPA Course
         for(int i=0;i<courseCount;i++)
         {
        	 String courseTitles = js.getString("courses["+i+"].title");
			    if(courseTitles.equals("RPA"))
			    {
			    System.out.println("number of copies in RPA="+js.getInt("courses["+i+"].copies"));
			    }
         }
         
         //6. Verify if Sum of all Course prices matches with Purchase Amount
         //course prices 
         int coursePrice=0;
         for(int i=0;i<courseCount;i++)
         {
        	    int prize = js.getInt("courses["+i+"].price");
        	    int copies = js.getInt("courses["+i+"].copies");
        	     coursePrice =coursePrice + (prize*copies);
         }
           System.out.println("coursePrice is "+coursePrice);
           if(coursePrice==purchesAmount)
           {
        	   System.out.println("coursePrice and purchase amount is equal");
           }
           Assert.assertEquals(purchesAmount, coursePrice);
         
}
}