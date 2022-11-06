package top.as.youdian.entity.word;

import lombok.Data;

import java.io.Serializable;

/**
 * (WordAudio)实体类
 *
 * @author makejava
 * @since 2021-10-23 17:17:01
 */
@Data
public class WordAudio implements Serializable {
    private String tag;
    private String tagDetail;
    private String url;


}