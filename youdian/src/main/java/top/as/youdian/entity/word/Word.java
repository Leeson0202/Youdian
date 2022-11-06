package top.as.youdian.entity.word;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * (Word)实体类
 *
 * @author makejava
 * @since 2021-10-20 21:31:59
 */
@Data
@ApiModel(value = "Word 单词实体类")
public class Word implements Serializable {
    private static final long serialVersionUID = -86132582198190788L;
    @ApiModelProperty("单词唯一id")
    @JsonProperty("wId")
    private String wId;
    @ApiModelProperty("拼写")
    private String spell;
    @ApiModelProperty("标签（高中ETC4ETC6）")
    private String tag;
    @ApiModelProperty("爬虫链接")
    private String href;
    @ApiModelProperty("音频")
    private List<WordAudio> audio;
    @ApiModelProperty("词性和翻译")
    private List<WordClear> clearfix;
    @ApiModelProperty("单词变型")
    private List<WordDef> deformation;
    @ApiModelProperty("短语")
    private List<WordPhrase> phrase;
    @ApiModelProperty("句子翻译")
    private List<WordSentences> sentences;
    @ApiModelProperty("相似次")
    private List<WordSimilar> similars;


// spell tag  clearfix href  word_deformation   phrase      sentences          audio                similar
// 拼写  标签  词性翻译  链接      变形              短语         例句列表           单词发音                近义词
// str   str   list   str    dict-list        dict->list   dict ->list         list                  list
//                        [{tag  tagList}]  [{key  value}] [{key  value}]  [{tag tagDetail url}] [{tag tagList}]


}