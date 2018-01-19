package harness;

import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlSuite.ParallelMode;
import org.testng.xml.XmlTest;

@SuppressWarnings("serial")
public class Executor extends ExtentTestNGReportBuilder {
 
	private static ArrayList<String> list;	
	private static Map<String,String> Propmap;
	private static String testname;
	protected Harness harness;
	

  public void ExecuteSuite() {
try{
	harness = new Harness();
	//Reading the executor file for all the artifacts
	list = Harness.ReadExecutorfile();
	if(list.isEmpty())
		throw new Exception();
	harness.ReadTestConfig();
	Propmap=Harness.ReadPropertyfile(); //reading the property file
	CleanReport();
	 
	 
	 //Getting the testcases from list now
	for(int i=0;i<list.size();i++){
	  testname =  testname+";"+list.get(i).split(";")[0];
	 }	 
     String tc[] =testname.split(";");

    
	XmlSuite suite = new XmlSuite();
	suite.setName("Regression Suite");
	if(Propmap.get("Execution").equalsIgnoreCase("parallel")) {
	suite.setParallel(ParallelMode.TESTS);
	suite.setThreadCount(2);}
	for(int i=1;i<tc.length;i++){  
		XmlTest test = new XmlTest(suite);
		Map<String, String> param = new HashMap<>();
		param.put("param",list.get(i-1));
		test.setParameters(param);
		test.setName(tc[i]+i);
		List<XmlClass> classes = new ArrayList<XmlClass>();
		classes.add(new XmlClass("test."+tc[i]));
		test.setXmlClasses(classes);	
	}
	List<XmlSuite> suites = new ArrayList<XmlSuite>();	
	suites.add(suite);
	TestNG tng = new TestNG();
	tng.setXmlSuites(suites);
	tng.run();
  }catch(Exception ee){
	 System.out.println(ee);
  }
 }
  
}
