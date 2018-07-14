package example.SDK.core;

import example.ExampleHttp;
import org.json.JSONObject;

/**
 * Created by zhaowei on 2017/1/11.
 */
public class ExampleFunctionBase extends ExampleSDKBase {
    public ExampleFunctionBase(){
        exampleHttp = new ExampleHttp(env);
        setCookie("sessionId=XXXX");
    }
    public JSONObject getParentInfo() {
        return get(EXAMPLE_URI,getData,null,bodyData);
    }
}
