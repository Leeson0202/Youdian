package top.as.youdian.entity.userWord;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserWordSimple {
    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;
    /**
     * 分类标记
     */
    @ApiModelProperty("分类标记：0：陌生单词，1：无需再背 3：三次全错 4：手动收藏 ")
    private Integer tag;
    /**
     * 单词id
     */
    @ApiModelProperty("单词id")
    @JsonProperty("wId")
    private String wId;
}
