package top.as.youdian.entity;

import java.io.Serializable;

/**
 * (UserLoad)实体类
 *
 * @author makejava
 * @since 2021-10-28 19:14:34
 */
public class UserLoad implements Serializable {
    private static final long serialVersionUID = 438012779168614555L;
    /**
    * 设备id
    */
    private String devId;
    /**
    * 用户id
    */
    private String uId;
    /**
    * 设备类型
    */
    private Object tag;
    /**
    * token
    */
    private String token;
    /**
    * ip
    */
    private String ip;


    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}