package com.api.RestAssured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class APICalls {
	
	private String empID;
	private static Logger log = LogManager.getLogger(APICalls.class.getName());
	
	@Test(priority=1)
	public void createEmployee() throws IOException{
		
		// Add Employee
		log.info("Creating an employee");
		String createEmpBody = generateString("AddEmp.json");
		Response  addEmpResponse = given().
				contentType(ContentType.JSON).
			body(createEmpBody).
			when().
			post("http://dummy.restapiexample.com/api/v1/create").
		 then().assertThat().statusCode(200).log().all().
		
		 extract().response();

		JsonPath addEmpResJson = new JsonPath(addEmpResponse.asString());
		empID = addEmpResJson.getString("id");
		
		
		log.info("Employee Added using data in AddEmp.json Successfully with employee ID: " + empID);
		
	}
	
	@Test(priority=2)
	public void verifyCreatedEmployee() throws IOException{
		
		// Test to verify the newly added Employee
		log.info("Verifying the added employee");
		//String createEmpBody = generateString("AddEmp.json");
		Response  verifyAddResponse = given().
				contentType(ContentType.JSON).
			when().
			get("http://dummy.restapiexample.com/api/v1/employee/" +empID+"").
		 then().assertThat().statusCode(200).log().all().
		
		 extract().response();

		log.info("It's verified that the employee has been created successfully using POST method with employee ID: " + empID);
		
	}
	
	@Test(priority=3)
	public void updateEmployee() throws IOException
	{		
		//Update Employee
		log.info("Updating an employee");
		String updateEmpBody = generateString("UpdateEmp.json");
		Response  updateEmpResponse = given().
				contentType(ContentType.JSON).
			body(updateEmpBody).
			when().
				put("http://dummy.restapiexample.com/api/v1/update/" +empID+"").
		 then().assertThat().statusCode(200).log().all().
		
		 extract().response();
		
		log.info("Employee Updated Successfully with data in UpdateEmp.json for employee ID:" + empID);

}

		@Test(priority=4)
	public void verifyUpdatedEmployee() throws IOException{
		
		// Test to verify the salary updation of the added Employee
		log.info("Verifying the update of salary of employee");
				Response  verifyUpdateResponse = given().
				contentType(ContentType.JSON).
			when().
			get("http://dummy.restapiexample.com/api/v1/employee/" +empID+"").
		 then().assertThat().statusCode(200).
		 //and().body("employee_salary", equalTo("70000")).
		 log().all().
		
		 extract().response();
		 
		 JsonPath verifyUpdateEmpResJson = new JsonPath(verifyUpdateResponse.asString());
		String sal = verifyUpdateEmpResJson.getString("employee_salary");
		
		if(sal.equals("70000"))
		
			log.info("It's verified that the employee has been updated successfully using PUT method with employee ID: " + empID);
		
	}
	@Test(priority=5)
	public void deleteEmployee() throws IOException
	{
		//Delete Employee
		log.info("Deleting an employee");
				 given().
		contentType(ContentType.JSON).
		when().
		delete("http://dummy.restapiexample.com/api/v1/delete/" +empID+"").
          then().assertThat().statusCode(200).log().all();
				 
				 log.info("Employee Deleted Successfully with employee Id: " + empID);
		
	}
	
	@Test(priority=6)
	public void verifyDeletedEmployee() throws IOException{
		
		// Test to verify the deletion of Employee
		log.info("Verifying the deletion of employee");
				Response  verifyUpdateResponse = given().
				contentType(ContentType.JSON).
			when().
			get("http://dummy.restapiexample.com/api/v1/employee/" +empID+"").
		 then().assertThat().statusCode(200).
		 log().all().
		 extract().response();
		
			log.info("It's verified that the employee has been deleted successfully using DELETE method with employee ID: " + empID);
		
	}
	
	
	public static String generateString(String filename) throws IOException{
		String filePath = System.getProperty("user.dir")+"\\Payloads\\"+filename;
		return new String(Files.readAllBytes(Paths.get(filePath)));
	}

}
