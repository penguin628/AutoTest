package util;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

public class MapUtils {

    public static String MapToHttpString(Map<String, Object> params) {
        String dataString = "";
        if (params ==null || params.isEmpty()) {
            return dataString;
        }
        try {
            for (String key : params.keySet()) {
                dataString += key + "=" + URLEncoder.encode(String.valueOf(params.get(key)), "utf-8") + "&";
            }
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return dataString.substring(0, dataString.length() - 1);
    }

    public static String mapToJson(Map<String, Object> dataMap) {
        Set keySet = dataMap.keySet();
        JSONObject js = new JSONObject(dataMap);
        return js.toString();
    }
    public static JSONObject mapToJsonOb(Map<String, Object> dataMap) {
        Set keySet = dataMap.keySet();
        JSONObject js = new JSONObject(dataMap);
        return js;
    }
}
