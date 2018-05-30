package test;

import lib.*;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import harness.ExtentTestNGReportBuilder;

public class Test2 extends ExtentTestNGReportBuilder {

	private  WebDriver driver;
	private  WebActions lib;

	@BeforeTest
	@Parameters("param")
	public void readdata(String param) {
		 lib = new WebActions(param);
		 createTest(param,"Test2 description");
		 driver = lib.getbrowser(param);
	}

@Test
  public void atest2() {
	  try {
		    lib.Launch("https://linkedin.com");
			String title = driver.getTitle();
			if(title.equalsIgnoreCase("Google")){
				pass("Title is displayed as "+title+" as expected");
				
			}else{
				fail("Title is displayed as "+title+" as expected-test2");
			}
			if(title.equalsIgnoreCase("Google")){
				fail("Title is displayed as "+title+" as expected-test2");
			}else{
				fail("Title is not matching test2");
			}
			if(title.equalsIgnoreCase("Google")){
				fail("Title is displayed as "+title+" as expected-test2");
			}else{
				fail("Title is not matching");
			}	
		} catch (Exception err) {
			System.out.println(err.getMessage());
			
		}finally {
			driver.quit(); 
		}
  }
}
