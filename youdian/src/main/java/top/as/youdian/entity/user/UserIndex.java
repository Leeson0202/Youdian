package top.as.youdian.entity.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserIndex extends UserSimple{

    /**
     * 描述
     */
    @ApiModelProperty("个人描述")
    private String desc;
    /**
     * 背景url
     */
    @ApiModelProperty("背景链接")
    @JsonProperty("bUrl")
    private String bUrl;
}
