package example;

import util.BaseHttp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpMethod;
import util.Tool;

import java.util.Map;

/**
 * Created by zhaowei2 on 2016/12/29.
 */
public class ExampleHttp extends BaseHttp {
    private static final Logger logger = LoggerFactory.getLogger(ExampleHttp.class);
    public String example_url = "";
    public ExampleHttp() {
    }

    public ExampleHttp(String env) {
        BaseHttp.env = env;
        this.headMap.put("Content-Type","application/json;charset=utf-8");
        loadCommConf();
    }

    public void loadCommConf() {
        ps = Tool.getProperties("conf/Example.properties");
        this.example_url = ps.getProperty(env + "_example_url");
    }


    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void setURI(String uri) {
        this.url = example_url+uri;
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
