package top.as.youdian.entity.userWord;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.as.youdian.entity.word.Word;

@Data
public class UserWordDetail extends UserWordSimple {
    /**
     * 是否复习
     */
    @ApiModelProperty("复习次数")
    @JsonProperty("timesReview")
    private Integer timesReview;
    /**
     * 难度c_fav
     */
    @ApiModelProperty("难度")
    private Integer difficult;
    // 单词详情
    @ApiModelProperty("单词详情")
    private Word word;
}
