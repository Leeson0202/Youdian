package top.as.youdian.exception;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MyException extends RuntimeException {
    private Integer state = 401;
    private String message = "参数异常";
    private Map<Integer, String> error = new HashMap<>();


    public MyException(Integer state) {
        super();
        this.state = state;
        this.message = getErrorMsg(state);
    }

    public MyException(Integer state, String message) {
        super();
        this.state = state;
        this.message = message;
    }

    public String getErrorMsg(Integer state) {
        switch (state) {
            case 100:
                return "请求者应当继续提出请求";
            case 101:
                return "未登录";
            case 200:
                return "请求成功并返回";
            case 400:
                return "请求参数出现空值";
            case 401:
                return "请求参数异常";
            case 402:
                return "验证码错误";
            case 410:
                return "账户不存在";
            case 411:
                return "密码错误";
            case 412:
                return "账户已经存在";
            case 420:
                return "tooken非法";
            case 421:
                return "tooken失效";
            case 422:
                return "tooken过期";
            default:
                return "服务器异常";
        }
    }


}