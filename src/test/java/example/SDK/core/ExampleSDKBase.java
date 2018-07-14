package example.SDK.core;

import example.ExampleHttp;
import example.SDK.implement.ExampleUriConstant;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpMethod;
import util.TestBase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaowei on 2017/1/11.
 */
public class ExampleSDKBase implements ExampleUriConstant {
    static {
        PropertyConfigurator.configure("log4j.properties");
    }

    protected static final Logger logger = LoggerFactory.getLogger(ExampleSDKBase.class);
    protected String callClassName = "";
    protected ExampleHttp exampleHttp;
    protected static Map<String, Object> getData = new HashMap<String, Object>();
    protected static Map<String, Object> bodyData = new HashMap<String, Object>();
    protected String env;
    public String cookie;

    public ExampleSDKBase() {

        //获取实例化此类的时的类名称
        StackTraceElement[] temp = Thread.currentThread().getStackTrace();
        StackTraceElement a = temp[3];
        if (a.getClassName().length() > 0) {
            String[] tmp = a.getClassName().split("\\.");
            callClassName = tmp[tmp.length - 1] + a.getLineNumber();
        }
        env = TestBase.env;
    }
    protected void setHead() {
        if (cookie != null) {
            exampleHttp.headMap.put("cookie", cookie);
        }
        logger.info("添加cookie:" + cookie);
    }

    public String getACookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    protected JSONObject request(HttpMethod method, String url, Map<String, Object> getData, String[] urlParameters, Map<String, Object> bodyData) {
        setHead();
        exampleHttp.setHttpInfo(url, method);
        JSONObject js = exampleHttp.restfulRequest(getData, urlParameters, bodyData);
        if (bodyData != null) {
            bodyData.clear();
        }
        if (getData != null) {
            getData.clear();
        }
        return js;
    }

    protected JSONObject put(String url,Map<String, Object> getData, String[] urlParameters, Map<String, Object> bodyData) {
        return request(HttpMethod.PUT, url, getData,urlParameters, bodyData);
    }

    protected JSONObject delete(String url,Map<String, Object> getData, String[] urlParameters, Map<String, Object> bodyData) {
        return request(HttpMethod.DELETE, url,getData, urlParameters, bodyData);
    }

    protected JSONObject post(String url, Map<String, Object> getData,String[] urlParameters, Map<String, Object> bodyData) {
        return request(HttpMethod.POST, url, getData,urlParameters, bodyData);
    }

    protected JSONObject get(String url, Map<String, Object> getData,String[] urlParameters, Map<String, Object> bodyData) {
        return request(HttpMethod.GET, url,getData, urlParameters, bodyData);
    }
}
