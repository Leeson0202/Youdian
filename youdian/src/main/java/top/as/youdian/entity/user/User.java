package top.as.youdian.entity.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import top.as.youdian.tools.DateToLongSerializer;

import java.util.Date;

/**
 * (User)实体类
 *
 * @author makejava
 * @since 2021-10-27 23:10:51
 */
@Data
public class User extends UserDetail {

    /**
     * 密码
     */
    private String pwd;
    /**
     * 微信
     */
    private String wx;
    /**
     * 创建时间
     */
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date cDate;


    // 陌生词库
    //朋友圈
    // 关注
    // 粉丝

    //朋友圈
//    private List<Circle> circle;
    // 关注


    // 粉丝


}