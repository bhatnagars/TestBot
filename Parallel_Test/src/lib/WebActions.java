package lib;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import harness.ExtentTestNGReportBuilder;
import harness.Harness;

public class WebActions extends ExtentTestNGReportBuilder {
	
	private   WebDriver driver;
	private  actions lib;
	
	public WebActions(String param){
		lib = new actions(param);
	}
	
	public  WebDriver getbrowser(String param) {
		driver = Harness.getbrowser(param.split(";")[2]);
		if(driver==null) {
			Reporter.log("Driver is null");
		}
		return driver;
	}
	
	
	public void Launch(String URL){
		try {
			driver.get(URL);
		//	driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			pass(URL + " is launched");
		} catch (Exception e) {
			fail(URL + e);
		}

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
		WebElement element = driver.findElement(lib.Getlocator(strlocator));
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
			WebElement a = driver.findElement(lib.Getlocator(strlocator));
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

	public void Navigate(String navigate2) {
		WebElement Navilink = null;
		try {
			System.out.println(navigate2);
			String Navigate[] = navigate2.split(">");
			for (int a = 0; a < Navigate.length; a++) {
				Thread.sleep(2000);
					Navilink = driver.findElement(By.xpath("//*[@class='nav navbar-nav navbar-right']//a[contains(text(),'" + Navigate[a].trim() + "')]"));
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
				elem_Username = driver.findElement(lib.Getlocator(Ele_Name));
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
			WebDriverWait wait = new WebDriverWait(driver,5);
			wait.until(ExpectedConditions.visibilityOf(driver.findElement(lib.Getlocator(Ele_Name))));
			blnFlg=true;
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

public boolean IselementPresent(String strelement) {
	boolean flgbln=false;
try {	
	if(driver.findElement(lib.Getlocator(strelement)).isDisplayed()) {
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

	public boolean verifyTextPresence(String Text) {
		boolean flg = false;
		try {
			driver.findElement(By.xpath("//*[contains(text(),'" + Text + "')]"));
			flg = true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			flg = false;
		}
		return flg;
	}
	
public String getdata(String param) {
	System.out.println(param+"=-=-=-=-=-=-getdata");
	String  data = "";
	data = lib.GettestData(param);
	return data;
}


public String getglbdata(String param) {
	String data ="";
	data=lib.Getglbldata(param);
	
	return data;
}
	


}
