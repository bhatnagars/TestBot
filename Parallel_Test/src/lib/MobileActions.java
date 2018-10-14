package lib;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;

import harness.ExtentTestNGReportBuilder;
import harness.Harness;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class MobileActions extends ExtentTestNGReportBuilder {
	
	private AndroidDriver<MobileElement> mobdriver;
	private static actions lib;
	
	public MobileActions(String param){
		lib = new actions(param);
		}
	
	
	public AndroidDriver<MobileElement> getbrowser(){
		 mobdriver = Harness.getmobbrowser();
		 if(mobdriver==null) {
			 System.out.println("=======skipping=================");
				skipclass("skip");
				throw new SkipException("skip");
		 }
		 return mobdriver;
	 }
	
	public void Tap(String strlocator) {
		try {
			WaitforElementPresent(strlocator,5);
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
	
	public void mobclick(String elelocator) {
		try {
		MobileElement ele= mobdriver.findElement(lib.Getlocator(elelocator));
		ele.click();
		pass("Clicked on element "+elelocator);
		}catch(Exception err) {
			fail(err.getMessage());
		}
		
	}
	
	/**
	 * 
	 * @param Ele_Name
	 * @param value
	 * @throws InterruptedException 
	 */
	public boolean WaitforElementPresent(String Ele_Name,int time){
	boolean blnFlg = false;
	do {
	try {
			WebDriverWait wait = new WebDriverWait(mobdriver,time);
			wait.until(ExpectedConditions.visibilityOfElementLocated((lib.Getlocator(Ele_Name))));
			blnFlg = true;
			break;
		} catch (Exception e) {
			time=time-1;

		}
	}while(time>1);
	 System.out.println(blnFlg);
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
	
	public boolean IsTextPresent(String strelement,String text) {
		boolean flgbln=false;
	try {
		WaitforElementPresent(strelement, 5);
		String eletext = mobdriver.findElement(lib.Getlocator(strelement)).getText().trim();
		if(eletext.equalsIgnoreCase(text)) {
			pass(eletext+" is displayed");
			flgbln=true;
		}else {
			fail(text+" is not displayed");
		}
	}catch(Exception err) {
		System.out.println(err.getMessage());
		fail(strelement+err.getMessage());
	}
	return flgbln;
	}


	public String testdata(String ErrMsg) {
		return lib.GettestData(ErrMsg);
	}
	
	public boolean Launchapp(String URL) {
		boolean result = false;
		mobdriver.get("https://linkedin.com");
		if(mobdriver.getTitle().contains("Linkedin")) {
			System.out.println("m here");
			pass("Application Luanched");
			result=true;
		}else {
			return result;
		}
		return result;
	}
	
	
	public void entervalue(String Ele_Name, String value) {
		WebElement elem_Username = null;
		try {
			if (WaitforElementPresent(Ele_Name,5)) {
				elem_Username = mobdriver.findElement(lib.Getlocator(Ele_Name));
				elem_Username.sendKeys(value);
				pass("Set the value '" + value + " ' on Eelement '" + Ele_Name + " ' successfully");
			} else {
				fail(Ele_Name + " is not visible on page");
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
