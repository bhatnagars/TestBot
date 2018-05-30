package harness;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.poi.ss.format.CellFormat;
import org.apache.poi.ss.format.CellFormatter;
import org.apache.poi.ss.format.CellTextFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class ExtentTestNGReportBuilder  {

	private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal<ExtentTest>();
    private static ThreadLocal<String> test= new ThreadLocal<String>();
    private static Harness harness;
    private static ArrayList<String> failedtest= new ArrayList<String>() ;
    private static ThreadLocal<Boolean> blnflg = new ThreadLocal<>();
    

	@BeforeSuite
	public void beforeSuite() {
		extent = ExtentManager.createInstance("Report");
//		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("Report");
//		extent.attachReporter(htmlReporter);
		harness = new Harness();
	}
	
	public void CleanReport(){
		File file = new File("Report.html");
		if(file.exists()){
			file.delete();
		}
	}
	

	
    //@BeforeClass
    public synchronized void createTest(String param,String Description) {
    	    String value[] = param.split(";");
    	    String testname = value[1].trim()+"-"+value[2].trim();
        ExtentTest parent = extent.createTest(testname,Description);
        parentTest.set(parent);  
        test.set(param);
    }
   
    
    public synchronized void skipclass(String Classname) {
    	    String classname[] = Classname.split(";");
    	    String testname = classname[0].trim()+"-["+classname[1].trim()+"-"+classname[2].trim()+"]";
        ExtentTest parent = extent.createTest(testname);
        parentTest.set(parent);
        parentTest.get().skip("Either TestData , OR Data or Config file is not present or not data present");
        extent.flush();
        if(failedtest.isEmpty()) {
        	String Header = "TestCase;TestData;Browser;Status";
        	failedtest.add(Header);
        }
        failedtest.add(Classname+";Skip");
        //throw new SkipException("Skipping this exception");
    } 

//    @BeforeMethod
//    public synchronized void beforeMethod(Method method) {
//        ExtentTest child = ((ExtentTest) parentTest.get()).createNode(method.getName());
//        test.set(child);
//    }
    
    public void info(String p) {
        parentTest.get().info(p);
        }
    
    public synchronized void pass(String p) {
    parentTest.get().pass(p);
    }
    
    public synchronized void fail(String p) {
    	try {
    String scrnshot=	harness.takesnapshot();
    	parentTest.get().fail(p,MediaEntityBuilder.createScreenCaptureFromPath(scrnshot).build());
    	Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
    	blnflg.set(false);
    	}catch(Exception er) {
    		System.out.println(er.getMessage());
    	}
    }

    
    @AfterMethod
    public synchronized void createResultFile(ITestResult result) {
        extent.flush();
        if(failedtest.isEmpty()) {
        	String Header = "TestCase;TestData;Browser;Status";
        	failedtest.add(Header);
        }
        System.out.println(blnflg.get().equals(false));
        if(blnflg.get().equals(false)) {
        failedtest.add(test.get()+";Fail");
        }
    }
    
    @AfterSuite(alwaysRun=true)
    public synchronized void GenFailedExecutor() { 
    	    		int rowNum = 0;
    	    	  try { 
    	    		  XSSFWorkbook workbook = new XSSFWorkbook();
    	    	      XSSFSheet sheet = workbook.createSheet("FailedTest");
    	    	 for(int x=0;x<failedtest.size();x++) {
    	    		 Row row = sheet.createRow(rowNum++);
    	    		 for(int y=0;y<failedtest.get(x).split(";").length;y++) {
    	    			 Cell cell = row.createCell(y);
    	              cell.setCellValue((String) failedtest.get(x).split(";")[y]);
    	    		 }
    	    	 }
    	    	      
    	    	 FileOutputStream outputStream = new FileOutputStream("FailedTest.xlsx");
    	    	 workbook.write(outputStream); 
    	    	 workbook.close();
    	    	 System.out.println("File with failed test cases is generated with success");
 	
    	    	  } catch (Exception err) { 
    	    		  System.out.println(err.getMessage()); 
    	    		  }
    	    	 }
    
}