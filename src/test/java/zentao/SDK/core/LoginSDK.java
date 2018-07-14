package zentao.SDK.core;

import example.ExampleHttp;
import org.json.JSONException;
import org.json.JSONObject;
import zentao.Accounts;
import zentao.ZenTaoHttp;

public class LoginSDK extends ZenTaoSDKBase {
    public LoginSDK(){
        zentaoHttp = new ZenTaoHttp(env);
        //setCookie("sessionId=zentaosid");
    }
    public String getZenTaoId(String account,String password) throws JSONException{
        zentaoHttp.isStr=true;
        bodyData.put("account",account);
        bodyData.put("password",password);
        bodyData.put("refer","http://116.196.88.42/zentaopms/www/my");
        return post(LOGIN_URI,getData,null,bodyData).
                getJSONObject("cookie").getString("zentaosid");
    }
    public String getZenTaoId() throws JSONException{
       return getZenTaoId(Accounts.QA_ACCOUNT,Accounts.QA_PASSWORD);
    }

    public static void main(String[] args) throws JSONException {
        System.out.println(new LoginSDK().getZenTaoId());
    }

}
