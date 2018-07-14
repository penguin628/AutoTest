package zentao;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import util.TestBase;

public class ZenTaoTest extends TestBase{
    public ZenTaoHttp zentaoHttp;

    private static Logger logger = LoggerFactory.getLogger(ZenTaoTest.class);


    @BeforeSuite(groups = {"smoke", "function"})
    public void beforeSuite() throws Exception {
        env = "qa";
        zentaoHttp = new ZenTaoHttp(env);
    }

    @AfterSuite(groups = {"smock", "function"})
    public void afterSuite() throws JSONException {
    }
}
