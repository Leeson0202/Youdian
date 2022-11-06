package top.as.youdian.entity.userWord;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.as.youdian.tools.DateToLongSerializer;

import java.util.Date;

/**
 * (UserWord)实体类
 *
 * @author makejava
 * @since 2021-10-31 17:58:43
 */
@Data
@ApiModel("UserWordDetail 用户的单词库实体类")
public class UserWord extends UserWordDetail {
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    @JsonProperty("uId")
    private String uId;
    @ApiModelProperty("创建时间")
    @JsonProperty("cDate")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date cDate;

}