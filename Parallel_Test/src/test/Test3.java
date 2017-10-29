package test;

import lib.*;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import harness.ExtentTestNGReportBuilder;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class Test3 extends ExtentTestNGReportBuilder {

	private static AndroidDriver<MobileElement> driver;
	private static MobileActions actions;

	@BeforeTest
	@Parameters("param")
	public void readdata(String param) {
		actions = new MobileActions(param);
		 createTest(param,"Amazon app test");
		 driver = actions.getbrowser();
	}

@Test
  public void gtest3() {
	  try {
		  //test is to test the
			driver.get("https://linkedin.com");
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
			fail(err.getMessage());
			
		}finally{	
			driver.quit();
		}
  }
}

