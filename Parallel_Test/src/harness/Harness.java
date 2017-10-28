package harness;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class Harness extends ExtentTestNGReportBuilder {

	private static Map<String, String> OR = new HashMap<>();
	private static Map<String, Object> testdata = new HashMap<>();
	private static Map<String, String> datamap = new HashMap<>();
	private static Map<String, String> Propmap = new HashMap<>();
	private static ThreadLocal<WebDriver> threadedDriver = new ThreadLocal<WebDriver>();
	private static ThreadLocal<AndroidDriver<MobileElement>> mobDriver = new ThreadLocal<AndroidDriver<MobileElement>>();
	private static ThreadLocal<String> TestDataRow = new ThreadLocal<String>();
	private static ThreadLocal<String> Browser = new ThreadLocal<String>();
	private static ArrayList<String> list = new ArrayList<>();

	public void ReadTestConfig() {
		try {
			File ORFile = new File("Obj_Rep//OR.json");
			File TestdataFile = new File("TestData//Testdata.json");
			OR = GetORMap(ORFile.getAbsolutePath().toString());
			testdata = ParseTestData(TestdataFile.getAbsolutePath().toString());
		} catch (Exception err) {
			System.out.println(err.getMessage());
		}
	}

	public static ArrayList<String> ReadExecutorfile() throws Exception {
		int Execution_col = 0, Browser_col = 0, TestDataSet_col = 0, TestCaseName_col = 0;
		XSSFWorkbook Xlfile = new XSSFWorkbook("Executor.xlsx");

		XSSFSheet sheet = Xlfile.getSheet("Sheet1");

		Row row = sheet.getRow(0);
		int Col_cnt = row.getPhysicalNumberOfCells();

		for (int a = 0; a < Col_cnt; a++) {
			Cell cell = row.getCell(a);
			// getting the execution cell name and column number
			switch (getcellvalue(cell)) {
			case "Execution":
				Execution_col = a;
				break;
			case "Browser":
				Browser_col = a;
				break;
			case "TestDataSet":
				TestDataSet_col = a;
				break;
			case "TestCaseName":
				TestCaseName_col = a;

			}
		}

		// creating the array list for execution suite
		for (int a = 1; a < sheet.getPhysicalNumberOfRows(); a++) {
			row = sheet.getRow(a);
			Cell cell = row.getCell(Execution_col);
			if (getcellvalue(cell).equalsIgnoreCase("y")) {
				String browser = getcellvalue(row.getCell(Browser_col));
				String TestData = getcellvalue(row.getCell(TestDataSet_col));
				String TestCaseName = getcellvalue(row.getCell(TestCaseName_col));
				list.add(TestCaseName + ";" + TestData + ";" + browser);
			}
		}
		Xlfile.close();
		return list;
	}

	public static String getcellvalue(Cell cell) {
		String cellval = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			cellval = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			cellval = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			cellval = String.valueOf(cell.getNumericCellValue());
			break;

		}
		return cellval;
	}

	/**
	 * 
	 * @param FileName
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static Map<String, String> GetORMap(String FileName)
			throws FileNotFoundException, IOException, ParseException {

		JSONParser parser = new JSONParser();

		Object obj = parser.parse(new FileReader(FileName));

		try {
			JSONObject jobj = (JSONObject) obj;
			@SuppressWarnings("unchecked")
			Iterator<String> NodeItr = jobj.keySet().iterator();
			while (NodeItr.hasNext()) {
				JSONObject cobj = (JSONObject) jobj.get(NodeItr.next());

				@SuppressWarnings("unchecked")
				Iterator<String> keysItr = cobj.keySet().iterator();
				while (keysItr.hasNext()) {
					String key = keysItr.next();
					Object value = cobj.get(key);
					OR.put(key, value.toString());
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
		return OR;
	}

	/**
	 * this method returns the overall test-data for the entire suite to be executed
	 * 
	 * @param Filepath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> ParseTestData(String Filepath)
			throws FileNotFoundException, IOException, ParseException {

		JSONParser parser = new JSONParser();

		Object obj = parser.parse(new FileReader(Filepath));
		try {
			JSONArray jobj = (JSONArray) obj;
			Iterator<JSONObject> keysItr = jobj.iterator();
			while (keysItr.hasNext()) {
				JSONObject cobj = (JSONObject) keysItr.next();
				Iterator<String> g = cobj.keySet().iterator();
				while (g.hasNext()) {
					String key = g.next();
					Object value = cobj.get(key);
					testdata.put(key, value);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return testdata;
	}

	public static void SetEnvdetails(String testdata, String browser) {
		Browser.set(null);
		TestDataRow.set(null);
		try {
			Browser.set(browser);
			TestDataRow.set(testdata);
		} catch (Exception er) {
			System.out.println(er.getMessage());
		}
	}

	public String GetEnvdetails() {
		return Browser + ";" + TestDataRow;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> Readtestdata(String TestCase) {

		String strvalue = null;
		String env[] = TestCase.split(";");
		SetEnvdetails(env[1].trim(), env[2].trim());
		try {
			datamap.clear();
			System.out.println(TestDataRow.get().toString());
			Object data1 = testdata.get(env[0].trim());
			JSONArray mobj = (JSONArray) data1;
			Iterator<JSONObject> mItr = mobj.iterator();
			while (mItr.hasNext()) {
				JSONObject kobj = (JSONObject) mItr.next();
				Iterator<String> h = kobj.keySet().iterator();
				while (h.hasNext()) {
					String key = h.next();
					if (key.equalsIgnoreCase(TestDataRow.get().toString())) {
						Object value = kobj.get(key);
						JSONArray tobj = (JSONArray) value;
						Iterator<JSONObject> tdItr = tobj.iterator();
						while (tdItr.hasNext()) {
							JSONObject sobj = (JSONObject) tdItr.next();
							Iterator<String> ditr = sobj.keySet().iterator();
							while (ditr.hasNext()) {
								String tdkey = ditr.next();
								Object tdvalue = sobj.get(tdkey);
								strvalue = tdvalue.toString();
								datamap.put(tdkey, strvalue);
							}
						}
					}
				}
			}
		} catch (Exception er) {
			System.out.println(er.getMessage());
		}
		return datamap;
	}

	/** Placing the OR file data */
	public static Map<String, String> GetOR() {
		Map<String, String> ORdata = null;
		try {
			ORdata = OR;
		} catch (Exception er) {
			System.out.println(er.getMessage());
		}
		return ORdata;
	}

	/** Placing the Properties file data */
	public static Map<String, String> GetPropertyData() {
		Map<String, String> gdata = null;
		try {
			gdata = Propmap;
		} catch (Exception er) {
			System.out.println(er.getMessage());
		}
		return gdata;
	}

	/** Read Properties file */
	public static Map<String, String> ReadPropertyfile() {
		try {
			Properties PropFile = new Properties();
			PropFile.load(new FileInputStream("Config.properties"));
			Iterator<Object> Keys = PropFile.keySet().iterator();
			while (Keys.hasNext()) {
				String strkey = Keys.next().toString();
				String value = PropFile.getProperty(strkey);
				Propmap.put(strkey, value);
			}
		} catch (Exception er) {
			System.out.println(er.getMessage());
		}
		return Propmap;
	}

	/**
	 * Getting the browser based on the parameter
	 * 
	 * @param browsername
	 * @return
	 */
	public static WebDriver getbrowser(String browser) {
		try {
			switch (browser.toLowerCase()) {
			case "firefox":
				WebDriver fdriver = new FirefoxDriver();
				setWebDriver(fdriver);
				break;
			case "safari":
				WebDriver sdriver = new SafariDriver();
				setWebDriver(sdriver);
				break;
			default:
				FirefoxDriver dfdriver = new FirefoxDriver();
				setWebDriver(dfdriver);
				break;
			}
		} catch (Exception err) {
			System.out.println(err.getMessage());
			throw new SkipException("Skipping test case");
		}
		return getDriver();
	}

	public static AndroidDriver<MobileElement> getmobbrowser() {
		try {
			setmobDriver(android());
		} catch (Exception err) {
			System.out.println(err.getMessage());
		}
		return getmobDriver();
	}

	public static void setWebDriver(WebDriver driver) {
		threadedDriver.set(driver);
	}

	public static void setmobDriver(AndroidDriver<MobileElement> driver) {
		mobDriver.set(driver);
	}

	public static AndroidDriver<MobileElement> getmobDriver() {
		return mobDriver.get();
	}

	public static WebDriver getDriver() {
		return threadedDriver.get();
	}

	public static AndroidDriver<MobileElement> android() throws MalformedURLException {
		// Create object of DesiredCapabilities class and specify android platform
		DesiredCapabilities capabilities = DesiredCapabilities.android();
		

		// set the capability to execute test in chrome browser
		 if(Browser.get().equalsIgnoreCase("androidapp")) {
			capabilities.setCapability("appPackage",Propmap.get("appPackage"));
			capabilities.setCapability("appActivity",Propmap.get("appActivity"));
		 }else {
			 capabilities.setCapability("browserName", Propmap.get("browserName")); 
		 }
			
			
		// set the capability to execute our test in Android Platform
		capabilities.setCapability("platform",Propmap.get("platform"));

		// we need to define platform name
		capabilities.setCapability("platformName",Propmap.get("platformName"));

		// Set the device name as well (you can give any name)
		capabilities.setCapability("deviceName",Propmap.get("deviceName"));

		// set the android version as well
		capabilities.setCapability("version",Propmap.get("version"));
		
		URL url= null;
		// Create object of URL class and specify the appium server address
		 if(Browser.get().equalsIgnoreCase("androidapp")) {
		  url = new URL("http://127.0.0.1:4723/wd/hub");
		 }else {
			  url = new URL("http://127.0.0.1:5723/wd/hub");
		 }
		// Create object of AndroidDriver class and pass the url and capability that we
		// created
		AndroidDriver<MobileElement> mdriver = new AndroidDriver<>(url, capabilities);

		return mdriver;
	}

	// @SuppressWarnings("unchecked")
	 

	public synchronized String takesnapshot() {
		String snapshotpath = "ScreenShots//" + System.currentTimeMillis() + ".png";
		File scrFile = null;
		try {
			if (getmobDriver() != null) {
				scrFile = ((TakesScreenshot) getmobDriver()).getScreenshotAs(OutputType.FILE);
			} else {
				scrFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
			}
			// The below method will save the screen shot in d drive with name
			// "screenshot.png"
			FileUtils.moveFile(scrFile, new File(snapshotpath));
		} catch (Exception er) {
			System.out.println(er.getMessage());
		}
		return snapshotpath;
	}


}