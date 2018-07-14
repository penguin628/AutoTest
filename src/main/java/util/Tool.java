package util;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;


public class Tool {
    private static final Logger logger = LoggerFactory.getLogger(Tool.class);

    public static JSONObject String2Json(String jsonString) throws JSONException {
        JSONObject js = null;
        try {
            js = new JSONObject(jsonString);
        } catch (JSONException e) {
            logger.error(e.getMessage());
        }
        if (js == null) {
            js = new JSONObject();
            js.put("orginString", jsonString);
        }
        return js;
    }

    public static String base64(String md5Str,boolean isUpper){
        logger.info(md5Str);
        byte[] b = null;
        String s = null;
        try {
            if(isUpper)
            {
                b = md5Str.toUpperCase().getBytes();
            }else {
                b= md5Str.toLowerCase().getBytes();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    public static String trimStr(String str,String del){
        String tmp = str.toUpperCase();
        int len = str.length();
        int del_len = del.length();
        if(len<del_len){
            return str;
        }else {
            if(tmp.startsWith(del.toUpperCase()))
            {
                return str.substring(del_len);
            }else if(tmp.endsWith(del.toUpperCase())){
                return str.substring(0,len-del_len);
            }
        }
        return  str;
    }
    public static String md5(String s){
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 把properties路径转为properties对象
     * @param path
     * @return
     */
    public static Properties getProperties(String path){
        Properties ps = new Properties();
        try {
            ps.load(new FileInputStream(path));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return ps;
    }
    public static  List<BasicNameValuePair> getBasicNameValuePair(Map<String,Object>dataMap){
        List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
        for (String key:dataMap.keySet()){
            BasicNameValuePair nv = new BasicNameValuePair(key,String.valueOf(dataMap.get(key)));
            nvps.add(nv);
        }
        return nvps;
    }

    public static String strTime(String format,long time){
        SimpleDateFormat strFormat = new SimpleDateFormat(format);
        if(time == 0){
            time = new Date().getTime();
        }
        return strFormat.format(time);
    }
    public static String[] jsonArrToStrngArr(JSONArray srcArr) throws JSONException{
        String[] retArr = new String[srcArr.length()];
        for (int i=0;i<srcArr.length();i++) {
            retArr[i] = srcArr.get(i).toString();
        }
        return retArr;
    }
    public static <T>String ArrToStrng(T[] arr){
        String str = "";
        for (int i=0;i<arr.length;i++) {
            str += arr[i]+",";
        }
        return str;
    }

    public static long getMinuteTimestamp(long time){
        String MinuteTimestamp = "yyyy/MM/dd HH:mm";
        SimpleDateFormat strFormat = new SimpleDateFormat(MinuteTimestamp);
        Date date = null ;
        if(time == 0){
            time = new Date().getTime();
        }
        String d = strFormat.format(time);
        try {
            date=strFormat.parse(d);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return date.getTime();
    }

    public static  String getUri(String urlString) throws MalformedURLException {
        if(urlString.endsWith("/")){
            urlString = urlString.substring(0,urlString.length()-1);
        }
        URL url = new URL(urlString);
        return url.getPath();
    }

    public static String getShortClassName(Class cls){
        String longName = cls.getName();
        return getShortClassName(longName);
    }
    public static String getShortClassName(String longName){
        String[] clsname = longName.split("\\.");
        return clsname[clsname.length-1];
    }

    /**
     * 根据 参数C，返回对应类型的 in对象，
     *
     * @param c
     * @param in
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Object converter(Class c, String in) {
        if(in.equals("null")){
            return null;
        }
        if (c == String.class) {
            return in;
        }
        if (c == int.class) {
            int index = in.lastIndexOf('.');
            if (index != -1)
                return Integer.parseInt(in.substring(0, index));
            return Integer.parseInt(in);
        }
        if (c == long.class) {
            return Long.parseLong(in);
        }
        if (c == double.class) {
            return Double.parseDouble(in);
        }
        if (c == float.class) {
            return Float.parseFloat(in);
        }
        if (c == Boolean.class) {
            return Boolean.parseBoolean(in);
        }
        if (c == boolean.class) {
            return Boolean.parseBoolean(in);
        }
        if (c == int[].class) {
            if(in.equals("null")) {
                return null;
            } else {
                return StringArrayToIntArray(in.split(",", -1));
            }
        }

        if (c == String[].class) {
            if(in.equals("null")) {
                return null;
            } else {
                return in.split(",", -1);
            }
        }
        return null;
    }

    public  static int [] StringArrayToIntArray(String[] src){
        int len = src.length;
        System.out.print(src);

        int [] result = new int[len];
        for (int i=0;i<len;i++){
            result[i] = Integer.valueOf(src[i]);
        }
        System.out.print(result);
        return result;
    }

    public static ArrayList<String> JSONStringToArrayList(String JSONString) {
        ArrayList<String> list = new ArrayList<String>();
        JSONString = JSONString.substring(1, JSONString.length() - 1);
        JSONString = JSONString.replace("\"", "");
        String[] list1 = JSONString.split(",");
        for (int i = 0; i < list1.length; i++) {
            list.add(list1[i]);
        }
        return  list;
    }



    public static void main(String []  args) throws MalformedURLException {

        System.out.print(""+getMinuteTimestamp(0));
    }
}
