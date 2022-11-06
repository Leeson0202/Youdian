package top.as.youdian.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class MyExceptionHandler {
    /**
     * 全局异常处理 返回json要加@ResponseBody
     *
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Map<String, Object> exceptionhander(Exception e) {
        Map<String, Object> map = new HashMap<>();
        System.out.println(new Date().toLocaleString() + "  ---  "+ e.getMessage()+"\n");
        map.put("state", 500);
        map.put("message", "服务器异常");
        return map;
    }

    /**
     * 全局异常处理 返回json要加@ResponseBody
     *
     * @return
     */
    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public Map<String, Object> paramsexceptionhander(MyException p) {
        Map<String, Object> map = new HashMap<>();
        map.put("state", p.getState());
        map.put("message", p.getMessage());
        System.out.println(new Date().toLocaleString() + "  ---  "+p.getState().toString() +"  "+ p.getMessage()+"\n");

        return map;
    }
}
