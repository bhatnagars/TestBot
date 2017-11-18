package test;

import lib.*;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import harness.ExtentTestNGReportBuilder;

public class test2 extends ExtentTestNGReportBuilder {

	private  WebDriver driver;
	private static actions lib;

	@BeforeTest
	@Parameters("param")
	public void readdata(String param) {
		 lib = new actions(param);
		 createTest(param,"Test2 description");
		 driver = lib.getbrowser(param);
	}

@Test
  public void atest2() {
	  try {
			driver.get("https://linkedin.com");
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
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
