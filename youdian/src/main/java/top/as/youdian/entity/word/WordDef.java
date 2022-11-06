package top.as.youdian.entity.word;

import lombok.Data;

import java.io.Serializable;

/**
 * (WordDef)实体类
 *
 * @author makejava
 * @since 2021-10-23 17:17:43
 */
@Data
public class WordDef implements Serializable {
    private String tag;
    private String tagList;

}