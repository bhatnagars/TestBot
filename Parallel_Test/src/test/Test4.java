package test;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import harness.ExtentTestNGReportBuilder;
import io.appium.java_client.android.AndroidDriver;
import lib.MobileActions;

public class Test4 extends ExtentTestNGReportBuilder {

		private  AndroidDriver<?> driver;
		private static MobileActions actions;

		@BeforeTest
		@Parameters("param")
		public void readdata(String param) {
			 actions = new MobileActions(param);
			 createTest(param,"Amazon app test");
			 driver = actions.getbrowser();
		}
	
@Test	
public 	void test4() {
	try {
		driver.launchApp();
		actions.Tap("Menu.List");
	    actions.Tap("Menu.ShopByCategory");
	    Thread.sleep(1000);
	    actions.Tap("ShopByCategory.MobnComp");
	    actions.IselementPresent("ShopByCategory.Mobiles");	
		} catch (Exception err) {
	System.out.println(err.getMessage());
	fail(err.getMessage());
}finally{	
	driver.quit();
}
}
}
