package top.as.youdian.entity.word;

import lombok.Data;

import java.io.Serializable;

/**
 * (WordPhrase)实体类
 *
 * @author makejava
 * @since 2021-10-23 17:18:00
 */
@Data
public class WordPhrase implements Serializable {
    private String key;
    private String value;


}