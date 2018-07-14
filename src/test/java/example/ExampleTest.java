package example;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import util.TestBase;

import java.util.ArrayList;

/**
 * Created by zhaowei2 on 2016/12/29.
 */
public class ExampleTest extends TestBase{
    public ExampleHttp exampleHttp;

    private static Logger logger = LoggerFactory.getLogger(ExampleTest.class);


    @BeforeSuite(groups = {"smoke", "function"})
    public void beforeSuite() throws Exception {
        env = "qa";
        exampleHttp = new ExampleHttp(env);
    }

    @AfterSuite(groups = {"smock", "function"})
    public void afterSuite() throws JSONException {
    }

}
