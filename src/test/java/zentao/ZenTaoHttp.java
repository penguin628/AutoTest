package zentao;

import example.ExampleHttp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.BaseHttp;
import util.HttpMethod;
import util.Tool;

import java.util.Map;

public class ZenTaoHttp extends BaseHttp {
    private static final Logger logger = LoggerFactory.getLogger(ZenTaoHttp.class);
    public String LOGIN_URI = "";
    public ZenTaoHttp() {
    }

    public ZenTaoHttp(String env) {
        BaseHttp.env = env;
        this.headMap.put("Content-Type","application/json;charset=utf-8");
        loadCommConf();
    }

    public void loadCommConf() {
        //JAVA∂¡≈‰÷√Œƒº˛
        ps = Tool.getProperties("conf/zentao.properties");
        this.LOGIN_URI = ps.getProperty(env + "_zentao_url");
    }


    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void setURI(String uri) {
        this.url = LOGIN_URI+uri;
    }

    public void setData(Map<String, Object> data) {
        if (data != null) {
            this.data = data;
        }
    }

    public void setHttpInfo(String uri, HttpMethod method) {
        setURI(uri);
        setMethod(method);
    }
}
