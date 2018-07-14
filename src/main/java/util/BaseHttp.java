package util;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BaseHttp {
    private static final Logger logger = LoggerFactory.getLogger(BaseHttp.class);
    public static String env = null;
    public String url = "";
    public Map<String, Object> data = new HashMap<>();
    public boolean isJson = false;
    public boolean isStr = false;
    public boolean isPost;
    public Map<String, String> headMap = new HashMap<String, String>();
    public static Map<String, String> hostMap = new HashMap<>();
    public HttpMethod method;
    public Properties ps;

    public void setData(Map<String, Object> data) {
        if (data != null) {
            this.data = data;
        }
    }

    public void setPost(boolean isPost) {
        this.isPost = isPost;
    }

    public void loadHost(String env) {
        try {
            String encoding = "UTF-8";
            File file = new File("host/host-" + env);
            if (file.isFile() && file.exists()) { //�ж��ļ��Ƿ����
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//���ǵ������ʽ
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    String[] tmp = lineTxt.split("=");
                    if (lineTxt.startsWith("#") || lineTxt.startsWith("//") || tmp.length < 2) {
                        continue;
                    }
                    BaseHttp.hostMap.put(tmp[1], tmp[0]);
                }
                read.close();
            } else {
                logger.error("�Ҳ���host�ļ�:" + "host/host-" + env);
            }
        } catch (Exception e) {
            logger.error("��ȡhost�ļ����ݳ���,:" + "host/host-" + env);
            e.printStackTrace();
        }
    }

    /**
     * ����url,�滻host
     */
    public String replaceUrl(String realUrl) {
        try {
            URL url = new URL(realUrl);
            String host = url.getHost();
            if (HttpRequest.debug) {
                logger.info("host:" + hostMap.toString());
            }
            if (hostMap.containsKey(host)) {
                realUrl = realUrl.replace(host, hostMap.get(host));
                headMap.put("Host", host);
            }
        } catch (MalformedURLException e) {
            logger.info("url��������," + realUrl);
            e.printStackTrace();
        }
        return realUrl;
    }


    public JSONObject doRequest(Map<String, Object> data) {

        return this.doRequest(data, (String) null);
    }

    public JSONObject doRequest(Map<String, Object> data, String realUrl) {
        this.getDebug();
        if (realUrl == null) {
            realUrl = this.url;
        }
        realUrl = this.replaceUrl(realUrl);
        JSONObject result = null;
        this.setData(data);
        try {
            this.addTimeStamp();
            this.addSign();
        } catch (JSONException e) {
            logger.error(e.toString());
        }
        int times = 3;
        while (times > 0) {
            if (times != 3) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                result = sendRequest(realUrl);
                if (result.getInt("httpcode") != 408) {
                    break;
                } else {
                    times = times - 1;
                    continue;
                }
            } catch (Exception e) {
                if (HttpRequest.debug) {
                    e.printStackTrace();
                }
                times = times - 1;
                continue;
            }
        }
        return result;
    }

    public JSONObject sendRequest(String realUrl) throws Exception {
        JSONObject result = null;
        if (this.isPost) {
            if (this.isJson) {
                this.headMap.put("Content-Type",
                        "application/json;charset=utf-8");
                result = HttpRequest.postJson(realUrl, this.data, this.headMap);
            } else {
                this.headMap.put("Content-Type",
                        "application/x-www-form-urlencoded;charset=utf-8");
                result = HttpRequest.post_str(realUrl, this.data, this.headMap);
            }
        } else {
            result = HttpRequest.get(realUrl, this.data, this.headMap);
        }
        return result;
    }


    public void getDebug() {
        if (Boolean.parseBoolean(ps.getProperty("debug"))) {
            HttpRequest.debug = true;
        }
    }


    public void addTimeStamp() throws JSONException {
        long timeStamps = new Date().getTime();
        this.data.put("timestamp", String.valueOf(timeStamps));
        return;
    }

    public void addSign() {
        return;
    }

    /**
     * �����в�������url��body�ж��в���
     * �����URI���пɱ����
     * ��Ӧ��GET��POST��PUT��DELETE����ʹ��
     *
     * @return
     */
    public JSONObject restfulRequest(Map<String, Object> getMap, String[] uriParams, Map<String, Object> bodyMap, String realUrl) {
        getDebug();
        if (realUrl == null) {
            realUrl = this.url;
        }
//        realUrl = replaceUrl(realUrl);
        JSONObject result = null;
        this.setData(bodyMap);
        if (uriParams != null) {
            for (int i = 0; i < uriParams.length; i++) {
                switch (uriParams.length) {
                    case 1:
                        realUrl = String.format(realUrl, uriParams[0]);
                        break;
                    case 2:
                        realUrl = String.format(realUrl, uriParams[0], uriParams[1]);
                        break;
                    case 3:
                        realUrl = String.format(realUrl, uriParams[0], uriParams[1], uriParams[2]);
                        break;
                }
            }
        }
        if (getMap != null && !getMap.isEmpty()) {
            realUrl += "?" + MapUtils.MapToHttpString(getMap);
        }
        if (!isStr) {
            this.headMap.put("Content-Type",
                    "application/json;charset=utf-8");
        } else {
            this.headMap.put("Content-Type",
                    "application/x-www-form-urlencoded;charset=utf-8");
        }
        try {
            switch (this.method) {
                case GET:
                    result = HttpRequest.get(realUrl, this.headMap);
                    break;
                case POST:
                    if (!isStr) {
                        result = HttpRequest.postJson(realUrl, this.data, this.headMap);
                    } else {
                        result = HttpRequest.post_str(realUrl, this.data, this.headMap);
                        isStr = false;
                    }
                    break;
                case PUT:
                    result = HttpRequest.put(realUrl, this.data, this.headMap);
                    break;
                case DELETE:
                    result = HttpRequest.delete(realUrl, this.data, this.headMap);
                    break;
                case DELETE_POST:
                    result = HttpRequest.deletePost(realUrl, this.data, this.headMap);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * �����в�������url��body�ж��в���
     * �����URI���пɱ����
     * ��Ӧ��GET��POST��PUT��DELETE����ʹ��
     *
     * @return
     */
    public JSONObject restfulRequest(Map<String, Object> getMap, String[] uriParams, Map<String, Object> bodyMap) {
        return restfulRequest(getMap, uriParams, bodyMap, null);
    }

    /**
     * ���������κβ���
     * �����URI���пɱ����
     * ��Ӧ��GET��DELETE����ʹ��
     *
     * @return
     */
    public JSONObject restfulRequest() {
        return restfulRequest(null, null, null);
    }


    /**
     * �����в�������body��,url���޲���
     * ��Ӧ��POST��PUT����ʹ��
     *
     * @param bodyMap
     * @return
     */
    public JSONObject restfulPostRequest(Map<String, Object> bodyMap) {
        return restfulRequest(null, null, bodyMap);
    }

    /**
     * �����в�������body��,url���޲���
     * URI���пɱ����
     * ��Ӧ��POST��PUT����ʹ��
     *
     * @param bodyMap
     * @return
     */
    public JSONObject restfulPostRequest(Map<String, Object> bodyMap, String[] uriParams) {
        return restfulRequest(null, uriParams, bodyMap);
    }

    /**
     * �����в�������body��,url���޲���
     * URI���пɱ����
     * ��Ӧ��POST��PUT����ʹ��
     *
     * @param uriParams
     * @return
     */
    public JSONObject restfulGetRequest(String[] uriParams) {
        return restfulRequest(null, uriParams, null);
    }

    /**
     * �����в�������url��,body���޲���,
     * �����URI�а����ɱ����,��Ҫ��ʽ��
     * ��Ӧ��get��delete����ʹ��
     *
     * @param uriParams
     * @return
     */
    public JSONObject restfulRequest(Map<String, Object> getMap, String[] uriParams) {
        return restfulRequest(getMap, uriParams, null);
    }

    /**
     * �����в�������url��,body���޲���,
     * �����URI���޿ɱ����
     * ��Ӧ��get��delete����ʹ��
     *
     * @return
     */
    public JSONObject restfulRequest(Map<String, Object> getMap) {
        return restfulRequest(getMap, null, null);
    }

    /**
     * �����в�������url��body�ж��в���
     * �����URI���޿ɱ����
     * ��Ӧ��POST��PUT��DELETE����ʹ��
     *
     * @return
     */
    public JSONObject restfulRequest(Map<String, Object> getMap, Map<String, Object> bodyMap) {
        return restfulRequest(getMap, null, bodyMap);
    }


}
