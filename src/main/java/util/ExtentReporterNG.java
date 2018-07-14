package util;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExtentReporterNG implements IReporter {
    private ExtentReports extent;
    String relativelyPath = System.getProperty("user.dir");
    String testDataPath = System.getProperty("user.dir") + "/reports";
    private static final Logger logger = LoggerFactory.getLogger(ExtentReporterNG.class);

    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        extent = new ExtentReports(testDataPath + File.separator + suites.get(0).getName() + Tool.strTime("_yyyy-MM-dd_HH_mm_ss", 0) + "_report.html", true);
        for (ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();

            for (ISuiteResult r : result.values()) {
                ITestContext context = r.getTestContext();
                buildTestNodes(context.getPassedTests(), LogStatus.PASS);
                buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
                buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
            }
        }
        extent.flush();
        extent.close();
    }

    private void buildTestNodes(IResultMap tests, LogStatus status)  {
        ExtentTest test;

        if (tests.size() > 0) {
            for (ITestResult result : tests.getAllResults()) {
                Object[] params = result.getParameters();
                ITestNGMethod method = result.getMethod();
                test = extent.startTest(getTestDescripe(result));
                test.assignCategory(result.getMethod().getTestClass().getName());
                test.assignAuthor("zhaowei");
                test.setStartedTime(getTime(result.getStartMillis()));
                test.setEndedTime(getTime(result.getEndMillis()));
                String clsname = result.getClass().getName();
                String paramStr = "参数列表:[";
                for (Object param : params) {
                    if (param instanceof String[]){
                        paramStr += Tool.ArrToStrng((String[])param);
                    }else if (param instanceof Integer []){
                        paramStr += Tool.ArrToStrng((Integer[])param);
                    }else {
                        paramStr += param + ",";
                    }
                }
                paramStr += "]";
                test.log(status, paramStr);
                for (String group : result.getMethod().getGroups())
                    test.assignCategory(group);

                if (result.getThrowable() != null) {
                    test.log(status, result.getThrowable());
                    test.log(status,result.getTestClass().getRealClass().toString());
                    test.log(status,result.getMethod().getFailedInvocationNumbers().toString());
                } else {
                    if (status.toString().toLowerCase().equals("skip")){
                        test.log(status,result.getMethod().getInvocationNumbers().toString());
//                        example.log(status,result.getMethod().get);
                    }
                    test.log(status, "Test " + status.toString().toLowerCase() + "ed");
                }
                extent.endTest(test);
            }
        }
    }

    private String getTestDescripe(ITestResult result) {
        String desc = result.getMethod().getRealClass().getSimpleName()+"."+ result.getMethod().getMethodName()+ "(" + result.getMethod().getDescription();
        Object[] params = result.getParameters();
        if (params.length > 0) {
            desc += ","+params[1].toString() + ")";
        } else {
            desc += ")";
        }
        return desc;
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
}