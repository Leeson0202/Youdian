package top.as.youdian.entity.word;

import lombok.Data;

import java.io.Serializable;

/**
 * (WordSimilar)实体类
 *
 * @author makejava
 * @since 2021-10-24 12:44:34
 */
@Data
public class WordSimilar implements Serializable {
    private String tag;
    private String tagList;


}