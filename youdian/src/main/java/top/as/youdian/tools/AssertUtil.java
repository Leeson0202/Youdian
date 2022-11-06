package top.as.youdian.tools;


import top.as.youdian.exception.MyException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssertUtil {

    private static String rootPath = "E:/youdian/static/";
//    private static String rootPath = "/home/leeson/music/static/";

    // 获取文件目录
    public static String getRootPath() {
        File targetFile = new File(rootPath);
        if (!targetFile.exists())
            return "/home/youdian/static/";
        else
            return rootPath;
    }

    public static Boolean isTrue(Boolean flag, Integer state) {
        if (flag)
            throw new MyException(state);
        else return false;
    }

    public static Boolean isTrue(Boolean flag, Integer state, String message) {
        if (flag)
            throw new MyException(state, message);
        else return false;
    }

    public static Boolean strIsEmpty(String[] msg) {
        for (String s : msg) {
            if (s == null || s.trim().isEmpty()) {
                throw new MyException(400);
            }
        }
        return false;
    }

    public static Boolean strIsEmpty(String[] msg, Integer state) {
        for (String s : msg) {
            if (s == null || s.trim().isEmpty()) {
                throw new MyException(state);
            }
        }
        return false;
    }

    public static boolean strIsEmpty(String s) {
        if (s == null || s.trim().isEmpty())
            throw new MyException(400);
        else return false;
    }

    public static Boolean strIsEmpty(String s, Integer state) {
        if (s == null || s.trim().isEmpty())
            throw new MyException(state);
        else return false;
    }

    public static Boolean strEqualsStr(String msg1, String msg2) {
        if (msg1 != null && msg2 != null && !msg1.trim().isEmpty() && msg1.equals(msg2))
            return true;
        else return false;
    }

    public static Map<String, Object> returnMap(Integer state) {
        return returnMap(state, null, null);
    }

    public static Map<String, Object> returnMap(Integer state, String message) {
        return returnMap(state, message, null);
    }

    public static Map<String, Object> returnMap(Integer state, String message, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("state", state);
        map.put("message", message);
        if (data != null) {
            map.put("data", data);
        }
        return map;
    }

}
