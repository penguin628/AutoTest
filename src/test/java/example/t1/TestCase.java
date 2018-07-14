package example.t1;

import example.ExampleTest;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import util.HttpMethod;


/**
 * Created by zhaowei2 on 2016/12/29.
 */
public class TestCase extends ExampleTest{
    public String uri = "";
    public HttpMethod method = HttpMethod.GET;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TestCase.class);

    @BeforeClass
    public void beforeClass() {
        exampleHttp.setHttpInfo(uri, method);
    }

    @BeforeMethod
    public void beforeMethod() {

    }

    @AfterMethod
    public void afterMethod() {

    }
    @Test(description = "示例",groups = {"smoke","function"})
    public void testNormal() throws JSONException {
        String[] uriParameters = {};
        //     postDataMap.put("Id", post_Id);
        JSONObject result = exampleHttp.restfulRequest(getDataMap,uriParameters,postDataMap);
        String data = result.getString("data");
        System.out.println(data);
    }
    @Test(description = "示例",dataProvider = "testData",groups = {"smoke","function"})
    public void normal(String id,String desc,String get_name,int post_age) throws JSONException {
        String[] uriParameters = {};
        setData(get_name,post_age);
        logger.info(getDataMap.toString());
        logger.info(postDataMap.toString());
        JSONObject result = exampleHttp.restfulRequest(getDataMap,uriParameters,postDataMap);
        String data = result.getString("data");
        System.out.println(data);
    }
}
