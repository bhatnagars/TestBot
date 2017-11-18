package lib;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.SkipException;

import harness.ExtentTestNGReportBuilder;
import harness.Harness;



public class actions extends ExtentTestNGReportBuilder{

	private  WebDriver driver;
	private AndroidDriver<WebElement> mobdriver;
	private  Map<String, String> globalData, OR;
	private  Map<String, String>  TestData;
	private  static Harness harness;

	public actions(String param){
		harness = new Harness();
		globalData = Harness.GetPropertyData();
		OR = Harness.GetOR();
		TestData =  harness.Readtestdata(param);
		if (OR.isEmpty() || globalData.isEmpty() || TestData.isEmpty() ) {
			skipclass(param);
			throw new SkipException("skip");
		}
		}

	
	public WebDriver getbrowser(String param) {
		driver = Harness.getbrowser(param.split(";")[2]);
		if(driver==null) {
			Reporter.log("Driver is null");
		}
		return driver;
	}
	
	
// public AndroidDriver<WebElement> getbrowser(){
//	 mobdriver = Harness.getmobbrowser();
//	 return mobdriver;
// }

	public void Launch(String URL){
		try {
			driver.get(URL);
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			pass(URL + " is launched");
		} catch (Exception e) {
			fail(URL);
		}

	}

	/** get data from Properties file */
	public String Getglbldata(String strKey) {
		String value = "";
		try {
			value = globalData.get(strKey);
		} catch (Exception er) {
			System.out.println(er.getMessage());
		}
		return value;
	}

	public String GettestData(String strkey) {
		String strdata = "";
		try {
			strdata = TestData.get(strkey);
		} catch (Exception err) {
			fail(err.getMessage());
		}
		return strdata;
	}

	public By Getlocator(String strlocator) {
		String locator = "";
		By by=null;
		try {
			locator = OR.get(strlocator);
		   String locatortype= locator.split(";")[0].trim();
		    locator = locator.split(";")[1].trim();
			switch (locatortype.toLowerCase()) {
			case "xpath" :
				   by= By.xpath(locator);
				   break;
			case "id" :
				  by= By.id(locator);
				  break;
			case "className" :
				  by= By.className(locator);
				  break;
				
			}
		} catch (Exception err) {
			System.out.println(err.getMessage());
		}
		return by;
	}

	

	public void click(WebElement strlocator) {
		try {
			String text =strlocator.getText();
			strlocator.click();
			pass("Clicked on "+text);
		} catch (Exception e) {
			fail("Failed to clicked on "+strlocator.getText());
		}
	}
	
	public void jsclick(String strlocator) {
		try {
		WebElement element = driver.findElement(Getlocator(strlocator));
		JavascriptExecutor js = (JavascriptExecutor)	driver;
		js.executeScript("arguments[0].click();", element);
		pass("Clicked on ");
		} catch (Exception e) {
			fail("Failed to click");
		}
	}
	
	public void jsclick(WebElement element) {
		try {
		String text = element.getText();	
		JavascriptExecutor js = (JavascriptExecutor)	driver;
		js.executeScript("arguments[0].click();", element);
		pass("Clicked on "+text);
		} catch (Exception e) {
			fail("Failed to click");
		}
	}	
	

	public void click(String strlocator) {
		try {
			WebElement a = driver.findElement(Getlocator(strlocator));
			a.click();
			pass("Clicked on element " + strlocator);
		} catch (Exception e) {
			try {
				jsclick(strlocator);
			}catch(Exception err) {
				fail("Not clicked on element " + strlocator + " as " + e.getMessage());
			}
		}
	}

	public void Navigate(String strNavigate) {
		WebElement Navilink = null;
		try {
			String Navigate[] = strNavigate.split(">");
			for (int a = 0; a < Navigate.length; a++) {
				Thread.sleep(2000);
					Navilink = driver.findElement(By.xpath("//*[@class='navbar-collapse collapse']//a[contains(text(),'" + Navigate[a].trim() + "')]"));
					jsclick(Navilink);
			}
		} catch (Exception e) {
			fail("Failed to Naigate to "+ Navilink+"\n"+e.getMessage());
		}
	}

	/**
	 * 
	 * @param Ele_Name
	 * @param value
	 */
	public void entervalue(String Ele_Name, String value) {
		WebElement elem_Username = null;
		try {
			if (WaitforElementPresent(Ele_Name)) {
				elem_Username = driver.findElement(Getlocator(Ele_Name));
				elem_Username.sendKeys(value);
				pass("Set the value '" + value + " ' on Eelement '" + Ele_Name + " ' successfully");
			} else {
				fail(Ele_Name + " is not visible on page");
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * 
	 * @param Ele_Name
	 * @param value
	 */
	public boolean WaitforElementPresent(String Ele_Name) {
		boolean blnFlg = false;
		try {
			if(driver!=null) {
			WebDriverWait wait = new WebDriverWait(driver,5);
			wait.until(ExpectedConditions.visibilityOf(driver.findElement(Getlocator(Ele_Name))));
			}else {
				WebDriverWait wait = new WebDriverWait(mobdriver,5);
				wait.until(ExpectedConditions.visibilityOf(mobdriver.findElement(Getlocator(Ele_Name))));
			}
			blnFlg = true;
		} catch (Exception e) {
			fail(e.getMessage());
		}
		return blnFlg;

	}

	public void smartsync(int time) throws InterruptedException {
		int itry = 0;
		boolean flg=false;
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			do{
		  flg =(boolean)js.executeScript("return (document.readyState == 'complete' && jQuery.active == 0)");
				itry = itry + 1;
				Thread.sleep(1000);
			}while (flg==false || itry<time);
		} catch (Exception err2) {
			System.out.println("Something went wrong");
		}

	}

public boolean verifytitle(String param) {
try {	
	if(driver.getTitle().contains(param)) {
	pass(param+" is displayed as expected");
	return true;
	}else {
		fail(driver.getTitle()+" is displayed expected  was "+param);
		return false;
	}
	}catch(Exception er) {
		return false;	
	}
}



	

}