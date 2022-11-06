package top.as.youdian.entity.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.as.youdian.tools.DateToLongSerializer;

import java.util.Date;

@Data
public class UserDetail extends UserIndex {

    //电话
    @ApiModelProperty("电话")
    private String tel;
    /**
     * 年级
     */
    @ApiModelProperty("年级")
    private String grade;
    /**
     * 生日
     */
    @ApiModelProperty("生日")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date birth;
    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

}
