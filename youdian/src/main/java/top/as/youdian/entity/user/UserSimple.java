package top.as.youdian.entity.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("User 用户实体类")
public class UserSimple {
    /**
     *
     * 用户id
     */
    @ApiModelProperty(name="uId",value = "uId",notes = "用户id")
    @JsonProperty("uId")
    private String uId;
    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String name;
    /**
     * 头像url
     */
    @ApiModelProperty("头像链接")
    @JsonProperty("hUrl")
    private String hUrl;


}
