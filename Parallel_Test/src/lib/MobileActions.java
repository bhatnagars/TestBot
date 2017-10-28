package lib;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import harness.ExtentTestNGReportBuilder;
import harness.Harness;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class MobileActions extends ExtentTestNGReportBuilder {
	
	private AndroidDriver<MobileElement> mobdriver;
	private static actions lib;
	private static int timeout=5;
	
	public MobileActions(String param){
		
		lib = new actions(param);
		}
	
	
	public AndroidDriver<MobileElement> getbrowser(){
		 mobdriver = Harness.getmobbrowser();
		 return mobdriver;
	 }
	
	public void Tap(String strlocator) {
		try {
			WaitforElementPresent(strlocator);
			MobileElement a = mobdriver.findElement(lib.Getlocator(strlocator));
			a.tap(1, 1);
			pass("Clicked on element " + strlocator);
		} catch (Exception e) {
	
				fail("Not clicked on element " + strlocator + " as " + e.getMessage());
		}
	}
	

	public void wait(int sec) {
		mobdriver.manage().timeouts().implicitlyWait(sec, TimeUnit.SECONDS);
	}
	
	
	
	/**
	 * 
	 * @param Ele_Name
	 * @param value
	 */
	public boolean WaitforElementPresent(String Ele_Name) {
	boolean blnFlg = false;
	try {
		  if(timeout==1) {
			  return blnFlg;
		  }
			WebDriverWait wait = new WebDriverWait(mobdriver,5);
			wait.until(ExpectedConditions.visibilityOf(mobdriver.findElement(lib.Getlocator(Ele_Name))));
			blnFlg = true;
		} catch (Exception e) {
			timeout=timeout-1;
			WaitforElementPresent(Ele_Name);
		}
		return blnFlg;

	}
	
	public void waitForActivity(int wait) throws InterruptedException
	{
	    int counter = 0;
	    do {
	        Thread.sleep(1000);
	        counter++;
	    } while(mobdriver.currentActivity().contains("com.amazon.mShop.home.web.MShopWebGatewayActivity") && (counter<=wait));

	    System.out.println("Activity appeared :" + mobdriver.currentActivity());
	}
	
	
	
	public boolean IselementPresent(String strelement) {
		boolean flgbln=false;
	try {	
		if(mobdriver.findElement(lib.Getlocator(strelement)).isDisplayed()) {
			pass(strelement+" is displayed");
			flgbln=true;
		}else {
			fail(strelement+" is not displayed");
		}
	}catch(Exception err) {
		System.out.println(err.getMessage());
		fail(strelement+err.getMessage());
	}
	return flgbln;
	}

}
