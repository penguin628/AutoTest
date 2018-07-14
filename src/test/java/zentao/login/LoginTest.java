package zentao.login;

import example.t1.TestCase;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import util.HttpMethod;
import zentao.AssertConstant;
import zentao.ZenTaoTest;

import static util.HttpMethod.POST;

public class LoginTest extends ZenTaoTest implements AssertConstant {
    public String uri = "/zentaopms/www/user-login.html";
    public HttpMethod method = HttpMethod.POST;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LoginTest.class);

    @BeforeClass
    public void beforeClass() {

        zentaoHttp.setHttpInfo(uri, POST);

    }

    @BeforeMethod
    public void beforeMethod() {

    }

    @AfterMethod
    public void afterMethod() {

    }
//    @Test(description = "Ê¾Àý",groups = {"smoke","function"}1 )
//    public void testNormal() throws JSONException {
//        String[] uriParameters = {};
//        //     postDataMap.put("Id", post_Id);
//        JSONObject result = zentaoHttp.restfulRequest(getDataMap,uriParameters,postDataMap);
//        String data = result.getString("data");
//        System.out.println(data);
//    }
    @Test(description = "ìøµÀµÇÂ¼²âÊÔ",dataProvider = "testData",groups = {"smoke","function"})
    public void normal(String id,String desc,String post_account,String post_password,String post_referer) throws JSONException {
        String[] uriParameters = {};
        zentaoHttp.isStr=true;
        setData(post_account, post_password,post_referer);
        logger.info(getDataMap.toString());
        logger.info(postDataMap.toString());
        JSONObject result = zentaoHttp.restfulRequest(getDataMap,null,postDataMap);
        System.out.println("result:"+result);
        int httpcode=result.getInt("httpcode");
        String data = result.getString("data");
        System.out.println(data);
        Assert.assertEquals(httpcode,HTTP_OK);
        Assert.assertTrue(data.contains(post_referer));
    }
}
