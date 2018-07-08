package test;

import lib.*;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import harness.ExtentTestNGReportBuilder;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class Test3 extends ExtentTestNGReportBuilder {

	private  AndroidDriver<MobileElement> driver;
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
		  	//test is to test the app
		    actions.Launchapp("http://linkedin.com");
			actions.WaitforElementPresent("LinkedIn.Submitbutton", 5);
			actions.entervalue("LinkedIn.regUsrName", "asdsdsds@jshsd.com");
			actions.entervalue("LinkedIn.regPassword", "asdsdsdscom");
			actions.mobclick("LinkedIn.Submitbutton");
			//actions.IsTextPresent("LinkedIn.Err",actions.testdata("ErrMsg"));
		} catch (Exception err) {
			System.out.println(err.getMessage());
			fail(err.getMessage());
		}finally {
			driver.quit();
		}
  }
}

