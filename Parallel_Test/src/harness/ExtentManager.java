package harness;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
    
    private static ExtentReports Report;
    
    public static ExtentReports getInstance() {
    	if (Report == null)
    		createInstance("Report");
    	
        return Report;
    }
    
    public static ExtentReports createInstance(String fileName) {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName+".html");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle("Automation Report");
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName("Regression Suite");
//        htmlReporter.loadXMLConfig("//Users//NIMIT//Documents//workspace//extentreports-java-3.0.7//extent-config.xml");
        Report = new ExtentReports();
        Report.attachReporter(htmlReporter);
        
        return Report;
    }
} 